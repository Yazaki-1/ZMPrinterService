package data_processing;

import com.ZMPrinter.conn.UsbConnector;
import common.CommonClass;
import common.LogType;
import server.ChannelMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PrintLabelFactory {

    private static final Lock lock = new ReentrantLock();

    public synchronized static void printLabel(String serial, byte[] data, String clientRemote) {
        lock.lock();
        try {
            String ps = UsbConnector.getPrinterStatus(serial, 0);
            if (ps.contains("|")) {
                ChannelMap.writeMessageToClient(clientRemote, ErrorCatcher.CatchConnectError(ps));
                return;
            }
            String ws = UsbConnector.writeToPrinter(serial, data, data.length, 0);
            if (ws.contains("|")) {
                ChannelMap.writeMessageToClient(clientRemote, ErrorCatcher.CatchConnectError(ws));
                return;
            }
            String readData = UsbConnector.read(serial, 10000, 0);
            if (readData.contains("|")) {
                ChannelMap.writeMessageToClient(clientRemote, ErrorCatcher.CatchConnectError(readData));
            }else {
                if (readData.equals("RP")) {
                    System.out.println("打印完成 -> RP");
                    ps = UsbConnector.getPrinterStatus(serial, 0);
                    if (ps.contains("|")) {
                        ChannelMap.writeMessageToClient(clientRemote, ErrorCatcher.CatchConnectError(ps));
                    }else {
                        String message = CommonClass.i18nMessage.getString("print.finish");
                        ChannelMap.writeMessageToClient(clientRemote, message);
                        CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                    }
                } else {
                    String message = "1012|无法获取打印完成状态或者读取未知数据!";
                    ChannelMap.writeMessageToClient(clientRemote, message);
                    CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                }
            }
        }finally {
            lock.unlock();
        }
    }
}
