package common;

public enum LogType {
    ServiceData("ServiceData-"),
    ErrorData("ErrorData-"),
    CalibrationData("CalibrationData-");

    private final String path;

    LogType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
