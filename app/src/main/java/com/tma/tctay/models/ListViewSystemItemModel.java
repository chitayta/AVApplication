package com.tma.tctay.models;

/**
 * Created by tctay on 7/5/2017.
 */

public class ListViewSystemItemModel {

    private String title;
    private String comment;

    private boolean isHeader = false;

    public ListViewSystemItemModel(String title)
    {
        this(title, null);
        isHeader = true;
    }

    public ListViewSystemItemModel(String title, String comment)
    {
        super();
        this.title = title;
        this.comment = comment;
    }
    public boolean isHeader(){
        return isHeader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

}
