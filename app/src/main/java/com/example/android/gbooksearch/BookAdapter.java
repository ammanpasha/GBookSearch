package com.example.android.gbooksearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.name;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by pasha on 30/08/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> bookObject)
    {
        super(context, 0, bookObject);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        //Title TextView
        TextView _title = (TextView) listItemView.findViewById(R.id.title);
        _title.setText(currentBook.getTitle());

        //Author TextView
        TextView _author = (TextView) listItemView.findViewById(R.id.author);
        _author.setText("Author(s): " + currentBook.getAuthors());

        //Description TextView
        TextView _description = (TextView) listItemView.findViewById(R.id.description);
        _description.setText(currentBook.getDescription());

        //Book cover ImageView
        ImageView _imgage = (ImageView) listItemView.findViewById(R.id.img);
        String temp_url = currentBook.getThumbnailLink();
        Picasso.with(this.getContext()).load(temp_url).into(_imgage); //This will auto fetch image from the given url



        return listItemView;
    }
}
