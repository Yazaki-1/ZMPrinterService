package data_processing;

import com.ZMPrinter.ZMPrinter;

import java.awt.image.BufferedImage;
import java.io.Serializable;

@SuppressWarnings("unused")
public class LabelData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String serial;
    private final ZMPrinter printer;
    private final long printWaiting;
    private final byte[] data;
    private final String clientRemote;
    private final int dataLen;
    private final BufferedImage image;
    private String jobName;
    private float labelWidth;
    private float labelHeight;

    public LabelData(ZMPrinter printer, long printWaiting, byte[] data, String clientRemote, BufferedImage image) {
        this.image = image;
        this.serial = "";
        this.printer = printer;
        this.printWaiting = printWaiting;
        this.data = data;
        this.clientRemote = clientRemote;
        this.dataLen = data.length;
    }

    public ZMPrinter getPrinter() {
        return printer;
    }

    public long getPrintWaiting() {
        return printWaiting;
    }

    public byte[] getData() {
        return data;
    }

    public String getClientRemote() {
        return clientRemote;
    }

    public int getDataLen() {
        return dataLen;
    }

    public String getSerial() {
        return serial;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public float getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(float labelWidth) {
        this.labelWidth = labelWidth;
    }

    public float getLabelHeight() {
        return labelHeight;
    }

    public void setLabelHeight(float labelHeight) {
        this.labelHeight = labelHeight;
    }
}
