package com.example.android.gbooksearch;

/**
 * Created by pasha on 30/08/2017.
 */


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;

/**
 *
 * This class takes the URL, fetches JSON response from the URL and transfroms it into different books.
 * Books are added into a List<Books> and returned
 *
 *
 */

public final class BookMaker {
    private final static String LOG_TAG = "Logged[BookMaker]: ";

    private BookMaker(){ } //private constructor because we won;t be making any objects of this class

    /**
     * FetchData method. Will be called in loader.
     * Also handles calling all other methods
     *
     */

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl); //calling createUrl method that is defined below

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url); //calling makeHttpsRequest method that is defined felow
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractBooks(jsonResponse); //calling method defined below to extract book info by passing JSONreponse as parameter
        return books;
    }

    //this method accepts raw JSON response from the website and extracts data from the response,
    //makes a Book object, adds the object to an ArrayList of type Book
    //and then returns the ArrayList to fetchBookData() method, which will then return this list to ResultActivity

    public static ArrayList<Book> extractBooks(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            Log.i(LOG_TAG, "JSON response was empty");
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject BasejsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = BasejsonObject.getJSONArray("items");
            int totalBooksFetched = 0;
            String _title;
            String _authors = "";
            int _authorCount;
            String _description;
            String _previewLink;
            String _thumbnailLink;

            totalBooksFetched = jsonArray.length();
            Log.i(LOG_TAG, "Total books fetched: " + totalBooksFetched);

            for (int i=0; i<totalBooksFetched; i++)
            {
                JSONObject jsonArrayObject = jsonArray.getJSONObject(i);
                JSONObject volumeInfoObject = jsonArrayObject.getJSONObject("volumeInfo");
                _title = volumeInfoObject.getString("title");
                if(volumeInfoObject.has("authors"))
                {
                    JSONArray authorsArray = volumeInfoObject.getJSONArray("authors");
                    _authorCount = authorsArray.length();
                    for (int j=0; j<_authorCount; j++)
                    {
                        if(j>0)
                        {
                            _authors += ", "; //adding a comma if more than one authors
                        }
                        _authors +=  authorsArray.getString(j);
                    }
                }
                else {
                    _authorCount = 1;
                    _authors = "N/A";
                }


                if(volumeInfoObject.has("imageLinks"))
                {
                    JSONObject imagelinks = volumeInfoObject.getJSONObject("imageLinks");
                    _thumbnailLink = imagelinks.getString("smallThumbnail");
                }
                else
                {
                    _thumbnailLink = "https://store.lexisnexis.com.au/__data/media/catalog/thumb//placeholder.jpg";
                }

                _previewLink = volumeInfoObject.getString("previewLink"); //PreviewLink is ALWAYS available for any given book. ALWAYS.

                if(jsonArrayObject.has("searchInfo"))
                {
                    JSONObject searchInfo = jsonArrayObject.getJSONObject("searchInfo");
                    if(searchInfo.has("textSnippet"))
                    {
                        _description = searchInfo.getString("textSnippet");
                        _description = _description.replaceAll("<[^>&#;]*>", "");
                    }
                    else {
                        _description = "A book by " + _authors;
                    }
                }
                else {
                    _description = "A book by " + _authors;
                }
                _description = _description.replaceAll("&#39;", "'");
                Log.i(LOG_TAG,"------------------------------------------------------------------------------------");
                Log.i(LOG_TAG, "Title: " + _title);
                Log.i(LOG_TAG, "\nAuthor count: " + _authorCount);
                Log.i(LOG_TAG, "\nAuthor: " + _authors);
                Log.i(LOG_TAG, "\nPreviewlink: " + _previewLink);
                Log.i(LOG_TAG, "\nThumnailLink: " + _thumbnailLink);
                Log.i(LOG_TAG, "\nDescription: " + _description);

                Book temp = new Book(_title, _authors, _authorCount, _previewLink, _thumbnailLink, _description);
                _authors = ""; //resetting author string to null for the next book
                books.add(temp);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;
    }


    //this methods makes URL out of the given string and returns URL object to fetchBookquakeData() method
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //this method takes URL object as arg and returns raw JSON reponse from the website
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //this is a helper method for makeHttpRequest() method
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}

