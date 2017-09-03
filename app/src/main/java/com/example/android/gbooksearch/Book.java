package com.example.android.gbooksearch;

/**
 * Created by pasha on 30/08/2017.
 */

public class Book {

    /**
     * Member variables
     * **/

    private String title;
    private String authors;
    private int authorCount;
    private String previewLink;
    private String thumbnailLink;
    private String description;



    /**
     *Constructor
     * **/
    public Book(String _title,
                String _authors,
                int _authorCount,
                String _previewLink,
                String _thumbnailLink,
                String _description)
    {
        title = _title;
        authors = _authors;
        authorCount = _authorCount;
        previewLink = _previewLink;
        thumbnailLink = _thumbnailLink;
        description = _description;
    }


    /**
     * Getter methods
     * **/

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public String getThumbnailLink() { return thumbnailLink; }

    public String getDescription() { return description; }
}

