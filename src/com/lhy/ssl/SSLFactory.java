package com.lhy.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.lhy.tools.Tools;

/**
 * 该类用来和服务器程序通信，建立SSL安全连接
 * 
 * @author lhy
 *
 */
public class SSLFactory implements GetSSLSocket {

	private int PORT;
	private SSLSocket ssl;
	private static char[] password = "astronomer".toCharArray();

	/**
	 * @param prop
	 * @param args
	 */
	private SSLSocket init() {
		KeyStore ts = null;
		SSLContext context = null;
		TrustManagerFactory tmf = null;
		try {
			ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream(Tools.getCerPath()), password);
			tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ts);
			TrustManager[] tm = tmf.getTrustManagers();
			context = SSLContext.getInstance("SSL");
			context.init(null, tm, null);
			SSLSocketFactory ssf = context.getSocketFactory();
			ssl = (SSLSocket) ssf.createSocket();
			ssl.connect(new InetSocketAddress(Tools.getServerIP(), PORT),
					Tools.getTimeOutOutGFW());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		}
		return ssl;
	}

	/**
	 * 返回一个SSLSocket
	 */
	@Override
	public SSLSocket getInstance() {
		return new SSLFactory().getSocket(new Random().nextInt(Tools
				.getServerPortCount()) + Tools.getServerPortStart());
	}

	private SSLSocket getSocket(int PORT) {
		this.PORT = PORT;
		return init();
	}
}
