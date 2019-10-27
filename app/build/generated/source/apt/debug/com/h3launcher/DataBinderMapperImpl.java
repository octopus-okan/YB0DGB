package com.h3launcher;

import android.databinding.DataBinderMapper;
import android.databinding.DataBindingComponent;
import android.databinding.ViewDataBinding;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import com.h3launcher.databinding.ActivityApps2BindingImpl;
import com.h3launcher.databinding.ActivityMainBindingImpl;
import com.h3launcher.databinding.ActivityMyApplicationBindingImpl;
import com.h3launcher.databinding.ActivityPullBindingImpl;
import com.h3launcher.databinding.ChooseactivityLayoutBindingImpl;
import com.h3launcher.databinding.MenuDialogBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYAPPS2 = 1;

  private static final int LAYOUT_ACTIVITYMAIN = 2;

  private static final int LAYOUT_ACTIVITYMYAPPLICATION = 3;

  private static final int LAYOUT_ACTIVITYPULL = 4;

  private static final int LAYOUT_CHOOSEACTIVITYLAYOUT = 5;

  private static final int LAYOUT_MENUDIALOG = 6;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(6);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.activity_apps2, LAYOUT_ACTIVITYAPPS2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.activity_my_application, LAYOUT_ACTIVITYMYAPPLICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.activity_pull, LAYOUT_ACTIVITYPULL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.chooseactivity_layout, LAYOUT_CHOOSEACTIVITYLAYOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.h3launcher.R.layout.menu_dialog, LAYOUT_MENUDIALOG);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYAPPS2: {
          if ("layout/activity_apps2_0".equals(tag)) {
            return new ActivityApps2BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_apps2 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMYAPPLICATION: {
          if ("layout/activity_my_application_0".equals(tag)) {
            return new ActivityMyApplicationBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_my_application is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYPULL: {
          if ("layout/activity_pull_0".equals(tag)) {
            return new ActivityPullBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_pull is invalid. Received: " + tag);
        }
        case  LAYOUT_CHOOSEACTIVITYLAYOUT: {
          if ("layout/chooseactivity_layout_0".equals(tag)) {
            return new ChooseactivityLayoutBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for chooseactivity_layout is invalid. Received: " + tag);
        }
        case  LAYOUT_MENUDIALOG: {
          if ("layout/menu_dialog_0".equals(tag)) {
            return new MenuDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for menu_dialog is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new com.android.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(6);

    static {
      sKeys.put("layout/activity_apps2_0", com.h3launcher.R.layout.activity_apps2);
      sKeys.put("layout/activity_main_0", com.h3launcher.R.layout.activity_main);
      sKeys.put("layout/activity_my_application_0", com.h3launcher.R.layout.activity_my_application);
      sKeys.put("layout/activity_pull_0", com.h3launcher.R.layout.activity_pull);
      sKeys.put("layout/chooseactivity_layout_0", com.h3launcher.R.layout.chooseactivity_layout);
      sKeys.put("layout/menu_dialog_0", com.h3launcher.R.layout.menu_dialog);
    }
  }
}
