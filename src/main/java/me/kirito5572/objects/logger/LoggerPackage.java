package me.kirito5572.objects.logger;

import me.kirito5572.listeners.logger.LoggerListener;
import me.kirito5572.objects.main.MySqlConnector;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggerPackage {
    private final MySqlConnector mySqlConnector;
    private static final Logger logger = LoggerFactory.getLogger(LoggerPackage.class);
    private final ConfigPackage configPackage;

    public LoggerPackage (MySqlConnector mySqlConnector, ConfigPackage configPackage) {
        this.mySqlConnector = mySqlConnector;
        this.configPackage = configPackage;
    }

    public boolean loggingMessageUpLoad(MessagePackage messagePackage) {
        @Language("MySQL") String queryString = "INSERT INTO ritobotv2_general.messagelogging VALUES (?, ?, ?, ?, ?, ?)";
        try {
            this.mySqlConnector.Insert_Query(queryString,
                    new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.INT},
                    new String[]{messagePackage.MessageId, messagePackage.GuildId, messagePackage.contentRaw,
                            messagePackage.authorId, (messagePackage.isAttachment) ? "1" : "0", String.valueOf(messagePackage.attachmentCount)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Message SQL INSERT ERROR");
            return false;
        }
    }

    public boolean loggingMessageUpdate(MessagePackage messagePackage) {
        @Language("MySQL") String queryString = "UPDATE ritobotv2_general.messagelogging SET contentRaw = ? WHERE messageId = ?";
        try {
            this.mySqlConnector.Insert_Query(queryString,
                    new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                    new String[]{messagePackage.contentRaw, messagePackage.MessageId});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Message SQL UPDATE ERROR");
            return false;
        }
    }

    @Nullable
    public MessagePackage loggingMessageDownLoad(MessagePackage messagePackage) {
        @Language("MySQL")String queryString = "SELECT * FROM ritobotv2_general.messageLogging WHERE messageId=?;";

        try (ResultSet rs = this.mySqlConnector.Select_Query(queryString,
                new int[]{this.mySqlConnector.STRING},
                new String[]{messagePackage.MessageId})){
            if(rs.next()) {
                messagePackage.contentRaw = rs.getString("contentRaw");
                messagePackage.authorId = rs.getString("authorId");
                messagePackage.isAttachment = rs.getString("isAttachment").equals("1");
                messagePackage.attachmentCount = rs.getInt("attachmentCount");
            }
        } catch (Exception ignored) {
            return null;
        }
        return messagePackage;
    }

    public void messageLoggingSend(@NotNull List<File> file, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.TEXT_LOGGING_CHANNEL);
        if (channelId != null) {
            try {
                List<FileUpload> sendFiles = new ArrayList<>();
                for(File temp : file) {
                    if(temp != null) {
                        sendFiles.add(FileUpload.fromData(temp));
                    }
                }

                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendFiles(sendFiles).queue();
                    }
                } else {
                    channel.sendFiles(sendFiles).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void messageLoggingSend(@NotNull EmbedBuilder builder, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.TEXT_LOGGING_CHANNEL);

        if (channelId != null) {
            try {
                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendMessageEmbeds(builder.build()).queue();
                    }
                } else {
                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("messageLoggingSend WARN");
            }
        }
    }

    public void channelLoggingSend(@NotNull EmbedBuilder builder, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.CHANNEL_LOGGING_CHANNEL);
        if (channelId != null) {
            try {
                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendMessageEmbeds(builder.build()).queue();
                    }
                } else {
                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("channelLoggingSend WARN");
            }
        }
    }

    public void memberLoggingSend(@NotNull EmbedBuilder builder, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.CHANNEL_LOGGING_CHANNEL);

        if (channelId != null) {
            try {
                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendMessageEmbeds(builder.build()).queue();
                    }
                } else {
                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("memberLoggingSend WARN");
            }
        }
    }

    public void guildLoggingSend(@NotNull EmbedBuilder builder, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.GUILD_LOGGING_CHANNEL);

        if (channelId != null) {
            try {
                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendMessageEmbeds(builder.build()).queue();
                    }
                } else {
                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("guildLoggingSend WARN");
            }
        }
    }

    public void sayLoggingSend(@NotNull EmbedBuilder builder, @NotNull Guild guild) {
        String channelId = returnConfigChannel(guild.getId(), ConfigPackage.SAY_OUTPUT_CHANNEL);

        if (channelId != null) {
            try {
                TextChannel channel = guild.getTextChannelById(channelId);
                if(channel == null) {
                    ThreadChannel channel1 = guild.getThreadChannelById(channelId);
                    if(channel1 != null) {
                        channel1.sendMessageEmbeds(builder.build()).queue();
                    }
                } else {
                    channel.sendMessageEmbeds(builder.build()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("sayLoggingSend WARN");
            }
        }
    }

    @Nullable
    public String returnConfigChannel(String guildId, int configType) {
        ConfigPackage.Config_Data configData = configPackage.getConfigData(guildId);
        return switch (configType) {
            case ConfigPackage.TEXT_LOGGING_CHANNEL -> configData.textLoggingChannel;
            case ConfigPackage.CHANNEL_LOGGING_CHANNEL -> configData.channelLoggingChannel;
            case ConfigPackage.MEMBER_LOGGING_CHANNEL -> configData.memberLoggingChannel;
            case ConfigPackage.GUILD_LOGGING_CHANNEL -> configData.guildLoggingChannel;
            case ConfigPackage.SAY_OUTPUT_CHANNEL ->  configData.sayOutputChannel;
            default -> null;
        };
    }

    public void onRoleUpdate(@NotNull Role role, @NotNull Guild guild, LoggerListener listener) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
        Date time = new Date();
        String time2 = format2.format(time);
        StringBuilder stringBuilder = new StringBuilder();

        for (Permission permission : role.getPermissions()) {
            stringBuilder.append(permission.getName()).append("\n");
        }

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle("역할 변경").setColor(role.getColor())
                .addField("역할명", role.getName() + "(" + role.getAsMention() + ")", false)
                .addField("권한", stringBuilder.toString(), false)
                .addField("멘션 가능", role.isMentionable() ? "예" : "아니오", true)
                .addField("유저 분리 표시", role.isHoisted() ? "예" : "아니오", true)
                .addField("시간", time2, false);
        memberLoggingSend(builder, guild);
    }

    public static class MessagePackage {
        public String GuildId;
        public String MessageId;
        public String contentRaw;
        public String authorId;
        public boolean isAttachment;
        public int attachmentCount;
    }
}
