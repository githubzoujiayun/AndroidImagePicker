package com.lidroid.xutils.util.core;

import com.lidroid.xutils.util.LogUtils;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.*;

/**
 * Created with IntelliJ IDEA.
 * User: wyouflf
 * Date: 13-10-11
 * Time: 上午11:52
 */
public class SimpleSSLSocketFactory extends SSLSocketFactory {

    private SSLContext sslContext = SSLContext.getInstance("TLS");

    private static KeyStore trustStore;

    static {
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    private static SimpleSSLSocketFactory instance;

    public static SimpleSSLSocketFactory getSocketFactory() {
        if (instance == null) {
            try {
                instance = new SimpleSSLSocketFactory();
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        return instance;
    }

    private SimpleSSLSocketFactory()
            throws UnrecoverableKeyException,
            NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException {
        super(trustStore);

        TrustManager trustAllCerts = new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }
        };
        sslContext.init(null, new TrustManager[]{trustAllCerts}, null);

        this.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
