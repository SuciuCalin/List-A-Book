package com.example.android.list_a_book;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JukUm on 5/25/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    List<Book> mBook;
    Context    mContext;

    /**
     * Constructs a new BookAdapter
     *
     * @param context is the current context that the adapter is being created in
     * @param books   is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Book> books) {
        this.mBook = books;
        this.mContext = context;
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View bookRecyclerItem = inflater.inflate(R.layout.book_list_item, parent, false);
        return new ViewHolder(bookRecyclerItem);
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, int position) {
        final Book bookListItem = mBook.get(position);

        holder.bookAuthor.setText(bookListItem.getBookAuthor());
        holder.bookTitle.setText(bookListItem.getBookTitle());
        holder.setClickListener(new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Uri bookUri = Uri.parse(bookListItem.getBookUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                mContext.startActivity(websiteIntent);

                Log.v("BookAdapter","List item: " + bookListItem + " with " + bookUri + " was clicked.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBook.size();
    }

    // View lookup cache
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView bookTitle;
        private TextView bookAuthor;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public void swap(List<Book> books) {
        mBook.clear();
        mBook.addAll(books);
        notifyDataSetChanged();
    }
}

