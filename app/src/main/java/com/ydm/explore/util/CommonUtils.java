package com.ydm.explore.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydm.explore.AppConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * Data：2019/4/3-17:15
 * Author: DerMing_You
 */
public class CommonUtils {
    /**
     * 获取颜色
     *
     * @param context
     * @param id
     */
    public static int getColor(Context context, int id) {
        int color = ContextCompat.getColor(context, id);
        return color;
    }

    /**
     * 获取图片
     *
     * @param context
     * @param id
     */
    public static Drawable getDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        return drawable;
    }

    /**
     * 从资源文件中获取图片（转换成byte[]）
     *
     * @param context    上下文
     * @param drawableId 资源文件id
     * @return
     */
    public static byte[] gainBitmap(Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                drawableId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 保留两位小数
     */
    public static String format(String price) {
        double temp = 0;
        if (!TextUtils.isEmpty(price)) {
            try {
                temp = Double.valueOf(price);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return getPriceString(temp);
    }

    /**
     * 获取价格字符串
     */
    public static String getPriceString(double price) {

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price).toString();
    }

    /**
     * 获取 去除首尾空格 的文字
     *
     * @param edt
     * @return
     */
    public static String getEdtText(EditText edt) {
        if (null == edt) {
            return "";
        }
        return edt.getText().toString().trim();
    }

    /**
     * 是否为空
     *
     * @param edt
     * @return
     */
    public static boolean isEdtEmpty(EditText edt) {
        if (TextUtils.isEmpty(getEdtText(edt))) {
            return true;
        }
        return false;
    }

    /**
     * 设置文本的HTML
     *
     * @param str
     * @param color
     * @param size
     * @return
     */
    public static Spanned setHtml(String data, String str, String color, String size) {
        String htmlStr = data + "<font color='" + color + "' size='" + size + "'> " + str + "</font>";
        return Html.fromHtml(htmlStr);
    }

    /**
     * 设置文本的HTML
     *
     * @param str
     * @param color
     * @return
     */
    public static Spanned setHtml(String data, String str, String color) {
        String htmlStr = data + "<font color='" + color + "' size='" + 2 + "'> " + str + "</font>";
        return Html.fromHtml(htmlStr);
    }

    /**
     * 动态获取mipmap 资源
     *
     * @param context
     * @param prefix
     * @param num
     * @return
     */
    public static int getIdentifier(Context context, String prefix, int num) {
        int res_id = context.getResources().getIdentifier(prefix + num, "mipmap", context.getPackageName());
        return res_id;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 发送短信(调起发短信页面)
     *
     * @param context
     * @param phoneNum
     */
    public static void sendMessage(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("smsto:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 设置文本图片
     *
     * @param context
     * @param id
     */
    public static void setCompoundDrawable(Context context, TextView tv, int id) {
        if (id != 0) {
            Drawable drawableBoy = CommonUtils.getDrawable(context, id);
            drawableBoy.setBounds(0, 0, drawableBoy.getMinimumWidth(), drawableBoy.getMinimumHeight());//这句一定要加
            tv.setCompoundDrawablesWithIntrinsicBounds(drawableBoy, null, null, null);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    // 邮箱规则：名称可为字母、数字、中下线和英文句号(只能在中间)，域名只能是字母、数字、中线(只能在中间)
    // 匹配Email地址的正则表达式：^[a-zA-Z0-9]+([._-]*[a-z0-9])*@([a-zA-Z0-9]+[a-zA-Z0-9-]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$
    public static boolean isEmail(String inputString) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher macher = pattern.matcher(inputString);
        return macher.find();
    }

    /**
     * 设置textview图片
     *
     * @param context
     * @param tv
     * @param id
     */
    public static void setDrawableRight(Context context, TextView tv, int id) {
        if (id != 0) {
            Drawable drawableBoy = CommonUtils.getDrawable(context, id);
            drawableBoy.setBounds(0, 0, drawableBoy.getMinimumWidth(), drawableBoy.getMinimumHeight());//这句一定要加
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableBoy, null);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public static void setImgGray(ImageView iv) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        iv.setColorFilter(filter);
    }


    /**
     * 获得一个UUID
     *
     * @return String UUID
     */

    public static long getNumbers() {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSS");
            String  formDate =sdf.format(date);
            return Long.parseLong(formDate);
        } catch (Exception e) {
            Log.d("TAG", e.toString());
        }
        return 0;
    }
}
