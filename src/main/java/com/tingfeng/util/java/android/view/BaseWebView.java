package com.tingfeng.util.java.android.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BaseWebView{
	 
	private WebViewClient webViewClient;
    private WebView webView; 
	private Activity baseActivity;
	private WebSettings webseting;
	private WebChromeClient baseWebChromeClient;
    
	public BaseWebView(WebView webView,Activity activity) {
    	this.webView=webView;
    	this.baseActivity=activity;
    	initBaseWebViewData();
	}
	public BaseWebView(int webViewId,Activity activity) {
    	this.webView=(WebView) activity.findViewById(webViewId);;
    	this.baseActivity=activity;
    	initBaseWebViewData();
	}
    public void setWebViewClient(WebViewClient webViewClient){
    	if(webViewClient!=null){
    		this.webViewClient=webViewClient;
    	}else if(webViewClient==null){
    		this.webViewClient=new BaseWebViewClient();
    	}
    }
    public void setWebChromeClient(WebChromeClient webChromeClient){
    	if(webChromeClient!=null){
    		this.baseWebChromeClient=webChromeClient;
    	}else if(baseWebChromeClient==null){
    		this.baseWebChromeClient=new BaseWebChromeClient(baseActivity);
    	}
    	this.webView.setWebChromeClient(baseWebChromeClient); 
    }
    @SuppressLint({"SetJavaScriptEnabled" })	
    public void initBaseWebViewData(){  
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){  
            // 4.0以后需要加入下列两行代码，才可以访问Web Service  
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
                    .detectDiskReads().detectDiskWrites().detectNetwork()  
                    .penaltyLog().build());   
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  
                    .penaltyLog().penaltyDeath().build());  
        }  
        //4.0以前版本不需要以上设置        
        //设置WebChromeClient  
         this.setWebViewClient(this.getWebViewClient());   	  
   	  //设置缓存
         this.webseting = webView.getSettings();	
         this.webseting.setJavaScriptEnabled(true);
         this.webseting.setDomStorageEnabled(true); 
         
         this.webseting.setAppCacheMaxSize(1024*1024*8);//设置缓冲大小，我设的是8M	    	  
	     String appCacheDir =baseActivity.getDir("cache", Context.MODE_PRIVATE).getPath();   	    	  
	     this.webseting.setAppCachePath(appCacheDir);	    	  
	     this.webseting.setAllowFileAccess(true);	    	  
	     this.webseting.setAppCacheEnabled(true);	    	  
	     this.webseting.setCacheMode(WebSettings.LOAD_DEFAULT);
        
	     this.setWebChromeClient(this.getBaseWebChromeClient());     	
        
    	//屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件:
	     this.webView.setOnLongClickListener(new OnLongClickListener() {         
            @Override  
            public boolean onLongClick(View v) {  
                return true;  
            }  
        });  
        
        if(Build.VERSION.SDK_INT >= 19) {//设置图片延迟加载
        	this.webView.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	    	this.webView.getSettings().setLoadsImagesAutomatically(false);
	    }	      	        
 }
	/**可以在parent的onDestroy中调用此方法
	 * 移出webView可以防止WebView中的一些服务在activity结束之后仍然运行;
	 * @param parantId
	 */
	public synchronized void removeWebViewFromParent(int parantLayoutId){
		ViewGroup viewgRoup =(ViewGroup)baseActivity.findViewById(parantLayoutId);
		 viewgRoup.removeView(this.webView);
		 this.webView.destroy();
	}
	
	
	public WebView getBaseWebView() {
		return this.webView;
	}

	public WebView getWebView() {
		return this.webView;
	}

	public Activity getBaseActivity() {
		return this.baseActivity;
	}

	public WebSettings getWebseting() {
		return this.webseting;
	}

	public WebChromeClient getBaseWebChromeClient() {
		return this.baseWebChromeClient;
	}

	public WebViewClient getWebViewClient() {
		return this.webViewClient;
	}
	
	
	
}
