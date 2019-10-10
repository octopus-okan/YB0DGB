package com.globalhome.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.globalhome.activities.MainActivity;
import com.globalhome.utils.Utils;
import com.h3launcher.R;
import com.globalhome.data.App;
import com.globalhome.utils.AppMain;
import com.globalhome.utils.GlideMgr;

public class AppAdapter extends BaseAdapter {

    private SparseArray<App> apps = new SparseArray<>();

    private int listMode = 0;
    int i = 0;

    public Context mContext;

    public AppAdapter(Context context) {
        mContext = context;
    }

    /**
     * 0 GridView activity
     * 1 ListView 弹窗
     * */
    public void setListMode(int listMode) {
        this.listMode = listMode;
    }

    public void setApps(SparseArray<App> apps) {
        if (this.apps != null) {
            this.apps.clear();
            this.apps = null;
        }
        if (apps != null) {
            this.apps = apps.clone();
        }
    }

    @Override
    public int getCount() {
        return this.apps != null ? this.apps.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (this.listMode) {
                case 0:
                    convertView = LayoutInflater.from(AppMain.ctx()).inflate(R.layout.apps_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.app_icon);
                    viewHolder.tv = convertView.findViewById(R.id.app_title);
                    convertView.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_MENU) {
                                Utils.showToast("dialog");
                                return true;
                            }
                            return false;
                        }
                    });
                    break;
                case 1:
                    convertView = LayoutInflater.from(AppMain.ctx()).inflate(R.layout.chooseactivity_item, null);
                    viewHolder.iv = convertView.findViewById(R.id.activity_icon);
                    viewHolder.tv = convertView.findViewById(R.id.activity_title);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        App app = apps.get(position);
        try {
            Log.d("xiaolp","app = "+app);
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), viewHolder.iv);
            viewHolder.tv.setText(app.getName());
        }catch (Exception e){
            e.printStackTrace();
        }

//        Log.e("vvvvvv","po = "+position+ "  i="+ i++);

//        viewHolder.iv.setImageDrawable(app.getIcon());

        return convertView;
    }

    public void release() {
        if (apps != null) {
            apps.clear();
            apps = null;
        }
    }


}
