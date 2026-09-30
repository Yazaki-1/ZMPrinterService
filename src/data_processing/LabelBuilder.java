package data_processing;

import com.ZMPrinter.*;
import com.ZMPrinter.LSF.LSFDecoder;
import com.ZMPrinter.conn.ConnectException;
import com.ZMPrinter.printer_connector.UsbConnect;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import common.CommonClass;
import common.LogType;
import function.FuncLabelCreator;
import function.FunctionalException;
import server.ChannelMap;
import utils.DataUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import static data_processing.FontResolve.*;

//import static common.CommonClass.AVAILABLE_FONTS;
//import static common.CommonClass.fontExist;

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
                throw new FunctionalException("3004|lsf文件路径错误 => " + jsonData.getLsfFilePath());
            } catch (Exception e) {
                throw new FunctionalException("3009|lsf路径异常 => " + jsonData.getLsfFilePath());
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
                            setContents(contents, array);
                        } catch (FunctionalException | ConnectException e) {
                            throw new FunctionalException(e.getMessage());
                        } catch (Exception e) {
                            throw new FunctionalException("3007|Json反序列化异常:" + e.getMessage());
                        }
                    }
                }
                // 如果是用的LsfFileVarList单张数据
                if (jsonData.getLsfFileVarList() != null) {
                    if (jsonData.getOperator().contains("preview")) {
                        preview_one = true;
                    }
                    JSONArray array = JSONArray.parseArray(jsonData.getLsfFileVarList().toString());
                    setContents(contents, array);
                }

                matchObjectList(contents);
                switch (jsonData.getOperator()) {
//            case "print":
//                printLabel(printer, label, contents, clientRemote);
//                break;*/
                    case "preview0":
                        if (preview_one) {
                            preview(finalLsfPrinter, labelFormat, contents, clientRemote, 0);
                            preview_one = false;
                        }
                        break;
                    case "preview":
                        if (preview_one) {
                            preview(finalLsfPrinter, labelFormat, contents, clientRemote, 1);
                            preview_one = false;
                        }
                        break;
                    case "setting":
                        throw new FunctionalException("3008|调用LSF模板不能使用Setting");
                    case "print":
                    case "batch":
                        addPrintQueue(finalLsfPrinter, labelFormat, contents, clientRemote);
                        break;
                    default:
                        throw new FunctionalException("3001|未定义的调用方式");
                }
            }
        } else {
            List<ZMLabelobject> labelObjectList = jsonData.getLabelObjectList();
            matchObjectList(labelObjectList);

            ZMPrinter printer = jsonData.getPrinter();
            ZMLabel labelFormat = jsonData.getLabelFormat();

            switch (jsonData.getOperator()) {
//                case "print":
//                    printLabel(printer, labelFormat, labelObjectList, clientRemote);
//                    break;*/
                case "preview0":
                    preview(printer, labelFormat, labelObjectList, clientRemote, 0);
                    break;
                case "preview":
                    preview(printer, labelFormat, labelObjectList, clientRemote, 1);
                    break;
                case "setting":
                    setting(printer, jsonData.getParameters(), clientRemote);
                    break;
                case "print":
                case "batch":
                    DataUtils.checkHexData(labelObjectList);
                    addPrintQueue(printer, labelFormat, labelObjectList, clientRemote);
                    break;
                default:
                    throw new FunctionalException("3001|未定义的调用方式");
            }
        }
    }

    private static void setContents(List<ZMLabelobject> contents, JSONArray array) {
        Map<String, String> lsfMaps = new HashMap<>();
        array.forEach(a -> {
            JSONObject lsfFileVar = JSONObject.parseObject(a.toString());
            if (lsfFileVar.containsKey("lsfFileVar")) {
                String var = lsfFileVar.get("lsfFileVar").toString();
                Map<String, Object> jsonMap = JSONObject.parseObject(var);
                String k = jsonMap.get("varname").toString();
                String v = jsonMap.get("varvalue").toString();
                lsfMaps.put(k, v);
            } else {
                String k = lsfFileVar.get("varname").toString();
                String v = lsfFileVar.get("varvalue").toString();
                lsfMaps.put(k, v);
            }
        });
        printUtility.setVarValue(contents, lsfMaps);
    }

    private static final String CHINESE_TEST_CHARS = "中文测试";

    // 此方法仅用于普通对象传参
    private static void matchObjectList(List<ZMLabelobject> labelObjectList) {
        labelObjectList.forEach(l -> {
            // 将所有text改为truetype
            if (l.ObjectName.contains("text") || l.ObjectName.contains("truetype")) {
                l.ObjectName = l.ObjectName.replace("text", "truetype");
                String data = l.objectdata;
                // data为空值可能是lsf模板绑定的子字符串共享名称，data会填充到Variables中
                if (data.isEmpty()) {
                    l.Variables.forEach(variable -> l.textfont = matchObjectList(variable.data, l.textfont));
                } else {
                    l.textfont = matchObjectList(data.trim(), l.textfont);
                }
            }
        });
    }

    private static String matchObjectList(String data, String font) {
        if (FuncLabelCreator.containsBasicChinese(data) && !canDisplayChinese(font)) {
            String hei = HEI_FONT;
            if (hei != null) {
                font = hei;
            }else {
                String fallBack = FALLBACK_FONT;
                if (fallBack != null) {
                    font = fallBack;
                }
            }
        }
        return font;
    }

    private static boolean canDisplayChinese(String fontName) {
        try {
            return fontExist(fontName) && new Font(fontName, Font.PLAIN, 12).canDisplayUpTo(CHINESE_TEST_CHARS) == -1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 查找系统中任何支持中文的字体
     */
//    private static String findAnyChineseFont() {
//        try {
//            System.out.println("开始查找系统中支持中文的字体...");
//
//            // 优先查找黑体系列
//            List<String> heitiKeywords = Arrays.asList(
//                    "Microsoft YaHei UI", "黑体", "Hei", "黑", "SimHei", "PingFang");
//
//            for (String font : AVAILABLE) {
//                if (canDisplayChinese(font)) {
//                    //System.out.println("找到支持中文的字体名称："+font);
//                    // 优先选择名称中包含黑体关键词的字体
//                    for (String keyword : heitiKeywords) {
//                        if (font.toLowerCase().contains(keyword.toLowerCase())) {
//                            System.out.println("找到支持中文的字体名称：" + font + "，匹配关键字：" + keyword + "成功。");
//                            return font;
//                        }
//                        System.out.println("找到支持中文的字体名称：" + font + "，匹配关键字：" + keyword + "失败。");
//                    }
//                }
//            }
//
//            // 返回第一个支持中文的字体
//            for (String font : AVAILABLE) {
//                if (canDisplayChinese(font)) {
//                    System.out.println("没有匹配到指定的字体名称，返回第一个支持中文的字体名称：" + font);
//                    return font;
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("查找中文字体时出错: " + e.getMessage());
//        }
//        return null;
//    }

//    private static void setLsfFileVar(ArrayList<Object> arrayList, List<ZMLabelobject> contents, String operator, ZMPrinter printer, ZMLabel label, String clientRemote) {
//        Map<String, String> lsfMaps = new HashMap<>();
//        arrayList.forEach(a -> {
//            JSONObject lsfFileVar = JSONObject.parseObject(a.toString());
//            if (lsfFileVar.containsKey("lsfFileVar")) {
//                String var = lsfFileVar.get("lsfFileVar").toString();
//                Map<String, Object> jsonMap = JSONObject.parseObject(var);
//                String k = jsonMap.get("varname").toString();
//                String v = jsonMap.get("varvalue").toString();
//                lsfMaps.put(k, v);
//            } else {
//                String k = lsfFileVar.get("varname").toString();
//                String v = lsfFileVar.get("varvalue").toString();
//                lsfMaps.put(k, v);
//            }
//        });
//        printUtility.setVarValue(contents, lsfMaps);
//        switch (operator) {
//            case "print":
//                printLabel(printer, label, contents, clientRemote);
//                break;*/
//            case "preview0":
//                if (preview_one) {
//                    preview(printer, label, contents, clientRemote, 0);
//                    preview_one = false;
//                }
//                break;
//            case "preview":
//                if (preview_one) {
//                    preview(printer, label, contents, clientRemote, 1);
//                    preview_one = false;
//                }
//                break;
//            case "setting":
//                throw new FunctionalException("3008|调用LSF模板不能使用Setting");
//            case "print":
//            case "batch":
//                addPrintQueue(printer, label, contents, clientRemote);
//                break;
//            default:
//                throw new FunctionalException("3001|未定义的调用方式");
//        }
//    }

//    @Deprecated
//    public static void printLabel(ZMPrinter printer, ZMLabel label, List<ZMLabelobject> contents, String clientRemote) {
//        PrinterOperator printerOperator = new PrinterOperatorImpl();
//
//        byte[] data = printUtility.CreateLabelCommand(printer, label, contents);
//        String connectType = DataUtils.getConnectType(printer.printerinterface);
//        try {
//            String writeResult;
//
//            switch (connectType) {
//                case "USB": {
//                    if (printer.printermbsn.isEmpty()) {
//                        List<String> printers = printerOperator.getPrinters();
//                        if (!printers.isEmpty()) {
//                            printer.printermbsn = printers.get(0);
//                        }
//                    }
//                    String serial = printer.printermbsn;
//                    if (printer.printerinterface == PrinterStyle.RFID_USB || printer.printerinterface == PrinterStyle.GJB_USB || printer.printerinterface == PrinterStyle.GBGM_USB) {
//                        PrintLabelFactory.printLabel(serial, data, clientRemote);
//                    }else {
//                        writeResult = UsbConnector.writeToPrinter(serial, ByteBuffer.wrap(data).array(), data.length);
//                        try {
//                            float speed = printer.printSpeed * 25.4f;
//                            float labelHeight = label.labelheight;
//                            long printWaiting = (long) (labelHeight / speed * 1000 / 3);
//                            Thread.sleep(printWaiting);
//                        }catch (InterruptedException e) {
//                            System.out.println(e.getMessage());
//                        }
//                        if (writeResult.contains("|")) {
//                            String message = ErrorCatcher.CatchConnectError(writeResult);
//                            ChannelMap.writeMessageToClient(clientRemote, message);
//                            CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ErrorData);
//                        }else {
//                            String message = CommonClass.i18nMessage.getString("print.finish");
//                            CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
//                            ChannelMap.writeMessageToClient(clientRemote, message);
//                        }
//                    }
//                    writeResult = "1";
//                    break;
//                }
//                case "NET": {
//                    writeResult = printerOperator.sendToPrinter(printer.printernetip, data);
//                    try {
//                        float speed = printer.printSpeed * 25.4f;
//                        float labelHeight = label.labelheight;
//                        long printWaiting = (long) (labelHeight / speed * 1000 / 3);
//                        Thread.sleep(printWaiting);
//                    }catch (InterruptedException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                }
//                case "DRIVER": {
//                    writeResult = printerOperator.sendToPrinterJob(printer.printername, data);
//                    break;
//                }
//                default: {
//                    writeResult = null;
//                }
//            }
//
//            if (writeResult != null) {
//                if (!writeResult.equals("1")) {
//                    String message = CommonClass.i18nMessage.getString("print.finish");
//                    try {
//                        // 防止lsf文件插入数据抢管道数据
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        throw new FunctionalException("4005|其他异常 => " + e.getMessage());
//                    }
//                    CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ServiceData);
//                    ChannelMap.writeMessageToClient(clientRemote, message);
//                }
//            } else {
//                throw new FunctionalException("4005|其他异常 => 未定义的printerInterface");
//            }
//        } catch (ConnectException e) {
//            String message = ErrorCatcher.CatchConnectError(e.getMessage());
//            ChannelMap.writeMessageToClient(clientRemote, message);
//            CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ErrorData);
//        }
//    }*/

    public static void addPrintQueue(ZMPrinter printer, ZMLabel label, List<ZMLabelobject> contents, String clientRemote) {
        float speed = printer.printSpeed * 25.4f;
        float labelHeight = label.labelheight;
        long printWaiting = (long) (labelHeight / speed * 1000);
        try {
            byte[] data = printUtility.CreateLabelCommand(printer, label, contents);
            BufferedImage image = printUtility.CreateLabelImage(printer, label, contents, 0);

            if (data == null) {
                String message = ErrorCatcher.CatchConnectError("3003|生成标签数据异常为空,请检查Json内容");
                CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ErrorData);
                ChannelMap.writeMessageToClient(clientRemote, message);
            } else {
                Map<String, Integer> readMap = hasRead(contents);
                int labelType = readMap.get("readLabelType") == null ? 0 : readMap.get("readLabelType");
                int readType = readMap.get("readType") == null ? 0 : readMap.get("readType");
                boolean hasRead = readMap.get("hasRead") != null && readMap.get("hasRead") > 0;

                LabelData labelData = new LabelData(printer, printWaiting, data, clientRemote, image, printer.printerinterface, hasRead, labelType, readType);

//                if (printer.printerinterface == PrinterStyle.PDF) {
//
//                    labelData.setLabelWidth(label.labelwidth);
//                    labelData.setLabelHeight(label.labelheight);
//
//                    for (ZMLabelobject labelObj : contents) {
//                        if (labelObj.ObjectName.contains("rfiduhf")) {
//                            StringBuilder rfidData = new StringBuilder();
//                            if (labelObj.objectdata.isEmpty()) {
//                                for (ObjectVariable v : labelObj.Variables) {
//                                    rfidData.append(v.data);
//                                }
//                            }else {
//                                rfidData = new StringBuilder(labelObj.objectdata);
//                            }
//                            labelData.setJobName(rfidData.toString());
//                        }
//                    }
//                }

                ChannelMap.addQueue(clientRemote, labelData);// 添加到打印队列
            }
        } catch (IllegalArgumentException e) {
            throw new FunctionalException(e.getMessage());
        }
    }

    private static Map<String, Integer> hasRead(List<ZMLabelobject> contents) {
        Map<String, Integer> map = new HashMap<>();
        for (ZMLabelobject label : contents) {
            if (label.ObjectName.contains("rfiduhf")) {
                if (label.ReadIDOnly) {
                    map.put("hasRead", 1);
                    map.put("readLabelType", label.RFIDEncodertype);
                    map.put("readType", label.ReadIDOnly_type);
                }
            }
        }
        return map;
    }

    public static void setting(ZMPrinter printer, String parameters, String clientRemote) {
        UsbConnect usbConnect = new UsbConnect();
        String[] params = parameters.split("\\|");
        StringBuilder builder = new StringBuilder();
        for (String param : params) {
            builder.append(param).append("\r\n");
        }
        byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
        try {
            usbConnect.write(printer.printermbsn, bytes, bytes.length);
        } catch (ConnectException e) {
            String message = ErrorCatcher.CatchConnectError(e.getMessage());
            CommonClass.saveAndShow(clientRemote + "    " + message, LogType.ErrorData);
            ChannelMap.writeMessageToClient(clientRemote, message);
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
        } catch (IllegalArgumentException e) {
            throw new FunctionalException(e.getMessage());
        }
    }
}
