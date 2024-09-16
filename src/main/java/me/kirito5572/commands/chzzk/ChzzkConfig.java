package me.kirito5572.commands.chzzk;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChzzkConfig implements ICommand {
    private static final Logger logger = LoggerFactory.getLogger(ChzzkConfig.class);

    private final String[][] optionList = new String[][]{
            {"방송 알림" , "chzzkConfig1" ," 치지직 방송 알림 설정"}
    };
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if(member == null) {
            event.reply("에러가 발생했습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            logger.warn("ChzzkConfig 에러발생");
            return;
        }
        if(!member.hasPermission(Permission.MANAGE_SERVER) || !member.hasPermission(Permission.MANAGE_CHANNEL) ||
                !member.hasPermission(Permission.MANAGE_ROLES) || !member.hasPermission(Permission.MESSAGE_MANAGE) ||
                !member.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("이 명령어를 사용할 권한이 없습니다!").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
            return;
        }

        StringSelectMenu.Builder builder= StringSelectMenu.create("치지직설정");
        for(String[] list : optionList) {
            builder.addOption(list[0], list[1], list[2]);
        }
        event.reply("설정할 기능을 선택해주세요.")
                .addActionRow(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "치지직 전용 설정 명령어입니다.";
    }

    @Override
    public String getInvoke() {
        return "치지직";
    }
}
