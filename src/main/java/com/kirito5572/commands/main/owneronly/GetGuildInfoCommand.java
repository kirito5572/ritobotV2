package com.kirito5572.commands.main.owneronly;

import com.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GetGuildInfoCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getMember()).getId().equals("284508374924787713")) {
            event.reply("해당 명령어를 사용할수 없습니다.").setEphemeral(true).queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Guild guild : event.getJDA().getGuilds()) {
            try {
                guild.retrieveInvites().queue(invites ->
                        stringBuilder.append(guild.getName()).append("(").append(invites.get(0)).append(")").append("\n")
                );

            } catch (Exception e) {
                e.printStackTrace();
                stringBuilder.append("\n");
            }
        }
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle("접속된 서버 정보")
                .setDescription(stringBuilder.toString());

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "관리 목적 명령어";
    }

    @Override
    public String getInvoke() {
        return "서버리스트";
    }
}
