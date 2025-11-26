package me.kirito5572.objects.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetWeather {
    private final Logger logger = LoggerFactory.getLogger(GetWeather.class);
    private final SimpleDateFormat clock_aa = new SimpleDateFormat("a");
    private final SimpleDateFormat clock_am = new SimpleDateFormat("K시 mm분 ss초(z)");
    private final SimpleDateFormat clock_pm = new SimpleDateFormat("h시 mm분 ss초(z)");
    private final String token;

    public GetWeather() {
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
            this.token = "error";
            return;
        }
        this.token = TOKEN.toString();
    }

    public WeatherInformation getNowWeather(String city_name) throws URISyntaxException, IOException {
        WeatherInformation weatherInformation = new WeatherInformation();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city_name + ",kr&appid=" + token + "&lang=kr" + "&units=metric";

        /*
        {
            "coord":{
            "lon":129.0403,
                    "lat":35.1028
        },
            "weather":[
            {
                "id":800,
                    "main":"Clear",
                    "description":"맑음",
                    "icon":"01n"
            }
],
            "base":"stations",
                "main":{
            "temp":0.99,
                    "feels_like":-0.69,
                    "temp_min":0.99,
                    "temp_max":0.99,
                    "pressure":1030,
                    "humidity":75
        },
            "visibility":10000,
                "wind":{
            "speed":1.54,
                    "deg":350
        },
            "clouds":{
            "all":0
        },
            "dt":1703688902,
                "sys":{
            "type":1,
                    "id":8086,
                    "country":"KR",
                    "sunrise":1703629828,
                    "sunset":1703665102
        },
            "timezone":32400,
                "id":1838524,
                "name":"Busan",
                "cod":200
        }
        */
        JsonObject element = JsonParser.parseString(getAPI(url)).getAsJsonObject();
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

        weatherInformation.weatherCord = parse_weather.get(0).getAsJsonObject().get("description").getAsString();     //날씨 상태
        weatherInformation.temp = parse_main.get("temp").getAsFloat();                                                //온도
        weatherInformation.tempFeels = parse_main.get("feels_like").getAsFloat();                                     //체감 온도
        weatherInformation.pressure = parse_main.get("pressure").getAsInt();                                          //대기압
        weatherInformation.humidity = parse_main.get("humidity").getAsInt();                                          //습도
        weatherInformation.windSpeed = parse_wind.get("speed").getAsInt();                                            //풍속
        weatherInformation.windDeg = parse_wind.get("deg").getAsInt();                                                //풍향

        if(weatherInformation.weatherCord.contains("비")) {
            if(parse_rain != null) {
                weatherInformation.rain_3hr = parse_rain.get("3h").getAsInt();                                        //3시간 강수량
            } else {
                weatherInformation.rain_3hr = 0;
            }
        } else {
            weatherInformation.rain_3hr = 0;
        }
        if(weatherInformation.weatherCord.contains("눈")) {
            if(parse_snow != null) {
                weatherInformation.snow_3hr = parse_snow.get("3h").getAsInt();                                         //3시간 적설량
            } else {
                weatherInformation.rain_3hr = 0;
            }
        } else {
            weatherInformation.snow_3hr = 0;
        }
        weatherInformation.ForecastTime = new Date(element.get("dt").getAsLong() * 1000);
        weatherInformation.sunRise = new Date(parse_sys.get("sunrise").getAsLong() * 1000);                            //일출시간
        weatherInformation.sunSet = new Date(parse_sys.get("sunset").getAsLong() * 1000);                              // 일몰시간
        return weatherInformation;
    }

    public List<WeatherInformation> getForecastWeather(String city_name) throws URISyntaxException, IOException {
        List<WeatherInformation> weatherInformationList = new ArrayList<>();
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city_name + ",kr&appid=" + token + "&lang=kr" + "&units=metric";
        JsonObject element = JsonParser.parseString(getAPI(url)).getAsJsonObject();
        int count = element.get("cnt").getAsInt();
        JsonArray list_forecast = element.get("list").getAsJsonArray();
        for(int i = 0; i < count; i++) {
            WeatherInformation weatherInformation = new WeatherInformation();
            JsonObject parse_forecast = list_forecast.get(i).getAsJsonObject();
            JsonObject forecast_main = parse_forecast.get("main").getAsJsonObject();
            JsonObject forecast_wind = forecast_main.get("wind").getAsJsonObject();
            JsonObject parse_rain = null;
            JsonObject parse_snow = null;
            try {
                parse_rain = parse_forecast.get("rain").getAsJsonObject();
            } catch (Exception ignored) {
            }
            try {
                parse_snow = parse_forecast.get("snow").getAsJsonObject();
            } catch (Exception ignored) {
            }
            weatherInformation.ForecastTime = new Date(parse_forecast.get("dt").getAsLong() * 1000);
            weatherInformation.weatherCord = parse_forecast.get("weather").getAsJsonArray()
                    .get(0).getAsJsonObject().get("description").getAsString();
            weatherInformation.temp = forecast_main.get("temp").getAsFloat();
            weatherInformation.tempFeels = forecast_main.get("feels_like").getAsFloat();
            weatherInformation.pressure = forecast_main.get("pressure").getAsInt();
            weatherInformation.humidity = forecast_main.get("humidity").getAsInt();
            weatherInformation.windSpeed = forecast_wind.get("speed").getAsInt();
            weatherInformation.windDeg = forecast_wind.get("deg").getAsInt();
            if(weatherInformation.weatherCord.contains("비")) {
                if(parse_rain != null) {
                    weatherInformation.rain_3hr = parse_rain.get("3h").getAsInt();                                        //3시간 강수량
                } else {
                    weatherInformation.rain_3hr = 0;
                }
            } else {
                weatherInformation.rain_3hr = 0;
            }
            if(weatherInformation.weatherCord.contains("눈")) {
                if(parse_snow != null) {
                    weatherInformation.snow_3hr = parse_snow.get("3h").getAsInt();                                         //3시간 적설량
                } else {
                    weatherInformation.snow_3hr = 0;
                }
            } else {
                weatherInformation.snow_3hr = 0;
            }
        }
        return weatherInformationList;
    }

    private String getAPI(String url) throws URISyntaxException, MalformedURLException {
        String result = "";
        URL get_url = new URI(url).toURL();
        String line;

        System.out.println(url);

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(get_url.openStream()))) {

            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }
        } catch (IOException ignored) {
        }
        return result;
    }

    @NotNull
    public String formatDate(Date date) {
        String flag = clock_aa.format(date);
        if(flag.equals("오전")) {
            return "오전 " + clock_am.format(date);
        } else {
            return "오후 " + clock_pm.format(date);
        }
    }

    public static class WeatherInformation {
        public Date ForecastTime;   //예보기준시간
        public String weatherCord;  //날씨코드
        public float temp;          //현재온도
        public float tempFeels;     //체감온도
        public int pressure;        //대기압
        public int humidity;        //습도
        public int windSpeed;       //풍속
        public int windDeg;         //풍향
        public int rain_3hr;        //3시간 강수량
        public int snow_3hr;        //3시간 적설량
        public Date sunRise;        //일출
        public Date sunSet;         //일몰
    }
}
