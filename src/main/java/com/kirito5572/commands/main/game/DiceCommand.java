package com.kirito5572.commands.main.game;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DiceCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int sides;
        int dices;

        OptionMapping opt = event.getOption("주사위면수");
        OptionMapping opt2 = event.getOption("주사위횟수");
        sides = opt == null ? 6 : opt.getAsInt();
        dices = opt2 == null ? 6 : opt2.getAsInt();

        if (sides < 1 | sides > 100) {
            event.reply("주사위의 면수가 최소 2 이상, 최대 100 이하 여야 합니다").setEphemeral(true).queue();

            return;
        }

        if (dices < 0 | dices > 20) {
            event.reply("적어도 1회 이상, 많으면 20회 이하로 주사위를 던저야 합니다").setEphemeral(true).queue();

            return;
        }


        Random random = new Random();
        StringBuilder builder = new StringBuilder()
                .append("결과:\n");

        for (int d = 1; d <= dices; d++) {
            builder.append("\uD83C\uDFB2 #")
                    .append(d)
                    .append(": **")
                    .append((random.nextInt(sides * 10) % sides) + 1)
                    .append("**\n");
        }

        event.reply(builder.toString()).setEphemeral(true).queue();
    }

    @NotNull
    @Override
    public String getHelp() {
        return "주사위를 굴립니다";
    }

    @NotNull
    @Override
    public String getInvoke() {
        return "주사위";
    }

}
