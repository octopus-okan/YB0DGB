package com.globalhome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.globalhome.listener.BaseListener;
import com.globalhome.utils.Utils;

/**
 * tf
 */
public class MediaCardStateReceiver extends BroadcastReceiver {
    private Context mContext;

    public MediaCardStateReceiver(Context context) {
        mContext = context;
    }

    public void onRegister() {
        if (mContext != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
            intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            intentFilter.addDataScheme("file");
            mContext.registerReceiver(this, intentFilter);
        }
    }

    public void onUnregister() {
        if (mContext != null) {
            mContext.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            Utils.log("ACTION_MEDIA_MOUNTED");
            if (listener != null) {
                listener.onUpdate(true);
            }
        } else if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
            Utils.log("ACTION_MEDIA_EJECT");
            if (listener != null) {
                listener.onUpdate(false);
            }
        } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) {
            Utils.log("ACTION_MEDIA_REMOVED");
        }
    }

    BaseListener.OnBoolUpdateListener listener;

    public void setOnUpdateListener(BaseListener.OnBoolUpdateListener listener) {
        this.listener = listener;
    }
}
