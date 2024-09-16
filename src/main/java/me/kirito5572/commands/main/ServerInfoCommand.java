package me.kirito5572.commands.main;

import me.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ServerInfoCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if(guild == null) {
            return;
        }
        String textChannel = String.valueOf(guild.getTextChannels().size());
        String voiceChannel = String.valueOf(guild.getVoiceChannels().size());
        String forumChannel = String.valueOf(guild.getForumChannels().size());
        String mediaChannel = String.valueOf(guild.getMediaChannels().size());
        String newsChannel = String.valueOf(guild.getNewsChannels().size());
        String stageChannel = String.valueOf(guild.getStageChannels().size());
        String threadChannel = String.valueOf(guild.getThreadChannels().size());
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle(guild.getName() + " 서버의 정보")
                .addField("서버 이름", guild.getName(), false)
                .addField("서버 소유자", guild.retrieveOwner().complete().getAsMention(), false)
                .addField("서버 생성일", guild.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 인원수", String.valueOf(guild.getMembers().size()), false)
                .addField("각 채널수(채팅/보이스/포럼/미디어/뉴스/스테이지/쓰레드)",
                        String.format("%s / %s / %s / %s / %s / %s / %s", textChannel, voiceChannel, forumChannel, mediaChannel, newsChannel, stageChannel, threadChannel), false)
                .addField("서버 역할수", String.valueOf(guild.getRoles().size()), false)
                .addField("서버 부스터 수", String.valueOf(guild.getBoostCount()), false)
                .addField("서버 이모지 수", String.valueOf(guild.getEmojis().size()), false)
                .addField("서버 스티커 수", String.valueOf(guild.getStickers().size()), false)
                .addField("서버 보안 단계", event.getGuild().getRequiredMFALevel().name(), false);

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "현재 있는 서버의 정보를 불러옵니다.";
    }

    @Override
    public String getInvoke() {
        return "서버정보";
    }
}
