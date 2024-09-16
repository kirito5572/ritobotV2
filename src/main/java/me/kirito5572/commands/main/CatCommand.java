package me.kirito5572.commands.main;

import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CatCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        int num;
        OptionMapping opt = event.getOption("수량");
        num = opt == null ? 1 : opt.getAsInt();
        if(num > 10) {
            event.getChannel().sendMessage("최대 전송 가능량은 10개입니다.").queue();

            return;
        }

        if(!selfMember.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            event.getChannel().sendMessage("봇이 링크 메세지를 보낼 권한이 없습니다.").queue();

            return;
        }
        for(int i = 0; i < num; i++) {
            WebUtils.ins.scrapeWebPage("https://nekos.life/").async((document) -> {
                String a = Objects.requireNonNull(document.getElementsByTag("head").first()).toString();
                int b = a.indexOf("meta property=\"og:url\" content=\"");
                int c = a.indexOf("<meta property=\"og:image\" content=\"");
                a = a.substring(b + 32, c - 5);
                EmbedBuilder embed = EmbedUtils.embedImage(a);
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();

            });
        }
    }

    @NotNull
    public String getHelp() {
        return "랜덤 씹덕네코 생성기";
    }

    @NotNull
    public String getInvoke() {
        return "네코";
    }

}