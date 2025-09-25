package layout;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import com.ZMPrinter.conn.ConnectException;
import common.CommonClass;
import common.PrinterDataFileCommon;
import data_processing.ErrorCatcher;
import function.CalibrationException;
import function.CalibrationFunction;
import function.CalibrationFunctionImpl;
import utils.DataUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


/*
 * Created by LKF on Sat Feb 13 17:56:20 CST 2021
 */

/**
 * @author LuoKunFan ph
 */
public class RFID_Calibration extends JDialog {

    private final CalibrationFunction calibrationFunction = new CalibrationFunctionImpl();
    private final PrinterOperator printerOperator = new PrinterOperatorImpl();
    public static Map<String, PrinterVO> map = new HashMap<>();
    private String selectPrinter = "";

    private BufferedImage CalibrationData_picture = null;
    private BufferedImage RSSI_data_picture = null;

    public RFID_Calibration(Window owner) {
        super(owner);
        calibrationFunction.getUsbAndNetPrinters();
        initComponents();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (CalibrationData_picture != null) {
            Graphics gr = CalibrationData_picturePanel.getGraphics();
            gr.drawImage(CalibrationData_picture, 0, 0, null);
            gr.dispose();
        }
        if (RSSI_data_picture != null) {
            Graphics gr = RSSI_data_picturePanel.getGraphics();
            gr.drawImage(RSSI_data_picture, 0, RSSI_data_picturePanel.getHeight() - RSSI_data_picture.getHeight(), null);
            gr.dispose();
        }
    }

    private void protocol_comboBoxItemStateChanged(ItemEvent e) {
        //0:ISO,1:GJB,2:GB
        if (protocol_comboBox.getSelectedIndex() == 0) {
            checkBox_gjb_jm.setVisible(false);
        } else {
            checkBox_gjb_jm.setVisible(protocol_comboBox.getSelectedIndex() == 1);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents1
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        JTabbedPane tabbedPane1 = new JTabbedPane();
        JPanel RFID_printer = new JPanel();
        JPanel printerInfo_panel = new JPanel();
        printer_dpi_label = new JLabel();
        paper_sensor_label = new JLabel();
        print_mode_label = new JLabel();
        firmware_label = new JLabel();
        JLabel label99 = new JLabel();
        printerListBox = new JComboBox<>();
        uhf_param = new JPanel();
        protocol_label = new JLabel();
        protocol_comboBox = new JComboBox<>();
        checkBox_gjb_jm = new JCheckBox();
        read_power_comboBox = new JComboBox<>();
        JLabel read_power_label2 = new JLabel();
        JLabel write_power_label2 = new JLabel();
        write_power_comboBox = new JComboBox<>();
        JLabel write_power_label1 = new JLabel();
        JLabel read_power_label1 = new JLabel();
        JLabel dr_value_label = new JLabel();
        dr_value_comboBox = new JComboBox<>();
        frequency_comboBox = new JComboBox<>();
        frequency_label = new JLabel();
        hf_param = new JPanel();
        JLabel HF_power_label1 = new JLabel();
        HF_power_comboBox = new JComboBox<>();
        JLabel HF_type_label = new JLabel();
        HF_type_comboBox = new JComboBox<>();
        JLabel HF_power_label2 = new JLabel();
        JPanel panel1 = new JPanel();
        refreshPrinterInfo_button = new JButton();
        setPrinter_button = new JButton();
        JButton addNetP = new JButton();
        delNetPrinter = new JButton();
        JPanel net_param = new JPanel();
        JLabel label22 = new JLabel();
        JLabel label23 = new JLabel();
        ip_textField = new JTextField();
        JLabel label24 = new JLabel();
        ip_subMask_textField = new JTextField();
        JLabel label25 = new JLabel();
        JLabel label26 = new JLabel();
        ip_gate_textField = new JTextField();
        JTextField ip_port_textField = new JTextField();
        JLabel label27 = new JLabel();
        net_set_button = new JButton();
        ip_mac_textField = new JTextField();
        JLabel label28 = new JLabel();
        JPanel advanceSetting = new JPanel();
        JLabel label4 = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        command_textArea = new JTextArea();
        reset_button = new JButton();
        command_button = new JButton();
        just_rfid = new JButton();
        getResult_button = new JButton();
        CalibrationData_picturePanel = new JPanel();
        JLabel calibrationData_label = new JLabel();
        RSSI_data_picturePanel = new JPanel();
        RSSI_data_label2 = new JLabel();
        rfid_panel = new JPanel();
        uhf_inlay_label = new JLabel();
        set_an_textField = new JTextField();
        set_an_label2 = new JLabel();
        set_an_button = new JButton();
        set_an_label1 = new JLabel();
        set_wp_textField = new JTextField();
        set_wp_label2 = new JLabel();
        set_wp_button = new JButton();
        set_wp_label1 = new JLabel();
        readWriteLine_label = new JLabel();
        rssiHeight_label = new JLabel();
        rssiNumber_label = new JLabel();
        JPanel rfid_signal_panel = new JPanel();
        sensor_level_label = new JLabel();
        JLabel sensor_level_label2 = new JLabel();
        sensor_level_comboBox = new JComboBox<>();
        sensor_level_button = new JButton();
        RSSI_data_label = new JLabel();
        RSSI_data_scrollPane = new JScrollPane();
        RSSI_data_textArea = new JTextArea();
        sysMsg_label = new JLabel();
        JLabel sensorLevel_label3 = new JLabel();
        maxLength_textField = new JTextField();
        JLabel label20 = new JLabel();
        maxLength_button = new JButton();
        label_height_label = new JLabel();
        sensor_value_label = new JLabel();
        just_label = new JButton();
        saveResultButton = new JButton();

        //======== this ========
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                setTitle("RFID Calibration [Ver" + CommonClass.SOFT_VERSION + "]");

                String osName = System.getProperty("os.name").toLowerCase();
                String osArch = System.getProperty("os.arch");
                String runTime = System.getProperty("java.runtime.version");
                if (runTime.length() > 20)
                    runTime = runTime.substring(0, 20);
                String sys_msg = osName + ", " + osArch + " (" + runTime + ")";
                sysMsg_label.setText(sys_msg);
            }
        });
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== RFID_printer ========
            {

                //======== printerInfo_panel ========
                {

                    //---- printer_dpi_label ----
                    printer_dpi_label.setText(CommonClass.i18nMessage.getString("printer_dpi") + "null");

                    //---- paper_sensor_label ----
                    paper_sensor_label.setText(CommonClass.i18nMessage.getString("paper_sensor") + "null");

                    //---- print_mode_label ----
                    print_mode_label.setText(CommonClass.i18nMessage.getString("print_mode") + "null");

                    //---- firmware_label ----
                    firmware_label.setText(CommonClass.i18nMessage.getString("firmware") + "V0.00XX");

                    //---- label99 ----
                    label99.setText(CommonClass.i18nMessage.getString("current_printer"));

                    GroupLayout printerInfo_panelLayout = new GroupLayout(printerInfo_panel);
                    printerInfo_panel.setLayout(printerInfo_panelLayout);
                    printerInfo_panelLayout.setHorizontalGroup(
                            printerInfo_panelLayout.createParallelGroup()
                                    .addGroup(printerInfo_panelLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(printerInfo_panelLayout.createParallelGroup()
                                                    .addGroup(printerInfo_panelLayout.createSequentialGroup()
                                                            .addComponent(label99)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(printerListBox, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                                                    .addGroup(printerInfo_panelLayout.createSequentialGroup()
                                                            .addGroup(printerInfo_panelLayout.createParallelGroup()
                                                                    .addComponent(firmware_label)
                                                                    .addComponent(print_mode_label)
                                                                    .addComponent(paper_sensor_label)
                                                                    .addComponent(printer_dpi_label))
                                                            .addGap(0, 149, Short.MAX_VALUE)))
                                            .addContainerGap())
                    );
                    printerInfo_panelLayout.setVerticalGroup(
                            printerInfo_panelLayout.createParallelGroup()
                                    .addGroup(printerInfo_panelLayout.createSequentialGroup()
                                            .addGroup(printerInfo_panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(printerListBox)
                                                    .addComponent(label99))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(firmware_label)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(print_mode_label)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(paper_sensor_label)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(printer_dpi_label)
                                            .addGap(23, 23, 23))
                    );
                }

                //======== uhf_param ========
                {

                    //---- protocol_label ----
                    protocol_label.setText("协议:");

                    //---- protocol_comboBox ----
                    protocol_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "ISO",
                            "GJB",
                            "GB"
                    }));
                    protocol_comboBox.setSelectedIndex(0);
                    protocol_comboBox.addItemListener(this::protocol_comboBoxItemStateChanged);

                    //---- checkBox_gjb_jm ----
                    checkBox_gjb_jm.setText("启用军密");

                    //---- read_power_comboBox ----
                    read_power_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "0",
                            "1",
                            "2",
                            "3",
                            "4",
                            "5",
                            "6",
                            "7",
                            "8",
                            "9",
                            "10",
                            "11",
                            "12",
                            "13",
                            "14",
                            "15",
                            "16",
                            "17",
                            "18",
                            "19",
                            "20",
                            "21",
                            "22",
                            "23",
                            "24",
                            "25",
                            "26",
                            "27"
                    }));

                    //---- read_power_label2 ----
                    read_power_label2.setText("dBm");

                    //---- write_power_label2 ----
                    write_power_label2.setText("dBm");

                    //---- write_power_comboBox ----
                    write_power_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "0",
                            "1",
                            "2",
                            "3",
                            "4",
                            "5",
                            "6",
                            "7",
                            "8",
                            "9",
                            "10",
                            "11",
                            "12",
                            "13",
                            "14",
                            "15",
                            "16",
                            "17",
                            "18",
                            "19",
                            "20",
                            "21",
                            "22",
                            "23",
                            "24",
                            "25",
                            "26",
                            "27"
                    }));

                    //---- write_power_label1 ----
                    write_power_label1.setText(CommonClass.i18nMessage.getString("write_power"));

                    //---- read_power_label1 ----
                    read_power_label1.setText(CommonClass.i18nMessage.getString("read_power"));

                    //---- dr_value_label ----
                    dr_value_label.setText(CommonClass.i18nMessage.getString("dr_value"));

                    //---- dr_value_comboBox ----
                    dr_value_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "0",
                            "1",
                            "2"
                    }));
                    dr_value_comboBox.setSelectedIndex(2);

                    //---- frequency_comboBox ----
                    frequency_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "Auto8",
                            "Auto9"
                    }));
                    frequency_comboBox.setSelectedIndex(1);

                    //---- frequency_label ----
                    frequency_label.setText(CommonClass.i18nMessage.getString("frequency"));

                    GroupLayout uhf_paramLayout = new GroupLayout(uhf_param);
                    uhf_param.setLayout(uhf_paramLayout);
                    uhf_paramLayout.setHorizontalGroup(
                            uhf_paramLayout.createParallelGroup()
                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(uhf_paramLayout.createParallelGroup()
                                                    .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addGroup(uhf_paramLayout.createSequentialGroup()
                                                                    .addComponent(dr_value_label)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(dr_value_comboBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(uhf_paramLayout.createSequentialGroup()
                                                                    .addComponent(frequency_label)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(frequency_comboBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                                            .addComponent(write_power_label1)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(write_power_comboBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(write_power_label2))
                                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(read_power_label1)
                                                                    .addComponent(protocol_label))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(uhf_paramLayout.createParallelGroup()
                                                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                                                            .addComponent(protocol_comboBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                            .addComponent(checkBox_gjb_jm))
                                                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                                                            .addComponent(read_power_comboBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(read_power_label2)))))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    uhf_paramLayout.setVerticalGroup(
                            uhf_paramLayout.createParallelGroup()
                                    .addGroup(uhf_paramLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(protocol_comboBox, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(checkBox_gjb_jm)
                                                    .addComponent(protocol_label))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(read_power_comboBox, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(read_power_label2, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(read_power_label1))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(write_power_comboBox, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(write_power_label2, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(write_power_label1))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(dr_value_comboBox, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(dr_value_label))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(uhf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(frequency_comboBox, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(frequency_label))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                //======== hf_param ========
                {

                    //---- HF_power_label1 ----
                    HF_power_label1.setText(CommonClass.i18nMessage.getString("hf_power"));

                    //---- HF_power_comboBox ----
                    HF_power_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            "12",
                            "24",
                            "36",
                            "48"
                    }));

                    //---- HF_type_label ----
                    HF_type_label.setText(CommonClass.i18nMessage.getString("hf_type"));

                    //---- HF_type_comboBox ----
                    HF_type_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                            CommonClass.i18nMessage.getString("default"),
                            "15693",
                            "14443A",
                            "NFC/UltraLight"
                    }));

                    //---- HF_power_label2 ----
                    HF_power_label2.setText("dBm");

                    GroupLayout hf_paramLayout = new GroupLayout(hf_param);
                    hf_param.setLayout(hf_paramLayout);
                    hf_paramLayout.setHorizontalGroup(
                            hf_paramLayout.createParallelGroup()
                                    .addGroup(hf_paramLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(hf_paramLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(hf_paramLayout.createSequentialGroup()
                                                            .addComponent(HF_power_label1)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(HF_power_comboBox, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(HF_power_label2))
                                                    .addGroup(hf_paramLayout.createSequentialGroup()
                                                            .addComponent(HF_type_label)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(HF_type_comboBox, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    hf_paramLayout.setVerticalGroup(
                            hf_paramLayout.createParallelGroup()
                                    .addGroup(hf_paramLayout.createSequentialGroup()
                                            .addGap(14, 14, 14)
                                            .addGroup(hf_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(HF_power_label1)
                                                    .addComponent(HF_power_comboBox, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(HF_power_label2))
                                            .addGap(7, 7, 7)
                                            .addGroup(hf_paramLayout.createParallelGroup()
                                                    .addComponent(HF_type_label)
                                                    .addComponent(HF_type_comboBox, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                //======== panel1 ========
                {

                    //---- refreshPrinterInfo_button ----
                    refreshPrinterInfo_button.setText(CommonClass.i18nMessage.getString("refresh"));

                    //---- setPrinter_button ----
                    setPrinter_button.setText(CommonClass.i18nMessage.getString("setting"));

                    //---- addNetP ----
                    addNetP.setText(CommonClass.i18nMessage.getString("add_net"));
                    addNetP.addActionListener(e -> {
                        NetPrinter printer = new NetPrinter(this, this);
                        printer.setVisible(true);
                    });

                    //---- delNetPrinter ----
                    delNetPrinter.setText(CommonClass.i18nMessage.getString("del_net"));

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                            panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panel1Layout.createParallelGroup()
                                                    .addComponent(refreshPrinterInfo_button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(addNetP, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                                    .addComponent(setPrinter_button, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                                    .addComponent(delNetPrinter, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                                            .addContainerGap())
                    );
                    panel1Layout.setVerticalGroup(
                            panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(refreshPrinterInfo_button, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(addNetP, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(delNetPrinter, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(setPrinter_button, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap())
                    );
                }

                GroupLayout RFID_printerLayout = new GroupLayout(RFID_printer);
                RFID_printer.setLayout(RFID_printerLayout);
                RFID_printerLayout.setHorizontalGroup(
                        RFID_printerLayout.createParallelGroup()
                                .addGroup(RFID_printerLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(printerInfo_panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(uhf_param, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hf_param, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                );
                RFID_printerLayout.setVerticalGroup(
                        RFID_printerLayout.createParallelGroup()
                                .addGroup(RFID_printerLayout.createSequentialGroup()
                                        .addGroup(RFID_printerLayout.createParallelGroup()
                                                .addComponent(hf_param, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(uhf_param, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(RFID_printerLayout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addGroup(RFID_printerLayout.createParallelGroup()
                                                                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(printerInfo_panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addContainerGap())
                );
            }
            tabbedPane1.addTab(CommonClass.i18nMessage.getString("rfid_printer"), RFID_printer);

            //======== net_param ========
            {

                //---- label22 ----
                label22.setText(CommonClass.i18nMessage.getString("net_printer_setting"));

                //---- label23 ----
                label23.setText(CommonClass.i18nMessage.getString("ip_addr"));

                //---- label24 ----
                label24.setText(CommonClass.i18nMessage.getString("subnet_mask"));

                //---- label25 ----
                label25.setText(CommonClass.i18nMessage.getString("gateway"));

                //---- label26 ----
                label26.setText(CommonClass.i18nMessage.getString("printer_port"));

                //---- ip_port_textField ----
                ip_port_textField.setText("9100");
                ip_port_textField.setEditable(false);

                //---- label27 ----
                label27.setText(CommonClass.i18nMessage.getString("net_setting_warning"));
                label27.setForeground(new Color(0x3399ff));

                //---- net_set_button ----
                net_set_button.setText(CommonClass.i18nMessage.getString("setting"));

                //---- ip_mac_textField ----
                ip_mac_textField.setEditable(false);

                //---- label28 ----
                label28.setText(CommonClass.i18nMessage.getString("mac_addr"));

                GroupLayout net_paramLayout = new GroupLayout(net_param);
                net_param.setLayout(net_paramLayout);
                net_paramLayout.setHorizontalGroup(
                        net_paramLayout.createParallelGroup()
                                .addGroup(net_paramLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(net_paramLayout.createParallelGroup()
                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addGroup(net_paramLayout.createParallelGroup()
                                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                                        .addComponent(label28, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(ip_mac_textField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(label27, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(net_set_button, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(207, 207, 207))
                                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                                        .addGroup(net_paramLayout.createParallelGroup()
                                                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                                                        .addComponent(label24, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                        .addComponent(ip_subMask_textField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                                                                        .addGap(18, 18, 18)
                                                                                        .addComponent(label26, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                        .addComponent(ip_port_textField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                                                        .addComponent(label23, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                        .addComponent(ip_textField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                                                                        .addGap(18, 18, 18)
                                                                                        .addComponent(label25, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                        .addComponent(ip_gate_textField, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)))
                                                                        .addContainerGap(319, Short.MAX_VALUE))))
                                                .addGroup(net_paramLayout.createSequentialGroup()
                                                        .addComponent(label22, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap(693, Short.MAX_VALUE))))
                );
                net_paramLayout.setVerticalGroup(
                        net_paramLayout.createParallelGroup()
                                .addGroup(net_paramLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label22)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(net_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label23)
                                                .addComponent(ip_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label25)
                                                .addComponent(ip_gate_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(net_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label24)
                                                .addComponent(ip_subMask_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label26)
                                                .addComponent(ip_port_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(5, 5, 5)
                                        .addGroup(net_paramLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label28)
                                                .addComponent(ip_mac_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label27)
                                                .addComponent(net_set_button))
                                        .addContainerGap(18, Short.MAX_VALUE))
                );
            }
            tabbedPane1.addTab(CommonClass.i18nMessage.getString("printer_net_params"), net_param);

            //======== advanceSetting ========
            {

                //---- label4 ----
                label4.setText(CommonClass.i18nMessage.getString("write_commands"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(command_textArea);
                }

                //---- reset_button ----
                reset_button.setText(CommonClass.i18nMessage.getString("reset"));

                //---- command_button ----
                command_button.setText(CommonClass.i18nMessage.getString("send_commands"));

                GroupLayout advanceSettingLayout = new GroupLayout(advanceSetting);
                advanceSetting.setLayout(advanceSettingLayout);
                advanceSettingLayout.setHorizontalGroup(
                        advanceSettingLayout.createParallelGroup()
                                .addGroup(advanceSettingLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label4)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 567, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(advanceSettingLayout.createParallelGroup()
                                                .addComponent(command_button, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                                .addComponent(reset_button, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                                        .addContainerGap())
                );
                advanceSettingLayout.setVerticalGroup(
                        advanceSettingLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, advanceSettingLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(advanceSettingLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                                .addGroup(advanceSettingLayout.createSequentialGroup()
                                                        .addGroup(advanceSettingLayout.createParallelGroup()
                                                                .addComponent(label4)
                                                                .addComponent(reset_button))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                                                        .addComponent(command_button)))
                                        .addContainerGap())
                );
            }
            tabbedPane1.addTab(CommonClass.i18nMessage.getString("option_setting"), advanceSetting);
        }

        //---- just_rfid ----
        just_rfid.setText(CommonClass.i18nMessage.getString("rfid.calibration"));

        //---- getResult_button ----
        getResult_button.setText(CommonClass.i18nMessage.getString("rfid.result"));

        //======== CalibrationData_picturePanel ========
        {
            CalibrationData_picturePanel.setBackground(new Color(0xcccccc));

            GroupLayout CalibrationData_picturePanelLayout = new GroupLayout(CalibrationData_picturePanel);
            CalibrationData_picturePanel.setLayout(CalibrationData_picturePanelLayout);
            CalibrationData_picturePanelLayout.setHorizontalGroup(
                    CalibrationData_picturePanelLayout.createParallelGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
            );
            CalibrationData_picturePanelLayout.setVerticalGroup(
                    CalibrationData_picturePanelLayout.createParallelGroup()
                            .addGap(0, 132, Short.MAX_VALUE)
            );
        }

        //---- CalibrationData_label ----
        calibrationData_label.setText(CommonClass.i18nMessage.getString("label.curve"));

        //======== RSSI_data_picturePanel ========
        {
            RSSI_data_picturePanel.setBackground(new Color(0xcccccc));

            GroupLayout RSSI_data_picturePanelLayout = new GroupLayout(RSSI_data_picturePanel);
            RSSI_data_picturePanel.setLayout(RSSI_data_picturePanelLayout);
            RSSI_data_picturePanelLayout.setHorizontalGroup(
                    RSSI_data_picturePanelLayout.createParallelGroup()
                            .addGap(0, 277, Short.MAX_VALUE)
            );
            RSSI_data_picturePanelLayout.setVerticalGroup(
                    RSSI_data_picturePanelLayout.createParallelGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
            );
        }

        //---- RSSI_data_label2 ----
        RSSI_data_label2.setText(CommonClass.i18nMessage.getString("rfid.signal"));

        //======== rfid_panel ========
        {

            //---- uhf_inlay_label ----
            uhf_inlay_label.setHorizontalAlignment(SwingConstants.LEFT);
            uhf_inlay_label.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 10));
            uhf_inlay_label.setText(CommonClass.i18nMessage.getString("uhf_inlay"));

            //---- set_an_textField ----
            set_an_textField.setText("0");

            //---- set_an_label2 ----
            set_an_label2.setText("mm");

            //---- set_an_button ----
            set_an_button.setText(CommonClass.i18nMessage.getString("little_set"));

            //---- set_an_label1 ----
            set_an_label1.setText(CommonClass.i18nMessage.getString("set_an"));

            //---- set_wp_textField ----
            set_wp_textField.setText("0");

            //---- set_wp_label2 ----
            set_wp_label2.setText("mm");

            //---- set_wp_button ----
            set_wp_button.setText(CommonClass.i18nMessage.getString("little_set"));

            //---- set_wp_label1 ----
            set_wp_label1.setText(CommonClass.i18nMessage.getString("set_write_position"));

            //---- readWriteLine_label ----
            readWriteLine_label.setText(CommonClass.i18nMessage.getString("write_position") + "00mm");

            //---- rssiHeight_label ----
            rssiHeight_label.setText(CommonClass.i18nMessage.getString("rssi.height") + "00mm");

            //---- rssiNumber_label ----
            rssiNumber_label.setText(CommonClass.i18nMessage.getString("rssi.number") + "0");

            GroupLayout rfid_panelLayout = new GroupLayout(rfid_panel);
            rfid_panel.setLayout(rfid_panelLayout);
            rfid_panelLayout.setHorizontalGroup(
                    rfid_panelLayout.createParallelGroup()
                            .addGroup(rfid_panelLayout.createSequentialGroup()
                                    .addGroup(rfid_panelLayout.createParallelGroup()
                                            .addComponent(rssiNumber_label)
                                            .addComponent(rssiHeight_label)
                                            .addComponent(readWriteLine_label)
                                            .addComponent(set_wp_label1)
                                            .addGroup(rfid_panelLayout.createSequentialGroup()
                                                    .addComponent(set_wp_textField, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(set_wp_label2)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(set_wp_button))
                                            .addComponent(set_an_label1)
                                            .addGroup(rfid_panelLayout.createSequentialGroup()
                                                    .addComponent(set_an_textField, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(set_an_label2)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(set_an_button)))
                                    .addGap(0, 12, Short.MAX_VALUE))
                            .addComponent(uhf_inlay_label, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
            );
            rfid_panelLayout.setVerticalGroup(
                    rfid_panelLayout.createParallelGroup()
                            .addGroup(rfid_panelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(rssiNumber_label)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rssiHeight_label)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(readWriteLine_label)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(set_wp_label1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(rfid_panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(set_wp_button)
                                            .addComponent(set_wp_label2)
                                            .addComponent(set_wp_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(set_an_label1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(rfid_panelLayout.createParallelGroup()
                                            .addGroup(rfid_panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(set_an_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(set_an_label2))
                                            .addComponent(set_an_button))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(uhf_inlay_label, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //======== rfid_signal_panel ========
        {

            //---- sensor_level_label ----
            sensor_level_label.setText(CommonClass.i18nMessage.getString("label.sensor_level"));

            //---- sensor_level_label2 ----
            sensor_level_label2.setText(CommonClass.i18nMessage.getString("label.new_sensor_level"));

            //---- sensor_level_comboBox ----
            sensor_level_comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23"
            }));
            sensor_level_comboBox.setSelectedIndex(0);

            //---- sensor_level_button ----
            sensor_level_button.setText(CommonClass.i18nMessage.getString("little_set"));
            sensor_level_button.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 9));

            //---- RSSI_data_label ----
            RSSI_data_label.setText(CommonClass.i18nMessage.getString("rssi.data"));

            //======== RSSI_data_scrollPane ========
            {

                //---- RSSI_data_textArea ----
                RSSI_data_textArea.setEditable(false);
                RSSI_data_textArea.setLineWrap(true);
                RSSI_data_scrollPane.setViewportView(RSSI_data_textArea);
            }

            //---- 当前系统名称 ----
            sysMsg_label.setText(CommonClass.i18nMessage.getString("uname"));

            //---- 最大打印长度: ----
            sensorLevel_label3.setText(CommonClass.i18nMessage.getString("max_print_length"));

            //---- maxLength_textField ----
            maxLength_textField.setText("0");

            //---- label20 ----
            label20.setText("mm");

            //---- maxLength_button ----
            maxLength_button.setText(CommonClass.i18nMessage.getString("little_set"));
            maxLength_button.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 9));

            GroupLayout rfid_signal_panelLayout = new GroupLayout(rfid_signal_panel);
            rfid_signal_panel.setLayout(rfid_signal_panelLayout);
            rfid_signal_panelLayout.setHorizontalGroup(
                    rfid_signal_panelLayout.createParallelGroup()
                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(rfid_signal_panelLayout.createParallelGroup()
                                            .addComponent(RSSI_data_scrollPane)
                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                    .addGroup(rfid_signal_panelLayout.createParallelGroup()
                                                            .addComponent(RSSI_data_label)
                                                            .addComponent(sysMsg_label)
                                                            .addComponent(sensor_level_label)
                                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                                    .addComponent(sensor_level_label2)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addGroup(rfid_signal_panelLayout.createParallelGroup()
                                                                            .addComponent(sensor_level_button, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                                                    .addComponent(sensor_level_comboBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                                                    .addGap(18, 18, 18)
                                                                                    .addComponent(sensorLevel_label3)))
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addGroup(rfid_signal_panelLayout.createParallelGroup()
                                                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                                                    .addComponent(maxLength_textField, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                    .addComponent(label20))
                                                                            .addComponent(maxLength_button, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                                                    .addGap(0, 0, Short.MAX_VALUE)))
                                    .addContainerGap())
            );
            rfid_signal_panelLayout.setVerticalGroup(
                    rfid_signal_panelLayout.createParallelGroup()
                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                    .addComponent(sensor_level_label)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(rfid_signal_panelLayout.createParallelGroup()
                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                    .addGroup(rfid_signal_panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(sensor_level_label2)
                                                            .addComponent(sensor_level_comboBox, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(sensorLevel_label3))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(sensor_level_button, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(rfid_signal_panelLayout.createSequentialGroup()
                                                    .addGroup(rfid_signal_panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(maxLength_textField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label20))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(maxLength_button, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(4, 4, 4)
                                    .addComponent(RSSI_data_label)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(RSSI_data_scrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                                    .addComponent(sysMsg_label)
                                    .addContainerGap())
            );
        }

        //---- 标签高度（含间隙）:00mm ----
        label_height_label.setText(CommonClass.i18nMessage.getString("label.height_gap") + "00mm");

        //---- sensor_value_label ----
        sensor_value_label.setText("  ");

        //---- 标签校准 ----
        just_label.setText(CommonClass.i18nMessage.getString("label.calibration"));

        //---- 保存校准结果 ----
        saveResultButton.setText(CommonClass.i18nMessage.getString("save_result"));
        saveResultButton.setVisible(false);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(197, 197, 197)
                                .addComponent(just_rfid, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(just_label, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(getResult_button, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(rfid_signal_panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(RSSI_data_picturePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(RSSI_data_label2))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(label_height_label)
                                                        .addComponent(rfid_panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(saveResultButton)
                                                )
                                        )
                                        .addComponent(tabbedPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(calibrationData_label)
                                                .addGap(55, 55, 55)
                                                .addComponent(sensor_value_label))
                                        .addComponent(CalibrationData_picturePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(just_label)
                                        .addComponent(just_rfid)
                                        .addComponent(getResult_button))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(calibrationData_label)
                                        .addComponent(sensor_value_label))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CalibrationData_picturePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(RSSI_data_label2)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(RSSI_data_picturePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addComponent(label_height_label)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(rfid_panel, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                                                .addComponent(saveResultButton)
                                                        ))
                                                .addGap(23, 23, 23))
                                        .addComponent(rfid_signal_panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component

        // 在控件初始化时隐藏所有控件
        noPrinterUI();
        initComponentListener();
    }

    private JLabel printer_dpi_label;
    private JLabel paper_sensor_label;
    private JLabel print_mode_label;
    private JLabel firmware_label;
    private JComboBox<String> printerListBox;
    private JPanel uhf_param;
    private JLabel protocol_label;
    private JComboBox<String> protocol_comboBox;
    private JCheckBox checkBox_gjb_jm;
    private JComboBox<String> read_power_comboBox;
    private JComboBox<String> write_power_comboBox;
    private JComboBox<String> dr_value_comboBox;
    private JComboBox<String> frequency_comboBox;
    private JLabel frequency_label;
    private JPanel hf_param;
    private JComboBox<String> HF_power_comboBox;
    private JComboBox<String> HF_type_comboBox;
    private JButton refreshPrinterInfo_button;
    private JButton setPrinter_button;
    private JTextField ip_textField;
    private JTextField ip_subMask_textField;
    private JTextField ip_gate_textField;
    private JButton net_set_button;
    private JTextArea command_textArea;
    private JButton command_button;
    private JButton just_rfid;
    private JButton getResult_button;
    private JPanel CalibrationData_picturePanel;
    private JPanel RSSI_data_picturePanel;
    private JLabel RSSI_data_label2;
    private JPanel rfid_panel;
    private JLabel uhf_inlay_label;
    private JTextField set_an_textField;
    private JLabel set_an_label2;
    private JButton set_an_button;
    private JLabel set_an_label1;
    private JTextField set_wp_textField;
    private JLabel set_wp_label2;
    private JButton set_wp_button;
    private JLabel set_wp_label1;
    private JLabel readWriteLine_label;
    private JLabel rssiHeight_label;
    private JLabel rssiNumber_label;
    private JLabel sensor_level_label;
    private JComboBox<String> sensor_level_comboBox;
    private JButton sensor_level_button;
    private JLabel RSSI_data_label;
    private JScrollPane RSSI_data_scrollPane;
    private JTextArea RSSI_data_textArea;
    private JLabel sysMsg_label;
    private JTextField maxLength_textField;
    private JButton maxLength_button;
    private JLabel label_height_label;
    private JLabel sensor_value_label;
    private JButton just_label;
    private JButton delNetPrinter;
    private JTextField ip_mac_textField;
    private JButton reset_button;
    private JButton saveResultButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initComponentListener() {
        initPrintListBox();
        //刷新按钮事件
        refreshPrinterInfo_button.addActionListener(e -> {
            calibrationFunction.getUsbAndNetPrinters();
            refreshPrintListBox();
        });
        //更改打印机RFID校验参数事件
        setPrinter_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                try {
                    //即将更改RFID校验参数,是否继续?
                    if (warningOption(CommonClass.i18nMessage.getString("warning.set_rfid"))) {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");
                        PrinterVO printerVO = map.get(selectPrinter);
                        if (printerVO != null) {
                            set_RFID_Params(printerVO, isNet);
                            //设置完成,请重新开始校准.
                            showInformationDialog(CommonClass.i18nMessage.getString("success.set_rfid"));
                        }
                    }
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        //发送指令到打印机
        command_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                try {
                    String command = command_textArea.getText();
                    boolean isNet = selectPrinter.contains(".");
                    calibrationFunction.sendCommand(selectPrinter, command, isNet);
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        //仅RFID校准
        just_rfid.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                try {
                    printerOperator.getPrinterStatus(selectPrinter, 1);
                    boolean isNet = selectPrinter.contains(".");
                    calibrationFunction.sendCommand(selectPrinter, "MR", isNet);
                    saveResultButton.setVisible(false);
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        //仅标签校准
        just_label.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                try {
                    printerOperator.getPrinterStatus(selectPrinter, 1);
                    boolean isNet = selectPrinter.contains(".");
                    calibrationFunction.sendCommand(selectPrinter, "MD", isNet);
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        //查询结果按钮事件
        getResult_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                //只有打印机纸张校准结束停止动作后才能查询结果,是否继续?
                if (warningOption(CommonClass.i18nMessage.getString("warning.check_result"))) {
                    printerOperator.getPrinterStatus(selectPrinter, 1);
                    getPrinterInfo(selectPrinter);
                    showResult(selectPrinter);

                    PrinterVO printerVO = map.get(selectPrinter);
                    if (printerVO != null) {
                        if (printerVO.getPrinterType() == PrinterType.GJB || printerVO.getPrinterType() == PrinterType.UHF) {
                            saveResultButton.setVisible(true);
                        }
                    }
                }
            }

        });

        set_wp_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                //是否设定读写位置为
                if (warningOption(CommonClass.i18nMessage.getString("warning.set_write_position") + " " + set_wp_textField.getText() + "mm?")) {
                    try {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");
                        String wpValue = set_wp_textField.getText();
                        String commandstring = "#UM>WP" + String.format("%03d", Integer.parseInt(wpValue));//设置读写位置
                        calibrationFunction.sendCommand(selectPrinter, commandstring, isNet);
                        //设置完成,打印机的“纸张”和“碳带”指示灯同时闪烁时生效.
                        //注意:无需再做校准操作,直接开始打印即可.
                        showInformationDialog(CommonClass.i18nMessage.getString("success.set_write_position") + "\n\n" + CommonClass.i18nMessage.getString("success.behind_set_wp"));
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
            }
        });

        set_an_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                //是否设定AN值为
                if (warningOption(CommonClass.i18nMessage.getString("warning.set_an_value") + " " + set_an_textField.getText() + "mm?")) {
                    try {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");
                        String anValue = set_an_textField.getText();
                        String commandstring = "#UM>AN" + String.format("%02d", Integer.parseInt(anValue));//设置AN值
                        calibrationFunction.sendCommand(selectPrinter, commandstring, isNet);
                        //设置完成,打印机的“纸张”和“碳带”指示灯同时闪烁时生效,请重新开始校准.
                        showInformationDialog(CommonClass.i18nMessage.getString("success.set_params"));
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
            }
        });

        maxLength_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                //是否设定最大打印长度为
                if (warningOption(CommonClass.i18nMessage.getString("warning.set_max_print_length") + " " + maxLength_textField.getText() + "mm?")) {
                    try {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");
                        String maxLength = maxLength_textField.getText();
                        String commandString = "#UM>DM" + String.format("%04d", Integer.parseInt(maxLength));//设置AN值
                        calibrationFunction.sendCommand(selectPrinter, commandString, isNet);
                        showInformationDialog(CommonClass.i18nMessage.getString("success.set_params"));
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
            }
        });

        net_set_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                //即将更改打印机网络参数,是否继续?
                if (warningOption(CommonClass.i18nMessage.getString("warning.set_net_params"))) {
                    try {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");

                        String printerIp = ip_textField.getText();
                        String mask = ip_subMask_textField.getText();
                        String gate = ip_gate_textField.getText();

                        String commandString = "#UM>IA" + printerIp + "\r\n#UM>MK" + mask + "\r\n#UM>GT" + gate;

                        calibrationFunction.sendCommand(selectPrinter, commandString, isNet);


                        PrinterVO printerVO = map.get(selectPrinter);
                        if (printerVO != null) {
                            if (isNet) {
                                String oldAddr = selectPrinter;
                                map.remove(oldAddr);
                                PrinterDataFileCommon.renamePrinter(oldAddr, printerIp);
                                refreshPrintListBox();
                            }
                            //设置完成,打印机的“纸张”和“碳带”指示灯同时闪烁时,请重启打印机后点击刷新按钮生效!
                            showInformationDialog(CommonClass.i18nMessage.getString("success.set_net_params"));
                        }
                    } catch (Exception ex) {
                        throw new CalibrationException(ex.getMessage());
                    }

                }
            }
        });

        sensor_level_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                String sensor_level = Objects.requireNonNull(sensor_level_comboBox.getSelectedItem()).toString();
                //是否设定探测等级为
                if (warningOption(CommonClass.i18nMessage.getString("warning.set_sensor_level") + " " + sensor_level + " ?")) {
                    try {
                        printerOperator.getPrinterStatus(selectPrinter, 1);
                        boolean isNet = selectPrinter.contains(".");
                        String commandString = "#UM>DL" + String.format("%02d", Integer.parseInt(sensor_level));//设置探测等级
                        calibrationFunction.sendCommand(selectPrinter, commandString, isNet);
                        showInformationDialog(CommonClass.i18nMessage.getString("success.set_params"));
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
            }
        });

        printerListBox.addActionListener(e -> {
            noPrinterUI();
            if (printerListBox.getSelectedItem() != null) {
                String select = printerListBox.getSelectedItem().toString();
                String addr = select.split(",")[1];
                selectPrinter = addr;
                getPrinterInfo(addr);
                delNetPrinter.setEnabled(selectPrinter.contains("."));
            } else {
                selectPrinter = "";
                delNetPrinter.setEnabled(false);
            }
        });

        delNetPrinter.addActionListener(e -> {
            if (!selectPrinter.isEmpty() && selectPrinter.contains(".")) {
                PrinterDataFileCommon.deletePrinter(selectPrinter);
                map.remove(selectPrinter);

                refreshPrintListBox();
            }
        });

        reset_button.addActionListener(e -> {
            if (!selectPrinter.isEmpty()) {
                if (selectPrinter.contains(".")) {
                    //不支持网络打印机初始化,因为会重置ip地址
                    showErrorMessage(CommonClass.i18nMessage.getString("warning.net_cant_reset"));
                } else {
                    //重置打印机会将保存的一些参数如打印正反向,纸张间隙等删除.您可能需要重新校准纸张和设置其他参数,是否继续?
                    if (warningOption(CommonClass.i18nMessage.getString("warning.reset"))) {
                        try {
                            calibrationFunction.initializationPrinter(selectPrinter, false);
                            //打印机的纸张和碳带指示灯会闪烁,请关闭打印机2秒后重开!
                            if (warningOption(CommonClass.i18nMessage.getString("reset.next_step"))) {
                                calibrationFunction.resetPrinter(selectPrinter, false);
                                //重置打印机完成,请关闭打印机2秒后重开!
                                showInformationDialog(CommonClass.i18nMessage.getString("success.reset"));
                            } else {
                                //未自动复位,需要手动进行复位操作!
                                showErrorMessage(CommonClass.i18nMessage.getString("error.reset"));
                            }
                        } catch (Exception ex) {
                            showErrorMessage(ex.getMessage());
                        }
                    }
                }
            }
        });

        saveResultButton.addActionListener(e -> {
            String ins = getInstructString();
            String an = "#UM>AN" + set_an_textField.getText() + "\r\n";
            String wp = "#UM>WP" + String.format("%03d", Integer.parseInt(set_wp_textField.getText())) + "\r\n";
            String dl = "#UM>DL" + Objects.requireNonNull(sensor_level_comboBox.getSelectedItem()) + "\r\n";
            ins += an + wp + dl + "MD\r\n";
            System.out.println(ins);
            SaveConfigure saveConfigure = new SaveConfigure(this, ins);
            saveConfigure.setVisible(true);
        });
    }

    private void initPrintListBox() {
        if (!map.isEmpty()) {
            map.keySet().forEach(k -> {
                PrinterVO printervo = map.get(k);
                if (printervo != null) {
                    String content = printervo.getName() + "," + k;
                    printerListBox.addItem(content);
                }
            });
        } else {
            printerListBox.removeAllItems();
            delNetPrinter.setEnabled(false);
        }

        if (printerListBox.getItemCount() > 0) {
            printerListBox.setSelectedIndex(0);
            String select = printerListBox.getItemAt(0);
            String addr = select.split(",")[1];
            getPrinterInfo(addr);
            selectPrinter = addr;
            delNetPrinter.setEnabled(selectPrinter.contains("."));
        }
    }

    public void refreshPrintListBox() {
        int count = printerListBox.getItemCount();
        // 遍历comboBox,填充新的list
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String item = printerListBox.getItemAt(i);
            String addr = item.split(",")[1];
            list.add(addr);
        }
        // 筛选UI的list,去除查询填充的map中没有的设备
        List<String> noMapList = list.stream().filter(item -> !map.containsKey(item)).collect(Collectors.toList());
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String item = printerListBox.getItemAt(i);
            String addr = item.split(",")[1];
            if (noMapList.contains(addr)) {
                //筛选的时候如果有设备断线,判断当前选中的设备路径是否是断线的,如果是则设为空
                if (selectPrinter.equals(addr)) {
                    selectPrinter = "";
                }
                tempList.add(item);
            }
        }
        if (!tempList.isEmpty()) {
            tempList.forEach(t -> {
                String addr = t.split(",")[1];
                printerListBox.removeItem(t);
                noMapList.remove(addr);
            });
        }
        // 查询新的map是否有存在UI的list没有的元素(新设备)
        map.keySet().forEach(m -> {
            if (!list.contains(m)) {
                PrinterVO printervo = map.get(m);
                if (printervo != null) {
                    String content = printervo.getName() + "," + m;
                    printerListBox.addItem(content);
                }
            }
        });
        // 如果之前选择的设备断线了导致选择的打印机为空,并且map有数据,将选择设为第一个
        if (selectPrinter.isEmpty() && !map.isEmpty()) {
            printerListBox.setSelectedIndex(0);
            String content = printerListBox.getItemAt(0);
            selectPrinter = content.split(",")[1];
        }
        // 在填充了selectPrinter之后不为空则getInfo,否则判断为没有打印机
        noPrinterUI();
        if (!selectPrinter.isEmpty()) {
            getPrinterInfo(selectPrinter);
            delNetPrinter.setEnabled(selectPrinter.contains("."));
        } else {
            delNetPrinter.setEnabled(false);
        }
    }

    private void getPrinterInfo(String key) {
        int type = key.contains(".") ? 2 : 1;
        if (map.containsKey(key)) {
            PrinterVO printerVO = map.get(key);
            if (printerVO != null) {
                PrinterInfoVO printerInfoVO = printerVO.getVo() == null ? new PrinterInfoVO() : printerVO.getVo();
                // 纸张感应器和打印模式
                byte[] modelByte = ("RQ" + type + ",2\r\n").getBytes(StandardCharsets.UTF_8);
                byte[] netMsgByte = ("RQ" + type + ",3\r\n").getBytes(StandardCharsets.UTF_8);
                try {
                    String modelRead = LayoutUtils.getPrinterMessage(key, modelByte);
                    String[] r = modelRead.split(",");
                    //transfer=热转印,direct=热敏
                    printerInfoVO.setPrintModel(CommonClass.i18nMessage.getString(r[0].equals("1") ? "thermal.transfer" : "thermal.direct"));
                    printerInfoVO.setPaperSensor(Integer.valueOf(r[1]));

                    String netMsgRead = LayoutUtils.getPrinterMessage(key, netMsgByte);
                    String[] n = netMsgRead.split(",");
                    printerInfoVO.setPrinterIp(n[0]);
                    printerInfoVO.setSubMask(n[1]);
                    printerInfoVO.setGate(n[2]);
                    String macString = n[4].replace(" ", "-");
                    if (macString.endsWith("-"))
                        macString = macString.substring(0, macString.length() - 1);
                    printerInfoVO.setMac(macString);

                    if (printerVO.getPrinterType() != PrinterType.NORMAL) { // RFID

                        setPrinter_button.setVisible(true);
                        rfid_panel.setVisible(true);

                        RSSI_data_label.setVisible(true);
                        RSSI_data_scrollPane.setVisible(true);

                        RSSI_data_label2.setVisible(true);
                        RSSI_data_picturePanel.setVisible(true);

                        protocol_label.setVisible(false);
                        protocol_comboBox.setVisible(false);
                        checkBox_gjb_jm.setVisible(false);

                        just_rfid.setVisible(true);

                        byte[] rfidByte = ("RQ" + type + ",5\r\n").getBytes(StandardCharsets.UTF_8);
                        String rfidRead = LayoutUtils.getPrinterMessage(key, rfidByte).replace("\r", "").replace("\n", "");
                        String[] readList = rfidRead.split(",");
                        if (readList.length == 1 && readList[0].isEmpty()) {
                            //查询功率返回异常!
                            throw new RuntimeException(CommonClass.i18nMessage.getString("error.check_power"));
                        }
                        if (printerVO.getPrinterType() == PrinterType.HF) { // 高频
                            uhf_param.setVisible(false);
                            hf_param.setVisible(true);

                            int power = Integer.parseInt(readList[1]);
                            HF_power_comboBox.setSelectedIndex(power);//模块功率

                            byte[] hfByte = ("RQ" + type + ",7\r\n").getBytes(StandardCharsets.UTF_8);
                            String hfRead = LayoutUtils.getPrinterMessage(key, hfByte);
                            HF_type_comboBox.setSelectedIndex(Integer.parseInt(hfRead.split(",")[1]));//高频协议类型
                        } else { // 超高频或者GJB
                            uhf_param.setVisible(true);
                            hf_param.setVisible(false);

                            String readStr = printerVO.getPrinterType() == PrinterType.GJB ? readList[4] : readList[5];
                            String writeStr = printerVO.getPrinterType() == PrinterType.GJB ? readList[1] : readList[6];
                            read_power_comboBox.setSelectedItem(String.valueOf(Integer.parseInt(readStr)));
                            write_power_comboBox.setSelectedItem(String.valueOf(Integer.parseInt(writeStr)));
                            dr_value_comboBox.setSelectedItem(String.valueOf(Integer.parseInt(readList[7])));

                            if (printerVO.getVersion() >= 2.366f || printerVO.getPrinterType() == PrinterType.GJB) { // V2.36UR 开始支持RQ" + type + ",11 获取频段
                                byte[] moduleByte = ("RQ" + type + ",11\r\n").getBytes(StandardCharsets.UTF_8);
                                String moduleRead = LayoutUtils.getPrinterMessage(key, moduleByte);
                                String[] moduleList = moduleRead.split(",");
                                frequency_label.setVisible(true);
                                frequency_comboBox.setVisible(true);
                                if (moduleList.length == 2) { // 普通的RFID模块:9,915125 或 1,860000
                                    if (moduleList[0].equals("1"))
                                        frequency_comboBox.setSelectedIndex(0);//860000,即Auto8
                                    else
                                        frequency_comboBox.setSelectedIndex(1);//自动,Auto频段
                                } else if (moduleList.length == 5 && moduleList[0].equals("1")) { // 鲲鹏3合一模块,且是ISO协议:1,9,000000,000,0
                                    if (moduleList[1].equals("1"))
                                        frequency_comboBox.setSelectedIndex(0);//860000,即Auto8
                                    else
                                        frequency_comboBox.setSelectedIndex(1);//自动,Auto频段
                                } else {
                                    frequency_label.setVisible(false);
                                    frequency_comboBox.setVisible(false);
                                }

                                if (printerVO.getPrinterType() == PrinterType.GJB) {
                                    protocol_label.setVisible(true);
                                    protocol_comboBox.setVisible(true);

                                    //1,1,902750,500
                                    //4,9,000000,000,0
                                    //4,0,000000,000,0
                                    //1 => ISO/6C;2 => GB;4 => GJB
                                    if (moduleList[0].equals("1")) {
                                        protocol_comboBox.setSelectedIndex(0);//ISO 18000-6C
                                        checkBox_gjb_jm.setVisible(false);
                                    } else if (moduleList[0].equals("2")) {
                                        protocol_comboBox.setSelectedIndex(2);//GB
                                        checkBox_gjb_jm.setVisible(false);
                                    } else {
                                        protocol_comboBox.setSelectedIndex(1);//GJB 7377.1
                                        if (moduleList.length >= 5) {
                                            checkBox_gjb_jm.setVisible(true);
                                            checkBox_gjb_jm.setSelected(!moduleList[4].equals("0"));
                                        }
                                    }
                                }
                            }
                        }

                        float version = printerVO.getVersion();
                        if (version == 2.36) {
                            System.out.println("从2.31版本开始才能设置读写位置");
                            System.out.println("从2.38版本开始才能显示AN值");
                        }

                        if (version > 2.30) { //从2.31版本开始才能设置读写位置
                            //region 显示设定读写位置控件
                            set_wp_label1.setVisible(true);
                            set_wp_label2.setVisible(true);
                            set_wp_textField.setVisible(true);
                            set_wp_button.setVisible(true);

                            if (version >= 2.38) {
                                byte[] anByte = ("RQ" + type + ",12\r\n").getBytes(StandardCharsets.UTF_8);
                                String anRead = LayoutUtils.getPrinterMessage(key, anByte);
                                int anValue = (int) (Float.parseFloat(anRead) / (printerVO.getDpi() / 25.4f));
                                set_an_textField.setText(String.valueOf(anValue));

                                // 显示设定AN值控件
                                set_an_label1.setVisible(true);
                                set_an_label2.setVisible(true);
                                set_an_textField.setVisible(true);
                                set_an_button.setVisible(true);
                            }
                        }
                    } else { // 普通打印机,隐藏RFID校准框、RFID信号数据框、RFID信号图框
                        just_rfid.setVisible(false);
                        RSSI_data_scrollPane.setVisible(false);
                        RSSI_data_picturePanel.setVisible(false);
                        RSSI_data_label2.setVisible(false);
                        rfid_panel.setVisible(false);

                        uhf_param.setVisible(false);
                        hf_param.setVisible(false);
                        setPrinter_button.setVisible(false);
                    }
                    printerVO.setVo(printerInfoVO);
                    showPrinterMessage(printerVO);
                } catch (ConnectException e) {
                    showErrorMessage(ErrorCatcher.CatchConnectError(e.getMessage()));
                }
            }
        }
    }

    private void showPrinterMessage(PrinterVO v) {
        PrinterInfoVO printerInfoVO = v.getVo();
        firmware_label.setText(CommonClass.i18nMessage.getString("firmware") + " " + v.getFirmware());
        printer_dpi_label.setText(CommonClass.i18nMessage.getString("printer_dpi") + " " + (int) v.getDpi() + " DPI");
        print_mode_label.setText(CommonClass.i18nMessage.getString("print_mode") + " " + printerInfoVO.getPrintModel());
        //transmissive=穿透式,reflective=反射式
        paper_sensor_label.setText(CommonClass.i18nMessage.getString("paper_sensor") +
                " " +
                CommonClass.i18nMessage.getString(printerInfoVO.getPaperSensor() == 0 ? "paper_sensor.transmissive" : "paper_sensor.reflective"));

        ip_textField.setText(printerInfoVO.getPrinterIp());
        ip_subMask_textField.setText(printerInfoVO.getSubMask());
        ip_gate_textField.setText(printerInfoVO.getGate());
        ip_mac_textField.setText(printerInfoVO.getMac());

        // 显示通用的控件
        just_label.setVisible(true);
        getResult_button.setVisible(true);
        sensor_value_label.setVisible(true);
    }

    private void showResult(String addr) {
        if (!addr.isEmpty()) {
            try {
                PrinterVO printerVO = map.get(selectPrinter);
                if (printerVO != null) {
                    PrinterInfoVO printerInfoVO = printerVO.getVo();
                    printerOperator.getPrinterStatus(selectPrinter, 1);
                    String commandString = selectPrinter.contains(".") ? "AL\r\n" : "AD\r\n"; //获取校准曲线的数据
                    String readData = LayoutUtils.getPrinterMessage(addr, commandString.getBytes(StandardCharsets.UTF_8));

                    float labelGapHeightPixel = 0;
                    float labelHeightPixel = 0;//标签的高度,mm
                    float labelGapPixel = 0;//间隙的高度,mm
                    int adThreshold = -1;//AD阀值

                    if (readData.contains("$")) {
                        String[] dataArr = readData.split("\\$");
                        if (dataArr.length > 1) {
                            if (dataArr[1].contains("&")) { //0612&12@0216
                                String[] atData = dataArr[1].split("@"); //分割字符串
                                String[] andData = atData[0].split("&"); //分割字符串

                                int sensorLevel = Integer.parseInt(andData[1]);
                                adThreshold = Integer.parseInt(atData[1]);

                                if (sensorLevel > 0) {
                                    sensor_level_label.setText(CommonClass.i18nMessage.getString("label.sensor_level") + sensorLevel);
                                    sensor_level_comboBox.setSelectedItem(String.valueOf(sensorLevel));
                                }

                                int label_height_dots = Integer.parseInt(andData[0]);
                                labelGapHeightPixel = 25.4f * label_height_dots / printerVO.getDpi();//标签和间隙的高度
                            }

                            if (dataArr[0].contains(":")) {//包含校准数据
                                String[] dataArr2 = dataArr[0].split(":"); //分割字符串
                                String[] dataArr3 = dataArr2[1].split(","); //分割字符串

                                // 自动分析得到标签高度和间隙高度
                                Map<String, Float> map = LayoutUtils.GetCalibraLabelHeight(dataArr3, printerInfoVO.getPaperSensor(), adThreshold, printerVO.getDpi());
                                if (map == null) {
                                    labelHeightPixel = 0;//标签的高度,mm
                                    labelGapPixel = 0;//间隙的高度,mm
                                } else {
                                    labelHeightPixel = map.get("labelHeight");
                                    labelGapPixel = map.get("labelGap");
                                }

                                //将信号范围画出
                                int labelWidthDots = dataArr3.length - 1;//标签加间隙的高度,单位是mm
                                int labelHeight;//探测器返回值最高1024
                                int maxLength;//显示最大打印长度
                                int minValue = 1024;
                                int maxValue = 0;
                                for (int i = 0; i < dataArr3.length - 1; i++) {
                                    if (minValue > Integer.parseInt(dataArr3[i]))
                                        minValue = Integer.parseInt(dataArr3[i]);
                                    if (maxValue < Integer.parseInt(dataArr3[i]))
                                        maxValue = Integer.parseInt(dataArr3[i]);
                                }
                                labelHeight = maxValue - minValue + 1;
                                maxLength = (int) (labelWidthDots / (printerVO.getDpi() / 25.4f) + 0.5f);
                                sensor_value_label.setText("Max:" + maxValue + "   Min:" + minValue + "   Threshold:" + adThreshold);//显示最大打印长度,探测最大值和最小值
                                maxLength_textField.setText(String.valueOf(maxLength));
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
                                    int panelWidth = CalibrationData_picturePanel.getWidth();
                                    int panelHeight = CalibrationData_picturePanel.getHeight();
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

                                    CalibrationData_picture = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D graphics2D = CalibrationData_picture.createGraphics();
                                    Image img = calibrationImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                    graphics2D.drawImage(img, 0, 0, null);
                                    graphics2D.dispose();
                                } else {
                                    CalibrationData_picture = new BufferedImage(CalibrationData_picturePanel.getWidth(), CalibrationData_picturePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                                    Graphics2D graphics2D = CalibrationData_picture.createGraphics();
                                    graphics2D.setBackground(new Color(204, 204, 204));
                                    graphics2D.clearRect(0, 0, CalibrationData_picture.getWidth(), CalibrationData_picture.getHeight());
                                    graphics2D.dispose();
                                }
                            }
                        }
                    }

                    if (printerVO.getPrinterType() != PrinterType.NORMAL) {
                        String command = selectPrinter.contains(".") ? "SL\r\n" : "SI\r\n";
                        String readRfidData = LayoutUtils.getPrinterMessage(addr, command.getBytes(StandardCharsets.UTF_8));
                        RSSI_data_textArea.setText(readRfidData);//RFID的原始数据

                        if (readRfidData.contains(",")) {
                            String[] dataArr = readRfidData.split(",");
                            if (dataArr[0].contains(":")) {
                                String[] RSSI_data = dataArr[0].split(":");
                                dataArr[0] = RSSI_data[1];
                            }

                            int rssiBlock = 0;//RFID信号读取区域块,一般是一块,信号不好可能会出现多块
                            int rssiBlockHeight = 0;//最强那一块区域的高度
                            int rssiBlockHeightTemp = 0;//当前块区域的高度

                            //region 将信号范围画出
                            int rfidLabelHeight = dataArr.length - 1;//dataArr最后一行是读写位置和回退步数,不是信号值
                            float step0 = 1f * RSSI_data_picturePanel.getHeight() / rfidLabelHeight;//将图片高度分割成多条
                            int step = Math.round(step0);//浮点转整型,四舍五入
                            int RSSI_imageWidth = RSSI_data_picturePanel.getWidth();
                            int RSSI_imageHeight = step * rfidLabelHeight;
                            int readWriteLine = 0;

                            RSSI_data_picture = new BufferedImage(RSSI_imageWidth, RSSI_imageHeight, 1);
                            Graphics2D gr2 = RSSI_data_picture.createGraphics();
                            gr2.setBackground(new Color(255, 255, 255));
                            gr2.clearRect(0, 0, RSSI_data_picture.getWidth(), RSSI_data_picture.getHeight());
                            gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            for (int i = 0; i < rfidLabelHeight; i++) {
                                int RSSI_value = Integer.parseInt(dataArr[i]);
                                if (RSSI_value == 0 || (RSSI_value > 1 && RSSI_value < 150)) {//没有信号
                                    if (rssiBlockHeightTemp > 0)//上一个信号块结束后,得到的上一个信号块的高度
                                        rssiBlock++;
                                    rssiBlockHeightTemp = 0;

                                    gr2.setColor(Color.white);
                                    gr2.fillRect(0, (rfidLabelHeight - 1 - i) * step, RSSI_data_picture.getWidth(), step);
                                } else {//超高频或高频
                                    rssiBlockHeightTemp++;
                                    if (rssiBlockHeight < rssiBlockHeightTemp)
                                        rssiBlockHeight = rssiBlockHeightTemp;

                                    if (RSSI_value == 1)//高频
                                        gr2.setColor(new Color(0, 0, 255));
                                    else//超高频
                                        gr2.setColor(new Color(255 - RSSI_value, 255 - RSSI_value, 255));
                                    gr2.fillRect(0, (rfidLabelHeight - 1 - i) * step, RSSI_data_picture.getWidth(), step);
                                }
                                if (i == rfidLabelHeight - 1 && RSSI_value > 0)//信号开始直到结束
                                    rssiBlock++;
                            }
                            //endregion

                            //region 画出读写位置
                            String readWriteLineString = dataArr[dataArr.length - 1];//最后一行数据是读写位置信息
                            if (readWriteLineString.contains("&")) {
                                String[] readWriteLineList = readWriteLineString.split("&");
                                readWriteLineString = readWriteLineList[0].replace("$", "");
                                readWriteLine = Integer.parseInt(readWriteLineString);//得到读写位置

                                gr2.setColor(Color.red);
                                gr2.fillRect(0, (rfidLabelHeight - readWriteLine) * step, RSSI_data_picture.getWidth(), step);
                            }
                            //endregion

                            // 画出半透明的灰色块表示标签间隙
                            if (labelHeightPixel > 1f && labelGapPixel > 1f) {
                                gr2.setColor(new Color(150, 150, 150, 80));//画一个半透明的间隙阴影
                                gr2.fillRect(0, 0, RSSI_data_picture.getWidth(), Math.round(labelGapPixel * RSSI_data_picture.getHeight() / (labelHeightPixel + labelGapPixel)));

                            }

                            gr2.dispose();

                            if (labelHeightPixel > 1f && labelGapPixel > 1f)
                                label_height_label.setText(CommonClass.i18nMessage.getString("label.height") + labelHeightPixel + "(" + labelGapPixel + ")mm");
                            else
                                label_height_label.setText(CommonClass.i18nMessage.getString("label.height_gap") + String.format("%.2f", labelGapHeightPixel) + "mm");

                            rssiNumber_label.setText(CommonClass.i18nMessage.getString("rssi.number") + rssiBlock);
                            rssiHeight_label.setText(CommonClass.i18nMessage.getString("rssi.height") + rssiBlockHeight + "mm");//最大区域高度
                            readWriteLine_label.setText(CommonClass.i18nMessage.getString("write_position") + readWriteLine + "mm");//显示读写位置
                            set_wp_textField.setText(String.valueOf(readWriteLine));//文本框显示读写位置
                        }

                        if (printerVO.getPrinterType() == PrinterType.UHF || printerVO.getPrinterType() == PrinterType.GJB) {
                            int type = selectPrinter.contains(".") ? 2 : 1;
                            byte[] getInlayCommand = ("RQ" + type + ",8\r\n").getBytes(StandardCharsets.UTF_8);
                            String inlayRead = LayoutUtils.getPrinterMessage(addr, getInlayCommand);
                            inlayRead = inlayRead.substring(1, inlayRead.length() - 1);
                            if (inlayRead.contains("000000000000")) {
                                uhf_inlay_label.setText(CommonClass.i18nMessage.getString("uhf_inlay"));
                            } else {
                                uhf_inlay_label.setText(DataUtils.getInlayChipName(inlayRead));
                            }
                        }
                    } else { // 普通打印机
                        if (labelHeightPixel > 1f && labelGapPixel > 1f)
                            label_height_label.setText(CommonClass.i18nMessage.getString("label.height")  + labelHeightPixel + "(" + labelGapPixel + ")mm");
                        else
                            label_height_label.setText(CommonClass.i18nMessage.getString("label.height_gap")  + String.format("%.2f", labelGapHeightPixel) + "mm");

                        uhf_inlay_label.setText(CommonClass.i18nMessage.getString("uhf_inlay"));
                    }
                    this.repaint();//重画,将调用paint
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    private void set_RFID_Params(PrinterVO printerVO, boolean isNet) {
        try {
            if (printerVO.getPrinterType() != PrinterType.NORMAL) {
                if (printerVO.getPrinterType() == PrinterType.HF) { // 高频
                    String commandString = "HS2," +
                            HF_type_comboBox.getSelectedIndex() +
                            ",1," +
                            HF_power_comboBox.getSelectedIndex() +
                            "\r\n#UM>KL" +
                            HF_type_comboBox.getSelectedIndex();

                    calibrationFunction.sendCommand(selectPrinter, commandString, isNet);
                    try {
                        Thread.sleep(200);//设置后暂停一会儿让打印机处理
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else { // 超高频
                    String commandString = getInstructString();
                    calibrationFunction.sendCommand(selectPrinter, commandString, isNet);
                    try {
                        Thread.sleep(200);//设置后暂停一会儿让打印机处理
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                //普通打印机没有RFID功能
                showErrorMessage(CommonClass.i18nMessage.getString("warning.normal_printer"));
            }
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    private String getInstructString() {
        String readPower = Objects.requireNonNull(read_power_comboBox.getSelectedItem()).toString();
        String writePower = Objects.requireNonNull(write_power_comboBox.getSelectedItem()).toString();
        String commandString = "RW" + readPower + "," + writePower + "\r\n";//设置读写功率
        commandString += "AP" + readPower + "," + writePower + "\r\n";//设置读写功率
        commandString += "#UM>DR" + Objects.requireNonNull(dr_value_comboBox.getSelectedItem()) + "\r\n";

        if (protocol_comboBox.isVisible())//三标协议
        {
            //配置: #UM>UT0 => ISO/6C
            //# UM>UT1 => GJB;
            //# UM>UT2 => GB ;
            if (protocol_comboBox.getSelectedIndex() == 0)//ISO 18000-6C
                commandString += "#UM>UT0\r\n";//设置为ISO
            else if (protocol_comboBox.getSelectedIndex() == 1) {
                commandString += "#UM>UT1\r\n";//设置为GJB
                if (checkBox_gjb_jm.isSelected())
                    commandString += "#UM>KR1\r\n";//设置军密有效
                else
                    commandString += "#UM>KR0\r\n";//设置军密无效
            } else {
                commandString += "#UM>UT2\r\n";//设置为GB
            }
        }
        if (frequency_comboBox.isVisible() && frequency_comboBox.getSelectedIndex() > -1) {
            if (Objects.equals(frequency_comboBox.getSelectedItem(), "Auto8"))
                commandString += "#UM>RE00\r\n#UM>PR1\r\n";
            else
                commandString += "#UM>RE00\r\n#UM>PR9\r\n";
        }
        return commandString;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean warningOption(String message) {
        Object[] options = {CommonClass.i18nMessage.getString("ok"), CommonClass.i18nMessage.getString("cancel")};
        int check = JOptionPane.showOptionDialog(this, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return check == 0;
    }

    private void showInformationDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void noPrinterUI() {
        uhf_param.setVisible(false);
        hf_param.setVisible(false);
        setPrinter_button.setVisible(false);
        just_rfid.setVisible(false);
        just_label.setVisible(false);
        getResult_button.setVisible(false);
        sensor_value_label.setVisible(false);
        rfid_panel.setVisible(false);
        RSSI_data_scrollPane.setVisible(false);
        label_height_label.setText(CommonClass.i18nMessage.getString("label.height_gap") + "00mm");
        set_an_textField.setText("0");
        set_wp_textField.setText("0");
        RSSI_data_textArea.setText("");
        sensor_level_label.setText(CommonClass.i18nMessage.getString("label.sensor_level"));
        sensor_level_comboBox.setSelectedIndex(0);
        maxLength_textField.setText("0");

        firmware_label.setText(CommonClass.i18nMessage.getString("firmware") + "V0.00XX");
        print_mode_label.setText(CommonClass.i18nMessage.getString("print_mode") + "null");
        paper_sensor_label.setText(CommonClass.i18nMessage.getString("paper_sensor") + "null");
        printer_dpi_label.setText(CommonClass.i18nMessage.getString("printer_dpi") + "null");
    }
}
