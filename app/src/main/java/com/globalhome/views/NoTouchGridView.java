package com.globalhome.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class NoTouchGridView extends GridView {
    public NoTouchGridView(Context context) {
        super(context);
    }

    public NoTouchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return true;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
