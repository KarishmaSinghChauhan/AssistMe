package com.example.assistme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuoteActivity extends AppCompatActivity {

    String author;
    String quotee;
    TextView quoteTextView;
    TextView authorTextView;
    ArrayList <String> list;

    public void randomQuote(View view) {

        author = "";
        quotee = "";

        try {
            DownloadTask task = new DownloadTask();
            task.execute("https://favqs.com/api/qotd");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(QuoteActivity.this,"Could not fetch a random quote. Try again!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        quoteTextView = (TextView)findViewById(R.id.quoteTextViewId);
        authorTextView = (TextView)findViewById(R.id.authorTextViewId);

        randomQuote(findViewById(R.id.anotherQuoteBtnId));
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(QuoteActivity.this,"Could not fetch a random quote. Try again!", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                String quote = jsonObject.getString("quote");

                JSONObject jsonObject2 = new JSONObject(quote);
                author = jsonObject2.getString("author");
                quotee = jsonObject2.getString("body");

                if(author.equals("") == false && quotee.equals("") == false) {
                    quoteTextView.setText(quotee);
                    authorTextView.setText("- " + author);
                } else {
                    Toast.makeText(QuoteActivity.this,"Could not fetch a random quote. Try again!", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(QuoteActivity.this,"Could not fetch a random quote. Try again!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
