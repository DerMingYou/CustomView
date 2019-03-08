package com.ydm.explore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ydm.explore.adapter.BookOpenModeAdapter;
import com.ydm.explore.animation.ContentScaleAnimation;
import com.ydm.explore.animation.Rotate3DAnimation;
import com.ydm.explore.base.BaseActivity;
import com.ydm.explore.bean.OpenBookBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 掌阅小说打开方式
 * Data：2019/3/6-16:32
 * Author: DerMing_You
 */
public class BookOpenModeActivity extends BaseActivity implements Animation.AnimationListener {

    @BindView(R.id.rv_book)
    RecyclerView rvBook;
    private BookOpenModeAdapter bookOpenModeAdapter;
    private ArrayList<OpenBookBean> contentList = new ArrayList<>();

    // 记录View的位置
    private int[] location = new int[2];
    // 内容页
    private ImageView mContent;
    // 封面
    private ImageView mFirst;
    // 缩放动画
    private ContentScaleAnimation scaleAnimation;
    // 3D旋转动画
    private Rotate3DAnimation threeDAnimation;
    // 状态栏的高度
    private int statusHeight;
    // 是否打开书籍 其实是是否离开当前界面，跳转到其他的界面
    private boolean isOpenBook = false;

    public static void launch(Context context) {
        Intent intent = new Intent(context, BookOpenModeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_open_mode;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        // 获取状态栏高度
        statusHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusHeight = getResources().getDimensionPixelSize(resourceId);
        }

        rvBook.setLayoutManager(new GridLayoutManager(mContext, 2));
        bookOpenModeAdapter = new BookOpenModeAdapter(contentList, mContext);
        rvBook.setAdapter(bookOpenModeAdapter);

        bookOpenModeAdapter.setOnItemClickListener((parent, view, position, id) -> {
            mFirst.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.VISIBLE);

            // 计算当前的位置坐标
            view.getLocationInWindow(location);
            int width = view.getWidth();
            int height = view.getHeight();

            // 两个ImageView设置大小和位置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFirst.getLayoutParams();
            params.leftMargin = location[0];
            params.topMargin = location[1] - statusHeight;
            params.width = width;
            params.height = height;
            mFirst.setLayoutParams(params);
            mContent.setLayoutParams(params);

            //mContent = new ImageView(MainActivity.this);
            Bitmap contentBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            contentBitmap.eraseColor(getResources().getColor(R.color.colorPrimary));
            mContent.setImageBitmap(contentBitmap);

            // mCover = new ImageView(MainActivity.this);
            Bitmap coverBitmap = BitmapFactory.decodeResource(getResources(),contentList.get(position).getImage());
            mFirst.setImageBitmap(coverBitmap);

            initAnimation(view);

            mContent.clearAnimation();
            mContent.startAnimation(scaleAnimation);
            mFirst.clearAnimation();
            mFirst.startAnimation(threeDAnimation);
        });
    }

    @Override
    public void initData() {
        Integer[] images = new Integer[]{R.mipmap.loading_01, R.mipmap.loading_02, R.mipmap.loading_03,
                R.mipmap.loading_04, R.mipmap.loading_05, R.mipmap.loading_06};
        String[] nameString = new String[]{"三国", "红楼", "西游", "水浒", "论语", "孟子"};
        for (int i = 0; i < 6; i++){
            OpenBookBean bookBean = new OpenBookBean();
            bookBean.setImage(images[i]);
            bookBean.setName(nameString[i]);
            contentList.add(bookBean);
        }
        bookOpenModeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(scaleAnimation.hasEnded() && threeDAnimation.hasEnded()) {
            // 两个动画都结束的时候再处理后续操作
            if (!isOpenBook) {
                isOpenBook = true;
                OpenBookActivity.launch(mContext);
            } else {
                isOpenBook = false;
                mFirst.clearAnimation();
                mContent.clearAnimation();
                mFirst.setVisibility(View.GONE);
                mContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void initAnimation(View view) {
        float viewWidth = view.getWidth();
        float viewHeight = view.getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float maxWidth = displayMetrics.widthPixels;
        float maxHeight = displayMetrics.heightPixels;
        float horScale = maxWidth / viewWidth;
        float verScale = maxHeight / viewHeight;
        float scale = horScale > verScale ? horScale : verScale;

        scaleAnimation = new ContentScaleAnimation(location[0], location[1], scale, false);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());  //设置插值器
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);  //动画停留在最后一帧
        scaleAnimation.setAnimationListener(BookOpenModeActivity.this);

        threeDAnimation = new Rotate3DAnimation(mContext, -180, 0
                , location[0], location[1], scale, true);
        threeDAnimation.setDuration(1000);                         //设置动画时长
        threeDAnimation.setFillAfter(true);                        //保持旋转后效果
        threeDAnimation.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 当界面重新进入的时候进行合书的动画
        if(isOpenBook) {
            scaleAnimation.reverse();
            threeDAnimation.reverse();
            mFirst.clearAnimation();
            mFirst.startAnimation(threeDAnimation);
            mContent.clearAnimation();
            mContent.startAnimation(scaleAnimation);
        }
    }
}
