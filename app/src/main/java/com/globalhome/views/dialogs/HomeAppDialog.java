package com.globalhome.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.globalhome.activities.AppsActivity;
import com.globalhome.utils.DataUtils;
import com.h3launcher.R;
import com.globalhome.activities.MainActivity;
import com.h3launcher.databinding.MenuDialogBinding;
import com.globalhome.utils.AppHandler;
import com.globalhome.utils.ShareAdapter;
import com.globalhome.utils.Utils;

/**
 * 菜单键的弹窗
 * Created by Oracle on 2017/12/2.
 */

public class HomeAppDialog extends Dialog implements View.OnClickListener,
            View.OnTouchListener {

    private static final String TAG = HomeAppDialog.class.getSimpleName();
    private MenuDialogBinding binding;
    private Context context;
    private String packageName;
    private int rId;

    public HomeAppDialog(@NonNull Context context, int themeResId, String packageName, int vId) {
        super(context, themeResId);
        this.context = context;
        this.packageName = packageName;
        this.rId = vId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.menu_dialog,
                null, false);
        setContentView(binding.getRoot());
        if (TextUtils.isEmpty(this.packageName)) {
            binding.del.setClickable(false);
            binding.remove.setClickable(false);
            binding.replace.setClickable(false);
            binding.del.setFocusable(false);
            binding.del.setFocusableInTouchMode(false);
            binding.remove.setFocusable(false);
            binding.remove.setFocusableInTouchMode(false);
            binding.replace.setFocusable(false);
            binding.replace.setFocusableInTouchMode(false);
            binding.del.setEnabled(false);
            binding.remove.setEnabled(false);
            binding.replace.setEnabled(false);
            binding.add.setOnClickListener(this);
        } else {
            binding.add.setEnabled(false);
            binding.add.setClickable(false);
            binding.add.setFocusable(false);
            binding.add.setFocusableInTouchMode(false);
            binding.del.setOnClickListener(this);
            binding.remove.setOnClickListener(this);
            binding.replace.setOnClickListener(this);
            binding.del.setOnClickListener(this);
        }
    }

    public static HomeAppDialog showHomeAppDialog(Activity activity, String packageName, int vId) {
        HomeAppDialog homeAppDialog = new HomeAppDialog(activity, R.style.MenuDialog,
                packageName, vId);
        homeAppDialog.show();
        return homeAppDialog;
    }

    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        handleClick(v);
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        packageName = null;
    }

    private void handleClick(View v) {
        switch (v.getId()) {
            case R.id.add:
            case R.id.replace:
                if (context.toString().contains("MainActivity")) {
                    ((MainActivity) context).showHomeAppsDialog(rId,packageName);
                }else if (context.toString().contains("AppsActivity")) {
                    ((AppsActivity) context).showHomeAppsDialog(rId);
                }
                break;
            case R.id.del:
                Utils.uninstallApp(context, packageName);
                break;
            case R.id.remove:
                clearCache();
                Intent intent = new Intent(AppHandler.CLEAR_ACTION);
                intent.setData(Uri.parse("package:www"));
                intent.putExtra("vId", rId);
                intent.putExtra("pName", packageName);
                context.sendBroadcast(intent);
                break;
        }
        dismiss();
    }

    private void clearCache() {
        ShareAdapter.getInstance().remove(packageName);
        ShareAdapter.getInstance().remove(String.valueOf(rId));
    }
}
