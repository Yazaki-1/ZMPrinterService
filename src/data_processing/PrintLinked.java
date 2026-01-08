package data_processing;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.ConnectException;
import com.ZMPrinter.conn.TcpConnector;
import com.ZMPrinter.conn.UsbConnector;
import common.CommonClass;
import common.LogType;
import server.ChannelMap;

import java.util.concurrent.LinkedBlockingQueue;

public class PrintLinked {

    private final LinkedBlockingQueue<LabelData> blockingQueue = new LinkedBlockingQueue<>();
    private volatile boolean started = true;
    private final Thread printThread;
    private final PrinterOperator printerOperator = new PrinterOperatorImpl();

    public PrintLinked() {
        printThread = new Thread(() -> {
            while (started && !Thread.currentThread().isInterrupted()) {
                try {
                    LabelData labelData = blockingQueue.take();
                    byte[] data = labelData.getData();
                    String clientRemote = labelData.getClientRemote();
                    switch (labelData.getPrinter().printerinterface) {
                        case RFID_USB:
                        case GJB_USB:
                        case GBGM_USB:
//                            String serial = labelData.getPrinter().printermbsn;
//                            if (serial.isEmpty()) { // 如果这台打印机mbsn为空值,默认选中USB第一台
//                                PrinterOperator printerOperator = new PrinterOperatorImpl();
//                                List<String> printers = printerOperator.getPrinters();
//                                if (!printers.isEmpty()) {
//                                    serial = printers.get(0);
//                                }
//                            }
//                            try {
//                                printLabel_USB_R(serial, data);
//                                String message = CommonClass.i18nMessage.getString("print.finish");
//                                ChannelMap.writeMessageToClient(clientRemote, message);
//                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
//                            } catch (ConnectException e) {
//                                blockingQueue.clear();
//                                String msg = ErrorCatcher.CatchConnectError(e.getMessage());
//                                msg = msg.startsWith("2") ? "PrinterStatus_USB:" + msg : msg;
//                                ChannelMap.writeMessageToClient(clientRemote, msg);
//                            }
//                            break;
                        case USB:
                            try {
                                printerOperator.sendToPrinter(labelData.getPrinter().printermbsn, data, labelData.getDataLen(), 1);
                                String message = CommonClass.i18nMessage.getString("print.finish");
                                ChannelMap.writeMessageToClient(clientRemote, message);
                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                                // 需要更频繁一点
                                Thread.sleep(labelData.getPrintWaiting());
                            } catch (ConnectException e) {
                                blockingQueue.clear();
                                String msg = ErrorCatcher.CatchConnectError(e.getMessage());
                                msg = msg.startsWith("2") ? "PrinterStatus_NET:" + msg : msg;
                                ChannelMap.writeMessageToClient(clientRemote, msg);
                            }
                            break;
                        case RFID_NET:
                        case GJB_NET:
                        case GBGM_NET:
                            String ip = labelData.getPrinter().printernetip;
                            try {
                                printLabel_NET_R(ip, data);
                                String message = CommonClass.i18nMessage.getString("print.finish");
                                ChannelMap.writeMessageToClient(clientRemote, message);
                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                            } catch (ConnectException e) {
                                blockingQueue.clear();
                                ChannelMap.writeMessageToClient(clientRemote, e.getMessage());
                            }
                            break;
                        case NET: {
                            try {
                                printerOperator.sendToPrinter(labelData.getPrinter().printernetip, data);
                                String message = CommonClass.i18nMessage.getString("print.finish");
                                ChannelMap.writeMessageToClient(clientRemote, message);
                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                                // 需要更频繁一点
                                Thread.sleep(labelData.getPrintWaiting());
                            } catch (ConnectException e) {
                                blockingQueue.clear();
                                ChannelMap.writeMessageToClient(clientRemote, e.getMessage());
                            }
                            break;
                        }
                        default: {
                            try {
                                printerOperator.sendToPrinterJob(labelData.getPrinter().printername, data);
                                String message = CommonClass.i18nMessage.getString("print.finish");
                                ChannelMap.writeMessageToClient(clientRemote, message);
                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                                // 需要更频繁一点
                                Thread.sleep(labelData.getPrintWaiting());
                            } catch (ConnectException e) {
                                blockingQueue.clear();
                                ChannelMap.writeMessageToClient(clientRemote, e.getMessage());
                            }
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        printThread.start();
    }

    public void push(LabelData labelData) {
        blockingQueue.add(labelData);
    }

    public void close() {
        blockingQueue.clear();
        started = false;
        printThread.interrupt();
    }

    @Deprecated
    private void printLabel_USB_R(String serial, byte[] data) throws InterruptedException {
        System.out.println("Start Writing");
        String ws = UsbConnector.writeToPrinter(serial, data, data.length, 0);
        if (ws.contains("|")) {
            System.out.println("write: " + ws);
            throw new ConnectException(ErrorCatcher.CatchConnectError(ws));
        }
        Thread.sleep(500);
        System.out.println("Start Reading");
        String readData = UsbConnector.read(serial, 15000, 0);
        if (readData.contains("|")) {
            System.out.println("read: " + readData);
            throw new ConnectException(ErrorCatcher.CatchConnectError(readData));
        } else {
            if (readData.equals("RP")) {
                System.out.println("打印完成 -> RP");
                String ps = UsbConnector.getPrinterStatus(serial, 0);
                if (ps.contains("|")) {
                    throw new ConnectException(ErrorCatcher.CatchConnectError(ps));
                }
            } else {
                throw new ConnectException("1012|无法获取打印完成状态或者读取未知数据!");
            }
        }
    }

    private void printLabel_NET_R(String ip, byte[] data) throws InterruptedException {
        String ps = TcpConnector.getPrinterStatus(ip);
        System.out.println(ps);
        if (ps.contains("|")) {
            if (!ps.startsWith("2004"))
                throw new ConnectException(ErrorCatcher.CatchConnectError(ps));
            else
                Thread.sleep(300);
        }
        try {
            String serverIp = CommonClass.receiveServerIp;
            int port = CommonClass.receiveServerPort;
            String dataRead = printerOperator.sendAndReadPrinter(ip, data, port, serverIp);
            dataRead = dataRead.replace("\u0002", "").replace("\u0003", "").replace("\r", "").replace("\n", "");

            if (dataRead.equals("PN")) {
                System.out.println("打印完成 -> PN");
                ps = TcpConnector.getPrinterStatus(ip);
                if (ps.contains("|")) {
                    throw new ConnectException(ErrorCatcher.CatchConnectError(ps));
                }
            } else {
                throw new ConnectException("1012|无法获取打印完成状态或者读取未知数据!");
            }
        } catch (ConnectException e) {
            throw new ConnectException(ErrorCatcher.CatchConnectError(e.getMessage()));
        }
    }
}
