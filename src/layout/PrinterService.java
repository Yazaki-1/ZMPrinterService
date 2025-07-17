/*
 * Created by JFormDesigner on Wed Jan 22 16:02:36 CST 2025
 */

package layout;

import common.CommonClass;
import common.LogType;
import server.PrinterWebSocketServer;
import utils.NetUtils;
import utils.RegUtil;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author PH
 */
public class PrinterService extends JFrame {
    private Thread serverThread;
    private JTextField portBox;
    public static JTextArea service_message;

    public PrinterService() {
        initComponents();//初始化Swing组件
        serverThread = new Thread(() -> {
            System.out.println(portBox.getText());
            PrinterWebSocketServer server = new PrinterWebSocketServer(Integer.parseInt(portBox.getText()));
            try {
                server.start_server();
            } catch (InterruptedException _e) {
                String msg = "服务重启,端口:" + portBox.getText();
                CommonClass.saveLog(msg, LogType.ServiceData);
            } catch (Exception e) {
                String message = e.getMessage() + "\n";
                CommonClass.showServiceMsg(message);
            }
        });
        serverThread.start();
    }

    private void initComponents() {
        JPanel baseLayout = new JPanel();
        JPanel bottomLayout = new JPanel();
        JPanel panel6 = new JPanel();
        JLabel sys_msg_label = new JLabel();
        JButton restart = new JButton();
        JLabel label4 = new JLabel();
        JButton quit = new JButton();
        JPanel panel2 = new JPanel();
        JPanel panel7 = new JPanel();
        JLabel label9 = new JLabel();
        JTextField ipBox = new JTextField();
        JPanel panel8 = new JPanel();
        JLabel label10 = new JLabel();
        portBox = new JTextField();
        JLabel label8 = new JLabel();
        JButton configuration = new JButton();
        JLabel label7 = new JLabel();
        JButton calibration = new JButton();
        JScrollPane scrollPane1 = new JScrollPane();
        service_message = new JTextArea();

        //======== this ========
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/logo.png"))).getImage());
        setTitle("ZMPrintService");
        Container contentPane = getContentPane();

        //======== baseLayout ========
        {

            //======== bottomLayout ========
            {
                bottomLayout.setLayout(new BoxLayout(bottomLayout, BoxLayout.X_AXIS));

                //======== panel6 ========
                {

                    //---- sys_msg_label ----
                    sys_msg_label.setText("System Message Label");

                    GroupLayout panel6Layout = new GroupLayout(panel6);
                    panel6.setLayout(panel6Layout);
                    panel6Layout.setHorizontalGroup(
                            panel6Layout.createParallelGroup()
                                    .addComponent(sys_msg_label, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    );
                    panel6Layout.setVerticalGroup(
                            panel6Layout.createParallelGroup()
                                    .addComponent(sys_msg_label, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    );
                }
                bottomLayout.add(panel6);

                //---- restart ----
                restart.setText("重启服务");
                bottomLayout.add(restart);

                //---- label4 ----
                label4.setText("     ");
                bottomLayout.add(label4);

                //---- quit ----
                quit.setText("退出程序");
                bottomLayout.add(quit);
            }

            //======== panel2 ========
            {

                //======== panel7 ========
                {
                    panel7.setLayout(new BoxLayout(panel7, BoxLayout.X_AXIS));

                    //---- label9 ----
                    label9.setText("   Service IP    ");
                    panel7.add(label9);

                    //---- ipBox ----
                    ipBox.setEditable(false);
                    panel7.add(ipBox);
                }

                //======== panel8 ========
                {
                    panel8.setLayout(new BoxLayout(panel8, BoxLayout.X_AXIS));

                    //---- label10 ----
                    label10.setText("Service Port   ");
                    panel8.add(label10);
                    panel8.add(portBox);
                    int port = CommonClass.localPort == null ? 1808 : CommonClass.localPort;
                    portBox.setText(String.valueOf(port));

                    //---- label8 ----
                    label8.setText("   ");
                    panel8.add(label8);

                    //---- configuration ----
                    configuration.setText("一键配置");
                    panel8.add(configuration);

                    //---- label7 ----
                    label7.setText("   ");
                    panel8.add(label7);

                    //---- calibration ----
                    calibration.setText("RFID校准");
                    panel8.add(calibration);
                }

                //======== scrollPane1 ========
                {

                    //---- service_message ----
                    service_message.setEditable(false);
                    scrollPane1.setViewportView(service_message);
                }

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(panel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(scrollPane1))
                                        .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(panel7, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                );
            }

            GroupLayout baseLayoutLayout = new GroupLayout(baseLayout);
            baseLayout.setLayout(baseLayoutLayout);
            baseLayoutLayout.setHorizontalGroup(
                    baseLayoutLayout.createParallelGroup()
                            .addGroup(baseLayoutLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(baseLayoutLayout.createParallelGroup()
                                            .addComponent(bottomLayout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())
            );
            baseLayoutLayout.setVerticalGroup(
                    baseLayoutLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, baseLayoutLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bottomLayout, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(baseLayout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(baseLayout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

        // 窗体运行
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent _e) {
                String osName = System.getProperty("os.name").toLowerCase();
                String osArch = System.getProperty("os.arch");
                String runTime = System.getProperty("java.runtime.version");
                if (runTime.length() > 20)
                    runTime = runTime.substring(0, 20);
                String sys_msg = osName + ", " + osArch + " (" + runTime + ")";
                sys_msg_label.setText(sys_msg);

                String ip = NetUtils.getRealIP();//当前电脑ip地址
                ipBox.setText(ip);
                ipBox.setCaretPosition(0);

                systemTray();

                if (CommonClass.tray) {
                    setVisible(false);
                }

                CommonClass.saveLog(ip + "    " + sys_msg, LogType.ServiceData);
                setTitle("ZMPrintService [Ver" + CommonClass.SOFT_VERSION + "]");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });

        addWindowStateListener(e -> {
            if ((e.getNewState() & JFrame.ICONIFIED) != 0) {
                setVisible(false);
            }
        });

        configuration.addActionListener(e -> {
            try {
                RFID_Configuration rfidConfiguration = new RFID_Configuration(this);
                rfidConfiguration.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 重启服务按钮响应事件
        restart.addActionListener(e -> {
            serverThread.interrupt();
            serverThread = new Thread(() -> {
                PrinterWebSocketServer server = new PrinterWebSocketServer(Integer.parseInt(portBox.getText()));
                try {
                    server.start_server();
                } catch (InterruptedException _e) {
                    String msg = "服务重启,端口:" + portBox.getText();
                    CommonClass.saveLog(msg, LogType.ServiceData);
                } catch (Exception _e) {
                    CommonClass.showServiceMsg(_e.getMessage());
                }
            });
            serverThread.start();
        });

        // 退出按钮响应事件
        quit.addActionListener(e -> System.exit(0));

        // 消息窗口右键功能
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem clearItem = new JMenuItem("清空内容");
        clearItem.addActionListener(e -> service_message.setText("")); // 清空文本内容
        popupMenu.add(clearItem);

        // 为文本组件添加鼠标监听器以显示右键菜单
        service_message.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 检测右键点击
                    popupMenu.show(service_message, e.getX(), e.getY()); // 显示右键菜单
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 检测右键点击
                    popupMenu.show(service_message, e.getX(), e.getY()); // 显示右键菜单
                }
            }
        });

        calibration.addActionListener(e -> {
            try {
                RFID_Calibration rfidCalibration = new RFID_Calibration(this);
                rfidCalibration.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 系统托盘和右键按钮事件
    private void systemTray() {
        if (SystemTray.isSupported()) {
            // 获取系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/logo16.png"))).getImage();
            TrayIcon trayIcon = getTrayIcon(image);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                CommonClass.saveLog("系统托盘添加失败: " + e.getMessage(), LogType.ErrorData);
            }
        }
    }

    private TrayIcon getTrayIcon(Image image) {
        TrayIcon trayIcon = new TrayIcon(image);
        // 添加鼠标点击事件监听器
        trayIcon.addActionListener(e -> {
            // 双击托盘图标时的操作
            setVisible(!this.isVisible());
            setState(JFrame.NORMAL);
            toFront();
        });
        trayIcon.setPopupMenu(getPopupMenu());
        return trayIcon;
    }

    private PopupMenu getPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem menuItem = new MenuItem("Open");
        menuItem.addActionListener(e -> {
            setExtendedState(Frame.NORMAL);//窗体恢复正常化状态
            setVisible(true);//双击托盘图标显示窗体
            toFront();
        });
        popupMenu.add(menuItem);//添加一个菜单项

        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            CheckboxMenuItem checkboxMenuItem = new CheckboxMenuItem("Auto Start");

            int i = RegUtil.INSTANCE.get_reg();

            if (i == -1) {
                CommonClass.saveAndShow("获取自动启动状态失败!可能是权限不足的问题.", LogType.ErrorData);
            } else {
                checkboxMenuItem.setState(i == 1);
            }

            checkboxMenuItem.addItemListener(l -> {
                int setAutoStart = checkboxMenuItem.getState() ? 1 : 0;
                String regPath = System.getProperty("user.dir") + "\\ZMPrinterService.exe";
                int result = RegUtil.INSTANCE.set_auto_start(setAutoStart, regPath);
                if (result == 0) {
                    CommonClass.saveAndShow("设置自动启动失败!可能是权限不足的问题.", LogType.ErrorData);
                }
            });

            popupMenu.add(checkboxMenuItem);//添加一个菜单项
        }

        menuItem = new MenuItem("Quit");
        menuItem.addActionListener(e -> {
            System.exit(0);//关闭程序
        });
        popupMenu.add(menuItem);//添加一个菜单项

        menuItem = new MenuItem("About");
        String messageBody = "ZMPrintService Ver" +
                CommonClass.SOFT_VERSION +
                "是网页前端打印服务器，\n" +
                "它启动后使得在所有浏览器上都可以使用网页控制打印机。\n" +
                "建议将此应用程序设置为开机自动运行。\n\n" +
                "ZMPrintService is a web print server.\n" +
                "After it is started, it is possible to use web pages to control the printer on all browsers.\n" +
                "It is recommended to set this application to run automatically after system startup.\n\n" +
                "ZMIN Technologies.";
        menuItem.addActionListener(e ->
                JOptionPane.showMessageDialog(null, messageBody,
                        "About",
                        JOptionPane.INFORMATION_MESSAGE));
        popupMenu.add(menuItem);//添加一个菜单项
        return popupMenu;
    }
}