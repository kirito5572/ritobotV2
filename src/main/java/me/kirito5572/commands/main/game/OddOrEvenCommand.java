package me.kirito5572.commands.main.game;

import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OddOrEvenCommand implements ICommand {
    private final Random random = new Random();
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        boolean even = random.nextBoolean();
        String message = event.getOption("홀짝", OptionMapping::getAsString);
        String[] result = new String[] {
                "a", "b", "c"
        };
        if(message == null) {
            event.reply("홀/짝이 정상적으로 입력되지 않았습니다.").setEphemeral(true).queue();
            return;
        }
        if(message.equals("홀수")) {
            result[2] = "ok";
            if(even) {
                result[0] = "홀수";
                result[1] = "성공";
            } else {
                result[0] = "짝수";
                result[1] = "실패";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("홀/짝")
                    .addField("결과", result[1], false)
                    .addField("추첨 결과", result[0], false)
                    .setFooter("과도한 도박은 정신건강에 해롭습니다");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        } else if (message.equals("짝수")) {
            result[2] = "ok";
            if(even) {
                result[0] = "홀수";
                result[1] = "실패";
            } else {
                result[0] = "짝수";
                result[1] = "성공";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("홀/짝")
                    .addField("결과", result[1], false)
                    .addField("추첨 결과", result[0], false)
                    .setFooter("과도한 도박은 정신건강에 해롭습니다");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        } else {
            event.reply("입력된 값이 홀수 또는 짝수가 아닙니다!").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        }
    }

    @NotNull
    @Override
    public String getHelp() {
        return "홀짝 게임 입니다.";
    }

    @NotNull
    @Override
    public String getInvoke() {
        return "홀짝";
    }
}
