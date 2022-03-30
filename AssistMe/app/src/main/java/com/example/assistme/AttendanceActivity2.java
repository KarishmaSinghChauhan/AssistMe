package com.example.assistme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.assistme.AttendanceActivity.sharedPreferences;
import static com.example.assistme.AttendanceActivity.subjectList;

public class AttendanceActivity2 extends AppCompatActivity {

    Intent intent;
    EditText subject;
    TextView presentTextView;
    TextView absentTextView;
    TextView totalTextView;
    TextView percentTextView;
    TextView minAttTextView;
    TextView summaryTextView;
    EditText presentEditText;
    EditText absentEditText;
    EditText minAttEditText;
    double f;

    public void rename(View view){

        subject = (EditText) findViewById(R.id.subject);
        Editable newSub = subject.getText();
        String s = " ";
        if(newSub != null) {
            s = newSub.toString();
        }

        intent = getIntent();
        String subName = (intent.getStringExtra("subjectName"));
        int pos = (int)intent.getIntExtra("position", -1);

        try {
            SQLiteDatabase vattendanceDB = this.openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);
            vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");
            vattendanceDB.execSQL("UPDATE vattendance SET subject = '"+s+"' WHERE positions = "+pos+"");
            subjectList.set(pos,s);
            sharedPreferences = this.getSharedPreferences("com.example.karishma.assistme", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("SUBJECTLIST", ObjectSerializer.serialize(subjectList)).apply();
//            subjectList = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString("SUBJECTLIST", ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (Exception ee) {
            ee.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error in renaming the subject!", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(), "Subject renamed!", Toast.LENGTH_SHORT).show();

        if(subjectList.size() != 0) {

            com.example.assistme.AttendanceActivity.arrayAdapter = new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_list_item_1, subjectList);
            com.example.assistme.AttendanceActivity.subjectListView.setAdapter(com.example.assistme.AttendanceActivity.arrayAdapter);
        }

        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void updateAttendance(View view) {

        presentTextView = (TextView) findViewById(R.id.presentTextView);
        absentTextView = (TextView) findViewById(R.id.absentTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        presentEditText = (EditText) findViewById(R.id.presentEditText);
        absentEditText = (EditText) findViewById(R.id.absentEditText);

        presentTextView.setText(presentEditText.getText());
        absentTextView.setText(absentEditText.getText());

        Editable a = null, b = null;
        double C = 0, d = 0;
        try {
            a = presentEditText.getText();
            b = absentEditText.getText();
            C = 0.0;
            d = 0.0;

            if (a != null) {
                C = Double.parseDouble(a.toString());
            }
            if (b != null) {
                d = Double.parseDouble(b.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int e = (int) (C+ d);

        totalTextView.setText(String.valueOf(e));
        f = C * 100 / e;
        f = Math.round(f * 100);
        f = f / 100;
        percentTextView.setText((String.valueOf(f)) + " %");

        double minA = 0;
        try {
            minA = Double.parseDouble((minAttEditText.getText()).toString());
            if (minA <= 100) {
                minAttTextView.setText(minAttEditText.getText() + " %");
                minA = Math.round(minA * 100);
                minA = minA / 100;

                if (f < minA) {
                    summaryTextView.setText("You've got a low attendance! You need to attend more classes. :(");
                }
                if (f >= minA) {
                    summaryTextView.setText("Good job! It seems you have regularly attended your classes. :)");
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Minimum percentage must not be greater than 100%", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception l) {
            Toast.makeText(getApplicationContext(), "Please enter the minimum attendance to proceed! (Error in MinAtt %)", Toast.LENGTH_SHORT).show();
        }

        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        intent = getIntent();
        String subName = (intent.getStringExtra("subjectName"));
        int pos = (int)intent.getIntExtra("position", -1);

        double pres = Double.parseDouble(presentTextView.getText().toString());
        double abs = Double.parseDouble(absentTextView.getText().toString());
        double minatt = Double.parseDouble(minAttEditText.getText().toString());

        try {
            SQLiteDatabase vattendanceDB = this.openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);
            vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");
            vattendanceDB.execSQL("UPDATE vattendance SET present = "+pres+" WHERE positions = "+pos+"");
            vattendanceDB.execSQL("UPDATE vattendance SET absent = "+abs+" WHERE positions = "+pos+"");
            vattendanceDB.execSQL("UPDATE vattendance SET minAtt = "+minatt+" WHERE positions = "+pos+"");
        }
        catch (Exception ee) {
            ee.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error in storing database!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance2);

        subject = (EditText)findViewById(R.id.subject);
        presentTextView = (TextView) findViewById(R.id.presentTextView);
        absentTextView = (TextView) findViewById(R.id.absentTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        presentEditText = (EditText) findViewById(R.id.presentEditText);
        absentEditText = (EditText) findViewById(R.id.absentEditText);
        minAttTextView = (TextView)findViewById(R.id.minAttTextView);
        summaryTextView = (TextView)findViewById(R.id.summaryTextView);
        minAttEditText = (EditText)findViewById(R.id.minAttEditText);

        intent = getIntent();
        subject.setText(intent.getStringExtra("subjectName"));
        String subName = (intent.getStringExtra("subjectName"));
        int pos = intent.getIntExtra("position", -1);

//        Toast.makeText(getApplicationContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), subName, Toast.LENGTH_SHORT).show();

        try {
            SQLiteDatabase vattendanceDB = getApplicationContext().openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);
            vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");
            Cursor c = vattendanceDB.rawQuery("SELECT * FROM vattendance", null);

            int positionsIndex = c.getColumnIndex("positions");
            int subjectIndex = c.getColumnIndex("subject");
            int presentIndex = c.getColumnIndex("present");
            int absentIndex = c.getColumnIndex("absent");
            int minAttIndex = c.getColumnIndex("minAtt");
            int idIndex = c.getColumnIndex("id");

            c.moveToFirst();
            int count = c.getCount();

            for (int i = 0; i <= count -1; i++) {

                if(i== pos) {

                    presentTextView.setText((CharSequence)(Double.toString(c.getDouble(presentIndex))));
                    absentTextView.setText((CharSequence)(Double.toString(c.getDouble(absentIndex))));
                    presentEditText.setText((CharSequence)(Double.toString(c.getDouble(presentIndex))));
                    absentEditText.setText((CharSequence)(Double.toString(c.getDouble(absentIndex))));

                    Editable a = null, b = null;
                    double C = 0, d = 0;
                    try {
                        a = presentEditText.getText();
                        b = absentEditText.getText();
                        C = 0.0;
                        d = 0.0;

                        if (a != null) {
                            C = Double.parseDouble(a.toString());
                        }
                        if (b != null) {
                            d = Double.parseDouble(b.toString());
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    int e = (int) (C + d);
                    totalTextView.setText(String.valueOf(e));
                    f = C * 100 / e;
                    f = Math.round(f * 100);
                    f = f / 100;
                    percentTextView.setText((String.valueOf(f)) + " %");

                    double minA = 0;
                    try {
                        minA = Double.parseDouble(Double.toString(c.getDouble(minAttIndex)));
                        if (minA <= 100) {
                            minAttTextView.setText(Double.toString(c.getDouble(minAttIndex)) + " %");
                            minAttEditText.setText(Double.toString(c.getDouble(minAttIndex)) );
                            minA = Math.round(minA * 100);
                            minA = minA / 100;

                            if (f < minA) {
                                summaryTextView.setText("You've got a low attendance! You need to attend more classes. :(");
                            }
                            if (f >= minA) {
                                summaryTextView.setText("Good job! It seems you have regularly attended your classes. :)");
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Minimum percentage must not be greater than 100%", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception l) {
                        Toast.makeText(getApplicationContext(), "Please enter the minimum attendance to proceed!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }

                c.moveToNext();
            }
        }
        catch(Exception e ){
            e.printStackTrace() ;
            Toast.makeText(getApplicationContext(), "Error in onCreate() database of AttendanceActivity2!", Toast.LENGTH_SHORT).show();
        }
    }
}
