package com.globalhome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.globalhome.data.App;
import com.globalhome.utils.AppMain;
import com.globalhome.utils.GlideMgr;
import com.globalhome.utils.Utils;
import com.h3launcher.R;

import java.util.ArrayList;
import java.util.List;

public class SelectAppAdapter extends BaseAdapter {
    List<App> apps = new ArrayList<>();

    Context mContext;

    public SelectAppAdapter(Context context) {
        mContext = context;
    }
    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
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
            convertView = LayoutInflater.from(AppMain.ctx()).inflate(R.layout.chooseactivity_item, null);
            viewHolder.iv = convertView.findViewById(R.id.activity_icon);
            viewHolder.tv = convertView.findViewById(R.id.activity_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        App app = apps.get(position);
        try {
            GlideMgr.loadNormalDrawableImg(mContext, app.getIcon(), viewHolder.iv);
            viewHolder.tv.setText(app.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
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
