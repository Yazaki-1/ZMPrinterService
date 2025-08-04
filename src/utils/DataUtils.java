package utils;

import com.ZMPrinter.PrinterStyle;
import com.ZMPrinter.ZMLabelobject;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import data_processing.JsonData;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DataUtils {
    public static <T> List<T> castList(Object obj, Class<T> tClass) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(tClass.cast(o));
            }
        }
        return result;
    }

    public static JsonData fromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JsonData jsonData = jsonObject.toJavaObject(JsonData.class);
        if (jsonObject.containsKey("LabelObjectList")) {
            List<ZMLabelobject> labelObjectList = jsonData.getLabelObjectList();
            JSONArray array = jsonObject.getJSONArray("LabelObjectList");
            array.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                if (obj.containsKey("ObjectName")) {
                    String objectName = obj.getString("ObjectName");
                    if (objectName.contains("image")) {
                        String objName = obj.getString("ObjectName");
                        String objData = obj.getString("imagedata1");
                        long count = labelObjectList.stream().filter(f -> f.ObjectName.equals(objName)).count();
                        if (count > 1) {
                            throw new RuntimeException("Image名字不能相同！");
                        }
                        labelObjectList.stream()
                                .filter(f -> f.ObjectName.equals(objName))
                                .findAny()
                                .ifPresent(f -> {
                                    if (objData != null && !objData.isEmpty())
                                        f.imagedata = Base64.getDecoder().decode(objData);
                                });
                    }
                }
            });
        }
        if (jsonData.getOperator() == null) {
            jsonData.setOperator("print");
        }
        return jsonData;
    }

    public static String getConnectType(PrinterStyle printerStyle) {
        switch (printerStyle) {
            case RFID_DRIVER:
            case DRIVER:
                return "DRIVER";
            case RFID_USB:
            case GBGM_USB:
            case GJB_USB:
            case USB:
                return "USB";
            case RFID_NET:
            case GBGM_NET:
            case GJB_NET:
            case NET:
                return "NET";
        }
        return "USB";
    }

    //通过分析TID得到芯片的厂商和型号
    public static String getInlayChipName(String tid) {
        //参考资料：https://www.gs1.org/epcglobal/standards/mdid
        //根据标签TID判断芯片厂商：E2 8 27 80 2 2000 00000128ADF7
        //其中开头的E2，是ISO/IEC 15963分配类标识符，固定不变的
        //随后的8或者0，一位数字，标签是否实现扩展标签标识等标识，一般为0或者8
        //随后的27，两位数字，是打印机厂商的标识
        //随后的80，两位数字，是产品号，厂商自定义
        //随后的2，一位数字，是产品细分型号，厂商自定义

        String MDIDstring;//厂商标识
        String ModelString;//标签型号标识
        if (tid.startsWith("E2")) {
            MDIDstring = tid.substring(3, 3 + 2);
            ModelString = tid.substring(5, 5 + 3);
        } else {
            MDIDstring = tid.substring(1, 1 + 2);
            ModelString = tid.substring(3, 3 + 3);
        }

        String manufactureName;//厂商名称，用英文
        String modelName;//标签型号

        switch (MDIDstring) {
            case "01":
                manufactureName = "ImpinJ";
                switch (ModelString) {
                    case "093":
                        modelName = "Monza 3";
                        break;
                    case "105":
                        modelName = "Monza 4QT";
                        break;
                    case "104":
                        modelName = "Monza 4U";
                        break;
                    case "10C":
                        modelName = "Monza 4E";
                        break;
                    case "100":
                        modelName = "Monza 4D";
                        break;
                    case "114":
                        modelName = "Monza 4I";
                        break;
                    case "130":
                        modelName = "Monza 5";
                        break;
                    case "140":
                        modelName = "Monza X-2K";
                        break;
                    case "150":
                        modelName = "Monza X-8K";
                        break;
                    case "160":
                        modelName = "Monza R6 (2 wordB)";
                        break;
                    case "170":
                        modelName = "Monza R6-P";
                        break;
                    case "171":
                        modelName = "Monza R6-A";
                        break;
                    case "173":
                        modelName = "Monza S6-C";
                        break;
                    case "191":
                        modelName = "M730";
                        break;
                    case "190":
                        modelName = "M750";
                        break;
                    default:
                        modelName = ModelString;
                        break;
                }
                break;
            case "02":
                manufactureName = "Texas";
                modelName = ModelString;
                break;
            case "03":
                manufactureName = "Alien";
                switch (ModelString) {
                    case "411":
                        modelName = "H2";
                        break;
                    case "412":
                        modelName = "H3";
                        break;
                    case "414":
                        modelName = "H4";
                        break;
                    case "821":
                        modelName = "H9";
                        break;
                    case "813":
                        modelName = "H10";
                        break;
                    default:
                        modelName = ModelString;
                        break;
                }
                break;
            case "04":
                manufactureName = "Intelleflex";
                modelName = ModelString;
                break;
            case "05":
                manufactureName = "Atmel";
                modelName = ModelString;
                break;
            case "06":
                manufactureName = "NXP";
                switch (ModelString) {
                    case "001":
                        modelName = "UCODE G2";
                        break;
                    case "003":
                        modelName = "UCODE G2XM";
                        break;
                    case "004":
                        modelName = "UCODE G2XL";
                        break;
                    case "890":
                        modelName = "UCODE 7(2 wordB)";
                        break;
                    case "811":
                        modelName = "UCODE 7m(2 wordB)";
                        break;
                    case "D12":
                        modelName = "UCODE 7xm-1K(2 wordB)";
                        break;
                    case "F12":
                        modelName = "UCODE 7xm-2K(2 wordB)";
                        break;
                    case "D92":
                        modelName = "UCODE 7xm+(2 wordB)";
                        break;
                    case "894":
                        modelName = "UCODE 8(2 wordB)";
                        break;
                    case "994":
                        modelName = "UCODE 8m(2 wordB)";
                        break;
                    case "995":
                        modelName = "UCODE 9";
                        break;
                    case "915":
                        modelName = "UCODE 9(new)";
                        break;
                    case "A16":
                        modelName = "UCODE 9XE";
                        break;
                    case "806":
                    case "B06":
                    case "906":
                        modelName = "UCODE G2iL";
                        break;
                    case "807":
                    case "B07":
                    case "907":
                        modelName = "UCODE G2iL+";
                        break;
                    case "80A":
                        modelName = "UCODE G2iM";
                        break;
                    case "80B":
                        modelName = "UCODE G2iM+";
                        break;
                    default:
                        modelName = ModelString;
                        break;
                }
                break;
            case "07":
                manufactureName = "ST";
                modelName = ModelString;
                break;
            case "08":
                manufactureName = "EP";
                modelName = ModelString;
                break;
            case "0A":
                manufactureName = "SSB";
                modelName = ModelString;
                break;
            case "0B":
                manufactureName = "EM";
                modelName = ModelString;
                break;
            case "0F":
                manufactureName = "Quanray";
                if (ModelString.equals("336")) modelName = "Qstar-73";
                else if (ModelString.equals("302")) modelName = "Qstar-7U";
                else
                    modelName = ModelString;
                break;
            case "1D":
                manufactureName = "Kiloway";
                switch (ModelString) {
                    case "010":
                        modelName = "KX2005X";
                        break;
                    case "011":
                        modelName = "KX2005X-B";
                        break;
                    case "012":
                        modelName = "KX2005X-S";
                        break;
                    case "013":
                        modelName = "KX2005X-544";
                        break;
                    case "014":
                        modelName = "KX2005X-240";
                        break;
                    case "015":
                        modelName = "KX2005X-BL";
                        break;
                    case "016":
                        modelName = "KX2005X-BT";
                        break;
                    case "017":
                        modelName = "KX2005X-BR";
                        break;
                    case "020":
                        modelName = "KX2005XG";
                        break;
                    case "021":
                        modelName = "KX2005XG-B";
                        break;
                    case "022":
                        modelName = "KX2005XG-S";
                        break;
                    case "023":
                        modelName = "KX2005XG-544";
                        break;
                    case "024":
                        modelName = "KX2005XG-240";
                        break;
                    case "025":
                        modelName = "KX2005XG-BL";
                        break;
                    case "026":
                        modelName = "KX2005XG-BT";
                        break;
                    case "027":
                        modelName = "KX2005XG-BR";
                        break;
                    case "030":
                        modelName = "KE2006";
                        break;
                    case "031":
                        modelName = "KE2006-B";
                        break;
                    case "032":
                        modelName = "KE2006-S";
                        break;
                    case "033":
                        modelName = "KE2006-544";
                        break;
                    case "034":
                        modelName = "KE2006-240";
                        break;
                    case "035":
                        modelName = "KE2006-BL";
                        break;
                    case "036":
                        modelName = "KE2006-BT";
                        break;
                    case "037":
                        modelName = "KE2006-BR";
                        break;
                    case "040":
                        modelName = "KX2006";
                        break;
                    case "041":
                        modelName = "KX2006-B";
                        break;
                    case "042":
                        modelName = "KX2006-S";
                        break;
                    case "043":
                        modelName = "KX2006-544";
                        break;
                    case "044":
                        modelName = "KX2006-240";
                        break;
                    case "045":
                        modelName = "KX2006-BL";
                        break;
                    case "046":
                        modelName = "KX2006-BT";
                        break;
                    case "047":
                        modelName = "KX2006-BR";
                        break;
                    case "050":
                        modelName = "KE2007";
                        break;
                    case "051":
                        modelName = "KE2007-B";
                        break;
                    case "052":
                        modelName = "KE2007-S";
                        break;
                    case "053":
                        modelName = "KE2007-544";
                        break;
                    case "054":
                        modelName = "KE2007-240";
                        break;
                    case "055":
                        modelName = "KE2007-BL";
                        break;
                    case "056":
                        modelName = "KE2007-BT";
                        break;
                    case "057":
                        modelName = "KE2007-BR";
                        break;
                    case "060":
                        modelName = "ONLY";
                        break;
                    case "061":
                        modelName = "ONLY 1";
                        break;
                    case "062":
                        modelName = "ONLY 2";
                        break;
                    default:
                        modelName = ModelString;
                        break;
                }
                break;
            case "1E":
                manufactureName = "Longjing";
                modelName = ModelString;
                break;
            case "22":
                manufactureName = "Daoyuan";
                modelName = ModelString;
                break;
            case "27":
                manufactureName = "Fudan";
                if (ModelString.equals("802")) modelName = "FM13UF0051E";
                else
                    modelName = ModelString;
                break;
            case "31":
                manufactureName = "Honeywell";
                modelName = ModelString;
                break;
            case "32":
                manufactureName = "Huada";
                modelName = ModelString;
                break;
            case "35":
                manufactureName = "Hangzhou Landa";
                modelName = ModelString;
                break;
            case "36":
                manufactureName = "南瑞";
                modelName = ModelString;
                break;
            case "3B":
                manufactureName = "汇成芯通";
                modelName = ModelString;
                break;
            case "3E":
                manufactureName = "智汇芯联";
                if (ModelString.equals("0E0")) modelName = "MW8112/A";
                else if (ModelString.equals("0F3")) modelName = "MW8113";
                else
                    modelName = ModelString;
                break;
            case "40":
                manufactureName = "山西众烁";
                modelName = ModelString;
                break;
            case "41":
                manufactureName = "国网信通";
                modelName = ModelString;
                break;
            case "42":
                manufactureName = "上海宜链";
                modelName = ModelString;
                break;
            case "43":
                manufactureName = "平头哥";
                modelName = ModelString;
                break;
            case "44":
                manufactureName = "金卡科技";
                modelName = ModelString;
                break;
            case "46":
                manufactureName = "星沿科技";
                modelName = ModelString;
                break;
            default:
                //未知的直接显示TID
                if (tid.startsWith("E2"))
                    manufactureName = tid.substring(2, 2 + 6);
                else {
                    if (tid.length() >= 24)
                        manufactureName = tid.substring(0, 24);
                    else
                        manufactureName = tid;
                }
                modelName = "";
                break;
        }

        return manufactureName + " " + modelName;
    }
}
