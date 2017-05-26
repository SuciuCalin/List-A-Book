package com.example.android.list_a_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JukUm on 5/25/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Constructs a new BookAdapter
     *
     * @param context is the current context that the adapter is being created in
     * @param books   is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    //Returns a list item that displays information about the book title and author
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //Checks if there is an existing list item view that we can reuse, otherwise, we inflate a new list item layout
        View bookListView = view;
        if (bookListView == null) {
            bookListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book listItemView = getItem(position);
        TextView bookTitleView = (TextView) bookListView.findViewById(R.id.book_title);
        TextView bookAuthorView = (TextView) bookListView.findViewById(R.id.book_author);

        bookTitleView.setText(listItemView.getBookTitle());
        bookAuthorView.setText(listItemView.getBookAuthor());

        return bookListView;
    }
}

