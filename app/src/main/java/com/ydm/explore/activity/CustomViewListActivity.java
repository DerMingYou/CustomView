package com.ydm.explore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ydm.explore.R;
import com.ydm.explore.base.BaseActivity;
import com.ydm.explore.view.dialog.CustomEditDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2019/4/3-17:01
 * Author: DerMing_You
 */
public class CustomViewListActivity extends BaseActivity {

    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    @BindView(R.id.tv_book)
    TextView tvBook;
    @BindView(R.id.tv_custom_view)
    TextView tvCustomView;
    private CustomEditDialog mDialog;

    public static void launch(Context context) {
        Intent intent = new Intent(context, CustomViewListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_view_list;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        mDialog = new CustomEditDialog(mContext, new CustomEditDialog.OnSelectListener() {
            @Override
            public void onClick(String name) {
                tvDialog.setText(name);
                mDialog.cancelDialog();
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_dialog, R.id.tv_book, R.id.tv_custom_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog:
                mDialog.show(getString(R.string.custom_dialog));
                break;
            case R.id.tv_book:
                break;
            case R.id.tv_custom_view:
                break;
        }
    }
}
