package com.tingfeng.util.java.android.img;

import java.io.File;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
/**
 * 
 * @author dview76
 * 如果需要更多操作缩略图,可以用ThumbnailUtils类;
 */
public class BitmapUtils {
	public final static String TAG="BitmapUtils";
	/**
	 * 判断一张图片是否损坏
	 * 如果不是图片也会返回true;
	 */
	public static boolean isBadImage(String filePath){
		File file=new File(filePath);
		if(!file.exists()||file.length()<1)
			return true;
		try{
			BitmapFactory.Options options = null;
			if (options == null) options = new BitmapFactory.Options();
			  options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options); //filePath代表图片路径
			if (options.mCancel || options.outWidth == -1|| options.outHeight == -1) {
				//表示图片已损毁
				return true;
			}
		}catch(Exception e){
			return true;
		}
	    return false;
	}
	
	/**
	 * 根据缩略图id,获得原始缩略图路径
	 * @param image_id
	 * @return
	 */
	String getOriginalImagePath(String image_id,Context context) {
		ContentResolver cr = context.getContentResolver();
		String path = null;
		Log.i(TAG, "---(^o^)----" + image_id);
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				Media._ID + "=" + image_id, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));

		}
		return path;
	}
	
}
