package com.kirito5572.listeners.main;

import com.kirito5572.objects.main.getWeather;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                if (event.getValues().get(0).equals(city)) {
                    event.replyEmbeds(LocalCityInfor(city)).setEphemeral(true).queue();
                    break;
                }
            }
        }
    }
    private MessageEmbed LocalCityInfor(String location) {
        String[] list = getWeather.getWeather_list();

        for (String[] s : local) {
            if(s[0].equals(location)) {
                location = s[1];
                break;
            }
        }
        getWeather.get_api(location);
        String[] data = getWeather.getWeather_information();

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle(location + "의 날씨 정보")
                .setFooter("Information from openweathermap.org", "https://openweathermap.org/");
        int j = 0;
        for(int i = 0; i < 10; i++) {

            if(!data[i].equals("null")) {
                builder.addField(
                        (i - j + 1)+ ". " + list[i] + "\n",
                        data[i],
                        false
                );
            } else {
                j++;
            }
        }

        return builder.build();
    }
}
