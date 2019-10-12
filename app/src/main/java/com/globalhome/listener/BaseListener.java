package com.globalhome.listener;

public class BaseListener {
    public interface OnBoolUpdateListener {
        void onUpdate(boolean result);
    }
    public interface OnIntUpdateListener {
        void onUpdate(int result);
    }

    public interface OnStringUpdateListener {
        void onUpdate(String result);
    }
}
