package com.globalhome.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.globalhome.data.App;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Utils {
	
	/**
	 * 获取SDK版本
	 */
	public static int getSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	public static void uninstallApp(Context context, String packageName) {
		Uri uri = Uri.parse(String.format("package:%s", packageName));
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(uri);
		context.startActivity(intent);
	}

	public static String getDevID() {
		String devID = getLanMac();
		if (TextUtils.isEmpty(devID)) {
			devID = getWifiMac();
		}
		return devID;
	}

	public static String getLanMac() {

		String mac = null;

		try {

			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				String name = networkInterface.getName();
				byte[] addr = networkInterface.getHardwareAddress();
				if ((addr == null) || (addr.length == 0)) {
					continue;
				}
				StringBuilder buffer = new StringBuilder();
				for (byte b : addr) {
					buffer.append(String.format("%02X:", b));
				}
				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1);
				}

				mac = buffer.toString().toLowerCase(Locale.ENGLISH);

				if (name.startsWith("eth")) {
					if (!jyMac(mac)) {
//						MLog.e(TAG, "mac format err");
						mac = "00:00:00:00:00:00";
					}
					return mac;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "");
		}

		return "00:00:00:00:00:00";
	}

	public static String getWifiMac() {

		WifiManager wifiManager = (WifiManager) AppMain.ctx().getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);

		boolean wifiInitState = wifiManager.isWifiEnabled();

		String mac = null;

		try {

			if(!wifiInitState) {
				boolean openWifi = wifiManager.setWifiEnabled(true);
//				if(openWifi) {
//					MLog.e(TAG, "open wifi OK");
//				} else {
//					MLog.e(TAG, "open wifi no OK");
//				}
			}

			for(int i = 0; i < 10; i++) {
				if(wifiManager.isWifiEnabled()) {
//					MLog.e(TAG, "wifi state OK");
					break;
				}
//				else {
//					MLog.e(TAG, "wifi state no OK");
//				}
				Thread.sleep(1000);
			}

			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				String name = networkInterface.getName();
				byte[] addr = networkInterface.getHardwareAddress();
				if ((addr == null) || (addr.length == 0)) {
					continue;
				}
				StringBuilder buffer = new StringBuilder();
				for (byte b : addr) {
					buffer.append(String.format("%02X:", b));
				}
				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1);
				}

				mac = buffer.toString().toLowerCase(Locale.ENGLISH);

				if(name.startsWith("wlan")) {
					if (!jyMac(mac)) {
						mac = "00:00:00:00:00:00";
					}
					return mac;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "");
		} finally {
			if(!wifiInitState) {
				Log.d(TAG, "wifi close");
				wifiManager.setWifiEnabled(false);
			}
		}

		return "00:00:00:00:00:00";
	}

	private static boolean jyMac(String mac) {

		if(TextUtils.isEmpty(mac)) {
			return false;
		}

		/**
		 * ^ 表示行的开始
		 * $ 表示行的结束
		 * */
		String patternMac = "^[a-f0-9]{2}(:[a-f0-9]{2}){5}$";

		return Pattern.compile(patternMac).matcher(mac).find();
	}

	public static int getVerCode() {
		try {
			return AppMain.ctx().getPackageManager().
					getPackageInfo(AppMain.ctx().getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return AppMain.ctx().getFilesDir().getAbsolutePath();
		}
	}

	/**
	 * 应用是否安装
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return packageInfo != null;
		} catch (PackageManager.NameNotFoundException e) {

		}
		return false;
	}

	//icon drawable
	public static Drawable getAppIconDrawable(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return packageInfo.applicationInfo.loadIcon(packageManager);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static App getAppInfo(Context context,String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				return new App(applicationInfo.loadLabel(packageManager).toString(),applicationInfo.loadIcon(packageManager),packageName);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取app的报名列表
	 * @param apps
	 * @return
	 */
	public static List<String> getAppPackageNames(List<App> apps) {
		List<String> pNames = new ArrayList<>();
		if (apps != null) {
			for (App app : apps) {
				if (app != null) {
					pNames.add(app.getPackageName());
				}
			}
		}
		return pNames;
	}

	public static void showToast(Object text) {
		Toast.makeText(AppMain.ctx(), "" + text, Toast.LENGTH_SHORT).show();
	}

	public static void log(Object text) {
		Log.e("zzz", ""+text);
	}
	/**
	 * 启动app
	 * @param context
	 * @param packageName
	 */
	public static void launchApp(Context context, String packageName) {

		if (TextUtils.isEmpty(packageName)) {
			return;
		}

		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent != null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			if (!Utils.isInstalled(context, packageName)) {
				Utils.showToast("应用未安装！");
			}else {
				Utils.showToast("应用启动失败！");
			}
			Log.d("tag", "launchApp---->" + packageName + "应用未安装");
		}
	}
}
