package function;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.ConnectException;
import com.ZMPrinter.conn.TcpConnector;
import com.ZMPrinter.conn.UsbConnector;
import common.CommonClass;
import common.PrinterDataFileCommon;
import data_processing.ErrorCatcher;
import layout.PrinterVO;
import layout.RFID_Calibration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalibrationFunctionImpl implements CalibrationFunction {
    @Override
    public void getUsbAndNetPrinters() {
        PrinterOperator printerOperator = new PrinterOperatorImpl();
        //尝试获取USB打印机,筛掉map中的非net打印机和获取不到系统USB的打印机(一般是打印机关机点刷新)
        try {
            List<String> devices = printerOperator.getPrinters();
            if (!RFID_Calibration.map.isEmpty()) {
                List<String> noConnectDevice = RFID_Calibration.map.keySet().stream().filter(key -> !key.contains(".") && !devices.contains(key)).collect(Collectors.toList());

                noConnectDevice.forEach(RFID_Calibration.map::remove);
            }
            devices.forEach(d -> {
                // USB打印机
                byte[] command = "RQ1,1\r\n".getBytes(StandardCharsets.UTF_8);
                if (!RFID_Calibration.map.containsKey(d)) {
                    String firmwareMessage = printerOperator.sendAndReadPrinter(d, command, command.length);
                    RFID_Calibration.map.put(d, RQ_toVO(firmwareMessage));
                }
            });
        } catch (ConnectException e) {
            if (!e.getMessage().startsWith("1008")) {
                throw new CalibrationException(ErrorCatcher.CatchConnectError(e.getMessage()));
            } else {
                // 1008代表没有USB打印机连接，需要将map中的序列号移除
                List<String> usbKey = new ArrayList<>();
                RFID_Calibration.map.forEach((key, value) -> {
                    if (!key.contains(".")) {
                        usbKey.add(key);
                    }
                });
                if (!usbKey.isEmpty()) {
                    usbKey.forEach(RFID_Calibration.map::remove);
                }
            }
        }
        Map<String, PrinterVO> pm = PrinterDataFileCommon.getPrinters();
        if (!pm.isEmpty()) {
            byte[] command = "RQ2,1\r\n".getBytes(StandardCharsets.UTF_8);
            pm.keySet().forEach(p -> {
                try {
                    checkConnection(p);
                    String firmwareMessage = printerOperator.sendAndReadPrinter(p, command, 12301, "127.0.0.1");
                    firmwareMessage = firmwareMessage.replace("\u0002", "").replace("\u0003", "").replace("\r\n", "");
                    RFID_Calibration.map.put(p, RQ_toVO(firmwareMessage));
                } catch (Exception e) {
                    // 连接失败,需要将map中的条数移去
                    RFID_Calibration.map.remove(p);
                }
            });
        }
    }

    @Override
    public PrinterVO addNetPrinter(String addr) {
        try {
            PrinterOperator printerOperator = new PrinterOperatorImpl();
            byte[] c = "RQ2,1\r\n".getBytes(StandardCharsets.UTF_8);
            String firmwareMessage = printerOperator.sendAndReadPrinter(addr, c, 12301, null);
            System.out.println(firmwareMessage);
            firmwareMessage = firmwareMessage.replace("\u0002", "").replace("\u0003", "").replace("\r\n", "");
            PrinterVO printerVO = RQ_toVO(firmwareMessage);
            RFID_Calibration.map.put(addr, printerVO);
            return printerVO;
        } catch (Exception e) {
            throw new CalibrationException(e.getMessage());
        }
    }

    @Override
    public void sendCommand(String addr, String command, boolean isNET) {
        if (command == null || command.isEmpty()) {
            throw new CalibrationException(CommonClass.i18nMessage.getString("instruction_empty"));
        }
        command += "\r\n";
        try {
            PrinterOperator printerOperator = new PrinterOperatorImpl();
            String status = printerOperator.getPrinterStatus(addr);
            if (status.equals("0")) {
                byte[] data = command.getBytes("GB2312");
                sendToPrinter(addr, isNET, data);
            }
        } catch (UnsupportedEncodingException e) {
            throw new CalibrationException(CommonClass.i18nMessage.getString("string_byte") + e.getMessage());
        } catch (CalibrationException e) {
            throw new CalibrationException(e.getMessage());
        }

    }

    @Override
    public void initializationPrinter(String addr, boolean isNET) {
        try {
            ZMPrinterFunction zmPrinterFunction = new ZMPrinterFunctionImpl();
            String status = zmPrinterFunction.getPrinterStatus(addr);
            // 实际上会被catch而不是进来if判断
            if (status.equals("|")) {
                throw new CalibrationException(status);
            }
            byte[] resetCommands = {0x23, 0x55, 0x4D, 0x3E, 0x49, 0x46, 0x0D, 0x0A};//打印机设置值
            sendToPrinter(addr, isNET, resetCommands);
        } catch (ConnectException | CalibrationException e) {
            throw new CalibrationException(ErrorCatcher.CatchConnectError(e.getMessage()));
        }
    }

    @Override
    public void resetPrinter(String addr, boolean isNET) {
        try {
            byte[] resetCommands = {0x5E, 0x40, 0x0D, 0x0A};//打印机设置值
            sendToPrinter(addr, isNET, resetCommands);
        } catch (CalibrationException e) {
            throw new CalibrationException(e.getMessage());
        }
    }

    @Override
    public boolean checkConnection(String addr) {
        try {
            String os = System.getProperty("os.name").toLowerCase().replace(" ", "");
            String pc = os.contains("windows") ? "ping " + addr + " -n 2 -w 2" : "ping " + addr + " -c 2 -W 2";
            Process p = Runtime.getRuntime().exec(pc);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connect = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if ((line.contains("ttl=") || line.contains("TTL=")) && line.contains("ms")) {
                    connect += 1;
                }
            }
            if (connect >= 1) {
                try {
                    PrinterOperator printerOperator = new PrinterOperatorImpl();
                    printerOperator.getPrinterStatus(addr);
                } catch (Exception e) {
                    throw new CalibrationException(CommonClass.i18nMessage.getString("failed.port"));
                }
                return true;
            } else {
                throw new CalibrationException(CommonClass.i18nMessage.getString("failed.ip"));
            }
        } catch (Exception e) {
            throw new CalibrationException(e.getMessage());
        }
    }

    private void sendToPrinter(String addr, boolean isNET, byte[] data) {
        String writeResult;
        if (isNET) { //TCP
            writeResult = TcpConnector.writeToPrinter(addr, data);
        } else {
            writeResult = UsbConnector.writeToPrinter(addr, data, data.length);
        }

        if (writeResult.contains("|")) {
            throw new CalibrationException(writeResult);
        }
    }

    private PrinterVO RQ_toVO(String firmwareMessage) {
        String[] printerDataList = firmwareMessage.split(",");//分隔字符串
        String printerName = printerDataList[0];
        String firmware = printerDataList[1].replace("ZMIN_", "");
        String productNumber = printerDataList[2];
        String printerDpi = printerDataList[3].replace("dpi", "").replace("DPI", "");

        return new PrinterVO(printerName, Float.parseFloat(printerDpi), firmware, productNumber);
    }
}
