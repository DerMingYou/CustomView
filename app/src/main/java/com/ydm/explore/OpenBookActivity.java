package com.ydm.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ydm.explore.base.BaseActivity;
import com.ydm.explore.widget.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Data：2019/3/6-17:54
 * Author: DerMing_You
 */
public class OpenBookActivity extends BaseActivity {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    public static void launch(Context context) {
        Intent intent = new Intent(context, OpenBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_book;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_empty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_empty:
                new CustomToast(mContext, "0983335693");
                break;
        }
    }
}
