package com.tingfeng.util.java.android.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebStorage.QuotaUpdater;

@SuppressWarnings("deprecation")
public class BaseWebChromeClient extends WebChromeClient{
	private Activity baseActivity;
	
	public BaseWebChromeClient(Activity activity) {
		this.baseActivity=activity;
	}
	
	//扩充缓存的容量 	        	
	@Override
	public void onReachedMaxAppCacheSize(long requiredStorage,
			long quota, QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		super.onReachedMaxAppCacheSize(requiredStorage*2, quota, quotaUpdater);
	}       	
    //处理javascript中的alert  
	@Override
	public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {  
        //构建一个Builder来显示网页中的对话框 
        Builder builder = new Builder(baseActivity);  
        builder.setTitle("Alert");  
        builder.setMessage(message);  
        builder.setPositiveButton(android.R.string.ok,  
            new OnClickListener() {  
        	@Override   
        	public void onClick(DialogInterface dialog, int which) {  
                	result.confirm();
                }
            });  
        builder.setCancelable(false);  
        builder.create();  
        builder.show();  
        return true;  
    }; 
    
	@Override
    //处理javascript中的confirm  
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {  
        Builder builder = new Builder(baseActivity);  
        builder.setTitle("confirm");  
        builder.setMessage(message);  
        builder.setPositiveButton(android.R.string.ok,  
            new OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                    result.confirm();  
                }  
            });  
        builder.setNegativeButton(android.R.string.cancel,  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                    result.cancel();  
                }  
            });  
        builder.setCancelable(false);  
        builder.create();  
        builder.show();  
        return true;  
    };  
          
    @Override  
    //设置网页加载的进度条  
    public void onProgressChanged(WebView view, int newProgress) {  
    	baseActivity.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);  
        super.onProgressChanged(view, newProgress);  	    	       
    }  
    @Override
    //设置应用程序的标题title  
    public void onReceivedTitle(WebView view, String title) {  
    	baseActivity.setTitle(title);  
        super.onReceivedTitle(view, title);  
    }
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {  
        Log.d("MyApplication", message + " -- From line "  
            + lineNumber + " of "  
            + sourceID);  
    } 
    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {  
        Log.d("MyApplication", cm.message() + " -- From line "  
            + cm.lineNumber() + " of "  
            + cm.sourceId() );  
        return true;  
    } 	    	    
}
