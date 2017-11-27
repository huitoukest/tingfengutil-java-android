package com.tingfeng.util.java.android.task;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import com.alibaba.fastjson.JSON;
/**
 * 
 * @author dview76
 * 自动返回T的回调,没有则回调传入null
 * @param <T>
 */
public abstract class WebServiceOneCallBack<T> extends WebServiceCallBack{
	/**
	 * callService先调用,callView后调用
	 * @param callService 给Service使用的回调
	 * @param callView 给View层使用的回调
	 */
	private Class<T> cls;
	public WebServiceOneCallBack(Class<T> cls){
		this.cls=cls;
	}
      /**
       * 此方法不再会回调任何方法,需要的话,请在normalResult_CallServiceBack自行实现回调
       */
	@Override
	public void normalResult_callBack(String result) { 
		T rms=null;
		try{
			rms=JSON.parseObject(result,cls);
			normalResult_CallServiceBack(rms);
		 }catch(Exception e){
			 unNormalResult_callBack(e.toString());
			 //Log.Error(this,e);
		 }
        
	}
	public abstract void normalResult_CallServiceBack(T t);
	/**
	 * 得到T的类型
	 * @return
	 */
	public Class<T> getType(){
        Class<T> entityClass = null;
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            entityClass = (Class<T>)p[0];
        }
     return entityClass;
	}

}
