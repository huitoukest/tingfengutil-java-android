package com.tingfeng.util.java.android.baiduMap;

import java.util.HashMap;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 地图操作工具类  
 * **/
public class MapUtils implements OnGetGeoCoderResultListener{

	private String city;//当前城市名称
	private String street;//我的位置
	private GeoCoder geoCoder = null; // 搜索模块，也可去掉地图模块独立使用
	
	public void getAddress(LatLng latLng){
		 //地址反向搜索   根据经纬度查询地址
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        LatLng ptCenter = latLng;//指定坐标
		// 反Geo搜索
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
				.location(ptCenter));
	}
	
	public void getLatlng(String address) {
		/*this.activity = activity;*/
		geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
		// Geo搜索
		geoCoder.geocode(new GeoCodeOption().city("北京").address(address));
	}
	
	//地理编码搜索（用地址检索坐标）
    @Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			
			return;
		}
		/*activity.point = result.getLocation();
		activity.searchButtonProcess("美食");*/
		/*String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);*/
		
	}

    //、反地理编码搜索（用坐标检索地址）
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			//没有获取到位置
			return;
		}
		city = result.getAddressDetail().city;
		street  = result.getAddressDetail().street+result.getAddressDetail().streetNumber;
   /*     Constants.myCity=city;
        Constants.myStreet=street;*/
	}
	
}
