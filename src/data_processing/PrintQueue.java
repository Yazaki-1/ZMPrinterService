package data_processing;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.ConnectException;
import common.CommonClass;
import common.LogType;
import server.ChannelMap;
import utils.DataUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

@Deprecated
public class PrintQueue {

    private final Timer queueTimer = new Timer();
    private final PrinterOperator printerOperator = new PrinterOperatorImpl();

    private final LinkedBlockingQueue<LabelData> blockingQueue;
    private boolean started;
    private LabelData cachedMessage = null;

    private int index = 0;// 打印下标记录?

    // 构造函数
    public PrintQueue(LinkedBlockingQueue<LabelData> blockingQueue) {
        this.blockingQueue = blockingQueue;
        started = false; // 默认为false,只有在开始打印才会为true
        queueTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (started) {
                    LabelData labelData = cachedMessage == null ? blockingQueue.poll() : cachedMessage;
                    if (labelData != null) {
                        String status;
                        try {
                            String connectType = DataUtils.getConnectType(labelData.getPrinter().printerinterface);
                            if (connectType.equals("USB")) {
                                status = printerOperator.getPrinterStatus(labelData.getPrinter().printermbsn, 1);
                            } else if (connectType.equals("NET")) {
                                status = printerOperator.getPrinterStatus(labelData.getPrinter().printernetip, 1);
                            } else {
                                status = "0"; //driver
                            }
                            if (status.equals("0")) {
                                try {
                                    String writeResult;
                                    byte[] data = labelData.getData();

                                    switch (connectType) {
                                        case "USB":
                                            writeResult = printerOperator.sendToPrinter(labelData.getPrinter().printermbsn, data, labelData.getDataLen(), 1);
                                            break;
                                        case "NET":
                                            writeResult = printerOperator.sendToPrinter(labelData.getPrinter().printernetip, data);
                                            break;
                                        case "DRIVER":
                                            writeResult = printerOperator.sendToPrinterJob(labelData.getPrinter().printername, data);
                                            break;
                                        default:
                                            writeResult = null;
                                            break;
                                    }

                                    if (cachedMessage != null) {
                                        cachedMessage = null;
                                    }
                                    // 需要更频繁一点
                                    Thread.sleep(labelData.getPrintWaiting());

                                    if (writeResult != null) {
                                        index++;
                                        ChannelMap.writeMessageToClient(labelData.getClientRemote(), "Printer: " + labelData.getPrinter().printermbsn + " " + CommonClass.i18nMessage.getString("print.finish") + ", index: " + index);
                                    }

                                } catch (ConnectException e) {
                                    // catch发送数据的异常,停止打印
                                    CommonClass.saveAndShow(labelData.getClientRemote() + "    " + ErrorCatcher.CatchConnectError(e.getMessage()), LogType.ErrorData);
                                    ChannelMap.writeMessageToClient(labelData.getClientRemote(), ErrorCatcher.CatchConnectError(e.getMessage()));
                                    started = false;
                                    index = 0;
                                } catch (InterruptedException e) {
                                    CommonClass.saveLog(labelData.getClientRemote() + "    " + e.getMessage(), LogType.ErrorData);
                                }
                            }
                        }catch (ConnectException e) {
                            if (!e.getMessage().contains("1009")) {//sn码有误或者没连上打印机，舍弃数据
                                // 异常,需要暂停,同时需要缓存!
                                cachedMessage = labelData;
                            }
                            CommonClass.saveAndShow(labelData.getClientRemote() + "    " + ErrorCatcher.CatchConnectError(e.getMessage()), LogType.ErrorData);
                            ChannelMap.writeMessageToClient(labelData.getClientRemote(), ErrorCatcher.CatchConnectError(e.getMessage()));
                            started = false;
                        }
                    } else {
                        started = false;
                        index = 0;
                    }
                }
            }
        }, 1000, 1000);
    }

    // 添加到打印队列
    @Deprecated
    public void addQueue(LabelData labelData) {
        blockingQueue.add(labelData);
    }

    // 开始打印
    @Deprecated
    public void startPrint() {
        this.started = true;
    }

    // 清空打印队列
    @Deprecated
    public void cleanQueue() {
        this.index = 0;
        this.blockingQueue.clear();
    }

    // 释放
    @Deprecated
    public void releaseQueue() {
        started = false;
        this.blockingQueue.clear();
        queueTimer.cancel();
    }
}