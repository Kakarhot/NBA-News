package com.example.android.nbanews;

/**
 * Created by wenshuo on 2016/10/4.
 */
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;
    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    public NewsLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<News> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        ArrayList<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
