package layout;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class LayoutUtils {
    public static String getPrinterMessage(String addr, byte[] data) {
        PrinterOperator printerOperator = new PrinterOperatorImpl();
        String dataRead;
        if (addr.contains(".")) {
            // 带.的是IP地址
            dataRead = printerOperator.sendAndReadPrinter(addr, data, 12301, "127.0.0.1");
            dataRead = dataRead.replace("\u0002", "").replace("\u0003", "").replace("\r", "").replace("\n", "");
        } else {
            dataRead = printerOperator.sendAndReadPrinter(addr, data, data.length, 2000, 1).replace("\r", "").replace("\n", "");
        }
        return dataRead;
    }

    public static int getFontsize(BufferedImage calibrationImage) {
        int fontsize;
        if (calibrationImage.getHeight() < 150)
            fontsize = 2;
        else if (calibrationImage.getHeight() < 250)
            fontsize = 3;
        else if (calibrationImage.getHeight() < 350)
            fontsize = 4;
        else if (calibrationImage.getHeight() < 450)
            fontsize = 5;
        else
            fontsize = 6;
        return fontsize;
    }

    //从校准数据中分析得出第1,2个标签的高度和间隙,错误或异常返回null,正常返回Map
    public static Map<String, Float> GetCalibraLabelHeight(String[] calibrationDataList, int mediaSensor, int adThreshold, float printerDpi) {
        Map<String, Float> map = new HashMap<>();
        float labelHeight;
        float labelGap;
        DecimalFormat df = new DecimalFormat(".00");//保留两位小数

        try {
            // 找出标签探测信号的最小值与最大值,计算出每个等级的跨度
            int levelStepValue = getLevelStepValue(calibrationDataList);
            if (levelStepValue == 0)//没有校准数据
            {
                return null;
            }
            //endregion

            boolean startIsLabel = false;//开始是标签
            boolean startIsGap = false;//开始是间隙

            boolean startLabelFound = false;//是否找到了第一个标签起始位置
            boolean startGapFound = false;//是否找到了第一个间隙起始位置
            boolean endLabelFound = false;//是否找到了第一个标签结束位置
            boolean endGapFound = false;//是否找到了第一个间隙结束位置

            int gapStartIndex = 0;//第一个间隙在数据数组中的起始位置
            int labelStartIndex = 0;//第一个标签在数据数组中的起始位置
            int gapEndIndex = 0;//第一个间隙在数据数组中的结束位置
            int labelEndIndex = 0;//第一个标签在数据数组中的结束位置

            //***********************************************************
            boolean startIsLabel2 = false;//开始是标签
            boolean startIsGap2 = false;//开始是间隙

            boolean startLabelFound2 = false;//是否找到了第2个标签起始位置
            boolean startGapFound2 = false;//是否找到了第2个间隙起始位置
            boolean endLabelFound2 = false;//是否找到了第2个标签结束位置
            boolean endGapFound2 = false;//是否找到了第2个间隙结束位置

            int gapStartIndex2 = 0;//第2个间隙在数据数组中的起始位置
            int labelStartIndex2 = 0;//第2个标签在数据数组中的起始位置
            int gapEndIndex2 = 0;//第2个间隙在数据数组中的结束位置
            int labelEndIndex2 = 0;//第2个标签在数据数组中的结束位置

            for (int i = 0; i < calibrationDataList.length - 1; i++)//遍历所有的标签探测数据,calibrationDataList最后一个值为空
            {
                if (i == 0)//第一个数据
                {
                    //region 判断开始是标签还是间隙
                    if (mediaSensor == 1)//反射式感应器
                    {
                        if (Integer.parseInt(calibrationDataList[i]) > adThreshold)//第一个值大于阈值,反射式感应器,大于阈值的认为是标签
                        {
                            startIsLabel = true;
                            startIsGap = false;
                        } else {
                            startIsLabel = false;
                            startIsGap = true;
                        }
                    } else//穿透式感应器
                    {
                        if (Integer.parseInt(calibrationDataList[i]) < adThreshold)//第一个值小于阈值,穿透式感应器,小于阈值的认为是标签
                        {
                            startIsLabel = true;
                            startIsGap = false;
                        } else {
                            startIsLabel = false;
                            startIsGap = true;
                        }
                    }
                    //endregion
                } else//后面的数据
                {
                    //mediaSensor值0是穿透式,1是反射式
                    if (mediaSensor == 1)//反射式
                    {
                        if (!startGapFound && !endGapFound && startIsLabel && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第一个间隙的开始
                        {
                            startIsLabel = false;
                            startIsGap = true;
                            gapStartIndex = i;
                            startGapFound = true;
                        } else if (!startLabelFound && !endLabelFound && startIsGap && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第一个标签的开始
                        {
                            startIsLabel = true;
                            startIsGap = false;
                            labelStartIndex = i;
                            startLabelFound = true;
                        } else if (startGapFound && !endGapFound && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第一个间隙的结束
                        {
                            gapEndIndex = i;
                            endGapFound = true;
                            startIsLabel2 = true;
                            startIsGap2 = false;
                        } else if (startLabelFound && !endLabelFound && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第一个标签的结束
                        {
                            labelEndIndex = i;
                            endLabelFound = true;
                            startIsLabel2 = false;
                            startIsGap2 = true;
                        }//**********************************************************************************************************************
                        else if (startGapFound && endGapFound && !startGapFound2 && !endGapFound2 && startIsLabel2 && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第2个间隙的开始
                        {
                            startIsLabel2 = false;
                            startIsGap2 = true;
                            gapStartIndex2 = i;
                            startGapFound2 = true;
                        } else if (startGapFound && endGapFound && !startLabelFound2 && !endLabelFound2 && startIsGap2 && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第2个标签的开始
                        {
                            startIsLabel2 = true;
                            startIsGap2 = false;
                            labelStartIndex2 = i;
                            startLabelFound2 = true;
                        } else if (startGapFound && endGapFound && startGapFound2 && !endGapFound2 && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第2个间隙的结束
                        {
                            gapEndIndex2 = i;
                            endGapFound2 = true;
                        } else if (startGapFound && endGapFound && startLabelFound2 && !endLabelFound2 && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第2个标签的结束
                        {
                            labelEndIndex2 = i;
                            endLabelFound2 = true;
                        }
                    } else//穿透式
                    {
                        if (!startGapFound && !endGapFound && startIsLabel && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第一个间隙的开始
                        {
                            startIsLabel = false;
                            startIsGap = true;
                            gapStartIndex = i;
                            startGapFound = true;
                        } else if (!startLabelFound && !endLabelFound && startIsGap && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第一个标签的开始
                        {
                            startIsLabel = true;
                            startIsGap = false;
                            labelStartIndex = i;
                            startLabelFound = true;
                        } else if (startGapFound && !endGapFound && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第一个间隙的结束
                        {
                            gapEndIndex = i;
                            endGapFound = true;
                            startIsLabel2 = true;
                            startIsGap2 = false;
                        } else if (startLabelFound && !endLabelFound && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第一个标签的结束
                        {
                            labelEndIndex = i;
                            endLabelFound = true;
                            startIsLabel2 = false;
                            startIsGap2 = true;
                        }//************************************************************************************************************************
                        else if (startGapFound && endGapFound && !startGapFound2 && !endGapFound2 && startIsLabel2 && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第2个间隙的开始
                        {
                            startIsLabel2 = false;
                            startIsGap2 = true;
                            gapStartIndex2 = i;
                            startGapFound2 = true;
                        } else if (startGapFound && endGapFound && !startLabelFound2 && !endLabelFound2 && startIsGap2 && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第2个标签的开始
                        {
                            startIsLabel2 = true;
                            startIsGap2 = false;
                            labelStartIndex2 = i;
                            startLabelFound2 = true;
                        } else if (startGapFound && endGapFound && startGapFound2 && !endGapFound2 && Integer.parseInt(calibrationDataList[i]) < adThreshold - levelStepValue)//碰到第2个间隙的结束
                        {
                            gapEndIndex2 = i;
                            endGapFound2 = true;
                        } else if (startGapFound && endGapFound && startLabelFound2 && !endLabelFound2 && Integer.parseInt(calibrationDataList[i]) > adThreshold + levelStepValue)//碰到第2个标签的结束
                        {
                            labelEndIndex2 = i;
                            endLabelFound2 = true;
                        }
                    }
                }
            }

            if (endLabelFound && endGapFound && endLabelFound2 && endGapFound2)//找到2张标签
            {
                int labelDots = labelEndIndex - labelStartIndex + 1;//第一个标签的高度点数
                int gapDots = gapEndIndex - gapStartIndex + 1;//第一个间隙的高度点数

                int labelDots2 = labelEndIndex2 - labelStartIndex2 + 1;//第2个标签的高度点数
                int gapDots2 = gapEndIndex2 - gapStartIndex2 + 1;//第2个间隙的高度点数

                //如果探测的前两张数据标签高度差距大于20%,则认为不正常
                if (1f * (Math.abs(labelDots2 - labelDots)) / labelDots2 > 0.2f)
                    labelDots = 0;

                if (labelDots == 0 || gapDots == 0) {
                    labelHeight = 0;
                    labelGap = 0;
                } else//正常的数据,计算前2张标签的平均值
                {
                    int labelDotSaveRage = (int) ((labelDots + labelDots2) / 2f + 0.5f);
                    int gapDotSaveRage = (int) ((gapDots + gapDots2) / 2f + 0.5f);

                    labelHeight = Float.parseFloat(df.format(labelDotSaveRage / (printerDpi / 25.4f)));//校准得到的标签高度,单位是mm,保留2位小数
                    labelGap = Float.parseFloat(df.format(gapDotSaveRage / (printerDpi / 25.4f)));//校准得到的标签间隙,单位是mm,保留2位小数
                }
            } else if (endLabelFound && endGapFound)//只找到第一张标签
            {
                int labelDots = labelEndIndex - labelStartIndex + 1;//第一个标签的高度点数
                int gapDots = gapEndIndex - gapStartIndex + 1;//第一个间隙的高度点数

                labelHeight = Float.parseFloat(df.format(labelDots / (printerDpi / 25.4f)));//float)Math.Round(labelDots / (printerDPI / 25.4f), 2);//校准得到的标签高度,单位是mm,保留2位小数
                labelGap = Float.parseFloat(df.format(gapDots / (printerDpi / 25.4f)));//(float)Math.Round(gapDots / (printerDPI / 25.4f), 2);//校准得到的标签间隙,单位是mm,保留2位小数
            } else//没有找到标签
            {
                labelHeight = 0;
                labelGap = 0;
            }

        } catch (Exception ex) {
            return null;
        }

        map.put("labelHeight", labelHeight);
        map.put("labelGap", labelGap);
        return map;
    }

    private static int getLevelStepValue(String[] calibrationDataList) {
        int levelStepValue;
        int minValue = Integer.parseInt(calibrationDataList[0]), maxValue = Integer.parseInt(calibrationDataList[0]);
        for (int i = 1; i < calibrationDataList.length; i++) {
            if (!(calibrationDataList[i] == null || calibrationDataList[i].isEmpty())) {
                if (minValue > Integer.parseInt(calibrationDataList[i]))
                    minValue = Integer.parseInt(calibrationDataList[i]);
                if (maxValue < Integer.parseInt(calibrationDataList[i]))
                    maxValue = Integer.parseInt(calibrationDataList[i]);
            }
        }
        levelStepValue = (int) (1f * (maxValue - minValue) / 24f + 0.5f);//计算出每个等级的跨度
        return levelStepValue;
    }

}
