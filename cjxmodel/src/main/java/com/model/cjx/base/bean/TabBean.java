package com.model.cjx.base.bean;

/**
 * create by cjx on 2018/12/17
 */
public class TabBean {
    private int icon;
    private String title;
    private boolean attach = false;

    public TabBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isAttach() {
        return attach;
    }

    public void setAttach(boolean attach) {
        this.attach = attach;
    }
}
