package com.globalhome.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalhome.activities.Apps2Activity;
import com.globalhome.data.App;
import com.globalhome.recycler.Data;
import com.globalhome.utils.AppMain;
import com.globalhome.utils.DataUtils;
import com.globalhome.utils.GlideMgr;
import com.globalhome.utils.PageType;
import com.globalhome.utils.Utils;
import com.h3launcher.R;
import com.h3launcher.databinding.ChooseactivityLayoutBinding;
import com.h3launcher.databinding.MenuDialogBinding;

import java.util.ArrayList;
import java.util.List;

public class App2Adapter extends RecyclerView.Adapter<App2Adapter.Holder> {

    private List<App> apps = new ArrayList<>();


    public Context mContext;
    Apps2Activity apps2Activity;

    public App2Adapter(Apps2Activity apps2Activity) {
        mContext = apps2Activity;
        this.apps2Activity = apps2Activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_home_app, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final App app = apps.get(position);
        if (app != null) {
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), holder.iv_icon);
            holder.tv_title.setText(app.getName());
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
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            if (position % 6 == 0) {
                                apps2Activity.leftSwitch();
                                return true;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            if (position == apps.size() - 1 //最后一个
                                    || position % 6 == 5) {//最右一个
                                apps2Activity.rightSwitch();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        } else {
            GlideMgr.loadNormalDrawableImg(mContext, mContext.getResources().getDrawable(R.drawable.item_img_add), holder.iv_icon);
            holder.tv_title.setText("请添加");
            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectAppDialog(position);
                }
            });
            holder.ll_root.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            if (position % 6 == 0) {
                                apps2Activity.leftSwitch();
                                return true;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            if (position == apps.size() - 1 //最后一个
                                    || position % 6 == 5) {//最右一个
                                apps2Activity.rightSwitch();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_title;
        private final View ll_root;

        public Holder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
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
        switch (apps2Activity.getPageType()) {
            case MY_APP_TYPE:
            case RECOMMEND_TYPE:
                binding.remove.setEnabled(false);
                binding.remove.setFocusable(false);
                binding.replace.setEnabled(false);
                binding.replace.setFocusable(false);
                break;
        }
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
        if (apps2Activity != null && !apps2Activity.isFinishing()) {
            PageType pageType = apps2Activity.getPageType();
            switch (pageType) {
                case MY_APP_TYPE:
                    break;
                case VIDEO_TYPE:
                    DataUtils.saveVideoApp(getData());
                    break;
                case MUSIC_TYPE:
                    DataUtils.saveMusicApp(getData());
                    break;
                case RECOMMEND_TYPE:
                    break;
                case FAVORITE_TYPE:
                    DataUtils.saveFavoriteApp(getData());
                    break;
            }
        }
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
