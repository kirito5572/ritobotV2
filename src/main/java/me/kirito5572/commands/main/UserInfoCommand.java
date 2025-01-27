package me.kirito5572.commands.main;

import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user;
        Member member;
        Member targetMember = event.getOption("유저", OptionMapping::getAsMember);
        if(targetMember == null) {
            user = Objects.requireNonNull(event.getMember()).getUser();
            member = event.getMember();
        } else {
            user = targetMember.getUser();
            member = targetMember;
        }
        StringBuilder serverRole = new StringBuilder();
        List<Role> role = member.getRoles();
        for (Role value : role) {
            serverRole.append(value.getAsMention()).append("\n");
        }
        MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setColor(member.getColor())
                .setThumbnail(member.getEffectiveAvatarUrl())
                .addField("유저이름#번호", String.format("%#s", user), false)
                .addField("서버 표시 이름", member.getEffectiveName(), false)
                .addField("유저 ID + 언급 멘션", String.format("%s (%s)", user.getId(), member.getAsMention()), false)
                .addField("디스코드 가입 일자", user.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 초대 일자", member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 부여 역할", serverRole.toString(), false)
                .addField("온라인 상태", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false)
                .addField("봇 여부", user.isBot() ? "예" : "아니요", false)
                .addField("검색 된 서버", member.getGuild().getName(), false)
                .build();

        event.replyEmbeds(embed).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "유저 정보를 출력합니다. 검색 대상 미입력시 본인의 정보를 출력합니다";
    }

    @Override
    public String getInvoke() {
        return "정보";
    }
}
