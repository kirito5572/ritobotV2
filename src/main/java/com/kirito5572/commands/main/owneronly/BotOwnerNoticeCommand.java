package com.kirito5572.commands.main.owneronly;

import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.main.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BotOwnerNoticeCommand implements ICommand {
    private final ConfigPackage configPackage;

    public BotOwnerNoticeCommand(ConfigPackage configPackage) {
        this.configPackage = configPackage;
    }
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getMember()).getId().equals("284508374924787713")) {
            event.reply("해당 명령어를 사용할수 없습니다.").setEphemeral(true).queue();
            return;
        }
        List<Guild> guildList = event.getJDA().getGuilds();
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        OptionMapping opt = event.getOption("footer");
        String footerString = opt == null ? null : opt.getAsString();
        String titleString = event.getOption("title", OptionMapping::getAsString);
        String mainString = event.getOption("main", OptionMapping::getAsString);
        assert titleString != null;
        assert mainString != null;
        builder.setTitle("공지사항").addField(titleString, mainString, false);
        if(footerString != null) {
            builder.setFooter(footerString);
        }
        for(Guild guild : guildList) {
            ConfigPackage.Config_Data configData = configPackage.getConfigData(guild.getId());
            if(!configData.noticeEnable) {
                return;
            }
            GuildChannel channel = guild.getChannelById(GuildChannel.class, configData.noticeChannel);
            GuildMessageChannel sendChannel;
            if(channel==null) {
                return;
            }

            switch (channel.getType()) {
                case ChannelType.TEXT -> sendChannel = guild.getChannelById(TextChannel.class, configData.noticeChannel);
                case ChannelType.NEWS -> sendChannel = guild.getChannelById(NewsChannel.class, configData.noticeChannel);
                case ChannelType.GUILD_NEWS_THREAD, ChannelType.GUILD_PRIVATE_THREAD, ChannelType.GUILD_PUBLIC_THREAD ->
                        sendChannel = guild.getChannelById(ThreadChannel.class, configData.noticeChannel);
                case ChannelType.VOICE -> sendChannel = guild.getChannelById(VoiceChannel.class, configData.noticeChannel);
                case ChannelType.STAGE -> sendChannel = guild.getChannelById(StageChannel.class, configData.noticeChannel);
                default -> {
                    return;
                }
            }
            if(sendChannel != null) {
                if(sendChannel.canTalk()) {
                    sendChannel.sendMessageEmbeds(builder.build()).queue();
                }
            }
        }
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
