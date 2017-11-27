package com.tingfeng.util.java.android.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

@SuppressWarnings("deprecation")
public class DynamicHttpClient {

	private String namespace;// ip地址
	private String methodName;
	private String wsdlUrl;
	private String soapResponseData;
	private int statusCode = -1;
	/**
	 * http连接超时时间;单位毫秒;
	 */
	public int http_connection_timeout = 6*1000;
	/**
	 * http读取数据超时时间;单位毫秒;
	 */
	public int http_so_timeout = 30*1000;

	public DynamicHttpClient() {
	};

	/**
	 * 
	 * @param namespace
	 * @param methodName
	 * @param wsdlUrl
	 */
	public DynamicHttpClient(String namespace, String methodName, String wsdlUrl) {

		this.namespace = namespace;
		this.methodName = methodName;
		this.wsdlUrl = wsdlUrl;
	}

	protected String getHttpKey(Map<String, String> patameterMap){
		String s="_";
		if(namespace!=null)
			s+=namespace;
		if(methodName!=null)
			s+=methodName;
		if(wsdlUrl!=null)
			s+=wsdlUrl;
		if(patameterMap!=null){
			s+=patameterMap.keySet().toString();
		}
		return s;
	}
	
	/**
	 * 返回的是http状态代码,如需要获取返回的内容,可以用 getter方法来获取
	 * 
	 * @param patameterMap
	 * @return
	 * @throws Exception
	 */
	public int invoke(Map<String, String> patameterMap) throws Exception {

		final PostMethod postMethod = new PostMethod(wsdlUrl);
		final String soapRequestData = buildRequestData(patameterMap);

		String httpKey=getHttpKey(patameterMap);
		synchronized (httpKey) {
			byte[] bytes = soapRequestData.getBytes("utf-8");
			InputStream inputStream = new ByteArrayInputStream(bytes, 0,
					bytes.length);
			RequestEntity requestEntity = new InputStreamRequestEntity(
					inputStream, bytes.length,
					"application/soap+xml; charset=utf-8; action=\""
							+ namespace + "" + methodName + "\"");
			postMethod.setRequestEntity(requestEntity);

			final HttpClient httpClient = new HttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT,
					http_connection_timeout);
			// read time out（这个是读取数据超时时间）
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, http_so_timeout);
			
			
			boolean hasSendRequest=false;
			Long startTime=System.currentTimeMillis();
			/**
			 * 防止网络死等,手动中断
			 */
			while(System.currentTimeMillis()-startTime<http_so_timeout+10*1000&&soapResponseData==null){
				if(!hasSendRequest){
					Thread thread=new Thread(new Runnable() {						
						@Override
						public void run() {
							try {// request time out（这个是请求超时时间）					
								statusCode = httpClient.executeMethod(postMethod);
							} catch (Exception e) {
								soapResponseData = "timeOut";
								return;
							}
							try {
								soapResponseData = postMethod.getResponseBodyAsString();
							} catch (IOException e) {
								e.printStackTrace();
							}							
						}
					});
					thread.start();				
				}
				hasSendRequest=true;
				Thread.sleep(1000);							
			}
			if (statusCode == 503 || statusCode == 103)
				soapResponseData = "<" + methodName + "Result>noNetwork</"
						+ methodName + "Result>";
			return statusCode;
		}
	}
 
   public void setRequestCode(int code){
	   this.statusCode=code;
   }
	
	private String buildRequestData(Map<String, String> patameterMap) {
		/*if(patameterMap==null)
			patameterMap=new HashMap<String, String>();*/
		StringBuffer soapRequestData = new StringBuffer();
		soapRequestData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		/**
		 * <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
		 * xmlns:ns="http://127.0.0.1:3333/"> <soap:Header/> <soap:Body>
		 * <ns:GetReserveBoundry> <ns:fromDate>2015-10-10</ns:fromDate>
		 * </ns:GetReserveBoundry> </soap:Body> </soap:Envelope>
		 **/

		soapRequestData
				.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\""
						+ namespace + "\">");
		soapRequestData.append("<soap:Header/><soap:Body>");

		boolean hasPatameter = false;
		String patameterString = "";
		if (patameterMap != null) {
			Set<String> nameSet = patameterMap.keySet();
			if (nameSet.size() > 0) {
				hasPatameter = true;
			}
			for (String name : nameSet) {
				patameterString += ("<ns:" + name + ">"
						+ patameterMap.get(name) + "</ns:" + name + ">");
			}
		}
		if (!hasPatameter) {
			soapRequestData.append("<ns:" + methodName + "/>");
		} else {
			soapRequestData.append("<ns:" + methodName + ">" + patameterString
					+ "</ns:" + methodName + ">");
		}
		soapRequestData.append("</soap:Body>");
		soapRequestData.append("</soap:Envelope>");

		return soapRequestData.toString();
	}

	/**
	 * 在遇上HTTPS安全模式或者操作cookie的时候使用HTTPclient会方便很多 使用HTTPClient（开源项目）向服务器提交参数
	 * 
	 * @param path
	 * @param params
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static boolean sendRequestFromHttpClient(String path,
			Map<String, String> params, String enc) throws Exception {
		// 需要把参数放到NameValuePair
		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramPairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		// 对请求参数进行编码，得到实体数据
		UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs,
				enc);
		// 构造一个请求路径
		HttpPost post = new HttpPost(path);
		// 设置请求实体
		post.setEntity(entitydata);
		// 浏览器对象
		DefaultHttpClient client = new DefaultHttpClient();
		// 执行post请求
		HttpResponse response = client.execute(post);
		// 从状态行中获取状态码，判断响应码是否符合要求
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		}
		return false;
	}

	/**
	 * 返回xml中<return>标签中的内容</return>
	 * 
	 * @return
	 */
	public String getSoapReturn() {

		String s = this.getSoapResponseData();
		if(s==null)
			return "";
		int start = s.indexOf("<" + methodName + "Result>");
		int end = s.lastIndexOf("</" + methodName + "Result>");
		if (start >= 0 && end >= 0) {
			start = start + ("<" + methodName + "Result>").length();
			s = s.substring(start, end);
			if (s.equals(""))
				s = "";
			return s;
		}

		return "";
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getWsdlLocation() {
		return wsdlUrl;
	}

	public void setWsdlLocation(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getSoapResponseData() {
		return soapResponseData;
	}

	public void setSoapResponseData(String soapResponseData) {
		this.soapResponseData = soapResponseData;
	}

	public static void main(String args[]) throws Exception {

		DynamicHttpClient dynamicHttpclientCall = new DynamicHttpClient(
				"http://192.168.1.91:3333", "GetReserveList",
				"http://192.168.1.91:3333/Webservice/ReserveWebService.asmx");
		// = new DynamicHttpClient( "http://service.test.ws.modules.dview.com/",
		// "sayHello","http://localhost:8888/ASPPV1/services/HelloService");

		Map<String, String> patameterMap = new HashMap<String, String>();

		// patameterMap.put("arg0","AA");
		patameterMap.put("id", "2070");
		String soapRequestData = dynamicHttpclientCall
				.buildRequestData(patameterMap);
		System.out.println(soapRequestData);

		int statusCode = dynamicHttpclientCall.invoke(patameterMap);
		if (statusCode == 200) {
			System.out.println("ok");
			System.out.println(dynamicHttpclientCall.getSoapReturn());
		} else {
			System.out.println("error" + statusCode);
		}

	}
}