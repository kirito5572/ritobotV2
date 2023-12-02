package com.kirito5572.commands.main.moderator;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClearCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        MessageChannelUnion channel = event.getChannel();
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        Member member = event.getMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("봇이 메세지를 삭제할 권한이 없습니다.").setEphemeral(true).queue();
            return;
        }
        assert member != null;
        if(!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("이 명령어를 사용할 권한이 없습니다!").setEphemeral(true).queue();
            return;
        }
        OptionMapping opt = event.getOption("수량");
        int deleteCount = opt == null ? 1 : opt.getAsInt();
        if (deleteCount < 1) {
            channel.sendMessage("1보다 큰 숫자를 입력해주세요").queue();
        } else if (deleteCount > 100) {
            channel.sendMessage("100보다 작은 숫자를 입력해주세요").queue();
        }
        channel.getIterableHistory()
                .takeAsync(deleteCount)
                .thenApplyAsync((messages) -> {
                    List<Message> goodMessages = messages.stream()
                            .filter((m) -> m.getTimeCreated().isBefore(
                                    OffsetDateTime.now().plusWeeks(2)
                            ))
                            .collect(Collectors.toList());

                    channel.purgeMessages(goodMessages);

                    return goodMessages.size();
                })
                .whenCompleteAsync((count, thr) -> event.replyFormat("`%d` 개의 채팅 삭제 완료", count).setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)))
                .exceptionally((thr) -> {
                    String cause = "";

                    if (thr.getCause() != null) {
                        cause = " 에러 발생 사유: " + thr.getCause().getMessage();
                    }

                    event.replyFormat("에러: %s%s", thr.getMessage(), cause).setEphemeral(true).queue();

                    return 0;
                });


    }

    @Override
    public String getHelp() {
        return "채팅을 깨끗하게 청소합니다!";
    }

    @Override
    public String getInvoke() {
        return "청소";
    }
}
