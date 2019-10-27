package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public abstract class ActivityApps2Binding extends ViewDataBinding {
  @NonNull
  public final LinearLayout appLinear;

  @NonNull
  public final GridView gridView;

  @NonNull
  public final ImageView ivLine;

  @NonNull
  public final ImageView ivNotice;

  @NonNull
  public final ImageView ivTitle;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final RelativeLayout titleLinear;

  @NonNull
  public final TextView tvTitle;

  protected ActivityApps2Binding(Object _bindingComponent, View _root, int _localFieldCount,
      LinearLayout appLinear, GridView gridView, ImageView ivLine, ImageView ivNotice,
      ImageView ivTitle, RecyclerView recyclerView, RelativeLayout titleLinear, TextView tvTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appLinear = appLinear;
    this.gridView = gridView;
    this.ivLine = ivLine;
    this.ivNotice = ivNotice;
    this.ivTitle = ivTitle;
    this.recyclerView = recyclerView;
    this.titleLinear = titleLinear;
    this.tvTitle = tvTitle;
  }

  @NonNull
  public static ActivityApps2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_apps2, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityApps2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityApps2Binding>inflateInternal(inflater, com.h3launcher.R.layout.activity_apps2, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityApps2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_apps2, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityApps2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityApps2Binding>inflateInternal(inflater, com.h3launcher.R.layout.activity_apps2, null, false, component);
  }

  public static ActivityApps2Binding bind(@NonNull View view) {
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
  public static ActivityApps2Binding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityApps2Binding)bind(component, view, com.h3launcher.R.layout.activity_apps2);
  }
}
