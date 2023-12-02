package com.kirito5572.objects.main;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;

public class getAirLocalData {
    private final Logger logger = LoggerFactory.getLogger(getAirLocalData.class);
    private static final String[] airKorea_data = new String[7];
    private static final String[] itemCode = new String[]{"PM10", "PM25", "O3", "SO2", "CO", "NO2", "측정시간"};

    public getAirLocalData() {
    }

    @NotNull
    public String[] getAirkorea_data() {
        return airKorea_data;
    }

    @NotNull
    public String[] getItemCode() {
        return itemCode;
    }

    public void get_API(String stationName) {
        try {
            String airKorea_url = "https://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst";
            String dataGubun = "HOUR";
            String numOfRows = "1";
            String pageNo = "1";
            String searchCondition = "WEEK";
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

            DocumentBuilderFactory airKorea_DB_Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder airKorea_Builder = airKorea_DB_Factory.newDocumentBuilder();

            for(i = 0; i < 6; ++i) {
                Document airKorea_doc = airKorea_Builder.parse(airKorea_url + "?serviceKey=" + TOKEN + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&itemCode=" + itemCode[i] + "&dataGubun=" + dataGubun + "&searchCondition=" + searchCondition);
                airKorea_doc.getDocumentElement().normalize();
                NodeList airKorea_nList = airKorea_doc.getElementsByTagName("item");
                Node nNode = airKorea_nList.item(0);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element)nNode;
                    airKorea_data[i] = getAirData.get_AirKoreaTagValue(stationName, eElement);
                    airKorea_data[6] = getAirData.get_AirKoreaTagValue("dataTime", eElement);
                }
            }
        } catch (Exception var16) {
            StackTraceElement[] eStackTrace = var16.getStackTrace();
            StringBuilder a = new StringBuilder();

            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }

            this.logger.warn(a.toString());
        }

    }
}
