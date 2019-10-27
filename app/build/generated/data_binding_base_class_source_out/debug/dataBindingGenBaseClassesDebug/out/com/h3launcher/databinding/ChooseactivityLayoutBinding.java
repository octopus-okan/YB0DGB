package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ChooseactivityLayoutBinding extends ViewDataBinding {
  @NonNull
  public final ListView chooseappListview;

  @NonNull
  public final LinearLayout dialogChossapp;

  @NonNull
  public final LinearLayout linear1;

  @NonNull
  public final LinearLayout linear2;

  @NonNull
  public final LinearLayout linear3;

  @NonNull
  public final TextView title;

  protected ChooseactivityLayoutBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ListView chooseappListview, LinearLayout dialogChossapp, LinearLayout linear1,
      LinearLayout linear2, LinearLayout linear3, TextView title) {
    super(_bindingComponent, _root, _localFieldCount);
    this.chooseappListview = chooseappListview;
    this.dialogChossapp = dialogChossapp;
    this.linear1 = linear1;
    this.linear2 = linear2;
    this.linear3 = linear3;
    this.title = title;
  }

  @NonNull
  public static ChooseactivityLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.chooseactivity_layout, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ChooseactivityLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ChooseactivityLayoutBinding>inflateInternal(inflater, com.h3launcher.R.layout.chooseactivity_layout, root, attachToRoot, component);
  }

  @NonNull
  public static ChooseactivityLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.chooseactivity_layout, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ChooseactivityLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ChooseactivityLayoutBinding>inflateInternal(inflater, com.h3launcher.R.layout.chooseactivity_layout, null, false, component);
  }

  public static ChooseactivityLayoutBinding bind(@NonNull View view) {
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
  public static ChooseactivityLayoutBinding bind(@NonNull View view, @Nullable Object component) {
    return (ChooseactivityLayoutBinding)bind(component, view, com.h3launcher.R.layout.chooseactivity_layout);
  }
}
