package me.kirito5572.listeners.main;

import me.kirito5572.objects.main.GetAirLocalData;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class AirCommandListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("지역별 공기질 측정 명령어 기능부 준비 완료");
    }

    private final Logger logger = LoggerFactory.getLogger(AirCommandListener.class);
    private final String[] cityList = new String[]{"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if(event.getComponentId().equals("광역시도")) {
            for (String city : cityList) {
                if (event.getValues().get(0).equals(city)) {
                    event.replyEmbeds(LocalCityInfor(city).build()).setEphemeral(true).queue();
                    break;
                }
            }
        }
    }

    private EmbedBuilder LocalCityInfor(String location) {
        GetAirLocalData airData = new GetAirLocalData();
        GetAirLocalData.AirData data = airData.get_API(location);
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle(location + "의 공기질 측정표");
        builder.addField("측정소 정보", data.stationName, false);
        builder.addField("측정 시간", data.dataTime, false);
        if(data.khalValue != 0) {
            if(data.khalValue == 1) {
                builder.setColor(Color.GREEN);
            } else if(data.khalValue == 2) {
                builder.setColor(Color.WHITE);
            } else if(data.khalValue == 3) {
                builder.setColor(Color.YELLOW);
            } else if(data.khalValue == 4) {
                builder.setColor(Color.RED);
            }
            builder.addField("통합대기환경지수", String.valueOf(data.khalValue), false);
        } else {
            builder.setColor(Color.BLACK);
            builder.addField("통합대기환경지수", "수치 계산 불가(데이터 부족)", false);
        }

        if(data.pm10Grade == 0) {
            builder.addField("미세먼지지수(PM10)", airData.AirDataFlagConvert(data.pm10Flag), false);
        } else {
            builder.addField("미세먼지지수(PM10)(현재/24시간평균)", data.pm10Value + " / " + data.pm10Value24, false);
        }
        if(data.pm25Grade == 0) {
            builder.addField("미세먼지지수(PM2.5)", airData.AirDataFlagConvert(data.pm25Flag), false);
        } else {
            builder.addField("미세먼지지수(PM2.5)(현재/24시간평균)", data.pm25Value + " / " + data.pm25Value24, false);
        }
        if(data.so2Grade == 0) {
            builder.addField("이산화황 지수", airData.AirDataFlagConvert(data.so2Flag), false);
        } else {
            builder.addField("이산화황 지수", String.valueOf(data.so2Value), false);
        }
        if(data.coGrade == 0) {
            builder.addField("일산화질소 지수", airData.AirDataFlagConvert(data.coFlag), false);
        } else {
            builder.addField("일산화질소 지수", String.valueOf(data.coValue), false);
        }
        if(data.o3Grade == 0) {
            builder.addField("오존 지수", airData.AirDataFlagConvert(data.o3Flag), false);
        } else {
            builder.addField("오존 지수", String.valueOf(data.o3Value), false);
        }
        if(data.no2Grade == 0) {
            builder.addField("이산화질소 지수", airData.AirDataFlagConvert(data.no2Grade), false);
        } else {
            builder.addField("이산화질소 지수", String.valueOf(data.no2Value), false);
        }
        builder.setFooter("data.go.kr / airkorea.or.kr 제공");

        return builder;
    }
}
