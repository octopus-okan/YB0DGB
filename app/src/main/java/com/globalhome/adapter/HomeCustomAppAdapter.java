package com.globalhome.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalhome.activities.Apps2Activity;
import com.globalhome.data.App;
import com.globalhome.utils.DataUtils;
import com.globalhome.utils.GlideMgr;
import com.globalhome.utils.PageType;
import com.globalhome.utils.Utils;
import com.h3launcher.R;
import com.h3launcher.databinding.ChooseactivityLayoutBinding;
import com.h3launcher.databinding.MenuDialogBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeCustomAppAdapter extends RecyclerView.Adapter<HomeCustomAppAdapter.Holder> {

    private List<App> apps = new ArrayList<>();

    public Context mContext;

    public HomeCustomAppAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_home_add_app, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final App app = apps.get(position);
        if (app != null) {
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), holder.iv_icon);
            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.launchApp(mContext, app.getPackageName());
                }
            });
            holder.ll_root.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {//must down,if not ,the position is next.
                        if (keyCode == KeyEvent.KEYCODE_MENU) {
                            showMenuDialog(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        } else {
            GlideMgr.loadNormalDrawableImg(mContext, mContext.getResources().getDrawable(R.drawable.item_img_add), holder.iv_icon);
            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectAppDialog(position);
                }
            });
            holder.ll_root.setOnKeyListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private final View ll_root;

        public Holder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            ll_root = itemView.findViewById(R.id.root);
        }
    }

    /**
     * 显示选择添加app的弹窗
     */
    private void showSelectAppDialog(final int pos) {
        ChooseactivityLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.chooseactivity_layout, null, false);
        final Dialog dialog = new Dialog(mContext, R.style.MenuDialog);
        dialog.setContentView(binding.getRoot());
        final SelectAppAdapter selectAppAdapter = new SelectAppAdapter(mContext);
        selectAppAdapter.setData(DataUtils.getExcludeAppList(mContext, Utils.getAppPackageNames(apps)));
        binding.chooseappListview.setAdapter(selectAppAdapter);
        binding.chooseappListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App app = selectAppAdapter.getData().get(position);
                getData().set(pos, app);
                //todo save data
                saveData();

                if (pos == getData().size() - 1) {
                    getData().add(null);//添加按钮 ，替换时不需要加号
                }
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 显示菜单弹窗
     *
     * @param pos
     */
    private void showMenuDialog(final int pos) {
        MenuDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.menu_dialog, null, false);
        final Dialog dialog = new Dialog(mContext, R.style.MenuDialog);
        dialog.setContentView(binding.getRoot());

        binding.add.setVisibility(View.GONE);
        binding.add.setEnabled(false);
        binding.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utils.uninstallApp(mContext, getData().get(pos).getPackageName());
                // 回来界面时需要刷新一遍数据，去掉删除的app
            }
        });
        binding.replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showSelectAppDialog(pos);
            }
        });
        binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getData().remove(pos);
                //todo save data
                saveData();

                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void saveData() {
        DataUtils.saveHomeCustomApp(getData());
    }

    public void setData(List<App> apps) {
        if (apps != null) {
            this.apps = apps;
            notifyDataSetChanged();
        }
    }

    public List<App> getData() {
        return apps;
    }
}
