package com.kirito5572.objects.logger;

import com.kirito5572.objects.main.MySqlConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConfigPackage {
    private final Logger logger = LoggerFactory.getLogger(ConfigPackage.class);
    private final MySqlConnector mySqlConnector;
    private final Map<String, Config_Data> configDataMap = new HashMap<>();
    public static final int GUILD_ID = 1; //TODO 설정 명령어 아직 안만듬
    public static final int MUSIC_ENABLE = 2; //시작시 적용 완료 TODO 중간 업데이트시 적용 필요
    public static final int FILTER_ENABLE = 3; //TODO 필터 기능 아직 안만듬
    public static final int TEXT_LOGGING_ENABLE = 4; //TODO 로그 아직 미구현
    public static final int CHANNEL_LOGGING_ENABLE = 5; //TODO 로그 아직 미구현
    public static final int MEMBER_LOGGING_ENABLE = 6; //TODO 로그 아직 미구현
    public static final int GUILD_LOGGING_ENABLE = 19; //TODO 로그 아직 미구현
    public static final int LEWD_COMMAND_ENABLE = 7; //시작시 적용 완료 TODO 중간 업데이트시 적용 필요
    public static final int LINK_FILTER_ENABLE = 8; //TODO 필터 기능 아직 안만듬
    public static final int NOTICE_ENABLE = 9; //TODO 공지 기능 아직 안만듬
    public static final int SAY_ENABLE = 10; //시작시 적용 완료 TODO 중간 업데이트시 적용 필요
    public static final int FILTER_OUTPUT_CHANNEL = 11; //TODO 필터 기능 아직 안만듬
    public static final int TEXT_LOGGING_CHANNEL = 12; //TODO 로그 아직 미구현
    public static final int CHANNEL_LOGGING_CHANNEL = 13; //TODO 로그 아직 미구현
    public static final int MEMBER_LOGGING_CHANNEL = 14; //TODO 로그 아직 미구현
    public static final int GUILD_LOGGING_CHANNEL = 20;
    public static final int LEWD_OUTPUT_CHANNEL = 15; //TODO 해당되는 명령어 아직 없음
    public static final int LINK_FILTER_CHANNEL = 16; //TODO 필터 기능 아직 안만듬
    public static final int NOTICE_CHANNEL = 17; //TODO 공지 기능 아직 안만듬
    public static final int SAY_OUTPUT_CHANNEL = 18; //TODO 만들어야함!

    public ConfigPackage(MySqlConnector mySqlConnector) {
        this.mySqlConnector = mySqlConnector;
        init();
    }

    private void init() {
        try (ResultSet rs = this.mySqlConnector.Select_Query("SELECT * FROM ritobotv2_general.config;", new int[]{}, new String[]{})){
            while(rs.next()) {
                Config_Data configData = ConfigDataInit(rs);
                configDataMap.put(configData.guildId, configData);
            }
        } catch (SQLException ignored) {
            logger.error("설정이 제대로 로딩되지 않았습니다!");
            return;
        }
        logger.info("모든 설정 로딩 완료!");
    }

    public boolean inviteServer(String guildId) {
        Config_Data configData = new Config_Data(guildId);
        try {
            mySqlConnector.Insert_Query("INSERT INTO ritobotv2_general.config VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    new int[] {
                            this.mySqlConnector.STRING, this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN,
                            this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN,
                            this.mySqlConnector.BOOLEAN, this.mySqlConnector.BOOLEAN, this.mySqlConnector.STRING, this.mySqlConnector.STRING,
                            this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.STRING,
                            this.mySqlConnector.STRING, this.mySqlConnector.STRING, this.mySqlConnector.BOOLEAN, this.mySqlConnector.STRING
                    },
                    new String [] {
                            configData.guildId, (configData.musicEnable) ? "1" : "0", (configData.filterEnable) ? "1" : "0", (configData.textLoggingEnable) ? "1" : "0",
                            (configData.channelLoggingEnable) ? "1" : "0", (configData.memberLoggingEnable) ? "1" : "0",
                            (configData.lewdCommandEnable) ? "1" : "0", (configData.linkFilterEnable) ? "1" : "0",
                            (configData.noticeEnable) ? "1" : "0", (configData.sayEnable) ? "1" : "0", configData.filterOutputChannel, configData.textLoggingChannel,
                            configData.channelLoggingChannel, configData.memberLoggingChannel, configData.lewdOutputChannel, configData.linkFilterOutputChannel,
                            configData.noticeChannel, configData.sayOutputChannel, (configData.guildLoggingEnable) ? "1" : "0", configData.guildLoggingChannel
                    });
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL ERROR ");
            return false;
        }
        return true;
    }

    public void configReload() {
        init();
    }

    public boolean configReload(String guildId) {
        try (ResultSet rs = this.mySqlConnector.Select_Query("SELECT * FROM ritobotv2_general.config WHERE guildId = ?;",
                new int[]{this.mySqlConnector.STRING}, new String[]{guildId})){
            while(rs.next()) {
                configDataMap.replace(guildId, ConfigDataInit(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("특정 서버 설정 재로딩중 에러가 발생했습니다!");
            return false;
        }
        return true;
    }

    private Config_Data ConfigDataInit(ResultSet rs) throws SQLException {
        Config_Data configData = new Config_Data();
        configData.guildId = rs.getString("guildId");
        configData.musicEnable = rs.getString("musicEnable").equals("1");
        configData.filterEnable = rs.getString("FilterEnable").equals("1");
        configData.textLoggingEnable = rs.getString("textLoggingEnable").equals("1");
        configData.channelLoggingEnable = rs.getString("channelLoggingEnable").equals("1");
        configData.memberLoggingEnable = rs.getString("memberLoggingEnable").equals("1");
        configData.guildLoggingEnable = rs.getString("guildLoggingEnable").equals("1");
        configData.lewdCommandEnable = rs.getString("lewdCommandEnable").equals("1");
        configData.linkFilterEnable = rs.getString("linkFilterEnable").equals("1");
        configData.noticeEnable = rs.getString("noticeEnable").equals("1");
        configData.sayEnable = rs.getString("SayEnable").equals("1");
        configData.filterOutputChannel = rs.getString("filterOutputChannel");
        configData.textLoggingChannel = rs.getString("textLoggingChannel");
        configData.channelLoggingChannel = rs.getString("channelLoggingChannel");
        configData.memberLoggingChannel = rs.getString("memberLoggingChannel");
        configData.guildLoggingChannel = rs.getString("guildLoggingChannel");
        configData.lewdOutputChannel = rs.getString("lewdOutputChannel");
        configData.linkFilterOutputChannel = rs.getString("linkFilterOutputChannel");
        configData.noticeChannel = rs.getString("noticeChannel");
        configData.sayOutputChannel = rs.getString("SayOutputChannel");
        return configData;
    }

    public void updateConfig(String guildId, int option, boolean enable) throws SQLException {
        switch (option) {
            case MUSIC_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET musicEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case FILTER_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET FilterEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case TEXT_LOGGING_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET textLoggingEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case CHANNEL_LOGGING_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET channelLoggingEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case MEMBER_LOGGING_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET memberLoggingEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case GUILD_LOGGING_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET guildLoggingEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case LEWD_COMMAND_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET lewdCommandEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case LINK_FILTER_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET linkFilterEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case NOTICE_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET noticeEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            case SAY_ENABLE:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET SayEnable = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{(enable) ? "1" : "0", guildId});
                break;
            default:
                logger.error("특정 명령어 업데이트시 에러가 발생했습니다.");
        }
        configReload();
    }

    public void updateConfig(String guildId, int option, String channelId) throws SQLException {
        switch (option) {
            case FILTER_OUTPUT_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET filterOutputChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case TEXT_LOGGING_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET textLoggingChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case CHANNEL_LOGGING_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET channelLoggingChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case MEMBER_LOGGING_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET memberLoggingChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case GUILD_LOGGING_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET guildLoggingChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case LEWD_OUTPUT_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET lewdOutputChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case LINK_FILTER_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET linkFilterOutputChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case NOTICE_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET noticeChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            case SAY_OUTPUT_CHANNEL:
                this.mySqlConnector.Insert_Query("UPDATE ritobotv2_general.config SET SayOutputChannel = ? WHERE guildId = ?",
                        new int[]{this.mySqlConnector.STRING, this.mySqlConnector.STRING},
                        new String[]{channelId, guildId});
                break;
            default:
                logger.error("특정 명령어 업데이트시 에러가 발생했습니다.");
        }
    }

    public Map<String, Config_Data> getConfigDataMap() {
        return configDataMap;
    }

    public Config_Data getConfigData(String guildId) {
        return configDataMap.get(guildId);
    }

    public static class Config_Data {
        public Config_Data(String guildId) {
            this.guildId = guildId;
        }
        public Config_Data() {

        }
        public String guildId;
        public boolean musicEnable = false;
        public boolean filterEnable = false;
        public boolean textLoggingEnable = false;
        public boolean channelLoggingEnable = false;
        public boolean memberLoggingEnable = false;
        public boolean guildLoggingEnable = false;
        public boolean lewdCommandEnable = false;
        public boolean linkFilterEnable = false;
        public boolean noticeEnable = true;
        public boolean sayEnable = false;
        public String filterOutputChannel = "0";
        public String textLoggingChannel = "0";
        public String channelLoggingChannel = "0";
        public String memberLoggingChannel = "0";
        public String guildLoggingChannel = "0";
        public String lewdOutputChannel = "0";
        public String linkFilterOutputChannel = "0";
        public String noticeChannel = "0";
        public String sayOutputChannel = "0";
    }

}
