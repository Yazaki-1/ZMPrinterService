import layout.PrinterService;
import server.PrinterWebSocketServer;

public class Main {
    public static void main(String[] args) {
        if (args != null && (args.length == 0 || !args[0].equals("hide"))) {
            PrinterService service = new PrinterService();
            service.setVisible(true);
        } else {
            PrinterWebSocketServer server = new PrinterWebSocketServer(1808);
            try {
                server.start_server();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}