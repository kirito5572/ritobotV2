package me.kirito5572.commands.main.game;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class WordGameCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.reply("개발중입니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queue());
    }

    @Override
    public String getHelp() {
        return "개발중";
    }

    @Override
    public String getInvoke() {
        return "끝말잇기";
    }
}
