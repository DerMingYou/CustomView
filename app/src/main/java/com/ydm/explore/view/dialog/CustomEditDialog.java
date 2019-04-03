package com.ydm.explore.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ydm.explore.R;
import com.ydm.explore.util.CommonUtils;
import com.ydm.explore.util.DisplayUtils;
import com.ydm.explore.widget.EditTextWithClear;
import com.ydm.explore.widget.CustomDialog;

/**
 * Description:
 * Data：2019/4/3-17:13
 * Author: DerMing_You
 */
public class CustomEditDialog {

    private CustomDialog dialog;
    private Context mContext;
    private EditTextWithClear mEtContent;
    private OnSelectListener mOnSelectListener;

    public EditTextWithClear getmEtContent() {
        return mEtContent;
    }

    public void setmEtContent(EditTextWithClear mEtContent) {
        this.mEtContent = mEtContent;
    }

    public CustomEditDialog(Context context, OnSelectListener onSelectListener) {
        dialog = new CustomDialog(context, R.style.custom_dialog,
                R.layout.dialog_photo_group_edit, DisplayUtils.getScreenWidth(context)
                , ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mContext = context;
        mOnSelectListener = onSelectListener;
    }

    public void show(String name) {
        mEtContent = dialog.findViewById(R.id.et_content);
        TextView tv_confirm = dialog.findViewById(R.id.tv_confirm);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        mEtContent.setText(name);
        mEtContent.setSelection(name.length());
        //确定
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectListener.onClick(CommonUtils.getEdtText(mEtContent));
            }
        });
        //取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        dialog.show();
    }


    /**
     * 取消弹窗
     */
    public void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface OnSelectListener {
        void onClick(String name);
    }
}
