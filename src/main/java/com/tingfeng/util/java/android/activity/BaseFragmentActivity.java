package com.tingfeng.util.java.android.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.tingfeng.util.java.android.common.ScreenSizeUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint({ "CommitTransaction", "InflateParams", "NewApi" })
public abstract class BaseFragmentActivity extends android.support.v4.app.FragmentActivity{
/**
 * 当前的界面是哪一个,包括内容界面/侧边栏/底部菜单三个部分
 */
private RelativeLayout  currentView;
private RelativeLayout.LayoutParams  currentParams;//内容项目的参数contentLayout的宽度值  

private View contentView;
private RelativeLayout.LayoutParams  contentParams;

/**
 * 侧边栏
 */
private RelativeLayout sideBarView;
private RelativeLayout.LayoutParams  sideBarParams;//菜单项目的参数

/**
 * 内容view的遮罩层,这样在百度mapview之前获取到触摸事件
 */
private RelativeLayout content_overLayView;
private RelativeLayout.LayoutParams  content_overLayParams;

/**
 * 底部菜单
 */
private RelativeLayout buttomView;
private RelativeLayout.LayoutParams buttomMenueParams;
/**
 *屏幕信息 
 */
private DisplayMetrics dm;
/**
 * 手指点下去的横坐标 
 */
private float xDown;
/**
 * 手指移动的横坐标 
 */
private float xMove;
/**
 * 记录手指上抬后的横坐标 
 */
private float xUp;

/**
 * 显示或者隐藏侧边栏的时候,需要达到的
 * 每秒滑过屏幕的百分之比snapSpeed,速度;
 */
private static final float snapSpeed=75f;
/**
 * menu完成显示,留给content的宽度,百分比
 */
private static final float sideMargin=0.3f;
/**
 * 当前是否按下
 */
private boolean isXDown=false;
/**
 * 当前是否抬起手指
 */
private boolean isXUp=false;
/**
 * 当前是否在移动
 */
private boolean isXMove=false;
/**
 * 手指按下的时间
 */
private Long XDownTime=0L;
/**
 * 手指抬起的时间
 */
private Long XUpTime=Long.MAX_VALUE;
/**
 * 手指在屏幕移动的时间
 */
private Long xMoveTime=Long.MAX_VALUE;
/**
 * 当前侧边栏的状态,false表示显示,true表示影藏
 */
private Boolean isSideBarinHide=true;
/**
 * 是否触发了侧边栏事件
 */
private boolean isTouchOffSideBar=false;
/**
 * 主要是用来恢复触摸拦截的触摸事件的作用
 */
private List<MotionEvent> motionEventList;
/**
 * 一次触摸事件是否结束
 */
private boolean isATouchActionCanceled=false;

/**
 * 点击返回按钮的时候,需要返回的activity;
 */
private Class<? extends Activity> backActivity=null;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityPool.getInstance().addActivity(this);	
	}
	
	
	
	@Override
	public View findViewById(int id) {	
		return super.findViewById(id);
		//return currentView.findViewById(id);
	}



	@Override
	protected void onStart() {
		super.onStart();	
	}


	/**
	 * 初始化侧边栏和底部菜单
	 */
	@SuppressWarnings("unused")
	protected final void initSideBarAndBottomMenue(){
        /* if(showSideBar())
		 sideBarView=(RelativeLayout) getLayoutInflater().inflate(R.layout.layout_sidebar,null);
		if(showBottomMenue()) 
         buttomView=(RelativeLayout) getLayoutInflater().inflate(R.layout.layout_bottom_menue,null);*/
	}
	
	@Override
	public void setContentView(int layoutResID) {		
		contentView =getLayoutInflater().inflate(layoutResID,null);	
		initLayoutParams(); 
		super.setContentView(currentView);
		setOnclickListenerOfSiderBarAndBottomMenue();
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		contentView=view;
		//contentView=getLayoutInflater().inflate(view.getId(),null);
		contentView.setLayoutParams(params);
		/*super.setContentView(R.layout.fragment_bottomsidebarmenu);
		initFragment();				
		fragmentManager.beginTransaction();
		transaction.replace(view.getId(), contentFragment);
		transaction.commit();*/
		 initLayoutParams(); 
		 super.setContentView(currentView);
		 setOnclickListenerOfSiderBarAndBottomMenue();
	}

	@Override
	public void setContentView(View view) {				
		contentView=view;
		//contentView=getLayoutInflater().inflate(view.getId(),null);
		/*super.setContentView(R.layout.fragment_bottomsidebarmenu);
		initFragment();
		fragmentManager.beginTransaction();
		transaction.replace(view.getId(), contentFragment);
		transaction.commit();
		*/
		 initLayoutParams(); 
		 super.setContentView(currentView);
		 setOnclickListenerOfSiderBarAndBottomMenue();
	}	
	
    /**
     * 为侧边栏和底部菜单设置相应的监听
     */
	@SuppressWarnings("unused")
	private void setOnclickListenerOfSiderBarAndBottomMenue(){
	}
	
	 /** 
	  *初始化Layout并设置其相应的参数 
	  */
	  private void initLayoutParams() 
	  { 
		  initSideBarAndBottomMenue();
	      
		  currentView=new RelativeLayout(this);
		  //得到屏幕的大小  
		  dm =ScreenSizeUtils.init(this);
	      
	      currentParams =new android.widget.RelativeLayout.LayoutParams(dm.widthPixels,dm.heightPixels);
	      currentView.setLayoutParams(currentParams);	      
	      currentParams.topMargin=0;
	      currentParams.leftMargin=0;	
	      
	      contentParams=currentParams;
	      contentView.setLayoutParams(contentParams);
	      currentView.addView(contentView);
	      
	      if(buttomView!=null){
	    	  buttomMenueParams=new android.widget.RelativeLayout.LayoutParams(dm.widthPixels,dm.heightPixels);
	    	  buttomMenueParams.topMargin=0;
	    	  buttomMenueParams.leftMargin=0;
	    	  buttomView.setLayoutParams(buttomMenueParams);
		      currentView.addView(buttomView);  		      
	      }	      	      	      
	      if(sideBarView!=null){
	    	  sideBarParams=new android.widget.RelativeLayout.LayoutParams(dm.widthPixels,dm.heightPixels);
	    	  sideBarParams.topMargin=0;
	    	  sideBarParams.leftMargin=0-dm.widthPixels;
	    	  sideBarView.setLayoutParams(sideBarParams);
	      	  currentView.addView(sideBarView);
	          
	      	  content_overLayView=new RelativeLayout(this);
	      	  content_overLayParams=new android.widget.RelativeLayout.LayoutParams(dm.widthPixels,dm.heightPixels);
	      	  content_overLayParams.topMargin=0;
	      	  content_overLayParams.leftMargin=0;
	    	  content_overLayView.setLayoutParams(content_overLayParams);
	          currentView.addView(content_overLayView);
	      }      	      	      	      
	      //初始化菜单和内容的宽和边距 
	      //sideBarParams.width = disPlayWidth - menuPadding; 
	      //sideBarParams.leftMargin = 0 - sideBarParams.width; 
	      //currentParams.width = disPlayWidth; 
	      //currentParams.leftMargin=0; 
	        
	      //设置参数 
	      //sideBarView.setLayoutParams(sideBarParams); 
	      //contentLayout.setLayoutParams(currentParams);
	      setOnTouchListener();
	  }
  /**
   * 是否需要底部菜单按钮功能
   * @return
   */
	public abstract boolean showBottomMenue();
	/**
	 * 是否需要侧边栏功能
	 * @return
	 */
	public abstract boolean showSideBar(); 
	/**
	 * 给当前界面设置触摸监听
	 */
	public void setOnTouchListener(){
		if(content_overLayView!=null)
		content_overLayView.setOnTouchListener(new View.OnTouchListener() {
			/**
			 * return false表示此事件会回传到父控件进行处理
			 * return true表示已经处理此事
			 */
			@Override
			public boolean onTouch(View view, MotionEvent event) {
			      switch (event.getAction()) 
			      { //在第一个点被按下时触发
			      case MotionEvent.ACTION_DOWN: 
			    	  xMove=xDown=event.getRawX();
			          isXDown=true;
			          isXUp=false;
			          xUp=0;
			          XDownTime=System.currentTimeMillis();	
			          break;			    
			          //当有点在屏幕上移动时触发。
			      case MotionEvent.ACTION_MOVE: 
			          xMove=event.getRawX();    
			          isXMove=true;
			          xMoveTime=System.currentTimeMillis();
			          break; 
			            //当屏幕上唯一的点被放开时触发
			      case MotionEvent.ACTION_UP: 
			          xUp=event.getRawX();
			          isXUp=true;
			          isXDown=false;
			          xDown=0;
			          xMove=0;
			          XUpTime=System.currentTimeMillis();
			          break; 			            
			      case MotionEvent.ACTION_CANCEL:
			    	  
			           break;  
			      }			 
			      if(!showSideBar())
			  		return false;	
			      return updateScreentView(event);
			      //return true; 
			}
		});
	}
	
	/**
	 * 更新界面,在触摸之后
	 */
	private synchronized boolean updateScreentView(MotionEvent event){
	/*if(motionEventList!=null)
      {
   	    motionEventList.add(event);
        if((-XDownTime+xMoveTime>300||XUpTime-XDownTime>300)&&isSideBarinHide==true&&isTouchOffSideBar==false){
    	 //传递触摸信息;
        	for(MotionEvent event2:motionEventList)
		     {
		    	 contentView.dispatchTouchEvent(event2);
		    	 if(Constants.map!=null)
				      Constants.map.dispatchTouchEvent(event);
		     }	     
        }
        motionEventList=null;
      }*/
      if(isScrollToShowMenu()){
		 new ShowSideBarAsyncTask(300L,(int)(dm.widthPixels*(1-sideMargin))).execute(0);
		 //isSideBarinHide=false;
		  isTouchOffSideBar=true;
		}else if(isScrollToHideMenue()){
		new ShowSideBarAsyncTask(300L,-(int)(dm.widthPixels*(1-sideMargin))).execute(0);
		//isSideBarinHide=true;
		  isTouchOffSideBar=true;
		}
    if(isTouchOffSideBar==true||isSideBarinHide==false||(xMoveTime-XDownTime<350&&xMoveTime-XDownTime>0)||xDown<dm.widthPixels*0.05)
    	return true;
	return false;
	}	
	 /** 
	  * 根据手指按下的距离，判断是否滚动显示菜单 
	  */
	  private boolean isScrollToShowMenu()
	  { 
		  if(isSideBarinHide&&isXDown==true&&isXMove==true)
		  {  boolean isStart=xDown>0.02*dm.widthPixels&&xDown<0.5*dm.widthPixels;
			 boolean isEnd=xMove>0.20*dm.widthPixels&&xMove<0.9*dm.widthPixels;  
			 boolean isDistanceOk=xMove-xDown>0.1*dm.widthPixels;//是否滑过10%的距离 
			 if(isDistanceOk&&isStart&&isEnd&&getSpeedOfSnapWidth()>=snapSpeed)
		    	  return true;
		  }
	        return false;
	  }
	  /**
	   * 当前是否滑动去隐藏菜单
	   * @return
	   */
	  private boolean isScrollToHideMenue(){		  
		  if(!isSideBarinHide&&isXDown==true&&isXMove==true)
		  {  boolean isStart=xDown>0.25*dm.widthPixels&&xDown<0.9*dm.widthPixels;
			 boolean isEnd=xMove>0.02*dm.widthPixels&&xMove<0.35*dm.widthPixels;
			 boolean isDistanceOk=xDown-xMove>0.1*dm.widthPixels;//是否滑过10%的距离
		      if(isDistanceOk&&isStart&&isEnd&&-getSpeedOfSnapWidth()>=snapSpeed)
		    	  return true;
		  }
		  return false;
	  }
	  
	  /**
	   * 手指滑动的速度,向右为正,向左为负数,
	   * 以手机屏幕的宽度为准,返回器百分比值
	   * 每秒,百分之多少X屏幕的宽度
	   * @return
	   */
	  private double getSpeedOfSnapWidth(){
		  double speedSnap=0d;
		  if(isXDown==true&&isXMove==true){
			 speedSnap=1000*(xMove-xDown)/(xMoveTime-XDownTime)*100;
			 speedSnap/=dm.widthPixels;
		  }
		  return speedSnap;
	  }
	  /** 
	  *指针按着时，滚动将菜单慢慢显示出来 
	  *@param scrollX 每次滚动移动的距离 
	  */
	  private void scrollToShowMenu(int scrollX) 
	  { 
	      if(scrollX>0&&scrollX<= currentParams.width) 
	      currentParams.leftMargin =-currentParams.width+scrollX; 
	      sideBarView.setLayoutParams(currentParams);  
	  } 
	  /**
	  *指针按着时，滚动将菜单慢慢隐藏出来 
	  *@param scrollX 每次滚动移动的距离 
	  */
	  private void scrollToHideMenu(int scrollX) 
	  { 
	      if(scrollX>=-currentParams.width&&scrollX<0) 
	      currentParams.leftMargin=scrollX; 
	      sideBarView.setLayoutParams(currentParams);  
	  } 	    
	    
	  /** 
	  * 
	  *：模拟动画过程，让肉眼能看到滚动的效果 
	  * 
	  */
	  class ShowSideBarAsyncTask extends AsyncTask<Integer, Integer, Integer> 
	  {   /**
	       *动画播放总时间
	       **/
		  private Long useTime;
	     /**
	      * 移动的总像素
	      */
		  private int movePx;
		  private final int sleepTime=30;
		  /**
		   * 每一次移动的像素
		   */
		  private int moveXPerTime;
		  /**
		   * 
		   * @param useTime 动画播放总时间
		   * @param movePx 移动的总像素,可以是正,可以是负数
		   */
		  public ShowSideBarAsyncTask(Long useTime,int movePx) {
	         this.useTime=useTime;
	         this.movePx=movePx;
	         moveXPerTime=(int) (30*movePx/useTime);
	  	  }  
	      @Override
	      protected Integer doInBackground(Integer... params) 
	      { 
	          int leftMargin = sideBarParams.leftMargin; 
	          
	          for(int i=0;i<useTime/30;i++){
	        	  
	        	  if(movePx>0){//如果是滑出
	        		  if(leftMargin+movePx>=0||leftMargin+movePx>-sideMargin*dm.widthPixels)
	        		  {
	        			  leftMargin=(int) (-sideMargin*dm.widthPixels);
	        			  break;
	        		  }else{
	        			  leftMargin+=movePx;
	        		  }
	        	  }else{
	        		  if(leftMargin+movePx<=dm.widthPixels)
	        		  {
	        			  leftMargin=-dm.widthPixels;
	        			  break;
	        		  }
	        	  }
	        	  publishProgress(leftMargin); 
	              try
	              { 
	                  Thread.sleep(sleepTime);//休眠一下，肉眼才能看到滚动效果 
	              } catch (InterruptedException e) 
	              { 
	                  e.printStackTrace(); 
	              } 
	          }//end for	          	       
	         return leftMargin; 
	      } 
	      @Override
	      protected void onProgressUpdate(Integer... value) 
	      { 
	          sideBarParams.leftMargin=value[0];	          
	          //sideBarParams.setMargins(0, 0, value[0], 0);
	          sideBarView.setLayoutParams(sideBarParams);
    	      currentView.updateViewLayout(sideBarView,sideBarParams);
	      }
	  
	      @Override
	      protected void onPostExecute(Integer result) 
	      { 
	    	  sideBarParams.leftMargin=result;	 
	    	  //sideBarParams.setMargins(0, 0, result, 0);
	      		//params.setMarginEnd(result);
	          sideBarView.setLayoutParams(sideBarParams);
	          currentView.updateViewLayout(sideBarView, sideBarParams);
	          if(movePx>0){
	        	  isSideBarinHide=false;
	          }else if(movePx<0){
	        	  isSideBarinHide=true;
	          }
	          isTouchOffSideBar=false;
	      }
	  
	  } 
	
    
   
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 返回true表示双击退出;
	 * @return
	 */
	public abstract boolean isExitBy2Click();
	/**
	 * 设置点击返回按钮后跳转的activity
	 * @param activity
	 */
	public void setBackActivity(Class<? extends Activity> activity){
		this.backActivity=activity;
	}
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	 if(isExitBy2Click()&&keyCode == KeyEvent.KEYCODE_BACK)
	 { 
	  exitBy2Click(); //调用双击退出函数
	  return true;
	 }else if(this.backActivity!=null){
		 Intent intent=new Intent(this,this.backActivity);
		 startActivity(intent);
		 return true;
	 }
	 return super.onKeyDown(keyCode, event);
	}
	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;	 
	private void exitBy2Click() {
	 Timer tExit = null;
	 if (isExit == false) {
	 isExit = true; // 准备退出
	 Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	 tExit = new Timer();
	 tExit.schedule(new TimerTask() {
	  @Override
	  public void run() {
	  isExit = false; // 取消退出
	  }
	 }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
	 
	 } else {
	  ActivityPool.getInstance().exit();
      //finish();
	 //System.exit(0);
	 }
	}

	/**
	 * 显示长提醒信息,会自动运行在ui线程
	 * @param msg
	 */
	public final void ShowToast(final String msg) {		
		this.ShowToast(msg, Toast.LENGTH_LONG);
	}
	
	/**
	 * 
	 * @param manage
	 * @param duration 1表示长提示,0表示短提示,会自动运行在ui线程
	 */
	public final void ShowToast(final String msg,final int duration) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {	
		Toast toast = Toast.makeText(getApplicationContext(), msg,
					duration);
			toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
			toast.show();
			}
			}
		);
		}
}
