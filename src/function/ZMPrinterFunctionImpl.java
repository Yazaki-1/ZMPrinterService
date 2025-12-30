package function;

import com.ZMPrinter.*;
import com.ZMPrinter.conn.ConnectException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class ZMPrinterFunctionImpl implements ZMPrinterFunction {

    private static final PrinterOperator printerOperator = new PrinterOperatorImpl();

    @Override
    public byte[] buildBase64Image(String imageStr, int width, int height) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(imageStr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);//转为图片

        return getImageBytes(bufferedImage, width, height);
    }

    @Override
    public byte[] getImageBytes(BufferedImage bufferedImage, int width, int height) throws IOException {
        //将Image转换成流数据，并保存为byte[]
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (width > 0 || height > 0) {
            // 处理图片的缩放
            int newWidth = width > 0 ? width : bufferedImage.getWidth();
            int newHeight = height > 0 ? height : bufferedImage.getHeight();

            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();
            Image image = bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            graphics2D.drawImage(image, 0, 0, null);
            graphics2D.dispose();

            //将Image转换成流数据，并保存为byte[]
            ImageIO.write(newImage, "png", out);
        } else {
            //将Image转换成流数据，并保存为byte[]
            ImageIO.write(bufferedImage, "png", out);
        }
        out.flush();
        return out.toByteArray();
    }

    @Override
    public void setBarcodeInfo(ZMLabelobject labelObject, float pixel, String x, String y, String direction) throws NumberFormatException {
        labelObject.ObjectName = "barcode";
        labelObject.Xposition = Integer.parseInt(x) / pixel;//X坐标，单位mm
        labelObject.Yposition = Integer.parseInt(y) / pixel;//Y坐标，单位mm
        labelObject.direction = Integer.parseInt(direction);
    }

    @Override
    public void setFontSize(ZMLabelobject object, String sizeStr, String scaleStr) {
        try {
            int fontSize = Integer.parseInt(sizeStr);
            switch (fontSize) {
                case 1:
                    object.fontsize = 6;
                    break;
                case 2:
                    object.fontsize = 8;
                    break;
                case 3:
                    object.fontsize = 12;
                    break;
                case 4:
                    object.fontsize = 16;
                    break;
                default:
                    object.fontsize = 24;
                    break;
            }
        } catch (NumberFormatException e) {
            int scale;
            try {
                scale = Integer.parseInt(scaleStr);
            } catch (NumberFormatException ex) {
                scale = 6;
            }
            switch (scale) {
                case 1:
                    object.fontsize = 8;
                    break;
                case 2:
                    object.fontsize = 12;
                    break;
                case 3:
                    object.fontsize = 16;
                    break;
                case 4:
                    object.fontsize = 24;
                    break;
                case 5:
                    object.fontsize = 32;
                    break;
                default:
                    object.fontsize = 48;
                    break;
            }
        }
    }

    @Override
    public void addVariables(ZMLabelobject labelObject, String dataString) {
        List<ObjectVariable> variables = new ArrayList<>();
        ObjectVariable variable = new ObjectVariable();
        variable.data = dataString.replace("[Divider]", "|");//内容
        variables.add(variable);
        labelObject.Variables = variables;
        labelObject.datachild_IDs = "1";
    }

    @Override
    public Font getFontResize(ZMLabelobject labelObject, float newFontSize, int dpi) {
        switch (labelObject.fontstyle) {
            case 1:
                return new Font(labelObject.textfont, Font.BOLD, (int) (newFontSize / 72f * dpi + 0.5f));
            case 2:
                return new Font(labelObject.textfont, Font.ITALIC, (int) (newFontSize / 72f * dpi + 0.5f));
            case 3:
                return new Font(labelObject.textfont, Font.BOLD + Font.ITALIC, (int) (newFontSize / 72f * dpi + 0.5f));
            default:
                return new Font(labelObject.textfont, Font.PLAIN, (int) (newFontSize / 72f * dpi + 0.5f));
        }
    }

    @Override
    public void checkTextPositionForBarcode(ZMLabelobject labelObject, String dataString) {
        int textPosition;//默认是N
        try {
            char[] textChars = dataString.toCharArray();//字符串转char
            textPosition = Character.getNumericValue(textChars[0]);//char转int
            if (textPosition != 66 && textPosition != 78)//不等于66（B）也不等于78（N）
                textPosition = Integer.parseInt(dataString);//string转int
        } catch (Exception ex) {
            textPosition = 78;
        }
        labelObject.textposition = textPosition == 66 ? 0 : 2;
    }

    @Override
    public String getNameAndSn() throws ConnectException {
        List<String> printers = printerOperator.getPrinters();
        StringBuilder printerBuilder = new StringBuilder();
        printers.forEach(p -> printerBuilder.append(getNameAndSn(p)).append("|"));
        return printerBuilder.substring(0, printerBuilder.toString().length() - 1);
    }

    @Override
    public String getNameAndSn(String serial) throws ConnectException {
        boolean isNet = serial.contains(".");
        int index = isNet ? 2 : 1;
        byte[] command = ("RQ" + index + ",1\r\n").getBytes(StandardCharsets.UTF_8);
        String info = isNet ? printerOperator.sendAndReadPrinter(serial, command, 12301, null) : printerOperator.sendAndReadPrinter(serial, command, command.length, 1500, 1);
        info = info.replace("dpi", "").replace("\u0002", "").replace("\u0003", "").replace("\r", "").replace("\n", "");
        String[] infos = info.split(",");
        String name = infos[0];
        String dpi = infos[3];
        return name + "," + serial + "," + dpi;
    }

    @Override
    public String getPrinterStatus(String address) throws ConnectException {
        if (address.isEmpty()) {
            List<String> printers = printerOperator.getPrinters();
            return printerOperator.getPrinterStatus(printers.get(0), 1);
        } else {
            return printerOperator.getPrinterStatus(address, 1);
        }
    }

    @Override
    public String readTagData(String addr, LabelType labelType, Map<String, Integer> configuration, Integer timeout, int use_default) throws ConnectException, IllegalAccessException {
        try {
            long serial = Long.parseLong(addr);
            String serialNumber = serial == 1 ? "" : addr;
            if (serialNumber.isEmpty()) {
                List<String> printers = printerOperator.getPrinters();
                if (!printers.isEmpty()) {
                    serialNumber = printers.get(0);
                }
            }
            return printerOperator.readTag(serialNumber, labelType, configuration, timeout, use_default);
        } catch (NumberFormatException e) {
            // 未通过Parse,catch为ip
            if (addr.contains(",") && addr.contains(":")) {
                // 如果使用网络必须指定有ip和port,一般用于特殊环境防火墙开放
                // 打印机IP,接收目标IP:接收目标端口   192.168.8.180,192.168.8.188:12301
                String printerIp = addr.substring(0, addr.indexOf(","));
                String[] targetIpAndPort = addr.substring(addr.indexOf(",") + 1).split(":");
                String targetIp = targetIpAndPort[0];
                int targetPort;
                try {
                    targetPort = Integer.parseInt(targetIpAndPort[1]);
                } catch (NumberFormatException ex) {
                    throw new IllegalAccessException("port参数不对");
                }
                printerOperator.getPrinterStatus(printerIp, 0);
                return printerOperator.readTag(printerIp, targetPort, targetIp, labelType, configuration);
            } else {
                throw new IllegalAccessException("参数格式不正确");
            }
        }
    }
}
