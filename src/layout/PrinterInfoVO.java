package layout;

import java.io.Serializable;

@SuppressWarnings("unused")
public class PrinterInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String printModel;
    private Integer paperSensor;
    private String printerIp;
    private String subMask;
    private String mac;
    private String gate;

    public PrinterInfoVO() {

    }

    public String getPrintModel() {
        return printModel;
    }

    public void setPrintModel(String printModel) {
        this.printModel = printModel;
    }

    public Integer getPaperSensor() {
        return paperSensor;
    }

    public void setPaperSensor(Integer paperSensor) {
        this.paperSensor = paperSensor;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }

    public String getSubMask() {
        return subMask;
    }

    public void setSubMask(String subMask) {
        this.subMask = subMask;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }
}
