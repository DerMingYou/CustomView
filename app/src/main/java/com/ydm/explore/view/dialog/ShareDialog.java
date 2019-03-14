package com.ydm.explore.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.Tencent;
import com.ydm.explore.AppConstants;
import com.ydm.explore.R;
import com.ydm.explore.util.ShareUiListener;
import com.ydm.explore.view.bean.ShareBean;
import com.ydm.explore.util.DisplayUtils;
import com.ydm.explore.util.FormatUtils;
import com.ydm.explore.util.PhotoBitmapUtils;
import com.ydm.explore.util.ToastUtils;
import com.ydm.explore.util.ToolButtonUtils;
import com.ydm.explore.widget.CustomDialog;

import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * Description:
 * Data：2019/3/14-14:45
 * Author: DerMing_You
 */
public class ShareDialog {
    public static String IMGURL = "http://1jbest-qatest.oss-cn-shanghai.aliyuncs.com/console/label/d1a753242436470a8c9d7b4034df3a02.jpg";
    private static String QQ = "qq";
    private static String QQQONE = "qqQone";
    private static String WEIBO = "weiBo";
    private static String WEIXIN = "weixin";
    private static String WXPY = "pengyouquan";
    private static final int THUMB_SIZE = 150;

    private CustomDialog dialog;
    private Context mContext;
    private Tencent mTencent;
    private ImageView ivQq;
    private IWXAPI api;

    public void show(final Context context, ShareBean shareBean) {
        dialog = new CustomDialog(context, R.style.custom_dialog,
                R.layout.dialog_share, DisplayUtils.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        mContext = context;

        TextView tv_share_title = dialog.findViewById(R.id.tv_share_title);
        TextView tv_QQ = dialog.findViewById(R.id.tv_QQ);
        TextView tv_gone = dialog.findViewById(R.id.tv_gone);
        TextView tv_weixin = dialog.findViewById(R.id.tv_weixin);
        TextView tv_pengyou_quan = dialog.findViewById(R.id.tv_pengyou_quan);
        LinearLayout ll_close = dialog.findViewById(R.id.ll_close);
        if (!TextUtils.isEmpty(shareBean.getShareTitle())) {
            tv_share_title.setText(Html.fromHtml(String.format(shareBean.getShareTitle())));
        }
        //QQ
        tv_QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolButtonUtils.isFastClick()) {
                    return;
                }
                shareQQ(QQ, shareBean);
                cancel();
            }
        });

        //QQ空间
        tv_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolButtonUtils.isFastClick()) {
                    return;
                }
                shareQQ(QQQONE, shareBean);
                cancel();
            }
        });

        //微信
        tv_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolButtonUtils.isFastClick()) {
                    return;
                }
                ShareWeiXin(WEIXIN, shareBean);
                cancel();
            }
        });

        //微信朋友圈
        tv_pengyou_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolButtonUtils.isFastClick()) {
                    return;
                }
                ShareWeiXin(WXPY, shareBean);
                cancel();
            }
        });
        //取消
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        dialog.show();
    }


    //取消弹窗
    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    /**
     * 分享到QQ或者分享到QQ空间
     *
     * @param type      分享的类型
     * @param shareBean 分享的参数
     */
    private void shareQQ(String type, ShareBean shareBean) {
        mTencent = Tencent.createInstance(AppConstants.QQ_APP_ID, mContext);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        if (TextUtils.isEmpty(shareBean.getQqAppName())) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, shareBean.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBean.getSubtitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.getAppUrl());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareBean.getImgUrl());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getString(R.string.app_name));
        } else {//动态指定参数
            params.putString(QQShare.SHARE_TO_QQ_TITLE, shareBean.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBean.getSubtitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.getAppUrl());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareBean.getImgUrl());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareBean.getQqAppName());
        }

        if (type.equals(QQ)) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }

        doShareToQQ(params,shareBean);
    }


    /**
     * 分享到微信或朋友圈
     */
    private void ShareWeiXin(final String type, ShareBean shareBean) {
        new Thread() {
            @Override
            public void run() {
                try {
                    api = WXAPIFactory.createWXAPI(mContext, AppConstants.APP_ID);
                    api.registerApp(AppConstants.APP_ID);

                    // 初始化一个WXWebpageObject对象
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl =shareBean.getAppUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    //                    msg.title = APPNAME;
                    msg.title = shareBean.getTitle();
                    msg.description = shareBean.getSubtitle();
                    Bitmap bmp;
                    if (FormatUtils.isUrl(shareBean.getImgUrl())) {
                        bmp = BitmapFactory.decodeStream(new URL(shareBean.getImgUrl()).openStream());
                    } else {
                        bmp = PhotoBitmapUtils.loadFilePath(shareBean.getImgUrl());
                    }

                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    //                    msg.thumbData = bmpToByteArray(thumbBmp, true);
                    msg.thumbData = bitmap2Bytes(thumbBmp, 32);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();

                    req.transaction = "" + System.currentTimeMillis();
                    req.message = msg;

                    if (type.equals(WEIXIN)) {
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                    } else {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    }
                    // 调用api接口发送数据到微信
                    boolean isSuc = api.sendReq(req);
                    if (!isSuc) {
                        Message message = Message.obtain();
                        message.arg1 = 0;
                        mHandler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                ToastUtils.showShortToast(mContext, mContext.getString(R.string.sorry_no_install_wechat));
            }
        }
    };


    /***************************** qq分享和qq空间分享**********************************/

    private void doShareToQQ(final Bundle params, ShareBean shareBean) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ((Activity) mContext, params
                        , new ShareUiListener(mContext,shareBean.isOtherOpera()));
            }
        });
    }

    /**************************** 微信分享和朋友圈**********************************/

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }
}
