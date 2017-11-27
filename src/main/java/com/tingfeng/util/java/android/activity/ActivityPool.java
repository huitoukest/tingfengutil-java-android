package com.tingfeng.util.java.android.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;


public class ActivityPool {
	private List<Activity> mList = new LinkedList<Activity>(); 
    private static ActivityPool instance; 
 
    private ActivityPool() {   
    } 
    public synchronized static ActivityPool getInstance() { 
        if (null == instance) { 
            instance = new ActivityPool(); 
        } 
        return instance; 
    } 
    // add Activity  
    public synchronized void addActivity(Activity activity) { 
        mList.add(activity); 
    }
    
    /**
     * 如果有存在的实例,那么返回此实例,否则返回null
     * @param c
     * @return
     */
    public synchronized Activity getActivity(Class c){
    	 for (Activity activity : mList) { 
           if(activity.getClass().getName().equals(c.getName()))
        	   return activity;
         } 
    	 return null;
    }
    /**
     * 关闭列表中所有的,activity和程序
     */
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            }         
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 

}
