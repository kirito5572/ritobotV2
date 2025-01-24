package me.kirito5572.objects.chzzk;

import com.google.gson.Gson;
import me.kirito5572.objects.logger.ConfigPackage;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Chzzk {

    private final ConfigPackage configPackage;

    public Chzzk(ConfigPackage configPackage) {
        this.configPackage = configPackage;
    }
    public static void main(String[] args) throws URISyntaxException, IOException {
        String channelURL = "https://chzzk.naver.com/live/86a9d2fe2121ef80c350a533852e4c3a";
        String channelId = channelURL.substring(channelURL.indexOf("chzzk.naver.com/") + 16);
        if(channelId.startsWith("live")) {
            channelId = channelId.substring(5);
        }
        channelId = channelId.substring(0, 32);

        ChzzkNormalData data = getNormalData(channelId);
        System.out.println(data);
    }

    public boolean isChzzkLiveAlive(String streamerId) throws URISyntaxException, IOException {
        ChzzkNormalData data = getNormalData(streamerId);
        if(data.content == null) {
            return false;
        }
        return data.content.openLive;
    }

/*    public EmbedBuilder chzzkLiveAlarm(String guildId) throws URISyntaxException, IOException, ParseException {
        String streamerId = configPackage.getConfigData(guildId).chzzkStreamerId;
        ChzzkNormalData data = getNormalData(streamerId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        //Date date = format.parse(data.content.openDate);
        return EmbedUtils.getDefaultEmbed()
                .setTitle(data.content.liveTitle, "https://chzzk.naver.com/live/" + streamerId)
                .setColor(Color.GREEN)
                .setImage(data.content.liveImageUrl.replaceFirst("\\{type}", "1080"))
                .addField(data.content.categoryType==null ? "" : data.content.categoryType, data.content.liveCategory, false)
                .addField("시청자수", String.valueOf(data.content.accumulateCount), false)
                .setTimestamp(date.toInstant())
                .setFooter(data.content.channel.channelName, data.content.channel.channelImageUrl);
    }
*/
    private ChzzkData getData(String streamerId) throws URISyntaxException, IOException {
        String apiURL = "https://api.chzzk.naver.com/service/v1/channels/";
        apiURL += URLEncoder.encode(streamerId, StandardCharsets.UTF_8);
        apiURL += "/live-detail";

        URL url = new URI(apiURL).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setUseCaches(false);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            con.setAllowUserInteraction(true);
        InputStream streams = con.getErrorStream();
        System.out.println(con.getResponseCode());
        System.out.println(streams);
        InputStream stream = con.getInputStream();
        System.out.println(stream);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine =br.readLine())!=null) {
            response.append(inputLine);
        }
        br.close();
        return new Gson().fromJson(response.toString(), ChzzkData.class);
    }

    private static ChzzkNormalData getNormalData(String streamerId) throws URISyntaxException, IOException {
        String apiURL = "https://api.chzzk.naver.com/service/v1/channels/";
        apiURL += URLEncoder.encode(streamerId, StandardCharsets.UTF_8);

        URL url = new URI(apiURL).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setUseCaches(false);
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        con.setAllowUserInteraction(true);
        InputStream stream = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine =br.readLine())!=null) {
            response.append(inputLine);
        }
        br.close();
        return new Gson().fromJson(response.toString(), ChzzkNormalData.class);
    }

    static class ChzzkData {
        int code;
        String message;
        ChzzkContentData content;
    }

    static class ChzzkNormalData {
        int code;
        String message;
        ChzzkNormalContentData content;
    }

    static class ChzzkNormalContentData {
        String channelId;
        String channelName;
        String channelImageUrl;
        boolean verifiedMark;
        String channelType;
        String channelDescription;
        int followerCount;
        boolean openLive;
        boolean subscriptionAvailability;
        ChzzkSubscriptionPaymentAvailabilityData subscriptionPaymentAvailability;
        boolean adMonetizationAvailability;
        String[] activatedChannelBadgeIds;
    }

    static class ChzzkSubscriptionPaymentAvailabilityData {
        boolean iapAvailability;
        boolean iabAvailability;
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
        boolean allowSubscriberInFollowerMode;
        String livePlaybackJson;
        String[] p2pQuality;
        ChzzkChannelData channel;
        String livePollingStatusJson;
        boolean userAdultStatus;
        boolean blindType;
        boolean chatDonationRankingExposure;
        ChzzkAdParameterData adParameter;
        String dropsCampaignNo;
        String watchPartyNo;
        String watchPartyTag;
    }
    static class ChzzkAdParameterData {
        String tag;
    }

    static class ChzzkChannelData {
        String channelId;
        String channelName;
        String channelImageUrl;
        boolean verifiedMark;
    }
}
