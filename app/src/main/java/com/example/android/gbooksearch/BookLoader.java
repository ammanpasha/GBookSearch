package com.example.android.gbooksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by pasha on 30/08/2017.
 */

public class BookLoader  extends AsyncTaskLoader<List<Book>> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructor
     * **/
    public BookLoader(Context context, String url)
    {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Books.
      final  List<Book> books = BookMaker.fetchBookData(mUrl);
        return books;
    }
}
