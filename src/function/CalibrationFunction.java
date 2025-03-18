package function;

import layout.PrinterVO;

public interface CalibrationFunction {
    void getUsbAndNetPrinters();

    PrinterVO addNetPrinter(String addr);

    // 发送指令
    void sendCommand(String addr, String command, boolean isNET);

    // 打印机初始化
    void initializationPrinter(String addr, boolean isNET);

    // 打印机复位
    void resetPrinter(String addr, boolean isNET);

    boolean checkConnection(String addr);
}
