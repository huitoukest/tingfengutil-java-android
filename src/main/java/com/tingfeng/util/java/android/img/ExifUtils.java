package com.tingfeng.util.java.android.img;

import java.io.File;
import java.io.IOException;

import com.tingfeng.util.java.android.common.Log;
import android.media.ExifInterface;

/**
 * 图片的Exif信息读写工具类
 * @author dview76
 *
 */
public class ExifUtils {
	/**
	 * 根据ExifInterface中提供的常量来选择提取相关的信息
	 * @param filepath
	 * @return ExifInterface
	 */
	 public static ExifInterface getExif(String filePath) {
	        ExifInterface exif = null;
	        try {
	            exif = new ExifInterface(filePath);
	        } catch (IOException ex) {
	        	Log.Error("ExifUtils_wg:getExif:error", ex.toString());
	        }
	       return exif;
	    }	 
	 public static boolean saveExif(String filePath,ExifInterface exif){		 
		 try {
			 exif.saveAttributes();
			 return true;
	        } catch (IOException ex) {
	        	Log.Error("ExifUtils_wg:saveExif:error", ex.toString());
	            return false;
	        }
	 }
	   
	  /**
	   * 将度分秒转换成为double格式
	   * @param degrees
	   * @param minutes
	   * @param seconds
	   * @return
	   */
	   public static double DMStoDouble(double degrees,double minutes,double seconds){
		   double dValue=0;
		   double mValue=0;
		   double sValue=0;
		   char[] m=(seconds+"").toCharArray();
		   double mod=60d;
		   for(int i=0;i<m.length;i++){
			   sValue+=m[i]*Math.pow(mod,-(i+1));
		   }
		   mValue=sValue%1;
		   sValue=sValue-mValue;
		   
		   m=(minutes+"").toCharArray();
		   for(int i=0;i<m.length;i++){
			   mValue+=m[i]*Math.pow(mod,-(i+1));
		   }
		   dValue=mValue%1;
		   mValue=mValue-dValue;
		   dValue=dValue+degrees;
		   dValue=Double.parseDouble(dValue+"."+mValue+sValue);
		   return dValue;
	   }
		/**
		 * 浮点型经纬度值转成度分秒格式
		 * 
		 * @param coord
		 * @return
		 */
		public static String decimalToDMS(double coord) {
			String output, degrees, minutes, seconds;
			// gets the modulus the coordinate divided by one (MOD1).
			// in other words gets all the numbers after the decimal point.
			// e.g. mod := -79.982195 % 1 == 0.982195
			//
			// next get the integer part of the coord. On other words the whole
			// number part.
			// e.g. intPart := -79
			double mod = coord % 1;
			int intPart = (int) coord;
			// set degrees to the value of intPart
			// e.g. degrees := "-79"
			degrees = String.valueOf(intPart);
			// next times the MOD1 of degrees by 60 so we can find the integer part
			// for minutes.
			// get the MOD1 of the new coord to find the numbers after the decimal
			// point.
			// e.g. coord := 0.982195 * 60 == 58.9317
			// mod := 58.9317 % 1 == 0.9317
			//
			// next get the value of the integer part of the coord.
			// e.g. intPart := 58
			coord = mod * 60;
			mod = coord % 1;
			intPart = (int) coord;
			if (intPart < 0) {
				// Convert number to positive if it's negative.
				intPart *= -1;
			}
			// set minutes to the value of intPart.
			// e.g. minutes = "58"
			minutes = String.valueOf(intPart);
			// do the same again for minutes
			// e.g. coord := 0.9317 * 60 == 55.902
			// e.g. intPart := 55
			coord = mod * 60;
			intPart = (int) coord;
			if (intPart < 0) {
				// Convert number to positive if it's negative.
				intPart *= -1;
			}
			// set seconds to the value of intPart.
			// e.g. seconds = "55"
			seconds = String.valueOf(intPart);
			// I used this format for android but you can change it
			// to return in whatever format you like
			// e.g. output = "-79/1,58/1,56/1"
			output = degrees + "/1," + minutes + "/1," + seconds + "/1";
			// Standard output of D°M′S″
			// output = degrees + "°" + minutes + "'" + seconds + "\"";
			return output;
		}

		/**
		 * 如果这个图片无经纬度信息,将经纬度信息写入JPEG图片文件里
		 * 
		 * @param picPath
		 *            JPEG图片文件路径
		 * @param dLat
		 *            纬度
		 * @param dLon
		 *            经度
		 */
		public static void writeLatLonIntoJpeg(String picPath, double dLat, double dLon) {
			File file = new File(picPath);
			if (file.exists()) {
				try {
					ExifInterface exif = new ExifInterface(picPath);
					String tagLat = exif
							.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
					String tagLon = exif
							.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
					if (tagLat == null && tagLon == null) // 无经纬度信息
					{
						exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
								decimalToDMS(dLat));
						exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,
								dLat > 0 ? "N" : "S"); // 区分南北半球
						exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
								decimalToDMS(dLon));
						exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,
								dLon > 0 ? "E" : "W"); // 区分东经西经

						exif.saveAttributes();
					}
				} catch (Exception e) {
					Log.Error("ExifUtils_wg:writeLatLonIntoJpeg:error", e.toString());
				}
			}
		}
}
