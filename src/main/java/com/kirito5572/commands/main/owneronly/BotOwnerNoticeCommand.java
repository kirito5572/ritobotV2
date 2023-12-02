package com.kirito5572.commands.main.owneronly;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BotOwnerNoticeCommand implements ICommand {
    //TODO 나중에 활성화 시키기
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getMember()).getId().equals("284508374924787713")) {
            event.reply("해당 명령어를 사용할수 없습니다.").setEphemeral(true).queue();
            return;
        }
        List<Guild> guildList = event.getJDA().getGuilds();

    }

    @Override
    public String getHelp() {
        return "봇 제작자가 공지를 목적으로 사용하는 명령어입니다.";
    }

    @Override
    public String getInvoke() {
        return "공지";
    }
}
