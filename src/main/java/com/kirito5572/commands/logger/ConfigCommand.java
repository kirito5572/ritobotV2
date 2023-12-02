package com.kirito5572.commands.logger;

import com.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConfigCommand implements ICommand {

    private static final Logger logger = LoggerFactory.getLogger(ConfigCommand.class);

    private final String[][] optionList = new String[][]{
            {"음악" , "config1" ,"음악 기능 활성화 여부 선택"},
            {"메세지 필터링", "config2" , "메세지 필터링 활성화 여부 선택"},
            {"로깅(메세지 관련)", "config3", "메세지 관련 로깅 기능 활성화 및 수신 채널 선택"},
            {"로깅(채널 관련)", "config4", "채널 관련 로깅 기능 활성화 및 수신 채널 선택"},
            {"로깅(유저 관련)", "config5", "유저 관련 로깅 기능 활성화 및 수신 채널 선택"},
            {"로깅(서버 관련)", "config6", "서버 관련 로깅 기능 활성화 및 수신 채널 선택"},
            {"LEWD 명령어", "config7", "LEWD 명령어 활성화 여부 선택"},
            {"링크 제한", "config8" , "링크 필터링 활성화 여부 선택"},
            {"전체 공지 수신", "config9", "관리성 공지 수신 및 수신채널 선택"},
            {"말 명령어", "config10", "말 명령어 활성화 여부 선택"}};
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if(member == null) {
            event.reply("에러가 발생했습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            logger.warn("ConfigCommand 에러발생");
            return;
        }
        if(!member.hasPermission(Permission.MANAGE_SERVER) || !member.hasPermission(Permission.MANAGE_CHANNEL) ||
                !member.hasPermission(Permission.MANAGE_ROLES) || !member.hasPermission(Permission.MESSAGE_MANAGE) ||
                !member.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("이 명령어를 사용할 권한이 없습니다!").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if(!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
            return;
        }

        StringSelectMenu.Builder builder= StringSelectMenu.create("설정");
        for(String[] list : optionList) {
            builder.addOption(list[0], list[1], list[2]);
        }
        event.reply("설정할 기능을 선택해주세요.")
                .addActionRow(builder.build()).setEphemeral(true).queue();
    }

    @Override
    public String getHelp() {
        return "각종 설정을 진행합니다.";
    }

    @Override
    public String getInvoke() {
        return "설정";
    }
}
