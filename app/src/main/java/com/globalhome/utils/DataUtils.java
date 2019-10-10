package com.globalhome.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.globalhome.data.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author: ztz
 * date: 2019/9/30 12:37
 * description:
 */
public class DataUtils {
    /**
     * 获取可以打开的app列表
     *
     * @param context
     * @return
     */
    public static List<App> getValidAppList(Context context) {
        return getExcludeAppList(context, null);
    }

    /**
     * 获取可以打开的app列表,排除某些app
     *
     * @param context
     * @return
     */
    public static List<App> getExcludeAppList(Context context, List<String> excludePackageNameList) {
        List<App> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // 查询所有已经安装的应用程序,某些应用无法打开，需要排除
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages = new HashSet();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }
        for (ApplicationInfo app : applicationInfos) {
            if (allowPackages.contains(app.packageName)) {
                //可以打开的有效app
                if (excludePackageNameList != null) {
                    if (!excludePackageNameList.contains(app.packageName)) {
                        appInfos.add(new App(app.loadLabel(pm).toString(), app.loadIcon(pm), app.packageName));
                    }//else需要额外排除的
                } else {
                    appInfos.add(new App(app.loadLabel(pm).toString(), app.loadIcon(pm), app.packageName));
                }
            }
        }
        return appInfos;
    }

    public static List<App> getVideoApp() {
        return getSaveApp("my_video_apps", "com.google.android.videos");
    }


    public static List<App> getRecommendApp() {
        List<App> myRecommendApps = getSaveApp("my_recommend_apps", "com.android.gallery3d", "com.android.settings");
        //去掉加号
        myRecommendApps.remove(null);
        return myRecommendApps;
    }


    public static List<App> getFavoriteApp() {
        return getSaveApp("my_favorite_apps",
                "com.vphone.launcher", "com.amaze.filemanager", "com.android.documentsui", "com.tencent.wstt.gt");
    }

    public static List<App> getMusicApp() {
        return getSaveApp("my_music_apps", "com.android.music");
    }

    //主页定制的app
    public static List<App> getHomeCustomApp() {
        return getSaveApp("my_home_custom_apps");
    }

    public static void saveHomeCustomApp(List<App> appList) {
        saveApp(appList,"my_home_custom_apps");
    }

    /**
     * 根据包名删除主页自定义app
     * @param pName remove package name
     * @return the end result
     */
    public static List<App> removeHomeCustomApp(String pName) {
        if (TextUtils.isEmpty(pName)) {
            return null;
        }
        List<App> resultApp = new ArrayList<>();
        List<App> homeCustomApp = getHomeCustomApp();
        for (App app : homeCustomApp) {
            if (app != null&&!pName.equals(app.getPackageName())) {
                resultApp.add(app);
            }
        }
        resultApp.add(null);
        saveHomeCustomApp(resultApp);
        return resultApp;
    }

    public static void saveVideoApp(List<App> appList) {
        saveApp(appList,"my_video_apps");
    }

    public static void saveFavoriteApp(List<App> appList) {
        saveApp(appList,"my_favorite_apps");

    }

    public static void saveMusicApp(List<App> appList) {
        saveApp(appList,"my_music_apps");

    }

    public static void saveRecommendApp(List<App> appList) {
        saveApp(appList,"my_recommend_apps");
    }

    private static void saveApp(List<App> appList, String key) {
        List<App> newList = new ArrayList<>();
        for (App app : appList) {
            if (app != null) {
                newList.add(new App(app.getName(),null,app.getPackageName()));
            }
        }
        String json = new Gson().toJson(newList);
        ShareAdapter.getInstance().saveStr(key, json);
    }


    /**
     * 获取界面上显示的数据，有加号
     *
     * @param key         sp的key
     * @param defaultApps 默认应用的包名数组，在用户修改过后，不予显示
     * @return
     */
    public static List<App> getSaveApp(String key, String... defaultApps) {
        List<App> apps = new ArrayList<>();
        String appStr = ShareAdapter.getInstance().getStr(key);
        if (!TextUtils.isEmpty(appStr)) {
            List<App> appList = new Gson().fromJson(appStr, new TypeToken<List<App>>() {
            }.getType());
            if (appList != null) {
                for (App app : appList) {
                    if (app != null && Utils.isInstalled(AppMain.ctx(), app.getPackageName())) {
                        app.setIcon(Utils.getAppIconDrawable(AppMain.ctx(), app.getPackageName()));
                        apps.add(app);
                    }
                }
            }
        } else {
            //设置默认值
            if (defaultApps != null && defaultApps.length > 0) {
                for (String packageName : defaultApps) {
                    App app = Utils.getAppInfo(AppMain.ctx(), packageName);
                    if (app != null) {
                        apps.add(app);
                    }
                }
            }
        }
        apps.add(null);//加号
        return apps;
    }
}
