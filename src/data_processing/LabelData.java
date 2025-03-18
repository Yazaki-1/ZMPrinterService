package data_processing;

import com.ZMPrinter.ZMPrinter;

import java.io.Serializable;

public class LabelData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ZMPrinter printer;
    private final long printWaiting;
    private final byte[] data;
    private final String clientRemote;
    private final int dataLen;

    public LabelData(ZMPrinter printer, long printWaiting, byte[] data, String clientRemote) {
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
}
