package com.kirito5572.objects.main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class getAirData {
    private final Logger logger = LoggerFactory.getLogger(getAirData.class);
    private static final String[] airKorea_data = new String[16];
    private static final String[] airKorea_List = new String[8];
    static {
        airKorea_List[0] = "측정시간";
        airKorea_List[1] = "SO2";
        airKorea_List[2] = "CO(일산화탄소)";
        airKorea_List[3] = "O3(오존)";
        airKorea_List[4] = "NO2(이산화질소)";
        airKorea_List[5] = "PM10(미세먼지)";
        airKorea_List[6] = "PM2.5(초미세먼지)";
        airKorea_List[7] = "KHAI(통합대기지수)";
    }

    @NotNull
    public String[] getAirKorea_data() {
        return airKorea_data;
    }
    @NotNull
    public String[] getAirKorea_List() {
        return airKorea_List;
    }
    @Nullable
    public String get_StationName(String sido, String dong) {
        String tmX = "error";
        String tmY = "error";
        try {
            StringBuilder TOKEN = new StringBuilder();
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\airKoreaLocationAPIKEY.txt");
            try (FileReader fileReader = new FileReader(file)){

                int signalCh;
                while ((signalCh = fileReader.read()) != -1) {
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
            String airKorea_url = "https://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getTMStdrCrdnt?";
            String numOfRows = "10";
            String pageNo = "1";
            String airKorea_serviceKey = TOKEN.toString();
            dong = URLEncoder.encode(dong, StandardCharsets.UTF_8);

            DocumentBuilderFactory airKorea_DB_Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder airKorea_Builder = airKorea_DB_Factory.newDocumentBuilder();
            Document airKorea_doc = airKorea_Builder.parse(airKorea_url + "serviceKey=" + airKorea_serviceKey + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&umdName=" + dong);

            // root tag
            airKorea_doc.getDocumentElement().normalize();
            // 파싱할 tag
            NodeList airKorea_nList = airKorea_doc.getElementsByTagName("item");

            Node node = airKorea_nList.item(0);


            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String sidoTemp = get_AirKoreaTagValue("sidoName", element);
                assert sidoTemp != null;
                if(sidoTemp.equals(sido)) {
                    tmX = get_AirKoreaTagValue("tmX", element);
                    tmY = get_AirKoreaTagValue("tmY", element);
                } else {
                    node = airKorea_nList.item(1);
                    if(node.getNodeType() == Node.ELEMENT_NODE) {
                        element = (Element) node;
                        sidoTemp = get_AirKoreaTagValue("sidoName", element);
                        assert sidoTemp != null;
                        if(sidoTemp.equals(sido)) {
                            tmX = get_AirKoreaTagValue("tmX", element);
                            tmY = get_AirKoreaTagValue("tmY", element);
                        } else {
                            node = airKorea_nList.item(2);
                            if(node.getNodeType() == Node.ELEMENT_NODE) {
                                element = (Element) node;
                                sidoTemp = get_AirKoreaTagValue("sidoName", element);
                                assert sidoTemp != null;
                                if(sidoTemp.equals(sido)) {
                                    tmX = get_AirKoreaTagValue("tmX", element);
                                    tmY = get_AirKoreaTagValue("tmY", element);
                                } else {
                                    return "error1234";
                                    //error1234 발생시 여기 더 추가해야함!
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {

            StackTraceElement[] eStackTrace = e.getStackTrace();
            StringBuilder a = new StringBuilder();
            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }
            logger.warn(a.toString());
        }
        assert tmX != null;
        if(tmX.equals("error")) {
            return "error1";
        }
        String stationName = "";
        try {
            StringBuilder TOKEN = new StringBuilder();
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\airKoreaLocationAPIKEY.txt");
            try (FileReader fileReader = new FileReader(file)){
                int signalCh;
                while ((signalCh = fileReader.read()) != -1) {
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
            String airKorea_url = "https://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?";
            String airKorea_serviceKey = TOKEN.toString();

            DocumentBuilderFactory airKorea_DB_Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder airKorea_Builder = airKorea_DB_Factory.newDocumentBuilder();
            Document airKorea_doc = airKorea_Builder.parse(airKorea_url + "tmX=" + tmX + "&tmY=" + tmY + "&serviceKey=" + airKorea_serviceKey);

            // root tag
            airKorea_doc.getDocumentElement().normalize();
            // 파싱할 tag
            NodeList airKorea_nList = airKorea_doc.getElementsByTagName("item");

            Node node = airKorea_nList.item(0);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                stationName = get_AirKoreaTagValue("stationName", element);
            }

        } catch (Exception e) {

            StackTraceElement[] eStackTrace = e.getStackTrace();
            StringBuilder a = new StringBuilder();
            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }
            logger.warn(a.toString());
        }
        airKorea_data[15] = sido + " " + stationName + " 측정소";
        assert stationName != null;
        stationName = URLEncoder.encode(stationName, StandardCharsets.UTF_8);
        return stationName;
    }


    public void get_API(String stationName) {
        try{

            StringBuilder TOKEN = new StringBuilder();
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\airKoreaAPIKEY.txt");
            try (FileReader fileReader = new FileReader(file)){
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

            String airKorea_url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";
            String dataTerm = "DAILY";
            String numOfRows = "10";
            String pageNo = "1";
            String ver = "1.3";
            String airKorea_serviceKey = TOKEN.toString();


            DocumentBuilderFactory airKorea_DB_Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder airKorea_Builder = airKorea_DB_Factory.newDocumentBuilder();
            String url = airKorea_url + "?&stationName=" + stationName + "&dataTerm=" + dataTerm + "&numOfRows" + numOfRows + "&pageNo=" + pageNo + "&ver=" + ver + "&serviceKey=" + airKorea_serviceKey;
            Document airKorea_doc = airKorea_Builder.parse(url);

            // root tag
            airKorea_doc.getDocumentElement().normalize();
            // 파싱할 tag
            NodeList airKorea_nList = airKorea_doc.getElementsByTagName("item");

            Node nNode = airKorea_nList.item(0);
            String airKorea_dataTime = "";
            String airKorea_so2V = "";
            String airKorea_coV = "";
            String airKorea_o3V = "";
            String airKorea_no2V = "";
            String airKorea_pm10V = "";
            String airKorea_pm25V = "";
            String airKorea_khaiV = "";
            String airKorea_khaiG = "-";
            String airKorea_so2G = "-";
            String airKorea_coG = "-";
            String airKorea_o3G = "-";
            String airKorea_no2G = "-";
            String airKorea_pm10G = "-";
            String airKorea_pm25G = "-";
            if(nNode.getNodeType() == Node.ELEMENT_NODE){

                Element eElement = (Element) nNode;
                airKorea_dataTime = (get_AirKoreaTagValue("dataTime", eElement));
                airKorea_so2V = (get_AirKoreaTagValue("so2Value", eElement));
                airKorea_coV = (get_AirKoreaTagValue("coValue", eElement));
                airKorea_o3V = (get_AirKoreaTagValue("o3Value", eElement));
                airKorea_no2V = (get_AirKoreaTagValue("no2Value", eElement));
                airKorea_pm10V = (get_AirKoreaTagValue("pm10Value", eElement));
                airKorea_pm25V = (get_AirKoreaTagValue("pm25Value", eElement));
                airKorea_khaiV = (get_AirKoreaTagValue("khaiValue", eElement));
                airKorea_khaiG = (get_AirKoreaTagValue("khaiGrade", eElement));
                airKorea_so2G = (get_AirKoreaTagValue("so2Grade", eElement));
                airKorea_coG = (get_AirKoreaTagValue("coGrade", eElement));
                airKorea_o3G = (get_AirKoreaTagValue("o3Grade", eElement));
                airKorea_no2G = (get_AirKoreaTagValue("no2Grade", eElement));
                airKorea_pm10G = (get_AirKoreaTagValue("pm10Grade", eElement));
                airKorea_pm25G = (get_AirKoreaTagValue("pm25Grade", eElement));
            }	// if end

            airKorea_data[0] = airKorea_dataTime;
            airKorea_data[1] = airKorea_so2V;
            airKorea_data[2] = airKorea_coV;
            airKorea_data[3] = airKorea_o3V;
            airKorea_data[4] = airKorea_no2V;
            airKorea_data[5] = airKorea_pm10V;
            airKorea_data[6] = airKorea_pm25V;
            airKorea_data[7] = airKorea_khaiV;
            airKorea_data[8] = airKorea_so2G;
            airKorea_data[9] = airKorea_coG;
            airKorea_data[10] = airKorea_o3G;
            airKorea_data[11] = airKorea_no2G;
            airKorea_data[12] = airKorea_pm10G;
            airKorea_data[13] = airKorea_pm25G;
            airKorea_data[14] = airKorea_khaiG;

        } catch (Exception e){

            StackTraceElement[] eStackTrace = e.getStackTrace();
            StringBuilder a = new StringBuilder();
            for (StackTraceElement stackTraceElement : eStackTrace) {
                a.append(stackTraceElement).append("\n");
            }
            logger.warn(a.toString());
        }	// try~catch end
    }
    @Nullable
    static String get_AirKoreaTagValue(String airKorea_tag, @NotNull Element airKorea_eElement) {
        NodeList nlList = airKorea_eElement.getElementsByTagName(airKorea_tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}