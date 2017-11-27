package com.tingfeng.util.java.android.common;

import android.app.Activity;
import android.util.DisplayMetrics;



public class ScreenSizeUtils {
	private static Integer height=null;
	private static Integer width=null;
	private static String error="Please invo init method first!";
	private static DisplayMetrics  dm=null;
	
	public  static DisplayMetrics init(Activity activity){
		 //得到屏幕的大小  
		dm = new DisplayMetrics(); 
	    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);  	    
	    height=dm.heightPixels;
	    width=dm.widthPixels;
	    return dm;
	}
	
	public static DisplayMetrics getDisplayMetrics(){
		if(dm==null) 
		printInfo(error);
		return dm;
	}
	
	public static int getHeight(){
		if(height!=null) return height;
		printInfo(error);
		return 0;
	}
	
	public static int getWidth(){
		if(width!=null) return width;
		printInfo(error);
		return 0;
	}
	
	private static void printInfo(String s) {
		System.out.println(s);
	}
}
