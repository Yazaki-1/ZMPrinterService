package function;

import com.ZMPrinter.*;
import com.ZMPrinter.barcodes.BarcodeStyle;
import common.CommonClass;
import common.LogType;
import data_processing.LabelBuilder;
import server.ChannelMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class FuncLabelCreator {

    private final static ZMPrinterFunction function = new ZMPrinterFunctionImpl();
    private final static Map<String, FuncBody> bodyMap = new HashMap<>();

    public static void analysis(String remoteAddress, String msg) throws FunctionalException {
        String funcHead;
        String[] funcParams = null;
        if (msg.contains("|")) { //函数式调用有参
            String message = msg.replace("||", "[Divider]");
            String[] strArray = message.split("\\|");//"|";//字符串分隔符
            funcHead = strArray[0];
            funcParams = strArray;
        } else funcHead = msg;

        FuncBody funcBody = bodyMap.containsKey(remoteAddress) ? bodyMap.get(remoteAddress) : new FuncBody();

        switch (funcHead) {
            // 开启打印流程
            case "OpenPort": {
                funcBody.init();
                if (funcParams != null) {
                    try {
                        int sn = Integer.parseInt(CommonClass.localSN);
                        funcBody.getPrinter().printermbsn = String.valueOf(sn);
                    } catch (NumberFormatException e) {
                        funcBody.getPrinter().printermbsn = "";
                    }
                    try {
                        int printerPort = Integer.parseInt(funcParams[1]); //打印机端口，如果不是数字的，则认为是打印机驱动名
                        if (printerPort == 255) {
                            funcBody.getPrinter().printername = "USB打印机";
                            funcBody.getPrinter().printerinterface = PrinterStyle.USB;
                        }
                    } catch (NumberFormatException e) {
                        funcBody.getPrinter().printername = funcParams[1];//设置打印机驱动名
                        CommonClass.saveLog(remoteAddress + "    打印机驱动名：" + funcParams[1], LogType.ServiceData);
                    }
                }
                bodyMap.put(remoteAddress, funcBody);
                break;
            }
            // 指定序列号开启打印流程
            case "OpenPortUsbSN": {
                funcBody.init();
                if (funcParams != null) {
                    funcBody.getPrinter().printermbsn = funcParams[1];
                }
                bodyMap.put(remoteAddress, funcBody);
                break;
            }
            // 关闭端口
            case "ClosePort": {
                bodyMap.remove(remoteAddress);
                break;
            }
            //打印Base64编码图片
            //图片左上角的起始坐标X，Y，图片的宽度、高度、最后是图片的Base64字符串，单位都是dots
            //图片左上角的起始坐标X，Y，图片的Base64字符串，单位都是dots
            case "PrintBase64Image": {
                //socket.send('PrintBase64Image|24|24|400|200|'+ base64);
                //socket.send('PrintBase64Image|24|24|'+ base64);
                ZMLabelobject image = new ZMLabelobject();
                image.ObjectName = "image";
                if (funcParams != null) {
                    try {
                        image.Xposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();
                        image.Yposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();

                        if (funcParams.length == 4 || funcParams.length == 6) {
                            String imageBase64 = funcParams.length == 4 ? funcParams[3] : funcParams[5];
                            System.out.println(imageBase64);
                            if (imageBase64.contains(","))
                                imageBase64 = imageBase64.substring(imageBase64.lastIndexOf(",") + 1);//去掉base64字符串头
                            if (funcParams.length == 4) {
                                //不指定图片尺寸
                                image.imagedata = function.buildBase64Image(imageBase64, 0, 0);
                            } else {
                                //指定图片尺寸
                                int width = Integer.parseInt(funcParams[3]);
                                int height = Integer.parseInt(funcParams[4]);
                                image.imagedata = function.buildBase64Image(imageBase64, width, height);
                            }
                        }
                    } catch (NumberFormatException e) {
                        throw new FunctionalException("4001|PrintBase64Image参数异常:" + e.getMessage());
                    } catch (IOException e) {
                        throw new FunctionalException("4005|图片IO异常:" + e.getMessage());
                    }
                    funcBody.getLabelObjects().add(image);
                }
                break;
            }
            //打印一张指定路径的图片
            //图片左上角的起始坐标X，Y，最后是图片的路径（支持网络路径）
            //图片左上角的起始坐标X，Y，图片的宽度、高度、最后是图片的路径（支持网络路径）
            case "PrintBase64ImageURL": {
                if (funcParams != null) {
                    //socket.send('PrintBase64ImageURL|'+parseInt(45*dot+0.5)+'|'+parseInt(25*dot+0.5)+'|C:/Users/lkf/Desktop/test.jpg');
                    //socket.send('PrintBase64ImageURL|'+parseInt(3*dot+0.5)+'|'+parseInt(26*dot+0.5)+'|'+parseInt(50*dot+0.5)+'|'+parseInt(6*dot+0.5)+'|http://www.xxx.com.cn/logo.png ');
                    ZMLabelobject imageURL = new ZMLabelobject();
                    try {
                        imageURL.Xposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();
                        imageURL.Yposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();

                        if (funcParams.length == 4 || funcParams.length == 6) {
                            String pathUrl = funcParams.length == 4 ? funcParams[3] : funcParams[5];
                            BufferedImage bufferedImage = pathUrl.toLowerCase().startsWith("http") ? ImageIO.read(new URL(pathUrl)) : ImageIO.read(new File(pathUrl));

                            if (funcParams.length == 4) {
                                //不指定图片尺寸
                                imageURL.imagedata = function.getImageBytes(bufferedImage, 0, 0);
                            } else {
                                //指定图片尺寸
                                int width = Integer.parseInt(funcParams[3]);
                                int height = Integer.parseInt(funcParams[4]);
                                imageURL.imagedata = function.getImageBytes(bufferedImage, width, height);
                            }
                        }
                    } catch (NumberFormatException e) {
                        throw new FunctionalException("4001|PrintBase64Image参数异常:" + e.getMessage());
                    } catch (IOException e) {
                        throw new FunctionalException(e.getMessage());
                    }
                    funcBody.getLabelObjects().add(imageURL);
                }
                break;
            }
            //预览标签
            //预览标签。参数1为打印机分辨率；参数2为图片的宽度（高度自动按比例缩放），如果参数2值为0则原始尺寸；参数3为边框的粗细，0为不画边框。
            case "ZM_PrintLabel_Preview": {
                //socket.send('ZM_PrintLabel_Preview|' + printDPI + '|0|1');
                try {
                    ZMPrinter printer = funcBody.getPrinter();
                    ZMLabel label = funcBody.getLabel();
                    List<ZMLabelobject> labelObjects = funcBody.getLabelObjects();
                    int border;
                    if (funcParams != null) {
                        funcBody.getPrinter().printerdpi = Integer.parseInt(funcParams[1]);//打印分辨率
                        border = funcParams.length > 3 ? Integer.parseInt(funcParams[3]) : 0;
                    } else {
                        border = 0;
                    }
                    LabelBuilder.preview(printer, label, labelObjects, remoteAddress, border);
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_PrintLabel_Preview参数异常:" + e.getMessage());
                }
                break;
            }
            case "ZM_PrintLabel_Storage": {
                //生成标签的打印数据
//                try {
//                    funcBody.getPrinter().copynum = Integer.parseInt(strings[1]);
//                } catch (Exception e) {
//                    funcBody.getPrinter().copynum = 1;
//                }
//                byte[] oneLabel = printUtility.CreateLabelCommand(printer, label, labelObjects);//生成一张页面的标签数据（可能多行多列）
//                printLabels.add(oneLabel);*/
                System.out.println("生成标签的打印数据");
                break;
            }
            //设置分辨率
            case "ZM_SetPrinterDPI": {
                try {
                    if (funcParams != null) {
                        funcBody.getPrinter().printerdpi = Integer.parseInt(funcParams[1]);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_SetPrinterDPI参数异常:" + e.getMessage());
                }
                break;
            }
            //设置打印速度
            case "ZM_SetPrintSpeed": {
                try {
                    if (funcParams != null) {
                        funcBody.getPrinter().printSpeed = Integer.parseInt(funcParams[1]);
                    }
                } catch (Exception e) {
                    throw new FunctionalException("4001|ZM_SetPrintSpeed参数异常:" + e.getMessage());
                }
                break;
            }
            //设置打印黑度
            case "ZM_SetDarkness": {
                try {
                    if (funcParams != null) {
                        funcBody.getPrinter().printDarkness = Integer.parseInt(funcParams[1]);
                    }
                } catch (Exception e) {
                    throw new FunctionalException("4001|ZM_SetDarkness参数异常:" + e.getMessage());
                }
                break;
            }
            //设置标签宽度高度
            case "ZM_SetLabelWidth": {
                try {
                    if (funcParams != null) {
                        funcBody.getLabel().labelwidth = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//标签高度，单位是mm
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_SetLabelWidth参数异常:" + e.getMessage());
                }
                break;
            }
            case "ZM_SetLabelHeight": {
                try {
                    if (funcParams != null) {
                        funcBody.getLabel().labelheight = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//标签高度，单位是mm
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_SetLabelHeight参数异常:" + e.getMessage());
                }
                break;
            }
            //设置内置字体
            case "ZM_DrawText": {
                //socket.send('ZM_DrawText|50|30|0|a|1|1|N|测试文字A1');
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        labelObject.ObjectName = "text";
                        labelObject.Xposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//X坐标，单位mm
                        labelObject.Yposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();//Y坐标，单位mm
                        labelObject.direction = Integer.parseInt(funcParams[3]);
                        labelObject.blackbackground = funcParams[7].equals("R");//黑底白字

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[8]);
                        //自动计算字体大小
                        function.setFontSize(labelObject, funcParams[4], funcParams[5]);
                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4004|ZM_DrawText参数异常:" + e.getMessage());
                }
                break;
            }
            //设置TrueType字体
            case "ZM_DrawTextTrueTypeW": {
                //socket.send('ZM_DrawTextTrueTypeW|50|30|30|0|微软雅黑|1|400|0|0|0|A1|测试文字A1');
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        labelObject.ObjectName = "truetype";
                        labelObject.Xposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//X坐标，单位mm
                        labelObject.Yposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();//Y坐标，单位mm
                        labelObject.textfont = funcParams[5];//字体名称
                        int dire = Integer.parseInt(funcParams[6]);
                        if (dire < 5) {
                            labelObject.direction = dire - 1;
                        } else if (dire < 9) {
                            dire -= 5;
                            //90和270反转
                            if (dire == 1) dire = 3;
                            else if (dire == 3) dire = 1;
                            labelObject.direction = dire;
                            labelObject.texttextalign = 1;
                        }

                        int weight = Integer.parseInt(funcParams[7]);
                        int fontX = Integer.parseInt(funcParams[8]);
                        if (weight < 500) {
                            labelObject.fontstyle = fontX > 0 ? 2 : 0;//字体样式,0 正常体，1 粗体，2 斜体，3 斜粗体
                        } else {
                            labelObject.fontstyle = fontX > 0 ? 3 : 1;//字体样式,0 正常体，1 粗体，2 斜体，3 斜粗体
                        }

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[12]);
                        //自动判断字号
                        BufferedImage checkImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D = checkImage.createGraphics();

                        int fontHeightDots = Integer.parseInt(funcParams[3]);//字体的高度，单位是点
                        float newFontSize = 1;//起始字号
                        int textHeight = 0;

                        while (textHeight < fontHeightDots) {
                            FontMetrics fm = graphics2D.getFontMetrics(function.getFontResize(labelObject, newFontSize, funcBody.getPrinter().printerdpi));
                            textHeight = fm.getHeight();//得到字符的高度
                            newFontSize += 0.2f;
                        }
                        graphics2D.dispose();
                        labelObject.fontsize = newFontSize;//字体大小

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4004|DrawTrueType参数异常:" + e.getMessage());
                }
                break;
            }
            //设置一维条码
            case "ZM_DrawBarcode": {
                //socket.send('ZM_DrawBarcode|100|200|0|1|3|3|60|78|DrawBarcode');//打印一维条码
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[3]);

                        String kind = funcParams[4];//条码类型
                        switch (kind) {
                            case "1":
                                labelObject.barcodekind = BarcodeStyle.Code_128_Auto;
                                break;
                            case "1A":
                                labelObject.barcodekind = BarcodeStyle.Code_128_A;
                                break;
                            case "1B":
                                labelObject.barcodekind = BarcodeStyle.Code_128_B;
                                break;
                            case "1C":
                                labelObject.barcodekind = BarcodeStyle.Code_128_C;
                                break;
                            case "30":
                                labelObject.barcodekind = BarcodeStyle.EAN13;
                                break;
                            case "3":
                                labelObject.barcodekind = BarcodeStyle.Code_39;
                                break;
                            case "3E":
                                labelObject.barcodekind = BarcodeStyle.Code_39_Extended;
                                break;
                            case "9":
                                labelObject.barcodekind = BarcodeStyle.Code_93;
                                break;
                            default:
                                throw new FunctionalException("3006|未知的条码类型");
                        }

                        labelObject.barcodescale = Integer.parseInt(funcParams[5]);
                        labelObject.code39widthratio = Integer.parseInt(funcParams[6]) / Integer.parseInt(funcParams[5]);//39码的条宽比
                        labelObject.barcodeheight = Integer.parseInt(funcParams[7]) / (funcBody.getPrinter().printerdpi / 25.4f);//条码高度，单位mm

                        //判断是否需要显示条码下方文字
                        function.checkTextPositionForBarcode(labelObject, funcParams[8]);

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[9]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|BARCODE参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //设置QR二维条码(固定形状大小)
            case "ZM_DrawBar2D_QR_SIZE": {
                //socket.send('ZM_DrawBar2D_QR_SIZE|32|24|3|0|5|4|0|8|P200213-B15\\x0D\\x0A http://www.baidu.com');//打印QR二维条码，\\x0D\\x0A是回车换行
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[4]);

                        labelObject.qrversion = Integer.parseInt(funcParams[3]);
                        labelObject.barcodekind = BarcodeStyle.QR_Code;
                        labelObject.barcodescale = Integer.parseInt(funcParams[5]);
                        labelObject.errorcorrection = Integer.parseInt(funcParams[7]);

                        //判断是否需要显示条码下方文字
                        function.checkTextPositionForBarcode(labelObject, funcParams[8]);

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[9]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|QR_CODE参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //设置QR二维条码
            case "ZM_DrawBar2D_QR": {
                //socket.send('ZM_DrawBar2D_QR|32|24|0|0|0|5|4|0|8|P200213-B15\\x0D\\x0A http://www.baidu.com');//打印QR二维条码，\\x0D\\x0A是回车换行
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[5]);

                        labelObject.barcodekind = BarcodeStyle.QR_Code;
                        labelObject.barcodescale = Integer.parseInt(funcParams[6]);
                        labelObject.errorcorrection = Integer.parseInt(funcParams[7]);

                        //判断是否需要显示条码下方文字
                        function.checkTextPositionForBarcode(labelObject, funcParams[8]);

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[10]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|QR_CODE参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //设置DATAMATRIX二维码
            case "ZM_DrawBar2D_DATAMATRIX": {
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[5]);

                        labelObject.barcodekind = BarcodeStyle.Data_Matrix;
                        labelObject.barcodescale = Integer.parseInt(funcParams[6]);

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[7]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|DATAMATRIX参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //设置PDF417二维码
            case "ZM_DrawBar2D_Pdf417": {
                //|x|y|0|0|0|0|3|7|10|2|0|0|PDF417-2021-11-22
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[12]);

                        labelObject.barcodekind = BarcodeStyle.PDF_417;
                        labelObject.errorcorrection = Integer.parseInt(funcParams[5]);
                        labelObject.barcodescale = Integer.parseInt(funcParams[7]);//横向缩放倍数
                        labelObject.barcodeheight = Float.parseFloat(funcParams[8]);//条码的高度，单位是mm
                        labelObject.textposition = 2;//文字在条码的方位，2为不显示
                        labelObject.pdf417_columns = 0;
                        labelObject.pdf417_rows = 0;
                        labelObject.charencoding = 0;//0为UTF-8，1为GB2312

                        //添加变量内容
                        function.addVariables(labelObject, funcParams[13]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|Pdf417参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //设置PDF417二维码_新
            case "ZM_DrawBar2D_Pdf417_New": {
                //ZM_DrawBar2D_Pdf417_New|x|y|width|height|0|0|0|PDF417内容-2021-11-22
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        function.setBarcodeInfo(labelObject, funcBody.getPixel(), funcParams[1], funcParams[2], funcParams[6]);

                        labelObject.barcodekind = BarcodeStyle.PDF_417;
                        labelObject.errorcorrection = Integer.parseInt(funcParams[5]);//纠错等级0~8
                        labelObject.charencoding = Integer.parseInt(funcParams[7]);//编码方式：0为UTF-8，1为GB2312
                        labelObject.barcodealign = 1;//0:左对齐，1:居中对齐，2:右对齐
                        labelObject.barcodeWidth = Integer.parseInt(funcParams[3]) / (funcBody.getPrinter().printerdpi / 25.4f);//条码的宽度，单位是mm
                        labelObject.barcodeheight = Integer.parseInt(funcParams[4]) / (funcBody.getPrinter().printerdpi / 25.4f);//条码的高度，单位是mm
                        labelObject.textposition = 2;//文字在条码的方位，2为不显示
                        labelObject.pdf417_columns = 0;
                        labelObject.pdf417_rows = 0;

                        //region 添加变量内容
                        function.addVariables(labelObject, funcParams[8]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|Pdf417参数必须为数字:" + e.getMessage());
                }
                break;
            }
            //画线
            case "ZM_DrawLineOr":
            case "ZM_DrawLineXor": {
                //socket.send('ZM_DrawLineXor|45|70|555|3');//画直线
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();

                        labelObject.ObjectName = "line";
                        labelObject.startXposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//单位mm
                        labelObject.startYposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();//单位mm

                        int lineWidth = Integer.parseInt(funcParams[3]);
                        int lineHeight = Integer.parseInt(funcParams[4]);
                        if (lineWidth > lineHeight) {//横线
                            labelObject.endXposition = labelObject.startXposition + lineWidth / funcBody.getPixel();//单位mm
                            labelObject.endYposition = labelObject.startYposition;
                            labelObject.lineWidth = lineHeight / funcBody.getPixel();
                        } else {//竖线
                            labelObject.endXposition = labelObject.startXposition;//单位mm
                            labelObject.endYposition = labelObject.startYposition + lineHeight / funcBody.getPixel();
                            labelObject.lineWidth = lineWidth / funcBody.getPixel();
                        }

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_DrawLineXor必须为数字:" + e.getMessage());
                }
                break;
            }
            //画矩形
            case "ZM_DrawRectangle": {
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();

                        labelObject.ObjectName = "rectangle";
                        labelObject.startXposition = Integer.parseInt(funcParams[1]) / funcBody.getPixel();//单位mm
                        labelObject.startYposition = Integer.parseInt(funcParams[2]) / funcBody.getPixel();//单位mm
                        labelObject.lineWidth = Integer.parseInt(funcParams[3]) / funcBody.getPixel();//单位mm
                        labelObject.endXposition = Integer.parseInt(funcParams[4]) / funcBody.getPixel();//单位mm
                        labelObject.endYposition = Integer.parseInt(funcParams[5]) / funcBody.getPixel();

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_DrawRectangle必须为数字:" + e.getMessage());
                }
            }
            //设置失败重试次数
            case "ZM_SetRFTagRetryCount": {
                if (funcParams != null) {
                    String[] finalParams = funcParams;
                    try {
                        funcBody.getLabelObjects().forEach(labelObject -> {
                            if (labelObject.ObjectName.startsWith("rfiduhf"))
                                labelObject.RFIDerrortimes = Integer.parseInt(finalParams[1]);
                        });
                    } catch (NumberFormatException e) {
                        throw new FunctionalException("4001|ZM_SetRFTagRetryCount必须为数字:" + e.getMessage());
                    }
                }
                break;
            }
            // 查询打印机名称和序列号
            case "ZM_GetPrinterName":
            case "ZM_GetPrinterNameAndSN": {
                String message;
                if (CommonClass.localSN.isEmpty()) {
                    message = funcParams == null ? function.getNameAndSn() : function.getNameAndSn(funcParams[1]);
                } else {
                    message = function.getNameAndSn(CommonClass.localSN);
                }
                ChannelMap.writeMessageToClient(remoteAddress, "PrinterInfo:" + message);
                break;
            }
            // 单张打印
            case "ZM_PrintLabel_CUPS":
            case "ZM_PrintLabel_Driver": {
                PrinterOperator printerOperator = new PrinterOperatorImpl();
                byte[] commands = funcBody.buildLabelCommand();
                printerOperator.sendToPrinterJob(funcBody.getPrinter().printername, commands);
                ChannelMap.writeMessageToClient(remoteAddress, "打印完成(finished)");
                break;
            }
            case "ZM_PrintLabel_R":// USB和NET的
            case "ZM_PrintLabel": {
                PrinterOperator printerOperator = new PrinterOperatorImpl();
                byte[] commands = funcBody.buildLabelCommand();
                int len = commands.length;
                String printResult;
                if (CommonClass.localSN.isEmpty()) {
                    if (funcBody.getPrinter().printermbsn.isEmpty() && funcBody.getPrinter().printernetip.isEmpty()) {
                        //sn和ip都没设置默认选中第一个USB打印机
                        List<String> serials = printerOperator.getPrinters();
                        if (serials.isEmpty()) {
                            CommonClass.saveAndShow("未连接打印机", LogType.ServiceData);
                            break;
                        } else {
                            printResult = printerOperator.sendToPrinter(serials.get(0), commands, len, 1);
                        }
                    } else if (funcBody.getPrinter().printermbsn.isEmpty()) {
                        //设了网络打印机ip
                        printResult = printerOperator.sendToPrinter(funcBody.getPrinter().printernetip, commands);
                    } else {
                        printResult = printerOperator.sendToPrinter(funcBody.getPrinter().printermbsn, commands, len, 1);
                    }
                } else {
                    printResult = printerOperator.sendToPrinter(CommonClass.localSN, commands, len, 1);
                }

                if (printResult.equals("0")) {
                    ChannelMap.writeMessageToClient(remoteAddress, "打印完成(finished)");
                } else {
                    throw new FunctionalException("4006|未知异常(函数没有抛出异常但是结果异常),返回值:" + printResult);
                }
                break;
            }
            // 查询打印机状态
            case "ZM_ErrorReportEx":
            case "ZM_ErrorReport_USB":
            case "ZM_GetPrinterStatus":
            case "ZM_GetPrinterStatus_NET":
            case "ZM_GetPrinterStatus_USB": {
                //ZM_GetPrinterStatus_USB|500或者ZM_GetPrinterStatus_USB|500|serial
                //不允许无参
                if (funcParams != null) {
                    String addr;
                    if (CommonClass.localSN.isEmpty()) {
                        try {
                            // 如果长度为3判定为最后一组参数是地址,否则是第二组
                            long serial = funcParams.length == 3 ? Long.parseLong(funcParams[2]) : Long.parseLong(funcParams[1]);
                            // 如果为500,则用""作为地址
                            addr = (serial == 500 || serial == 0) ? "" : String.valueOf(serial);
                            String status = function.getPrinterStatus(addr);
                            ChannelMap.writeMessageToClient(remoteAddress, "PrinterStatus_USB:" + status); // 必定是0,如果不是会被catch
                        } catch (NumberFormatException ex) {
                            // 未通过Parse,catch为ip
                            addr = funcParams.length == 3 ? funcParams[2] : funcParams[1];
                            String status = function.getPrinterStatus(addr);
                            ChannelMap.writeMessageToClient(remoteAddress, "PrinterStatus_NET:" + status); // 必定是0,如果不是会被catch
                        }
                    } else {
                        String status = function.getPrinterStatus(CommonClass.localSN);
                        ChannelMap.writeMessageToClient(remoteAddress, "PrinterStatus_USB:" + status); // 必定是0,如果不是会被catch
                    }
                }
                break;
            }
            case "ZM_RW_RfidFormat": {
                // RFID超高频写入数据
                //socket.send('ZM_RW_RfidFormat|1|0|0|4|1|31323334');//写入数据
                try {
                    if (funcParams != null) {
                        ZMLabelobject labelObject = new ZMLabelobject();
                        labelObject.ObjectName = "rfiduhf";
                        labelObject.ReadIDOnly = false;//非读，即为写
                        labelObject.RFIDEncodertype = 0;//0为UHF，1为HF 15693，2为HF 14443，3为NFC
                        if (Integer.parseInt(funcParams[5]) == 1)
                            labelObject.RFIDDatablock = 0;//数据区：0为EPC，1为USER
                        else
                            labelObject.RFIDDatablock = 1;//数据区：0为EPC，1为USER

                        // 添加变量内容
                        function.addVariables(labelObject, funcParams[6]);

                        funcBody.getLabelObjects().add(labelObject);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_RW_RfidFormat参数异常:" + e.getMessage());
                }

                break;
            }
            // 读取超高频标签
            case "ZM_GetUHFTagData_NET":
            case "ZM_GetUHFTagData_USB":
            case "ZM_GetUHFTagData": {
                //socket.send('ZM_GetUHFTagData_USB|1|0|0|3|2000|type');//读取超高频标签数据
                //参数说明：参数1为10位打印机主板序号或者ip（只连接1台打印机可设置为1,如果是网络打印机可以ip）
                //        参数2为需要操作的标签区域（0为TID，1为EPC，2为TID+EPC）；
                //        参数3为模块读取功率(值为0~25dBm，0为不指定，使用打印机已经设定的功率)；
                //        参数4为读取操作完成后标签停止位置（0为回到原始位置，1为走纸到撕纸位置，2为走纸到打印位置，3为走纸到写入位置（需要写数据））；
                //        参数5为读取超时时间，单位毫秒（建议设置值为2000） 无效
                //        参数6为标签类型，1-UHF, 2-GJB, 3-GB, 4-GM
                try {
                    if (funcParams != null) {
                        Map<String, Integer> configuration = new HashMap<>();
                        int area = Integer.parseInt(funcParams[2]);
                        int feed = Integer.parseInt(funcParams[4]);
                        configuration.put("area", area);
                        configuration.put("feed", feed);

                        Integer timeout = Integer.parseInt(funcParams[5]);

                        LabelType labelType = setLabelType(funcParams.length == 7 ? Integer.parseInt(funcParams[6]) : 1);

                        String tagData = function.readTagData(funcParams[1], labelType, configuration, timeout, 1);
                        ChannelMap.writeMessageToClient(remoteAddress, "UHFTagData:" + tagData);
                        CommonClass.saveLog("UHFTagData:" + tagData, LogType.ServiceData);
                    }
                } catch (NumberFormatException e) {
                    throw new FunctionalException("4001|ZM_GetUHFTagData参数异常:" + e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new FunctionalException("4002|函数式调用参数异常:" + e.getMessage());
                }
                break;
            }
            // 读取高频标签
            case "ZM_GetHFTagData_NET":
            case "ZM_GetHFTagData_USB":
            case "ZM_GetHFTagData": {
                //socket.send('ZM_GetHFTagData|1|0|0|3|2000');//读取高频标签数据，目前只能读UID
                //参数说明：参数1为10位打印机主板序号或者ip（只连接1台打印机可设置为1,如果是网络打印机可以ip）
                //        参数2为需要操作的标签区域（0为UID，其他不支持）
                //        参数3为模块读取功率(0:12db, 1:24db, 2:36db, 3:48db)
                //        参数4为读取操作完成后标签停止位置（0为回到原始位置，1为走纸到撕纸位置，2为走纸到打印位置，3为走纸到写入位置（需要写数据））
                //        参数5为读取超时时间，单位毫秒（建议设置值为2000） 无效
                try {
                    if (funcParams != null) {
                        Map<String, Integer> configuration = new HashMap<>();
                        int power = Integer.parseInt(funcParams[3]);
                        int feed = Integer.parseInt(funcParams[4]);
                        configuration.put("power", power);
                        configuration.put("feed", feed);
                        Integer timeout = Integer.parseInt(funcParams[5]);

                        String tagData = function.readTagData(funcParams[1], LabelType.HF, configuration, timeout, 1);
                        ChannelMap.writeMessageToClient(remoteAddress, "HFTagData:" + tagData);
                    }
                } catch (NumberFormatException e) {
                    String message = "4001|ZM_GetHFTagData参数异常:" + e.getMessage();
                    throw new FunctionalException(message);
                } catch (IllegalAccessException e) {
                    String message = "4002|函数式调用参数异常:" + e.getMessage();
                    throw new FunctionalException(message);
                }
                break;
            }
            // 继续打印
            case "StartPrint": {
                ChannelMap.startQueue(remoteAddress);
                break;
            }
            // 清空打印队列
            case "ClearPrintList": {
                ChannelMap.cleanQueue(remoteAddress);
                break;
            }
            // 清空标签内容
            case "ZM_ClearBuffer": {
                funcBody.getLabelObjects().clear();
                break;
            }
            default: {
                throw new FunctionalException("4003|未定义的函数:" + funcHead);
            }
        }
    }

    private static LabelType setLabelType(int typeCode) throws FunctionalException {
        switch (typeCode) {
            case 1:
                return LabelType.UHF;
            case 2:
                return LabelType.GJB;
            case 3:
                return LabelType.GB;
            case 4:
                return LabelType.GM;
            default:
                throw new FunctionalException("4001|LabelType参数异常");
        }
    }
}
