/*
 * Created by JFormDesigner on Fri Mar 14 13:39:15 CST 2025
 */

package layout;

import common.CommonClass;
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
        setTitle(CommonClass.i18nMessage.getString("add_net"));
        Container contentPane = getContentPane();

        //======== netMsgPanel ========
        {
            netMsgPanel.setLayout(new GridLayout(2, 1));

            //======== ipPanel ========
            {

                //---- label1 ----
                label1.setText(CommonClass.i18nMessage.getString("ip_addr"));

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
            addPrinterBtn.setText(CommonClass.i18nMessage.getString("add_net"));
            btnPanel.add(addPrinterBtn);

            //---- testConnectBtn ----
            testConnectBtn.setText(CommonClass.i18nMessage.getString("test.connect"));
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
        testConnectBtn.addActionListener(e -> {
            try {
                String addr = ipText.getText();
                if (calibrationFunction.checkConnection(addr)) {
                    testConnectMessage.setText(CommonClass.i18nMessage.getString("success.test"));
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
                Object[] warn = {CommonClass.i18nMessage.getString("ok")};
                JOptionPane.showOptionDialog(this, CommonClass.i18nMessage.getString("test.first"), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, warn, warn[0]);
            } else {
                Object[] options = {CommonClass.i18nMessage.getString("ok"), CommonClass.i18nMessage.getString("cancel")};
                int check = JOptionPane.showOptionDialog(this, CommonClass.i18nMessage.getString("calibration.port"), "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (check == 0) {
                    // *文本框出现变化会将isCheck变为false*
                    String addr = ipText.getText();
                    if (RFID_Calibration.map.containsKey(addr)) {
                        JOptionPane.showOptionDialog(this, CommonClass.i18nMessage.getString("has_printer"), "Error", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                    } else {
                        try {
                            PrinterVO printerVO = calibrationFunction.addNetPrinter(addr);
                            boolean b = PrinterDataFileCommon.addPrinter(addr, printerVO);
                            if (b) {
                                int addCheck = JOptionPane.showOptionDialog(this, CommonClass.i18nMessage.getString("success.add"), "Success", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                                if (addCheck == 0) {
                                    parentDialog.refreshPrintListBox();
                                    this.setVisible(false);
                                }
                            } else {
                                JOptionPane.showOptionDialog(this, CommonClass.i18nMessage.getString("failed.add"), "Error", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
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
}
