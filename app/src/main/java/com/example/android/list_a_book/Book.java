package com.example.android.list_a_book;

/**
 * Created by JukUm on 5/25/2017.
 */

public class Book {

    private String mBookTitle;      //title of the book
    private String mBookAuthor;     //author of the book
    private String mBookUrl;        //website url of the book

    /**
     * Create a new Book object with the book title, author, and url
     *
     * @param bookTitle  is the title of the book
     * @param bookAuthor is the author of the book
     * @param bookUrl    is the website url of the book
     */
    public Book(String bookTitle, String bookAuthor, String bookUrl) {
        this.mBookTitle = bookTitle;
        this.mBookAuthor = bookAuthor;
        this.mBookUrl = bookUrl;
    }

    //Returns the title of the book
    public String getBookTitle() {
        return mBookTitle;
    }

    //Returns the author of the book
    public String getBookAuthor() {
        return mBookAuthor;
    }

    //Returns the website URL of the book
    public String getBookUrl() {
        return mBookUrl;
    }

}