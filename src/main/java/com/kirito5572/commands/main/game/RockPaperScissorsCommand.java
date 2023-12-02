package com.kirito5572.commands.main.game;

import com.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RockPaperScissorsCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String message = event.getOption("가위바위보", OptionMapping::getAsString);
        String myMessage;
        boolean botWin = false;
        boolean userWin = false;
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0 -> myMessage = "가위";
            case 1 -> myMessage = "바위";
            case 2 -> myMessage = "보";
            default -> {
                event.reply("에러가 발생했습니다.").setEphemeral(true).queue();
                return;
            }
        }
        if(message == null) {
            event.reply("값이 정상적으로 입력되지 않았습니다.").setEphemeral(true).queue();
            return;
        }
        switch (message) {
            case "가위" -> {
                switch (myMessage) {
                    case "바위" -> botWin = true;
                    case "보" -> userWin = true;
                }
            }
            case "바위" -> {
                switch (myMessage) {
                    case "가위" -> userWin = true;
                    case "보" -> botWin = true;
                }
            }
            case "보" -> {
                switch (myMessage) {
                    case "가위" -> botWin = true;
                    case "바위" -> userWin = true;
                }
            }
            default -> {
                event.reply("가위/바위/보 중 하나를 입력하여 주십시오").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                return;
            }
        }
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        if(botWin) {
            builder.setTitle("패배")
                    .setColor(Color.RED)
                    .addField("플레이어", message, false)
                    .addField("봇", myMessage, false);
        } else if(userWin) {
            builder.setTitle("승리")
                    .setColor(Color.GREEN)
                    .addField("플레이어", message, false)
                    .addField("봇", myMessage, false);
        } else {
            builder.setTitle("무승부")
                    .setColor(Color.GRAY)
                    .addField("플레이어", message, false)
                    .addField("봇", myMessage, false);
        }
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "봇과 가위바위보를 합니다.";
    }

    @Override
    public String getInvoke() {
        return "가위바위보";
    }
}
