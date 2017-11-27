package com.tingfeng.util.java.android.task;

import java.util.Map;

import com.tingfeng.util.java.android.common.HttpConnection;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
/**
 * 其中Object表示,传入的参数,Integer表示进度,String表示返回的结果
 * @author tingfeng
 *
 */
public abstract class HttpAsyncTask extends AsyncTask<Object, Integer, String> {
	
	private double httpAsyncTaskId;//通过id号码来确定线程的唯一编号;
	protected String ServerIp="http://"+"Constants.WebServiceHost"+"/";
	protected String ServerAddress=ServerIp+"";
    private ProgressBar progressBar; 
    private Map<String,String> patameterMap; 
    private Object obj;//用来保存一些Service等等的变量
    private String result;
    private String url;
     /**
      * @param webView
      * @param patameterMap 想要传递的参数
      * @param callBack 回调相关WebView中相关javascript的方法
      * @param progressBar
      */
    public HttpAsyncTask(Map<String, String> patameterMap,ProgressBar progressBar) {  
        super();
       this.patameterMap=patameterMap;
       this.progressBar = progressBar;  
       httpAsyncTaskId=Math.random();
    } 
  
  
    /**  
     * 这里的Object参数对应AsyncTask中的第一个参数   
     * 这里的String返回值对应AsyncTask的第三个参数  
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改  
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作  
     */  
    @Override
	protected String doInBackground(Object... arg0) {   	
    	String result="{\"success\":false,\"msg\":\"无法连接到网络!\"}";
		try {
            result=HttpConnection.httpconnection(url, patameterMap);
            return result;
		} catch (Exception e) {			
			Log.e("HttpAsyncTask:doInBackground:", e.toString());
			setResult(result);
			return result;
		}			
	}
    
    /**  
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）  
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置  
     */  
    @Override  
    protected void onPostExecute(String result) {
    	  setResult(result);
    	  if(beforePostExecute(getResult()))
    	  {  
	    	
		  afterMethodExecute(getResult());
	      }
    }  
    /**
     * 执行完了doInBackground
     * 可以用setResult来更改结果值
     * @param result
     * @return 返回true表示继续执行,返回false表示不执行onPostExecute中的内容
     */
    public boolean beforePostExecute(String result){   	
    	return true;
    }    
    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置  
    @Override  
    protected void onPreExecute() {  
       // textView.setText("开始执行异步线程");  
    }  
 /**
  * 在线程执行完毕之后执行的回调方法,可以在里面执行修改ui的相关操作
  * @param result
  * @param webView
  */
    public abstract void afterMethodExecute(String result);   
	/**  
     * 这里的Intege参数对应AsyncTask中的第二个参数  
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行  
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作  
     */  
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        int vlaue = values[0];  
        if(progressBar!=null)
        progressBar.setProgress(vlaue);  
    }                
    @Override
	public boolean equals(Object o) {
		if(o instanceof HttpAsyncTask && httpAsyncTaskId-((HttpAsyncTask) o).getHttpAsyncTaskId()==0)
			return true;
		return false;
	}
	@Override
	public int hashCode() {
		return (int)(httpAsyncTaskId*100000000);
	}	
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
		
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}	
	
	public Map<String, String> getpatameterMap() {
		return patameterMap;
	}
		
	public void setpatameterMap(Map<String, String> patameterMap) {
		this.patameterMap = patameterMap;
	}
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public double getHttpAsyncTaskId() {
		return httpAsyncTaskId;
	}


	public void setHttpAsyncTaskId(double httpAsyncTaskId) {
		this.httpAsyncTaskId = httpAsyncTaskId;
	}
	public Map<String, String> getPatameterMap() {
		return patameterMap;
	}


	public void setPatameterMap(Map<String, String> patameterMap) {
		this.patameterMap = patameterMap;
	}

	public String getServerAddress() {
		return ServerAddress;
	}


	public void setServerAddress(String serverAddress) {
		ServerAddress = serverAddress;
	}


	public String getServerIp() {
		return ServerIp;
	}


	public void setServerIp(String serverIp) {
		ServerIp = serverIp;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
    
	
    
}

