package com.ydm.explore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.ydm.explore.R;
import com.ydm.explore.base.BaseActivity;
import com.ydm.explore.util.ShareUiListener;
import com.ydm.explore.util.eventbus.ShareEventBus;
import com.ydm.explore.view.bean.ShareBean;
import com.ydm.explore.view.dialog.ShareDialog;
import com.ydm.explore.widget.CustomToast;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
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
    public int getLayoutId() {
        return R.layout.activity_open_book;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void initData() {

    }

    @Subscribe
    public void onEventMainThread(ShareEventBus event) {
        if (event.isOtherOpera()) {
            new CustomToast(mContext, "0983335693");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new ShareUiListener(mContext));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_empty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_empty:
                String encodeUrl = "www.baidu.com";
                String shareImageUrl = "http://1jbest-qatest.oss-cn-shanghai.aliyuncs.com/console/label/d1a753242436470a8c9d7b4034df3a02.jpg";
                new ShareDialog().show(mContext, new ShareBean(getString(R.string.share_title), getString(R.string.share_sub_title), shareImageUrl, encodeUrl));
                break;
        }
    }
}
