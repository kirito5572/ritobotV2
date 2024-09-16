package me.kirito5572.commands.main;

import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NaverCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        WebUtils.ins.scrapeWebPage("https://datalab.naver.com/keyword/realtimeList.naver").async((document) -> {
            String[] data = new String[10];
            String a = Objects.requireNonNull(document.getElementsByTag("body").first()).toString();
            a = a.substring(a.indexOf("<ul class=\"ranking_list\">"));
            a = a.substring(0, a.indexOf("</ul>"));
            for(int i = 1; i < 11; i++) {
                String temp = a.substring(a.indexOf("<span class=\"item_num\">" + i + "</span>"));
                temp = temp.substring(temp.indexOf("<span class=\"item_title\">"));
                temp = temp.substring(25, temp.indexOf("</span>"));
                data[i - 1] = temp;
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("네이버 실시간 검색어");
            for(int i = 0; i < 9; i++) {
                builder.addField("","[0" + (i + 1) + ". " + data[i] + "](" + "https://search.naver.com/search.naver?query=" + URLEncoder.encode(data[i], StandardCharsets.UTF_8) + ")", false);
            }
            builder.addField("","[" + (10) + ". " + data[9] + "](" + "https://search.naver.com/search.naver?query=" + URLEncoder.encode(data[9], StandardCharsets.UTF_8) + ")", false);

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        });
    }

    @Override
    public String getHelp() {
        return "네이버의 현재 실시간 검색어를 받아옵니다.";
    }

    @Override
    public String getInvoke() {
        return "실검";
    }
}
