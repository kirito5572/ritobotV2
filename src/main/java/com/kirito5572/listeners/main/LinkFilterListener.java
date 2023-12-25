package com.kirito5572.listeners.main;

import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.main.LinkConfirm;
import com.sun.security.auth.callback.TextCallbackHandler;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkFilterListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(LinkFilterListener.class);
    private final ConfigPackage configPackage;

    public LinkFilterListener(ConfigPackage configPackage) {
        this.configPackage = configPackage;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("링크 차단 기능부 준비 완료");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) {
            return;
        }
        if(!event.isWebhookMessage()) {
            return;
        }
        if(!event.getAuthor().isBot()) {
            return;
        }
        ConfigPackage.Config_Data data = configPackage.getConfigData(event.getGuild().getId());
        if(data.linkFilterEnable) {
            Member member = event.getMember();
            if(member == null) {
                return;
            }
            if(member.hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_ROLES, Permission.MANAGE_PERMISSIONS,
                    Permission.MANAGE_SERVER, Permission.MANAGE_EVENTS, Permission.MANAGE_THREADS, Permission.MANAGE_WEBHOOKS,
                    Permission.MESSAGE_MANAGE, Permission.ADMINISTRATOR, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)) {
                //TODO 나중에는 이것도 설정 가능하게 만들기 + 로그 채널만 삭제 기능
                return;
            }
            boolean isLinkContain = LinkConfirm.isLink(event.getMessage().getContentRaw());
            if(isLinkContain) {
                event.getMessage().delete().queue();

                EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("링크 차단됨")
                        .addField("전송한 사람", event.getAuthor().getName(), false);

                String channelId = data.linkFilterOutputChannel;
                if(channelId == null || channelId.equals("0")) {
                    return;
                }
                StandardGuildMessageChannel channel = event.getGuild().getChannelById(StandardGuildMessageChannel.class, channelId);
                if(channel == null) {
                    return;
                }
                channel.sendMessageEmbeds(builder.build()).queue();
                //TODO 이후 필터 기능 만들기
            }
        }
    }
}
