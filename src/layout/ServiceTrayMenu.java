package layout;

import common.CommonClass;
import common.LogType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ServiceTrayMenu {

    protected SystemTray systemTray;
    private TrayIcon trayIcon;
    protected JPopupMenu systemTrayPopupMenu;
    protected JDialog hiddenDialog;
    protected static int POPUP_WIDTH = 150;
    protected static int POPUP_HEIGHT = 168;

    public ServiceTrayMenu() {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            systemTrayPopupMenu = getMenu();
            trayIcon = new TrayIcon(
                    new ImageIcon(Objects.requireNonNull(ServiceTrayMenu.class.getResource("/resources/logo16.png"))).getImage()
            );
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        systemTrayPopupMenu.setLocation(e.getX(), e.getY() - POPUP_HEIGHT);
                        hiddenDialog.setLocation(e.getX(), e.getY() - POPUP_HEIGHT);
                        systemTrayPopupMenu.setInvoker(hiddenDialog);
                        hiddenDialog.setVisible(true);
                        systemTrayPopupMenu.setVisible(true);
                    }
                }
            });
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                CommonClass.saveLog(CommonClass.i18nMessage.getString("err.tray") + e.getMessage(), LogType.ErrorData);
            }

            hiddenDialog = new JDialog();
            hiddenDialog.setSize(1, 1);
            hiddenDialog.setUndecorated(true);

            hiddenDialog.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowLostFocus(WindowEvent we) {
                    hiddenDialog.setVisible(false);
                }

                @Override
                public void windowGainedFocus(WindowEvent we) {
                }
            });
        }
    }

    public void removeTray() {
        systemTray.remove(trayIcon);
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    protected JPopupMenu getMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setPopupSize(POPUP_WIDTH, POPUP_HEIGHT);
        try {
            BufferedImage home = ImageIO.read(Objects.requireNonNull(ServiceTrayMenu.class.getResourceAsStream("/resources/home.png")));
            JMenuItem menuItem = getItem(" " + CommonClass.i18nMessage.getString("menu.home"), home);
            menuItem.addActionListener(e -> {
                if (CommonClass.PARENT_LAYOUT != null) {
                    CommonClass.PARENT_LAYOUT.setVisible(true);
                }
            });
            menu.add(menuItem);
        } catch (IOException e) {
            CommonClass.saveLog(CommonClass.i18nMessage.getString("err.tray") + e.getMessage(), LogType.ErrorData);
        }

        try {
            BufferedImage setting = ImageIO.read(Objects.requireNonNull(ServiceTrayMenu.class.getResourceAsStream("/resources/setting.png")));
            JMenuItem menuItem = getItem(" " + CommonClass.i18nMessage.getString("menu.setting"), setting);
            menuItem.addActionListener(e -> {
                SettingForm settingForm = new SettingForm();
                settingForm.setVisible(true);
            });
            menu.add(menuItem);
        } catch (IOException e) {
            CommonClass.saveLog(CommonClass.i18nMessage.getString("err.tray") + e.getMessage(), LogType.ErrorData);
        }

        try {
            BufferedImage about = ImageIO.read(Objects.requireNonNull(ServiceTrayMenu.class.getResourceAsStream("/resources/about.png")));
            JMenuItem menuItem = getItem(" " + CommonClass.i18nMessage.getString("menu.about"), about);
            String messageBody = "ZMPrintService Ver" +
                    CommonClass.SOFT_VERSION +
                    CommonClass.i18nMessage.getString("about_message") + "\n\n" +
                    "ZMIN Technologies.";
            menuItem.addActionListener(e ->
                    JOptionPane.showMessageDialog(null, messageBody,
                            "About",
                            JOptionPane.INFORMATION_MESSAGE));
            menu.add(menuItem);
        } catch (IOException e) {
            CommonClass.saveLog(CommonClass.i18nMessage.getString("err.tray") + e.getMessage(), LogType.ErrorData);
        }

        try {
            BufferedImage quit = ImageIO.read(Objects.requireNonNull(ServiceTrayMenu.class.getResourceAsStream("/resources/quit.png")));
            JMenuItem menuItem = getItem(" " + CommonClass.i18nMessage.getString("menu.quit"), quit);
            menuItem.addActionListener(e -> {
                System.exit(0);//关闭程序
            });
            menu.add(menuItem);
        } catch (IOException e) {
            CommonClass.saveLog(CommonClass.i18nMessage.getString("err.tray") + e.getMessage(), LogType.ErrorData);
        }

        menu.setBorderPainted(false);
        menu.setOpaque(true);
        menu.setUI(new PopupMenuUI() {
            @Override
            public void uninstallUI(JComponent c) {
                super.uninstallUI(c);
            }
        });
        menu.setLayout(new GridLayout(4, 1));

        return menu;
    }

    private JMenuItem getItem(String label, BufferedImage iconImage) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setIcon(new ImageIcon(iconImage));
        menuItem.setFont(new Font("黑体", Font.PLAIN, 15));
        menuItem.setUI(new BasicButtonUI() {
            @Override
            protected void uninstallDefaults(AbstractButton b) {
                b.setBorderPainted(false);
                b.setContentAreaFilled(false);
                b.setBackground(new Color(255, 255, 255));
                LookAndFeel.installProperty(b, "opaque", Boolean.TRUE);
                super.uninstallDefaults(b);
            }

            @Override
            protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();
                Icon icon = b.getIcon();
                Icon tmpIcon = null;

                if (icon == null) {
                    return;
                }

                Icon selectedIcon = null;

                if (model.isSelected()) {
                    selectedIcon = b.getSelectedIcon();
                    if (selectedIcon != null) {
                        icon = selectedIcon;
                    }
                }

                if (!model.isEnabled()) {
                    if (model.isSelected()) {
                        tmpIcon = b.getDisabledSelectedIcon();
                        if (tmpIcon == null) {
                            tmpIcon = selectedIcon;
                        }
                    }

                    if (tmpIcon == null) {
                        tmpIcon = b.getDisabledIcon();
                    }
                } else if (model.isPressed() && model.isArmed()) {
                    tmpIcon = b.getPressedIcon();
                    if (tmpIcon != null) {
                        clearTextShiftOffset();
                    }
                } else if (b.isRolloverEnabled() && model.isRollover()) {
                    if (model.isSelected()) {
                        tmpIcon = b.getRolloverSelectedIcon();
                        if (tmpIcon == null) {
                            tmpIcon = selectedIcon;
                        }
                    }

                    if (tmpIcon == null) {
                        tmpIcon = b.getRolloverIcon();
                    }
                }

                if (tmpIcon != null) {
                    icon = tmpIcon;
                }

                icon.paintIcon(c, g, 18, iconRect.y);
            }

            @Override
            protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
                super.paintText(g, c, textRect, text);
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                Color color = new Color(255, 255, 255, 255);
                Color hoverColor = new Color(246, 250, 254);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (b.getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else if (b.getModel().isArmed()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(color);
                }
                b.setSize(POPUP_WIDTH, 42);
                g2.fillRect(0, 0, b.getWidth(), b.getHeight());
                super.paint(g, c);
            }
        });
        return menuItem;
    }
}
