package data_processing;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.*;
import com.ZMPrinter.printer_connector.TcpConnect;
import com.ZMPrinter.printer_connector.TcpConnectImpl;
import com.ZMPrinter.printer_connector.UsbConnect;
import common.CommonClass;
import common.LogType;
import server.ChannelMap;

import java.util.concurrent.LinkedBlockingQueue;

public class PrintLinked {

    private final LinkedBlockingQueue<LabelData> blockingQueue = new LinkedBlockingQueue<>();
    private volatile boolean started = true;
    private final Thread printThread;

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
                            String serial = labelData.getPrinter().printermbsn;
                            try {
                                printLabel_USB_R(serial, data);
                                String message = CommonClass.i18nMessage.getString("print.finish");
                                ChannelMap.writeMessageToClient(clientRemote, message);
                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                            } catch (ConnectException e) {
                                blockingQueue.clear();
                                String msg = ErrorCatcher.CatchConnectError(e.getMessage());
                                msg = msg.startsWith("2") ? "PrinterStatus_USB:" + msg : msg;
                                ChannelMap.writeMessageToClient(clientRemote, msg);
                                CommonClass.saveAndShow(clientRemote + "    " + msg, LogType.ServiceData);
                            }
                            break;
                        case USB:
                            try {
                                UsbConnect usbConnect = new UsbConnect();
                                usbConnect.write(labelData.getPrinter().printermbsn, data, labelData.getDataLen());
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
                                TcpConnect tcpConnect = new TcpConnectImpl();
                                tcpConnect.sendToPrinter(labelData.getPrinter().printernetip, data);
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
//                        case PDF: {
//                            try {
//                                PrinterOperator printerOperator = new PrinterOperatorImpl();
//                                printerOperator.sendPdfToPrinterJob(labelData.getPrinter().printername, labelData.getImage(), labelData.getJobName(), labelData.getLabelWidth(), labelData.getLabelHeight());
//                                String message = CommonClass.i18nMessage.getString("print.finish");
//                                ChannelMap.writeMessageToClient(clientRemote, message);
//                                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
//                            } catch (ConnectException e) {
//                                blockingQueue.clear();
//                                ChannelMap.writeMessageToClient(clientRemote, e.getMessage());
//                            }
//                        }
                        default: {
                            try {
                                PrinterOperator printerOperator = new PrinterOperatorImpl();
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

    private void printLabel_USB_R(String serial, byte[] data) throws InterruptedException {
        UsbConnect usbConnect = new UsbConnect();
        int status = usbConnect.status(serial);
        if (status != 2004 && status != 0) {
            throw new ConnectException(ErrorCatcher.CatchConnectError(status + "|"));
        }

        usbConnect.write(serial, data, data.length);
        String readData = usbConnect.read(serial, CommonClass.usbTimeout, 256);
        if (readData.equals("RP")) {
            System.out.println("打印完成 -> RP");
            int rp_status = usbConnect.status(serial);
            if (rp_status != 0) {
                System.out.println(rp_status);
                throw new ConnectException(ErrorCatcher.CatchConnectError(rp_status + "|"));
            }
        } else {
            throw new ConnectException("1012|无法获取打印完成状态或者读取未知数据!");
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
            TcpConnect tcpConnect = new TcpConnectImpl();
            String serverIp = CommonClass.receiveServerIp;
            int port = CommonClass.receiveServerPort;
            String dataRead = tcpConnect.sendAndReadPrinter(ip, data, port, serverIp);
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
