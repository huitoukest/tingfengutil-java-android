package com.tingfeng.util.java.android.baiduMap;

import java.util.ArrayList;
import java.util.List;

import com.tingfeng.util.java.android.activity.BaseFragmentActivity;
import com.tingfeng.util.java.android.listener.MyOrientationListener;
import com.tingfeng.util.java.android.listener.MyOrientationListener.OnOrientationListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
/**
 * 百度地图的超类,又有一些公用的属性和设置,
 * @author dview76
 *
 */
public abstract class BaseBaiduMapActivity extends BaseFragmentActivity{
		/**
		 * 关于定位的参数
		 */
		private LocationClient mLocClient;
		
		public MyLocationListenner myListener = new MyLocationListenner();
		/**
		 * 当前定位的模式
		 */
		private LocationMode locationMode;
		/**
		 * 地图View
		 */
		private MapView mMapView = null;
		/**
		 * 地图mapView的id编号
		 */
		private int mapViewId=-1;
		/**
		 * 地图对象
		 */
		private BaiduMap baidu_map = null;
		/**
		 * 地图的设置
		 */
		private UiSettings mUiSettings;
		
		//private Button requestLocButton;
		//private EditText Edit_address;
		//private ImageView addressQuery;

		//private List<Reserve_Manage> reserve_ManageList;
		/**
		 * 方向传感器监听类
		 */
		private  MyOrientationListener myOrientationListener;
		/**
		 * 当前机头的方向信息
		 */
		private int mXDirection;
		/**百度的当前定位参数
		 */
		private BDLocation bdLocation;
		/**
		 * 是否开启罗盘
		 */
		boolean compassEnabled=true;
		/**
		 * 是否隐藏缩放空间
		 */
		boolean hideZoomControls=true;
		/**
		 * 是否隐藏比例尺
		 */
		boolean hidescale=true;
		/**
		 * 地图当前的缩放比例
		 */
		float zoomValue=8.0f;
		/**
		 * 如果是首次定位成功,是否将之移动到首次定位成功的位置
		 */
		private boolean ifFirstMoveToNowLocation=false;
		/**
		 * 是否开启定位以及定位图层等
		 */
		private boolean isGetLocation=true;
		/**
		 * 定位的间隔毫秒数,默认2000,即2秒
		 */
		public int spaceOfGetLocation=1000;		
		/**
		 * 用来在activity destroy的时候,回收 bitmap资源
		 */
		public List<BitmapDescriptor> baseMap_BitmapDescriptorList;
		/**
		 * 
		 * @param mapViewId 百度mapView的id
		 * @param zoomValue 加载地图之后的缩放比例,默认8.0f
		 * @param isGetLocation 是否开启定位图层
		 * @param ifFirstMoveToNowLocation 首次定位成功是否移动到其位置
		 * @param compassEnabled 是否隐藏罗盘
		 * @param mode 定位的模式com.baidu.mapapi.map.MyLocationConfiguration.LocationMode
		 * @param hideZoomControls 是否隐藏缩放控件
		 * @param hideScale 是否隐藏比例尺
		 */
		public BaseBaiduMapActivity(int mapViewId,float zoomValue,boolean isGetLocation,boolean ifFirstMoveToNowLocation,boolean compassEnabled,LocationMode mode,boolean hideZoomControls,boolean hideScale){
			this.mapViewId=mapViewId;
			this.zoomValue=zoomValue;
			this.compassEnabled=compassEnabled;
			this.locationMode=mode;
			this.hidescale=hideScale;
			this.hidescale=hideScale;
			this.isGetLocation=isGetLocation;
			this.ifFirstMoveToNowLocation=ifFirstMoveToNowLocation;
			baseMap_BitmapDescriptorList=new ArrayList<BitmapDescriptor>();
		}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);	
			// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
			// 注意该方法要再setContentView方法之前实现
			SDKInitializer.initialize(getApplicationContext());			
		}						
		@Override
		public void setContentView(int layoutResID) {
			super.setContentView(layoutResID);
			initBaiduMap();	
		}
		@Override
		public void setContentView(View view, LayoutParams params) {
			super.setContentView(view, params);
			initBaiduMap();	
		}
		@Override
		public void setContentView(View view) {
			super.setContentView(view);
			initBaiduMap();	
		}
		/**
		 * 在activity执行onDestory方法的时候是否调用马屁View的onDestory方法;
		 * @return
		 */
		public abstract boolean isDestoryMapViewWhileDestroy();
		/**
		 * 自己手动设置百度地图的marker监听器
		 * @param baiduMap
		 */
		public abstract void setOnMarkerClickListener(BaiduMap baiduMap);	
		/**
		 * 手动设置地图的点击监听
		 */
		public abstract void setOnMapClickListener(BaiduMap baiduMap);
		
		/**
		 * 对百度地图初始化,在初始化一些界面之后
		 */
		public final void initBaiduMap(){
			this.setMapViewAndMap(getMapViewId());
			this.setLocationMode(getLocationMode());
			this.setCompass(isCompassEnabled());
			this.setZoom(isHideZoomControls());
			this.setScale(isHidescale());
			this.setZoomTo(8f);
			initMyLocation();
			initOritationListener();			
			setOnMarkerClickListener(getBaidumap());
			setOnMapClickListener(getBaidumap());
		}
		/**
		 * 获取设置地图控件
		 * @param mapViewId
		 */
		public void setMapViewAndMap(int map_ViewId){			
		   mMapView = (MapView) findViewById(map_ViewId);
		   baidu_map = mMapView.getMap();
		   MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(this.zoomValue);
		   baidu_map.setMapStatus(msu);
		   /*Constants.map = mMapView;*/
		   this.mapViewId=map_ViewId;
		}
		/**
		 * 设置指南针
		 * @param enAbled
		 */
		public void setCompass(boolean enAbled){
			mUiSettings = baidu_map.getUiSettings();
			mUiSettings.setCompassEnabled(enAbled);//开启指南针
			this.compassEnabled=enAbled;
		}
		/**
		 * 设置当前的定位模式,// normal普通 FOLLOWING 跟随 
		 * 此方法同时会调用地图来实现此设置
		 * @param mo
		 */
		public void setLocationMode(LocationMode mo){
			locationMode = mo;
			baidu_map.setMyLocationConfigeration(new MyLocationConfiguration(locationMode, true, null));
		}
		/**
		 * 设置是否隐藏缩放控件
		 * @param isHide
		 */
		public void setZoom(boolean isHide){
			int childCount = mMapView.getChildCount();
			View zoom = null;
			for (int i = 0; i < childCount; i++) {
				View child = mMapView.getChildAt(i);
				if (child instanceof ZoomControls) {
					zoom = child;
					break;
				}
			}
			if(isHide)
			zoom.setVisibility(View.GONE);
			else zoom.setVisibility(View.VISIBLE);
			this.hideZoomControls=isHide;
		}
		/**
		 * 设置是否隐藏比例尺
		 * @param isHide
		 */
		public void setScale(boolean isHide){
			int count = mMapView.getChildCount();
			View scale = null;
			for (int i = 0; i < count; i++) {
				View child = mMapView.getChildAt(i);
				if (child instanceof ZoomControls) {
					scale = child;
					break;
				}
			}
			if(isHide)
			scale.setVisibility(View.GONE);
			else scale.setVisibility(View.VISIBLE);
			this.hidescale=isHide;
		}		
		/**
		 * 设置到当前的缩放比例,默认8
		 * @param f
		 */
		public void setZoomTo(float f){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(f);
			baidu_map.setMapStatus(msu);
			this.zoomValue=f;
		}								
		/** 
	     * 初始化定位相关代码 
	     */  
	    private void initMyLocation()
	    {     
	        // 开启定位图层
			baidu_map.setMyLocationEnabled(isGetLocation);				
			if(!isGetLocation)
			return;
	        //定位初始化
			mLocClient = new LocationClient(getApplicationContext());
			if(mLocClient!=null)
			{
				mLocClient.registerLocationListener(myListener);			
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);// 打开gps;
				option.setCoorType("bd09ll"); // 设置坐标类型;
				option.setScanSpan(spaceOfGetLocation);//每秒钟定位一次;
				option.setIsNeedAddress(true);// 返回的定位结果包含地址信息;
				option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
				mLocClient.setLocOption(option);
				mLocClient.start();
			}
	    }  
	    
	    /** 
	     * 初始化方向传感器 
	     */  
	    private void initOritationListener()  
	    {  
	        myOrientationListener = new MyOrientationListener(  
	                getApplicationContext());  
	        myOrientationListener.setOnOrientationListener(new OnOrientationListener()  
	                {  
	                    @Override  
	                    public void onOrientationChanged(float x)  
	                    {  
	                    	if(bdLocation==null)
	                    		return;
	                        mXDirection = (int)x;  
	                         // 构造定位数据  
	                        MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())  
	                                // 此处设置开发者获取到的方向信息，顺时针0-360  
	                                .direction(mXDirection)  
	                                .latitude(bdLocation.getLatitude())  
	                                .longitude(bdLocation.getLongitude()).build();
	                        // 设置定位数据  
	                        baidu_map.setMyLocationData(locData);  
	                        // 设置自定义图标  
	                        /*BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory  
	                                .fromResource(R.drawable.navi_map_gps_locked); */ 
	                        MyLocationConfiguration config = new MyLocationConfiguration(  
	                                locationMode, true, null);  
	                        baidu_map.setMyLocationConfigeration(config);   
	                    }  
	                });  
	    }  
		/**
		 * 将地图移动到指定的位置
		 * @param latLng 一个经纬度坐标
		 */
		public void moveMapTOLatLang(LatLng latLng){
			 MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);  
		     baidu_map.setMapStatus(u);
		}			
		/**
		 * 设置遮罩层,如有需要,可自定调用Baidumap.clear()再设置遮罩层; 
		 * @return 返回最后一个遮罩marker的坐标
		 * @param keyName 装入Bundle的键值对中的key的名称,其中值就是marker对象
		 * @param markerList 需要遮罩的数据列表,会以markInfo的变量名称保存到报毒地图中;
		 * @param zIndex 推荐5
		 */
		public final LatLng setOverLay(List<? extends BaseMarker> markerList,int zIndex,String keyName){			 
	        OverlayOptions overlayOptions = null;  
	        LatLng latLng=null;
	        Marker marker = null;
			 for(BaseMarker info : markerList)  
		        {   latLng=info.getLatLng();
		            BitmapDescriptor bd=info.getBitmapDescriptor();
		        	baseMap_BitmapDescriptorList.add(bd);
				    overlayOptions = new MarkerOptions().position(latLng)  
		                    .icon(bd).zIndex(zIndex);  
		            marker = (Marker) (baidu_map.addOverlay(overlayOptions));  
		            Bundle bundle = new Bundle();  
		            bundle.putSerializable(keyName, info);  
		            marker.setExtraInfo(bundle);  
		        }  
		     return latLng;
		}
		
		/**
		 * 停止定位和传感器的监听,手动调节更加省电
		 */
		public void stopLocationAndOrientationListener(){
			// 关闭图层定位  
	        baidu_map.setMyLocationEnabled(false);  
	        if(mLocClient!=null&&mLocClient!=null)
	        mLocClient.stop();
	        // 关闭方向传感器  
	        if(myOrientationListener!=null)
	        myOrientationListener.stop();  
		}
		/**
		 * 开始定位和传感器的监听,手动调节更加省电
		 */
		public void startLocationAndOrientationListener(){
			// 开启图层定位  
	        baidu_map.setMyLocationEnabled(isGetLocation);  
	        if (mLocClient!=null&&!mLocClient.isStarted())  
	        {  
	            mLocClient.start();  
	        }
	        if(isGetLocation&&myOrientationListener!=null)
	        // 开启方向传感器  
	        myOrientationListener.start();
		}
	
		@Override
		protected void onPause() {
			try{
				if(mMapView!=null)
				mMapView.onPause();
				stopLocationAndOrientationListener();
				super.onPause();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		protected void onResume() {
			try{
				if(mMapView!=null)
				mMapView.onResume();
				startLocationAndOrientationListener();
				super.onResume();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		protected void onDestroy() {
			try{
				// 退出时销毁定位
				if(mLocClient!=null)
				mLocClient.stop();
				// 关闭定位图层
				baidu_map.setMyLocationEnabled(false);
				if(isDestoryMapViewWhileDestroy()){
					if(mMapView!=null)
					mMapView.onDestroy();
					mMapView = null;
				}
				super.onDestroy();
				/**
				 * 回收 bitmap 资源
				 */
				if(baseMap_BitmapDescriptorList.size()>0){
					for(BitmapDescriptor bd:baseMap_BitmapDescriptorList){
						if(bd!=null) bd.recycle();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
			
		@Override  
	    protected void onStart()  
	    {  			
	        super.onStart();  
	    } 	
		
	    @Override  
	    protected void onStop()  
	    {  
	        super.onStop();  
	    }  			    				
		/**
		 * 定位SDK监听函数
		 */
		public class MyLocationListenner implements BDLocationListener {
			@Override
			public void onReceiveLocation(BDLocation location) {
				bdLocation=location;
				LatLng ll = null ;				
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null)
					return;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(mXDirection).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();				
				if(getLocationMode().equals(LocationMode.FOLLOWING))
				baidu_map.setMyLocationData(locData);
				if (ifFirstMoveToNowLocation) {
					ifFirstMoveToNowLocation = false;
					ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapUtils mapUtile = new MapUtils();
					mapUtile.getAddress(ll);
/*					Constants.latLng = ll;*/
					moveMapTOLatLang(ll);
				}
				/*if(ll!=null)
				Constants.latLng = ll;*/
			}

			public void onReceivePoi(BDLocation poiLocation) {
			}
		}		
		
		public LocationClient getmLocClient() {
			return mLocClient;
		}
		public MyLocationListenner getMyListener() {
			return myListener;
		}
		public MapView getMapView() {
			return mMapView;
		}
		public int getMapViewId() {
			return mapViewId;
		}
		public BaiduMap getBaidumap() {
			return baidu_map;
		}
		public void setBaidumap(BaiduMap baidu_map) {
			this.baidu_map = baidu_map;
		}
		public BDLocation getBdLocation() {
			return bdLocation;
		}
		public boolean isCompassEnabled() {
			return compassEnabled;
		}
		public boolean isHideZoomControls() {
			return hideZoomControls;
		}
		public boolean isHidescale() {
			return hidescale;
		}
		public float getZoomValue() {
			return zoomValue;
		}
		public boolean isIfFirstMoveToNowLocation() {
			return ifFirstMoveToNowLocation;
		}
		public boolean isGetLocation() {
			return isGetLocation;
		}
		public int getSpaceOfGetLocation() {
			return spaceOfGetLocation;
		}
		public LocationMode getLocationMode() {
			return locationMode;
		}				
		
}
