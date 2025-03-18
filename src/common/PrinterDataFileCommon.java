package common;

import com.alibaba.fastjson2.JSONObject;
import layout.PrinterVO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PrinterDataFileCommon {

    private static final String PRINTER_DATA_DIR = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "data";

    public static boolean addPrinter(String addr, PrinterVO printerVO) {
        String data = PRINTER_DATA_DIR + FileSystems.getDefault().getSeparator() + addr;
        String jsonString = JSONObject.toJSONString(printerVO);

        try (FileWriter writer = new FileWriter(data + ".json")) {
            writer.write(jsonString);
            writer.flush();
            return true;
        } catch (FileNotFoundException e1) {
            try {
                //尝试创建
                Files.createDirectory(Paths.get(PRINTER_DATA_DIR));
                try (FileWriter writer = new FileWriter(data + ".json")) {
                    writer.write(jsonString);
                    writer.flush();
                    return true;
                } catch (IOException e2) {
                    CommonClass.saveLog(e2.getMessage(), LogType.ErrorData);
                    return false;
                }
            } catch (Exception e3) {
                CommonClass.saveLog(e3.getMessage(), LogType.ErrorData);
                return false;
            }
        } catch (IOException e) {
            CommonClass.saveLog(e.getMessage(), LogType.ErrorData);
            return false;
        }
    }

    public static Map<String, PrinterVO> getPrinters() {
        File fileFolder = new File(PRINTER_DATA_DIR);
        File[] files = fileFolder.listFiles();
        Map<String, PrinterVO> map = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                Path path = Paths.get(file.getAbsolutePath());
                try {
                    byte[] data = Files.readAllBytes(path);
                    JSONObject jsonObject = JSONObject.parseObject(new String(data, StandardCharsets.UTF_8));
                    PrinterVO printerVO = jsonObject.toJavaObject(PrinterVO.class);
                    String key = file.getName().replace(".json", "");
                    map.put(key, printerVO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return map;
    }

    public static void deletePrinter(String addr) {
        try {
            File file = new File(PRINTER_DATA_DIR + FileSystems.getDefault().getSeparator() + addr + ".json");
            if (file.delete()) {
                CommonClass.saveLog("删除网络打印机: " + addr, LogType.CalibrationData);
            } else {
                CommonClass.saveLog("删除失败: " + addr, LogType.CalibrationData);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void renamePrinter(String oldAddr, String newAddr) {
        // 原始文件路径
        File originalFile = new File(PRINTER_DATA_DIR + FileSystems.getDefault().getSeparator() + oldAddr + ".json");
        // 目标文件路径
        File newFile = new File(PRINTER_DATA_DIR + FileSystems.getDefault().getSeparator() + newAddr + ".json");

        // 重命名或移动文件
        if (originalFile.renameTo(newFile)) {
            CommonClass.saveLog("更新网络打印机: " + newAddr, LogType.CalibrationData);
        } else {
            CommonClass.saveLog("更新失败: " + newAddr, LogType.CalibrationData);
        }
    }
}
