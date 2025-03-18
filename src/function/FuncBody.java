package function;

import com.ZMPrinter.PrintUtility;
import com.ZMPrinter.ZMLabel;
import com.ZMPrinter.ZMLabelobject;
import com.ZMPrinter.ZMPrinter;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FuncBody implements Serializable {
    private ZMPrinter printer;
    private ZMLabel label;
    private List<ZMLabelobject> labelObjects;
    private String remoteAddress;

    public FuncBody() {
        init();
    }

    public void init() {
        printer = new ZMPrinter();
        label = new ZMLabel();
        labelObjects = new ArrayList<>();
        remoteAddress = "";
    }

    public byte[] buildLabelCommand() {
        PrintUtility printUtility = new PrintUtility();
        return printUtility.CreateLabelCommand(printer, label, labelObjects);
    }

    public BufferedImage buildImage() {
        PrintUtility printUtility = new PrintUtility();
        return printUtility.CreateLabelImage(printer, label, labelObjects);
    }

    public float getPixel() {
        return printer.printerdpi / 25.4f;
    }

    public ZMPrinter getPrinter() {
        return printer;
    }

    public void setPrinter(ZMPrinter printer) {
        this.printer = printer;
    }

    public ZMLabel getLabel() {
        return label;
    }

    public void setLabel(ZMLabel label) {
        this.label = label;
    }

    public List<ZMLabelobject> getLabelObjects() {
        return labelObjects;
    }

    public void setLabelObjects(List<ZMLabelobject> labelObjects) {
        this.labelObjects = labelObjects;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
