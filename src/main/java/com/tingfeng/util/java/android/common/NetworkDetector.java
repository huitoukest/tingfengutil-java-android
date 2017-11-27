package com.tingfeng.util.java.android.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 网络是否连接
 * @author dview76
 *
 */
public class NetworkDetector {
	/**
	 * 有网络连接返回true,否则返回false
	 * @return
	 */
	public static boolean isConnecttedToInternet(Context context){
		   ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
			if (info != null && info.isAvailable()){ 
			return true;
			} 
			else
			{
			//Toast.makeText(context,"无互联网连接",Toast.LENGTH_SHORT).show();
			return false;
			}   
	   }
}
