package com.kirito5572.listeners.logger;

import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.logger.LoggerPackage;
import com.kirito5572.objects.main.CommandManager;
import com.kirito5572.objects.main.MySqlConnector;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConfigListener extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(ConfigListener.class);
    private final LoggerPackage loggerPackage;
    private final ConfigPackage configPackage;
    private final MySqlConnector mySqlConnector;
    private final CommandManager commandManager;


    public ConfigListener(LoggerPackage loggerPackage, ConfigPackage configPackage, MySqlConnector mySqlConnector, CommandManager commandManager) {
        this.loggerPackage = loggerPackage;
        this.configPackage = configPackage;
        this.mySqlConnector = mySqlConnector;
        this.commandManager = commandManager;
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("설정 명령어 기능부 준비 완료");
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if(event.getComponentId().equals("설정")) {
            event.getMessage().delete().queue();
            List<String> values = event.getValues();
            switch (values.get(0)) {
                case "config1" -> //음악 기능
                        event.reply("음악 기능 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("MusicCommandEnable", "활성화"),
                                        Button.danger("MusicCommandDisable", "비활성화"),
                                        Button.success("CommandReload","새로고침")
                                ).setEphemeral(true).queue();
                case "config2" -> //메세지 필터링
                        event.reply("메세지 필터링 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("MessageFilterEnable", "활성화"),
                                        Button.danger("MessageFilterDisable", "비활성화"),
                                        Button.success("MessageFilterWordAddRemove","단어 등록/삭제"),
                                        Button.success("MessageFilterLoggingChannelChange", "필터링 결과 채널 설정")
                                ).setEphemeral(true).queue();
                case "config3" -> //로깅(메세지 관련)
                        event.reply("메세지 관련 로깅 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("MessageLoggerEnable", "활성화"),
                                        Button.danger("MessageLoggerDisable", "비활성화"),
                                        Button.success("MessageLoggerChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config4" -> //로깅(채널 관련)
                        event.reply("채널 관련 로깅 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("ChannelLoggerEnable", "활성화"),
                                        Button.danger("ChannelLoggerDisable", "비활성화"),
                                        Button.success("ChannelLoggerChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config5" -> //로깅(유저 관련)
                        event.reply("유저 관련 로깅 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("MemberLoggerEnable", "활성화"),
                                        Button.danger("MemberLoggerDisable", "비활성화"),
                                        Button.success("MemberLoggerChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config6" -> //로깅(서버 관련)
                        event.reply("서버 관련 로깅 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("GuildLoggerEnable", "활성화"),
                                        Button.danger("GuildLoggerDisable", "비활성화"),
                                        Button.success("GuildLoggerChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config7" -> //LEWD 명령어
                        event.reply("성인용 명령어 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("LewdCommandEnable", "활성화"),
                                        Button.danger("LewdCommandDisable", "비활성화"),
                                        Button.success("CommandReload","새로고침"),
                                        Button.success("LewdLoggerChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config8" -> //링크 제한
                        event.reply("링크 제한 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("LinkFilterEnable", "활성화"),
                                        Button.danger("LinkFilterDisable", "비활성화"),
                                        Button.success("LinkFilterChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
                case "config9" -> //전체 공지 수신
                        event.reply("공지 수신 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("NoticeEnable", "수신"),
                                        Button.danger("NoticeDisable", "비수신"),
                                        Button.success("NoticeChannelChange","수신 채널 변경")
                                ).setEphemeral(true).queue();
                case "config10" -> //말 명령어
                        event.reply("말 명령어 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("SayCommandEnable", "활성화"),
                                        Button.danger("SayCommandDisable", "비활성화"),
                                        Button.success("CommandReload","새로고침"),
                                        Button.success("SayCommandChannelChange","로그 출력 채널 변경")
                                ).setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        if(guild == null) {
            event.getMessage().delete().queue();
            event.reply("해당 명령어는 서버에서만 사용할 수 있습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        switch (event.getComponentId()) {
            //음악 기능
            case "MusicCommandEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).musicEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MUSIC_ENABLE, true);
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 음악 명령어가 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "MusicCommandDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).musicEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MUSIC_ENABLE, false);
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 음악 명령어가 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "CommandReload" -> {
                commandManager.updateCommand(event.getGuild().getId());
                event.getMessage().delete().queue();
                event.reply("명령어 리스트가 새로고침 되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            }
            //메세지 필터링
            case "MessageFilterEnable", "MessageFilterDisable", "MessageFilterWordAddRemove", "MessageFilterLoggingChannelChange" ->
                    event.reply("메세지 필터링 기능은 현재 개발중입니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            //로깅(메세지 관련)
            case "MessageLoggerEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).textLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.TEXT_LOGGING_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.TEXT_LOGGING_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 활성화 됩니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "MessageLoggerDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).textLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.TEXT_LOGGING_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.TEXT_LOGGING_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 종료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "MessageLoggerChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).textLoggingEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).textLoggingChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.TEXT_LOGGING_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("로깅(메세지 관련) 기능이 비활성화 되어있습니다").queue();
                }
            }
            //로깅(채널 관련)
            case "ChannelLoggerEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).channelLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHANNEL_LOGGING_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 활성화 됩니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "ChannelLoggerDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).channelLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHANNEL_LOGGING_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHANNEL_LOGGING_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 종료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "ChannelLoggerChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).channelLoggingEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).channelLoggingChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHANNEL_LOGGING_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("로깅(채널 관련) 기능이 비활성화 되어있습니다").queue();
                }
            }
            //로깅(유저 관련)
            case "MemberLoggerEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).memberLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MEMBER_LOGGING_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MEMBER_LOGGING_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 활성화 됩니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "MemberLoggerDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).memberLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MEMBER_LOGGING_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MEMBER_LOGGING_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 종료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "MemberLoggerChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).memberLoggingEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).memberLoggingChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.MEMBER_LOGGING_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("로깅(유저 관련) 기능이 비활성화 되어있습니다").queue();
                }
            }
            //로깅(서버 관련)
            case "GuildLoggerEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).guildLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.GUILD_LOGGING_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.GUILD_LOGGING_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 활성화 됩니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "GuildLoggerDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).guildLoggingEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.GUILD_LOGGING_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.GUILD_LOGGING_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 종료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "GuildLoggerChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).guildLoggingEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).guildLoggingChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.GUILD_LOGGING_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("로깅(서버 관련) 비활성화 되어있습니다").queue();
                }
            }
            //lewd 명령어
            case "LewdCommandEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).lewdCommandEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LEWD_COMMAND_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LEWD_OUTPUT_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 LEWD 명령어가 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "LewdCommandDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).lewdCommandEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LEWD_COMMAND_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LEWD_OUTPUT_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 LEWD 명령어가 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "LewdLoggerChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).lewdCommandEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).lewdOutputChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LEWD_OUTPUT_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("lewd 명령어가 비활성화 되어있습니다").queue();
                }
            }
            //링크 제한
            case "LinkFilterEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).linkFilterEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LINK_FILTER_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LINK_FILTER_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다. 로그는 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 링크 차단 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "LinkFilterDisable" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).linkFilterEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LINK_FILTER_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LINK_FILTER_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 링크 차단 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "LinkFilterChannelChange" -> {
                if (configPackage.getConfigData(event.getGuild().getId()).linkFilterEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).linkFilterOutputChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.LINK_FILTER_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("링크 차단 기능이 비활성화 되어있습니다").queue();
                }
            }
            //전체 공지 수신
            case "NoticeEnable" -> {
                if(!configPackage.getConfigData(event.getGuild().getId()).noticeEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.NOTICE_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.NOTICE_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 활성화 됩니다. 공지채널은 명령어가 실행된 채널로 지정되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "NoticeDisable" -> {
                if(configPackage.getConfigData(event.getGuild().getId()).noticeEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.NOTICE_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.NOTICE_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("기능이 종료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 해당 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "NoticeChannelChange" -> {
                if(configPackage.getConfigData(event.getGuild().getId()).noticeEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).noticeChannel;
                    if (nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 공지 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.NOTICE_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("공지기능이 비활성화 되어있습니다").queue();
                }
            }
            //말 명령어
            case "SayCommandEnable" -> {
                if(!configPackage.getConfigData(event.getGuild().getId()).sayEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.SAY_ENABLE, true);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.SAY_OUTPUT_CHANNEL, event.getChannelId());
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 말 명령어가 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "SayCommandDisable" -> {
                if(configPackage.getConfigData(event.getGuild().getId()).sayEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.SAY_ENABLE, false);
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.SAY_OUTPUT_CHANNEL, "0");
                        commandManager.updateCommand(event.getGuild().getId());
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 말 명령어가 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "SayCommandChannelChange" -> {
                if(configPackage.getConfigData(event.getGuild().getId()).sayEnable) {
                    String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).sayOutputChannel;
                    if(nowChannelId.equals(event.getChannelId())) {
                        event.getMessage().delete().queue();
                        event.reply("이미 현재 채널이 로그 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } else {
                        try {
                            configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.SAY_OUTPUT_CHANNEL, event.getChannelId());
                            event.getMessage().delete().queue();
                            event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                        } catch (SQLException e) {
                            event.getMessage().delete().queue();
                            event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                        }
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("lewd 명령어가 비활성화 되어있습니다").queue();
                }
            }
        }
    }
}
