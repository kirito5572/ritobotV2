package com.kirito5572.listeners.main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTypeEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MemberCountListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MemberCountListener.class);
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("멤버 카운트 기능부 준비 완료");
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String categoryName = "\uD83D\uDCCB서버 상태\uD83D\uDCCB";
        Category category;
        Guild guild = event.getGuild();
        if(guild == null) {
            event.getMessage().delete().queue();
            event.reply("해당 명령어는 서버에서만 사용할 수 있습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        if(event.getComponentId().equals("MemberCountStart")) {
            try {
                category = guild.getCategoriesByName(categoryName, true).get(0);
                if (category != null) {
                    event.getMessage().delete().queue();
                    event.reply("이미 유저카운팅이 시작되어있습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    return;
                }
            } catch (Exception ignored) {
            }
            guild.createCategory(categoryName)
                    .setPosition(0)
                    .complete();
            category = guild.getCategoriesByName(categoryName, true).get(0);
            String memberCountName = "총 멤버 수";
            category.createVoiceChannel(memberCountName + " : " + guild.getMembers().size()).queue();
            int numOfBot = 0;
            int numOfUser = 0;
            for (int i = 0; i < guild.getMembers().size(); i++) {
                if (guild.getMembers().get(i).getUser().isBot()) {
                    numOfBot++;
                } else {
                    numOfUser++;
                }
            }
            String botCountName = "봇 수";
            category.createVoiceChannel(botCountName + " : " + numOfBot).queue();
            String userCountName = "유저 수";
            category.createVoiceChannel(userCountName + " : " + numOfUser).queue();
            String channelCountName = "채널 수";
            category.createVoiceChannel(channelCountName + " : " + (guild.getChannels().size() - guild.getCategories().size())).queue();
            String roleCountName = "역할 갯수";
            category.createVoiceChannel(roleCountName + " : " + guild.getRoles().size()).queue();
            event.getMessage().delete().queue();
            event.reply("유저 카운팅이 시작되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } else if(event.getComponentId().equals("MemberCountStop")) {
            try {
                category = guild.getCategoriesByName(categoryName, true).get(0);
                for (int i = 0; i < category.getChannels().size(); i++) {
                    category.getChannels().get(i).delete().queue();
                }
                category.delete().queue();
                event.getMessage().delete().queue();
                event.reply("유저 카운팅이 종료되었습니다..").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            } catch (Exception e) {
                event.getMessage().delete().queue();
                event.reply("유저 카운팅을 하고 있지 않습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            }
        } else if(event.getComponentId().equals("MemberCountRefresh")) {
            try {
                count(guild);
            } catch (Exception ignored) {
                event.getMessage().delete().queue();
                event.reply("유저 카운팅이 실행중이지 않습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                return;
            }
            event.getMessage().delete().queue();
            event.reply("새로고침이 완료되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onChannelUpdateType(@NotNull ChannelUpdateTypeEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        try {
            count(event.getGuild());
        } catch (Exception ignored) {
        }
    }

    private void count(@NotNull Guild guild) {
        String categoryName = "\ud83d\udccb:서버 상태";
        String categoryName1 = "\ud83d\udccb서버 상태\ud83d\udccb";

        Category category;
        try {
            category = guild.getCategoriesByName(categoryName, true).get(0);
        } catch (Exception var12) {
            category = guild.getCategoriesByName(categoryName1, true).get(0);
        }

        int numOfBot = 0;
        int numOfUser = 0;

        for(int i = 0; i < guild.getMembers().size(); ++i) {
            if (guild.getMembers().get(i).getUser().isBot()) {
                ++numOfBot;
            } else {
                ++numOfUser;
            }
        }

        String botCountName;
        String userCountName;
        String channelCountName;
        String roleCountName;
        String memberCountName;
        if (guild.getId().equals("609985979167670272")) {
            memberCountName = "\ud83c\udf3f총 유저 수";
            if (!category.getChannels().get(0).getName().equals(memberCountName + " : " + guild.getMembers().size())) {
                category.getChannels().get(0).getManager().setName(memberCountName + " : " + guild.getMembers().size()).queue();
            }

            botCountName = "\ud83c\udf3f봇 수";
            if (!category.getChannels().get(1).getName().equals(botCountName + " : " + numOfBot)) {
                category.getChannels().get(1).getManager().setName(botCountName + " : " + numOfBot).queue();
            }

            userCountName = "\ud83c\udf3f유저 수";
            if (!category.getChannels().get(2).getName().equals(userCountName + " : " + numOfUser)) {
                category.getChannels().get(2).getManager().setName(userCountName + " : " + numOfUser).queue();
            }

            channelCountName = "\ud83c\udf3f채널 수";
            if (!category.getChannels().get(3).getName().equals(channelCountName + " : " + (guild.getChannels().size() - guild.getCategories().size()))) {
                category.getChannels().get(3).getManager().setName(channelCountName + " : " + (guild.getChannels().size() - guild.getCategories().size())).queue();
            }

            roleCountName = "\ud83c\udf3f역할 갯수";
            if (!category.getChannels().get(4).getName().equals(roleCountName + " : " + guild.getRoles().size())) {
                category.getChannels().get(4).getManager().setName(roleCountName + " : " + guild.getRoles().size()).queue();
            }
        } else {
            memberCountName = "총 유저 수";
            if (!category.getChannels().get(0).getName().equals(memberCountName + " : " + guild.getMembers().size())) {
                category.getChannels().get(0).getManager().setName(memberCountName + " : " + guild.getMembers().size()).queue();
            }

            botCountName = "봇 수";
            if (!category.getChannels().get(1).getName().equals(botCountName + " : " + numOfBot)) {
                category.getChannels().get(1).getManager().setName(botCountName + " : " + numOfBot).queue();
            }

            userCountName = "유저 수";
            if (!category.getChannels().get(2).getName().equals(userCountName + " : " + numOfUser)) {
                category.getChannels().get(2).getManager().setName(userCountName + " : " + numOfUser).queue();
            }

            channelCountName = "채널 수";
            if (!category.getChannels().get(3).getName().equals(channelCountName + " : " + (guild.getChannels().size() - guild.getCategories().size()))) {
                category.getChannels().get(3).getManager().setName(channelCountName + " : " + (guild.getChannels().size() - guild.getCategories().size())).queue();
            }

            roleCountName = "역할 갯수";
            if (!category.getChannels().get(4).getName().equals(roleCountName + " : " + guild.getRoles().size())) {
                category.getChannels().get(4).getManager().setName(roleCountName + " : " + guild.getRoles().size()).queue();
            }
        }
    }
}
