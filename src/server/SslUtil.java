package server;

import common.CommonClass;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

public class SslUtil {

    public static SSLContext createSSLContext() throws Exception {
        String path = CommonClass.certPath;
        String pwd = CommonClass.password;
        KeyStore ks = KeyStore.getInstance("JKS"); /// "JKS"
        InputStream ksInputStream = Files.newInputStream(Paths.get(path)); /// 证书存放地址
        ks.load(ksInputStream, pwd.toCharArray());
        //KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
        kmf.init(ks, pwd.toCharArray());
        //SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        return sslContext;
    }
}
