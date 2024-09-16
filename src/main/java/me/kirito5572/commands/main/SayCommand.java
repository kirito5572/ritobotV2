package me.kirito5572.commands.main;

import me.kirito5572.objects.logger.LoggerPackage;
import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SayCommand implements ICommand {
    private final LoggerPackage loggerPackage;

    public SayCommand(LoggerPackage loggerPackage) {
        this.loggerPackage = loggerPackage;
    }
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("봇에 권한이 없어 명령어를 사용할수 없습니다. \n" +
                    "필요한 권한: 메세지 관리").queue();

            return;
        }
        Member member = event.getMember();
        if(member == null) {
            return;
        }
        if(!member.getId().equals("284508374924787713")) {
            if (!member.hasPermission(Permission.MANAGE_ROLES)) {
                event.reply("당신은 이 명령어를 사용 할 수 없습니다.").setEphemeral(true).queue();
                return;
            }
        }
        String chat = event.getOption("말", OptionMapping::getAsString);
        if(chat == null) {
            return;
        }
        event.getChannel().sendMessage(chat).queue();
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle("말 커맨드 사용 로그")
                .setColor(Color.GREEN)
                .addField("내용", chat, false)
                .addField("사용자", member.getUser().getAsMention(), false);
        loggerPackage.sayLoggingSend(builder, event.getGuild());
        event.reply("처리완료").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
    }

    @Override
    public String getHelp() {
        return "봇이 대신 말해줍니다~";
    }

    @Override
    public String getInvoke() {
        return "말";
    }
}
