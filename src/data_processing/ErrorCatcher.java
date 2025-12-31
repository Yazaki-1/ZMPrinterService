package data_processing;

import common.CommonClass;

public class ErrorCatcher {
    public static String CatchConnectError(String err) {
        if (err.contains("|")) {
            String code = err.substring(0, err.indexOf("|"));
            String key = "msg_code." + code;
            return CommonClass.i18nMessage.getString(key) + ":" + getErrorMsg(err);
        } else {
            return err;
        }
    }

    private static String getErrorMsg(String err) {
        if (err.contains("=>")) {
            String re = err.replace(" ", "");
            String[] msg = re.split("=>");
            return msg[1];
        }
        return "";
    }
}
