package com.ydm.explore.util;

import android.content.Context;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ydm.explore.util.eventbus.ShareEventBus;

import org.greenrobot.eventbus.EventBus;

/**
 * Description:
 * Data：2019/3/14-15:03
 * Author: DerMing_You
 */
public class ShareUiListener implements IUiListener {

    private final Context mContext;
    private boolean mOtherOpera;

    public ShareUiListener(Context context){
        mContext=context;
    }

    /**
     *
     * @param context
     * @param otherOpera  true 分享成功有其他操作   false 没有其他操作
     */
    public ShareUiListener(Context context,boolean otherOpera){
        mContext=context;
        mOtherOpera=otherOpera;
    }
    /**
     * 分享成功
     *
     * @param o
     */
    @Override
    public void onComplete(Object o) {
        if (!mOtherOpera){
            ToastUtils.showShortToast(mContext, "分享成功");
        }else {
            EventBus.getDefault().post(new ShareEventBus(true));
        }

    }

    /**
     * 分享取消
     */
    @Override
    public void onCancel() {
        ToastUtils.showShortToast(mContext, "取消分享");
    }

    /**
     * 分享异常
     *
     * @param uiError
     */
    @Override
    public void onError(UiError uiError) {
        ToastUtils.showShortToast(mContext, uiError.errorMessage);
    }
}
