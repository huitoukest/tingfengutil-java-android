package com.tingfeng.util.java.android.baiduMap;

import java.util.List;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 在地图上画出闭合的图形,/点、线、多边形、圆、文字等等
 * @author dview76
 *
 */
public class MyOverDelay{

	public BaiduMap mBaiduMap=null;
	
	public MyOverDelay(BaiduMap mBaiduMap){
		this.mBaiduMap=mBaiduMap;
	}
	/**
	 * 向地图中添加点
	 * @param points
	 * @param color
	 */
	public void drawCustomPoint(List<LatLng> points,int color,int width){
		OverlayOptions ooPolyline = new PolylineOptions().width(width)
				.color(color).points(points);
		mBaiduMap.addOverlay(ooPolyline);
	}
	/**
	 * 向地图中添加弧线
	 * @param points points中只能包含三个点;
	 * @param color
	 * @param width 线条的宽度
	 */
	public void drawCustomArc(List<LatLng> points,int color,int width){
		// 添加弧线
				OverlayOptions ooArc = new ArcOptions().color(color).width(width)
						.points(points.get(0),points.get(1),points.get(2));
				mBaiduMap.addOverlay(ooArc);
	}
	
	public void drawCustomCircle(List<LatLng> points,int color){
		
	}
	/**
	 * 通过中心点和半径来画圆
	 * @param centerPoint
	 * @param radius
	 * @param color
	 * @stroke stroke 边框,由宽度和颜色可以构建一个边框
	 */
	public void drawCustomCircle(LatLng centerPoint,int radius,int color,Stroke stroke){
		OverlayOptions ooCircle = new CircleOptions().fillColor(color)
				.center(centerPoint).stroke(stroke)
				.radius(radius);
		mBaiduMap.addOverlay(ooCircle);		
	}
	
	/**
	 * 通过中心点和半径来圆形的点
	 * @param centerPoint
	 * @param radius
	 * @param color
	 */
	public void drawCustomDot(LatLng centerPoint,int radius,int color){	
		OverlayOptions ooDot = new DotOptions().center(centerPoint).radius(6)
				.color(color);
		mBaiduMap.addOverlay(ooDot);
		
	}
	/**
	 * 画一个多边形
	 * @param points 点的集合
	 * @param color 背景颜色
	 * @stroke stroke 边框,由宽度和颜色可以构建一个边框
	 */
	public void drawCustomPolygon(List<LatLng> points,int color,Stroke stroke){
		OverlayOptions ooPolygon = new PolygonOptions().points(points).stroke(stroke).fillColor(color);
		mBaiduMap.addOverlay(ooPolygon);
	}
	/**
	 * 
	 * @param point 文字坐标
	 * @param text 文字
	 * @param fontSize 字体大小
	 * @param fontColor 紫图颜色
	 * @param bgColor 背景颜色
	 * @param rotate 旋转角度,-360~360;
	 */
	public void drawCustomText(LatLng point,String text,int fontSize,int fontColor,int bgColor,int rotate){
		// 添加文字
				OverlayOptions ooText = new TextOptions().bgColor(bgColor)
						.fontSize(fontSize).fontColor(fontColor).text(text).rotate(rotate)
						.position(point);
				mBaiduMap.addOverlay(ooText);
	}
}
