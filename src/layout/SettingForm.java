/*
 * Created by JFormDesigner on Thu Sep 04 13:42:16 CST 2025
 */

package layout;

import common.CommonClass;
import common.LogType;
import utils.DataJsonUtil;
import utils.RegUtil;

import java.awt.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author PH
 */
public class SettingForm extends JFrame {
    private static final boolean OS = System.getProperty("os.name").toLowerCase().contains("windows");

    public SettingForm() {
        initComponents();
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/logo.png"))).getImage());
        setTitle("设置");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // JFormDesigner - Variables declaration - DO 1 NOT MODIFY  //GE1N-BEGIN:variables  @formatter:off
        JTabbedPane tabbedPane1 = new JTabbedPane();
        JPanel panel8 = new JPanel();
        JPanel sslPanel = new JPanel();
        JPanel autoStartPanel = new JPanel();
        JLabel autoLabel = new JLabel();
        set_auto_start = new JCheckBox();
        JPanel defaultPorPanel = new JPanel();
        JLabel defaultPortLabel = new JLabel();
        portBox = new JTextField();
        JPanel systemTrayPanel = new JPanel();
        JLabel sysTrayLabel = new JLabel();
        autoTray = new JCheckBox();
        JPanel panel5 = new JPanel();
        JLabel label4 = new JLabel();
        connectBox = new JComboBox<>();
        JPanel panel6 = new JPanel();
        JLabel label5 = new JLabel();
        languageBox = new JComboBox<>();
        JPanel panel7 = new JPanel();
        JPanel panel9 = new JPanel();
        JButton cancel = new JButton();
        JButton apply = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBorder(null);

            //======== panel8 ========
            {
                panel8.setLayout(new GridLayout(0, 1));

                //======== AutoStartPanel ========
                {

                    //---- autoLabel ----
                    autoLabel.setText("开机启动");

                    //---- set_auto_start ----
                    set_auto_start.setText("设置开机自动启动");

                    GroupLayout AutoStartPanelLayout = new GroupLayout(autoStartPanel);
                    autoStartPanel.setLayout(AutoStartPanelLayout);
                    AutoStartPanelLayout.setHorizontalGroup(
                        AutoStartPanelLayout.createParallelGroup()
                            .addGroup(AutoStartPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(autoLabel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(set_auto_start, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addContainerGap())
                    );
                    AutoStartPanelLayout.setVerticalGroup(
                        AutoStartPanelLayout.createParallelGroup()
                            .addGroup(AutoStartPanelLayout.createSequentialGroup()
                                .addGroup(AutoStartPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(autoLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                    .addComponent(set_auto_start, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                    );
                }
                panel8.add(autoStartPanel);

                //======== DefaultPorPanel ========
                {

                    //---- defaultPortLabel ----
                    defaultPortLabel.setText("服务默认端口");

                    GroupLayout DefaultPorPanelLayout = new GroupLayout(defaultPorPanel);
                    defaultPorPanel.setLayout(DefaultPorPanelLayout);
                    DefaultPorPanelLayout.setHorizontalGroup(
                        DefaultPorPanelLayout.createParallelGroup()
                            .addGroup(DefaultPorPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(defaultPortLabel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(portBox, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addContainerGap())
                    );
                    DefaultPorPanelLayout.setVerticalGroup(
                        DefaultPorPanelLayout.createParallelGroup()
                            .addGroup(DefaultPorPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(defaultPortLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addComponent(portBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    );
                }
                panel8.add(defaultPorPanel);

                //======== SystemTrayPanel ========
                {

                    //---- sysTrayLabel ----
                    sysTrayLabel.setText("系统托盘");

                    //---- autoTray ----
                    autoTray.setText("启动自动缩小");

                    GroupLayout SystemTrayPanelLayout = new GroupLayout(systemTrayPanel);
                    systemTrayPanel.setLayout(SystemTrayPanelLayout);
                    SystemTrayPanelLayout.setHorizontalGroup(
                        SystemTrayPanelLayout.createParallelGroup()
                            .addGroup(SystemTrayPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(sysTrayLabel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoTray, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addContainerGap())
                    );
                    SystemTrayPanelLayout.setVerticalGroup(
                        SystemTrayPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, SystemTrayPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(autoTray, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addComponent(sysTrayLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    );
                }
                panel8.add(systemTrayPanel);

                //======== panel5 ========
                {

                    //---- label4 ----
                    label4.setText("服务连接类型");

                    //---- connectBox ----
                    connectBox.setModel(new DefaultComboBoxModel<>(new String[] {
                        "WebSocket",
                        "TCP"
                    }));

                    GroupLayout panel5Layout = new GroupLayout(panel5);
                    panel5.setLayout(panel5Layout);
                    panel5Layout.setHorizontalGroup(
                        panel5Layout.createParallelGroup()
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label4, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(connectBox, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    panel5Layout.setVerticalGroup(
                        panel5Layout.createParallelGroup()
                            .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                .addComponent(connectBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    );
                }
                panel8.add(panel5);

                //======== panel6 ========
                {

                    //---- label5 ----
                    label5.setText("语言(Language)");

                    //---- languageBox ----
                    languageBox.setModel(new DefaultComboBoxModel<>(new String[] {
                        "简体中文(Simplified Chinese)",
                        "English"
                    }));

                    GroupLayout panel6Layout = new GroupLayout(panel6);
                    panel6.setLayout(panel6Layout);
                    panel6Layout.setHorizontalGroup(
                        panel6Layout.createParallelGroup()
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label5, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(languageBox, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    panel6Layout.setVerticalGroup(
                        panel6Layout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(languageBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    );
                }
                panel8.add(panel6);

                //======== panel7 ========
                {

                    GroupLayout panel7Layout = new GroupLayout(panel7);
                    panel7.setLayout(panel7Layout);
                    panel7Layout.setHorizontalGroup(
                        panel7Layout.createParallelGroup()
                            .addGap(0, 368, Short.MAX_VALUE)
                    );
                    panel7Layout.setVerticalGroup(
                        panel7Layout.createParallelGroup()
                            .addGap(0, 40, Short.MAX_VALUE)
                    );
                }
                panel8.add(panel7);

                //======== panel9 ========
                {

                    GroupLayout panel9Layout = new GroupLayout(panel9);
                    panel9.setLayout(panel9Layout);
                    panel9Layout.setHorizontalGroup(
                        panel9Layout.createParallelGroup()
                            .addGap(0, 368, Short.MAX_VALUE)
                    );
                    panel9Layout.setVerticalGroup(
                        panel9Layout.createParallelGroup()
                            .addGap(0, 40, Short.MAX_VALUE)
                    );
                }
                panel8.add(panel9);
            }
            tabbedPane1.addTab("基础设置", panel8);

            //
            {
                sslPanel.setLayout(new GridLayout(0, 1));
            }
            tabbedPane1.addTab("基础设置", sslPanel);
        }

        //---- cancel ----
        cancel.setText("取消");

        //---- apply ----
        apply.setText("应用");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(apply)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cancel)
                    .addContainerGap())
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancel)
                        .addComponent(apply))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  1@formatter:on

        autoLabel.setVisible(OS);
        set_auto_start.setVisible(OS);
        portBox.setText(String.valueOf(CommonClass.localPort));
        autoTray.setSelected(CommonClass.tray);
        connectBox.setSelectedIndex(CommonClass.tcp_receive ? 1 : 0);
        languageBox.setSelectedIndex(CommonClass.language.equals("zh_CN") ? 0 : 1);

        if (OS) {
            int i = RegUtil.INSTANCE.get_reg();
            if (i == -1) {
                CommonClass.saveLog(CommonClass.i18nMessage.getString("err.get_autoStart_permission"), LogType.ErrorData);
            } else {
                set_auto_start.setSelected(i == 1);
            }
        }

        apply.addActionListener(e -> {
            if (OS) {
                int setAutoStart = set_auto_start.isSelected() ? 1 : 0;
                String regPath = System.getProperty("user.dir") + "\\ZMPrinterService.exe";
                int result = RegUtil.INSTANCE.set_auto_start(setAutoStart, regPath);
                if (result == 0) {
                    CommonClass.saveLog(CommonClass.i18nMessage.getString("err.set_autoStart_permission"), LogType.ErrorData);
                    CommonClass.auto_start = false;
                }
            }else {
                CommonClass.auto_start = false;
            }

            // --- sysTray ---
            CommonClass.tray = autoTray.isSelected();

            // --- connect type ---
            CommonClass.tcp_receive = Objects.equals(connectBox.getSelectedItem(), "TCP");

            if (Objects.equals(languageBox.getSelectedItem(), "English")) {
                CommonClass.language = "en_US";
                CommonClass.i18nMessage = ResourceBundle.getBundle("i18n/messages", new Locale("en", "US"));
            }else if (Objects.requireNonNull(languageBox.getSelectedItem()).toString().contains("Simplified Chinese")) {
                CommonClass.language = "zh_CN";
                CommonClass.i18nMessage = ResourceBundle.getBundle("i18n/messages", new Locale("zh", "CN"));
            }

            // --- default ip---
            try {
                int port = Integer.parseInt(portBox.getText());
                if (port < 1 || port > 65535) {
                    CommonClass.saveLog(CommonClass.i18nMessage.getString("error.port"), LogType.ErrorData);
                    showErrorMessage(CommonClass.i18nMessage.getString("error.port"));
                }else {
                    CommonClass.localPort = port;
                }

                DataJsonUtil.write();

                // 重载
                if (CommonClass.PARENT_LAYOUT != null) {
                    CommonClass.PARENT_LAYOUT.serverThread.interrupt();
                    CommonClass.PARENT_LAYOUT.serviceTrayMenu.removeTray();
                    CommonClass.PARENT_LAYOUT.dispose();
                    CommonClass.PARENT_LAYOUT = new PrinterService();
                    CommonClass.PARENT_LAYOUT.setVisible(true);
                }

                setVisible(false);
            } catch (NumberFormatException ex) {
                showErrorMessage(ex.getMessage());
            }
        });

        cancel.addActionListener(e -> setVisible(false));
    }

    private JCheckBox set_auto_start;
    private JTextField portBox;
    private JCheckBox autoTray;
    private JComboBox<String> connectBox;
    private JComboBox<String> languageBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:1on

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
