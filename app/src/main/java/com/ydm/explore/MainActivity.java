package com.ydm.explore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ydm.explore.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_picture_wall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_picture_wall:
                PictureWallActivity.launch(mContext);
                break;
        }
    }
}
