/*
 * Created by JFormDesigner on Fri Mar 14 13:39:15 CST 2025
 */

package layout;

import common.PrinterDataFileCommon;
import function.CalibrationFunction;
import function.CalibrationFunctionImpl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author PH
 */
public class NetPrinter extends JDialog {

    private boolean isCheck = false;
    private final CalibrationFunction calibrationFunction = new CalibrationFunctionImpl();
    private final RFID_Calibration parentDialog;

    public NetPrinter(Window owner, RFID_Calibration calibration) {
        super(owner);
        parentDialog = calibration;
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off 11
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off11
        JPanel netMsgPanel = new JPanel();
        JPanel ipPanel = new JPanel();
        JLabel label1 = new JLabel();
        ipText = new JTextField();
        testConnectMessage = new JLabel();
        JPanel btnPanel = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        JButton addPrinterBtn = new JButton();
        JButton testConnectBtn = new JButton();

        //======== this ========
        setTitle("添加网络打印机");
        Container contentPane = getContentPane();

        //======== netMsgPanel ========
        {
            netMsgPanel.setLayout(new GridLayout(2, 1));

            //======== ipPanel ========
            {

                //---- label1 ----
                label1.setText("IP地址：");

                GroupLayout ipPanelLayout = new GroupLayout(ipPanel);
                ipPanel.setLayout(ipPanelLayout);
                ipPanelLayout.setHorizontalGroup(
                    ipPanelLayout.createParallelGroup()
                        .addGroup(ipPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ipText, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                            .addContainerGap())
                );
                ipPanelLayout.setVerticalGroup(
                    ipPanelLayout.createParallelGroup()
                        .addGroup(ipPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(ipPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                .addComponent(ipText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                );
            }
            netMsgPanel.add(ipPanel);
            netMsgPanel.add(testConnectMessage);
        }

        //======== btnPanel ========
        {
            btnPanel.setLayout(new GridLayout(1, 2));

            //======== panel5 ========
            {

                GroupLayout panel5Layout = new GroupLayout(panel5);
                panel5.setLayout(panel5Layout);
                panel5Layout.setHorizontalGroup(
                    panel5Layout.createParallelGroup()
                        .addGap(0, 114, Short.MAX_VALUE)
                );
                panel5Layout.setVerticalGroup(
                    panel5Layout.createParallelGroup()
                        .addGap(0, 30, Short.MAX_VALUE)
                );
            }
            btnPanel.add(panel5);

            //======== panel6 ========
            {

                GroupLayout panel6Layout = new GroupLayout(panel6);
                panel6.setLayout(panel6Layout);
                panel6Layout.setHorizontalGroup(
                    panel6Layout.createParallelGroup()
                        .addGap(0, 114, Short.MAX_VALUE)
                );
                panel6Layout.setVerticalGroup(
                    panel6Layout.createParallelGroup()
                        .addGap(0, 30, Short.MAX_VALUE)
                );
            }
            btnPanel.add(panel6);

            //---- addPrinterBtn ----
            addPrinterBtn.setText("添加网络打印机");
            btnPanel.add(addPrinterBtn);

            //---- testConnectBtn ----
            testConnectBtn.setText("测试连接");
            btnPanel.add(testConnectBtn);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(netMsgPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(netMsgPanel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btnPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                isCheck = false;
                ipText.setText("");
            }
        });
//        initComponentsListener();
        testConnectBtn.addActionListener(e -> {
            try {
                String addr = ipText.getText();
                if (calibrationFunction.checkConnection(addr)) {
                    testConnectMessage.setText("连接测试成功!");
                    testConnectMessage.setForeground(new Color(69, 234, 72));
                    isCheck = true;
                }
            } catch (Exception ex) {
                testConnectMessage.setText(ex.getMessage());
                testConnectMessage.setForeground(new Color(209, 64, 64));
            }
        });

        addPrinterBtn.addActionListener(e -> {
            if (!isCheck) {
                JOptionPane.showMessageDialog(this, "请先测试链接是否可用！", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                Object[] options = {"确定", "取消"};
                int check = JOptionPane.showOptionDialog(this, "使用网络打印机校准还需要检查12301端口是否开放!\n确定添加网络打印机?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (check == 0) {
                    // *文本框出现变化会将isCheck变为false*
                    String addr = ipText.getText();
                    if (RFID_Calibration.map.containsKey(addr)) {
                        JOptionPane.showOptionDialog(this, "该网络打印机已存在!", "Error", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                    }else {
                        try {
                            PrinterVO printerVO = calibrationFunction.addNetPrinter(addr);
                            boolean b = PrinterDataFileCommon.addPrinter(addr, printerVO);
                            if (b) {
                                int addCheck = JOptionPane.showOptionDialog(this, "添加成功!", "Success", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                                if (addCheck == 0) {
                                    parentDialog.refreshPrintListBox();
                                    this.setVisible(false);
                                }
                            }else {
                                JOptionPane.showOptionDialog(this, "添加失败!", "Error", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                            }
                        } catch (Exception ex) {
                            testConnectMessage.setText(ex.getMessage());
                            testConnectMessage.setForeground(new Color(209, 64, 64));
                        }
                    }
                }
            }
        });
    }

    private JTextField ipText;
    private JLabel testConnectMessage;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on11

//    private void initComponentsListener() {
//        ipText.setDocument(new PlainDocument() {
//            @Override
//            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
//                String tmp = getText(0, offset).concat(str);
//                if (tmp.length() <= 3) {
//
//                }
////                Matcher m = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}").matcher("192.168.8.8");
////                if (m.matches()) {
////                    System.out.println("111");
////                }
////                super.insertString(offset, str, a);
//            }
//        });
//    }*/
}
