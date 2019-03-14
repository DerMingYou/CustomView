package com.ydm.explore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.ExifInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.ydm.explore.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description:
 * Data：2019/3/14-14:58
 * Author: DerMing_You
 */
public class PhotoBitmapUtils {
    private static final String TAG = PhotoBitmapUtils.class.getSimpleName();
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    // 防止实例化
    private PhotoBitmapUtils() {
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param path   图片保存的路径
     * @param bitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(Context context, String path, Bitmap bitmap) {
        File tempFile = new File(path);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tempFile);
            // 把数据写入文件，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 处理旋转后的图片
     *
     * @param originPath 原图路径
     * @param context    上下文
     * @param outputPath 图片输出的位置
     * @param reqWidth   需要的宽度
     * @param reqHeight  需要的高度
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(Context context, String originPath, String outputPath, int reqWidth, int reqHeight) {
        if (TextUtils.isEmpty(originPath))
            return originPath;

        if (TextUtils.isEmpty(outputPath)) {
            outputPath = originPath;
        }

        // 取得图片旋转角度
        int angle = readPictureDegree(originPath);

        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = decodeSampledBitmapFromFile(originPath, reqWidth, reqHeight);

        // 修复图片被旋转的角度
        Bitmap bitmap = rotatingImageView(angle, bmp);

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(context, outputPath, bitmap);
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 把原图按比例压缩，比例是根据原图和需求大小计算的一个最接近比例
     *
     * @param path      原图的路径
     * @param reqWidth  需要压缩到的尺寸宽度
     * @param reqHeight 需要压缩到的尺寸高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 计算压缩比率
     *
     * @param options
     * @param reqWidth  要求的宽度
     * @param reqHeight 要求的高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // 原本图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (reqWidth == 0 || reqHeight == 0) {
            return inSampleSize;
        }

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 读取本地
     *
     * @param filePath
     * @return
     */
    public static Bitmap loadFilePath(String filePath) {
        File mFile = new File(filePath);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return bitmap;
        }
        return null;
    }

    /**
     * 从资源文件中获取图片
     *
     * @param context    上下文
     * @param drawableId 资源文件id
     * @return
     */
    public static Bitmap gainBitmap(Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                drawableId);
        return bmp;
    }

    /**
     * 将bitmap转成 byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] toBtyeArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将byte数组转成 bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 截取指定View内容，保存到指定的文件目录
     *
     * @param mView    截屏目标View
     * @param filePath jpg文件路径+文件名
     * @param quality  0-100压缩率
     * @return
     */
    public static File takeViewScreenShot(View mView, String filePath,
                                          int quality) {
        File myCaptureFile = new File(filePath);
        mView.setDrawingCacheEnabled(true);
        try {

            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            mView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG,
                    quality, bos);

            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView.setDrawingCacheEnabled(false);
        return myCaptureFile;
    }

    /**
     * 替换Bitmap线条颜色
     *
     * @param inBitmap
     * @param tintColor
     * @return
     */
    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * 替换Bitmap背景颜色
     *
     * @param color
     * @param orginBitmap
     * @return
     */
    public static Bitmap drawBg4Bitmap(Bitmap orginBitmap, int color) {
        if (orginBitmap == null) {
            return null;
        }
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     *
     * @param filePic
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(File filePic, Bitmap mBitmap) {
        try {
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, filePic.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePic.getAbsolutePath();
    }

    /**
     * 设置缩略图
     *
     * @param bitMap
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 0;
        int newHeight = 0;
        if (width > height) {
            newWidth = 600;
            newHeight = 100;
        } else {
            // 设置想要的大小
            newWidth = 100;
            newHeight = 600;
        }
        // 计算缩放比例
        //        float scaleWidth = ((float) newWidth) / width;
        //        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = (float) (height * 1.0 / newHeight);
        float scaleHeight = (float) (width * 1.0 / newWidth);
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        newBitMap = Bitmap.createScaledBitmap(newBitMap, 500, 500, true);
        newBitMap = bytesToBimap(bmpToByteArray(newBitMap, true));
        return newBitMap;
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
}
