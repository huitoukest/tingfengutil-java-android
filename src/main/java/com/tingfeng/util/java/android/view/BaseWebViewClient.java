package com.tingfeng.util.java.android.view;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BaseWebViewClient extends WebViewClient{
	/**
	 * 点击超链接的时候重新在原来进程上加载URL
	 * 返回false表示在当前页面打开url
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
         //view.loadUrl(url);//
         //return true;
		return false; 
     }
	/**
	 * 多张图片引用的是相同的src时，会只有一个image标签得到加载
	 */
	@Override
	 public void onPageFinished(WebView webView, String url) {
	        if(!webView.getSettings().getLoadsImagesAutomatically()) {
	            webView.getSettings().setLoadsImagesAutomatically(true);
	        }
	    }
}
