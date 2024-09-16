package me.kirito5572.listeners.main;

import me.kirito5572.objects.main.getAirLocalData;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        getAirLocalData airData = new getAirLocalData();
        airData.get_API(location);
        String[] data = airData.getAirkorea_data();
        String[] air_list = airData.getItemCode();
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                .setTitle(location + "의 공기질 측정표");
        builder.addField(
                "1. " + air_list[6] + "\n",
                data[6],
                false
        );
        for (int i = 0; i < 6; i++) {
            builder.addField(
                    String.format(
                            i + ". %s\n",
                            air_list[i]),
                    String.format(
                            "%s \n",
                            data[i]),
                    false
            );
        }
        return builder;
    }
}
