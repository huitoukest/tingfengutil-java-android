package com.tingfeng.util.java.android.common;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ApplicationUtils {
	
	/**
	 * * 获取版本号 * @return 当前应用的版本号,默认返回0 　　
	 */
	public static String getVersion(Activity activity) {
		try {
			PackageManager manager = activity.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					activity.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
}
