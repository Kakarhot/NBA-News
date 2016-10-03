package com.example.android.nbanews;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wenshuo on 2016/10/4.
 */

public class NewsAdapter extends ArrayAdapter {

    public NewsAdapter(Context context, ArrayList<News> news){
        super(context,0,news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        News news = (News)getItem(position);

        TextView titleView = (TextView)listItemView.findViewById(R.id.title);
        TextView dateView = (TextView)listItemView.findViewById(R.id.date);
        titleView.setTypeface(null, Typeface.BOLD);
        dateView.setTypeface(null, Typeface.ITALIC);

        String title = news.getTitle();
        String date = news.getDate();

        titleView.setText(title);
        dateView.setText(date);

        return listItemView;
    }
}
