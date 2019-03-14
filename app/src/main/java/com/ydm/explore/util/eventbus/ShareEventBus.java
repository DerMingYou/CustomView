package com.ydm.explore.util.eventbus;

/**
 * Description:
 * Data：2019/3/14-15:08
 * Author: DerMing_You
 */
public class ShareEventBus {
    private boolean otherOpera;

    public ShareEventBus(boolean otherOpera) {
        this.otherOpera = otherOpera;
    }

    public boolean isOtherOpera() {
        return otherOpera;
    }
}
