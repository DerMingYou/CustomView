package com.ydm.explore.view.bean;

/**
 * Description:
 * Data：2019/3/14-14:40
 * Author: DerMing_You
 */
public class ShareBean {
    private String title;
    private String subtitle;
    private String imgUrl;
    private String appUrl;
    private String shareTitle;
    private String qqAppName;
    private boolean otherOpera;

    /**
     * 不修改布局中的分享标题(默认使用)
     * @param title
     * @param subtitle
     * @param imgUrl
     * @param appUrl
     */
    public ShareBean(String title, String subtitle, String imgUrl, String appUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.imgUrl = imgUrl;
        this.appUrl = appUrl;
    }

    /**
     * 修改布局中的分享标题
     * @param title
     * @param subtitle
     * @param imgUrl
     * @param appUrl
     * @param shareTitle 分享标题
     */
    public ShareBean(String title, String subtitle, String imgUrl, String appUrl, String shareTitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.imgUrl = imgUrl;
        this.appUrl = appUrl;
        this.shareTitle = shareTitle;
    }

    /**
     * 修改布局中的分享标题
     *
     * @param title
     * @param subtitle
     * @param imgUrl
     * @param appUrl
     * @param shareTitle
     * @param qqAppName   修改QQ分享右下角显示的名称
     */
    public ShareBean(String title, String subtitle, String imgUrl, String appUrl, String shareTitle, String qqAppName) {
        this.title = title;
        this.subtitle = subtitle;
        this.imgUrl = imgUrl;
        this.appUrl = appUrl;
        this.shareTitle = shareTitle;
        this.qqAppName = qqAppName;
    }

    /**
     * QQ分享成功以后 是否有其他操作
     * @param title
     * @param subtitle
     * @param imgUrl
     * @param appUrl
     * @param shareTitle
     * @param otherOpera  true 有其他操作  false 没有其他操作
     */
    public ShareBean(String title, String subtitle, String imgUrl, String appUrl, String shareTitle
            , boolean otherOpera) {
        this.title = title;
        this.subtitle = subtitle;
        this.imgUrl = imgUrl;
        this.appUrl = appUrl;
        this.shareTitle = shareTitle;
        this.otherOpera = otherOpera;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getQqAppName() {
        return qqAppName;
    }

    public void setQqAppName(String qqAppName) {
        this.qqAppName = qqAppName;
    }

    public boolean isOtherOpera() {
        return otherOpera;
    }

    public void setOtherOpera(boolean otherOpera) {
        this.otherOpera = otherOpera;
    }
}
