package com.globalhome.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ListView;
import android.widget.Toast;

import com.globalhome.activities.AppsActivity;
import com.globalhome.activities.MainActivity;
import com.h3launcher.R;
import com.globalhome.data.App;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Oracle on 2017/12/1.
 */

public class AppHandler {

    private static final String TAG = AppHandler.class.getSimpleName();
    private Context context;
    private static final String recentAppKey = "recent_apps";
    private static final String videoAppKey = "video_apps";
    private static final String recommendAppKey = "recommend_apps";
    private static final String myAppKey = "my_apps";
    private static final String musicAppKey = "music_apps";
    private static final String favoriteAppKey = "favorite_apps";

    public static final String CLEAR_ACTION = "com.h3launcher.action.clear";
    public static final String ADD_ACTION = "com.h3launcher.action.add";
    public static final String SEND_APP = "send_app";

    List<String> videoList = new ArrayList<>();
    List<String> recommendList = new ArrayList<>();
    List<String> musicList = new ArrayList<>();
    List<String> favoriteList = new ArrayList<>();

    private Drawable drawable;

    private PageType type;
    private PageType oldType;
    private ExecutorService scanExecutorService;
    private PackageManager packageManager;
    public String settings;

    public Context mContext;

    public SparseArray<App> appv;
    private StringBuilder sbv;
    private StringBuilder sbm;
    private StringBuilder sbf;

    public AppHandler(Context context, PageType type) {
        mContext = context;
        this.context = context;
        this.type = type;
        this.oldType = this.type;
        this.packageManager = this.context.getPackageManager();
        scanExecutorService = Executors.newSingleThreadExecutor();
        drawable = mContext.getResources().getDrawable(R.drawable.item_img_add);
    }

    public void changePageType(PageType pageType) {
        oldType = type;
        type = pageType;
    }

    public void resetPageType() {
        type = oldType;
    }

    public void scan() {
        scanExecutorService.submit(new ScanTask());
    }

    public void scanVideo() {
        scanExecutorService.submit(new ScanVideoTask());
    }

    public void scanRecommend() {
        scanExecutorService.submit(new ScanRecommendTask());
    }

    public void scanMusic() {
        scanExecutorService.submit(new ScanMusicTask());
    }

    public void scanFavorite() {
        scanExecutorService.submit(new ScanFavoriteTask());
    }


    //扫描添加的应用
    public void scanHome() {
        scanExecutorService.submit(new ScanHomeTask(false));
    }

    public void scanRecent() {
        scanExecutorService.submit(new ScanRecentTask());
    }

    public void scanBottom() {
        scanExecutorService.submit(new ScanBottomTask());
    }


    public void release() {
        context = null;
        scanExecutorService.shutdownNow();
        scanExecutorService = null;
    }

    public void setOnRecentListener(OnRecentListener onRecentListener) {
        this.onRecentListener = onRecentListener;
    }

    public void setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
    }

    class ScanTask implements Runnable {

        @Override
        public void run() {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> scanVals = packageManager.queryIntentActivities(intent, 0);
            List<ResolveInfo> scanValsTmp = new LinkedList<ResolveInfo>();
            for (int i = 0; i < scanVals.size(); i++) {
                ResolveInfo resolveInfo = scanVals.get(i);
                if (!isPreInstallApp(resolveInfo.activityInfo.packageName)) {
                    scanValsTmp.add(resolveInfo);
                }
            }

            int size = scanValsTmp.size();
            final SparseArray<App> list = new SparseArray<>();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = scanValsTmp.get(i);
                App app = new App();
                app.setName(resolveInfo.loadLabel(packageManager).toString());
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setIcon(resolveInfo.loadIcon(packageManager));
                Log.e("Tag", "app=" + app);
                //获取设置的包名
                if (app.getName().equals("设置") || app.getName().equals("Settings") || app.getName().equals("settings") || app.getName().equals("Thiết lập")) {
                    settings = app.getPackageName();
                    Log.d("Tag", "settings=" + settings);
                }
                list.put(i, app);
                Log.d(TAG, app.toString());
            }
            appv = list;
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(list);
                    }
                });
            }

            hehe(list);
        }
    }


    public void hehe(SparseArray<App> list) {
        //默认的app
//        videoList.add("com.android.browser");
        videoList.add("com.google.android.videos");
        recommendList.add("com.android.gallery3d");
        recommendList.add("com.android.settings");
//        musicList.add("com.bignox.app.store.hd");
        musicList.add("com.android.music");
        favoriteList.add("com.vphone.launcher");
        favoriteList.add("com.amaze.filemanager");
        favoriteList.add("com.android.documentsui");
        favoriteList.add("com.tencent.wstt.gt");

        Log.e("===cjw", musicList.size() + " , " + favoriteList.size());
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < videoList.size(); j++) {
                if (videoList.get(j).equals(list.get(i).getPackageName())) {
                    scanExecutorService.submit(new SaveVideoTask(list.get(i).getPackageName()));
                }
            }
            for (int j = 0; j < recommendList.size(); j++) {
                if (recommendList.get(j).equals(list.get(i).getPackageName())) {
                    scanExecutorService.submit(new SaveRecommendTask(list.get(i).getPackageName()));
                }
            }
            for (int j = 0; j < musicList.size(); j++) {
                Log.e("===cjw", "musicList:" + musicList.get(j) + " ;  " + list.get(i).getPackageName());
                if (musicList.get(j).equals(list.get(i).getPackageName())) {
                    Log.e("===cjw", "musicList");
                    scanExecutorService.submit(new SaveMusicTask(list.get(i).getPackageName()));
                }
            }
            for (int j = 0; j < favoriteList.size(); j++) {
                if (favoriteList.get(j).equals(list.get(i).getPackageName())) {
                    Log.e("===cjw", "favoriteList");
                    scanExecutorService.submit(new SaveFavoriteTask(list.get(i).getPackageName()));
                }
            }
        }
    }

    class ScanVideoTask implements Runnable {

        @Override
        public void run() {

            //用于存放Video类应用
            StringBuilder sbVideo = new StringBuilder();
            String VideoApps = ShareAdapter.getInstance().getStr(videoAppKey);
            String[] VideoApp = !TextUtils.isEmpty(VideoApps) ?
                    VideoApps.split(";") : new String[0];

            int VideoAppNum = VideoApp.length;
            final SparseArray<App> VideoList = new SparseArray<>();
            for (int VideoIndex = 0, appIndex = 0; VideoIndex < VideoAppNum;
                 VideoIndex++) {

                App VideoAppObj = new App();

                try {
                    String packageName = VideoApp[VideoIndex];
                    PackageInfo packageInfo
                            = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    VideoAppObj.setName(applicationInfo.loadLabel(packageManager).toString());
                    VideoAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                    VideoAppObj.setPackageName(applicationInfo.packageName);
                    VideoList.put(appIndex++, VideoAppObj);
                    sbVideo.append(packageName).append(";");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            App appv = new App();
            appv.setName("请添加");
            appv.setPackageName("add");
            appv.setIcon(drawable);
            VideoList.put(VideoAppNum, appv);
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(VideoList);
                    }
                });
            }
            ShareAdapter.getInstance().saveStr(videoAppKey, sbVideo.toString());
        }

    }

    public StringBuilder appKEYv() {
        if (!"".equals(sbv) && null != sbv) {
            StringBuilder s = sbv;
            return s;
        }
        return null;
    }

    public StringBuilder appKEYm() {
        if (!"".equals(sbm) && null != sbm) {
            StringBuilder s = sbm;
            return s;
        }
        return null;
    }

    public StringBuilder appKEYf() {
        if (!"".equals(sbf) && null != sbf) {
            StringBuilder s = sbf;
            return s;
        }
        return null;
    }

    /**
     * 保存Video应用
     */
    class SaveVideoTask implements Runnable {

        String packageName = null;

        public SaveVideoTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {

            ShareAdapter shareAdapter = ShareAdapter.getInstance();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageName).append(";");
            String hdAppsStr = shareAdapter.getStr(videoAppKey);
            if (!TextUtils.isEmpty(hdAppsStr)) {
                String[] hdAppArr = hdAppsStr.split(";");
                for (String hdApp : hdAppArr) {
                    if (!hdApp.equals(packageName)) {
                        stringBuilder.append(hdApp).append(";");
                    }
                }
            }
            sbv = stringBuilder;
            shareAdapter.saveStr(videoAppKey, stringBuilder.toString());
            scanExecutorService.submit(new ScanHomeTask(true));
        }
    }


    class ScanRecommendTask implements Runnable {

        @Override
        public void run() {
            //用于存放Recommend类应用
            StringBuilder sbRecommend = new StringBuilder();
            String RecommendApps = ShareAdapter.getInstance().getStr(recommendAppKey);
            String[] RecommendApp = !TextUtils.isEmpty(RecommendApps) ?
                    RecommendApps.split(";") : new String[0];

            int RecommendAppNum = RecommendApp.length;
            final SparseArray<App> RecommendList = new SparseArray<>();
            for (int RecommendIndex = 0, appIndex = 0; RecommendIndex < RecommendAppNum;
                 RecommendIndex++) {

                App RecommendAppObj = new App();

                try {
                    String packageName = RecommendApp[RecommendIndex];
                    PackageInfo packageInfo
                            = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    RecommendAppObj.setName(applicationInfo.loadLabel(packageManager).toString());
                    RecommendAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                    RecommendAppObj.setPackageName(applicationInfo.packageName);
                    RecommendList.put(appIndex++, RecommendAppObj);
                    sbRecommend.append(packageName).append(";");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            Log.e("Tag", "RecommendList=" + RecommendList);
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(RecommendList);
                    }
                });
            }
            ShareAdapter.getInstance().saveStr(recommendAppKey, sbRecommend.toString());
        }
    }

    /**
     * 保存Recommend应用
     */
    class SaveRecommendTask implements Runnable {

        String packageName = null;

        public SaveRecommendTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            ShareAdapter shareAdapter = ShareAdapter.getInstance();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageName).append(";");
            String hdAppsStr = shareAdapter.getStr(recommendAppKey);
            if (!TextUtils.isEmpty(hdAppsStr)) {
                String[] hdAppArr = hdAppsStr.split(";");
                for (String hdApp : hdAppArr) {
                    if (!hdApp.equals(packageName)) {
                        stringBuilder.append(hdApp).append(";");
                    }
                }
            }
            shareAdapter.saveStr(recommendAppKey, stringBuilder.toString());
            scanExecutorService.submit(new ScanHomeTask(true));
        }
    }


    class ScanMusicTask implements Runnable {
        @Override
        public void run() {
            //用于存放Music类应用
            StringBuilder sbMusic = new StringBuilder();
            String MusicApps = ShareAdapter.getInstance().getStr(musicAppKey);
            String[] MusicApp = !TextUtils.isEmpty(MusicApps) ?
                    MusicApps.split(";") : new String[0];

            int MusicAppNum = MusicApp.length;
            final SparseArray<App> MusicList = new SparseArray<>();
            for (int MusicIndex = 0, appIndex = 0; MusicIndex < MusicAppNum;
                 MusicIndex++) {

                App MusicAppObj = new App();

                try {
                    String packageName = MusicApp[MusicIndex];
                    PackageInfo packageInfo
                            = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    MusicAppObj.setName(applicationInfo.loadLabel(packageManager).toString());
                    MusicAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                    MusicAppObj.setPackageName(applicationInfo.packageName);
                    MusicList.put(appIndex++, MusicAppObj);
                    sbMusic.append(packageName).append(";");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            App appv = new App();
            appv.setName("请添加");
            appv.setPackageName("add");
            appv.setIcon(drawable);
            MusicList.put(MusicAppNum, appv);
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(MusicList);
                    }
                });
            }
            ShareAdapter.getInstance().saveStr(musicAppKey, sbMusic.toString());
        }
    }

    /**
     * isPreInstallApp
     * 保存Music应用
     */
    class SaveMusicTask implements Runnable {

        String packageName = null;

        public SaveMusicTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            ShareAdapter shareAdapter = ShareAdapter.getInstance();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(packageName).append(";");
            String hdAppsStr = shareAdapter.getStr(musicAppKey);
            if (!TextUtils.isEmpty(hdAppsStr)) {
                String[] hdAppArr = hdAppsStr.split(";");
                for (String hdApp : hdAppArr) {
                    if (!hdApp.equals(packageName)) {
                        stringBuilder.append(hdApp).append(";");
                    }
                }
            }

            sbm = stringBuilder;
            shareAdapter.saveStr(musicAppKey, stringBuilder.toString());
            scanExecutorService.submit(new ScanHomeTask(true));
        }
    }


    class ScanFavoriteTask implements Runnable {
        @Override
        public void run() {
            //用于存放Favorite类应用
            StringBuilder sbFavorite = new StringBuilder();
            String FavoriteApps = ShareAdapter.getInstance().getStr(favoriteAppKey);
            String[] FavoriteApp = !TextUtils.isEmpty(FavoriteApps) ?
                    FavoriteApps.split(";") : new String[0];

            int FavoriteAppNum = FavoriteApp.length;
            final SparseArray<App> FavoriteList = new SparseArray<>();
            for (int FavoriteIndex = 0, appIndex = 0; FavoriteIndex < FavoriteAppNum;
                 FavoriteIndex++) {

                App FavoriteAppObj = new App();

                try {
                    String packageName = FavoriteApp[FavoriteIndex];
                    PackageInfo packageInfo
                            = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    FavoriteAppObj.setName(applicationInfo.loadLabel(packageManager).toString());
                    FavoriteAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                    FavoriteAppObj.setPackageName(applicationInfo.packageName);
                    FavoriteList.put(appIndex++, FavoriteAppObj);
                    sbFavorite.append(packageName).append(";");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            App appv = new App();
            appv.setName("请添加");
            appv.setPackageName("add");
            appv.setIcon(drawable);
            FavoriteList.put(FavoriteAppNum, appv);
            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(FavoriteList);
                    }
                });
            }
            ShareAdapter.getInstance().saveStr(favoriteAppKey, sbFavorite.toString());
        }
    }

    /**
     * 保存Favorite应用
     */
    class SaveFavoriteTask implements Runnable {

        String packageName = null;

        public SaveFavoriteTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            ShareAdapter shareAdapter = ShareAdapter.getInstance();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageName).append(";");
            String hdAppsStr = shareAdapter.getStr(favoriteAppKey);
            if (!TextUtils.isEmpty(hdAppsStr)) {
                String[] hdAppArr = hdAppsStr.split(";");
                for (String hdApp : hdAppArr) {
                    if (!hdApp.equals(packageName)) {
                        stringBuilder.append(hdApp).append(";");
                    }
                }
            }
            sbf = stringBuilder;
            shareAdapter.saveStr(favoriteAppKey, stringBuilder.toString());
            scanExecutorService.submit(new ScanHomeTask(true));
        }
    }


    private boolean isPreInstallApp(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return true;
        }
//        if (packageName.equals("com.android.vending")) {
//            Log.e(TAG,"过滤谷歌商店="+packageName);
//            return true;
//        }
        if (packageName.equals("com.h3launcher")) {
            //隐藏自己
            return true;
        }
        return false;
    }

    //扫描主页添加的app
    class ScanHomeTask implements Runnable {

        boolean isOnlyRecent = false;

        public ScanHomeTask(boolean isOnlyRecent) {
            this.isOnlyRecent = isOnlyRecent;
        }

        @Override
        public void run() {

            final boolean listenerOk = addRemoeveListener != null;
            ShareAdapter shareAdapter = ShareAdapter.getInstance();

            if (!isOnlyRecent) {
                if (true) {
                    if (listenerOk) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listenerOk) {
                                    addRemoeveListener.homeAppResult(DataUtils.getHomeCustomApp());
                                }
                            }
                        });
                    }
                    return;
                }
                int[] ids = new int[]{
                        R.id.fl_add1, R.id.fl_add2, R.id.fl_add3, R.id.fl_add4, R.id.fl_add5, R.id.fl_add6, R.id.fl_add7
                };
                App addApp = new App();
                addApp.setIcon(AppMain.res().getDrawable(R.drawable.item_img_add));
                App resultApp = null;
                int idNum = ids.length;
                for (int i = 0; i < idNum; i++) {
                    final int idI = ids[i];
                    Log.d(TAG, "idI = " + idI);
                    String idStr = String.valueOf(idI);
                    String packageName = shareAdapter.getStr(idStr);
                    Log.d(TAG, "packageName " + packageName);
                    if (listenerOk) {
                        if (TextUtils.isEmpty(packageName)) {
                            resultApp = addApp;
                        } else {
                            try {
                                PackageInfo packageInfo
                                        = packageManager.getPackageInfo(packageName, 0);
                                Drawable drawable
                                        = packageInfo.applicationInfo.loadIcon(packageManager);
                                String title
                                        = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                                resultApp = new App(title, drawable, packageName);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                resultApp = addApp;
                            }
                        }
                        Log.d(TAG, resultApp.toString());
                        final App sendApp = resultApp;
                        if (listenerOk) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "idI=" + idI + "  sendApp=" + sendApp);
                                    addRemoeveListener.addRemove(idI, sendApp);
                                }
                            });
                        }
                    }
                }
            }
            /**
             * 最近使用
             */
//            String recentAppsStr = shareAdapter.getStr(recentAppKey);
//            if (!TextUtils.isEmpty(recentAppsStr)) {
//                final int[] recentIds = new int[]{
////                        R.id.recent_iv1, R.id.recent_iv2, R.id.recent_iv3,
////                        R.id.recent_iv4
//                };
//                String[] recentApps = recentAppsStr.split(";");
//                int recentIdNum = recentIds.length;
//                int recentAppNum = recentApps.length;
//                App recentApp = null;
//                for (int idIndex = 0, appIndex = 0; idIndex < recentIdNum; appIndex++) {
//
//                    final int vId = recentIds[idIndex];
//                    recentApp = new App();
//
//                    if (idIndex < recentAppNum) {
//
//                        try {
//                            String packageName = recentApps[appIndex];
//                            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
//                            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//                            recentApp.setIcon(applicationInfo.loadIcon(packageManager));
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                            continue;
//                        }
//                    }
//
//                    idIndex++;
//
//                    final App sendRecentApp = recentApp;
//                    if (listenerOk) {
//                        ((Activity) context).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                addRemoeveListener.addRemove(vId, sendRecentApp);
//                            }
//                        });
//                    }
//                }
//            }

            /**
             *   video
             */
//            String videoAppsStr = shareAdapter.getStr(videoAppKey);
//            if (!TextUtils.isEmpty(videoAppsStr)) {
//                final int[] videoIds = new int[]{
//                           0,1,2,3,4,5,6,7,8,9
//                };
//                String[] videoApps = videoAppsStr.split(";");
//                int videoIdNum = 100;
//                int videoAppNum = videoApps.length;
//                App videoApp = null;
//                for (int idIndex = 0, appIndex = 0; idIndex < videoIdNum; appIndex++) {
//                    final int vId = videoIds[idIndex];
//                    videoApp = new App();
//                    if (idIndex < videoAppNum) {
//                        try {
//                            String packageName = videoApps[appIndex];
//                            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
//                            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//                            videoApp.setIcon(applicationInfo.loadIcon(packageManager));
//                            Log.e("Tag","videoAppsStr="+videoAppsStr+"   >"+videoApp);
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                            continue;
//                        }
//                    }
//
//                    idIndex++;
//
//                    final App sendVideoApp = videoApp;
//                    Log.e(TAG,"sendApp="+sendVideoApp);
//
//                    if (listenerOk) {
//                        ((Activity) context).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e(TAG,"vId="+"  sendApp="+sendVideoApp);
//                                addRemoeveListener.addRemove(vId, sendVideoApp);
//                            }
//                        });
//                    }
//                }
//            }
        }
    }

    public void regAppReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(ADD_ACTION);
        intentFilter.addAction(CLEAR_ACTION);
        intentFilter.addDataScheme("package");
        context.registerReceiver(appReceiver, intentFilter);
    }

    public void unRegAppReceiver() {
        context.unregisterReceiver(appReceiver);
    }

    private BroadcastReceiver appReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, String.format("appReceiver %s", intent.getAction()));
//            Toast.makeText(context,
//                    String.format("appReceiver %s", intent.getAction()), Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            if (action.equals(CLEAR_ACTION)) {//菜单移除app
                if (true) {
                    String pName = intent.getStringExtra("pName");
                    if (pName != null) {
                        List<App> apps = DataUtils.removeHomeCustomApp(pName);
                        if (addRemoeveListener != null) {
                            addRemoeveListener.homeAppResult(apps);
                        }
                    }
                    return;
                }
                //xxxx
                int vId = intent.getIntExtra("vId", -1);
                if (vId >= 0) {
                    App clearApp = new App();
                    clearApp.setIcon(AppMain.res().getDrawable(R.drawable.item_img_add));
                    if (onBottomListener != null) {
                        onBottomListener.updateBottom(vId, clearApp);
                    }
                    if (addRemoeveListener != null) {
                        clearApp.setName(AppMain.res().getString(R.string.please_add));
                        addRemoeveListener.addRemove(vId, clearApp);
                    }
                    ShareAdapter.getInstance().remove(String.valueOf(vId));
                }
            } else if (action.equals(ADD_ACTION)) {//xxx
                App app = intent.getParcelableExtra(SEND_APP);
                Log.d(TAG, "recv send app " + app);
                if (app != null) {
                    if (addRemoeveListener != null) {
                        int vId = intent.getIntExtra("vId", -1);
                        Log.d(TAG, "recv send app " + vId);
                        if (vId >= 0) {
                            addRemoeveListener.addRemove(vId, app);
                            addAppToShort(app.getPackageName(), vId);
                        }
                    }
                }
            } else {
                switch (type) {
                    case HOME_TYPE:
                        scanHome();
                        break;
                    case MY_APP_TYPE:
                        scan();
                        break;
                    case BOTTOM_TYPE:
                        scanBottom();
                        break;
                    case RECENT_TYPE:
                        scanRecent();
                        break;
                    case HOME_BOTTOM_TYPE:
                        scanHome();
                        scanBottom();
                        break;
                    case VIDEO_TYPE:
                        scanVideo();
                        break;
                    case RECOMMEND_TYPE:
                        scanRecommend();
                        break;
                    case MUSIC_TYPE:
                        scanMusic();
                        break;
                    case FAVORITE_TYPE:
                        scanFavorite();
                        break;
                }
            }
        }
    };

    public void addAppToShort(String packageName, int vId) {
        ShareAdapter.getInstance().saveStr(String.valueOf(vId), packageName);
    }

    public void addAppToShorts(StringBuilder packageName, String s) {
        ShareAdapter.getInstance().saveStr(s, packageName.toString());
    }

    public void launchApp(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            scanExecutorService.submit(new SaveRecentTask(packageName));
        } else {
            Log.d("apphandler", packageName + " not install ...");
        }
    }


    /**
     * 保存最近打开的app
     */
    class SaveRecentTask implements Runnable {

        String packageName = null;

        public SaveRecentTask(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public void run() {
            ShareAdapter shareAdapter = ShareAdapter.getInstance();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageName).append(";");
            String recentAppsStr = shareAdapter.getStr(recentAppKey);
            if (!TextUtils.isEmpty(recentAppsStr)) {
                String[] recentAppArr = recentAppsStr.split(";");
                for (String recentApp : recentAppArr) {
                    if (!recentApp.equals(packageName)) {
                        stringBuilder.append(recentApp).append(";");
                    }
                }
            }
            shareAdapter.saveStr(recentAppKey, stringBuilder.toString());
            scanExecutorService.submit(new ScanHomeTask(true));
        }
    }

    class ScanRecentTask implements Runnable {

        @Override
        public void run() {

            StringBuilder stringBuilder = new StringBuilder();
            String recentAppsStr = ShareAdapter.getInstance().getStr(recentAppKey);
            String[] recentAppArr = !TextUtils.isEmpty(recentAppsStr) ?
                    recentAppsStr.split(";") : new String[0];

            int recentAppNum = recentAppArr.length;
            final SparseArray<App> list = new SparseArray<>();
            for (int recentIndex = 0, appIndex = 0; recentIndex < recentAppNum;
                 recentIndex++) {

                App recentAppObj = new App();

                try {
                    String packageName = recentAppArr[recentIndex];
                    Log.d(TAG, "ScanRecentTask " + packageName);
                    PackageInfo packageInfo
                            = packageManager.getPackageInfo(packageName, 0);
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    recentAppObj.setName(applicationInfo.loadLabel(packageManager).toString());
                    recentAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                    recentAppObj.setPackageName(applicationInfo.packageName);
                    list.put(appIndex++, recentAppObj);
                    stringBuilder.append(packageName).append(";");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            if (listener != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(list);
                    }
                });
            }

            ShareAdapter.getInstance().saveStr(recentAppKey, stringBuilder.toString());
        }
    }

    class ScanBottomTask implements Runnable {

        @Override
        public void run() {

            int[] vIds = new int[]{
                    R.id.pull_tag_9301, R.id.pull_tag_9302, R.id.pull_tag_9303, R.id.pull_tag_9304,
                    R.id.pull_tag_9305, R.id.pull_tag_9306, R.id.pull_tag_9307, R.id.pull_tag_9308,
                    R.id.pull_tag_9309, R.id.pull_tag_9310, R.id.pull_tag_9311, R.id.pull_tag_9312,
                    R.id.pull_tag_9313, R.id.pull_tag_9314, R.id.pull_tag_9315, R.id.pull_tag_9316
            };

            if (onBottomListener != null) {

                int vIdNum = vIds.length;
                for (int vIdIndex = 0; vIdIndex < vIdNum; vIdIndex++) {

                    final int idI = vIds[vIdIndex];
                    final App bottomAppObj = new App();
                    final String idStr = String.valueOf(idI);

                    try {
                        String packageName = ShareAdapter.getInstance().getStr(idStr);
                        Log.d(TAG, "ScanBottomTask " + packageName);
                        PackageInfo packageInfo
                                = packageManager.getPackageInfo(packageName, 0);
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        bottomAppObj.setIcon(applicationInfo.loadIcon(packageManager));
                        bottomAppObj.setPackageName(applicationInfo.packageName);
                    } catch (PackageManager.NameNotFoundException e) {
                    }

                    Log.d(TAG, "ScanBottomTask " + bottomAppObj);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onBottomListener.updateBottom(idI, bottomAppObj);
                        }
                    });
                }
            }
        }
    }

    public interface OnScanListener {
        void onResponse(SparseArray<App> apps);
    }


    public interface OnAddRemoeveListener {
        void addRemove(int id, App app);

        void homeAppResult(List<App> apps);
    }

    public interface OnRecentListener {
        void updateRecent(int vId, App app);
    }

    public interface OnBottomListener {
        void updateBottom(int vId, App app);
    }

    private OnScanListener listener;
    private OnAddRemoeveListener addRemoeveListener;
    private OnRecentListener onRecentListener;
    private OnBottomListener onBottomListener;

    public void setOnScanListener(OnScanListener listener) {
        this.listener = listener;
    }

    public void setAddRemoeveListener(OnAddRemoeveListener listener) {
        this.addRemoeveListener = listener;
    }
}
