package com.kirito5572.listeners.logger;

import com.kirito5572.objects.logger.AWSConnector;
import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.logger.LoggerPackage;
import com.kirito5572.objects.main.MySqlConnector;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.sticker.Sticker;
import net.dv8tion.jda.api.events.automod.AutoModExecutionEvent;
import net.dv8tion.jda.api.events.automod.AutoModRuleCreateEvent;
import net.dv8tion.jda.api.events.automod.AutoModRuleDeleteEvent;
import net.dv8tion.jda.api.events.automod.AutoModRuleUpdateEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.*;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.*;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerAddedEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerRemovedEvent;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateNameEvent;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateTagsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.ImageProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class LoggerListener extends ListenerAdapter {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
    private static final Logger logger = LoggerFactory.getLogger(LoggerListener.class);

    private final LoggerPackage loggerPackage;
    private final ConfigPackage configPackage;
    private final MySqlConnector mySqlConnector;
    private final AWSConnector awsConnector;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("로거 기능부 준비 완료");
    }

    public LoggerListener(LoggerPackage loggerPackage, ConfigPackage configPackage, MySqlConnector mySqlConnector, AWSConnector awsConnector) {
        this.loggerPackage = loggerPackage;
        this.configPackage = configPackage;
        this.mySqlConnector = mySqlConnector;
        this.awsConnector = awsConnector;
    }

    private boolean isConfigEnable(String guildId, int configType) {
        ConfigPackage.Config_Data configData = configPackage.getConfigData(guildId);
        return switch (configType) {
            case ConfigPackage.TEXT_LOGGING_ENABLE -> configData.textLoggingEnable;
            case ConfigPackage.CHANNEL_LOGGING_ENABLE -> configData.channelLoggingEnable;
            case ConfigPackage.MEMBER_LOGGING_ENABLE -> configData.memberLoggingEnable;
            case ConfigPackage.GUILD_LOGGING_ENABLE -> configData.guildLoggingEnable;
            default -> false;
        };
    }

    @Nullable
    public String returnConfigChannel(String guildId, int configType) {
        ConfigPackage.Config_Data configData = configPackage.getConfigData(guildId);
        return switch (configType) {
            case ConfigPackage.TEXT_LOGGING_CHANNEL -> configData.textLoggingChannel;
            case ConfigPackage.CHANNEL_LOGGING_CHANNEL -> configData.channelLoggingChannel;
            case ConfigPackage.MEMBER_LOGGING_CHANNEL -> configData.memberLoggingChannel;
            case ConfigPackage.GUILD_LOGGING_CHANNEL -> configData.guildLoggingChannel;
            default -> null;
        };
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        LoggerPackage.MessagePackage messagePackage = new LoggerPackage.MessagePackage();
        Message message = event.getMessage();
        messagePackage.GuildId = event.getGuild().getId();
        messagePackage.MessageId = message.getId();
        messagePackage.contentRaw = message.getContentRaw();
        messagePackage.authorId = event.getAuthor().getId();
        messagePackage.isAttachment = false;
        if (isConfigEnable(messagePackage.GuildId, ConfigPackage.TEXT_LOGGING_ENABLE)) {
            if (event.getAuthor().isBot()) {
                return;
            }
            if (message.isWebhookMessage() || message.isTTS() || message.isEphemeral()) {
                return;
            }
            List<Message.Attachment> files = message.getAttachments();
            if (!files.isEmpty()) {
                int i = 0;
                for (Message.Attachment attachment : files) {
                    if (attachment.isImage()) {
                        messagePackage.isAttachment = true;
                        i++;
                        String extension = attachment.getFileExtension();
                        File file;
                        if(extension != null) {
                            file = new File(messagePackage.MessageId + "." + extension);
                        } else {
                            file = new File(messagePackage.MessageId + ".tmp");
                        }

                        file = attachment.getProxy().downloadToFile(file).join();
                        this.awsConnector.S3UploadObject(file, messagePackage.MessageId + "_" + i);

                        if (!file.delete()) {
                            logger.warn("파일 삭제 실패");
                        }
                    }
                }
                messagePackage.attachmentCount = i;
            }
            final boolean[] temp = {loggerPackage.loggingMessageUpLoad(messagePackage)};
            if (!temp[0]) {
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        temp[0] = loggerPackage.loggingMessageUpLoad(messagePackage);
                        if (temp[0]) {
                            timer.cancel();
                            this.cancel();
                        }
                    }
                };
                timer.scheduleAtFixedRate(timerTask, 0, 1000);
            }
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        LoggerPackage.MessagePackage messagePackage = new LoggerPackage.MessagePackage();
        Message message = event.getMessage();
        messagePackage.GuildId = event.getGuild().getId();
        messagePackage.MessageId = message.getId();
        String updateMessageContent = message.getContentRaw();
        messagePackage.authorId = event.getAuthor().getId();

        if (isConfigEnable(messagePackage.GuildId, ConfigPackage.TEXT_LOGGING_ENABLE)) {
            if (event.getAuthor().isBot()) {
                return;
            }
            if (message.isWebhookMessage() || message.isTTS() || message.isEphemeral()) {
                return;
            }
            messagePackage = loggerPackage.loggingMessageDownLoad(messagePackage);
            if(messagePackage == null) {
                return;
            }
            if (messagePackage.contentRaw == null) {
                //MESSAGE MISSING! INSERT MESSAGE
                messagePackage.contentRaw = updateMessageContent;
                messagePackage.isAttachment = false;
                final boolean[] temp = {loggerPackage.loggingMessageUpLoad(messagePackage)};
                if (!temp[0]) {
                    LoggerPackage.MessagePackage mP = messagePackage;
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            temp[0] = loggerPackage.loggingMessageUpLoad(mP);
                            if (temp[0]) {
                                timer.cancel();
                                this.cancel();
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask, 0, 1000);
                }
                return;
            }
            String beforeMessage = messagePackage.contentRaw;
            messagePackage.contentRaw = updateMessageContent;
            final boolean[] temp = new boolean[]{loggerPackage.loggingMessageUpdate(messagePackage)};
            if (!temp[0]) {
                LoggerPackage.MessagePackage mP = messagePackage;
                final Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        temp[0] = loggerPackage.loggingMessageUpdate(mP);
                        if (temp[0]) {
                            timer.cancel();
                            this.cancel();
                        }
                    }
                };
                timer.scheduleAtFixedRate(timerTask, 0L, 1000L);
            }

            Member member = event.getGuild().getMemberById(messagePackage.authorId);

            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("수정된 메세지")
                    .setColor(Color.ORANGE)
                    .setDescription("메세지 수정: " + event.getChannel().getAsMention() + "\n[메세지 이동](" + message.getJumpUrl() + ")");

            try {
                builder.addField("수정전 내용", beforeMessage, false);
            } catch (Exception e) {
                e.printStackTrace();
                builder.addField("수정전 내용", "1024자 이상이라서 표현할 수 없습니다.", false);
            }

            try {
                builder.addField("수정후 내용", messagePackage.contentRaw, false);
            } catch (Exception e) {
                e.printStackTrace();
                builder.addField("수정후 내용", "1024자 이상이라서 표현할 수 없습니다.", false);
            }
            builder.addField("수정 시간", time, false);
            if(member != null) {
                builder.setFooter(member.getEffectiveName() + "(" + member.getEffectiveName() + ")", member.getUser().getAvatarUrl());
            } else {
                builder.setFooter("알수 없는 유저");
            }

            this.loggerPackage.messageLoggingSend(builder, event.getGuild());

        }
    }

    @Override
    public void onMessageDelete(@Nonnull MessageDeleteEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        List<File> attachment = new ArrayList<>();
        Guild guild = event.getGuild();
        LoggerPackage.MessagePackage messagePackage = new LoggerPackage.MessagePackage();
        messagePackage.MessageId = event.getMessageId();
        messagePackage.GuildId = event.getGuild().getId();
        messagePackage = loggerPackage.loggingMessageDownLoad(messagePackage);
        if(messagePackage == null) {
            return;
        }
        if (messagePackage.contentRaw != null) {
            if(messagePackage.isAttachment) {
                for(int i = 0; i < messagePackage.attachmentCount;) {
                    i++;
                    attachment.add(this.awsConnector.S3DownloadObject(messagePackage.MessageId + "_" + i));
                }
            }

            try {
                if (messagePackage.contentRaw.isEmpty() && attachment.isEmpty()) {
                    return;
                }
            } catch (NullPointerException ignored) {
                return;
            }

            Member member = event.getGuild().getMemberById(messagePackage.authorId);
            Date date = new Date();
            String time = format.format(date);
            if (messagePackage.contentRaw.isEmpty()) {
                messagePackage.contentRaw = "사진 파일만 있는 메세지";
            }

            if (messagePackage.contentRaw.length() > 1024) {
                messagePackage.contentRaw = "1024자 초과로 인한 처리 불가";
            }

            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("삭제된 메세지")
                    .setColor(Color.RED)
                    .setDescription("메세지 삭제: " + event.getChannel().getAsMention())
                    .addField("내용", messagePackage.contentRaw, false)
                    .addField("메세지 ID", messagePackage.MessageId, false)
                    .addField("삭제 시간", time, false);

            if(member != null) {
                builder.setFooter(member.getEffectiveName() + "(" + member.getEffectiveName() + ")", member.getUser().getAvatarUrl());
            } else {
                builder.setFooter("알수 없는 유저");
            }
            loggerPackage.messageLoggingSend(builder, guild);
            if (!attachment.isEmpty()) {
                loggerPackage.messageLoggingSend(attachment, guild);
            }
            for(File file : attachment) {
                if (file != null && !file.delete()) {
                    logger.warn("파일이 삭제되지 않음");
                }
            }
        }
    }

    @Override
    public void onSessionResume(@NotNull SessionResumeEvent event) {
        try {
            mySqlConnector.reConnection();
        } catch (SQLException e) {
            logger.error("SQL reConnection FAIL");
            e.printStackTrace();
        }

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        boolean isSuccess = configPackage.inviteServer(event.getGuild().getId());
        if(isSuccess) {
            DefaultGuildChannelUnion union = event.getGuild().getDefaultChannel();
            if(union != null) {
                if (union.getType().isMessage()) {
                    union.asTextChannel().sendMessage("설정 명령어를 통해 기본기능을 설정할수 있습니다. 기능을 사용하고 싶은 경우 설정을 확인하여 주시기 바랍니다.").queue();
                }
            }
        }
        //TODO 혹시 Slash 명령어체계 활성화가 필요한 건지, 혹은 자동활성화가 되는지 확인 필요함
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            String oldName = event.getOldValue();
            String newName = event.getNewValue();
            if(oldName == null) {
                oldName = "알 수 없음";
            }
            if(newName == null) {
                newName = "알 수 없음";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("채널 이름 변경")
                    .setColor(Color.GREEN).addField("이전 이름", oldName, false)
                    .addField("변경된 이름", newName, false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateRegion(@NotNull ChannelUpdateRegionEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            Region old = event.getOldValue();
            String oldValue;
            String newValue;
            if (old == null) {
                oldValue = "알 수 없음";
            } else {
                oldValue = old.getName() + "(" + old.getEmoji() + ")";
            }
            Region newV1 = event.getNewValue();
            if (newV1 == null) {
                newValue = "알 수 없음";
            }else {
                newValue = newV1.getName() + "(" + newV1.getEmoji() + ")";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("채널 토픽 변경")
                    .setColor(Color.GREEN)
                    .addField("채널명", event.getChannel().getName(), false)
                    .addField("이전 지역 서버", oldValue, false)
                    .addField("변경된 지역 서버", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateTopic(@NotNull ChannelUpdateTopicEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            String oldValue = event.getOldValue();
            if (oldValue == null) {
                oldValue = "없음";
            }
            String newValue = event.getNewValue();
            if (newValue == null) {
                newValue = "없음";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("텍스트 채널 토픽 변경")
                    .setColor(Color.GREEN)
                    .addField("채널명", event.getChannel().getName(), false)
                    .addField("이전 토픽", oldValue, false)
                    .addField("변경된 토픽", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onPermissionOverrideCreate(@NotNull PermissionOverrideCreateEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
            StringBuilder stringBuilder = new StringBuilder();
            for (Permission permission : event.getPermissionOverride().getAllowed()) {
                stringBuilder.append("➕").append(permission.getName()).append("\n");
            }
            switch (event.getChannelType()) {
                case NEWS,FORUM,GROUP, MEDIA, CATEGORY, TEXT, VOICE -> {
                    if (event.getPermissionOverride().isRoleOverride()) {
                        builder.setTitle("권한 생성(역할)")
                                .setColor(Color.GREEN)
                                .addField("해당 카테고리/채널명", event.getChannel().getName(), false)
                                .addField("변경된 권한(" + Objects.requireNonNull(event.getPermissionOverride().getRole()).getAsMention() + ")", stringBuilder.toString(), false)
                                .addField("변경 시간", time, false);
                    } else if (event.getPermissionOverride().isMemberOverride()) {
                        builder.setTitle("권한 생성(멤버)")
                                .setColor(Color.GREEN)
                                .addField("해당 카테고리/채널명", event.getChannel().getName(), false)
                                .addField("변경된 권한(" + Objects.requireNonNull(event.getPermissionOverride().getMember()).getAsMention() + ")", stringBuilder.toString(), false)
                                .addField("변경 시간", time, false);
                    }
                }
            }
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    public void onPermissionOverrideUpdate(@Nonnull PermissionOverrideUpdateEvent event) {
        //TODO 나중에 만들기
        super.onPermissionOverrideUpdate(event);
    }

    @Override
    public void onPermissionOverrideDelete(@NotNull PermissionOverrideDeleteEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
            StringBuilder stringBuilder = new StringBuilder();
            for (Permission permission : event.getPermissionOverride().getDenied()) {
                stringBuilder.append("➖").append(permission.getName()).append("\n");
            }
            switch (event.getChannelType()) {
                case NEWS,FORUM,GROUP, MEDIA, CATEGORY, TEXT, VOICE -> {
                    if (event.getPermissionOverride().isRoleOverride()) {
                        builder.setTitle("권한 삭제(역할)")
                                .setColor(Color.GREEN)
                                .addField("해당 카테고리/채널명", event.getChannel().getName(), false)
                                .addField("변경된 권한(" + Objects.requireNonNull(event.getPermissionOverride().getRole()).getAsMention() + ")", stringBuilder.toString(), false)
                                .addField("변경 시간", time, false);
                    } else if (event.getPermissionOverride().isMemberOverride()) {
                        builder.setTitle("권한 삭제(멤버)")
                                .setColor(Color.GREEN)
                                .addField("해당 카테고리/채널명", event.getChannel().getName(), false)
                                .addField("변경된 권한(" + Objects.requireNonNull(event.getPermissionOverride().getMember()).getAsMention() + ")", stringBuilder.toString(), false)
                                .addField("변경 시간", time, false);
                    }
                }
            }
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateNSFW(@NotNull ChannelUpdateNSFWEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder;
            if (Boolean.TRUE.equals(event.getOldValue())) {
                builder = EmbedUtils.getDefaultEmbed()
                        .setTitle("후방 주의 채널 해제")
                        .setColor(Color.GREEN)
                        .addField("채널명", event.getChannel().getAsMention(), false)
                        .addField("변경 시간", time, false);
            } else {
                builder = EmbedUtils.getDefaultEmbed()
                        .setTitle("후방 주의 채널 지정")
                        .setColor(Color.RED)
                        .addField("채널명", event.getChannel().getAsMention(), false)
                        .addField("변경 시간", time, false);
            }
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateSlowmode(@NotNull ChannelUpdateSlowmodeEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("텍스트 채널 슬로우 모드 지정")
                    .setColor(Color.YELLOW)
                    .addField("채널명", event.getChannel().getAsMention(), false)
                    .addField("이전 슬로우 모드 시간", String.valueOf(event.getOldValue()), false)
                    .addField("현재 슬로우 모드 시간", String.valueOf(event.getNewValue()), false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        Guild guild = event.getGuild();

        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("채널 생성")
                    .setColor(Color.GREEN)
                    .addField("채널명", event.getChannel().getAsMention(), false)
                    .addField("채널 타입", event.getChannelType().toString(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("채널 삭제")
                    .setColor(Color.RED)
                    .addField("채널명", event.getChannel().getName(), false)
                    .addField("채널 타입", event.getChannelType().toString(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateUserLimit(@NotNull ChannelUpdateUserLimitEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            String oldValue;
            String newValue;
            Integer tmp = event.getOldValue();
            if(tmp == null) {
                oldValue = "알수없음";
            } else {
                oldValue = tmp.toString();
            }
            Integer tmp2 = event.getNewValue();
            if(tmp2 == null) {
                newValue = "알수없음";
            } else {
                newValue = tmp2.toString();
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("채널 유저 제한 수 변경")
                    .setColor(Color.YELLOW)
                    .addField("채널명", event.getChannel().getName(), false)
                    .addField("이전 제한 수", oldValue, false)
                    .addField("변경 제한 수", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateAfkChannel(@NotNull GuildUpdateAfkChannelEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            Date date = new Date();
            String time = format.format(date);
            String oldValue;
            String newValue;
            VoiceChannel oldAfkChannel = event.getOldAfkChannel();
            if(oldAfkChannel == null) {
                oldValue = "알수없음";
            } else {
                oldValue = oldAfkChannel.getAsMention();
            }
            VoiceChannel newAfkChannel = event.getNewAfkChannel();
            if(newAfkChannel == null) {
                newValue = "알수없음";
            } else {
                newValue = newAfkChannel.getAsMention();
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 AFK 채널 변경")
                    .setColor(Color.GREEN)
                    .addField("이전 채널", oldValue, false)
                    .addField("변경 채널", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateAfkTimeout(@NotNull GuildUpdateAfkTimeoutEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            Date date = new Date();
            String time = format.format(date);
            int oldValue;
            int newValue;
            try {
                oldValue = event.getOldValue().getSeconds();
            } catch (NullPointerException e) {
                oldValue = 0;
            }
            try {
                newValue = event.getNewValue().getSeconds();
            } catch (NullPointerException e) {
                newValue = 0;
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 AFK 기준 시간값 변경")
                    .setColor(Color.GREEN)
                    .addField("변경전 시간값", oldValue + "분", false)
                    .addField("변경후 시간값", newValue + "분", false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateBoostTier(@NotNull GuildUpdateBoostTierEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            Date date = new Date();
            String time = format.format(date);
            String oldValue;
            String newValue;
            try {
                oldValue = event.getOldValue().name();
            } catch (NullPointerException e) {
                oldValue = "알수없음";
            }
            try {
                newValue = event.getNewValue().name();
            } catch (NullPointerException e) {
                newValue = "알수없음";
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버부스트 티어 변경")
                    .setColor(Color.GREEN)
                    .addField("이전 티어", oldValue, false)
                    .addField("현재 티어", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateBanner(@NotNull GuildUpdateBannerEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            Date date = new Date();
            String time = format.format(date);
            InputStream newValue = null;
            InputStream oldValue = null;
            ImageProxy oldV = event.getOldBanner();
            ImageProxy newV = event.getNewBanner();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 배너 변경")
                    .setColor(Color.GREEN)
                    .addField("변경 시간", time, false);
            if(oldV != null) {
                try(InputStream value = oldV.download().join()) {
                    oldValue = value;
                } catch (IOException ignored) {
                }
            }
            if(newV != null) {
                try(InputStream value = newV.download().join()) {
                    newValue = value;
                } catch (IOException ignored) {
                }
            }
            String channelId = returnConfigChannel(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL);
            if (channelId != null) {
                try {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    if(channel == null) {
                        ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                        if(channel1 != null) {
                            channel1.sendMessageEmbeds(builder.build()).queue();
                            if(oldValue != null) channel1.sendMessage("변경 전 배너").addFiles(FileUpload.fromData(oldValue, "이전 배너")).queue();
                            if(newValue != null) channel1.sendMessage("변경 후 배너").addFiles(FileUpload.fromData(newValue, "신규 배너")).queue();
                        }
                    } else {
                        channel.sendMessageEmbeds(builder.build()).queue();
                        if(oldValue != null) channel.sendMessage("변경 전 배너").addFiles(FileUpload.fromData(oldValue, "이전 배너")).queue();
                        if(newValue != null) channel.sendMessage("변경 후 배너").addFiles(FileUpload.fromData(newValue, "신규 배너")).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("guildLoggingSend WARN");
                }
            }
            try {
                if(oldValue != null) oldValue.close();
                if(newValue != null) newValue.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Invite invite = event.getInvite();
            String time = format.format(invite.getTimeCreated());
            String value = invite.getUrl();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 초대 링크 생성")
                    .setColor(Color.GREEN)
                    .addField("생성자", (invite.getInviter() != null) ? invite.getInviter().getName() : "알수 없음", false)
                    .addField("생성된 URL", value, false)
                    .addField("최대 사용 횟수", (invite.getMaxUses() == 0) ? "무제한" : invite.getMaxUses() + "회", false)
                    .addField("최대 사용 횟수", (invite.getMaxAge() == 0) ? "무제한" : invite.getMaxUses() + "초", false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            String value = "discord.gg/" + event.getCode();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 초대 링크 삭제")
                    .setColor(Color.RED)
                    .addField("삭제된 URL", value, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            String oldValue = event.getOldName();
            String newValue = event.getNewName();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 이름 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 이름", oldValue, false)
                    .addField("변경 후 이름", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateMFALevel(@NotNull GuildUpdateMFALevelEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            Guild.MFALevel oldValue = event.getOldMFALevel();
            Guild.MFALevel newValue = event.getNewMFALevel();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 보안 단계 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 단계", oldValue.name(), false)
                    .addField("변경 후 단계", newValue.name(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateLocale(@NotNull GuildUpdateLocaleEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            DiscordLocale oldValue = event.getOldValue();
            DiscordLocale newValue = event.getNewValue();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버의 음성 연결 서버 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 서버", oldValue.getLanguageName() + "(" + oldValue.getNativeName() + ")", false)
                    .addField("변경 후 서버", newValue.getLanguageName() + "(" + newValue.getNativeName() + ")", false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateIcon(@NotNull GuildUpdateIconEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            Date date = new Date();
            String time = format.format(date);
            InputStream newValue = null;
            InputStream oldValue = null;
            ImageProxy oldV = event.getOldIcon();
            ImageProxy newV = event.getNewIcon();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 아이콘 변경")
                    .setColor(Color.GREEN)
                    .addField("변경 시간", time, false);
            if(oldV != null) {
                try(InputStream value = oldV.download().join()) {
                    oldValue = value;
                } catch (IOException ignored) {
                }
            }
            if(newV != null) {
                try (InputStream value = newV.download().join()) {
                    newValue = value;
                } catch (IOException ignored) {
                }
            }
            String channelId = returnConfigChannel(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL);
            if (channelId != null) {
                try {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    if(channel == null) {
                        ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                        if(channel1 != null) {
                            channel1.sendMessageEmbeds(builder.build()).queue();
                            if(oldValue != null) channel1.sendMessage("변경 전 아이콘").addFiles(FileUpload.fromData(oldValue, "이전 아이콘")).queue();
                            if(newValue != null) channel1.sendMessage("변경 후 아이콘").addFiles(FileUpload.fromData(newValue, "신규 아이콘")).queue();
                        }
                    } else {
                        channel.sendMessageEmbeds(builder.build()).queue();
                        if(oldValue != null) channel.sendMessage("변경 전 아이콘").addFiles(FileUpload.fromData(oldValue, "이전 아이콘")).queue();
                        if(newValue != null) channel.sendMessage("변경 후 아이콘").addFiles(FileUpload.fromData(newValue, "신규 아이콘")).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("guildLoggingSend WARN");
                }
            }
            try {
                if(oldValue != null) oldValue.close();
                if(newValue != null) newValue.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onGuildUpdateDescription(@NotNull GuildUpdateDescriptionEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            String oldValue = event.getOldDescription();
            String newValue = event.getNewDescription();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버의 설명 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 설명", (oldValue == null) ? "알수 없음" : oldValue, false)
                    .addField("변경 후 설명", (newValue == null) ? "알수 없음" : newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateOwner(@NotNull GuildUpdateOwnerEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            String oldValue = event.getOldOwnerId();
            String newValue = event.getNewOwnerId();
            User oldUser = event.getJDA().getUserById(oldValue);
            User newUser = event.getJDA().getUserById(newValue);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 소유자 변경")
                    .setColor(Color.RED)
                    .addField("이전 소유자", (oldUser == null) ? "알수 없음" : oldUser.getName(), false)
                    .addField("현재 소유자", (newUser == null) ? "알수 없음" : newUser.getName(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateSystemChannel(@NotNull GuildUpdateSystemChannelEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            TextChannel oldValue = event.getOldSystemChannel();
            TextChannel newValue = event.getOldSystemChannel();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 시스템 채널 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 채널", (oldValue == null) ? "알수 없음" : oldValue.getAsMention(), false)
                    .addField("변경 후 채널", (newValue == null) ? "알수 없음" : newValue.getAsMention(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUpdateRulesChannel(@NotNull GuildUpdateRulesChannelEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            TextChannel oldValue = event.getOldRulesChannel();
            TextChannel newValue = event.getOldRulesChannel();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 규칙 채널 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 채널", (oldValue == null) ? "알수 없음" : oldValue.getAsMention(), false)
                    .addField("변경 후 채널", (newValue == null) ? "알수 없음" : newValue.getAsMention(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildStickerAdded(@NotNull GuildStickerAddedEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            Sticker sticker = event.getSticker();
            InputStream input = null;
            try(InputStream value = sticker.getIcon().download().join()) {
                input = value;
            } catch (IOException ignored) {
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 스티커 추가")
                    .setColor(Color.RED)
                    .addField("변경 시간", time, false);
            String channelId = returnConfigChannel(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL);
            if (channelId != null) {
                try {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    if(channel == null) {
                        ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                        if(channel1 != null) {
                            channel1.sendMessageEmbeds(builder.build()).queue();
                            if(input != null) channel1.sendMessage("추가된 스티커").addFiles(FileUpload.fromData(input, sticker.getId())).queue();
                        }
                    } else {
                        channel.sendMessageEmbeds(builder.build()).queue();
                        if(input != null) channel.sendMessage("추가된 스티커").addFiles(FileUpload.fromData(input, sticker.getId())).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("guildLoggingSend WARN");
                }
            }
            try {
                if(input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onGuildStickerRemoved(@NotNull GuildStickerRemovedEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            Sticker sticker = event.getSticker();
            InputStream input = null;
            try(InputStream value = sticker.getIcon().download().join()) {
                input = value;
            } catch (IOException ignored) {
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 스티커 삭제")
                    .setColor(Color.RED)
                    .addField("변경 시간", time, false);
            String channelId = returnConfigChannel(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL);

            if (channelId != null) {
                try {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    if(channel == null) {
                        ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                        if(channel1 != null) {
                            channel1.sendMessageEmbeds(builder.build()).queue();
                            if(input != null) channel1.sendMessage("삭제된 스티커").addFiles(FileUpload.fromData(input, sticker.getId())).queue();
                        }
                    } else {
                        channel.sendMessageEmbeds(builder.build()).queue();
                        if(input != null) channel.sendMessage("삭제된 스티커").addFiles(FileUpload.fromData(input, sticker.getId())).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("guildLoggingSend WARN");
                }
            }
            try {
                if(input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onGuildStickerUpdateName(@NotNull GuildStickerUpdateNameEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            String oldValue = event.getOldValue();
            String newValue = event.getNewValue();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 스티커 이름 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 이름", oldValue, false)
                    .addField("변경 후 이름", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildStickerUpdateTags(@NotNull GuildStickerUpdateTagsEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            Set<String> oldValue = event.getOldValue();
            Set<String> newValue = event.getNewValue();
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 스티커 태그 변경")
                    .setColor(Color.RED)
                    .addField("변경 전 태그", oldValue.toString(), false)
                    .addField("변경 후 태그", newValue.toString(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onAutoModRuleCreate(@NotNull AutoModRuleCreateEvent event) {
        Guild guild = event.getRule().getGuild();
        AutoModRule rule = event.getRule();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("신규 AutoMod 설정 감지")
                    .setColor(Color.RED)
                    .addField("설정명", rule.getName(), false)
                    .addField("설정 생성자", Objects.requireNonNull(guild.getMemberById(rule.getCreatorId())).getUser().getName(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onAutoModRuleDelete(@NotNull AutoModRuleDeleteEvent event) {
        Guild guild = event.getRule().getGuild();
        AutoModRule rule = event.getRule();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("AutoMod 설정 삭제 감지")
                    .setColor(Color.RED)
                    .addField("설정명", rule.getName(), false)
                    .addField("설정 삭제자", Objects.requireNonNull(guild.getMemberById(rule.getCreatorId())).getUser().getName(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onAutoModRuleUpdate(@NotNull AutoModRuleUpdateEvent event) {
        Guild guild = event.getRule().getGuild();
        AutoModRule rule = event.getRule();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("AutoMod 설정 변경 감지")
                    .setColor(Color.RED)
                    .addField("설정명", rule.getName(), false)
                    .addField("설정 변경자", Objects.requireNonNull(guild.getMemberById(rule.getCreatorId())).getUser().getName(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onAutoModExecution(@NotNull AutoModExecutionEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("AutoMod 실행 감지")
                    .setColor(Color.RED)
                    .addField("감지 타입", event.getTriggerType().name(), false)
                    .addField("실행된 룰", event.getGuild().retrieveAutoModRuleById(event.getRuleId()).complete().getName(), false)
                    .addField("트리거 유저", Objects.requireNonNull(guild.getMemberById(event.getUserId())).getAsMention(), false)
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onChannelUpdateBitrate(@NotNull ChannelUpdateBitrateEvent event) {
        Guild guild = event.getGuild();

        if (isConfigEnable(guild.getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE)) {
            
            Date date = new Date();
            String time = format.format(date);
            String oldValue;
            String newValue;
            Integer tmp = event.getOldValue();
            if(tmp == null) {
                oldValue = "알수없음";
            } else {
                oldValue = tmp.toString();
            }
            Integer tmp2 = event.getNewValue();
            if(tmp2 == null) {
                newValue = "알수없음";
            } else {
                newValue = tmp2.toString();
            }
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("보이스 채널 비트레이트 변경")
                    .setColor(Color.YELLOW)
                    .addField("채널명", event.getChannel().getName(), false)
                    .addField("이전 비트레이트", oldValue, false)
                    .addField("변경 비트레이트", newValue, false)
                    .addField("변경 시간", time, false);
            loggerPackage.channelLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 입장")
                    .setDescription(event.getMember().getAsMention() + "유저가 서버에 들어왔습니다.")
                    .setColor(Color.GREEN)
                    .addField("유저명", event.getMember().getEffectiveName() + "(" + event.getMember().getUser().getName() + ") ", false)
                    .addField("유저 가입일", event.getMember().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                    .addField("입장 시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 퇴장")
                    .setDescription(event.getUser().getAsMention() + "유저가 서버에서 나갔습니다.")
                    .setColor(Color.RED)
                    .addField("유저명", event.getUser().getEffectiveName() + "(" + event.getUser().getName() + ") ", false)
                    .addField("유저 가입일", event.getUser().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                    .addField("퇴장 시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            StringBuilder stringBuilder = new StringBuilder();

            for (Role role : event.getRoles()) {
                stringBuilder.append(role.getAsMention()).append("\n");
            }
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 역할 추가")
                    .setDescription("대상 유저:" + event.getMember().getAsMention()).setColor(Color.GREEN)
                    .addField("유저명", event.getMember().getEffectiveName() + "(" + event.getMember().getUser().getName() + ") ", false)
                    .addField("추가된 역할", stringBuilder.toString(), false)
                    .addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            StringBuilder stringBuilder = new StringBuilder();

            for (Role role : event.getRoles()) {
                stringBuilder.append(role.getAsMention()).append("\n");
            }
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("유저 역할 삭제")
                    .setDescription("대상 유저:" + event.getMember().getAsMention())
                    .setColor(Color.RED)
                    .addField("유저명", event.getMember().getEffectiveName() + "(" + event.getMember().getUser().getName() + ") ", false)
                    .addField("삭제된 역할", stringBuilder.toString(), false).addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            String nickName = event.getOldNickname();
            if (nickName == null) {
                nickName = "없음";
            }
            String newNickName = event.getNewNickname();
            if (newNickName == null) {
                newNickName = "없음";
            }

            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 닉네임 변경")
                    .setDescription("대상 유저:" + event.getMember().getAsMention()).setColor(Color.GREEN)
                    .addField("이전 이름", nickName, false)
                    .addField("현재 이름", newNickName, false)
                    .addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 밴")
                    .setDescription("대상 유저:" + event.getUser().getName()).setColor(Color.RED).addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("유저 밴 헤제")
                    .setDescription("대상 유저:" + event.getUser().getName())
                    .setColor(Color.GREEN).addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onGuildVoiceGuildMute(@NotNull GuildVoiceGuildMuteEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
            builder.setColor(Color.RED)
                    .addField("시간", time, false);
            if (event.isGuildMuted()) {
                builder.setTitle("유저 강제 뮤트")
                        .setDescription("대상 유저:" + event.getMember().getEffectiveName() + "(" + event.getMember().getAsMention() + ")");
            } else {
                builder.setTitle("유저 강제 뮤트 해제")
                        .setDescription("대상 유저:" + event.getMember().getEffectiveName() + "(" + event.getMember().getAsMention() + ")");
            }
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            Role role = event.getRole();
            String time = format.format(date);
            StringBuilder stringBuilder = new StringBuilder();

            for (Permission permission : role.getPermissions()) {
                stringBuilder.append(permission.getName()).append("\n");
            }

            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("역할 생성").setColor(role.getColor())
                    .addField("역할명", role.getName() + "(" + role.getAsMention() + ")", false)
                    .addField("권한", stringBuilder.toString(), false)
                    .addField("멘션 가능", role.isMentionable() ? "예" : "아니오", true)
                    .addField("유저 분리 표시", role.isHoisted() ? "예" : "아니오", true)
                    .addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            Date date = new Date();
            Role role = event.getRole();
            String time = format.format(date);
            StringBuilder stringBuilder = new StringBuilder();

            for (Permission permission : role.getPermissions()) {
                stringBuilder.append(permission.getName()).append("\n");
            }

            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("역할 삭제").
                    setColor(role.getColor())
                    .addField("역할명", role.getName(), false)
                    .addField("권한", stringBuilder.toString(), false)
                    .addField("멘션 가능", role.isMentionable() ? "예" : "아니오", true)
                    .addField("유저 분리 표시", role.isHoisted() ? "예" : "아니오", true)
                    .addField("시간", time, false);
            loggerPackage.memberLoggingSend(builder, guild);
        }
    }

    @Override
    public void onRoleUpdateColor(@NotNull RoleUpdateColorEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            loggerPackage.onRoleUpdate(event.getRole(), guild, this);
        }
    }

    public void onRoleUpdateHoisted(@Nonnull RoleUpdateHoistedEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            loggerPackage.onRoleUpdate(event.getRole(), guild, this);
        }
    }

    public void onRoleUpdateMentionable(@Nonnull RoleUpdateMentionableEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            loggerPackage.onRoleUpdate(event.getRole(), guild, this);
        }
    }

    public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            loggerPackage.onRoleUpdate(event.getRole(), guild, this);
        }
    }

    public void onRoleUpdatePermissions(@Nonnull RoleUpdatePermissionsEvent event) {
        Guild guild = event.getGuild();
        if (isConfigEnable(guild.getId(), ConfigPackage.MEMBER_LOGGING_ENABLE)) {
            loggerPackage.onRoleUpdate(event.getRole(), guild, this);
        }
    }

    @Override
    public void onEmojiAdded(@NotNull EmojiAddedEvent event) {
        Guild guild = event.getGuild();
        Emoji emote = event.getEmoji();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 이모지 추가")
                    .setColor(Color.GREEN)
                    .addField("이모지명", emote.getName(), false)
                    .setDescription("[이모지 보기](" + emote.getAsReactionCode() + ")")
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onEmojiRemoved(@NotNull EmojiRemovedEvent event) {
        Guild guild = event.getGuild();
        Emoji emote = event.getEmoji();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 이모지 제거").setColor(Color.GREEN)
                    .addField("이모지명", emote.getName(), false)
                    .setDescription("[이모지 보기](" + emote.getAsReactionCode() + ")")
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }

    @Override
    public void onEmojiUpdateName(@NotNull EmojiUpdateNameEvent event) {
        Guild guild = event.getGuild();
        Emoji emote = event.getEmoji();
        if (isConfigEnable(guild.getId(), ConfigPackage.GUILD_LOGGING_ENABLE)) {
            Date date = new Date();
            String time = format.format(date);
            EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                    .setTitle("서버 이모지 업데이트").setColor(Color.GREEN)
                    .addField("이전 이모지명", event.getOldName(), false)
                    .addField("변경된 이모지명", event.getNewName(), false)
                    .setDescription("[이모지 보기](" + emote.getAsReactionCode() + ")")
                    .addField("변경 시간", time, false);
            loggerPackage.guildLoggingSend(builder, guild);
        }
    }
}