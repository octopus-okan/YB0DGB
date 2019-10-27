package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMyApplicationBinding extends ViewDataBinding {
  @NonNull
  public final GridView allapps;

  @NonNull
  public final LinearLayout appLinear;

  @NonNull
  public final ImageView ivLine;

  @NonNull
  public final RelativeLayout layout;

  @NonNull
  public final RelativeLayout notice;

  @NonNull
  public final ImageView okImage;

  @NonNull
  public final ImageView titleImage;

  @NonNull
  public final RelativeLayout titleLinear;

  @NonNull
  public final TextView titleText;

  protected ActivityMyApplicationBinding(Object _bindingComponent, View _root, int _localFieldCount,
      GridView allapps, LinearLayout appLinear, ImageView ivLine, RelativeLayout layout,
      RelativeLayout notice, ImageView okImage, ImageView titleImage, RelativeLayout titleLinear,
      TextView titleText) {
    super(_bindingComponent, _root, _localFieldCount);
    this.allapps = allapps;
    this.appLinear = appLinear;
    this.ivLine = ivLine;
    this.layout = layout;
    this.notice = notice;
    this.okImage = okImage;
    this.titleImage = titleImage;
    this.titleLinear = titleLinear;
    this.titleText = titleText;
  }

  @NonNull
  public static ActivityMyApplicationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_my_application, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMyApplicationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMyApplicationBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_my_application, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMyApplicationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_my_application, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMyApplicationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMyApplicationBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_my_application, null, false, component);
  }

  public static ActivityMyApplicationBinding bind(@NonNull View view) {
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
  public static ActivityMyApplicationBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMyApplicationBinding)bind(component, view, com.h3launcher.R.layout.activity_my_application);
  }
}
