package me.kirito5572.commands.main;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AirInforCommand implements ICommand {

    //나중에 구현하기 5055개 읍면동은 에바다....ㅠㅠ
    private final String[] cityList = new String[]{"A"};

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Logger logger = LoggerFactory.getLogger(AirInforCommand.class);
        String[] rank = new String[8];
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
            return;
        }

        StringSelectMenu.Builder builder= StringSelectMenu.create("광역시도");
        for(String city : cityList) {

        }
        event.reply("지역을 선택해주세요.")
                .addActionRow();
    }

    @Override
    public String getHelp() {
        return "선택된 지역의 미세먼지값을 받아옵니다(airkorea.or.kr/data.go.kr 제공)";
    }

    @Override
    public String getInvoke() {
        return "공기정보상세";
    }
}
