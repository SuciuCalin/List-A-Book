package com.example.android.list_a_book;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JukUm on 5/25/2017.
 */

public class Utils {

    private Utils() {
    }

    //Returns a list of Book objects, that has been built up from parsing the given JSON response.
    public static List<Book> fetchBookData(String bookJSON) {
        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(bookJSON);
            if (jsonResponse.getInt("totalItems") == 0) {
                return books;
            }

            JSONArray bookJsonArray = jsonResponse.getJSONArray("items");
            //Create a new Book object, for each book in the bookJsonArray.
            for (int i = 0; i < bookJsonArray.length(); i++) {
                JSONObject bookListObject = bookJsonArray.getJSONObject(i);
                JSONObject volumeInfo = bookListObject.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                String authorsList = null;
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    authorsList = formatAuthorsList(authors);
                }

                String url = volumeInfo.getString("infoLink");

                //Create a new Book object with the title, author, and url from the JSON response
                Book book = new Book(title, authorsList, url);
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the JSON results", e);
        }
        return books;
    }

    private static String formatAuthorsList(JSONArray authorsList) throws JSONException {
        String authorList = null;
        if (authorsList.length() == 0) {
            return null;
        }
        for (int i = 0; i < authorsList.length(); i++) {
            if (i == 0) {
                authorList = authorsList.getString(0);
            } else {
                authorList += "\n" + authorsList.getString(i);
            }
        }
        return authorList;
    }
}