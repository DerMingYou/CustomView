package com.ydm.explore.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:
 * Data：2019/3/6-14:42
 * Author: DerMing_You
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected View mContentView;
    public Activity mContext;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setBaseView(getLayoutId());

        initView(savedInstanceState, mContentView);

        initData();
    }

    @SuppressLint("ResourceType")
    protected void setBaseView(@LayoutRes int layoutId) {
        if (layoutId <= 0) return;
        setContentView(mContentView = LayoutInflater.from(this).inflate(layoutId, null));
        mUnbinder = ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    public abstract void initView(final Bundle savedInstanceState, final View contentView);

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
