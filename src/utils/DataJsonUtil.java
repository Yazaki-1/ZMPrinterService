package utils;

import com.alibaba.fastjson2.JSONObject;
import common.CommonClass;
import common.LogType;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class DataJsonUtil {
    protected static String dataPath =
            (System.getProperty("os.name").toLowerCase().contains("windows") ?
                    System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() :
                    System.getProperty("user.home") + "/zmsoft/ZMPrinterService/") + "bin/data.json";

    protected static String jsonContent = "";
    protected static JSONObject object = null;

    public static void read() {

        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(dataPath)));
            object = JSONObject.parseObject(jsonContent);

            Integer port = object.getInteger("port");
            if (port != null)
                CommonClass.localPort = port;
            else
                CommonClass.localPort = 1808;

            String sn = object.getString("sn");
            if (sn != null)
                CommonClass.localSN = sn;
            else
                CommonClass.localSN = "";

            Integer hide = object.getInteger("hide");
            if (hide != null)
                CommonClass.hideVisible = hide != 0;
            else
                CommonClass.hideVisible = false;

            Integer tray = object.getInteger("tray");
            if (tray != null)
                CommonClass.tray = tray != 0;
            else
                CommonClass.tray = false;

            Integer auto_start = object.getInteger("auto_start");
            if (auto_start != null)
                CommonClass.auto_start = auto_start != 0;
            else
                CommonClass.auto_start = false;

            Integer tcp = object.getInteger("tcp");
            if (tcp != null)
                CommonClass.tcp_receive = tcp != 0;
            else
                CommonClass.tcp_receive = false;

            String language = object.getString("language");
            if (language != null) {
                String[] arr = language.split("_");
                String lang = arr[0];
                String country = arr[1];
                CommonClass.language = language;
                Locale locale = new Locale(lang, country);
                CommonClass.i18nMessage = ResourceBundle.getBundle("i18n/messages", locale);
            } else {
                Locale locale = new Locale("zh", "CN");
                CommonClass.i18nMessage = ResourceBundle.getBundle("i18n/messages", locale);
            }

            Boolean ssl = object.getBoolean("ssl");
            if (ssl != null) {
                CommonClass.ssl = ssl;
            }else {
                CommonClass.ssl = false;
            }

            String certPath = object.getString("cert_path");
            if (sn != null)
                CommonClass.certPath = certPath;
            else
                CommonClass.certPath = "";

            String certPassword = object.getString("cert_password");
            if (sn != null)
                CommonClass.password = certPassword;
            else
                CommonClass.password = "";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            CommonClass.saveLog("No data found: " + e.getMessage(), LogType.ErrorData);
            CommonClass.localPort = 1808;
            CommonClass.localSN = "";
            CommonClass.hideVisible = false;
            CommonClass.tray = false;
            CommonClass.auto_start = false;
            CommonClass.tcp_receive = false;
            Locale locale = new Locale("zh", "CN");
            CommonClass.i18nMessage = ResourceBundle.getBundle("i18n/messages", locale);
            CommonClass.ssl = false;
        }
    }

    public static void write() {
        try {
            if (object == null) {
                object = new JSONObject();
            }
            object.put("port", CommonClass.localPort);
            object.put("sn", CommonClass.localSN);
            object.put("hide", CommonClass.hideVisible ? 1 : 0);
            object.put("tray", CommonClass.tray ? 1 : 0);
            object.put("auto_start", CommonClass.auto_start ? 1 : 0);
            object.put("tcp", CommonClass.tcp_receive ? 1 : 0);
            object.put("language", CommonClass.language);

            Files.write(Paths.get(dataPath), object.toJSONString().getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            CommonClass.saveLog(e.getMessage(), LogType.ErrorData);
        }
    }
}
