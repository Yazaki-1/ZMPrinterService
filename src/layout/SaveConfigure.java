/*
 * Created by JFormDesigner on Thu Jul 17 11:06:33 CST 2025
 */

package layout;

import com.ZMPrinter.PrinterOperator;
import com.ZMPrinter.PrinterOperatorImpl;
import utils.ConfigureUtils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author PH
 */
public class SaveConfigure extends JDialog {
    private BufferedImage labelImage = null;
    private final String instruct;
    private final String configDir;
    private final List<String> fileNames = new ArrayList<>();

    public SaveConfigure(Window owner, String instruct) {
        super(owner);
        this.instruct = instruct;
        initComponents();

        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            String dir = System.getProperty("user.dir");
            configDir = dir + "/bin/configure/";
        } else if (os.toLowerCase().contains("linux")) {
            configDir = "/opt/zmsoft/ZMPrinterService/bin/configure/";
        } else
            throw new RuntimeException("暂不支持" + os);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (labelImage != null) {
            Graphics graphics = labelPictureContainer.getGraphics();
            graphics.drawImage(labelImage, 0, 0, null);
            graphics.dispose();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelPictureContainer = new JPanel();
        label1 = new JLabel();
        labelName = new JTextField();
        label2 = new JLabel();
        uploadButton = new JButton();
        label3 = new JLabel();
        saveButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== labelPictureContainer ========
        {
            labelPictureContainer.setBorder(LineBorder.createBlackLineBorder());

            GroupLayout labelPictureContainerLayout = new GroupLayout(labelPictureContainer);
            labelPictureContainer.setLayout(labelPictureContainerLayout);
            labelPictureContainerLayout.setHorizontalGroup(
                labelPictureContainerLayout.createParallelGroup()
                    .addGap(0, 449, Short.MAX_VALUE)
            );
            labelPictureContainerLayout.setVerticalGroup(
                labelPictureContainerLayout.createParallelGroup()
                    .addGap(0, 248, Short.MAX_VALUE)
            );
        }

        //---- label1 ----
        label1.setText("\u4e3a\u4fdd\u5b58\u7684\u6807\u7b7e\u8bbe\u7f6e\u4e00\u4e2a\u540d\u79f0");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));

        //---- label2 ----
        label2.setText("\u8bf7\u4e0a\u4f20\u4e00\u5f20\u6807\u7b7e\u56fe\u7247");
        label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));

        //---- uploadButton ----
        uploadButton.setText("\u4e0a\u4f20");

        //---- label3 ----
        label3.setText("*\u8bf7\u5728\u4fdd\u5b58\u6807\u7b7e\u53c2\u6570\u4e4b\u524d\u786e\u5b9aRFID\u8bfb\u5199\u529f\u7387\u662f\u5426\u80fd\u6b63\u5e38\u4f7f\u7528\uff01\uff01\uff01\uff01");
        label3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        label3.setForeground(new Color(0xff3333));

        //---- saveButton ----
        saveButton.setText("\u4fdd\u5b58");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(label1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(labelName, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                        .addComponent(labelPictureContainer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(label2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(uploadButton))
                                .addComponent(label3))
                            .addGap(0, 36, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGap(0, 373, Short.MAX_VALUE)
                            .addComponent(saveButton)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label2)
                        .addComponent(uploadButton))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(labelPictureContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(label3)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveButton)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

        setTitle("保存标签配置");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fileNames.clear();
                File[] commands = new File(configDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
                if (commands != null) {
                    for (File command : commands) {
                        String name = command.getName().replace(".txt", "");
                        fileNames.add(name);
                    }
                }
            }
        });

        saveButton.addActionListener(e -> {
            if (warningOption()) {
                try {
                    String fileName = labelName.getText();
                    if (fileName.isEmpty()) {
                        fileName = "标签" + new Date().getTime();
                    }
                    String finalName = fileName;
                    if (fileNames.stream().anyMatch(file -> file.toLowerCase().equals(finalName))) {
                        showErrorMessage("已有重复名称的标签！");
                    } else {
                        try (FileWriter writer = new FileWriter(configDir + finalName + ".txt", true)) { // true for append mode
                            writer.write(instruct);
                        } catch (IOException ei) {
                            showErrorMessage(ei.getMessage());
                        }

                        if (labelImage != null) {
                            try {
                                ImageIO.write(labelImage, "jpg", new File(configDir + finalName + ".jpg"));
                            } catch (Exception ei) {
                                showErrorMessage(ei.getMessage());
                            }
                        }

                        Object[] options = {"确定"};
                        int check = JOptionPane.showOptionDialog(this, "保存成功！", "Success", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (check == 0) {
                            setVisible(false);
                        }
                    }
                } catch (Exception ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });

        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            if (file != null) {
                if (file.getName().toLowerCase().endsWith(".jpg")) {
                    int width = labelPictureContainer.getWidth();
                    int height = labelPictureContainer.getHeight();

                    try {
                        labelImage = ConfigureUtils.getLabelImage(file, width, height);
                        repaint();
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                } else {
                    showErrorMessage("请选择JPG图片!");
                }
            }
        });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel labelPictureContainer;
    private JLabel label1;
    private JTextField labelName;
    private JLabel label2;
    private JButton uploadButton;
    private JLabel label3;
    private JButton saveButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private boolean warningOption() {
        Object[] options = {"确定", "取消"};
        int check = JOptionPane.showOptionDialog(this, "是否保存配置?请确认配置能正常使用!", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return check == 0;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
