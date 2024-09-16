package me.kirito5572.commands.main.moderator;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class KickCommand implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(KickCommand.class);
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("이 명령어를 사용하기 위한 권한이 없습니다").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("봇에게 추방할 권한이 부여되어 있지 않습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }

        User targetUser = event.getOption("유저", OptionMapping::getAsUser);
        Member target = event.getOption("유저", OptionMapping::getAsMember);
        String reason = event.getOption("사유", OptionMapping::getAsString);
        Member member = event.getMember();

        assert target != null;
        assert member != null;
        if (!member.canInteract(target)) {
            event.reply("해당 사용자를 킥할 권한이 없습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }

        if (!selfMember.canInteract(target)) {
            event.reply("봇이 해당 사용자를 밴할 수 없습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }

        if(targetUser == null) {
            event.reply("유저를 제대로 입력해주세요.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        event.deferReply().queue();
        AuditableRestAction<Void> action = event.getGuild().kick(targetUser);
        if (reason != null) {
            action = action.reason(reason);
        }
        action.queue(v -> event.getHook().editOriginal("**" + event.getUser().getName() + "** 에게 **" + targetUser.getName() + "** 가 추방되었습니다!").queue(), error -> {
            // Tell the user we encountered some error
            event.getHook().editOriginal("에러가 발생했습니다. 다시 시도해주세요.").queue();
            logger.warn("에러 발생 KickCommand:60");
        });

    }

    @Override
    public String getHelp() {
        return "이 서버에서 추방합니다.";
    }

    @Override
    public String getInvoke() {
        return "추방";
    }
}
