/*
 * Created by JFormDesigner on Thu Jul 17 11:06:33 CST 2025
 */

package layout;

import common.CommonClass;
import utils.ConfigureUtils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

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
            configDir = System.getProperty("user.home") + "/zmsoft/ZMPrinterService/bin/configure/";
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
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GE N-BEGIN:initComponents  @formatter:off
        labelPictureContainer = new JPanel();
        JLabel label1 = new JLabel();
        labelName = new JTextField();
        JLabel label2 = new JLabel();
        JButton uploadButton = new JButton();
        JLabel label3 = new JLabel();
        JButton saveButton = new JButton();

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
        label1.setText("为保存的标签设置一个名称");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));

        //---- label2 ----
        label2.setText("请上传一张标签图片");
        label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));

        //---- uploadButton ----
        uploadButton.setText("上传");

        //---- label3 ----
        label3.setText("*请在保存标签参数之前确定RFID读写功率是否能正常使用！！！！");
        label3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        label3.setForeground(new Color(0xff3333));

        //---- saveButton ----
        saveButton.setText("保存");

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

                        Object[] options = {CommonClass.i18nMessage.getString("ok")};
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

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GE N-BEGIN:variables  @formatter:off
    private JPanel labelPictureContainer;
    private JTextField labelName;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private boolean warningOption() {
        Object[] options = {CommonClass.i18nMessage.getString("ok"), CommonClass.i18nMessage.getString("cancel")};
        int check = JOptionPane.showOptionDialog(this, "是否保存配置?请确认配置能正常使用!", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return check == 0;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
