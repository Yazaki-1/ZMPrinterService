package utils;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface RegUtil extends Library {

    RegUtil INSTANCE = Native.load(getResourcePath(), RegUtil.class);

    int set_auto_start(int start, String programPath);

    int get_reg();

    static String getResourcePath() {
        System.out.println(1);
        String os = System.getProperty("os.name").toLowerCase().replace(" ", "");
        if (os.contains("windows7")) {
            return "resources/dll/reg_set_by_win7.dll";
        } else {
            return "resources/dll/reg_set.dll";
        }
    }

}
