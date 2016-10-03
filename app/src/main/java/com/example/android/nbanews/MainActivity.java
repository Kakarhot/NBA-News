package com.example.android.nbanews;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String REQUEST_URL = "http://content.guardianapis.com/search";
    private static final String QUERY = "NBA";
    private static final String FROM_DATE = "2016-01-01";
    private static final String API_KEY= "test";

    private NewsAdapter mAdapter;
    private ListView newsListView;
    private TextView emptyView;
    private ProgressBar progressBar;
    private NewsLoader newsLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        mAdapter = new NewsAdapter(this,new ArrayList<News>());
        newsListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView)findViewById(R.id.empty_view);
        progressBar = (ProgressBar)findViewById(R.id.loading_spinner);

        newsListView.setEmptyView(emptyView);
        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = (News)parent.getItemAtPosition(position);
                String url = news.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE) ;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(0, null, this);
        }
        else{
            emptyView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {;
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q",QUERY);
        uriBuilder.appendQueryParameter("from-date",FROM_DATE);
        uriBuilder.appendQueryParameter("api-key",API_KEY);
        newsLoader = new NewsLoader(this,uriBuilder.toString());
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if(news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
        else
            emptyView.setText(R.string.no_news_found);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }
}
