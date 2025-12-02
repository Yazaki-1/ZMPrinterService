package common;

import layout.PrinterService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CommonClass {

    public static PrinterService PARENT_LAYOUT = null;

    public static ResourceBundle i18nMessage = ResourceBundle.getBundle("i18n/messages");
    public static final String SOFT_VERSION = "3.2.2.2 Last-Version";
    private static final String configDataDir =
            (System.getProperty("os.name").toLowerCase().contains("windows") ?
                    System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() :
                    System.getProperty("user.home") + "/zmsoft/ZMPrinterService/") + "logs";
    private static final String startLog =
            (System.getProperty("os.name").toLowerCase().contains("windows") ?
                    System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() :
                    System.getProperty("user.home") + "/zmsoft/ZMPrinterService/") + "startLog.log";
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");// 定义时间格式
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
    public static String localSN = "";
    public static Integer localPort = null;
    public static boolean hideVisible = false;
    public static boolean tray = false;
    public static boolean auto_start = false;
    public static boolean tcp_receive = false;
    public static String language = "zh_CN";
    public static boolean ssl = false;
    public static String certPath = "";
    public static String password = "";

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
        if (!hideVisible) {
            showServiceMsg(data);
        }
    }

    public static synchronized void writeStartLog(String data) {
        String nowTime = timeFormat.format(new Date());// 格式化时间
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(startLog, true), StandardCharsets.UTF_8));
            bw.write("V" + CommonClass.SOFT_VERSION + "  " + nowTime + "  " + data);
            bw.write(System.lineSeparator());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException ex) {
            //表示用户是第一次使用,需要在用户目录新建各种文件夹
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                String soft = System.getProperty("user.home") + "/zmsoft";
                String service = soft + "/ZMPrinterService";
                String logs = service + "/logs";
                String bin = service + "/bin";
                String configure = bin + "/configure";

                //没有目录
                try {
                    //尝试创建
                    Files.createDirectory(Paths.get(soft));
                    Files.createDirectory(Paths.get(service));
                    Files.createDirectory(Paths.get(logs));
                    Files.createDirectory(Paths.get(bin));
                    Files.createDirectory(Paths.get(configure));
                    //设置写入文本文件的编码为UTF-8
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(startLog, true), StandardCharsets.UTF_8));
                    bw.write("V" + CommonClass.SOFT_VERSION + "  " + nowTime + "  " + data);
                    bw.write(System.lineSeparator());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
