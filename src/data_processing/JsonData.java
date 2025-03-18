package data_processing;

import com.ZMPrinter.ZMLabel;
import com.ZMPrinter.ZMLabelobject;
import com.ZMPrinter.ZMPrinter;
import com.alibaba.fastjson2.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class JsonData implements Serializable {
    @JSONField(name = "Printer")
    private ZMPrinter printer;

    @JSONField(name = "LabelFormat")
    private ZMLabel labelFormat;

    @JSONField(name = "LabelObjectList")
    private List<ZMLabelobject> labelObjectList;

    @JSONField(name = "Operate")
    private String operator;

    private List<String> labels;

    private List<String> lsfFileVarList;

    private String lsfFilePath;

    @JSONField(name = "Parameters")
    private String parameters;

    public JsonData(ZMPrinter printer, ZMLabel labelFormat, String operator, List<String> labels, List<String> lsfFileVarList, String lsfFilePath, String parameters) {
        this.printer = printer;
        this.labelFormat = labelFormat;
        this.operator = operator;
        this.labels = labels;
        this.lsfFileVarList = lsfFileVarList;
        this.lsfFilePath = lsfFilePath;
        this.parameters = parameters;
    }

    public ZMPrinter getPrinter() {
        return printer;
    }

    public void setPrinter(ZMPrinter printer) {
        this.printer = printer;
    }

    public ZMLabel getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(ZMLabel labelFormat) {
        this.labelFormat = labelFormat;
    }

    public List<ZMLabelobject> getLabelObjectList() {
        return labelObjectList;
    }

    public void setLabelObjectList(List<ZMLabelobject> labelObjectList) {
        this.labelObjectList = labelObjectList;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getLsfFileVarList() {
        return lsfFileVarList;
    }

    public void setLsfFileVarList(List<String> lsfFileVarList) {
        this.lsfFileVarList = lsfFileVarList;
    }

    public String getLsfFilePath() {
        return lsfFilePath;
    }

    public void setLsfFilePath(String lsfFilePath) {
        this.lsfFilePath = lsfFilePath;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
