package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.globalhome.views.MainUpView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final ImageView bgAdd1;

  @NonNull
  public final ImageView bgAdd10;

  @NonNull
  public final ImageView bgAdd11;

  @NonNull
  public final ImageView bgAdd12;

  @NonNull
  public final ImageView bgAdd2;

  @NonNull
  public final ImageView bgAdd3;

  @NonNull
  public final ImageView bgAdd4;

  @NonNull
  public final ImageView bgAdd5;

  @NonNull
  public final ImageView bgAdd6;

  @NonNull
  public final ImageView bgAdd7;

  @NonNull
  public final ImageView bgAdd8;

  @NonNull
  public final ImageView bgAdd9;

  @NonNull
  public final ImageView bgIv1;

  @NonNull
  public final ImageView bgIv2;

  @NonNull
  public final ImageView bgIv3;

  @NonNull
  public final ImageView bgIv4;

  @NonNull
  public final ImageView bgIv5;

  @NonNull
  public final ImageView bgIv6;

  @NonNull
  public final ImageView bgIv7;

  @NonNull
  public final ImageView bgIv8;

  @NonNull
  public final FrameLayout fl1;

  @NonNull
  public final FrameLayout fl2;

  @NonNull
  public final FrameLayout fl3;

  @NonNull
  public final FrameLayout fl4;

  @NonNull
  public final FrameLayout fl5;

  @NonNull
  public final FrameLayout fl6;

  @NonNull
  public final FrameLayout fl7;

  @NonNull
  public final FrameLayout fl8;

  @NonNull
  public final FrameLayout flAdd1;

  @NonNull
  public final FrameLayout flAdd10;

  @NonNull
  public final FrameLayout flAdd11;

  @NonNull
  public final FrameLayout flAdd12;

  @NonNull
  public final FrameLayout flAdd2;

  @NonNull
  public final FrameLayout flAdd3;

  @NonNull
  public final FrameLayout flAdd4;

  @NonNull
  public final FrameLayout flAdd5;

  @NonNull
  public final FrameLayout flAdd6;

  @NonNull
  public final FrameLayout flAdd7;

  @NonNull
  public final FrameLayout flAdd8;

  @NonNull
  public final FrameLayout flAdd9;

  @NonNull
  public final ImageView imsetting;

  @NonNull
  public final ImageView ivBg;

  @NonNull
  public final ImageView ivTf;

  @NonNull
  public final ImageView ivUsb;

  @NonNull
  public final LinearLayout llStatus;

  @NonNull
  public final RelativeLayout mainRl;

  @NonNull
  public final MainUpView mainUpView;

  @NonNull
  public final ImageView netIv;

  @NonNull
  public final TextView timeTv;

  @NonNull
  public final TextView titleTv1;

  @NonNull
  public final TextView titleTv2;

  @NonNull
  public final TextView titleTv3;

  @NonNull
  public final TextView titleTv5;

  @NonNull
  public final TextView titleTv6;

  @NonNull
  public final RelativeLayout topInfo;

  @NonNull
  public final TextView tvDate;

  protected ActivityMainBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView bgAdd1, ImageView bgAdd10, ImageView bgAdd11, ImageView bgAdd12, ImageView bgAdd2,
      ImageView bgAdd3, ImageView bgAdd4, ImageView bgAdd5, ImageView bgAdd6, ImageView bgAdd7,
      ImageView bgAdd8, ImageView bgAdd9, ImageView bgIv1, ImageView bgIv2, ImageView bgIv3,
      ImageView bgIv4, ImageView bgIv5, ImageView bgIv6, ImageView bgIv7, ImageView bgIv8,
      FrameLayout fl1, FrameLayout fl2, FrameLayout fl3, FrameLayout fl4, FrameLayout fl5,
      FrameLayout fl6, FrameLayout fl7, FrameLayout fl8, FrameLayout flAdd1, FrameLayout flAdd10,
      FrameLayout flAdd11, FrameLayout flAdd12, FrameLayout flAdd2, FrameLayout flAdd3,
      FrameLayout flAdd4, FrameLayout flAdd5, FrameLayout flAdd6, FrameLayout flAdd7,
      FrameLayout flAdd8, FrameLayout flAdd9, ImageView imsetting, ImageView ivBg, ImageView ivTf,
      ImageView ivUsb, LinearLayout llStatus, RelativeLayout mainRl, MainUpView mainUpView,
      ImageView netIv, TextView timeTv, TextView titleTv1, TextView titleTv2, TextView titleTv3,
      TextView titleTv5, TextView titleTv6, RelativeLayout topInfo, TextView tvDate) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bgAdd1 = bgAdd1;
    this.bgAdd10 = bgAdd10;
    this.bgAdd11 = bgAdd11;
    this.bgAdd12 = bgAdd12;
    this.bgAdd2 = bgAdd2;
    this.bgAdd3 = bgAdd3;
    this.bgAdd4 = bgAdd4;
    this.bgAdd5 = bgAdd5;
    this.bgAdd6 = bgAdd6;
    this.bgAdd7 = bgAdd7;
    this.bgAdd8 = bgAdd8;
    this.bgAdd9 = bgAdd9;
    this.bgIv1 = bgIv1;
    this.bgIv2 = bgIv2;
    this.bgIv3 = bgIv3;
    this.bgIv4 = bgIv4;
    this.bgIv5 = bgIv5;
    this.bgIv6 = bgIv6;
    this.bgIv7 = bgIv7;
    this.bgIv8 = bgIv8;
    this.fl1 = fl1;
    this.fl2 = fl2;
    this.fl3 = fl3;
    this.fl4 = fl4;
    this.fl5 = fl5;
    this.fl6 = fl6;
    this.fl7 = fl7;
    this.fl8 = fl8;
    this.flAdd1 = flAdd1;
    this.flAdd10 = flAdd10;
    this.flAdd11 = flAdd11;
    this.flAdd12 = flAdd12;
    this.flAdd2 = flAdd2;
    this.flAdd3 = flAdd3;
    this.flAdd4 = flAdd4;
    this.flAdd5 = flAdd5;
    this.flAdd6 = flAdd6;
    this.flAdd7 = flAdd7;
    this.flAdd8 = flAdd8;
    this.flAdd9 = flAdd9;
    this.imsetting = imsetting;
    this.ivBg = ivBg;
    this.ivTf = ivTf;
    this.ivUsb = ivUsb;
    this.llStatus = llStatus;
    this.mainRl = mainRl;
    this.mainUpView = mainUpView;
    this.netIv = netIv;
    this.timeTv = timeTv;
    this.titleTv1 = titleTv1;
    this.titleTv2 = titleTv2;
    this.titleTv3 = titleTv3;
    this.titleTv5 = titleTv5;
    this.titleTv6 = titleTv6;
    this.topInfo = topInfo;
    this.tvDate = tvDate;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_main, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMainBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_main, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMainBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityMainBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMainBinding)bind(component, view, com.h3launcher.R.layout.activity_main);
  }
}
