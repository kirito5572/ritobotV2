package com.kirito5572.objects.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getWeather {
    private static final Logger logger = LoggerFactory.getLogger(getWeather.class);
    private static final SimpleDateFormat clock_aa = new SimpleDateFormat("a");
    private static final SimpleDateFormat clock_am = new SimpleDateFormat("K시 mm분 ss초(z)");
    private static final SimpleDateFormat clock_pm = new SimpleDateFormat("h시 mm분 ss초(z)");
    private static final String[] weather_information = new String[12];
    private static final String[] weather_list = new String[] {
            "날씨 상태", "현재 온도", "대기압", "습도", "풍속",
            "바람의 방향", "3시간 강수량", "3시간 적설량",
            "일출 시간", "일몰 시간"
    };

    public static void get_api(String city_name) {

        try {

            StringBuilder TOKEN = new StringBuilder();
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\weather_key.txt");
            try(FileReader fileReader = new FileReader(file)) {
                int signalCh;
                while((signalCh = fileReader.read()) != -1) {
                    TOKEN.append((char) signalCh);
                }
            } catch (Exception e) {

                StackTraceElement[] eStackTrace = e.getStackTrace();
                StringBuilder a = new StringBuilder();
                for (StackTraceElement stackTraceElement : eStackTrace) {
                    a.append(stackTraceElement).append("\n");
                }
                logger.warn(a.toString());
            }

            String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city_name + "&appid=" + TOKEN + "&lang=kr" + "&units=metric";
            URL get_url = new URI(url).toURL();

            BufferedReader bf;
            String line;
            String result = "";

            bf = new BufferedReader(new InputStreamReader(get_url.openStream()));

            while((line=bf.readLine()) != null) {
                result = result.concat(line);
            }
            /*
            {
              "coord": {
                "lon": 126.5219,
                "lat": 33.5097
              },
              "weather": [
                {
                  "id": 803,
                  "main": "Clouds",
                  "description": "튼구름",
                  "icon": "04d"
                }
              ],
              "base": "stations",
              "main": {
                "temp": 10,
                "feels_like": 5.27,
                "temp_min": 10,
                "temp_max": 10,
                "pressure": 1026,
                "humidity": 71
              },
              "visibility": 10000,
              "wind": {
                "speed": 5.14,
                "deg": 80
              },
              "clouds": {
                "all": 75
              },
              "dt": 1615106842,
              "sys": {
                "type": 1,
                "id": 8087,
                "country": "KR",
                "sunrise": 1615067719,
                "sunset": 1615109699
              },
              "timezone": 32400,
              "id": 1846266,
              "name": "Jeju City",
              "cod": 200
            }
             */
            bf.close();
            JsonObject element = JsonParser.parseString(result).getAsJsonObject();
            JsonArray parse_weather = element.get("weather").getAsJsonArray();
            JsonObject parse_main = element.get("main").getAsJsonObject();
            JsonObject parse_wind = element.get("wind").getAsJsonObject();
            JsonObject parse_rain = null;
            JsonObject parse_snow = null;
            try {
                parse_rain = element.get("rain").getAsJsonObject();
            } catch (Exception ignored) {
            }
            try {
                parse_snow = element.get("snow").getAsJsonObject();
            } catch (Exception ignored) {
            }
            JsonObject parse_sys = element.get("sys").getAsJsonObject();

            weather_information[0] = parse_weather.get(0).getAsJsonObject().get("description").getAsString();   //날씨 상태
            weather_information[1] = parse_main.get("temp").getAsString() + "Cº";      // 온도

            weather_information[2] = parse_main.get("pressure").getAsString() + "hPa";      // 대기압

            weather_information[3] = parse_main.get("humidity").getAsString() + "%";      // 습도

            weather_information[4] = parse_wind.get("speed").getAsString() + "m/s";      // 풍속

            weather_information[5] = parse_wind.get("deg").getAsString() + "º";      // 풍향

            if(weather_information[0].equals("비")) {
                if(parse_rain == null) {
                    assert false;
                    weather_information[6] = parse_rain.get("3h").getAsString() + "mm";      // 3시간 강수량
                }
            } else {
                weather_information[6] = "null";
            }
            if(weather_information[0].equals("눈")) {
                if(parse_snow == null) {
                    assert false;
                    weather_information[7] = parse_snow.get("3h").getAsString() + "cm";      // 3시간 적설량
                }
            } else {
                weather_information[7] = "null";
            }

            Date time;
            weather_information[8] = parse_sys.get("sunrise").getAsString();      // 일출시간
            time = new Date(Long.parseLong(weather_information[8]) * 1000);
            weather_information[8] = formatDate(time);

            weather_information[9] = parse_sys.get("sunset").getAsString();      // 일몰시간
            time = new Date(Long.parseLong(weather_information[9]) * 1000);
            weather_information[9] = formatDate(time);

        } catch (Exception e) {

            StackTraceElement[] eStackTrace = e.getStackTrace();
            StringBuilder a = new StringBuilder();
            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }
            logger.warn(a.toString());
        }

    }
    @NotNull
    private static String formatDate(Date date) {
        String flag = clock_aa.format(date);
        if(flag.equals("오전")) {
            return "오전 " + clock_am.format(date);
        } else {
            return "오후 " + clock_pm.format(date);
        }
    }

    @NotNull
    public static String[] getWeather_information() {
        return weather_information;
    }

    @NotNull
    public static String[] getWeather_list() {
        return weather_list;
    }
}
