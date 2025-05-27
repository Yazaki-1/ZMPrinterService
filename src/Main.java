import com.alibaba.fastjson2.JSONObject;
import common.CommonClass;
import common.LogType;
import layout.PrinterService;
import server.PrinterWebSocketServer;
import utils.RegUtil;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            String dataPath = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "bin" + FileSystems.getDefault().getSeparator() + "data.json";
            System.out.println(dataPath);
            String jsonContent = new String(Files.readAllBytes(Paths.get(dataPath)));

            JSONObject object = JSONObject.parseObject(jsonContent);
            CommonClass.localPort = Integer.parseInt(object.getString("port"));
            CommonClass.localSN = object.getString("sn");
            CommonClass.hideVisible = Integer.parseInt(object.getString("hide")) != 0;
            CommonClass.tray = Integer.parseInt(object.getString("tray")) != 0;
            CommonClass.auto_start = Integer.parseInt(object.getString("auto_start")) != 0;
        } catch (IOException | NumberFormatException e) {
            CommonClass.localPort = 1808;
            CommonClass.localSN = "";
            CommonClass.hideVisible = false;
            CommonClass.tray = false;
            CommonClass.auto_start = false;
        }
        if (!CommonClass.hideVisible) {
            PrinterService service = new PrinterService();
            service.setVisible(true);
        } else {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                String regPath = System.getProperty("user.dir") + "\\ZMPrinterService.exe";
                int autoStart = CommonClass.auto_start ? 1 : 0;
                int result = RegUtil.INSTANCE.set_auto_start(autoStart, regPath);
                if (result == 0) {
                    CommonClass.saveAndShow("设置自动启动失败!可能是权限不足的问题.", LogType.ErrorData);
                }
            }
            int port = CommonClass.localPort == null ? 1808 : CommonClass.localPort;
            System.out.println(port + " " + CommonClass.localSN);
            PrinterWebSocketServer server = new PrinterWebSocketServer(port);
            try {
                server.start_server();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}