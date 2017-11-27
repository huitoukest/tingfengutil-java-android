package com.tingfeng.util.java.android.baiduMap;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;

/**
 * 主要完成地址搜索功能,即传入关键字,传回搜索结果
 * @author dview76
 * 
 */
public abstract class BaseMapAddressSearch<T extends BaseMarker> implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener{	   	   
/**
 * 百度poiSearch
 */
private PoiSearch mPoiSearch = null;
/**
 * 百度SuggestionSearch
 */
private SuggestionSearch mSuggestionSearch = null;

/**
 * 搜索结果的List
 */
private ArrayList<T> markerModellist;
/**搜索结果的索引
 * 
 */
private int load_Index = 1;  

private ArrayAdapter<String> sugAdapter = null;
/**
 * 搜索的关键字
 */
private String search_keyWords;
/**
 * 搜索的城市
 */
private String search_cityName;

public BaseMapAddressSearch(){
	initData();
}
/**
 * 加载遮罩层,即在搜索结果返回之前起一个缓冲作用
 */
public abstract void showShadeView();
/**
 * 移除遮罩
 */
public abstract void removeView();

/**
 * 自定义的搜索结果,默认加载在百度搜索结果之前
 * @param search_cityName 搜索城市名称
 * @param search_KeyWords 搜索关键字
 * @return 返回搜索的结果,如果不需要自己搜索,可以返回null
 */
public abstract List<T> searchPoiByCustom(String search_cityName,String search_KeyWords);

/**
 * 
 * @param string
 * @param i 0表示短提醒,1表示长提醒
 */
public abstract  void  ShowToast(String string, int i);
/**
 * 根据百度搜索得到结果PoiInfo,将PoiInfo转换为我们需要的marker
 * @param poiInfo
 * @return
 */
public abstract T getMarkerByPoiInfo(PoiInfo poiInfo);
/**
 * 当获得搜索结果之后,会回调此方法
 * @param markerModellist
 */
public abstract void onGetSearchResult(ArrayList<T> markerModellist);

/**
 * 初始化地址搜索的相关信息
 */
protected void initData(){
	mPoiSearch = PoiSearch.newInstance();
	mPoiSearch.setOnGetPoiSearchResultListener(this);
	mSuggestionSearch = SuggestionSearch.newInstance();
	mSuggestionSearch.setOnGetSuggestionResultListener(this);
	markerModellist=new ArrayList<T>();
}

/**
 * 搜索地址,每次搜索之前会清空原来的结果
 * @param search_cityName 搜索城市的名称
 * @param search_KeyWords 搜索的关键字
 */
public void addressSearch(String search_cityName,String search_KeyWords){	
	this.search_cityName=search_cityName;
	if(search_cityName==null)
	{
		ShowToast("没有获取到所在城市!",0);
		return;
	}
	this.search_keyWords=search_KeyWords;
	if(search_keyWords==null||search_keyWords.trim().length()<1){
		return;
	}
	markerModellist.clear();	
	List<T> mmlist=searchPoiByCustom(search_KeyWords, search_KeyWords);
	if(mmlist!=null)
	markerModellist.addAll(mmlist);
	searchPoiByBaidu(load_Index);
}
/**
 * 搜索百度的结果
 * @param pageNum 页码.默认1为起始页
 */
private void searchPoiByBaidu(int pageNum){
	mPoiSearch.searchInCity((new PoiCitySearchOption()).city(search_cityName)
			.keyword(search_keyWords).pageNum(pageNum));
}

/**
 * 搜索进行建议搜索的总的次数
 */
private static int countOfSuggestCitySearch=-1;
/**
 * 建议搜索的纪律最多搜索的次数
 */
private static int countOfSuggestCity=-1;
/**
 * 获取到的搜索结果
 */
@Override
public void onGetPoiResult(PoiResult result) {			
	if (result == null|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
	}
	if (result.error == SearchResult.ERRORNO.NO_ERROR) {		
		List<PoiInfo> poiInfos = new ArrayList<PoiInfo>();
		poiInfos = result.getAllPoi();
		if (poiInfos!= null) {		
			for (PoiInfo poiInfo : poiInfos) {
	           T markerInfo=getMarkerByPoiInfo(poiInfo);
				 markerModellist.add(markerInfo);
			}			
		}
	}
	if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {					
		if(result.getSuggestCityList()!=null){
		if(countOfSuggestCity==-1){
			countOfSuggestCity=result.getSuggestCityList().size()<2?result.getSuggestCityList().size():2;
		}
			for (int i=0;i<result.getSuggestCityList().size()&&i<=2;i++) {
				CityInfo cityInfo=result.getSuggestCityList().get(i);
				mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityInfo.city)
						.keyword(search_keyWords).pageNum(0));
			}
		}else{
			countOfSuggestCitySearch=2;
		}		
		// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
		//ShowToast("未搜索到相关地点!");
		/*String strInfo = "在";
		for (CityInfo cityInfo : result.getSuggestCityList()) {
			strInfo += cityInfo.city;
			strInfo += ",";
		}
		strInfo += "找到结果";
		ShowToast(strInfo);*/
	}	
	if(countOfSuggestCity!=-1){
		countOfSuggestCitySearch++;
	}
	if(countOfSuggestCitySearch>=countOfSuggestCity)
	{
	 onGetSearchResult(markerModellist);
	 countOfSuggestCitySearch=-1;
	 countOfSuggestCity=-1;
	 removeView();
	}
}


@Override
public void onGetPoiDetailResult(PoiDetailResult result) {
	if (result.error != SearchResult.ERRORNO.NO_ERROR) {
		ShowToast("抱歉，未找到结果",0);
	} else {
		ShowToast(result.getAddress(),0);
	}
}

@Override
public void onGetSuggestionResult(SuggestionResult res) {
	if (res == null || res.getAllSuggestions() == null) {
		return;
	}
	sugAdapter.clear();
	for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
		if (info.key != null)
			sugAdapter.add(info.key);
	}
	sugAdapter.notifyDataSetChanged();
}

}
