package me.kirito5572.listeners.main;

import me.kirito5572.objects.chzzk.Chzzk;
import me.kirito5572.objects.logger.ConfigPackage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ChzzkListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ChzzkListener.class);
    private final ConfigPackage configPackage;
    private final Chzzk chzzk;
    public ChzzkListener (ConfigPackage configPackage) {
        this.configPackage = configPackage;
        this.chzzk = new Chzzk(configPackage);
    }


    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if(event.getComponentId().equals("치지직설정")) {
            event.getMessage().delete().queue();
            List<String> values = event.getValues();
            switch (values.getFirst()) {
                case "chzzkConfig1" -> { //방송 채널 지정
                    //최초 설정과 이후 설정시로 분기
                    //TODO 생방송 시작 설정 만들어야함, 이후 설정 되는지 확인하고 자동 이벤트 생성하면 됨
                    if(configPackage.getConfigData(event.getGuild().getId()).chzzkStreamerId == null) { //최초 설정
                        TextInput url_sbj = TextInput.create("chzzkChannelIdInput", "치지직 채널 ID(필수)", TextInputStyle.SHORT)
                                .setPlaceholder("치치직 채널 URL을 입력해주세요(필수)")
                                .setRequiredRange(30, 300)
                                .setRequired(true)
                                .build();
                        TextInput message_sbj = TextInput.create("chzzkLiveStartMessage", "라이브 시작시 문구", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("라이브 시작시 전송될 안내 문구를 적어주세요!(최대 500자)")
                                .setRequiredRange(0, 500)
                                .build();
                        Modal modal = Modal.create("chzzkModal", "최초 설정")
                                .addComponents(ActionRow.of(url_sbj), ActionRow.of(message_sbj))
                                .build();
                        event.getMessage().delete().queue();
                        event.replyModal(modal).queue();
                    } else { //이후 설정
                        event.reply("치지직 방송 알림 설정 옵션입니다. 아래 버튼을 눌러주세요.")
                                .addActionRow(
                                        Button.primary("ChzzkLiveInfoEnable", "활성화"),
                                        Button.danger("ChzzkLiveInfoDisable", "비활성화"),
                                        Button.success("DiscordChannelIdChange","알림 채널 변경")
                                ).setEphemeral(true).queue();
                    }
                }
            }
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("chzzkModal")) {
            ModalMapping modalMapping = event.getValue("chzzkChannelIdInput");
            String channelURL = modalMapping == null ? null : modalMapping.getAsString();
            if(channelURL == null) {
                event.reply("잘못된 값이 입력되었습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                return;
            }
            String channelId = channelURL.substring(channelURL.indexOf("chzzk.naver.com/") + 16);
            if(channelId.startsWith("live")) {
                channelId = channelId.substring(5);
            }
            channelId = channelId.substring(0, 32);
            ModalMapping modalMapping2 = event.getValue("chzzkLiveStartMessage");
            String liveStartMessage = modalMapping2 == null ? null : modalMapping2.getAsString();
            try {
                configPackage.chzzkInit(event.getGuild().getId(), channelId, event.getChannelId(), liveStartMessage);
            } catch (SQLException e) {
                e.printStackTrace();
                event.reply("에러가 발생했습니다").setEphemeral(true).queue();
            }
            event.reply("등록이 완료되었습니다").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.getMessage().delete().queue();
            event.reply("해당 명령어는 서버에서만 사용할 수 있습니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        switch (event.getComponentId()) {
            //음악 기능
            case "ChzzkLiveInfoEnable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).chzzkLiveInfoEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHZZK_LIVE_OUTPUT_ENABLE, true);
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 기능이 활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "ChzzkLiveInfoDisable" -> {
                if (!configPackage.getConfigData(event.getGuild().getId()).chzzkLiveInfoEnable) {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHZZK_LIVE_OUTPUT_ENABLE, false);
                        event.getMessage().delete().queue();
                        event.reply("설정이 완료되었습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                } else {
                    event.getMessage().delete().queue();
                    event.reply("이미 기능이 비활성화 되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            case "DiscordChannelIdChange" -> {
                String nowChannelId = configPackage.getConfigData(event.getGuild().getId()).chzzkLiveChannelId;
                if (nowChannelId.equals(event.getChannelId())) {
                    event.getMessage().delete().queue();
                    event.reply("이미 현재 채널이 알림 채널로 등록되어있습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                } else {
                    try {
                        configPackage.updateConfig(event.getGuild().getId(), ConfigPackage.CHZZK_LIVE_OUTPUT_CHANNEL, event.getChannelId());
                        event.getMessage().delete().queue();
                        event.reply("현재 채널로 설정이 완료되었습니다.").queue();
                    } catch (SQLException e) {
                        event.getMessage().delete().queue();
                        event.reply("설정중 오류가 발생했습니다.").queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    }
                }
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Timer timer = new Timer();
        TimerTask startTask = new TimerTask() {
            @Override
            public void run() {
                List<Guild> list = event.getJDA().getGuilds();
                for(Guild guild : list) {
                    ConfigPackage.Config_Data data = configPackage.getConfigData(guild.getId());
                    if(data.chzzkLiveInfoEnable) {
                        new Thread(() -> {
                        try {
                            if(chzzk.isChzzkLiveAlive(data.chzzkStreamerId)) {
                                if(!data.isChzzkLiveSend) {
                                    try {
                                        configPackage.updateConfig(data.guildId, ConfigPackage.IS_CHZZK_LIVE_SEND, true);
                                        new Thread(() -> {
                                            try {
                                                EmbedBuilder builder = chzzk.chzzkLiveAlarm(data.guildId);
                                                TextChannel channel = guild.getTextChannelById(data.chzzkLiveChannelId);
                                                if (channel != null) channel.sendMessage(configPackage.getConfigData(data.guildId).chzzkLiveMessageOption).addEmbeds(builder.build()).queue();
                                            } catch (URISyntaxException | IOException | ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }).start();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if(data.isChzzkLiveSend) {
                                    configPackage.updateConfig(data.guildId, ConfigPackage.IS_CHZZK_LIVE_SEND, false);
                                }
                            }
                        } catch (URISyntaxException | IOException | SQLException e) {
                            e.printStackTrace();
                        }
                        }).start();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(startTask, 0, 5000);
        logger.info("치지직 기능 처리부 준비 완료");
    }
}
