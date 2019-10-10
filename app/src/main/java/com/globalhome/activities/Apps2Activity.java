package com.globalhome.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.globalhome.adapter.App2Adapter;
import com.globalhome.adapter.AppAdapter;
import com.globalhome.utils.DataUtils;
import com.globalhome.utils.PageType;
import com.h3launcher.R;
import com.h3launcher.databinding.ActivityApps2Binding;

/**
 * todo 边界切换  save data  homepage
 */
public class Apps2Activity extends AppCompatActivity {

    private ActivityApps2Binding mBinding;
    /**
     * current page type
     */
    public PageType mPageType = PageType.MY_APP_TYPE;

    Context mContext;
    private App2Adapter mApp2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_apps2);
        mContext = this;

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 6));
        mApp2Adapter = new App2Adapter(this);
        mBinding.recyclerView.setAdapter(mApp2Adapter);
        mBinding.recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        leftSwitch();
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        rightSwitch();
                        return true;
                    }
                }
                return false;
            }
        });

        String type = getIntent().getStringExtra("type");
        mPageType = TextUtils.isEmpty(type) ? PageType.MY_APP_TYPE : PageType.valueOf(type);
//        initData(mPageType);
    }

    //向左边切换
    public void leftSwitch() {
        switch (mPageType) {
            case MY_APP_TYPE:
                initData(PageType.RECOMMEND_TYPE);
                break;
            case VIDEO_TYPE:
                initData(PageType.FAVORITE_TYPE);
                break;
            case MUSIC_TYPE:
                initData(PageType.MY_APP_TYPE);
                break;
            case RECOMMEND_TYPE:
                initData(PageType.VIDEO_TYPE);
                break;
            case FAVORITE_TYPE:
                initData(PageType.MUSIC_TYPE);
                break;
        }
    }

    //向右切换菜单
    public void rightSwitch() {
        switch (mPageType) {
            case MY_APP_TYPE:
                initData(PageType.MUSIC_TYPE);
                break;
            case VIDEO_TYPE:
                initData(PageType.RECOMMEND_TYPE);
                break;
            case MUSIC_TYPE:
                initData(PageType.FAVORITE_TYPE);
                break;
            case RECOMMEND_TYPE:
                initData(PageType.MY_APP_TYPE);
                break;
            case FAVORITE_TYPE:
                initData(PageType.VIDEO_TYPE);
                break;
        }
    }

    private void initData(PageType pageType) {
        mPageType = pageType;
        switch (pageType) {
            case MY_APP_TYPE:
                mBinding.ivTitle.setImageResource(R.drawable.app);
                mBinding.tvTitle.setText("我的应用");
                mBinding.ivNotice.setImageResource(R.drawable.prompt_app);
                mApp2Adapter.setData(DataUtils.getValidAppList(mContext));//todo 没有加号，菜单只有卸载
                break;
            case VIDEO_TYPE:
                mBinding.ivTitle.setImageResource(R.drawable.video);
                mBinding.tvTitle.setText("在线视频");
                mBinding.ivNotice.setImageResource(R.drawable.prompt_video);
                mApp2Adapter.setData(DataUtils.getVideoApp());
                break;
            case MUSIC_TYPE:
                mBinding.ivTitle.setImageResource(R.drawable.music);
                mBinding.tvTitle.setText("音乐");
                mBinding.ivNotice.setImageResource(R.drawable.prompt_music);
                mApp2Adapter.setData(DataUtils.getMusicApp());
                break;
            case RECOMMEND_TYPE:
                mBinding.ivTitle.setImageResource(R.drawable.recommend);
                mBinding.tvTitle.setText("推荐");
                mBinding.ivNotice.setImageResource(R.drawable.prompt_recommend);
                mApp2Adapter.setData(DataUtils.getRecommendApp());
                break;
            case FAVORITE_TYPE:
                mBinding.ivTitle.setImageResource(R.drawable.local);
                mBinding.tvTitle.setText("Favorite");
                mBinding.ivNotice.setImageResource(R.drawable.prompt_local);
                mApp2Adapter.setData(DataUtils.getFavoriteApp());
                break;
        }
    }

    public static void launch(Context context, PageType pageType) {
        Intent intent = new Intent(context, Apps2Activity.class);
        intent.putExtra("type", pageType.name());
        context.startActivity(intent);
    }

    public PageType getPageType() {
        return mPageType;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(mPageType);
    }
}
