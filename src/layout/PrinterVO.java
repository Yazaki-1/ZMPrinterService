package layout;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class PrinterVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private float dpi;
    private String firmware;
    private String productNumber;
    private float version;
    private PrinterType printerType;
    private boolean hasSelect;

    private PrinterInfoVO vo;

    public PrinterVO(String name, float dpi, String firmware, String productNumber) {
        this.name = name;
        this.dpi = dpi;
        this.firmware = firmware;
        this.productNumber = productNumber;
        this.vo = null;

        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(firmware);
        if (matcher.find()) {
            String ver = matcher.group().substring(1);
            this.version = Float.parseFloat(ver);
        }

        if (name.startsWith("ZRM")) {
            this.printerType = PrinterType.GJB;
        }else {
            //判断是RFID打印机还是普通打印机
            String rfidSign = productNumber.substring(3, 4);//1011002112字符串第4个字符为1是RFID，为0是普通打印机
            if (rfidSign.equals("0")) {
                this.printerType = PrinterType.NORMAL;
            }else {
                if (firmware.endsWith("HF")) {
                    this.printerType = PrinterType.HF;
                }else {
                    this.printerType = PrinterType.UHF;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDpi() {
        return dpi;
    }

    public void setDpi(float dpi) {
        this.dpi = dpi;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public PrinterType getPrinterType() {
        return printerType;
    }

    public void setPrinterType(PrinterType printerType) {
        this.printerType = printerType;
    }

    public boolean isHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(boolean hasSelect) {
        this.hasSelect = hasSelect;
    }

    public PrinterInfoVO getVo() {
        return vo;
    }

    public void setVo(PrinterInfoVO vo) {
        this.vo = vo;
    }
}
