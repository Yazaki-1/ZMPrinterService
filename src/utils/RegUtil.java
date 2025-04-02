package utils;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface RegUtil extends Library {

    RegUtil INSTANCE = Native.load("resources/dll/reg_set.dll", RegUtil.class);

    int set_auto_start(int start, String programPath);

    int get_reg();

}
