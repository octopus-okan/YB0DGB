package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MenuDialogBinding extends ViewDataBinding {
  @NonNull
  public final Button add;

  @NonNull
  public final Button del;

  @NonNull
  public final LinearLayout linear;

  @NonNull
  public final Button remove;

  @NonNull
  public final Button replace;

  protected MenuDialogBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button add, Button del, LinearLayout linear, Button remove, Button replace) {
    super(_bindingComponent, _root, _localFieldCount);
    this.add = add;
    this.del = del;
    this.linear = linear;
    this.remove = remove;
    this.replace = replace;
  }

  @NonNull
  public static MenuDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.menu_dialog, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MenuDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MenuDialogBinding>inflateInternal(inflater, com.h3launcher.R.layout.menu_dialog, root, attachToRoot, component);
  }

  @NonNull
  public static MenuDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.menu_dialog, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MenuDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MenuDialogBinding>inflateInternal(inflater, com.h3launcher.R.layout.menu_dialog, null, false, component);
  }

  public static MenuDialogBinding bind(@NonNull View view) {
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
  public static MenuDialogBinding bind(@NonNull View view, @Nullable Object component) {
    return (MenuDialogBinding)bind(component, view, com.h3launcher.R.layout.menu_dialog);
  }
}
