package com.tingfeng.util.java.android.baiduMap;

import java.io.Serializable;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;

/**
 * 一个基础的搜索显示maker信息;
 * 点击电池marker信息的bean的接口
 * @author dview76
 * 构造函数中一般传入这个marker的除开图标/坐标之外的信息 
 */
public interface BaseMarker extends Serializable{
	/**
	 * 返回名称
	 * @return
	 */
	public String getMarkerName();
	/**
	 * 通过marker得到经纬度的坐标
	 * @param t
	 * @return
	 */
	public abstract LatLng getLatLng();
	/**
	 * 通过marker得到其图标
	 * @param t
	 * @return
	 */
	public abstract BitmapDescriptor getBitmapDescriptor();
}			