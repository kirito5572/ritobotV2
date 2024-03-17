package com.kirito5572.commands.main.moderator;

import com.kirito5572.objects.logger.AWSConnector;
import com.kirito5572.objects.main.ICommand;
import com.kirito5572.objects.main.MySqlConnector;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessagePinCommand implements ICommand {
    private final AWSConnector awsConnector;
    private final MySqlConnector mySqlConnector;
    private final Logger logger = LoggerFactory.getLogger(MessagePinCommand.class);

    public MessagePinCommand(MySqlConnector mySqlConnector, AWSConnector awsConnector) {
        this.mySqlConnector = mySqlConnector;
        this.awsConnector = awsConnector;
    }
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("이 명령어를 사용하기 위한 권한이 없습니다").setEphemeral(true).queue();
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("봇이 이 명령어를 처리할 권한이 없습니다.").setEphemeral(true).queue();
            return;
        }
        String channelId = event.getChannel().getId();
        try (ResultSet resultSet = this.mySqlConnector.Select_Query("SELECT * FROM ritobotv2_general.pin WHERE channelId=?", new int[]{this.mySqlConnector.STRING}, new String[]{channelId})){
            if(resultSet.next()) {
                event.getChannel().deleteMessageById(resultSet.getString("messageId")).queue();
                this.mySqlConnector.Insert_Query("DELETE FROM ritobotv2_general.pin WHERE channelId=?",new int[]{this.mySqlConnector.STRING},new String[]{channelId});
                event.reply("핀이 해제되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            } else {
                String content = event.getOption("내용", OptionMapping::getAsString);
                Message.Attachment contentAttachment = event.getOption("파일", OptionMapping::getAsAttachment);
                File file = null;
                if(contentAttachment != null){
                    if (contentAttachment.isImage()) {
                        String extension = contentAttachment.getFileExtension();
                        if(extension != null) {
                            file = new File(channelId + "." + extension);
                        } else {
                            file = new File(channelId + ".tmp");
                        }
                        file = contentAttachment.getProxy().downloadToFile(file).join();
                        this.awsConnector.S3UploadObject(file, channelId + "channel");
                    }
                }
                if(content == null) {
                    event.reply("메세지 내용이 없습니다! 내용을 입력해주세요").setEphemeral(true).queue(e -> e.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    return;
                }
                EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                        .setTitle("고정된 메세지")
                        .setColor(Color.GREEN)
                        .setDescription(content);
                if(file == null) {
                    event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> {
                        try {
                            this.mySqlConnector.Insert_Query("INSERT INTO ritobotv2_general.pin VALUES (?, ?);",
                                    new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                                    new String[]{message.getChannelId(), message.getId()});
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    event.getChannel().sendMessageEmbeds(builder.build()).addFiles(FileUpload.fromData(file)).queue(message -> {
                        try {
                            this.mySqlConnector.Insert_Query("INSERT INTO ritobotv2_general.pin VALUES (?, ?);",
                                    new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                                    new String[]{message.getChannelId(), message.getId()});
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    if (!file.delete()) {
                        logger.warn("파일이 삭제되지 않음");
                    }
                }
                event.reply("고정 처리가 완료되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));

            }
        } catch (SQLException ignored) {
            event.reply("에러가 발생했습니다.").setEphemeral(true).queue();
        }
    }

    @Override
    public String getHelp() {
        return "채널에서 특정 메세지를 고정합니다. (고정된 메세지와 다른 기능입니다)";
    }

    @Override
    public String getInvoke() {
        return "핀";
    }
}
