package com.example.assistme;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuoteActivity extends AppCompatActivity {

    String author;
    String quotee;
    ListView listView;
    ArrayList <String> list;
    ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    SQLiteDatabase quotesDB;

/*    public void set(String a, String b)
    {
        TextView quoteTextView = (TextView)findViewById(R.id.quoteTextViewId);
        TextView authorTextView = (TextView)findViewById(R.id.authorTextViewId);
        quoteTextView.setText(a);
        authorTextView.setText(b);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        //sharedPreferences.edit().remove("List").apply();

        listView = (ListView)findViewById(R.id.quoteListViewId);
        list = new ArrayList();
        arrayAdapter = new ArrayAdapter(QuoteActivity.this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        //sharedPreferences.edit().remove("List").apply();

        quotesDB = this.openOrCreateDatabase("Quotes",MODE_PRIVATE, null);
        quotesDB.execSQL("CREATE TABLE IF NOT EXISTS quotes (quote VARCHAR, author VARCHAR)");
        quotesDB.execSQL("DELETE FROM quotes");

        DownloadTask task = new DownloadTask();

        try {
            task.execute("https://favqs.com/api/qotd");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //  listView.setAdapter(arrayAdapter);
        //  set(quotee, author);

        /*
        sharedPreferences = this.getSharedPreferences("com.example.karishma.mypersonaldiary", Context.MODE_PRIVATE);
        try{
            list =(ArrayList)ObjectSerializer.deserialize(sharedPreferences.getString("List", ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1)
                {
                    char current = (char)data;

                    result += current;

                    data = reader.read();
                }

//                Log.i("URLContent : ", result);

                JSONObject jsonObject = new JSONObject(result);

                String quote = jsonObject.getString("quote");
                //   String quote = jsonObject.getString("body");
                //   Log.i("Quote : " , quote);
//                Log.i("Quote : ", quote);

                JSONObject jsonObject2 = new JSONObject(quote);

                author = jsonObject2.getString("author");
                quotee = jsonObject2.getString("body");

//                Log.i("Quotee : " , quotee);
//                Log.i("Author : ", author);

                String sql = "INSERT INTO quotes (quote, author) VALUES (?,?)";
                SQLiteStatement statement = quotesDB.compileStatement(sql);
                statement.bindString(1, quotee);
                statement.bindString(2, author);
                statement.execute();

                Cursor c = quotesDB.rawQuery("SELECT * FROM quotes", null);

                int quoteIndex = c.getColumnIndex("quote");
                int authorIndex = c.getColumnIndex("author");

                c.moveToFirst();

                list.add(c.getString(quoteIndex));
                list.add(c.getString(authorIndex));

                arrayAdapter.notifyDataSetChanged();
/*
                listView.setAdapter(arrayAdapter);
                set(quotee, author);
                list = new ArrayList();
                list.add(quotee);
                list.add(author);
                sharedPreferences = this.getSharedPreferences("com.example.karishma.mypersonaldiary", Context.MODE_PRIVATE);
                try{
                    sharedPreferences.edit().putString("List", ObjectSerializer.serialize(list)).apply();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(DatesGoalsNotesActivity.this,"Error in deleting Note! in OnCreate()", Toast.LENGTH_SHORT).show();                                }
                }
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
*/
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
