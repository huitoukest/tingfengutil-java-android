package com.tingfeng.util.java.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * 过渡activity,比如启动界面等等
 * @author dview76
 *
 */
@SuppressWarnings("static-access")
public class TransitionActivity extends InstrumentedActivity{ 
   /**
    * //停留时间,单位毫秒
    */
	public final int delayTime=100;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPool.getInstance().addActivity(this);
       /* try {
			//StartMainActivity(cls);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
        initData();
    }
	/**
	 * 初始化一些系统的信息
	 */
	public void initData(){
		
	}
	private void StartMainActivity(final Class<? extends Activity> cls) throws InterruptedException{
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent	intent = new Intent(TransitionActivity.this,cls);
				startActivity(intent);
				TransitionActivity.this.finish();			
			}
		}, delayTime);//停留时间
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}
}
