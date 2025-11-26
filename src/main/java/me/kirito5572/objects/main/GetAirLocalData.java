package me.kirito5572.objects.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GetAirLocalData {
    private final Logger logger = LoggerFactory.getLogger(GetAirLocalData.class);
    private static final String[] airKorea_data = new String[7];
    private static final String[] itemCode = new String[]{"PM10", "PM25", "O3", "SO2", "CO", "NO2", "측정시간"};

    public GetAirLocalData() {}

    @NotNull
    public String[] getAirkorea_data() {
        return airKorea_data;
    }

    @NotNull
    public String[] getItemCode() {
        return itemCode;
    }

    public AirData get_API(String sidoName) {
        AirData airdata = new AirData();
        try {
            String airKorea_url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
            String returnType = "json";
            String numOfRows = "1";
            String pageNo = "1";
            StringBuilder TOKEN = new StringBuilder();

            int i;
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\airkoreaAPIKEY.txt");
            try (FileReader fileReader = new FileReader(file)){
                while((i = fileReader.read()) != -1) {
                    TOKEN.append((char)i);
                }
            } catch (Exception var15) {
                StackTraceElement[] eStackTrace = var15.getStackTrace();
                StringBuilder a = new StringBuilder();

                for (StackTraceElement stackTraceElement : eStackTrace) {
                    a.append(stackTraceElement).append("\n");
                }

                this.logger.warn(a.toString());
            }
            /*
            {
               "response":{
                  "body":{
                     "totalCount":40,
                     "items":[
                        {
                           "pm25Grade1h":"1",
                           "pm10Value24":"44",
                           "so2Value":"0.0027",
                           "pm10Grade1h":"1",
                           "pm10Value":"27",
                           "o3Grade":"1",
                           "pm25Flag":null,
                           "khaiGrade":"2",
                           "pm25Value":"13",
                           "no2Flag":null,
                           "mangName":"도로변대기",
                           "stationName":"정릉로",
                           "no2Value":"0.0292",
                           "so2Grade":"1",
                           "stationCode":"111162",
                           "coFlag":null,
                           "khaiValue":"79",
                           "coValue":"0.43",
                           "pm10Flag":null,
                           "sidoName":"서울",
                           "pm25Value24":"27",
                           "no2Grade":"1",
                           "o3Flag":null,
                           "pm25Grade":"2",
                           "so2Flag":null,
                           "coGrade":"1",
                           "dataTime":"2025-11-26 14:00",
                           "pm10Grade":"2",
                           "o3Value":"0.0212"
                        }
                     ],
                     "pageNo":1,
                     "numOfRows":1
                  },
                  "header":{
                     "resultMsg":"NORMAL_CODE",
                     "resultCode":"00"
                  }
               }
            }
             */
            String url = airKorea_url + "?serviceKey=" + TOKEN + "&returnType=" + returnType + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&ver=1.5" + "&sidoName=" + URLEncoder.encode(sidoName,StandardCharsets.UTF_8);
            JsonObject element = JsonParser.parseString(getAPI(url)).getAsJsonObject().getAsJsonObject("response").getAsJsonObject("body").getAsJsonArray("items").get(0).getAsJsonObject();
            airdata.mangName = element.get("mangName").getAsString();
            airdata.stationName = element.get("stationName").getAsString();
            airdata.dataTime = element.get("dataTime").getAsString();

            airdata.so2Flag = AirDataFlagConvert(!element.get("so2Flag").isJsonNull() ? element.get("so2Flag").getAsString() : "-");
            if(airdata.so2Flag == AirData.NODATA) {
                airdata.so2Value = element.get("so2Value").getAsFloat();
                airdata.so2Grade = element.get("so2Grade").getAsInt();
            }

            airdata.coFlag = AirDataFlagConvert(!element.get("coFlag").isJsonNull() ? element.get("coFlag").getAsString() : "-");
            if(airdata.coFlag == AirData.NODATA) {
                airdata.coValue = element.get("coValue").getAsFloat();
                airdata.coGrade = element.get("coGrade").getAsInt();
            }

            airdata.o3Flag = AirDataFlagConvert(!element.get("o3Flag").isJsonNull() ? element.get("o3Flag").getAsString() : "-");
            if(airdata.o3Flag == AirData.NODATA) {
                airdata.o3Value = element.get("o3Value").getAsFloat();
                airdata.o3Grade = element.get("o3Grade").getAsInt();
            }

            airdata.no2Flag = AirDataFlagConvert(!element.get("no2Flag").isJsonNull()  ? element.get("no2Flag").getAsString() : "-");
            if(airdata.so2Flag == AirData.NODATA) {
                airdata.no2Value = element.get("no2Value").getAsFloat();
                airdata.no2Grade = element.get("no2Grade").getAsInt();
            }

            airdata.pm10Flag = AirDataFlagConvert(!element.get("pm10Flag").isJsonNull()  ? element.get("pm10Flag").getAsString() : "-");
            if(airdata.pm10Flag == AirData.NODATA) {
                airdata.pm10Value = element.get("pm10Value").getAsInt();
                airdata.pm10Value24 = element.get("pm10Value24").getAsInt();
                airdata.pm10Grade = element.get("pm10Grade").getAsInt();
                airdata.getPm10Grade1h = element.get("pm10Grade1h").getAsInt();
            }

            airdata.pm25Flag = AirDataFlagConvert(!element.get("pm25Flag").isJsonNull() ? element.get("pm25Flag").getAsString() : "-");
            if(airdata.pm25Flag == AirData.NODATA) {
                airdata.pm25Value = element.get("pm25Value").getAsInt();
                airdata.pm25Value24 = element.get("pm25Value24").getAsInt();
                airdata.pm25Grade = element.get("pm25Grade").getAsInt();
                airdata.getPm25Grade1h = element.get("pm25Grade1h").getAsInt();
            }

            airdata.khaiGrade = !element.get("khaiGrade").isJsonNull() ? element.get("khaiGrade").getAsInt() : 4;
            airdata.khalValue = airdata.khaiGrade == 0 ? element.get("khaiValue").getAsInt() : 0;

        } catch (Exception var16) {
            StackTraceElement[] eStackTrace = var16.getStackTrace();
            StringBuilder a = new StringBuilder();

            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }

            this.logger.warn(a.toString());
        }
        return airdata;
    }

    public static void main(String[] args) {
        GetAirLocalData getAirLocalData = new GetAirLocalData();
        AirData data = getAirLocalData.get_API("서울");
        System.out.println(data.toString());
    }

    public int AirDataFlagConvert(String value) {
        int result = 0;
        switch (value) {
            case "점검및교정":
                result = 1;
                break;
            case "장비점검":
                result = 2;
                break;
            case "자료이상":
                result = 3;
                break;
            case "통신장애":
                result = 4;
                break;
        }
        return result;
    }

    public String AirDataFlagConvert(int value) {
        String result = "-";
        switch (value) {
            case AirData.NODATA:
                break;
            case AirData.CALIBRATION:
                result = "장비 교정";
                break;
            case AirData.MAINTENANCE:
                result = "장비 점검";
                break;
            case AirData.DATA_ERROR:
                result = "자료 이상";
                break;
            case AirData.COMMUNICATION_ERROR:
                result = "통신 장애";
                break;
        }
        return result;
    }

    private String getAPI(String url) throws URISyntaxException, MalformedURLException {
        String result = "";
        URL get_url = new URI(url).toURL();
        String line;

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(get_url.openStream()))) {

            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }
        } catch (IOException ignored) {
        }
        return result;
    }

    public static class AirData {

        public final String[] GradeTranslation = {"데이터 없음","좋음","보통","나쁨", "매우나쁨"};

        public static final int NODATA = 0;
        public static final int GOOD = 1;
        public static final int NORMAL = 2;
        public static final int BAD = 3;
        public static final int VERY_BAD = 4;

        public static final int CALIBRATION = 1;
        public static final int MAINTENANCE = 2;
        public static final int DATA_ERROR = 3;
        public static final int COMMUNICATION_ERROR = 4;


        public String stationName;
        public int stationCode;
        public String mangName;
        public String sidoName;
        public String dataTime;
        public float so2Value;
        public float coValue;
        public float o3Value;
        public float no2Value;
        public int pm10Value;
        public int pm10Value24;
        public int pm25Value;
        public int pm25Value24;
        public int khalValue;
        public int khaiGrade = NODATA;
        public int so2Grade = NODATA;
        public int coGrade = NODATA;
        public int o3Grade = NODATA;
        public int no2Grade = NODATA;
        public int pm10Grade = NODATA;
        public int pm25Grade = NODATA;
        public int getPm10Grade1h = NODATA;
        public int getPm25Grade1h = NODATA;
        public int so2Flag = NODATA;
        public int coFlag = NODATA;
        public int o3Flag = NODATA;
        public int no2Flag = NODATA;
        public int pm10Flag = NODATA;
        public int pm25Flag = NODATA;
    }
}
