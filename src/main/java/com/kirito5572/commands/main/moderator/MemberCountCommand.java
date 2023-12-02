package com.kirito5572.commands.main.moderator;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MemberCountCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        Member member = event.getMember();
        if(!selfMember.hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("봇이 채널을 만들 권한이 없습니다.").setEphemeral(true).queue();
            return;
        }
        assert member != null;
        if(!member.hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("이 명령어를 사용할 권한이 없습니다!").setEphemeral(true).queue();
            return;
        }
        event.reply("아래 버튼을 눌러주세요.")
                .addActionRow(
                        Button.primary("MemberCountStart", "시작"),
                        Button.danger("MemberCountStop", "종료"),
                        Button.success("MemberCountRefresh","새로고침")
                ).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "유저카운터 기능입니다";
    }

    @Override
    public String getInvoke() {
        return "유저카운터";
    }
}
