package com.example.android.list_a_book;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String GOOGLE_BOOKS_API =
            "https://www.googleapis.com/books/v1/volumes?q=search+";

    private EditText mEditText;
    private BookAdapter mBookAdapter;
    private TextView mNoResultsTextView;
    private ImageButton mSearchImageButton;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEditText = (EditText) findViewById(R.id.edit_text_search);
        mNoResultsTextView = (TextView) findViewById(R.id.no_results_found);
        mSearchImageButton = (ImageButton) findViewById(R.id.image_button_search);

        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        mRecyclerView.setAdapter(mBookAdapter);

        // If there is a network connection, fetch the data.
        // Else display the no connection error message.
        mSearchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnectionAvailable()) {
                    BookListAsynkTask task = new BookListAsynkTask();
                    task.execute();

                } else {
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Checks the state of the network connectivity.
    private boolean isInternetConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //Checks to update the UI with the search results
    private void updateUi(List<Book> bookList) {
        if (bookList.isEmpty()) {
            mNoResultsTextView.setVisibility(View.VISIBLE);
        } else {
            mNoResultsTextView.setVisibility(View.GONE);
        }
        mBookAdapter.swap(bookList);
    }

    //Gets the search input
    private String getUserSearchInput() {
        return mEditText.getText().toString();
    }

    //Formats the search input, so that it can be used for a new URL object
    private String getUrlRequest() {
        return GOOGLE_BOOKS_API + getUserSearchInput().trim();
    }

    private class BookListAsynkTask extends AsyncTask<URL, Void, List<Book>> {
        //Create an URL object to perform an HTTP request to, and receive a JSON response
        @Override
        protected List<Book> doInBackground(URL... urls) {
            URL url = createUrl(getUrlRequest());
            String jsonResponse = null;

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            return parseJson(jsonResponse);
        }

        @Override
        protected void onPostExecute(List<Book> bookList) {
            if (bookList == null) {
                return;
            }
            updateUi(bookList);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem creating the URL", e);
            }
            return url;
        }

        //Make a HTTP request to the given URL, and return the response as a String
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromSteam(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the GoogleBooks JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        //Converts the input stream into a String which contains the entire JSON response
        private String readFromSteam(InputStream inputStream) throws IOException {
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

        private List<Book> parseJson(String json) {
            if (json == null) {
                return null;
            }
            return Utils.fetchBookData(json);
        }
    }
}