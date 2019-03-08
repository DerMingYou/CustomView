package com.ydm.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ydm.explore.base.BaseActivity;

/**
 * Description:
 * Data：2019/3/6-17:54
 * Author: DerMing_You
 */
public class OpenBookActivity extends BaseActivity {

    public static void launch(Context context){
        Intent intent = new Intent(context,OpenBookActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void initData() {

    }
}
