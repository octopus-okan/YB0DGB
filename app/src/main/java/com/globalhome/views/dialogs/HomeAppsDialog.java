package com.globalhome.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.globalhome.activities.AppsActivity;
import com.globalhome.utils.AppHandler;
import com.globalhome.utils.DataUtils;
import com.globalhome.utils.PageType;
import com.globalhome.utils.ShareAdapter;
import com.globalhome.utils.Utils;
import com.h3launcher.R;
import com.globalhome.activities.MainActivity;
import com.globalhome.adapter.AppAdapter;
import com.globalhome.data.App;
import com.h3launcher.databinding.ChooseactivityLayoutBinding;

import java.util.List;

/**
 * Created by Oracle on 2017/12/3.
 * 选择app的弹窗
 */

public class HomeAppsDialog extends Dialog {

    private static final String TAG = HomeAppsDialog.class.getSimpleName();
    private ChooseactivityLayoutBinding binding;
    private Context context;
    private SparseArray<App> appSparseArray = null;
    private AppAdapter appAdapter;
    private int vId;
    private AppHandler appHandler;
    private String string;
    SparseArray<App> appSA;

    private String pName;//弹窗home item 的包名

    public HomeAppsDialog(@NonNull Context context) {
        this(context, 0, 0);
    }

    public HomeAppsDialog(@NonNull Context context, int themeResId, int vId) {
        super(context, themeResId);
        this.context = context;
        this.vId = vId;
    }

    public HomeAppsDialog(@NonNull Context context, int themeResId, int vId, String pName) {
        super(context, themeResId);
        this.context = context;
        this.vId = vId;
        this.pName = pName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.chooseactivity_layout, null, false);
        setContentView(binding.getRoot());

        if (context instanceof MainActivity) {
            ((MainActivity) context).scan();
        }


        binding.chooseappListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.format("onItemClick %s", appSparseArray.get(position)));
//                Intent intent = new Intent(AppHandler.ADD_ACTION);
//                intent.putExtra(AppHandler.SEND_APP, appSparseArray.get(position));
//                intent.putExtra("vId", vId);
//                intent.setData(Uri.parse("package:www"));
//                context.sendBroadcast(intent);
                if (context instanceof MainActivity) {
                    if (true) {
                        App selectApp = appSparseArray.get(position);

                        //限制重复添加
                        List<App> homeCustomApp = DataUtils.getHomeCustomApp();
                        for (App app : homeCustomApp) {
                            if (app != null && app.getPackageName().equals(selectApp.getPackageName())) {
                                Utils.showToast("不能重复添加！");
                                return;
                            }
                        }
                        if (TextUtils.isEmpty(pName)) {
                            //添加
                            homeCustomApp.remove(null);
                            homeCustomApp.add(selectApp);
                            homeCustomApp.add(null);
                        } else {
                            //替换
                            for (int i=0;i<homeCustomApp.size();i++) {
                                App app = homeCustomApp.get(i);
                                if (app != null && app.getPackageName().equals(pName)) {
                                    homeCustomApp.set(i, selectApp);
                                }
                            }
                        }
                        //保存数据
                        DataUtils.saveHomeCustomApp(homeCustomApp);
                        //显示数据
                        MainActivity mainActivity = ((MainActivity) context);
                        mainActivity.homeAppResult(homeCustomApp);
                    } else {
                        App app = appSparseArray.get(position);
                        MainActivity mainActivity = ((MainActivity) context);
                        mainActivity.updateBottom(vId, app);
                        mainActivity.addRemove(vId, app);
                        mainActivity.appHandler.addAppToShort(app.getPackageName(), vId);
                    }
                }

                if (string.equals("Favorite")) {
                    if (context instanceof AppsActivity) {
                        App app = appSA.get(position);
                        AppsActivity AppsActivity = ((AppsActivity) context);
                        String saveDatas = ShareAdapter.getInstance().getStr("favorite_apps");
//                        StringBuilder strb = AppsActivity.appHandler.appKEYf().append(app.getPackageName()).append(";");
                        if (saveDatas == null) saveDatas = "";
                        StringBuilder strb = new StringBuilder().append(saveDatas).append(app.getPackageName()).append(";");
                        Utils.log(strb.toString());
                        AppsActivity.appHandler.addAppToShorts(strb, "favorite_apps");
                        ((AppsActivity) context).scanFavorite();
                    }
                } else if (string.equals("音乐")) {
                    if (context instanceof AppsActivity) {
                        App app = appSA.get(position);
                        AppsActivity AppsActivity = ((AppsActivity) context);
                        StringBuilder strb = AppsActivity.appHandler.appKEYm().append(app.getPackageName()).append(";");
                        AppsActivity.appHandler.addAppToShorts(strb, "music_apps");
                        ((AppsActivity) context).scanMusic();
                    }
                } else if (string.equals("在线视频")) {
                    if (context instanceof AppsActivity) {
                        App app = appSA.get(position);
                        AppsActivity AppsActivity = ((AppsActivity) context);
                        StringBuilder strb = AppsActivity.appHandler.appKEYv().append(app.getPackageName()).append(";");
                        AppsActivity.appHandler.addAppToShorts(strb, "video_apps");
                        ((AppsActivity) context).scanVideo();
                    }
                }

                dismiss();
            }
        });
    }

    public void loadAppData(SparseArray<App> appSparseArray, final String title) {
        this.appSparseArray = appSparseArray.clone();
        appSA = appSparseArray;
        string = title;
        appAdapter = new AppAdapter(context);
        appAdapter.setListMode(1);
        appAdapter.setApps(appSparseArray);
        binding.chooseappListview.setAdapter(appAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.appSparseArray != null) {
            this.appSparseArray.clear();
            this.appSparseArray = null;
        }
        if (appAdapter != null) {
            appAdapter.release();
            appAdapter = null;
        }
        context = null;
    }

    public static HomeAppsDialog showHomeAppDialog(Context context, int vId, String pName) {
        HomeAppsDialog homeAppsDialog = new HomeAppsDialog(context, R.style.MenuDialog, vId, pName);
        homeAppsDialog.show();
        return homeAppsDialog;
    }
}
