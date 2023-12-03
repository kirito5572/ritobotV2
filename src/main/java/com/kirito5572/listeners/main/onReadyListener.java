package com.kirito5572.listeners.main;

import com.kirito5572.App;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.Presence;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class onReadyListener extends ListenerAdapter {
    private int i = 0;

    private static final Logger logger = LoggerFactory.getLogger(onReadyListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        if(App.appMode == App.APP_ALPHA) {
            Objects.requireNonNull(event.getJDA().getPresence()).setActivity(Activity.watching("알파 테스트에 오신것을 환영합니다."));
        } else if(App.appMode == App.APP_BETA) {
            autoActivityChangeModule(event);
        } else if(App.appMode == App.APP_STABLE) {
            autoActivityChangeModule(event);
        }

        logger.info("상태 변경 기능부 준비 완료");
    }

    private void autoActivityChangeModule(@NotNull ReadyEvent event) {
        JDA jda = event.getJDA();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Presence presence = jda.getPresence();
                switch (i) {
                    case 0,2,4,6,8 -> {
                        if(App.appMode == App.APP_STABLE) {
                            presence.setActivity(Activity.watching("리토봇 V2"));
                        } else if(App.appMode == App.APP_BETA) {
                            presence.setActivity(Activity.watching("리토봇 V2 베타"));
                        }
                    }
                    case 1 -> presence.setActivity(Activity.listening(App.getVersion()));
                    case 3 -> {
                        if(App.appMode == App.APP_STABLE) {
                            presence.setActivity(Activity.playing("오류 발생시 제작자에게 문의해주세요."));
                        } else if(App.appMode == App.APP_BETA) {
                            presence.setActivity(Activity.competing("베타 소프트웨어로 불안정 할 수 있습니다"));
                        }
                    }
                    case 5 -> presence.setActivity(Activity.streaming("kirito5572 제작","https://github.com/kirito5572"));
                    case 7 -> presence.setActivity(Activity.competing("베타 소프트웨어로 불안정 할 수 있습니다"));
                    case 9 -> presence.setActivity(Activity.playing("오류 발생시 제작자에게 문의해주세요."));
                }
                i++;
                if(App.appMode == App.APP_STABLE) {
                    if (i > 5) {
                        i = 0;
                    }
                } else if(App.appMode == App.APP_BETA) {
                    if (i > 9) {
                        i = 0;
                    }
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 7000);
    }
}
