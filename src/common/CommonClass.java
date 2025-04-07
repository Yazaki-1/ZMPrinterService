package common;

import layout.PrinterService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CommonClass {
    public static final String SOFT_VERSION = "3.0.9 Last-Version";
    private static final String configDataDir = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "logs";
    private static final String startLog = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "startLog.log";
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");// 定义时间格式
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式

    //保存数据到log文件，被synchronized修饰的代码块及方法，在同一时间，只能被单个线程访问。
    public static synchronized void saveLog(String data, LogType logType) {
        Date date = new Date();// 获得当前的时间戳
        String now = dateFormat.format(date);// 格式化时间
        String nowTime = timeFormat.format(date);// 格式化时间
        BufferedWriter bw = null;
        String logFileName = logType.getPath() + now + ".log";
        try {
            //设置写入文本文件的编码为UTF-8
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configDataDir + FileSystems.getDefault().getSeparator() + logFileName, true), StandardCharsets.UTF_8));
        } catch (FileNotFoundException ex) {
            //没有目录
            try {
                //尝试创建
                Files.createDirectory(Paths.get(configDataDir));
                //设置写入文本文件的编码为UTF-8
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFileName, true), StandardCharsets.UTF_8));
            } catch (IOException e) {
                writeStartLog(e.getMessage());
            }
        }
        if (bw != null) {
            writeFile(bw, data, nowTime);
        }
    }

    private static void writeFile(BufferedWriter bw, String data, String nowTime) {
        try {
            bw.write("V" + CommonClass.SOFT_VERSION + "  " + nowTime + "  " + data);
            bw.write(System.lineSeparator());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            writeStartLog(ex.getMessage());
        }
    }

    public static synchronized void showServiceMsg(String data) {
        PrinterService.service_message.append(timeFormat.format(new Date()) + " " + data + "\n");
        PrinterService.service_message.setCaretPosition(PrinterService.service_message.getDocument().getLength());
    }

    public static synchronized void saveAndShow(String data, LogType logType) {
        saveLog(data, logType);
        showServiceMsg(data);
    }

    public static synchronized void writeStartLog(String data) {
        String nowTime = timeFormat.format(new Date());// 格式化时间
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(startLog, true), StandardCharsets.UTF_8));
            bw.write("V" + CommonClass.SOFT_VERSION + "  " + nowTime + "  " + data);
            bw.write(System.lineSeparator());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
