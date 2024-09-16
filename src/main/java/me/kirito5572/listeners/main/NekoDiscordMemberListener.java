package me.kirito5572.listeners.main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class NekoDiscordMemberListener extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(NekoDiscordMemberListener.class);

    private final String[] nekoDiscordTeamRoleId = {
            "439828127133466634", "439828232779333632", "439828312328765460",  // 지휘 / 정보 / 교육
            "439828408684380180", "439828528758915073", "439828616814133249",  // 안전 / 중앙 / 징계
            "439828703661391873", "439828771059531796", "439830502334136320",  // 복지 / 기록 / 추출
            "619921319533740032"                                               // 설계
    };
    private final Random random = new Random();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("네코샘플 디스코드 특별 기능부 준비 완료");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild()) {
            return;
        }
        Guild guild = event.getGuild();
        String nekoDiscordServerId = "439780696999985172";
        if(guild.getId().equals(nekoDiscordServerId)) {
            if(event.getMessage().getContentRaw().startsWith("!join")) {
                Role role = guild.getRoleById(nekoDiscordTeamRoleId[random.nextInt(10)]);
                assert role != null;
                guild.addRoleToMember(event.getAuthor(), role).queue(voids ->{
                    try {
                        event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("랜덤으로 팀이 배정되었습니다. 배정된 팀은 **\"" + role.getName() + "\"**").queue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        }
    }
}
