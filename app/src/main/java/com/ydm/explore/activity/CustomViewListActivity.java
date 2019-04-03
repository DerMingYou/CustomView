package com.ydm.explore.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydm.explore.R;
import com.ydm.explore.base.BaseActivity;
import com.ydm.explore.view.GradientTextView;
import com.ydm.explore.view.dialog.CustomEditDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.iv_bluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.iv_fore_bluetooth)
    ImageView ivForeBluetooth;
    @BindView(R.id.rl_wrap_bluetooth)
    RelativeLayout rlWrapBluetooth;
    @BindView(R.id.tv_search_range_finder)
    GradientTextView tvSearchRangeFinder;
    @BindView(R.id.theFirstRow)
    LinearLayout theFirstRow;
    @BindView(R.id.tv_search_desc)
    TextView tvSearchDesc;
    private CustomEditDialog mDialog;

    public Timer timer = new Timer(true);

    public TimerTask timerTask;
    private AnimationDrawable animationDrawable;

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

        animationDrawable = (AnimationDrawable) ivBluetooth.getDrawable();
    }

    @Override
    public void initData() {

    }

    /**
     * 开始搜索蓝牙时显示文字颜色滚动效果的循环器
     */
    public void startTimerTaskForTextView() {
        if (timer != null) {
            if (timerTask != null) {
                //将原任务从队列中移除
                timerTask.cancel();
            }

            timerTask = new MyTimerTask(); // 新建一个任务
            timer.schedule(timerTask, 0, 3000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (tvSearchRangeFinder != null) {
                    tvSearchRangeFinder.start(3000);
                }
            });
        }
    }

    /**
     * 结束搜索蓝牙时显示文字颜色滚动效果的循环器
     */
    public void stopTimerTaskForTextView() {
        if (timer != null) {
            if (timerTask != null) {
                //将原任务从队列中移除
                timerTask.cancel();
            }
        }
    }

    public void startAnimForScanBluetooth() {
        //显示“搜索测距仪”，有颜色滚动效果
        tvSearchRangeFinder.setText(getString(R.string.search_range_finder));
        tvSearchRangeFinder.setChangeColor(ContextCompat.getColor(this, R.color.orange));
        //这是属性动画，只有效果时长，所以要循环，就要使用循环方法
        startTimerTaskForTextView();

        //提示“正在扫描”
        tvSearchDesc.setText("正在扫描");

        //播放蓝牙图标的帧动画
        animationDrawable.start();
        //因为不知道怎么控制帧动画停止时显示要的那一张，就用上面覆盖一张来显示动画停止时要显示的那张
        //不可直接在显示动画那个ImageView上设置某张图，因为在显示动画时就会崩溃
        ivForeBluetooth.setVisibility(View.GONE);
    }

    public void stopAnimForScanBluetoothSuccess() {
        stopTimerTaskForTextView();
        tvSearchRangeFinder.setText("连接成功");
        tvSearchRangeFinder.setColor(ContextCompat.getColor(this, R.color.orange));

        tvSearchDesc.setText("点击断开连接");

        animationDrawable.stop();
        ivForeBluetooth.setVisibility(View.VISIBLE);
        ivForeBluetooth.setImageResource(R.mipmap.icon_my_bluetooth_3);
    }


    @OnClick({R.id.tv_dialog, R.id.tv_book, R.id.tv_custom_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog:
                mDialog.show(getString(R.string.custom_dialog));
                break;
            case R.id.tv_book:
                startAnimForScanBluetooth();
                new Handler().postDelayed(() -> {
                    stopAnimForScanBluetoothSuccess();
                }, 5000);
                break;
            case R.id.tv_custom_view:
                break;
        }
    }
}
