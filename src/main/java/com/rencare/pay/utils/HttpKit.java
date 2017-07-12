package com.rencare.pay.utils;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;

/**
 * 创建时间：2016年11月8日 下午5:16:32
 * 
 * @author andy
 * @version 2.2
 */
public class HttpKit {

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final int CONNECT_TIME_OUT = 5000; // 链接超时时间3秒

	private static SSLContext wx_ssl_context = null; // 微信支付ssl证书

	static {

		Resource resource = new ClassPathResource("wx_apiclient_cert.p12"); // 获取微信证书
																			// 或者直接从文件流读取
		char[] keyStorePassword = ConfigUtil.getProperty("wx.mchid").toCharArray(); // 证书密码
		try {
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			keystore.load(resource.getInputStream(), keyStorePassword);
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keystore, keyStorePassword);
			SSLContext wx_ssl_context = SSLContext.getInstance("TLS");
			wx_ssl_context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 功能描述: get 请求
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @param headers
	 *            headers参数
	 * @return 请求失败返回null
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		AsyncHttpClient http = new AsyncHttpClient(
				new AsyncHttpClientConfig.Builder().setConnectTimeout(CONNECT_TIME_OUT).build());
		AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
		builder.setBodyEncoding(DEFAULT_CHARSET);
		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				builder.addQueryParam(key, params.get(key));
			}
		}

		if (headers != null && !headers.isEmpty()) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				builder.addHeader(key, params.get(key));
			}
		}
		Future<Response> f = builder.execute();
		String body = null;
		try {
			body = f.get().getResponseBody(DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		http.close();
		return body;
	}

	/**
	 * @description 功能描述: get 请求
	 * @param url
	 *            请求地址
	 * @return 请求失败返回null
	 */
	public static String get(String url) {
		return get(url, null);
	}

	/**
	 * @description 功能描述: get 请求
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @return 请求失败返回null
	 */
	public static String get(String url, Map<String, String> params) {
		return get(url, params, null);
	}

	/**
	 * @description 功能描述: post 请求
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @return 请求失败返回null
	 */
	public static String post(String url, Map<String, String> params) {
		AsyncHttpClient http = new AsyncHttpClient(
				new AsyncHttpClientConfig.Builder().setConnectTimeout(CONNECT_TIME_OUT).build());
		AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
		builder.setBodyEncoding(DEFAULT_CHARSET);
		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				builder.addQueryParam(key, params.get(key));
			}
		}
		Future<Response> f = builder.execute();
		String body = null;
		try {
			body = f.get().getResponseBody(DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		http.close();
		return body;
	}

	/**
	 * @description 功能描述: post 请求
	 * @param url
	 *            请求地址
	 * @param s
	 *            参数xml
	 * @return 请求失败返回null
	 */
	public static String post(String url, String s) {
		AsyncHttpClient http = new AsyncHttpClient(
				new AsyncHttpClientConfig.Builder().setConnectTimeout(CONNECT_TIME_OUT).build());
		AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
		builder.setBodyEncoding(DEFAULT_CHARSET);
		builder.setBody(s);
		Future<Response> f = builder.execute();
		String body = null;
		try {
			body = f.get().getResponseBody(DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		http.close();
		return body;
	}

	/**
	 * @description 功能描述: post https请求，服务器双向证书验证
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @return 请求失败返回null
	 */
	public static String posts(String url, Map<String, String> params) {

		AsyncHttpClient http = new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
				.setConnectTimeout(CONNECT_TIME_OUT).setSSLContext(wx_ssl_context).build());
		AsyncHttpClient.BoundRequestBuilder bbuilder = http.preparePost(url);
		bbuilder.setBodyEncoding(DEFAULT_CHARSET);
		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				bbuilder.addQueryParam(key, params.get(key));
			}
		}
		Future<Response> f = bbuilder.execute();
		String body = null;
		try {
			body = f.get().getResponseBody(DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		http.close();
		return body;
	}

	/**
	 * @description 功能描述: post https请求，服务器双向证书验证
	 * @param url
	 *            请求地址
	 * @param s
	 *            参数xml
	 * @return 请求失败返回null
	 */
	public static String posts(String url, String s) {
		AsyncHttpClient http = new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
				.setConnectTimeout(CONNECT_TIME_OUT).setSSLContext(wx_ssl_context).build());
		AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
		builder.setBodyEncoding(DEFAULT_CHARSET);
		builder.setBody(s);
		Future<Response> f = builder.execute();
		String body = null;
		try {
			body = f.get().getResponseBody(DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		http.close();
		return body;
	}
}