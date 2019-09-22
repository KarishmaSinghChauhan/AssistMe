package com.example.assistme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;

public class AttendanceActivity extends AppCompatActivity {

    static ArrayList<String> subjectList = new ArrayList<String>();
    static SharedPreferences sharedPreferences;
    static ArrayAdapter<String> arrayAdapter;
    static ListView subjectListView;
    Intent intent;
    String subName;
    int count;
    int countpos;
    // ArrayList<String> storedSubject = new ArrayList<String>();

    public void addSubject(View view) {
        EditText addSubject = (EditText) findViewById(R.id.addSubject);
        subjectListView = (ListView) findViewById(R.id.subjectListView);

        if(((((addSubject.getText()).toString()).isEmpty()))  == true) {

            Toast.makeText(getApplicationContext(), "Empty Subject! Please enter a name.", Toast.LENGTH_SHORT).show();
        }

        else{

            subjectList.add(addSubject.getText().toString());
            String subName = addSubject.getText().toString();

            addSubject.getText().clear();

            arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subjectList);
            subjectListView.setAdapter(arrayAdapter);

            sharedPreferences = this.getSharedPreferences("com.example.karishma.mypersonaldiary", Context.MODE_PRIVATE);

            try {

                sharedPreferences.edit().putString("SUBJECTLIST", ObjectSerializer.serialize(subjectList)).apply();
                //Toast.makeText(getApplicationContext(), SUBJECTLIST.toString(), Toast.LENGTH_SHORT).show();





                SQLiteDatabase vattendanceDB = getApplicationContext().openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);

                // getApplicationContext().deleteDatabase("attendanceDB");

                // attendanceDB = this.openOrCreateDatabase("Attendance", MODE_PRIVATE, null);

                vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");

                // zattendanceDB.execSQL("INSERT INTO vattendance (positions,subject, present,absent,minAtt) VALUES (1,'es',0,40, 10)");

                // ERROR LINE //
                vattendanceDB.execSQL("INSERT INTO vattendance (positions, subject, present, absent, minAtt) VALUES (0, '"+subName+"'  , 0,0,0)");

                Cursor c = vattendanceDB.rawQuery("SELECT * FROM vattendance", null);
                count = c.getCount();
                countpos = count -1;

                vattendanceDB.execSQL("UPDATE vattendance SET positions = "+countpos+" WHERE subject = '"+subName+"'");



                /*
                 int positionsIndex = c.getColumnIndex("positions");
                 int subjectIndex = c.getColumnIndex("subject");
                 int presentIndex = c.getColumnIndex("present");
                 int absentIndex = c.getColumnIndex("absent");
                 int minAttIndex = c.getColumnIndex("minAtt");
                 int idIndex = c.getColumnIndex("id");

                 c.moveToFirst();

                 int count = c.getCount();
                 //POSITION = id ;

                 for (int i = 0; i <= count -1; i++) {

                     if(i== position)
                     {
                         //DISPLAY THE DATABASE FIELDS IN THEIR RESPECTIVE TEXTFEILDS
                         Log.i("POSITION of subject = ", Long.toString(id));
                         Log.i("positions = ", Integer.toString(c.getInt(positionsIndex)));
                         Log.i("subject = ", c.getString(subjectIndex));
                         Log.i("id = ", Integer.toString(c.getInt(idIndex)));
                         Log.i("present = ", Double.toString(c.getDouble(presentIndex)));
                         Log.i("absent = ", Double.toString(c.getDouble(absentIndex)));


                         break;
                     }

                     c.moveToNext();
                 }

                 */

            }

            catch(Exception e) {

                e.printStackTrace();
            }

            /*
            subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    try {

                        subName = subjectList.get(position);


                        SQLiteDatabase vattendanceDB = getApplicationContext().openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);

                        // getApplicationContext().deleteDatabase("attendanceDB");

                        // attendanceDB = this.openOrCreateDatabase("Attendance", MODE_PRIVATE, null);

                        vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");

                        // zattendanceDB.execSQL("INSERT INTO vattendance (positions,subject, present,absent,minAtt) VALUES (1,'es',0,40, 10)");

                        // ERROR LINE //
                        vattendanceDB.execSQL("INSERT INTO vattendance (positions, subject, present, absent, minAtt) VALUES (" + position + ",' " + subName + "'  , 0,0,0)");

                    }
                    catch(Exception e ){
                        e.printStackTrace() ;
                        //  Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Error in onCreate() database of AttendanceActivity!", Toast.LENGTH_SHORT).show();
                    }


                    Intent intent = new Intent(getApplicationContext(),AttendanceActivity2.class);
                    intent.putExtra("subjectName", subName);
                    intent.putExtra("position", position);
                    startActivity(intent);


                }
            });

            */


            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

                /*

            try {

                subjectList = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString("subjectList", ObjectSerializer.serialize(new ArrayList<String>())));
           } catch (Exception e) {

                 e.printStackTrace();
            }

            */
            Toast.makeText(getApplicationContext(), (subjectList.toString()), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),AttendanceActivity2.class);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //subjectList.clear();
        subjectListView = (ListView) findViewById(R.id.subjectListView);
        sharedPreferences = this.getSharedPreferences("com.example.karishma.mypersonaldiary", Context.MODE_PRIVATE);

        try {

            subjectList = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString("SUBJECTLIST", ObjectSerializer.serialize(new ArrayList<String>())));

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //sharedPreferences.edit().remove("SUBJECTLIST").apply();
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } catch (Exception e) {

            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), (subjectList.toString()), Toast.LENGTH_SHORT).show();


        //   if(storedSubject.size()!=0) {
        //   Toast.makeText(getApplicationContext(), Integer.toString(storedSubject.size()), Toast.LENGTH_SHORT).show();
        //}

        if(subjectList.size() != 0) {

            arrayAdapter = new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_list_item_1, subjectList);
            subjectListView.setAdapter(arrayAdapter);
        }

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //subName = subjectList.get(position);

                int POSITION ;



                try {

                    POSITION = (int)id ;
                    subName = subjectList.get((int)(id));

                    SQLiteDatabase vattendanceDB = getApplicationContext().openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);

                    // getApplicationContext().deleteDatabase("attendanceDB");

                    // attendanceDB = this.openOrCreateDatabase("Attendance", MODE_PRIVATE, null);

                    vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");
                    //vattendanceDB.execSQL("UPDATE vattendance SET positions = "+POSITION+" WHERE subject = '"+subName+"'");

                    // zattendanceDB.execSQL("INSERT INTO vattendance (positions,subject, present,absent,minAtt) VALUES (1,'es',0,40, 10)");

                    // ERROR LINE //
                    /////////////// vattendanceDB.execSQL("INSERT INTO vattendance (positions, subject, present, absent, minAtt) VALUES ("+position+", '"+subName+"'  , 0,0,0)");



                    Cursor c = vattendanceDB.rawQuery("SELECT * FROM vattendance", null);

                    int positionsIndex = c.getColumnIndex("positions");
                    int subjectIndex = c.getColumnIndex("subject");
                    int presentIndex = c.getColumnIndex("present");
                    int absentIndex = c.getColumnIndex("absent");
                    int minAttIndex = c.getColumnIndex("minAtt");
                    int idIndex = c.getColumnIndex("id");

                    c.moveToFirst();

                    count = c.getCount();
                    //POSITION = id ;

                    for (int i = 0; i <= count -1; i++) {

                        if(i== position)
                        {
                            //DISPLAY THE DATABASE FIELDS IN THEIR RESPECTIVE TEXTFEILDS
                            Log.i("POSITION of subject = ", Long.toString(id));
                            Log.i("positions = ", Integer.toString(c.getInt(positionsIndex)));
                            Log.i("subject = ", c.getString(subjectIndex));
                            Log.i("id = ", Integer.toString(c.getInt(idIndex)));
                            Log.i("present = ", Double.toString(c.getDouble(presentIndex)));
                            Log.i("absent = ", Double.toString(c.getDouble(absentIndex)));
                            Log.i("cursor count = ", Integer.toString(count));


                            break;
                        }

                        c.moveToNext();

                    }

                    int ID = (int) id;
                    subName = subjectList.get(ID);
                    Intent intent = new Intent(getApplicationContext(),AttendanceActivity2.class);
                    intent.putExtra("subjectName", subName);
                    intent.putExtra("position",POSITION);
                    startActivity(intent);

                }
                catch(Exception e ){
                    e.printStackTrace() ;
                    //  Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Error in onCreate() database of AttendanceActivity!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //INSERT LONG CLICK LISTENER CODE HERE!!!

        subjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long id) {

                new AlertDialog.Builder(AttendanceActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Subject")
                        .setMessage("Do you really want to delete this subject?")
                        .setPositiveButton( "Yes"  , new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        subjectList.remove(i);
                                        arrayAdapter.notifyDataSetChanged();
                                        //subjectList.remove(i);

                                        try {

                                            sharedPreferences.edit().putString("SUBJECTLIST", ObjectSerializer.serialize(subjectList)).apply();

                                            SQLiteDatabase vattendanceDB = AttendanceActivity.this.openOrCreateDatabase("Vattendance", MODE_PRIVATE, null);

                                            vattendanceDB.execSQL("CREATE TABLE IF NOT EXISTS vattendance (positions INT, subject VARCHAR, present DOUBLE, absent DOUBLE, minAtt DOUBLE, id INTEGER PRIMARY KEY)");

                                            Cursor c = vattendanceDB.rawQuery("SELECT * FROM vattendance", null);
                                            count = c.getCount();
                                            int positionsIndex = c.getColumnIndex("positions");
                                            int subjectIndex = c.getColumnIndex("subject");
                                            int presentIndex = c.getColumnIndex("present");
                                            int absentIndex = c.getColumnIndex("absent");
                                            int minAttIndex = c.getColumnIndex("minAtt");
                                            int idIndex = c.getColumnIndex("id");

                                            c.moveToFirst();
                                            int m;
                                            for(int k = 0; k <= count -1; k++)
                                            {
                                                if(k > i)
                                                {
                                                    m=k-1;

                                                    String newSubject = c.getString(subjectIndex);
                                                    double newPresent = c.getDouble(presentIndex);
                                                    double newAbsent = c.getDouble(absentIndex);
                                                    double newMinAtt = c.getDouble(minAttIndex);

                                                    //c.moveToPrevious();

                                                    vattendanceDB.execSQL("UPDATE vattendance SET subject = '"+newSubject+"' WHERE positions = "+m+"");
                                                    vattendanceDB.execSQL("UPDATE vattendance SET present = "+newPresent+" WHERE positions = "+m+"");
                                                    vattendanceDB.execSQL("UPDATE vattendance SET absent = "+newAbsent+" WHERE positions = "+m+"");
                                                    vattendanceDB.execSQL("UPDATE vattendance SET minAtt = "+newMinAtt+" WHERE positions = "+m+"");

                                                }

                                                //c.moveToNext();
                                                c.moveToNext();

                                            }

                                            c.moveToLast();
                                            //count = c.getCount();
                                            countpos = count-1;
                                            vattendanceDB.execSQL("DELETE FROM vattendance WHERE positions = "+countpos+"");

                                        }

                                        catch(Exception e) {

                                            e.printStackTrace();
                                            Toast.makeText(AttendanceActivity.this, "Error in deleting subject! in OnCreate()", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                        )
                        .setNegativeButton("No" ,  null )
                        .show();

                return true;
            }
        });

        /*
        ArrayAdapter<String> adapter=new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, listItems){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);


                textView.setTextColor(Color.BLUE);

                return view;
            }
        };


        setListAdapter(adapter);
        */

    }

}
