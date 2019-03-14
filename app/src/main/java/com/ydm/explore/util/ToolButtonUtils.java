package com.ydm.explore.util;

/**
 * Description: 快速点击判断
 * Data：2019/3/14-14:50
 * Author: DerMing_You
 */
public class ToolButtonUtils {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
