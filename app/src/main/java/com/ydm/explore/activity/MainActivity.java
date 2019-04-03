package com.ydm.explore.activity;

import android.os.Bundle;
import android.view.View;

import com.ydm.explore.R;
import com.ydm.explore.base.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_picture_wall, R.id.tv_book, R.id.tv_custom_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_picture_wall:
                PictureWallActivity.launch(mContext);
                break;
            case R.id.tv_book:
                BookOpenModeActivity.launch(mContext);
                break;
            case R.id.tv_custom_view:
                CustomViewListActivity.launch(mContext);
                break;
        }
    }
}
