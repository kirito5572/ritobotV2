package com.kirito5572.commands.main;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AirCityInfoCommand implements ICommand {
    //추후 재개발
    private final String[] cityList = new String[]{"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Logger logger = LoggerFactory.getLogger(AirInforCommand.class);
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
            return;
        }

        StringSelectMenu.Builder builder= StringSelectMenu.create("광역시도");
        for(String city : cityList) {
            builder.addOption(city, city, city);
        }
        event.reply("지역을 선택해주세요.")
                .addActionRow(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "광역시도별 공기질 정보를 불러옵니다.(airkorea.or.kr/data.go.kr 제공)";
    }

    @Override
    public String getInvoke() {
        return "공기정보";
    }
}
