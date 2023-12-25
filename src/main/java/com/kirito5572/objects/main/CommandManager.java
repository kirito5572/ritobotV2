package com.kirito5572.objects.main;


import com.kirito5572.commands.logger.ConfigCommand;
import com.kirito5572.commands.main.*;
import com.kirito5572.commands.main.game.DiceCommand;
import com.kirito5572.commands.main.game.OddOrEvenCommand;
import com.kirito5572.commands.main.game.RockPaperScissorsCommand;
import com.kirito5572.commands.main.game.RussianRouletteCommand;
import com.kirito5572.commands.main.moderator.*;
import com.kirito5572.commands.main.owneronly.BotIpInfoCommand;
import com.kirito5572.commands.main.owneronly.BotOwnerNoticeCommand;
import com.kirito5572.commands.main.owneronly.EvalCommand;
import com.kirito5572.commands.main.owneronly.GetGuildInfoCommand;
import com.kirito5572.commands.music.*;
import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.logger.LoggerPackage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class CommandManager {
    private final MySqlConnector mySqlConnector;
    private final ConfigPackage configPackage;
    private final SqliteConnector sqliteConnector;
    private final LoggerPackage loggerPackage;
    private final GoogleAPI googleAPI;
    private final JDA jda;
    private final Map<String, SlashCommandData> commands = new HashMap<>();
    private final Map<String, SlashCommandData> musicCommands = new HashMap<>();
    private final Map<String, ICommand> commandHandle = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(CommandManager.class);


    public CommandManager(JDA jda, MySqlConnector mySqlConnector, SqliteConnector sqliteConnector, GoogleAPI googleAPI, ConfigPackage configPackage, LoggerPackage loggerPackage) {
        this.jda = jda;
        this.mySqlConnector = mySqlConnector;
        this.sqliteConnector = sqliteConnector;
        this.googleAPI = googleAPI;
        this.configPackage = configPackage;
        this.loggerPackage = loggerPackage;
        addCommand();
        updateCommand();

    }

    private void addCommand() {
        ICommand command = new PingCommand(mySqlConnector, sqliteConnector);
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp()));
        this.commandHandle.put(command.getInvoke(), command);

        command = new BanCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .setGuildOnly(true)
                .addOption(OptionType.USER, "유저", "밴 할 유저", true)
                .addOption(OptionType.STRING, "사유", "밴 하는 사유"));
        this.commandHandle.put(command.getInvoke(), command);

        command = new BotIpInfoCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
        this.commandHandle.put(command.getInvoke(), command);

        command = new ClearCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
                .setGuildOnly(true)
                .addOption(OptionType.INTEGER, "수량", "삭제할 메세지 수량", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new DiceCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setGuildOnly(true)
                .addOption(OptionType.INTEGER, "주사위면수", "던질 주사위의 면수(기본값 6)")
                .addOption(OptionType.INTEGER, "주사위횟수", "주사위를 던질 횟수(기본값 1)"));
        this.commandHandle.put(command.getInvoke(), command);

        command = new OddOrEvenCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setGuildOnly(true)
                .addOption(OptionType.STRING, "홀짝", "홀수 또는 짝수를 입력해주세요", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new RockPaperScissorsCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setGuildOnly(true)
                .addOption(OptionType.STRING, "가위바위보", "가위/바위/보 중 하나를 입력해주세요", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new RussianRouletteCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setGuildOnly(true)
                .addOption(OptionType.STRING, "재장전", "재장전을 입력하면 재장전됩니다."));
        this.commandHandle.put(command.getInvoke(), command);

        command = new KickCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS))
                .setGuildOnly(true)
                .addOption(OptionType.USER, "유저", "추방 할 유저", true)
                .addOption(OptionType.STRING, "사유", "추방 하는 사유"));
        this.commandHandle.put(command.getInvoke(), command);

        command = new MemberCountCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
                .setGuildOnly(true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new MessagePinCommand(this.mySqlConnector);
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
                .addOption(OptionType.STRING, "내용", "고정할 내용을 입력해주세요(없는 경우 기존 핀이 해제됩니다)")
                .setGuildOnly(true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new UnBanCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .setGuildOnly(true)
                .addOption(OptionType.USER, "유저", "언밴 할 유저", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new GetGuildInfoCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
        this.commandHandle.put(command.getInvoke(), command);

        command = new EvalCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.STRING, "실행코드", "실행 할 코드", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new SayCommand(loggerPackage);
        this.commandHandle.put(command.getInvoke(), command);

        command = new CatCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .addOption(OptionType.INTEGER, "수량", "실행 횟수(최대 10회, 미입력시 1회)"));
        this.commandHandle.put(command.getInvoke(), command);

        command = new NaverCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp()));
        this.commandHandle.put(command.getInvoke(), command);

        command = new ServerInfoCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                .setGuildOnly(true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new BotInfoCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp()));
        this.commandHandle.put(command.getInvoke(), command);

        command = new UserInfoCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .addOption(OptionType.USER, "유저", "검색 할 유저", true));
        this.commandHandle.put(command.getInvoke(), command);

        command = new VendingMachineCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp()));
        this.commandHandle.put(command.getInvoke(), command);

        command = new WeatherCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp()));
        this.commandHandle.put(command.getInvoke(), command);

        command = new ConfigCommand();
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL, Permission.MANAGE_ROLES, Permission.MESSAGE_MANAGE, Permission.ADMINISTRATOR)));
        this.commandHandle.put(command.getInvoke(), command);

        //music Commands
        {
            command = new JoinCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new LeaveCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new NowPlayingCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new PauseCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new PlayCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true)
                    .addOption(OptionType.STRING, "url", "재생할 음악의 URL", true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new QueueCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true)
                    .addOption(OptionType.INTEGER, "재생목록회차", "최대 20개씩 나눠서 출력, 출력할 회차를 입력, 미입력시 제일 앞부터 출력"));
            this.commandHandle.put(command.getInvoke(), command);

            command = new QueueDetectCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true)
                    .addOption(OptionType.INTEGER, "삭제할_수량", "재생목록에서 삭제할 수량, 미입력시 전체 초기화"));
            this.commandHandle.put(command.getInvoke(), command);

            command = new QueueMixCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new SearchCommand(googleAPI);
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true)
                    .addOption(OptionType.STRING, "검색", "검색할 내용", true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new SkipCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new StopClearCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new StopCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true));
            this.commandHandle.put(command.getInvoke(), command);

            command = new VolumeCommand();
            this.musicCommands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_CONNECT))
                    .setGuildOnly(true)
                    .addOption(OptionType.INTEGER, "볼륨", "10~100사이로 입력", true));
            this.commandHandle.put(command.getInvoke(), command);
        }
        command = new BotOwnerNoticeCommand(configPackage);
        this.commands.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.USER, "title", "공지 제목", true)
                .addOption(OptionType.USER, "main", "공지 내용", true)
                .addOption(OptionType.USER, "footer", "주석", false));
        this.commandHandle.put(command.getInvoke(), command);
    }

    private void updateCommand() {
        logger.info("명령어 로딩중");
        this.jda.updateCommands().addCommands(commands.values()).complete();
        for(Guild guild : this.jda.getGuilds()) {
            updateCommand(guild.getId());
        }
        logger.info("명령어 셋팅 완료");
    }

    public void updateCommand(String guildId) {
        Guild guild = this.jda.getGuildById(guildId);
        Map<String, SlashCommandData> data = new HashMap<>();
        ConfigPackage.Config_Data configData = configPackage.getConfigData(guildId);
        if(configData.musicEnable) {
            data.putAll(musicCommands);
        }
        if(configData.lewdCommandEnable) {
            //TODO 나중에 생기면 만들기
        }
        if(configData.sayEnable) {
            ICommand command = new SayCommand(loggerPackage);
            data.put(command.getInvoke(), Commands.slash(command.getInvoke(), command.getHelp())
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                    .setGuildOnly(true)
                    .addOption(OptionType.STRING, "말", "입력 할 말", true));
        }
        if(guild != null) {
            guild.updateCommands().addCommands(data.values()).queue();
        }
    }



    public void handleCommand(SlashCommandInteractionEvent command) {
        if(this.commandHandle.containsKey(command.getName())) {
            this.commandHandle.get(command.getName()).handle(command);
        } else {
            command.getChannel().sendMessage("해당 명령어를 사용할 수 없습니다.").queue();
        }
    }
}
