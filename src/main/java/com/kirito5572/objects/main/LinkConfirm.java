//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kirito5572.objects.main;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkConfirm {
    private static String link;

    public static boolean isLink(@NotNull String rawMessage, String s) {
        if (rawMessage.startsWith("http://")) {
            rawMessage = rawMessage.replaceFirst("http://", "");
        }

        if (rawMessage.startsWith("https://")) {
            rawMessage = rawMessage.replaceFirst("https://", "");
        }

        Pattern p = Pattern.compile("[A-Za-z0-9_]+" + s + "$");
        Matcher m = p.matcher(rawMessage);
        if (rawMessage.contains(" ")) {
            p = Pattern.compile("\\s[A-Za-z0-9_]+" + s + "$");
            m = p.matcher(rawMessage);
        }

        boolean hangulFlag = false;
        if (!m.find()) {
            p = Pattern.compile("[가-힣][A-Za-z0-9_]+" + s + "$");
            m = p.matcher(rawMessage);
            hangulFlag = true;
        }

        int i = 0;

        while(m.find()) {
            try {
                String text = m.group(i);
                if (text.contains(" ")) {
                    text = text.replaceFirst(" ", "");
                }

                if (hangulFlag) {
                    text = text.substring(1);
                }

                URL url = new URI("https://" + text).toURL();
                link = text;
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 202 || connection.getResponseCode() == 301 || connection.getResponseCode() == 302 || connection.getResponseCode() == 204) {
                    return true;
                }

                ++i;
                if (i < m.groupCount()) {
                    return false;
                }
            } catch (Exception var9) {
                var9.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public static String getLink() {
        return link;
    }
}
