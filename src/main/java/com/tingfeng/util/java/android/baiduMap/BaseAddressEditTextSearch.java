package com.tingfeng.util.java.android.baiduMap;

import java.util.ArrayList;
import java.util.List;

import com.tingfeng.util.java.android.common.ScreenSizeUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * 
 * @author dview76
 * 同时是地址搜索,只是加入对通用的
 * 用EidtText搜索,用ListView展示的功能
 */
public abstract class BaseAddressEditTextSearch<T extends BaseMarker> extends BaseMapAddressSearch<T> {

	private EditText searchEdit=null;
	private String search_cityName="";
	/**
	 * 如果不需要某一个功能,传入null即可
	 * @param addressListView 如果传入list,则表示搜索到的结果将会自动映射到listView之上
	 * @param searchEdit 如果传入EditText则,表示会用EditText的结果来进行搜索
	 */
	public BaseAddressEditTextSearch(String search_cityName,EditText searchEdit) {
		this.search_cityName=search_cityName;
	    this.searchEdit=searchEdit;
	    initBaseAddressEditTextSearchShowByListViewData();
	}
    private void initBaseAddressEditTextSearchShowByListViewData(){
    	if(searchEdit!=null)
    	{
    		setTextOnchangeListener();
    		setOnEditorActionListener();
    	}
    }             	
	@Override
	public abstract void showShadeView();
	
	@Override
	public abstract void removeView();

	@Override
	public abstract List<T> searchPoiByCustom(String search_cityName,
			String search_KeyWords);

	@Override
	public abstract void ShowToast(String string, int i);

	@Override
	public abstract T getMarkerByPoiInfo(PoiInfo poiInfo);
    
	/**
     * showAddressList()此方法会自动调用ListView来展示结果
     */
	@Override
	public abstract void onGetSearchResult(ArrayList<T> markerModellist);
	/**
	 * 得到搜索结果之后会首先调用此方法
	 * @param markerModellist
	 * @return 返回false,表示结果不在listView中自动展示,返回true表示自动在lsitView中展示结果
	 */
	public abstract boolean isShowInListViewOnGetSearchResult(ArrayList<T> markerModellist);
	
	/**
     * 在给定的editText文字变动之后,回调此方法
     * @param s
     * @return 返回false,程序将不会根据此文字搜索结果,返回true则会搜索相应的结果
     */
	public abstract boolean onAfterTextChanged(Editable s);
	/**
	 * 点击软键盘go的时候,是否进行搜索
	 * @return
	 */
	public abstract boolean isSearchOnEditorActionGO();
	 /**
     * 监听目的地和出发地EditText输入的文字的变动
     */
    private void setTextOnchangeListener(){
    	//设置目的地监听器
    	searchEdit.addTextChangedListener(new TextWatcher() {  	  
        	@Override  
        	public void onTextChanged(CharSequence s, int start, int before,  
        	int count) { }       	  
        	@Override  
        	public void beforeTextChanged(CharSequence s, int start, int count,  
        	int after) {}    	  
        	@Override  
        	public void afterTextChanged(Editable s) {  
        		if(!onAfterTextChanged(s)){
        			return;      			
        		}else{
        			if(s!=null)
        			addressSearch(search_cityName,s.toString());
        		}
        	}
        });
    }
    /**
     * 
     * @param listView_address 
     * @param heightPercent 0~1之间的一个float数字,传入null,会默认0.75的比例;
     * @return 得到指定百分比高度的,在指定EditText下面浮动的一个RelativeLayout的一个位置参数
     */
    public RelativeLayout.LayoutParams getRelativeLayoutParams_belowEditText(ListView listView_address,Float heightPercent){
    	RelativeLayout.LayoutParams params=(LayoutParams)listView_address.getLayoutParams();
		//location[0] x坐标,location[1] y坐标
		int[] locationScreen=new int[2];
		//获取编辑按钮的底部绝对坐标
		searchEdit.getLocationOnScreen(locationScreen);
		int xbottom=locationScreen[1]+searchEdit.getHeight();
		params.topMargin=(int) (xbottom+0.01*ScreenSizeUtils.getWidth());
		if(heightPercent==null)
			heightPercent=0.75f;
		if(heightPercent<0) heightPercent=0f;
		if(heightPercent>1) heightPercent=1f;
		params.height=(int) (ScreenSizeUtils.getHeight()*0.75-xbottom);
		return params;
    }
    
    private void setOnEditorActionListener(){
	// 软键盘点击搜索
    	searchEdit.setOnEditorActionListener(new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView textView, int actionId,
				KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_GO) {
				if(isSearchOnEditorActionGO()){
					if(searchEdit.getText()!=null)
					addressSearch(search_cityName,searchEdit.getText().toString());
				    return true;
				}
			}
			return false;
		}
    	});
    }
    public EditText getSearchEdit() {
		return searchEdit;
	}
	public void setSearchEdit(EditText searchEdit) {
		this.searchEdit = searchEdit;
	}
	public String getSearch_cityName() {
		return search_cityName;
	}
	public void setSearch_cityName(String search_cityName) {
		this.search_cityName = search_cityName;
	}
}
