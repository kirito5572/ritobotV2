package me.kirito5572.commands.main;


import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WeatherCommand implements ICommand {

    private final String[] cityList = new String[] {
            "서울", "부산", "대구", "인천",
            "광주", "대전", "울산", "경기",
            "강원", "충북", "충남", "전북",
            "전남", "경북", "경남", "제주",
            "세종"};
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
            return;
        }//TODO 예보도 확인가능하도록 기능 추가 할 것
        StringSelectMenu.Builder builder = StringSelectMenu.create("아래_광역시도중_하나를_선택해주세요");
        for(String city : cityList) {
            builder.addOption(city, city, city);
        }
        event.reply("지역을 선택해주세요.")
                .addComponents(
                        ActionRow.of(
                                builder.build()
                        )
                ).setEphemeral(true).queue();
    }
    @Override
    public String getHelp() {
        return "지역의 현재 날씨 또는 예보를 확인합니다. `From openweathermap.org`";
    }

    @Override
    public String getInvoke() {
        return "날씨";
    }
}
