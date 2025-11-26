package me.kirito5572.objects.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class getAirLocalData {
    private final Logger logger = LoggerFactory.getLogger(getAirLocalData.class);
    private static final String[] airKorea_data = new String[7];
    private static final String[] itemCode = new String[]{"PM10", "PM25", "O3", "SO2", "CO", "NO2", "측정시간"};

    public getAirLocalData() {}

    @NotNull
    public String[] getAirkorea_data() {
        return airKorea_data;
    }

    @NotNull
    public String[] getItemCode() {
        return itemCode;
    }

    public void get_API(String sidoName) {
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

            String url = airKorea_url + "?serviceKey=" + TOKEN + "&returnType=" + returnType + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&sidoName=" + sidoName;
            JsonObject element = JsonParser.parseString(getAPI(url)).getAsJsonObject();


        } catch (Exception var16) {
            StackTraceElement[] eStackTrace = var16.getStackTrace();
            StringBuilder a = new StringBuilder();

            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }

            this.logger.warn(a.toString());
        }

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
}
