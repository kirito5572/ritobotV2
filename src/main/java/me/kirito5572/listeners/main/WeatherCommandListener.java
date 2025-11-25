package me.kirito5572.listeners.main;

import me.kirito5572.objects.main.GetWeather;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class WeatherCommandListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WeatherCommandListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("날씨 명령어 기능부 준비 완료");
    }

    private static final String[][] local = new String[][]{
            {"서울", "seoul"}, {"부산", "busan"}, {"대구", "daegu"}, {"인천", "incheon"},
            {"광주", "gwangju"}, {"대전", "daejeon"}, {"울산", "ulsan"}, {"경기", "gyeonggi"},
            {"강원", "gangwon"}, {"충북", "chungbuk"}, {"충남", "chungnam"}, {"전북", "jeonbuk"},
            {"전남", "jeonnam"}, {"경북", "gyeongbuk"}, {"경남", "gyeongnam"}, {"제주", "jeju"},
            {"세종", "sejong"}};
    private final String[] cityList = new String[] {
            "서울", "부산", "대구", "인천",
            "광주", "대전", "울산", "경기",
            "강원", "충북", "충남", "전북",
            "전남", "경북", "경남", "제주",
            "세종"};

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if(event.getComponentId().equals("아래_광역시도중_하나를_선택해주세요")) {
            for (String city : cityList) {
                if (event.getValues().getFirst().equals(city)) {
                    try {
                        event.getMessage().delete().queue();
                        event.replyEmbeds(LocalCityInfor(city)).setEphemeral(true).queue();
                        break;
                    } catch (NullPointerException | URISyntaxException | IOException e) {
                        event.reply("처리중 에러가 발생했습니다.").setEphemeral(true).queue(m -> m.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                }
            }
        }
    }

    @NotNull
    private MessageEmbed LocalCityInfor(String location) throws NullPointerException, URISyntaxException, IOException {
        String locationEng = null;
        for (String[] s : local) {
            if(s[0].equals(location)) {
                locationEng = s[1];
                break;
            }
        }
        if(locationEng == null) {
            throw new NullPointerException();
        }
        GetWeather getWeather = new GetWeather();
        GetWeather.WeatherInformation weatherInformation = getWeather.get_api(locationEng);
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle(location + "의 날씨 정보")
                .setFooter("Information from openweathermap.org", "https://openweathermap.org/");
        builder.addField("기준 시간", getWeather.formatDate(weatherInformation.ForecastTime),true)
                .addField("현재 날씨", weatherInformation.weatherCord, true)
                .addField("현재 온도", weatherInformation.temp + "Cº", true)
                .addField("체감 온도", weatherInformation.tempFeels + "Cº", true)
                .addField("대기압", weatherInformation.pressure + "hPa", true)
                .addField("습도", weatherInformation.humidity + "%", true)
                .addField("풍속", weatherInformation.windSpeed + "m/s", true)
                .addField("풍향", weatherInformation.windDeg + "º", true)
                .addField("일출시간", getWeather.formatDate(weatherInformation.sunRise), false)
                .addField("일몰시간", getWeather.formatDate(weatherInformation.sunSet), false);
        if(weatherInformation.rain_3hr != 0) {
            builder.addField("3시간 강수량", weatherInformation.rain_3hr + "mm", true);
        }
        if(weatherInformation.snow_3hr != 0) {
            builder.addField("3시간 적설량", weatherInformation.snow_3hr + "cm", true);
        }
        return builder.build();
    }
}
