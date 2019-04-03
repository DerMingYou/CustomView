package com.ydm.explore.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ydm.explore.R;

/**
 * Description:
 * Data：2019/4/3-17:42
 * Author: DerMing_You
 */
public class GradientTextView extends AppCompatTextView {
    private int mOriginalColor = Color.BLACK;
    private int mChangeColor = Color.BLUE;
    private Paint mOriginalPaint, mChangePaint;
    private int baseLine;
    private float mCurrentProgress;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        mOriginalColor = td.getColor(R.styleable.GradientTextView_originalColor, mOriginalColor);
        mChangeColor = td.getColor(R.styleable.GradientTextView_changeColor, mChangeColor);
        td.recycle();
        //根据颜色获取画笔
        mOriginalPaint = getPaintByColor(mOriginalColor);
        mChangePaint = getPaintByColor(mChangeColor);
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);//防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float middle = mCurrentProgress * getWidth();
        Paint.FontMetricsInt fontMetricsInt = mOriginalPaint.getFontMetricsInt();
        baseLine = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom + getHeight() / 2
                + getPaddingTop() / 2 - getPaddingBottom() / 2;
        //前半部分
        clipRect(canvas, 0, middle, mChangePaint);
        clipRect(canvas, middle, getWidth(), mOriginalPaint);
    }

    private void clipRect(Canvas canvas, float start, float region, Paint paint) {
        //改变的颜色
        canvas.save();
        canvas.clipRect(start + getPaddingLeft(), 0, region, getHeight());
        canvas.drawText(getText().toString(), getPaddingLeft(), baseLine, paint);
        canvas.restore();
    }

    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setOriginalColor(int color) {
        this.mOriginalPaint = getPaintByColor(color);
    }

    public void setChangeColor(int color) {
        this.mChangePaint = getPaintByColor(color);
    }

    /**
     * 设置当前要显示的颜色
     */
    public void setColor(int color) {
        this.mChangePaint = getPaintByColor(color);
        postInvalidate();
    }

    public void start(long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            setCurrentProgress(value);
        });
        animator.start();
    }
}
