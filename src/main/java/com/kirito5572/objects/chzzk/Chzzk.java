package com.kirito5572.objects.chzzk;

import com.google.gson.Gson;
import com.kirito5572.objects.logger.ConfigPackage;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class Chzzk {

    private final ConfigPackage configPackage;

    public Chzzk(ConfigPackage configPackage) {
        this.configPackage = configPackage;
    }
    public static void main(String[] args) {
        String channelURL = "https://chzzk.naver.com/live/86a9d2fe2121ef80c350a533852e4c3a/profile";
        String channelId = channelURL.substring(channelURL.indexOf("chzzk.naver.com/") + 16);
        if(channelId.startsWith("live")) {
            channelId = channelId.substring(5);
        }
        channelId = channelId.substring(0, 32);

        System.out.println(channelId);
    }

    public boolean isChzzkLiveAlive(String streamerId) throws URISyntaxException, IOException {
        ChzzkData data = getData(streamerId);
        return data.content.status.equals("OPEN");
    }

    public EmbedBuilder chzzkLiveAlarm(String guildId) throws URISyntaxException, IOException, ParseException {
        String streamerId = configPackage.getConfigData(guildId).chzzkStreamerId;
        ChzzkData data = getData(streamerId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date date = format.parse(data.content.openDate);
        return EmbedUtils.getDefaultEmbed()
                .setTitle(data.content.liveTitle, "https://chzzk.naver.com/live/" + streamerId)
                .setColor(Color.GREEN)
                .setImage(data.content.liveImageUrl.replaceFirst("\\{type}", "1080"))
                .addField(data.content.categoryType==null ? "" : data.content.categoryType, data.content.liveCategory, false)
                .addField("시청자수", String.valueOf(data.content.accumulateCount), false)
                .setTimestamp(date.toInstant())
                .setFooter(data.content.channel.channelName, data.content.channel.channelImageUrl);
    }



    private ChzzkData getData(String streamerId) throws URISyntaxException, IOException {
        String apiURL = "https://api.chzzk.naver.com/service/v2/channels/";
        apiURL += streamerId;
        apiURL += "/live-detail";

        URL url = new URI(apiURL).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine =br.readLine())!=null) {
            response.append(inputLine);
        }
        br.close();
        ChzzkData data = new Gson().fromJson(response.toString(), ChzzkData.class);
        return data;
    }

    static class ChzzkData {
        int code;
        String message;
        ChzzkContentData content;
    }

    static class ChzzkContentData {
        int liveId;
        String liveTitle;
        String status;
        String liveImageUrl;
        String defaultThumbnailImageUrl;
        int concurrentUserCount;
        int accumulateCount;
        String openDate;
        String closeDate;
        boolean adult;
        String chatChannelId;
        String categoryType;
        String liveCategory;
        String liveCategoryValue;
        String chatActive;
        String chatAvailableGroup;
        boolean paidPromotion;
        String chatAvailableCondition;
        int minFollowerMinute;
        String livePlaybackJson;
        ChzzkChannelData channel;
        String livePollingStatusJson;
        boolean userAdultStatus;
    }

    static class ChzzkChannelData {
        String channelId;
        String channelName;
        String channelImageUrl;
        boolean verifiedMark;
    }
}
