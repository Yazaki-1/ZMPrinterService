package data_processing;

import com.ZMPrinter.*;
import com.ZMPrinter.LSF.LSFDecoder;
import com.ZMPrinter.conn.ConnectException;
import com.ZMPrinter.conn.UsbConnector;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import common.CommonClass;
import common.LogType;
import function.FunctionalException;
import server.ChannelMap;
import utils.DataUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @description: 数据处理中心
 * @author: PH
 * @date: 2025/3/5
 */
public class LabelBuilder {
    private static final PrintUtility printUtility = new PrintUtility();
    private static boolean preview_one = false;

    public static void build(JsonData jsonData, String clientRemote) {
        if (jsonData.getLsfFilePath() != null) {
            //调用LSF文件
            LSFDecoder lsfDecoder = new LSFDecoder();
            Map<String, Object> map;
            try {
                // 尝试getZMLabelObjectList,如果路径有误会catch异常
                map = lsfDecoder.getZMLabelObjectList(jsonData.getLsfFilePath());
            } catch (FileNotFoundException e) {
                throw new FunctionalException("3004|lsf文件路径错误:" + jsonData.getLsfFilePath());
            }

            if (map != null) {
                ZMPrinter lsfPrinter = (ZMPrinter) map.get("zmprinter");//从文件中恢复打印机参数
                ZMLabel labelFormat = jsonData.getLabelFormat() == null ? (ZMLabel) map.get("zmlabel") : jsonData.getLabelFormat();//恢复标签参数,优先使用json中的format
                List<ZMLabelobject> contents = DataUtils.castList(map.get("zmlabelobjectlist"), ZMLabelobject.class);//恢复标签对象列表的参数

                if (jsonData.getPrinter() != null) {
                    // 如果设置了dpi但是和模板中的dpi不一致则缩放处理
                    if (jsonData.getPrinter().printerdpi > 0 && jsonData.getPrinter().printerdpi != lsfPrinter.printerdpi) {
                        printUtility.SetLabelObjectScale(
                                jsonData.getPrinter(),
                                lsfPrinter.printerdpi,
                                contents
                        );
                    }
                    lsfPrinter = jsonData.getPrinter();
                }
                List<String> labels = jsonData.getLabels();
                ZMPrinter finalLsfPrinter = lsfPrinter;
                if (labels != null) {
                    // 数据填充-labels: varvalue,varname
                    for (int i = 0; i < labels.size(); i++) {
                        if (i == 0 && jsonData.getOperator().contains("preview")) {
                            preview_one = true;
                        }
                        JSONObject label = JSONObject.parseObject(labels.get(i));
                        try {
                            JSONArray array = label.getJSONArray("lsfFileVarList");
                            setLsfFileVar(array, contents, jsonData.getOperator(), finalLsfPrinter, labelFormat, clientRemote);
                        } catch (FunctionalException e) {
                            throw new FunctionalException(e.getMessage());
                        } catch (Exception e) {
                            throw new FunctionalException("3007|Json反序列化异常:" + e.getMessage());
                        }
                    }
                }
                // 如果是用的LsfFileVarList单张数据
                if (jsonData.getLsfFileVarList() != null) {
                    JSONArray array = JSONArray.parseArray(jsonData.getLsfFileVarList().toString());
                    setLsfFileVar(array, contents, jsonData.getOperator(), finalLsfPrinter, labelFormat, clientRemote);
                }
            }
        } else {
            //普通json
            List<ZMLabelobject> labelObjectList = jsonData.getLabelObjectList();
            labelObjectList.forEach(l -> {
                // 将所有text改为truetype
                if (l.ObjectName.contains("text")) {
                    l.ObjectName = l.ObjectName.replace("text", "truetype");
                }
            });
            ZMPrinter printer = jsonData.getPrinter();
            ZMLabel labelFormat = jsonData.getLabelFormat();

            switch (jsonData.getOperator()) {
                case "print":
                    printLabel(printer, labelFormat, labelObjectList, clientRemote);
                    break;
                case "preview":
                    int border = jsonData.getOperator().endsWith("0") ? 0 : 1;
                    preview(printer, labelFormat, labelObjectList, clientRemote, border);
                    break;
                case "setting":
                    setting(printer, jsonData.getParameters(), clientRemote);
                    break;
                case "batch":
                    addPrintQueue(printer, labelFormat, labelObjectList, clientRemote);
                    break;
                default:
                    throw new FunctionalException("3001|未定义的调用方式");
            }
        }
    }

    private static void setLsfFileVar(ArrayList<Object> arrayList, List<ZMLabelobject> contents, String operator, ZMPrinter printer, ZMLabel label, String clientRemote) {
        Map<String, String> lsfMaps = new HashMap<>();
        arrayList.forEach(a -> {
            JSONObject lsfFileVar = JSONObject.parseObject(a.toString());
            if (lsfFileVar.containsKey("lsfFileVar")) {
                String var = lsfFileVar.get("lsfFileVar").toString();
                Map<String, Object> jsonMap = JSONObject.parseObject(var);
                String k = jsonMap.get("varname").toString();
                String v = jsonMap.get("varvalue").toString();
                lsfMaps.put(k, v);
            }
        });
        printUtility.setVarValue(contents, lsfMaps);
        int border = operator.endsWith("0") ? 0 : 1;
        switch (operator) {
            case "print":
                printLabel(printer, label, contents, clientRemote);
                break;
            case "preview": {
                if (preview_one) {
                    preview(printer, label, contents, clientRemote, border);
                    preview_one = false;
                    break;
                }
            }
            case "setting":
                throw new FunctionalException("3001|调用LSF模板不能使用Setting");
            case "batch":
                addPrintQueue(printer, label, contents, clientRemote);
                break;
            default:
                throw new FunctionalException("3001|未定义的调用方式");
        }
    }

    public static void printLabel(ZMPrinter printer, ZMLabel label, List<ZMLabelobject> contents, String clientRemote) {
        PrinterOperator printerOperator = new PrinterOperatorImpl();

        byte[] data = printUtility.CreateLabelCommand(printer, label, contents);
        String connectType = DataUtils.getConnectType(printer.printerinterface);
        try {
            String writeResult;

            switch (connectType) {
                case "USB": {
                    if (printer.printermbsn.isEmpty()) {
                        List<String> printers = printerOperator.getPrinters();
                        if (!printers.isEmpty()) {
                            printer.printermbsn = printers.get(0);
                        }
                    }
                    writeResult = printerOperator.sendToPrinter(printer.printermbsn, data, data.length);
                    break;
                }
                case "NET": {
                    writeResult = printerOperator.sendToPrinter(printer.printernetip, data);
                    break;
                }
                case "DRIVER": {
                    writeResult = printerOperator.sendToPrinterJob(printer.printername, data);
                    break;
                }
                default: {
                    writeResult = null;
                }
            }

            if (writeResult != null) {
                String message = "打印完成(Print finished)";
                try {
                    // 防止lsf文件插入数据抢管道数据
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new FunctionalException("4005|其他异常 => " + e.getMessage());
                }
                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
                ChannelMap.writeMessageToClient(clientRemote, message);
            } else {
                throw new FunctionalException("4005|其他异常 => 未定义的printerInterface");
            }
        } catch (ConnectException e) {
            ChannelMap.writeMessageToClient(clientRemote, e.getMessage());
            CommonClass.saveAndShow(clientRemote + "    " + e.getMessage(), LogType.ErrorData);
        }
    }

    public static void addPrintQueue(ZMPrinter printer, ZMLabel label, List<ZMLabelobject> contents, String clientRemote) {
        if (printer.printermbsn.isEmpty()) {
            PrinterOperator printerOperator = new PrinterOperatorImpl();
            List<String> printers = printerOperator.getPrinters();
            if (!printers.isEmpty()) {
                printer.printermbsn = printers.get(0);
            }
        }
        float speed = printer.printSpeed * 25.4f;
        float labelHeight = label.labelheight;
        long printWaiting = (long) (labelHeight / speed * 1000 / 3);
        byte[] data = printUtility.CreateLabelCommand(printer, label, contents);
        if (data == null) {
            String message = "3003|生成标签数据异常为空,请检查Json内容";
            CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ErrorData);
            ChannelMap.writeMessageToClient(clientRemote, message);
        } else {
            LabelData labelData = new LabelData(printer, printWaiting, data, clientRemote);
            ChannelMap.addQueue(clientRemote, labelData);// 添加到打印队列
        }
    }

    public static void setting(ZMPrinter printer, String parameters, String clientRemote) {
        String[] params = parameters.split("\\|");
        StringBuilder builder = new StringBuilder();
        for (String param : params) {
            builder.append(param).append("\r\n");
        }
        byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
        String writeResult = UsbConnector.writeToPrinter(printer.printermbsn, bytes, bytes.length);
        if (writeResult.equals("|")) {
            CommonClass.saveAndShow(clientRemote + "    " + writeResult, LogType.ErrorData);
            ChannelMap.writeMessageToClient(clientRemote, writeResult);
        }
    }

    public static void preview(ZMPrinter printer, ZMLabel label, List<ZMLabelobject> contents, String clientRemote, int border) throws FunctionalException {
        try {
            BufferedImage labelImage = printUtility.CreateLabelImage(printer, label, contents, border);//生成标签图片
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(labelImage, "png", stream);
            String base64Image = Base64.getEncoder().encodeToString(stream.toByteArray());
            ChannelMap.writeMessageToClient(clientRemote, "ZM_PrintLabel_Preview:data:image/png;base64," + base64Image);
        } catch (IOException e) {
            throw new FunctionalException("3005|图片预览错误:" + e.getMessage());
        }
    }
}
