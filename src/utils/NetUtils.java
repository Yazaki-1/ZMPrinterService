package utils;

import java.net.*;
import java.util.Enumeration;

public class NetUtils {
    //获取当前电脑ip
    public static String getRealIP() {
        try {
            StringBuilder currentIp = new StringBuilder();
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isVirtual() || !netInterface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null) {
                        // ipv4
                        if (ip instanceof Inet4Address) {
                            currentIp.append(ip.getHostAddress()).append(",  ");
                        }
                    }
                }
            }
            return currentIp.substring(0, currentIp.length() - 3);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static String isIp(String input) {
        try {
            InetAddress ipAddress = InetAddress.getByName(input);
            return ipAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
