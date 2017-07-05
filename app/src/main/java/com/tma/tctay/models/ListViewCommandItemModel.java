package com.tma.tctay.models;

/**
 * Created by tctay on 6/28/2017.
 */

public class ListViewCommandItemModel {
    private int icon;
    private String title;
    private int switchButton;

    private boolean isHeader = false;

    public ListViewCommandItemModel(String title)
    {
        this(-1, title, -1);
        isHeader = true;
    }

    public ListViewCommandItemModel(int icon, String title, int switchButton)
    {
        super();
        this.icon = icon;
        this.title = title;
        this.switchButton = switchButton;
    }
    public boolean isHeader(){
        return isHeader;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public int getSwitchButton() {
        return switchButton;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSwitchButton(int switchButton) {
        this.switchButton = switchButton;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
