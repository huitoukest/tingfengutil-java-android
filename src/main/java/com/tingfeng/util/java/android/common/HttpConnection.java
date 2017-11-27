package com.tingfeng.util.java.android.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;


/**
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class HttpConnection {
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpconnection(String url, Map<String, String> params) {
		HttpClient client = new DefaultHttpClient();		
		HttpPost httpPost = new HttpPost(url);
		String online = "";
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.addAll(mapToPairList(params));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = null;
			try {
				// request time out（这个是请求超时时间）
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				// read time out（这个是读取数据超时时间）
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 20000);
				response = client.execute(httpPost);
			} catch (Exception e) {
				// TODO: handle exception
				return "timeOut";
			}
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					online = EntityUtils.toString(entity);
				}
			} else {
				online = "";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		client.getConnectionManager().shutdown();
		return online;
	}
	/**
	 * 
	 * @param params
	 * @return
	 */
	private static List<NameValuePair> mapToPairList(Map<String, String> params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (params == null)
			return nameValuePairs;

		for (Map.Entry<String, String> entry : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		return nameValuePairs;
	}
}