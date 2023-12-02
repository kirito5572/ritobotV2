package com.kirito5572.commands.main;

import com.kirito5572.objects.main.ICommand;
import com.kirito5572.objects.main.MySqlConnector;
import com.kirito5572.objects.main.SqliteConnector;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PingCommand implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(PingCommand.class);
    private final MySqlConnector mySqlConnector;
    private final SqliteConnector sqliteConnector;
    public PingCommand(MySqlConnector mySqlConnector, SqliteConnector sqliteConnector) {
        this.mySqlConnector = mySqlConnector;
        this.sqliteConnector = sqliteConnector;
    }
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        long a;
        try {
            a = event.getJDA().getRestPing().submit().get();
        } catch (@NotNull InterruptedException | ExecutionException e) {
            this.logger.warn("에러 발생: PingCommand:30");
            return;
        }
        long b = event.getJDA().getGatewayPing();
        long Start = System.currentTimeMillis();
        long mysqlEnd = System.currentTimeMillis();
        try (ResultSet ignored = mySqlConnector.Select_Query("SELECT * FROM ritobotv2_general.pin", new int[]{}, new String[]{})) {
            mysqlEnd = System.currentTimeMillis();
        } catch (SQLException e){
            this.logger.warn("에러 발생: PingCommand:38");
        }
        String mysqlTimeString;
        long mysqlTime = (mysqlEnd - Start);
        if(mysqlTime < 0) {
            mysqlTimeString = "접속 에러";
        } else if(mysqlTime == 1 || mysqlTime == 0) {
            mysqlTimeString = "<1";
        } else {
            mysqlTimeString = String.valueOf(mysqlTime);
        }
        EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                .setTitle("ping-pong!")
                .addField("API 응답 시간 (UDP/TCP)", b + "ms / " + a + "ms", false)
                .addField("SQL 명령어 처리 시간  (MySQL / sqlite.db)", mysqlTimeString +
                        "ms", false)
                .setFooter("이 메세지는 10초후 자동으로 삭제됩니다");

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
    }

    @Override
    public String getHelp() {
        return "봇과 서버와의 핑을 계산합니다.";
    }

    @Override
    public String getInvoke() {
        return "핑";
    }
}
