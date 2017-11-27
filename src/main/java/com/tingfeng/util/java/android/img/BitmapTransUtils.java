package com.tingfeng.util.java.android.img;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Base64;
/**
 * 
 * @author dview76
 * Bitmap的转换工具集合
 */
public class BitmapTransUtils {
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio>widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	
	
	//计算图片的缩放值
	public static int calculateSizeHalfOfScreen(BitmapFactory.Options options) {
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    int inSampleSize = 1;
		        return inSampleSize;
	}
	
	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int reqWidth, int reqHeight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeFile(filePath, options);
	    }
	
	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(Resources res,int id,int reqWidth, int reqHeight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeResource(res, id, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeResource(res, id, options);
	    }
	
	/**
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(InputStream is,int reqWidth, int reqHeight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        Rect rct=new Rect(0,0,400,400);
	        BitmapFactory.decodeStream(is,rct, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	   return BitmapFactory.decodeStream(is,rct, options);
	    }
	
	//把bitmap转换成BASE64类型的String
	public static String bitmapToString(String filePath) {

	        Bitmap bm = getSmallBitmap(filePath,400,400);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] b = baos.toByteArray();
	        return Base64.encodeToString(b, Base64.DEFAULT);
  }		
}
