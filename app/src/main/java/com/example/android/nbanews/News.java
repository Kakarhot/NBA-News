package com.example.android.nbanews;

/**
 * Created by wenshuo on 2016/10/4.
 */

public class News {
    private String mTitle;
    private String mDate;
    private String mUrl;

    public News(String title, String date, String url) {
        mTitle = title;
        mDate = date;
        mUrl = url;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public String getDate(){
        return this.mDate;
    }

    public String getUrl(){
        return this.mUrl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
