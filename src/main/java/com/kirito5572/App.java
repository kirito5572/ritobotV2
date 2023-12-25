package com.kirito5572;

import com.kirito5572.listeners.logger.ConfigListener;
import com.kirito5572.listeners.logger.LoggerListener;
import com.kirito5572.listeners.main.*;
import com.kirito5572.objects.logger.AWSConnector;
import com.kirito5572.objects.logger.ConfigPackage;
import com.kirito5572.objects.logger.LoggerPackage;
import com.kirito5572.objects.main.*;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Date date;
    private static String TOKEN;
    private final static @NotNull String OSStringData = System.getProperty("os.name").toLowerCase();
    public static int OS = 3;
    public final static int WINDOWS = 0;
    public final static int MAC = 1;
    public final static int UNIX = 2;
    public final static int UNSUPPORTED = 3;
    public final static int APP_STABLE = 0;
    public final static int APP_BETA = 1;
    public  final static int APP_ALPHA = 2;
    public static int appMode = 3;
    @SuppressWarnings("unused")
    private final static int APP_UNKNOWN = 3;
    private static @NotNull String version = "";
    private static @NotNull String build_time = "";
    private static @NotNull String build_os = "";
    private static @NotNull String build_jdk = "";

    public static void main(String[] args) throws SQLException, URISyntaxException, ClassNotFoundException, InterruptedException {
        App app = new App();
        app.start();
    }

    public App() {
        logger.info("                                                                                                                                                                       ");
        logger.info("                                                                 bbbbbbbb                                                                                              ");
        logger.info("                     iiii          tttt                          b::::::b                                      tttt     VVVVVVVV           VVVVVVVV 222222222222222    ");
        logger.info("                    i::::i      ttt:::t                          b::::::b                                   ttt:::t     V::::::V           V::::::V2:::::::::::::::22  ");
        logger.info("                     iiii       t:::::t                          b::::::b                                   t:::::t     V::::::V           V::::::V2::::::222222:::::2 ");
        logger.info("                                t:::::t                           b:::::b                                   t:::::t     V::::::V           V::::::V2222222     2:::::2 ");
        logger.info("rrrrr   rrrrrrrrr  iiiiiiittttttt:::::ttttttt       ooooooooooo   b:::::bbbbbbbbb       ooooooooooo   ttttttt:::::tttttttV:::::V           V:::::V             2:::::2 ");
        logger.info("r::::rrr:::::::::r i:::::it:::::::::::::::::t     oo:::::::::::oo b::::::::::::::bb   oo:::::::::::oo t:::::::::::::::::t V:::::V         V:::::V              2:::::2 ");
        logger.info("r:::::::::::::::::r i::::it:::::::::::::::::t    o:::::::::::::::ob::::::::::::::::b o:::::::::::::::ot:::::::::::::::::t  V:::::V       V:::::V            2222::::2  ");
        logger.info("rr::::::rrrrr::::::ri::::itttttt:::::::tttttt    o:::::ooooo:::::ob:::::bbbbb:::::::bo:::::ooooo:::::otttttt:::::::tttttt   V:::::V     V:::::V        22222::::::22   ");
        logger.info(" r:::::r     r:::::ri::::i      t:::::t          o::::o     o::::ob:::::b    b::::::bo::::o     o::::o      t:::::t          V:::::V   V:::::V       22::::::::222     ");
        logger.info(" r:::::r     rrrrrrri::::i      t:::::t          o::::o     o::::ob:::::b     b:::::bo::::o     o::::o      t:::::t           V:::::V V:::::V       2:::::22222        ");
        logger.info(" r:::::r            i::::i      t:::::t          o::::o     o::::ob:::::b     b:::::bo::::o     o::::o      t:::::t            V:::::V:::::V       2:::::2             ");
        logger.info(" r:::::r            i::::i      t:::::t    tttttto::::o     o::::ob:::::b     b:::::bo::::o     o::::o      t:::::t    tttttt   V:::::::::V        2:::::2             ");
        logger.info(" r:::::r           i::::::i     t::::::tttt:::::to:::::ooooo:::::ob:::::bbbbbb::::::bo:::::ooooo:::::o      t::::::tttt:::::t    V:::::::V         2:::::2       222222");
        logger.info(" r:::::r           i::::::i     tt::::::::::::::to:::::::::::::::ob::::::::::::::::b o:::::::::::::::o      tt::::::::::::::t     V:::::V          2::::::2222222:::::2");
        logger.info(" r:::::r           i::::::i       tt:::::::::::tt oo:::::::::::oo b:::::::::::::::b   oo:::::::::::oo         tt:::::::::::tt      V:::V           2::::::::::::::::::2");
        logger.info(" rrrrrrr           iiiiiiii         ttttttttttt     ooooooooooo   bbbbbbbbbbbbbbbb      ooooooooooo             ttttttttttt         VVV            22222222222222222222");
        date = new Date();
        if (OSStringData.contains("win")) {
            OS = WINDOWS;
        } else if (OSStringData.contains("mac")) {
            OS = MAC;
        } else if (OSStringData.contains("nix") || OSStringData.contains("nux") || OSStringData.contains("aix")) {
            OS = UNIX;
        } else {
            OS = UNSUPPORTED;
        }
        logger.info("OS: " + OSStringData);

        try {
            String location = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getAbsolutePath();
            try (JarFile file = new JarFile(location)) {
                Attributes attribute = file.getManifest().getMainAttributes();
                version = attribute.getValue("Version");
                build_time = attribute.getValue("BuildDate");
                build_os = attribute.getValue("BuildOS");
                build_jdk = attribute.getValue("BuildJDK");
            }
        } catch (@NotNull URISyntaxException | IOException e) {
            version = "beta version";
            build_time = "beta";
            build_os = "windows 11";
            build_jdk = "JAVA 17";
        }
        System.out.println(getVersion());
        if (getVersion().contains("STABLE") || getVersion().contains("stable")) {
            appMode = APP_STABLE;
            logger.info("   SSSSSSSSSSSSSSS TTTTTTTTTTTTTTTTTTTTTTT         AAA               BBBBBBBBBBBBBBBBB   LLLLLLLLLLL             EEEEEEEEEEEEEEEEEEEEEE");
            logger.info(" SS:::::::::::::::ST:::::::::::::::::::::T        A:::A              B::::::::::::::::B  L:::::::::L             E::::::::::::::::::::E");
            logger.info("S:::::SSSSSS::::::ST:::::::::::::::::::::T       A:::::A             B::::::BBBBBB:::::B L:::::::::L             E::::::::::::::::::::E");
            logger.info("S:::::S     SSSSSSST:::::TT:::::::TT:::::T      A:::::::A            BB:::::B     B:::::BLL:::::::LL             EE::::::EEEEEEEEE::::E");
            logger.info("S:::::S            TTTTTT  T:::::T  TTTTTT     A:::::::::A             B::::B     B:::::B  L:::::L                 E:::::E       EEEEEE");
            logger.info("S:::::S                    T:::::T            A:::::A:::::A            B::::B     B:::::B  L:::::L                 E:::::E             ");
            logger.info(" S::::SSSS                 T:::::T           A:::::A A:::::A           B::::BBBBBB:::::B   L:::::L                 E::::::EEEEEEEEEE   ");
            logger.info("  SS::::::SSSSS            T:::::T          A:::::A   A:::::A          B:::::::::::::BB    L:::::L                 E:::::::::::::::E   ");
            logger.info("    SSS::::::::SS          T:::::T         A:::::A     A:::::A         B::::BBBBBB:::::B   L:::::L                 E:::::::::::::::E   ");
            logger.info("       SSSSSS::::S         T:::::T        A:::::AAAAAAAAA:::::A        B::::B     B:::::B  L:::::L                 E::::::EEEEEEEEEE   ");
            logger.info("            S:::::S        T:::::T       A:::::::::::::::::::::A       B::::B     B:::::B  L:::::L                 E:::::E             ");
            logger.info("            S:::::S        T:::::T      A:::::AAAAAAAAAAAAA:::::A      B::::B     B:::::B  L:::::L         LLLLLL  E:::::E       EEEEEE");
            logger.info("SSSSSSS     S:::::S      TT:::::::TT   A:::::A             A:::::A   BB:::::BBBBBB::::::BLL:::::::LLLLLLLLL:::::LEE::::::EEEEEEEE:::::E");
            logger.info("S::::::SSSSSS:::::S      T:::::::::T  A:::::A               A:::::A  B:::::::::::::::::B L::::::::::::::::::::::LE::::::::::::::::::::E");
            logger.info("S:::::::::::::::SS       T:::::::::T A:::::A                 A:::::A B::::::::::::::::B  L::::::::::::::::::::::LE::::::::::::::::::::E");
            logger.info(" SSSSSSSSSSSSSSS         TTTTTTTTTTTAAAAAAA                   AAAAAAABBBBBBBBBBBBBBBBB   LLLLLLLLLLLLLLLLLLLLLLLLEEEEEEEEEEEEEEEEEEEEEE");
            logger.info("program version: " + getVersion());
        } else if (getVersion().contains("BETA") || getVersion().contains("beta")) {
            appMode = APP_BETA;
            logger.warn("BBBBBBBBBBBBBBBBB   EEEEEEEEEEEEEEEEEEEEEETTTTTTTTTTTTTTTTTTTTTTT         AAA               !!! ");
            logger.warn("B::::::::::::::::B  E::::::::::::::::::::ET:::::::::::::::::::::T        A:::A             !!:!!");
            logger.warn("B::::::BBBBBB:::::B E::::::::::::::::::::ET:::::::::::::::::::::T       A:::::A            !:::!");
            logger.warn("BB:::::B     B:::::BEE::::::EEEEEEEEE::::ET:::::TT:::::::TT:::::T      A:::::::A           !:::!");
            logger.warn(  "B::::B     B:::::B  E:::::E       EEEEEETTTTTT  T:::::T  TTTTTT     A:::::::::A          !:::!");
            logger.warn("  B::::B     B:::::B  E:::::E                     T:::::T            A:::::A:::::A         !:::!");
            logger.warn("  B::::BBBBBB:::::B   E::::::EEEEEEEEEE           T:::::T           A:::::A A:::::A        !:::!");
            logger.warn("  B:::::::::::::BB    E:::::::::::::::E           T:::::T          A:::::A   A:::::A       !:::!");
            logger.warn("  B::::BBBBBB:::::B   E:::::::::::::::E           T:::::T         A:::::A     A:::::A      !:::!");
            logger.warn("  B::::B     B:::::B  E::::::EEEEEEEEEE           T:::::T        A:::::AAAAAAAAA:::::A     !:::!");
            logger.warn("  B::::B     B:::::B  E:::::E                     T:::::T       A:::::::::::::::::::::A    !!:!!");
            logger.warn("  B::::B     B:::::B  E:::::E       EEEEEE        T:::::T      A:::::AAAAAAAAAAAAA:::::A    !!! ");
            logger.warn("BB:::::BBBBBB::::::BEE::::::EEEEEEEE:::::E      TT:::::::TT   A:::::A             A:::::A       ");
            logger.warn("B:::::::::::::::::B E::::::::::::::::::::E      T:::::::::T  A:::::A               A:::::A  !!! ");
            logger.warn("B::::::::::::::::B  E::::::::::::::::::::E      T:::::::::T A:::::A                 A:::::A!!:!!");
            logger.warn("BBBBBBBBBBBBBBBBB   EEEEEEEEEEEEEEEEEEEEEE      TTTTTTTTTTTAAAAAAA                   AAAAAAA!!! ");
            logger.warn("beta program version: " + getVersion());
        } else if (getVersion().contains("ALPHA") || getVersion().contains("alpha")) {
            appMode = APP_ALPHA;
            logger.error("               AAA               LLLLLLLLLLL             PPPPPPPPPPPPPPPPP   HHHHHHHHH     HHHHHHHHH               AAA               !!! ");
            logger.error("              A:::A              L:::::::::L             P::::::::::::::::P  H:::::::H     H:::::::H              A:::A             !!:!!");
            logger.error("             A:::::A             L:::::::::L             P::::::PPPPPP:::::P H:::::::H     H:::::::H             A:::::A            !:::!");
            logger.error("            A:::::::A            LL:::::::LL             PP:::::P     P:::::PHH::::::H     H::::::HH            A:::::::A           !:::!");
            logger.error("           A:::::::::A             L:::::L                 P::::P     P:::::P  H:::::H     H:::::H             A:::::::::A          !:::!");
            logger.error("          A:::::A:::::A            L:::::L                 P::::P     P:::::P  H:::::H     H:::::H            A:::::A:::::A         !:::!");
            logger.error("         A:::::A A:::::A           L:::::L                 P::::PPPPPP:::::P   H::::::HHHHH::::::H           A:::::A A:::::A        !:::!");
            logger.error("        A:::::A   A:::::A          L:::::L                 P:::::::::::::PP    H:::::::::::::::::H          A:::::A   A:::::A       !:::!");
            logger.error("       A:::::A     A:::::A         L:::::L                 P::::PPPPPPPPP      H:::::::::::::::::H         A:::::A     A:::::A      !:::!");
            logger.error("      A:::::AAAAAAAAA:::::A        L:::::L                 P::::P              H::::::HHHHH::::::H        A:::::AAAAAAAAA:::::A     !:::!");
            logger.error("     A:::::::::::::::::::::A       L:::::L                 P::::P              H:::::H     H:::::H       A:::::::::::::::::::::A    !!:!!");
            logger.error("    A:::::AAAAAAAAAAAAA:::::A      L:::::L         LLLLLL  P::::P              H:::::H     H:::::H      A:::::AAAAAAAAAAAAA:::::A    !!! ");
            logger.error("   A:::::A             A:::::A   LL:::::::LLLLLLLLL:::::LPP::::::PP          HH::::::H     H::::::HH   A:::::A             A:::::A       ");
            logger.error("  A:::::A               A:::::A  L::::::::::::::::::::::LP::::::::P          H:::::::H     H:::::::H  A:::::A               A:::::A  !!! ");
            logger.error(" A:::::A                 A:::::A L::::::::::::::::::::::LP::::::::P          H:::::::H     H:::::::H A:::::A                 A:::::A!!:!!");
            logger.error("AAAAAAA                   AAAAAAALLLLLLLLLLLLLLLLLLLLLLLLPPPPPPPPPP          HHHHHHHHH     HHHHHHHHHAAAAAAA                   AAAAAAA!!! ");
        } else {
            logger.error("unknown program version, shutdown program");
            System.exit(-1);
        }
        switch (appMode) {
            case APP_STABLE -> TOKEN = openFileData("TOKEN");
            case APP_BETA -> TOKEN = openFileData("BETA_TOKEN");
            case APP_ALPHA -> TOKEN = openFileData("ALPHA_TOKEN");
        }
    }

    private void start() throws SQLException, ClassNotFoundException, URISyntaxException, InterruptedException {
        WebUtils.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) JDA/4.4.0_352");
        logger.info("SQL 서버/파일 접속중");
        MySqlConnector mySqlConnector = new MySqlConnector();
        SqliteConnector sqliteConnector = new SqliteConnector(mySqlConnector);
        logger.info("접속 완료...");


        //FilterSystem

        //loading Objects
        ConfigPackage configPackage = new ConfigPackage(mySqlConnector);
        LoggerPackage loggerPackage = new LoggerPackage(mySqlConnector, configPackage);
        AWSConnector awsConnector = new AWSConnector();
        GoogleAPI googleAPI = new GoogleAPI(openFileData("YOUTUBE_DATA_API_KEY"));

        logger.info("봇 시작중... 디스코드로 부터의 응답 대기중");

        JDA jda = JDABuilder.createDefault(TOKEN)
                .setAutoReconnect(true)
                .setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        logger.info("서버 연결 성공, 개별 기능부 실행");
        //CommandManager
        CommandManager manager = new CommandManager(jda, mySqlConnector, sqliteConnector, googleAPI, configPackage, loggerPackage);
        //Listeners
        onReadyListener onReadyListener = new onReadyListener();
        CommandListener listener = new CommandListener(manager);
        MemberCountListener memberCountListener = new MemberCountListener();
        MessagePinListener messagePinListener = new MessagePinListener(mySqlConnector);
        AirCommandListener airCommandListener = new AirCommandListener();
        WeatherCommandListener weatherCommandListener = new WeatherCommandListener();
        LoggerListener loggerListener = new LoggerListener(loggerPackage, configPackage, mySqlConnector, awsConnector);
        ConfigListener configListener = new ConfigListener(loggerPackage, configPackage, mySqlConnector, manager);
        NekoDiscordMemberListener nekoDiscordMemberListener = new NekoDiscordMemberListener();
        LinkFilterListener linkFilterListener = new LinkFilterListener(configPackage);
        logger.info("개별 기능부 정상 시작, 연결 대기중....");

        jda.addEventListener(onReadyListener, listener, memberCountListener, messagePinListener,
                airCommandListener, weatherCommandListener, loggerListener, configListener,
                nekoDiscordMemberListener, linkFilterListener);

        jda.awaitReady();

        logger.info("연결 완료, 부팅 완료");
    }

    public static @NotNull String openFileData(@NotNull String Data) {
        StringBuilder reader = new StringBuilder();
        try {
            File file = getFile(Data);
            try (FileReader fileReader = new FileReader(file)) {
                int signalCh;
                while ((signalCh = fileReader.read()) != -1) {
                    reader.append((char) signalCh);
                }
            }
        } catch (Exception e) {
            logger.error("예외 발생:\n" + e);
            System.exit(-1);
        }
        return reader.toString();
    }

    @NotNull
    private static File getFile(@NotNull String Data) throws UnsupportedOSException {
        String sep = File.separator;
        String path;
        path = switch (OS) {
            case WINDOWS -> "C:" + sep + "DiscordServerBotSecrets" + sep + "ritobotV2" + sep + Data + ".txt";
            case MAC -> sep + "etc" + sep + "DiscordServerBotSecrets" + sep + "ritobotV2" + sep + Data + ".txt";
            case UNIX -> sep + "home" + sep + "DiscordServerBotSecrets" + sep + "ritobotV2" + sep + Data + ".txt";
            default -> throw new UnsupportedOSException("이 운영체제는 지원하지 않습니다.");
        };
        return new File(path);
    }

    public static @NotNull String getVersion() {
        return version;
    }

    /**
     * return discord bot build time
     *
     * @return the build time of discord bot
     */

    public static @NotNull String getBuild_time() {
        return build_time;
    }

    /**
     * return discord bot build os
     *
     * @return the build os of discord bot
     */

    @SuppressWarnings("unused")
    public static @NotNull String getBuild_os() {
        return build_os;
    }

    /**
     * return discord bot build jdk
     *
     * @return the build jdk of discord bot
     */

    public static @NotNull String getBuild_jdk() {
        return build_jdk;
    }

    /**
     * return discord bot start-up time
     *
     * @return the start-up time of discord bot
     */

    public static Date getDate() {
        return date;
    }

}
