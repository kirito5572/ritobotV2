package com.kirito5572.commands.main.game;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class RussianRouletteCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String inputString = event.getOption("종류", OptionMapping::getAsString);
        if(inputString != null) {
            return;
        }
        event.reply("개발중입니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
    }

    @Override
    public String getHelp() {
        return "6발 리볼버로 즐기는 러시안 룰렛";
    }

    @Override
    public String getInvoke() {
        return "러시안룰렛";
    }
}
