package com.h3launcher.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.globalhome.views.MainUpView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityPullBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout activityPull;

  @NonNull
  public final RelativeLayout pullDownContain;

  @NonNull
  public final MainUpView pullMianup;

  @NonNull
  public final ImageView pullTag9301;

  @NonNull
  public final ImageView pullTag9302;

  @NonNull
  public final ImageView pullTag9303;

  @NonNull
  public final ImageView pullTag9304;

  @NonNull
  public final ImageView pullTag9305;

  @NonNull
  public final ImageView pullTag9306;

  @NonNull
  public final ImageView pullTag9307;

  @NonNull
  public final ImageView pullTag9308;

  @NonNull
  public final ImageView pullTag9309;

  @NonNull
  public final ImageView pullTag9310;

  @NonNull
  public final ImageView pullTag9311;

  @NonNull
  public final ImageView pullTag9312;

  @NonNull
  public final ImageView pullTag9313;

  @NonNull
  public final ImageView pullTag9314;

  @NonNull
  public final ImageView pullTag9315;

  @NonNull
  public final ImageView pullTag9316;

  protected ActivityPullBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout activityPull, RelativeLayout pullDownContain, MainUpView pullMianup,
      ImageView pullTag9301, ImageView pullTag9302, ImageView pullTag9303, ImageView pullTag9304,
      ImageView pullTag9305, ImageView pullTag9306, ImageView pullTag9307, ImageView pullTag9308,
      ImageView pullTag9309, ImageView pullTag9310, ImageView pullTag9311, ImageView pullTag9312,
      ImageView pullTag9313, ImageView pullTag9314, ImageView pullTag9315, ImageView pullTag9316) {
    super(_bindingComponent, _root, _localFieldCount);
    this.activityPull = activityPull;
    this.pullDownContain = pullDownContain;
    this.pullMianup = pullMianup;
    this.pullTag9301 = pullTag9301;
    this.pullTag9302 = pullTag9302;
    this.pullTag9303 = pullTag9303;
    this.pullTag9304 = pullTag9304;
    this.pullTag9305 = pullTag9305;
    this.pullTag9306 = pullTag9306;
    this.pullTag9307 = pullTag9307;
    this.pullTag9308 = pullTag9308;
    this.pullTag9309 = pullTag9309;
    this.pullTag9310 = pullTag9310;
    this.pullTag9311 = pullTag9311;
    this.pullTag9312 = pullTag9312;
    this.pullTag9313 = pullTag9313;
    this.pullTag9314 = pullTag9314;
    this.pullTag9315 = pullTag9315;
    this.pullTag9316 = pullTag9316;
  }

  @NonNull
  public static ActivityPullBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_pull, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityPullBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityPullBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_pull, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityPullBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_pull, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityPullBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityPullBinding>inflateInternal(inflater, com.h3launcher.R.layout.activity_pull, null, false, component);
  }

  public static ActivityPullBinding bind(@NonNull View view) {
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
  public static ActivityPullBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityPullBinding)bind(component, view, com.h3launcher.R.layout.activity_pull);
  }
}
