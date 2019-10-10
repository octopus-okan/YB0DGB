package com.globalhome.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import com.globalhome.views.dialogs.HomeAppDialog;
import com.globalhome.views.dialogs.HomeAppsDialog;
import com.h3launcher.R;
import com.globalhome.adapter.AppAdapter;
import com.globalhome.data.App;
import com.h3launcher.databinding.ActivityMyApplicationBinding;
import com.globalhome.utils.AppHandler;
import com.globalhome.utils.PageType;
import com.globalhome.utils.Utils;

import java.util.List;

/**
 * Created by Oracle on 2017/12/1.
 */

public class AppsActivity extends AppCompatActivity{

    private static final String TAG = AppsActivity.class.getSimpleName();
    private ActivityMyApplicationBinding binding;
    public AppHandler appHandler;
    private AppAdapter appAdapter;
    public PageType pageType;
    private HomeAppDialog homeAppDialog;
    private HomeAppsDialog homeAppsDialog;
    private Context context;
    private int ids = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_application);

        String pageTypeStr = getIntent().getStringExtra("type");

        pageType = TextUtils.isEmpty(pageTypeStr) ?
                PageType.MY_APP_TYPE : PageType.valueOf(pageTypeStr);
        appHandler = new AppHandler(AppsActivity.this, pageType);
        appAdapter = new AppAdapter(this);

        scans();

        appHandler.setOnScanListener(new AppHandler.OnScanListener() {
            @Override
            public void onResponse(SparseArray<App> apps) {
                appAdapter.setApps(apps);
                binding.allapps.setAdapter(appAdapter);
            }
        });
        binding.allapps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App app = (App) appAdapter.getItem(position);
                if (app.getPackageName().equals("add")){
                    lunchHomeAppDialog(null, (int) id);
                }
                if (app != null && !app.getPackageName().equals("add")) {
                    String packageName = app.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        appHandler.launchApp(packageName);
                    }
                }
            }
        });
        binding.allapps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ids = position;
//                View v = appAdapter.getView(position,view,parent);
//                v.animate().scaleX(1.1f).scaleY(1.1f).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.allapps.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                }
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        int i = (ids/6)*6;
                        if (ids == i){
                            if (binding.titleText.getText().equals("推荐")){
                                binding.titleImage.setImageResource(R.drawable.video);
                                binding.titleText.setText("在线视频");
                                binding.okImage.setImageResource(R.drawable.prompt_video);
                                appHandler.scanVideo();
                            }else if (binding.titleText.getText().equals("我的应用")){
                                binding.titleImage.setImageResource(R.drawable.recommend);
                                binding.titleText.setText("推荐");
                                binding.okImage.setImageResource(R.drawable.prompt_recommend);
                                appHandler.scanRecommend();
                            } else if (binding.titleText.getText().equals("音乐")){
                                binding.titleImage.setImageResource(R.drawable.app);
                                binding.titleText.setText("我的应用");
                                binding.okImage.setImageResource(R.drawable.prompt_app);
                                appHandler.scan();
                            } else if (binding.titleText.getText().equals("Favorite")){
                                binding.titleImage.setImageResource(R.drawable.music);
                                binding.titleText.setText("音乐");
                                binding.okImage.setImageResource(R.drawable.prompt_music);
                                appHandler.scanMusic();
                            } else if (binding.titleText.getText().equals("在线视频")){
                                binding.titleImage.setImageResource(R.drawable.local);
                                binding.titleText.setText("Favorite");
                                binding.okImage.setImageResource(R.drawable.prompt_local);
                                appHandler.scanFavorite();
                            }
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        int o =  (ids/6)*6+5;
                        if (ids == o || ids == appAdapter.getCount()-1){
                            if (binding.titleText.getText().equals("Favorite")){
                                binding.titleImage.setImageResource(R.drawable.video);
                                binding.titleText.setText("在线视频");
                                binding.okImage.setImageResource(R.drawable.prompt_video);
                                appHandler.scanVideo();
                            }else if (binding.titleText.getText().equals("在线视频")){
                                binding.titleImage.setImageResource(R.drawable.recommend);
                                binding.titleText.setText("推荐");
                                binding.okImage.setImageResource(R.drawable.prompt_recommend);
                                appHandler.scanRecommend();
                            } else if (binding.titleText.getText().equals("推荐")){
                                binding.titleImage.setImageResource(R.drawable.app);
                                binding.titleText.setText("我的应用");
                                binding.okImage.setImageResource(R.drawable.prompt_app);
                                appHandler.scan();
                            } else if (binding.titleText.getText().equals("我的应用")){
                                binding.titleImage.setImageResource(R.drawable.music);
                                binding.titleText.setText("音乐");
                                binding.okImage.setImageResource(R.drawable.prompt_music);
                                appHandler.scanMusic();
                            } else if (binding.titleText.getText().equals("音乐")){
                                binding.titleImage.setImageResource(R.drawable.local);
                                binding.titleText.setText("Favorite");
                                binding.okImage.setImageResource(R.drawable.prompt_local);
                                appHandler.scanFavorite();
                            }
                        }
                        break;
                }
                return false;
            }
        });

        binding.allapps.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                App app = (App) appAdapter.getItem(position);
                if (app != null) {
                    String packageName = app.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        Utils.uninstallApp(AppsActivity.this, packageName);
                    }
                }
                return true;
            }
        });
    }

    public void scans() {
        appHandler.scan();
    }

    public void scanVideo() {
        appHandler.scanVideo();
    }
    public void scanMusic() {
        appHandler.scanMusic();
    }
    public void scanFavorite() {
        appHandler.scanFavorite();
    }

    private void setData() {
        String title = (String) binding.titleText.getText();
        if (homeAppsDialog != null && appHandler.appv != null) {
            homeAppsDialog.loadAppData(appHandler.appv,title);
        }
    }

    public void lunchHomeAppDialog(Object obj, int id) {
        homeAppDialog = HomeAppDialog.showHomeAppDialog(this,
                obj != null ? obj.toString() : null, id);
    }
    public void showHomeAppsDialog(int rId) {
        homeAppsDialog = HomeAppsDialog.showHomeAppDialog(AppsActivity.this, rId,null);
        setData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (pageType) {
            case RECENT_TYPE:
                appHandler.scanRecent();
                break;
            case MY_APP_TYPE:
                binding.titleImage.setImageResource(R.drawable.app);
                binding.titleText.setText("我的应用");
                binding.okImage.setImageResource(R.drawable.prompt_app);
                appHandler.scan();
                break;
            case VIDEO_TYPE:
                binding.titleImage.setImageResource(R.drawable.video);
                binding.titleText.setText("在线视频");
                binding.okImage.setImageResource(R.drawable.prompt_video);
                appHandler.scanVideo();
                break;
            case MUSIC_TYPE:
                binding.titleImage.setImageResource(R.drawable.music);
                binding.titleText.setText("音乐");
                binding.okImage.setImageResource(R.drawable.prompt_music);
                appHandler.scanMusic();
                break;
            case RECOMMEND_TYPE:
                binding.titleImage.setImageResource(R.drawable.recommend);
                binding.titleText.setText("推荐");
                binding.okImage.setImageResource(R.drawable.prompt_recommend);
                appHandler.scanRecommend();
                break;
            case FAVORITE_TYPE:
                binding.titleImage.setImageResource(R.drawable.local);
                binding.titleText.setText("Favorite");
                binding.okImage.setImageResource(R.drawable.prompt_local);
                appHandler.scanFavorite();
                break;
        }
        appHandler.regAppReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appHandler.unRegAppReceiver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        appHandler.release();
        appHandler.setOnScanListener(null);
        appAdapter.release();
        appHandler = null;
        appAdapter = null;
        binding = null;
    }

    public static void lunchAppsActivity(Context context, PageType pageType) {
        Intent intent = new Intent(context, AppsActivity.class);
        intent.putExtra("type", pageType.name());
        context.startActivity(intent);
    }

    

}
