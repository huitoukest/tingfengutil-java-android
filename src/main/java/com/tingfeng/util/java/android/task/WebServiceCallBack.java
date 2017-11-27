package com.tingfeng.util.java.android.task;

public abstract class WebServiceCallBack {
	/**
	 * 用于标志当前是否已经产生了回调
	 */
     public boolean isCallBack=false;
     
	  /**
       * 在网络完成后调用此方法,然后此方法根据结果来调用
       * @param result
       */
      public void setResult(String result){  
    	  isCallBack=true;
    	  if(result==null)
    	  {
    		  unNormalResult_callBack("NULL POINT");
    	  }else if(result.trim().equals("true")){
    		  normalResult_callBack(result);
    	  }else if(result.equals("timeOut")){
 			 timeOutResult_callBack(result);
 			unNormalResult_callBack(result);
 		 }else if(result.equals("noNetwork")){
 			 noNetworkResult_callBack(result);
 			unNormalResult_callBack(result);
 		 }else if(result==""||result.trim().length()<1||!(result.trim().indexOf("[")==0||result.trim().indexOf("{")==0)){
    		noDataResult_callBack(result);
    		unNormalResult_callBack(result);
		}else{
 			 normalResult_callBack(result);			 
 		 }
      }
      
      /**
       * 没有数据返回的时候调用此方法
       * @param result
       * 
       */
      public void noDataResult_callBack(String result){};
      /**
       * 超时的时候调用此方法
       * @param result
       */
      public void timeOutResult_callBack(String result){};
      /**
       * 正常结果的时候调用此方法
       * @param result
       */
      public abstract void normalResult_callBack(String result);
      /**
       * 在没有数据/超时/连接不到网络的三种情况下,会回调此方法,如果需要对
       * 此三种情况细分,可以复写相关方法;
       * @param result
       */
      public abstract void unNormalResult_callBack(String result);
      /**
       * 连接不到网络或者服务器的时候调用此方法
       * @param result
       */
      public void noNetworkResult_callBack(String result){};
}
