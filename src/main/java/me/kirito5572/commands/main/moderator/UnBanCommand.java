package me.kirito5572.commands.main.moderator;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UnBanCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if(member == null) {
            return;
        }
        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("이 명령어를 사용할 권한이 없습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }

        if (!Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("봇이 이 명령어를 사용할 권한이 없습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }

        User targetUser = event.getOption("유저", OptionMapping::getAsUser);

        if (targetUser == null) {
            event.reply("해당 유저는 존재하지 않습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }


        event.getGuild().retrieveBanList().queue((bans) -> {

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, targetUser.getName()))
                    .map(Guild.Ban::getUser).toList();

            if (goodUsers.isEmpty()) {
                event.reply("해당 유저는 밴 되지 않았습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                return;
            }

            String mod = String.format("%#s", event.getMember().getUser());

            event.getGuild().unban(targetUser)
                    .reason("언밴한 유저: " + mod).queue();

            event.reply("유저 " + targetUser + " 가 언밴되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));

        });
    }

    @Override
    public String getHelp() {
        return "밴한 사용자를 취소합니다.";
    }

    @Override
    public String getInvoke() {
        return "언밴";
    }
    private boolean isCorrectUser(@NotNull Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();

        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg)
                || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }
}
