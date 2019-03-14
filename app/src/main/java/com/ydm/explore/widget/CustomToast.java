package com.ydm.explore.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ydm.explore.R;

/**
 * Description:
 * Data：2019/3/12-11:25
 * Author: DerMing_You
 */
public class CustomToast {
    private final Toast toast;

    /**
     *
     * @param context
     * @param phoneNum
     */
    public CustomToast(Context context, String phoneNum) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0); // Toast显示的位置
        toast.setDuration(Toast.LENGTH_SHORT); // Toast显示的时间
        toast.setView(view);

        View monitorView = toast.getView();
        monitorView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View v) {
                // 去拨号
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                Uri data = Uri.parse("tel:" + phoneNum);
//                intent.setData(data);
//                context.startActivity(intent);
            }

            @Override
            public void onViewAttachedToWindow(View v) {
                Log.d("CustomToast = ", "显示");

            }
        });
        toast.show();
    }

    public interface OnSelectListener {
        void onShow(View view);
    }
}
