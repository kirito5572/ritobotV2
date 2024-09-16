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
        Guild guildA = null;
        User targetUser = event.getOption("유저", OptionMapping::getAsUser);
        Member targetMember = event.getOption("유저", OptionMapping::getAsMember);
        if(targetUser == null) {
            user = Objects.requireNonNull(event.getMember()).getUser();
            member = event.getMember();
            guildA = event.getGuild();
        } else {
            try {
                boolean bypass = false;
                Member foundMember = null;
                List<Guild> guilds = event.getJDA().getGuilds();
                for (Guild guild : guilds) {
                    if(!bypass) {
                        if(guild.getMembers().contains(targetMember)) {
                            bypass = true;
                            guildA = guild;
                            foundMember = targetMember;
                        }
                    }
                }
                if(foundMember == null) {
                    event.getChannel().sendMessage("'" + targetUser + "' 라는 유저는 없습니다.").queue();
                    return;
                }

                user = foundMember.getUser();
                member = foundMember;

            } catch (Exception e) {
                event.getChannel().sendMessage("해당 유저를 봇이 찾을수 없거나, 인수가 잘못 입력되었습니다.").queue();

                return;
            }
        }
        StringBuilder serverRole = new StringBuilder();
        if(Objects.requireNonNull(event.getGuild()).getId().equals(Objects.requireNonNull(guildA).getId())) {
            List<Role> role = member.getRoles();
            for (Role value : role) {
                serverRole.append(value.getAsMention()).append("\n");
            }
        } else {
            List<Role> role = member.getRoles();
            for (Role value : role) {
                serverRole.append(value.getName()).append("\n");
            }
        }
        MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setColor(member.getColor())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("유저이름#번호", String.format("%#s", user), false)
                .addField("서버 표시 이름", member.getEffectiveName(), false)
                .addField("유저 ID + 언급 멘션", String.format("%s (%s)", user.getId(), member.getAsMention()), false)
                .addField("디스코드 가입 일자", user.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 초대 일자", member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 부여 역할", serverRole.toString(), false)
                .addField("온라인 상태", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false)
                .addField("봇 여부", user.isBot() ? "예" : "아니요", false)
                .addField("검색 된 서버", guildA.getName(), false)
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
