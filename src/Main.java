import common.CommonClass;
import common.LogType;
import layout.PrinterService;
import server.PrinterTcpSocketServer;
import server.PrinterWebSocketServer;
import utils.DataJsonUtil;
import utils.RegUtil;

public class Main {
    public static void main(String[] args) {
        DataJsonUtil.read();
        if (!CommonClass.hideVisible) {
            CommonClass.PARENT_LAYOUT = new PrinterService();
            CommonClass.PARENT_LAYOUT.setVisible(true);
        } else {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                String regPath = System.getProperty("user.dir") + "\\ZMPrinterService.exe";
                int autoStart = CommonClass.auto_start ? 1 : 0;
                int result = RegUtil.INSTANCE.set_auto_start(autoStart, regPath);
                if (result == 0) {
                    CommonClass.saveLog(CommonClass.i18nMessage.getString("err.set_autoStart_permission"), LogType.ErrorData);
                }
            }
            int port = CommonClass.localPort == null ? 1808 : CommonClass.localPort;
            System.out.println(port + " " + CommonClass.localSN);
            if (CommonClass.tcp_receive) {
                PrinterTcpSocketServer tcp_server = new PrinterTcpSocketServer(port);
                try {
                    tcp_server.start_server();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                PrinterWebSocketServer ws_server = new PrinterWebSocketServer(port);
                try {
                    ws_server.start_server();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}