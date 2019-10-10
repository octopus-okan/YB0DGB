package com.globalhome.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.globalhome.adapter.AppAdapter;
import com.globalhome.adapter.HomeCustomAppAdapter;
import com.globalhome.bridge.SelEffectBridge;
import com.globalhome.data.App;
import com.globalhome.data.json.regoem.CheckMacBean;
import com.globalhome.data.json.regoem.IpBean;
import com.globalhome.data.json.regoem.Recommend3Bean;
import com.globalhome.data.json.regoem.RecommendBean;
import com.globalhome.data.json.regoem.RecommendbgBean;
import com.globalhome.data.json.regoem.RecommendlogoBean;
import com.globalhome.data.json.regoem.RecommendmarqueeBean;
import com.globalhome.data.json.regoem.RecommendversionBean;
import com.globalhome.data.json.regoem.RemoveAppBean;
import com.globalhome.data.json.regoem.VersionBean;
import com.globalhome.data.marquee.Marquee;
import com.globalhome.data.marquee.Root;
import com.globalhome.utils.AppHandler;
import com.globalhome.utils.AppMain;
import com.globalhome.utils.AppManager;
import com.globalhome.utils.DataUtils;
import com.globalhome.utils.GlideMgr;
import com.globalhome.utils.HttpUtils;
import com.globalhome.utils.NetTool;
import com.globalhome.utils.ShareAdapter;
import com.globalhome.utils.TimeHandler;
import com.globalhome.utils.Utils;
import com.globalhome.utils.WallperHandler;
import com.globalhome.views.dialogs.BottomAppDialog;
import com.globalhome.views.dialogs.HomeAppDialog;
import com.globalhome.views.dialogs.HomeAppsDialog;
import com.google.gson.Gson;
import com.h3launcher.BuildConfig;
import com.h3launcher.R;
import com.h3launcher.databinding.ActivityMainBinding;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.globalhome.utils.PageType.FAVORITE_TYPE;
import static com.globalhome.utils.PageType.HOME_BOTTOM_TYPE;
import static com.globalhome.utils.PageType.HOME_TYPE;
import static com.globalhome.utils.PageType.MUSIC_TYPE;
import static com.globalhome.utils.PageType.MY_APP_TYPE;
import static com.globalhome.utils.PageType.RECENT_TYPE;
import static com.globalhome.utils.PageType.VIDEO_TYPE;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        View.OnClickListener, TimeHandler.OnTimeDateListener, NetTool.OnNetListener,
        AppHandler.OnScanListener, AppHandler.OnAddRemoeveListener,
        View.OnKeyListener, AppHandler.OnBottomListener, WallperHandler.OnWallperUpdateListener {

    private static final String TAG = "H3Launcher/Main";

    private ActivityMainBinding binding;
    private SelEffectBridge selEffectBridge;
    private TimeHandler timeHandler;
    private NetTool netTool;
    public AppHandler appHandler;
//    public WallperHandler wallperHandler;

    private HomeAppDialog homeAppDialog;
    private HomeAppsDialog homeAppsDialog;
    private BottomAppDialog bottomAppDialog;

    private boolean updateScanOK = false;
    private boolean marqueeScanOK = false;
    private List<String> imgUrls;

    private Context mContext;


    /**
     * apk的下载状态
     * 0：没有下载过 1：下载中  2：下载完成  3：下载失败
     */
    private Map<String, Integer> apkType = new HashMap<>();
    //网络数据/cache
    private RecommendBean recommendBean;
    private RecommendlogoBean recommendlogoBean;
    private Recommend3Bean recommend3Bean;
    private RecommendmarqueeBean recommendmarqueeBean;
    private RecommendbgBean recommendbgBean;

    //加载推荐app成功
    private boolean isLoadAppSucc = false;
    private boolean isCheckVersion = false;
    private FrameLayout[] flItems;
    private FrameLayout[] flItemAdd;
    private ProgressBar[] pbItems;
    private TextView[] tvItems;

    //    private SimpleDraweeView[] ivs;
    private ImageView[] ivs;
    private ImageView[] ivadd;
    private Drawable[] drawable;

    String cacheImg = "";
    public static final String bootVideo = "/system/media/boot.mp4";
    public static final String newBootVideo = "/system/media/new_boot.zip";

    private String deviceId = "729";//TVBOX 中性版
//    private String appName = "LRM01";
//    private String lunchname = "LRM01";

//    private String appName = "LRM02";
//    private String lunchname = "LRM02";
//    int cid = 43 ;    //客户号  (国内)
//    int cid = 44;       //客户号  (越南)(有logo)
//    int cid = 46;         //客户号  (越南)(无logo)

    private String appName = "TOPDGB";    //蒋总
    private String lunchname = "TOPDGB";
    int cid = 49;    //客户号  蒋总(国内)

    String netMac;   //设备的以太网mac地址
    //    String wifiMac;   //设备的wifi的mac地址
    String cidIP;   //设备的IP
    String region; //设备所在地区

    //服务器地址
//    String host = "http://yue.jhzappdev.com:8976/";   //越南
    private String host = "http://cn.jhzappdev.com:8976/";    //国内


    private String removeResult;
    private HomeCustomAppAdapter homeCustomAppAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        flItems = new FrameLayout[]{binding.fl1, binding.fl2, binding.fl3, binding.fl4, binding.fl5, binding.fl6, binding.fl7, binding.fl8};
        ivs = new ImageView[]{binding.bgIv1, binding.bgIv2, binding.bgIv3, binding.bgIv4, binding.bgIv5, binding.bgIv6, binding.bgIv7, binding.bgIv8};

        flItemAdd = new FrameLayout[]{binding.flAdd1, binding.flAdd2, binding.flAdd3, binding.flAdd4, binding.flAdd5, binding.flAdd6, binding.flAdd7, binding.flAdd8, binding.flAdd9, binding.flAdd10, binding.flAdd11, binding.flAdd12};
        ivadd = new ImageView[]{binding.bgAdd1, binding.bgAdd2, binding.bgAdd3, binding.bgAdd4, binding.bgAdd5, binding.bgAdd6, binding.bgAdd7, binding.bgAdd8, binding.bgAdd9, binding.bgAdd10, binding.bgAdd11, binding.bgAdd12};

        binding.fl5.setOnClickListener(this);
        binding.fl5.setOnKeyListener(this);
        binding.fl5.setOnFocusChangeListener(this);
        binding.fl6.setOnClickListener(this);
        binding.fl6.setOnKeyListener(this);
        binding.fl6.setOnFocusChangeListener(this);
        binding.fl3.setOnClickListener(this);
        binding.fl3.setOnKeyListener(this);
        binding.fl3.setOnFocusChangeListener(this);
        binding.fl4.setOnClickListener(this);
        binding.fl4.setOnKeyListener(this);
        binding.fl4.setOnFocusChangeListener(this);
        binding.fl1.setOnClickListener(this);
        binding.fl1.setOnKeyListener(this);
        binding.fl1.setOnFocusChangeListener(this);
        binding.fl2.setOnClickListener(this);
        binding.fl2.setOnKeyListener(this);
        binding.fl2.setOnFocusChangeListener(this);
        binding.fl7.setOnKeyListener(this);
        binding.fl7.setOnClickListener(this);
        binding.fl7.setOnFocusChangeListener(this);
        binding.fl8.setOnClickListener(this);
        binding.fl8.setOnKeyListener(this);
        binding.fl8.setOnFocusChangeListener(this);

        //隐藏全部加号
        for (int i = 0; i < flItemAdd.length; i++) {
            flItemAdd[i].setVisibility(View.INVISIBLE);
            ivadd[i].setVisibility(View.INVISIBLE);
            flItemAdd[i].setOnFocusChangeListener(this);
            flItemAdd[i].setOnClickListener(this);
            flItemAdd[i].setOnKeyListener(this);
        }


        selEffectBridge = (SelEffectBridge) binding.mainUpView.getEffectBridge();
        selEffectBridge.setUpRectResource(R.drawable.home_sel_btn);
        binding.topInfo.getViewTreeObserver().addOnGlobalFocusChangeListener(
                new ViewTreeObserver.OnGlobalFocusChangeListener() {
                    @Override
                    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                        Log.d(TAG, "onGlobalFocusChanged " + newFocus + " " + oldFocus);
                        if (newFocus == null) {
                            return;
                        }
                        int focusVId = newFocus.getId();
                        switch (focusVId) {
                            case R.id.fl1:
                            case R.id.fl2:
                            case R.id.fl3:
                            case R.id.fl4:
                            case R.id.fl5:
                            case R.id.fl6:
                            case R.id.fl7:
                            case R.id.fl8:
                            case R.id.fl_add1:
                            case R.id.fl_add2:
                            case R.id.fl_add3:
                            case R.id.fl_add4:
                            case R.id.fl_add5:
                            case R.id.fl_add6:
                            case R.id.fl_add7:
                            case R.id.fl_add8:
                            case R.id.fl_add9:
                            case R.id.fl_add10:
                            case R.id.fl_add11:
                            case R.id.fl_add12:
                                selEffectBridge.setVisibleWidget(false);
                                binding.mainUpView.setFocusView(newFocus, oldFocus, 0f);
                                newFocus.bringToFront();
                                break;
                        }
                    }
                });

        //时间更新
        timeHandler = new TimeHandler(this);
        timeHandler.setOnTimeDateListener(this);
        //监听网络状态
        netTool = new NetTool(this);
        netTool.setOnNetListener(this);

        appHandler = new AppHandler(MainActivity.this, HOME_TYPE);
        appHandler.setOnScanListener(this);
        appHandler.setAddRemoeveListener(this);
        appHandler.setOnBottomListener(this);

        appHandler.scan();

        drawable = new Drawable[]{binding.bgAdd1.getDrawable(), binding.bgAdd2.getDrawable(), binding.bgAdd3.getDrawable(), binding.bgAdd4.getDrawable(),
                binding.bgAdd5.getDrawable(), binding.bgAdd6.getDrawable(), binding.bgAdd7.getDrawable(), binding.bgAdd8.getDrawable()};


        String mac = Utils.getDevID().toUpperCase();
        NetTool.setMac(mac);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        timeHandler.regTimeReceiver();
        netTool.registerNetReceiver();
        appHandler.regAppReceiver();
        appHandler.scanHome();//显示最近使用历史，扫描添加本地应用
        if (bottomAppDialog != null) {
            appHandler.scanBottom();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        Log.d(TAG, "launcher is stop");
        timeHandler.unRegTimeReceiver();
        netTool.unRegisterNetReceiver();
        appHandler.unRegAppReceiver();
//        binding.adBg.stopAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Log.d(TAG, "launcher was killed");
        timeHandler.release();
        netTool.release();
        appHandler.release();
//        wallperHandler.release();
        timeHandler.setOnTimeDateListener(null);
        netTool.setOnNetListener(null);
        appHandler.setAddRemoeveListener(null);
        appHandler.setOnScanListener(null);
        timeHandler = null;
        netTool = null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange " + v);
        switch (v.getId()) {
            case R.id.fl1: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv1.setImageResource(
//                        hasFocus ? R.drawable.img_googleplay : R.drawable.img_googleplay);
                binding.fl1.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl2: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv2.setImageResource(
//                        hasFocus ? R.drawable.img_browser : R.drawable.img_browser);
                binding.fl2.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl3: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv3.setImageResource(
//                        hasFocus ? R.drawable.img_video : R.drawable.img_video);
                binding.fl3.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl4: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv4.setImageResource(
//                        hasFocus ? R.drawable.img_app : R.drawable.img_app);
                binding.fl4.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl5: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv5.setImageResource(
//                        hasFocus ? R.drawable.img_setting : R.drawable.img_setting);
                binding.fl5.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl6: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv6.setImageResource(
//                        hasFocus ? R.drawable.img_kodi : R.drawable.img_kodi);
                binding.fl6.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl7: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv7.setImageResource(
//                        hasFocus ? R.drawable.img_music : R.drawable.img_music);
                binding.fl7.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl8: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgIv8.setImageResource(
//                        hasFocus ? R.drawable.img_memory : R.drawable.img_memory);
                binding.fl8.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv4.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add1: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd1.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd1.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add2: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd2.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd2.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add3: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd3.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd3.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add4: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd4.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd4.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add5: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd5.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd5.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add6: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd6.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd6.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add7: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
//                binding.bgAdd7.setImageResource(
//                        hasFocus ? R.drawable.item_child_6 : R.drawable.item_child_6);
                binding.flAdd7.animate().scaleX(scale).scaleY(scale).start();
//                binding.topTv5.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                return;
            }
            case R.id.fl_add8: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
                binding.flAdd8.animate().scaleX(scale).scaleY(scale).start();
                return;
            }
            case R.id.fl_add9: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
                binding.flAdd9.animate().scaleX(scale).scaleY(scale).start();
                return;
            }
            case R.id.fl_add10: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
                binding.flAdd10.animate().scaleX(scale).scaleY(scale).start();
                return;
            }
            case R.id.fl_add11: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
                binding.flAdd11.animate().scaleX(scale).scaleY(scale).start();
                return;
            }
            case R.id.fl_add12: {
                selEffectBridge.setVisibleWidget(true);
                float scale = hasFocus ? 1.1f : 1.0f;
                binding.flAdd12.animate().scaleX(scale).scaleY(scale).start();
                return;
            }

        }
    }

    @Override
    public void onClick(View v) {
        handleViewKey(v, -1, true);
    }

    @Override
    public void onTimeDate(String time, String date) {
        Log.e(TAG, "onTimeDate " + time + "==" + date);
        String[] split = time.split(" ");
        String hour = split[0];
        if (!TextUtils.isEmpty(hour)) {
            binding.timeTv.setText(hour);
        }
        String day = split[1];
        if (!TextUtils.isEmpty(day)) {
            binding.tvDate.setText(day);
        }
    }

    @Override
    public void onNetState(boolean isConnected, int type) {
        boolean b = NetTool.isNetworkOK();
//        if (b) {
//            String n = getEthernetMacAddress();
//            String w = getWiFiMacAddress(mContext);
//            if (!"".equals(n) && null != n) {
//                netMac = n;
//            } else if (!"".equals(w) && null != w) {
//                netMac = w;
//            }
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    GetNetIp();
//                }
//            }.start();
////
//            if (!binding.netIv.isShown()) {
//                binding.netIv.setVisibility(View.VISIBLE);
//            }
//            switch (type) {
//                case 1: // WIFI
//                    binding.netIv.setImageResource(R.drawable.wifi5);
//                    break;
//                default: // 2 -> ETH
//                    binding.netIv.setImageResource(R.drawable.img_status_ethernet);
//                    break;
//            }
//        } else {
//            binding.netIv.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void wifiLevel(int level) {
//        switch (level) {
//            case 0:
//                binding.netIv.setImageResource(R.drawable.wifi1);
//                break;
//            case 1:
//                binding.netIv.setImageResource(R.drawable.wifi2);
//                break;
//            case 2:
//                binding.netIv.setImageResource(R.drawable.wifi3);
//                break;
//            case 3:
//                binding.netIv.setImageResource(R.drawable.wifi4);
//                break;
//            case 4:
//                binding.netIv.setImageResource(R.drawable.wifi5);
//                break;
//        }
    }

    @Override
    public void onResponse(SparseArray<App> apps) {
        if (homeAppsDialog != null) {
            homeAppsDialog.loadAppData(apps, "我的应用");
        }
    }

    /**
     * 添加或者删除桌面图标
     *
     * @param id
     * @param app
     */
    @Override
    public void addRemove(int id, App app) {
        Log.d(TAG, String.format("addRemove %d %s", id, app));
//        if (true) {
//            return;
//        }
        switch (id) {
            case R.id.fl_add1: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd1);
                binding.flAdd1.setTag(app.getPackageName());
                if (binding.flAdd1.getTag() != null) {
                    for (int i = 0; i < 2; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 2; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add2: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd2);
                binding.flAdd2.setTag(app.getPackageName());
                if (binding.flAdd2.getTag() != null) {
                    for (int i = 0; i < 3; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 3; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add3: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd3);
                binding.flAdd3.setTag(app.getPackageName());
                if (binding.flAdd3.getTag() != null) {
                    for (int i = 0; i < 4; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 4; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add4: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd4);
                binding.flAdd4.setTag(app.getPackageName());
                if (binding.flAdd4.getTag() != null) {
                    for (int i = 0; i < 5; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 5; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add5: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd5);
                binding.flAdd5.setTag(app.getPackageName());
                if (binding.flAdd5.getTag() != null) {
                    for (int i = 0; i < 6; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 6; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add6: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd6);
                binding.flAdd6.setTag(app.getPackageName());
                if (binding.flAdd6.getTag() != null) {
                    for (int i = 0; i < 7; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 7; i < flItemAdd.length; i++) {
                        flItemAdd[i].setVisibility(View.GONE);
                        ivadd[i].setVisibility(View.GONE);
                    }
                }
                return;
            }
            case R.id.fl_add7: {
                GlideMgr.loadNormalDrawableImg(MainActivity.this,
                        app.getIcon(), binding.bgAdd7);
                binding.flAdd7.setTag(app.getPackageName());
                if (binding.flAdd7.getTag() != null) {
                    for (int i = 0; i < 8; i++) {
                        flItemAdd[i].setVisibility(View.VISIBLE);
                        ivadd[i].setVisibility(View.VISIBLE);
                    }
                }
                return;
            }
//            case R.id.fl_add8: {
//                GlideMgr.loadNormalDrawableImg(MainActivity.this,
//                        app.getIcon(), binding.bgAdd8);
//                binding.flAdd8.setTag(app.getPackageName());
//                return;
//            }

        }
    }

    @Override
    public void homeAppResult(List<App> apps) {
        if (apps == null) return;
        Log.d("homeapp", apps.toString());
        for (int i = 0; i < flItemAdd.length; i++) {
            ImageView imageView = ivadd[i];
            FrameLayout frameLayout = flItemAdd[i];
            if (i < apps.size()) {
                App app = apps.get(i);
                if (app != null) {
//                    GlideMgr.loadNormalDrawableImg(MainActivity.this,
//                            app.getIcon(), ivadd[i]);
                    imageView.setImageDrawable(app.getIcon());
                    frameLayout.setTag(app.getPackageName());
                } else {
                    //加号
//                    GlideMgr.loadNormalDrawableImg(MainActivity.this,
//                            AppMain.res().getDrawable(R.drawable.item_img_add), ivadd[i]);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.item_img_add));
                    frameLayout.setTag(null);
                }
                frameLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            } else {
                frameLayout.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void showHomeAppsDialog(int rId, String pName) {
        homeAppsDialog = HomeAppsDialog.showHomeAppDialog(MainActivity.this, rId, pName);
    }

    public void scan() {
        appHandler.scan();
    }

    public void scanRecent() {
        appHandler.scanRecent();
    }

    public void launchApp(String packageName) {
        appHandler.launchApp(packageName);
    }

    /**
     * 处理点击事件、菜单键
     *
     * @param v
     * @param keyCode
     * @param isClick true:点击 false:菜单
     */
    private void handleViewKey(View v, int keyCode, boolean isClick) {
        int id = v.getId();
        switch (id) {
            /**下面内容**/
            case R.id.fl1:
                launchApp("com.brtzel.tv");//glaxy g
//                launchApp("com.android.vending");
                break;
            case R.id.fl2:
                Apps2Activity.launch(this, MY_APP_TYPE);
//                launchApp("com.android.chrome");
                break;
            case R.id.fl3:
                launchApp("dp.ws.popcorntime");
//                Apps2Activity.launch(this, VIDEO_TYPE);
                break;
            case R.id.fl4:
                launchApp("com.valor.mfc.droid.tvapp.generic");//family
//                Apps2Activity.launch(this, MY_APP_TYPE);
                break;
            case R.id.fl5:
                launchApp("com.android.settings");
                break;
            case R.id.fl6:
                launchApp("com.android.vending");//googleplay
//                launchApp("org.xbmc.kodi");
//                launchApp("com.softwinner.TvdFileManager");
                break;
            case R.id.fl7:
                launchApp("com.google.android.youtube.tv");
//                Apps2Activity.launch(this, MUSIC_TYPE);
                break;
            case R.id.fl8:
                launchApp("com.netflix.mediaclient");
//                launchApp("kantv.clean");
                break;
            case R.id.fl_add1:
            case R.id.fl_add2:
            case R.id.fl_add3:
            case R.id.fl_add4:
            case R.id.fl_add5:
            case R.id.fl_add6:
            case R.id.fl_add7:
            case R.id.fl_add8:
            case R.id.fl_add9:
            case R.id.fl_add10:
            case R.id.fl_add11:
            case R.id.fl_add12:
                Object obj = v.getTag();//pName
                if (obj == null || !isClick) {
                    lunchHomeAppDialog(obj, id);//显示四个菜单项弹窗
                } else if (obj != null && isClick) {
                    launchApp(obj.toString());
                }
                break;
        }
    }


    /**
     * 点击广告图片，跳转到相对应的网页
     */
    private void WebRedirection() {
        String url = null;
        url = web.get(ClickOnTheAD);
        Log.e("tag", "url=" + url);
        clickAd(ad_id);
        if (!"".equals(url) && null != url) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            this.startActivity(intent);
        } else {
            Toast.makeText(mContext, "没有广告链接!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param obj pName
     * @param id  view id
     */
    public void lunchHomeAppDialog(Object obj, int id) {
        homeAppDialog = HomeAppDialog.showHomeAppDialog(this,
                obj != null ? obj.toString() : null, id);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            return false;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
//                Toast.makeText(mContext, "menu1", Toast.LENGTH_SHORT).show();
                handleViewKey(v, keyCode, false);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                //todo 底部弹窗
//                handleViewKeyDown(v);
                break;
        }
        if (BuildConfig.DEBUG && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            handleViewKey(v, keyCode, false);
        }
        return false;
    }

    @Override
    public void updateBottom(int vId, App app) {
        if (bottomAppDialog != null) {
            bottomAppDialog.updateBottom(vId, app);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory");
    }

    public void scanBottom() {
        appHandler.scanBottom();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_0:// 0
                    inputNumber("0");
                    break;
                case KeyEvent.KEYCODE_1:// 1
                    inputNumber("1");
                    break;
                case KeyEvent.KEYCODE_2:// 2
                    inputNumber("2");
                    break;
                case KeyEvent.KEYCODE_3:// 3
                    inputNumber("3");
                    break;
                case KeyEvent.KEYCODE_4:// 4
                    inputNumber("4");
                    break;
                case KeyEvent.KEYCODE_5:// 5
                    inputNumber("5");
                    break;
                case KeyEvent.KEYCODE_6:// 6
                    inputNumber("6");
                    break;
                case KeyEvent.KEYCODE_7:// 7
                    inputNumber("7");
                    break;
                case KeyEvent.KEYCODE_8:// 8
                    inputNumber("8");
                    break;
                case KeyEvent.KEYCODE_9:// 9
                    inputNumber("9");
                    break;
                case KeyEvent.KEYCODE_F1: //F1
                    inputNumber("F1");
                    break;
                case KeyEvent.KEYCODE_F2:    //F2
                    inputNumber("F2");
                    break;
                case KeyEvent.KEYCODE_F3:     //F3
                    inputNumber("F3");
                    break;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    private static final String StartDragonTest = "1379";//测试
    private static final String StartDragonAging = "2379";//老化
    private static final String versionInfo = "3379";//版本信息

    private static final String zhibo = "F1";  //直播
    private static final String dianbo = "F2";  //点播
    private static final String app = "F3";    //我的应用

    long oldTime = 0;
    String num = "";

    private void inputNumber(String i) {
        long inputTime = System.currentTimeMillis();
        if (inputTime - oldTime < 1000) {
            //1s内输入有效
            num += i;
        } else {
            //如果输入时间超过1s,num统计的值重置为输入值
            num = i;
        }
        oldTime = inputTime;
//        Toast.makeText(mContext, num, Toast.LENGTH_SHORT).show();
        switch (num) {
            case StartDragonTest:
                //重置输入
                num = "";
                oldTime = 0;
//                Toast.makeText(this, "启动测试:", Toast.LENGTH_SHORT).show();
                if (AppManager.isInstallApp(mContext, "com.wxs.scanner")) {
//                    startActivity(new Intent().setClassName("com.kong.apptesttools", "com.kong.apptesttools.MainActivity"));
                    startActivity(new Intent().setClassName("com.wxs.scanner", "com.wxs.scanner.activity.workstation.CheckActivity"));
                } else {
//                    Toast.makeText(mContext, "未安装测试App", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, R.string.no_install_app, Toast.LENGTH_SHORT).show();
                }
                break;
            case StartDragonAging:
                //重置输入
                num = "";
                oldTime = 0;
//                Toast.makeText(this, "启动老化测试:", Toast.LENGTH_SHORT).show();
                if (AppManager.isInstallApp(mContext, "com.softwinner.agingdragonbox")) {
                    AppManager.startAgingApk(mContext);
                } else {
//                    Toast.makeText(mContext, "未安装老化App", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, R.string.no_install_old_app, Toast.LENGTH_SHORT).show();
                }
                break;
            case versionInfo:
                num = "";
                oldTime = 0;
                String deviceName;
                if ("702".equals(deviceId)) {
                    deviceName = "柴喜";
                } else if ("701".equals(deviceId)) {
//                    deviceName = "拓普赛特";
                    deviceName = "Mở rộng khu vực";
                } else if ("704".equals(deviceId)) {
                    deviceName = "老凤祥";
                } else if ("696".equals(deviceId)) {
                    deviceName = "精合智";
                } else {
                    deviceName = "其它";
                }
                new AlertDialog.Builder(mContext)
//                        .setTitle("版本信息")
//                        .setMessage(appName + "-" + BuildConfig.VERSION_NAME +
//                                "\n服务范围：" + (host.startsWith("http://192.168.") ? "内网" : "外网") +
//                                "\n品牌商：" + deviceName)
                        .setTitle("Phiên bản thông tin")
                        .setMessage(appName + "-" + BuildConfig.VERSION_NAME +
                                "\nPhạm vi dịch vụ：" + (host.startsWith("http://192.168.") ? "Mạng nội bộ" : "Mạng bên ngoài") +
                                "\nThương hiệu：" + deviceName)
                        .show();
                break;

//            case zhibo:
//                num = "";
//                oldTime = 0;
//                if (AppManager.isInstallApp(mContext,live)) {
//                    launchApp(live);
//                }else {
//                    Toast.makeText(mContext,R.string.no_app,Toast.LENGTH_LONG).show();
//                }
//                break;
//
//            case dianbo:
//                num = "";
//                oldTime = 0;
//                if (AppManager.isInstallApp(mContext,vod)) {
//                    launchApp(vod);
//                }else {
//                    Toast.makeText(mContext,R.string.no_app,Toast.LENGTH_LONG).show();
//                }
//                break;

            case app:
                num = "";
                oldTime = 0;
                Apps2Activity.launch(this, MY_APP_TYPE);
                break;

//            case "6666":
//                num = "";
//                oldTime = 0;
//                String cache = getSharedPreferences("my_setting", MODE_PRIVATE).getString("recommend_cache", null);
//                View view = LayoutInflater.from(mContext).inflate(R.layout.test_cache, null);
//                ((TextView) view.findViewById(R.id.tv_content)).setText("" + cache);
//                new AlertDialog.Builder(mContext)
//                        .setTitle("cache")
//                        .setView(view)
//                        .show();
//                break;
//            case "7777":
//                num = "";
//                oldTime = 0;
//                View view1 = LayoutInflater.from(mContext).inflate(R.layout.test_cache, null);
//                ((TextView) view1.findViewById(R.id.tv_content)).setText("" + cacheImg);
//                new AlertDialog.Builder(mContext)
//                        .setTitle("cacheImg")
//                        .setView(view1)
//                        .show();
//                break;
            case "8888":
                num = "";
                oldTime = 0;
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.test_cache, null);
                ((TextView) view1.findViewById(R.id.tv_content)).setText("" + removeResult);
                new AlertDialog.Builder(mContext)
                        .setTitle("remove result")
                        .setView(view1)
                        .show();

                break;
        }
    }


    @Override
    public void wallperUpdate() {
        Log.d(TAG, "wallperUpdate");
//        List<String> tmpImgUrls = wallperHandler.getImgUrls();
        List<String> tmpImgUrls = null;
        if (tmpImgUrls == null || tmpImgUrls.size() == 0) {
            return;
        }
//        binding.adBg.stopAutoPlay();
//        this.imgUrls = tmpImgUrls;
//        binding.adBg.setData(R.layout.ad_item, imgUrls, null);
//        binding.adBg.setPageTransformer(Transformer.Cube);
//        binding.adBg.setmAdapter(new XBanner.XBannerAdapter() {
//            @Override
//            public void loadBanner(XBanner banner, Object model, View view, int position) {
//                Log.d(TAG, "loadBanner " + view);
//                String url = imgUrls.get(position);
//                Log.d(TAG, "loadBanner " + url);
//                if (!TextUtils.isEmpty(url)) {
//                    ((SimpleDraweeView) view).setImageURI(Uri.parse(imgUrls.get(position)));
//                }
//            }
//        });
//        binding.adBg.startAutoPlay();
    }

    /**
     * 检查mac是否可用
     */
    private void checkMac(String mac) {
        String url = host + "jhzBox/box/loadBox.do?cy_brand_id=" + deviceId + "&mac=" + mac + "&netCardMac=" + netMac + "&cid=" + cid + "&codeIp=" + cidIP + "&region=" + region;

        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag", "访问失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String res = response.body().string();
                    Log.e("Tag", "res=" + res);
                    final CheckMacBean checkMacBean = new Gson().fromJson(res, CheckMacBean.class);
                    if (checkMacBean.getStatus() != 0) {
                        //设备不可用
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(R.string.hint)
                                        .setMessage(checkMacBean.getMsg())
                                        .setCancelable(false)
                                        .show();
                            }
                        });

                    } else {//设备可用时加载数据
//                        getRemoveApp();
//                        if (!isLoadAppSucc) {
//                            getRecommendApp();
//                            getRecommendAd();
//                            getRecommendLogo();
//                            getRecommendMarquee();
//                            getRecommendBgImg();
//                        }
                    }
                    if (!isCheckVersion) {
                        checkVersion();
                    }
                }

            }
        });
    }

    /**
     * 获取卸载的app列表
     */
    private void getRemoveApp() {
        String url = host + "jhzBox/box/unload.do?cy_brand_id=" + deviceId;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String res = response.body().string();
                    Log.e(TAG, "onResponse: " + res);
                    RemoveAppBean removeAppBean = new Gson().fromJson(res, RemoveAppBean.class);
                    if (removeAppBean.getStatus() == 0) {
                        //卸载app
                        List<String> data = removeAppBean.getData();
                        for (String pck : data) {
//                            removeResult = AppManager.unInstall(mContext, pck);
//                            AppManager.clientUninstall(pck);
//                            AppManager.uninstall(mContext,pck);
//                            AppManager.uninstallApp(mContext,pck);
                            if (AppManager.isInstallApp(mContext, pck)) {
//                                AppManager.myUninstall(mContext,pck);
//                                removeResult = AppManager.execCommand("rm", "-f", "/data/app/"+pck+"-1.apk");
//                                removeResult = AppManager.execCommand("pm", "uninstall",pck);
//                                removeResult = String.valueOf(AppManager.slientunInstall(pck));
                                AppManager.uninstallApk(mContext, pck);
                            }
                        }
                    }
                }

            }
        });
    }


    /**
     * 获取推荐的app列表
     */
    private void getRecommendApp() {
        String url = host + "jhzBox/box/loadPushApp.do?pitClass=01&cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&lunchname=" + lunchname + "&netCardMac=" + netMac + "&cid=" + cid;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String res = response.body().string();
                    Log.e(TAG, "onResponse:app= " + res);
                    final RecommendBean rBean = new Gson().fromJson(res, RecommendBean.class);
                    if (rBean.getStatus() == 0) {
                        //加载成功
                        final List<RecommendBean.DataBean> data = rBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //保存缓存
                                getSharedPreferences("my_setting", MODE_PRIVATE).edit().putString("recommend_cache", res).commit();
                                recommendBean = rBean;
                                isLoadAppSucc = true;
                                if (data != null && data.size() != 0) {
                                    for (int i = 0; i < ivs.length; i++) {
                                        if (!TextUtils.isEmpty(data.get(i).getSyy_app_img())) {
                                            Glide.with(mContext).load(data.get(i).getSyy_app_img()).into(ivs[i]);
                                        }
                                    }
                                }
                                //是否显示未下载标签
//                                setupItemBottomTag();
                            }

                        });
                    }
                }
            }
        });
    }

    /**
     * 获取推荐的跑马灯
     */
    private void getRecommendMarquee() {
        String url = host + "jhzBox/box/loadMarquee.do?cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&lunchname=" + lunchname + "&netCardMac=" + netMac + "&cid=" + cid;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String res = response.body().string();
                    Log.e(TAG, "onResponse:ma= " + res);
                    final RecommendmarqueeBean rBean = new Gson().fromJson(res, RecommendmarqueeBean.class);
                    if (rBean.getStatus() == 0) {
                        //加载成功
                        final RecommendmarqueeBean.DataBean data = rBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSharedPreferences("my_setting", MODE_PRIVATE).edit().putString("recommend_marquee_cache", res).commit();
                                //显示跑马灯
//                                if (data != null) {
//                                    binding.scrollTv.setText(data.getMarquee());
//                                }
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * 获取推荐的背景图
     */
    private void getRecommendBgImg() {
        String url = host + "jhzBox/box/backgroundImg.do?cy_brand_id=" + deviceId + "&lunchname=" + lunchname;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String res = response.body().string();
                    Log.e(TAG, "onResponse:bg= " + res);
                    final RecommendbgBean rBean = new Gson().fromJson(res, RecommendbgBean.class);
                    if (rBean.getStatus() == 0) {
                        //加载成功
                        final List<RecommendbgBean.DatabgBean> data = rBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSharedPreferences("my_setting", MODE_PRIVATE).edit().putString("recommend_bg_cache", res).commit();
                                //显示背景图
                                if (data != null && data.size() != 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        if (!TextUtils.isEmpty(data.get(i).getBackgroundImgAddress())) {
                                            Glide.with(mContext).load(data.get(i).getBackgroundImgAddress()).into(binding.ivBg);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取推荐的广告列表
     */
    HashMap<String, String> web = new HashMap<>();
    String ClickOnTheAD = null;
    String ad_id = null;

    //广告视频链接
    String urls = null;

    private void getRecommendAd() {
        String url = host + "jhzBox/box/loadAdv.do?cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&lunchname=" + lunchname + "&netCardMac=" + netMac + "&cid=" + cid;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String res = response.body().string();
                    Log.e(TAG, "onResponse:ad= " + res);
                    final Recommend3Bean rBean = new Gson().fromJson(res, Recommend3Bean.class);
                    if (rBean.getStatus() == 0) {
                        //加载成功
                        final List<Recommend3Bean.DataBean> data = rBean.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示广告
                                if (data != null && data.size() > 0) {
                                    getSharedPreferences("my_setting", MODE_PRIVATE).edit().putString("recommend_ad_cache", res).commit();

                                    for (int i = 0; i < data.size(); i++) {
                                        //广告链接
                                        web.put(data.get(i).getCy_advertisement_imgAddress(), data.get(i).getAdvLink());
                                        //视频广告链接
                                        if (data.get(i).getCy_advertisement_videoAddress() != null) {
                                            urls = (String) data.get(i).getCy_advertisement_videoAddress();
                                        }
                                    }
//                                    urls = "http://192.168.5.101/cc.mp4";
//                                    if (urls != null){
//                                        advertisementVideo(urls);
//                                    }else {
//                                        binding.adBg.stopAutoPlay();
//                                        binding.adBg.setData(R.layout.ad_item, data, null);
//                                        binding.adBg.setmAdapter(new XBanner.XBannerAdapter() {
//                                            @Override
//                                            public void loadBanner(XBanner banner, Object model, View view, int position) {
//                                                String url = data.get(position).getCy_advertisement_imgAddress();
//                                                //获取被点击广告页的网址链接
//                                                if (position == 0) {
//                                                    ClickOnTheAD = data.get(data.size() - 1).getCy_advertisement_imgAddress();
//                                                    ad_id = data.get(data.size() - 1).getCy_advertisement_id();
//                                                } else {
//                                                    ClickOnTheAD = data.get(position - 1).getCy_advertisement_imgAddress();
//                                                    ad_id = data.get(position - 1).getCy_advertisement_id();
//                                                }
//                                                if (!TextUtils.isEmpty(url)) {
//                                                    Glide.with(mContext).load(url).into((SimpleDraweeView) view);
//                                                }
//                                            }
//                                        });
//                                        binding.adBg.startAutoPlay();
//                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * 统计用户在线时长
     */
    private void onlineTime() {
        String url = host + "jhzBox/box/onlineTime.do?&cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&netCardMac=" + netMac + "&cid=" + cid;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Tag", "访问失败！");
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("Tag", "访问成功！");
            }
        });
    }


    /**
     * 统计用户点击app次数
     */
    private void clickApp(String app) {
        String url = host + "jhzBox/box/appLike.do?&cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&netCardMac=" + netMac + "&cid=" + cid + "&lunchname=" + lunchname + "&syy_app_id=" + app;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Tag", "app访问失败！");
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("Tag", "app访问成功！");
            }
        });
    }


    /**
     * 统计用户点击广告的次数
     */
    private void clickAd(String ad) {
        String url = host + "jhzBox/box/advLike.do?&cy_brand_id=" + deviceId + "&mac=" + Utils.getDevID().toUpperCase() + "&netCardMac=" + netMac + "&cid=" + cid + "&lunchname=" + lunchname + "&cy_advertisement_id=" + ad;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Tag", "ad访问失败！");
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("Tag", "ad访问成功！");
            }
        });
    }


    /**
     * 带进度下载apk
     *
     * @param dataBean
     * @param index
     */
    private void downloadApk(final RecommendBean.DataBean dataBean, final int index) {
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder()
                .url(dataBean.getSyy_app_download()).tag(dataBean.getSyy_app_packageName()).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                flItems[index].setEnabled(true);
//                                tvItems[index].setText("下载失败");
                                tvItems[index].setText(R.string.download_failed);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e(TAG, "响应:" + response);
                        if (response != null && response.isSuccessful()) {
                            InputStream inputStream = response.body().byteStream();
                            String filePath = AppManager.getAppDir() + dataBean.getSyy_app_download().substring(dataBean.getSyy_app_download().lastIndexOf("/") + 1);

                            Log.e(TAG, "下载路径：" + filePath);
                            FileOutputStream fos = new FileOutputStream(filePath);
                            int len = 0;
                            byte[] buffer = new byte[1024 * 10];
                            while ((len = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                            }
                            fos.flush();
                            fos.close();
                            inputStream.close();
                            Log.d(TAG, "下载完成！");
                            //静默安装应用 todo
//                            final int result = AppManager.installSilent(filePath);
//                            Log.e(TAG, "install apk result: " + result);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    flItems[index].setEnabled(true);
//                                    if (result == 0) {
//                                        //安装成功
//                                        pbItems[index].setVisibility(View.GONE);
//                                        tvItems[index].setVisibility(View.GONE);
//                                    } else {
//                                        tvItems[index].setText("安装失败");
//                                    }
//                                }
//                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    flItems[index].setEnabled(true);
                                    pbItems[index].setVisibility(View.GONE);
                                    tvItems[index].setVisibility(View.GONE);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    flItems[index].setEnabled(true);
//                                    tvItems[index].setText("下载失败");
                                    tvItems[index].setText(R.string.download_failed);
                                }
                            });

                        }
                    }
                });
    }


    /**
     * 普通下载apk安装
     *
     * @param url
     */
    private void downloadApk(final String url) {
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder()
                .url(url).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(mContext, "下载失败!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null && response.isSuccessful()) {
                            InputStream inputStream = response.body().byteStream();

                            final String filePath = AppManager.getAppDir() + url.substring(url.lastIndexOf("/") + 1);
                            FileOutputStream fos = new FileOutputStream(filePath);
                            int len = 0;
                            byte[] buffer = new byte[1024 * 10];
                            while ((len = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                            }
                            fos.flush();
                            fos.close();
                            inputStream.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //安装
                                    AppManager.install(mContext, filePath);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(mContext, "下载失败!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    private void downloadBootVideo(String url, final String md5) {
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder()
                .url(url).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null && response.isSuccessful()) {
                            //下载视频
                            InputStream inputStream = response.body().byteStream();
                            FileOutputStream fos = new FileOutputStream(newBootVideo);
                            int len = 0;
                            byte[] buffer = new byte[1024 * 10];
                            while ((len = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                            }
                            fos.flush();
                            fos.close();
                            inputStream.close();

                            File newFile = new File(newBootVideo);
                            String new_md5 = AppManager.getMD5(newFile);
                            if (md5.equals(new_md5)) {
                                //删除原来的视频
                                File file = new File(bootVideo);
                                if (file.exists() && file.isFile()) {
                                    file.delete();
                                }
                                //重命名现在的视频
                                newFile.renameTo(file);
                            }
                        }
                    }
                });
    }

    /**
     * 检查更新版本
     */
    private void checkVersion() {
        isCheckVersion = true;
        String url = host + "jhzBox/box/appOnlineVersion.do?versionNum=" + BuildConfig.VERSION_NAME + "&cy_brand_id=" + deviceId
                + "&cy_versions_name=" + appName + "&lunchname=" + lunchname + "&mac=" + Utils.getDevID().toUpperCase() + "&cid=" + cid + "&netCardMac=" + netMac;
        HttpUtils.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null && response.isSuccessful()) {
                            String res = response.body().string();
                            Log.e(TAG, "onResponse:version= " + res);
                            final RecommendversionBean versionBean = new Gson().fromJson(res, RecommendversionBean.class);
                            if (versionBean.getStatus() == 4) {
                                final RecommendversionBean.DataBean data = versionBean.getData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(mContext)
                                                .setTitle(R.string.version_updating)
                                                .setMessage(data.getCy_versions_info())
                                                .setNegativeButton(R.string.cancles, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(mContext, R.string.background_download, Toast.LENGTH_SHORT).show();
                                                        downloadApk(data.getCy_versions_path());
                                                    }
                                                }).show();
                                    }
                                });
                            }
                        }
                    }
                });
    }

}
