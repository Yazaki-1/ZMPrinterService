package function;

import com.ZMPrinter.LabelType;
import com.ZMPrinter.ZMLabelobject;
import com.ZMPrinter.conn.ConnectException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public interface ZMPrinterFunction {
    byte[] buildBase64Image(String imageStr, int width, int height) throws IOException;

    byte[] getImageBytes(BufferedImage bufferedImage, int width, int height) throws IOException;

    void setBarcodeInfo(ZMLabelobject labelObject, float pixel, String x, String y, String direction) throws NumberFormatException;

    void setFontSize(ZMLabelobject object, String sizeStr, String scaleStr);

    void addVariables(ZMLabelobject labelObject, String dataString);

    Font getFontResize(ZMLabelobject labelObject, float newFontSize, int dpi);

    void checkTextPositionForBarcode(ZMLabelobject labelObject, String dataString);

    String getNameAndSn() throws ConnectException;

    String getNameAndSn(String serial) throws ConnectException;

    String getPrinterStatus(String address) throws ConnectException;

    String readTagData(String funcParams_One, LabelType labelType, Map<String, Integer> configuration) throws ConnectException, IllegalAccessException;
}
