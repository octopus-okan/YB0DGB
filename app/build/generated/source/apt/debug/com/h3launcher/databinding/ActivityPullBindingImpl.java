package com.h3launcher.databinding;
import com.h3launcher.R;
import com.h3launcher.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityPullBindingImpl extends ActivityPullBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.pull_down_contain, 1);
        sViewsWithIds.put(R.id.pull_tag_9301, 2);
        sViewsWithIds.put(R.id.pull_tag_9302, 3);
        sViewsWithIds.put(R.id.pull_tag_9303, 4);
        sViewsWithIds.put(R.id.pull_tag_9304, 5);
        sViewsWithIds.put(R.id.pull_tag_9305, 6);
        sViewsWithIds.put(R.id.pull_tag_9306, 7);
        sViewsWithIds.put(R.id.pull_tag_9307, 8);
        sViewsWithIds.put(R.id.pull_tag_9308, 9);
        sViewsWithIds.put(R.id.pull_tag_9309, 10);
        sViewsWithIds.put(R.id.pull_tag_9310, 11);
        sViewsWithIds.put(R.id.pull_tag_9311, 12);
        sViewsWithIds.put(R.id.pull_tag_9312, 13);
        sViewsWithIds.put(R.id.pull_tag_9313, 14);
        sViewsWithIds.put(R.id.pull_tag_9314, 15);
        sViewsWithIds.put(R.id.pull_tag_9315, 16);
        sViewsWithIds.put(R.id.pull_tag_9316, 17);
        sViewsWithIds.put(R.id.pull_mianup, 18);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPullBindingImpl(@Nullable android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private ActivityPullBindingImpl(android.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.RelativeLayout) bindings[1]
            , (com.globalhome.views.MainUpView) bindings[18]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.ImageView) bindings[14]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[16]
            , (android.widget.ImageView) bindings[17]
            );
        this.activityPull.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}