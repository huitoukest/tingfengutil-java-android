package com.tingfeng.util.java.android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog{
	public LoadingDialog dialog;
	private View dialogView;
	private int imageViewId;
	private int textViewId;
	//private Context con;

	
	public LoadingDialog(Context context,int theme,String msg,View dialogView,int imageViewId,int textViewId) {
		super(context, theme);
		this.dialogView=dialogView;
		this.imageViewId=imageViewId;
		this.textViewId=textViewId;
		//con = context;
		createDialog(msg);
	}	
	public void setDialogText(String msg){
		TextView tvMsg = (TextView)this.dialog.findViewById(imageViewId);
		tvMsg.setText(msg);
	}
	
	private LoadingDialog createDialog(String msg) {
		dialog = this;
		dialog.setContentView(dialogView);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		TextView tvMsg = (TextView) dialog.findViewById(textViewId);
		tvMsg.setText(msg);

		// 动画
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotateAnim = new RotateAnimation(1,1081,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnim.setInterpolator(new LinearInterpolator());
		rotateAnim.setDuration(5000);
		rotateAnim.setRepeatCount(-1);

		animationSet.addAnimation(rotateAnim);

		ImageView img = (ImageView) dialog.findViewById(imageViewId);
		img.startAnimation(animationSet);
	return dialog;
	}
	
}
