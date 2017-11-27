package com.tingfeng.util.java.android.task;

import java.util.List;

import com.tingfeng.util.java.java.Serialization.json.FastJsonUtils;
/**
 * 自动返回一个List<T>的回调,没有则返回null
 * @author dview76
 *
 * @param <T>
 */
public abstract class WebServiceListCallBack<T> extends WebServiceCallBack{
	/**
	 * callService先调用,callView后调用
	 * @param callService 给Service使用的回调
	 * @param callView 给View层使用的回调
	 */
	private Class<T> cls;
	public WebServiceListCallBack(Class<T> cls){
		this.cls=cls;
	}
    /**
     * 此方法不再会回调任何方法,需要的话,请在normalResult_CallServiceBack自行实现回调
     * 
     */
	@SuppressWarnings("unchecked")
	@Override
	public void normalResult_callBack(final String result) {	
		Thread thread=new Thread(){
			@Override
			public void run() {
				List<T> rms=null;
				try{
					Long start=System.currentTimeMillis();
					rms=(List<T>)FastJsonUtils.parseToArray(result, cls);
					//Log.Info(result.substring(1,100)+"stringToJsonTime:"+(System.currentTimeMillis()-start)/1000+"秒!");
					normalResult_CallServiceBack(rms);
				 }catch(Exception e){
					 unNormalResult_callBack(e.toString());
					 //Log.Error(this,e);
					 return;
				 }		
			 }				
	     };
		thread.start();
		
	}	
	public abstract void normalResult_CallServiceBack(List<T> ts) throws Exception;
}
