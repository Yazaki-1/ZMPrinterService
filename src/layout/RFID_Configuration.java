/*
 * Created by JFormDesigner on Tue Jul 15 15:10:17 CST 2025
 */

package layout;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.ConnectException;
import common.CommonClass;
import data_processing.ErrorCatcher;
import function.CalibrationFunctionImpl;
import utils.ConfigureUtils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author PH
 */
public class RFID_Configuration extends JDialog {
    private String selectPrinter = "";
    private BufferedImage calibrationADImage = null;
    private String selectConfigure = "";
    private BufferedImage configureImage = null;
    private Map<String, BufferedImage> configureImageMap;
    private Map<String, File> configureFileMap;
    private final PrinterOperator printerOperator = new PrinterOperatorImpl();

    public RFID_Configuration(Window owner) {
        super(owner);
        new CalibrationFunctionImpl().getUsbAndNetPrinters();
        initComponents();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (calibrationADImage != null) {
            Graphics gr = calibrationData_picturePanel.getGraphics();
            gr.drawImage(calibrationADImage, 0, 0, null);
            gr.dispose();
        }

        if (configureImage != null) {
            Graphics gr = labelPicture.getGraphics();
            gr.drawImage(configureImage, 0, 0, null);
            gr.dispose();
        }
    }

    private void initComponents() {
        setResizable(false);
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        topPanel = new JPanel();
        JPanel panel8 = new JPanel();
        JLabel label3 = new JLabel();
        printerListBox = new JComboBox<>();
        firmware_label = new JLabel();
        paper_sensor_label = new JLabel();
        refreshButton = new JButton();
        printer_dpi_label = new JLabel();
        JPanel panel9 = new JPanel();
        JLabel label7 = new JLabel();
        JLabel label8 = new JLabel();
        JLabel label9 = new JLabel();
        JLabel label10 = new JLabel();
        JLabel label11 = new JLabel();
        offsetButton = new JButton();
        horizontal_level = new JSpinner();
        vertical_level = new JSpinner();
        centerPanel = new JPanel();
        JPanel panel13 = new JPanel();
        JScrollPane scrollPane1 = new JScrollPane();
        configureList = new JList<>();
        JPanel panel14 = new JPanel();
        startConfig = new JButton();
        labelPicture = new JPanel();
        bottomPanel = new JPanel();
        JLabel label12 = new JLabel();
        calibrationData_picturePanel = new JPanel();
        label_height_label = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== topPanel ========
        {

            //======== panel8 ========
            {
                panel8.setBorder(LineBorder.createBlackLineBorder());

                //---- label3 ----
                label3.setText("打印机: ");

                //---- firmware_label ----
                firmware_label.setText("Firmware: ");

                //---- paper_sensor_label ----
                paper_sensor_label.setText("感应器类型: ");

                //---- refreshButton ----
                refreshButton.setText("刷新");

                //---- printer_dpi_label ----
                printer_dpi_label.setText("打印机分辨率: ");

                GroupLayout panel8Layout = new GroupLayout(panel8);
                panel8.setLayout(panel8Layout);
                panel8Layout.setHorizontalGroup(
                    panel8Layout.createParallelGroup()
                        .addGroup(panel8Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel8Layout.createParallelGroup()
                                .addGroup(panel8Layout.createSequentialGroup()
                                    .addComponent(label3)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(printerListBox, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(refreshButton))
                                .addComponent(firmware_label)
                                .addComponent(paper_sensor_label)
                                .addComponent(printer_dpi_label))
                            .addContainerGap(62, Short.MAX_VALUE))
                );
                panel8Layout.setVerticalGroup(
                    panel8Layout.createParallelGroup()
                        .addGroup(panel8Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(panel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(printerListBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(refreshButton))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(firmware_label)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(paper_sensor_label)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printer_dpi_label)
                            .addContainerGap())
                );
            }

            //======== panel9 ========
            {
                panel9.setBorder(LineBorder.createBlackLineBorder());

                //---- label7 ----
                label7.setText("打印内容位置微调(水平负数左移, 垂直负数上移)");

                //---- label8 ----
                label8.setText("水平偏移");

                //---- label9 ----
                label9.setText("垂直偏移");

                //---- label10 ----
                label10.setText("毫米");

                //---- label11 ----
                label11.setText("毫米");

                //---- offsetButton ----
                offsetButton.setText("设置");

                GroupLayout panel9Layout = new GroupLayout(panel9);
                panel9.setLayout(panel9Layout);
                panel9Layout.setHorizontalGroup(
                    panel9Layout.createParallelGroup()
                        .addGroup(panel9Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel9Layout.createParallelGroup()
                                .addComponent(label7)
                                .addGroup(panel9Layout.createSequentialGroup()
                                    .addGap(46, 46, 46)
                                    .addGroup(panel9Layout.createParallelGroup()
                                        .addGroup(panel9Layout.createSequentialGroup()
                                            .addComponent(label9)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(vertical_level, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(label11)
                                            .addGap(18, 18, 18)
                                            .addComponent(offsetButton)
                                            .addGap(0, 32, Short.MAX_VALUE))
                                        .addGroup(panel9Layout.createSequentialGroup()
                                            .addComponent(label8)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(horizontal_level, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(label10)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)))))
                            .addContainerGap())
                );
                panel9Layout.setVerticalGroup(
                    panel9Layout.createParallelGroup()
                        .addGroup(panel9Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(label7)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label8)
                                .addComponent(label10)
                                .addComponent(horizontal_level, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label9)
                                .addComponent(label11)
                                .addComponent(vertical_level, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(offsetButton))
                            .addGap(9, 9, 9))
                );
            }

            GroupLayout topPanelLayout = new GroupLayout(topPanel);
            topPanel.setLayout(topPanelLayout);
            topPanelLayout.setHorizontalGroup(
                topPanelLayout.createParallelGroup()
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addComponent(panel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            topPanelLayout.setVerticalGroup(
                topPanelLayout.createParallelGroup()
                    .addComponent(panel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
        }

        //======== centerPanel ========
        {

            //======== panel13 ========
            {

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(configureList);
                }

                GroupLayout panel13Layout = new GroupLayout(panel13);
                panel13.setLayout(panel13Layout);
                panel13Layout.setHorizontalGroup(
                    panel13Layout.createParallelGroup()
                        .addGroup(panel13Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                            .addContainerGap())
                );
                panel13Layout.setVerticalGroup(
                    panel13Layout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                );
            }

            //======== panel14 ========
            {

                //---- startConfig ----
                startConfig.setText("开始配置打印机");

                GroupLayout panel14Layout = new GroupLayout(panel14);
                panel14.setLayout(panel14Layout);
                panel14Layout.setHorizontalGroup(
                    panel14Layout.createParallelGroup()
                        .addGroup(panel14Layout.createSequentialGroup()
                            .addGap(112, 112, 112)
                            .addComponent(startConfig)
                            .addContainerGap(113, Short.MAX_VALUE))
                );
                panel14Layout.setVerticalGroup(
                    panel14Layout.createParallelGroup()
                        .addGroup(panel14Layout.createSequentialGroup()
                            .addComponent(startConfig)
                            .addGap(0, 0, Short.MAX_VALUE))
                );
            }

            //======== labelPicture ========
            {
                labelPicture.setBorder(LineBorder.createBlackLineBorder());

                GroupLayout labelPictureLayout = new GroupLayout(labelPicture);
                labelPicture.setLayout(labelPictureLayout);
                labelPictureLayout.setHorizontalGroup(
                    labelPictureLayout.createParallelGroup()
                        .addGap(0, 455, Short.MAX_VALUE)
                );
                labelPictureLayout.setVerticalGroup(
                    labelPictureLayout.createParallelGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                );
            }

            GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
            centerPanel.setLayout(centerPanelLayout);
            centerPanelLayout.setHorizontalGroup(
                centerPanelLayout.createParallelGroup()
                    .addGroup(centerPanelLayout.createSequentialGroup()
                        .addGroup(centerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panel14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelPicture, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            centerPanelLayout.setVerticalGroup(
                centerPanelLayout.createParallelGroup()
                    .addGroup(centerPanelLayout.createSequentialGroup()
                        .addComponent(panel13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel14, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(labelPicture, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
        }

        //======== bottomPanel ========
        {
            bottomPanel.setBorder(LineBorder.createBlackLineBorder());

            //---- label12 ----
            label12.setText("标签定位曲线");

            //======== calibrationData_picturePanel ========
            {
                calibrationData_picturePanel.setBackground(new Color(0xcccccc));

                GroupLayout calibrationData_picturePanelLayout = new GroupLayout(calibrationData_picturePanel);
                calibrationData_picturePanel.setLayout(calibrationData_picturePanelLayout);
                calibrationData_picturePanelLayout.setHorizontalGroup(
                    calibrationData_picturePanelLayout.createParallelGroup()
                        .addGap(0, 804, Short.MAX_VALUE)
                );
                calibrationData_picturePanelLayout.setVerticalGroup(
                    calibrationData_picturePanelLayout.createParallelGroup()
                        .addGap(0, 121, Short.MAX_VALUE)
                );
            }

            //---- label_height_label ----
            label_height_label.setText("标签高度(间隙): ");

            GroupLayout bottomPanelLayout = new GroupLayout(bottomPanel);
            bottomPanel.setLayout(bottomPanelLayout);
            bottomPanelLayout.setHorizontalGroup(
                bottomPanelLayout.createParallelGroup()
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(label12)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 488, Short.MAX_VALUE)
                        .addComponent(label_height_label)
                        .addGap(157, 157, 157))
                    .addComponent(calibrationData_picturePanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            bottomPanelLayout.setVerticalGroup(
                bottomPanelLayout.createParallelGroup()
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label12)
                            .addComponent(label_height_label))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calibrationData_picturePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(topPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bottomPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(topPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(centerPanel, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(bottomPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GE N-END:initComponents  @formatter:1on

        configureList.setFixedCellHeight(42);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                setTitle("RFID Configuration [Ver" + CommonClass.SOFT_VERSION + "]");
                String configDir;
                String os = System.getProperty("os.name");
                if (os.toLowerCase().contains("windows")) {
                    String dir = System.getProperty("user.dir");
                    configDir = dir + "/bin/configure";
                }else if (os.toLowerCase().contains("linux")) {
                    configDir = System.getProperty("user.home") + "/zmsoft/ZMPrinterService/bin/configure/";
                }else
                    throw new RuntimeException("暂不支持" + os);

                configureFileMap = ConfigureUtils.configureCommandsMap(configDir);
                int boxWidth = labelPicture.getWidth();
                int boxHeight = labelPicture.getHeight();
                try {
                    configureImageMap = ConfigureUtils.configureListMap(configDir, boxWidth, boxHeight);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }

                List<String> names = new ArrayList<>(configureFileMap.keySet());
                String[] strings = new String[names.size()];
                names.toArray(strings);
                configureList.setListData(strings);

                if (names.isEmpty()) {
                    startConfig.setEnabled(false);
                }
            }
        });

        initPrintListBox();

        startConfig.addActionListener(e -> {
            String text = startConfig.getText();
            setComponentEnable(false);

            File file = configureFileMap.get(selectConfigure);
            try {
                List<String> contents = Files.readAllLines(file.toPath());
                StringBuilder instruct = new StringBuilder();
                contents.forEach(c -> instruct.append(c).append("\r\n"));
                System.out.println(instruct);
                try{
                    printerOperator.getPrinterStatus(selectPrinter, 1);

                    if (selectPrinter.contains(".")) printerOperator.sendToPrinter(selectPrinter, instruct.toString().getBytes(StandardCharsets.UTF_8));
                    else printerOperator.sendToPrinter(selectPrinter, instruct.toString().getBytes(), instruct.length(), 1);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (int i = 19; i >= 0; i--) {
                                try{
                                    Thread.sleep(1000);
                                }catch(InterruptedException e){
                                    throw new RuntimeException(e);
                                }
                                startConfig.setText("配置中..." + "(" + i + "s)");
                            }
                            startConfig.setText(text);
                            setComponentEnable(true);
                            showResult(selectPrinter);
                            showInformationDialog("配置完成, 请开始打印!");
                        }
                    }, 20);
                }catch (ConnectException ex) {
                    showErrorMessage(ErrorCatcher.CatchConnectError(ex.getMessage()));
                }
            } catch (IOException ex) {
                showErrorMessage("配置文件异常!");
            }
        });

        horizontal_level.setModel(new SpinnerNumberModel(0, -80, 80, 0.5));
        vertical_level.setModel(new SpinnerNumberModel(0, -8, 80, 0.5));

        offsetButton.addActionListener(e -> {
            String dpiS = printer_dpi_label.getText().replace("打印机分辨率: ", "");
            if (!dpiS.isEmpty()) {
                String commands = "";
                dpiS = dpiS.replace(" DPI", "");
                int dpi = Integer.parseInt(dpiS);
                String offsetHorizontal = horizontal_level.getModel().getValue().toString();
                int offsetH = Math.round(Float.parseFloat(offsetHorizontal) * dpi / 25.4f);

                if (dpi == 203 && offsetH < 0) offsetH += 864;
                else if (dpi == 300 && offsetH < 0) offsetH += 1248;
                else if (dpi == 600 && offsetH < 0) offsetH += 2496;

                commands += "#UM>MX+" + String.format("%04d", offsetH) + "\r\n";

                String offsetVertical = vertical_level.getModel().getValue().toString();
                int offsetV = Math.round(Float.parseFloat(offsetVertical) * dpi / 25.4f);

                if (offsetV >= 0) commands += "#UM>MY+" + String.format("%04d", Math.abs(offsetV)) + "\r\n";
                else commands += "#UM>MY-" + String.format("%04d", Math.abs(offsetV)) + "\r\n";

                System.out.println(commands);

                try{
                    printerOperator.getPrinterStatus(selectPrinter, 1);

                    if (selectPrinter.contains(".")) printerOperator.sendToPrinter(selectPrinter, commands.getBytes(StandardCharsets.UTF_8));
                    else printerOperator.sendToPrinter(selectPrinter, commands.getBytes(), commands.length(), 1);
                    showInformationDialog("设置完成,请【重启】打印机生效!!!");
                }catch (ConnectException ex) {
                    showErrorMessage(ErrorCatcher.CatchConnectError(ex.getMessage()));
                }
            }else {
                showErrorMessage("未选择打印机");
            }

        });
        refreshButton.addActionListener(e -> System.out.println("刷新打印机列表"));

        configureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectConfigure = configureList.getSelectedValue();
                configureImage = configureImageMap.get(selectConfigure);
                repaint();
            }
        });
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:1off
    private JPanel topPanel;
    private JComboBox<String> printerListBox;
    private JLabel firmware_label;
    private JLabel paper_sensor_label;
    private JLabel printer_dpi_label;
    private JPanel centerPanel;
    private JList<String> configureList;
    private JPanel labelPicture;
    private JPanel bottomPanel;
    private JPanel calibrationData_picturePanel;
    private JLabel label_height_label;
    private JButton startConfig;
    private JButton refreshButton;
    private JButton offsetButton;
    private JSpinner horizontal_level;
    private JSpinner vertical_level;
    // JFormDesigner - End of variables declaration  //GE N-END:variables  @formatter:1on

    private void initPrintListBox() {
        if (!RFID_Calibration.map.isEmpty()) {
            RFID_Calibration.map.keySet().forEach(k -> {
                PrinterVO printervo = RFID_Calibration.map.get(k);
                if (printervo != null) {
                    String content = printervo.getName() + "," + k;
                    printerListBox.addItem(content);
                }
            });
        } else {
            printerListBox.removeAllItems();
        }

        if (printerListBox.getItemCount() > 0) {
            printerListBox.setSelectedIndex(0);
            String select = printerListBox.getItemAt(0);
            String addr = select.split(",")[1];
            getPrinterInfo(addr);
            selectPrinter = addr;
        }
    }

    private void setComponentEnable(boolean enable) {
        printerListBox.setEnabled(enable);
        startConfig.setEnabled(enable);
        refreshButton.setEnabled(enable);
        offsetButton.setEnabled(enable);
        configureList.setEnabled(enable);
        horizontal_level.setEnabled(enable);
        vertical_level.setEnabled(enable);
    }

    private void getPrinterInfo(String key) {
        int type = key.contains(".") ? 2 : 1;
        if (RFID_Calibration.map.containsKey(key)) {
            PrinterVO printerVO = RFID_Calibration.map.get(key);
            if (printerVO != null) {
                PrinterInfoVO printerInfoVO = printerVO.getVo() == null ? new PrinterInfoVO() : printerVO.getVo();
                if (printerVO.getPrinterType() == PrinterType.UHF || printerVO.getPrinterType() == PrinterType.GJB) {
                    // 纸张感应器
                    byte[] modelByte = ("RQ" + type + ",2\r\n").getBytes(StandardCharsets.UTF_8);
                    try {
                        String modelRead = LayoutUtils.getPrinterMessage(key, modelByte);
                        String[] r = modelRead.split(",");
                        printerInfoVO.setPrintModel(r[0].equals("1") ? "热转印" : "热敏");
                        printerInfoVO.setPaperSensor(Integer.valueOf(r[1]));

                        printerVO.setVo(printerInfoVO);
                        showPrinterMessage(printerVO);
                    } catch (ConnectException e) {
                        showErrorMessage(ErrorCatcher.CatchConnectError(e.getMessage()));
                    }
                } else {
                    topPanel.setEnabled(false);
                    centerPanel.setEnabled(false);
                    bottomPanel.setEnabled(false);
                    showErrorMessage("当前打印机无法使用一键配置工具!");
                }
            }
        }
    }

    private void showResult(String addr) {
        if (!addr.isEmpty()) {
            try {
                PrinterVO printerVO = RFID_Calibration.map.get(selectPrinter);
                if (printerVO != null) {
                    PrinterInfoVO printerInfoVO = printerVO.getVo();
                    PrinterOperator printerOperator = new PrinterOperatorImpl();
                    printerOperator.getPrinterStatus(selectPrinter, 1);
                    String commandString = selectPrinter.contains(".") ? "AL\r\n" : "AD\r\n"; //获取校准曲线的数据
                    String readData = LayoutUtils.getPrinterMessage(addr, commandString.getBytes(StandardCharsets.UTF_8));
                    System.out.println(readData);
                    int adThreshold = -1;//AD阈值

                    float labelGapHeightPixel = 0;

                    if (readData.contains("$")) {
                        String[] dataArr = readData.split("\\$");
                        if (dataArr.length > 1) {
                            if (dataArr[1].contains("&")) { //0612&12@0216
                                String[] atData = dataArr[1].split("@"); //分割字符串
                                String[] andData = atData[0].split("&"); //分割字符串

                                adThreshold = Integer.parseInt(atData[1]);

                                int label_height_dots = Integer.parseInt(andData[0]);
                                labelGapHeightPixel = 25.4f * label_height_dots / printerVO.getDpi();//标签和间隙的高度
                            }

                            if (dataArr[0].contains(":")) {//包含校准数据
                                String[] dataArr2 = dataArr[0].split(":"); //分割字符串
                                String[] dataArr3 = dataArr2[1].split(","); //分割字符串

                                // 自动分析得到标签高度和间隙高度
                                Map<String, Float> map = LayoutUtils.GetCalibraLabelHeight(dataArr3, printerInfoVO.getPaperSensor(), adThreshold, printerVO.getDpi());
                                float labelHeightPixel = map == null ? 0 : map.get("labelHeight");
                                float labelGapPixel = map == null ? 0 : map.get("labelGap");

                                if (labelHeightPixel > 1f && labelGapPixel > 1f)
                                    label_height_label.setText("标签高度(间隙):" + labelHeightPixel + "(" + labelGapPixel + ")mm");
                                else
                                    label_height_label.setText("标签高度（含间隙）：" + String.format("%.2f", labelGapHeightPixel) + "mm");

                                //将信号范围画出
                                int labelWidthDots = dataArr3.length - 1;//标签加间隙的高度，单位是mm
                                int labelHeight;//探测器返回值最高1024
                                int minValue = 1024;
                                int maxValue = 0;
                                for (int i = 0; i < dataArr3.length - 1; i++) {
                                    if (minValue > Integer.parseInt(dataArr3[i]))
                                        minValue = Integer.parseInt(dataArr3[i]);
                                    if (maxValue < Integer.parseInt(dataArr3[i]))
                                        maxValue = Integer.parseInt(dataArr3[i]);
                                }
                                labelHeight = maxValue - minValue + 1;
                                BufferedImage calibrationImage = new BufferedImage(labelWidthDots, labelHeight, 1);//用于显示校准的图片

                                Graphics2D gr = calibrationImage.createGraphics();
                                gr.setBackground(Color.lightGray);//new Color(255, 255, 255));
                                gr.clearRect(0, 0, calibrationImage.getWidth(), calibrationImage.getHeight());
                                gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                                if (labelHeight > 1) {
                                    // 画一个带刻度的标尺
                                    gr.setColor(Color.gray);
                                    gr.fillRect(0, 0, 20, calibrationImage.getHeight());

                                    //绘制Y轴
                                    gr.setColor(Color.black);

                                    int fontsize = LayoutUtils.getFontsize(calibrationImage);
                                    Font txtFont = new Font("Arial", Font.BOLD, (int) (fontsize / 72.0F * printerVO.getDpi() + 0.5F));
                                    gr.setFont(txtFont);

                                    float yStep = calibrationImage.getHeight() / 24f;
                                    for (int i = 1; i <= 24; i++) {
                                        int yLocation = (int) (calibrationImage.getHeight() - (i * yStep) + 0.5f);
                                        Point start = new Point(0, yLocation);
                                        Point end = new Point(15, yLocation);
                                        gr.drawLine(start.x, start.y, end.x, end.y);
                                        gr.drawString(String.valueOf(i), 20, yLocation - fontsize);
                                    }

                                    // 画出波形图
                                    for (int i = 0; i < labelWidthDots - 1; i++) {
                                        int RSSI_value = labelHeight - (Integer.parseInt(dataArr3[i]) - minValue);
                                        int RSSI_value2 = labelHeight - (Integer.parseInt(dataArr3[i + 1]) - minValue);
                                        gr.drawLine(i, RSSI_value, i + 1, RSSI_value2);
                                    }
                                    if (adThreshold != -1) {
                                        gr.setColor(Color.red);
                                        gr.drawLine(0, labelHeight - (adThreshold - minValue), labelWidthDots, labelHeight - (adThreshold - minValue));
                                    }
                                    gr.dispose();

                                    //region 缩放图片
                                    int panelWidth = calibrationData_picturePanel.getWidth();
                                    int panelHeight = calibrationData_picturePanel.getHeight();
                                    int imageWidth = calibrationImage.getWidth();
                                    int imageHeight = calibrationImage.getHeight();
                                    if (labelWidthDots > panelWidth) {
                                        imageWidth = panelWidth;
                                        imageHeight = (int) (1f * labelHeight * panelWidth / labelWidthDots + 0.5f);
                                    }
                                    if (imageHeight > panelHeight) {
                                        imageHeight = panelHeight;
                                        imageWidth = (int) (1f * imageWidth * panelHeight / imageHeight + 0.5f);
                                    }

                                    calibrationADImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D graphics2D = calibrationADImage.createGraphics();
                                    Image img = calibrationImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                    graphics2D.drawImage(img, 0, 0, null);
                                    graphics2D.dispose();
                                } else {
                                    calibrationADImage = new BufferedImage(calibrationData_picturePanel.getWidth(), calibrationData_picturePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                                    Graphics2D graphics2D = calibrationADImage.createGraphics();
                                    graphics2D.setBackground(new Color(204, 204, 204));
                                    graphics2D.clearRect(0, 0, calibrationADImage.getWidth(), calibrationADImage.getHeight());
                                    graphics2D.dispose();
                                }
                            }
                            repaint();
                        } else {
                            calibrationADImage = null;
                        }
                    }
                }
            } catch (ConnectException e) {
                throw new RuntimeException(ErrorCatcher.CatchConnectError(e.getMessage()));
            }
        }
    }

    private void showPrinterMessage(PrinterVO v) {
        PrinterInfoVO printerInfoVO = v.getVo();
        firmware_label.setText("Firmware: " + v.getFirmware());
        printer_dpi_label.setText("打印机分辨率: " + (int) v.getDpi() + " DPI");
        paper_sensor_label.setText("感应器类型: " + (printerInfoVO.getPaperSensor() == 0 ? "穿透式" : "反射式"));
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInformationDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
