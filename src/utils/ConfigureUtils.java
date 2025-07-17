package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigureUtils {
    public static Map<String, File> configureCommandsMap(String fileDir) {
        Map<String, File> configureMap = new HashMap<>();
        File[] commands = new File(fileDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (commands != null) {
            for (File command : commands) {
                String name = command.getName().replace(".txt", "");
                configureMap.put(name, command);
            }
            return configureMap;
        }
        return new HashMap<>();
    }

    public static Map<String, BufferedImage> configureListMap(String fileDir, int containerWidth, int containerHeight) throws IOException {
        Map<String, BufferedImage> configureMap = new HashMap<>();
        File[] commands = new File(fileDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        if (commands != null) {
            for (File command : commands) {
                String name = command.getName().replace(".jpg", "");
                BufferedImage image = getLabelImage(command, containerWidth, containerHeight);
                configureMap.put(name, image);
            }
            return configureMap;
        }
        return new HashMap<>();
    }

    public static BufferedImage getLabelImage(File fileName, int containerWidth, int containerHeight) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(fileName);

        BufferedImage newImg = new BufferedImage(containerWidth, containerHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = newImg.createGraphics();
        Image image = bufferedImage.getScaledInstance(containerWidth, containerHeight, Image.SCALE_SMOOTH);
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return newImg;
    }
}
