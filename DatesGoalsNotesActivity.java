package com.example.assistme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DatesGoalsNotesActivity extends AppCompatActivity {

    Intent intent;
    static ArrayList<String> notesList;
    static ListView notesListView;
    static ArrayAdapter arrayAdapter;
    static SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_notes_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.addNotesId) {
            intent = new Intent(getApplicationContext(), DatesGoalsNotes2Activity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.deleteAllId) {
            new AlertDialog.Builder(DatesGoalsNotesActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete Notes")
                    .setMessage("Do you really want to delete all the saved notes?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            notesList.clear();

                            if(notesList.size()==0) {
                                notesList.add("Tap to add your first entry...");
                            }

                            arrayAdapter.notifyDataSetChanged();

                            try{
                                sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(notesList)).apply();
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                                Toast.makeText(DatesGoalsNotesActivity.this,"Error in deleting all Notes via the menu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_goals_notes);

        notesListView = (ListView)findViewById(R.id.ListViewId);
        notesList = new ArrayList<>();
        notesList.add("Tap to add your first entry...");
        sharedPreferences = this.getSharedPreferences("com.example.karishma.assistme", Context.MODE_PRIVATE);

        try{
            notesList =(ArrayList)ObjectSerializer.deserialize(sharedPreferences.getString("NotesList", ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(notesList == null) {
            notesList = new ArrayList<>();
            notesList.add("Tap to add your first entry...");
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            intent = new Intent(getApplicationContext(), DatesGoalsNotes2Activity.class);
            intent.putExtra("notesMaterial", notesList.get(position));
            intent.putExtra("listPosition", position);
            startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                 new AlertDialog.Builder(DatesGoalsNotesActivity.this)
                         .setIcon(android.R.drawable.ic_dialog_alert)
                         .setTitle("Delete Note")
                         .setMessage("Do you really want to delete this note?")
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                                 notesList.remove(position);

                                 if(notesList.size()==0) {
                                     notesList.add("Tap to add your first entry...");
                                 }

                                 arrayAdapter.notifyDataSetChanged();

                                 try{
                                     sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(notesList)).apply();
                                 }
                                 catch(Exception e) {
                                     e.printStackTrace();
                                     Toast.makeText(DatesGoalsNotesActivity.this,"Error in deleting Note! in OnCreate()", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         })
                         .setNegativeButton("No", null)
                         .show();
                 return true;
             }
        });
    }
}
