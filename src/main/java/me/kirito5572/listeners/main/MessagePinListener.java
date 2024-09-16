package me.kirito5572.listeners.main;

import me.kirito5572.objects.logger.AWSConnector;
import me.kirito5572.objects.main.MySqlConnector;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessagePinListener extends ListenerAdapter {
    private final MySqlConnector mySqlConnector;

    private static final Logger logger = LoggerFactory.getLogger(MessagePinListener.class);
    private final AWSConnector awsConnector;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("메세지 핀 기능부 준비 완료");
    }

    public MessagePinListener(MySqlConnector mySqlConnector, AWSConnector awsConnector) {
        this.mySqlConnector = mySqlConnector;
        this.awsConnector = awsConnector;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) {
            return;
        }
        if (!event.getAuthor().getId().equals(event.getGuild().getSelfMember().getId())) {
            if(event.isWebhookMessage()) {
                return;
            }
            try (ResultSet resultSet = this.mySqlConnector.Select_Query("SELECT * FROM ritobotv2_general.pin WHERE channelId=?", new int[]{this.mySqlConnector.STRING}, new String[]{event.getChannel().getId()})) {
                if(resultSet.next()) {
                    try {
                        event.getChannel().retrieveMessageById(resultSet.getString("messageId")).queue((message -> {
                            MessageEmbed embed = message.getEmbeds().getFirst();
                            File file = this.awsConnector.S3DownloadObject(event.getChannel().getId() + "channel");
                            message.delete().queue();
                            if(file == null) {
                                message.getChannel().sendMessageEmbeds(embed).queue(message1 -> {
                                    try {
                                        this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.pin SET messageId =? WHERE channelId =?",
                                                new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                                                new String[]{message1.getId(), message.getChannelId()});
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } else {
                                message.getChannel().sendMessageEmbeds(embed).addFiles(FileUpload.fromData(file)).queue(message1 -> {
                                    try {
                                        this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.pin SET messageId =? WHERE channelId =?",
                                                new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                                                new String[]{message1.getId(), message.getChannelId()});
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        }));
                    } catch (ErrorResponseException ignored) {
                        this.mySqlConnector.Insert_Query("DELETE FROM ritobotv2_general.pin WHERE channelId =?",
                                new int[]{this.mySqlConnector.STRING},
                                new String[]{event.getChannel().getId()});
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
