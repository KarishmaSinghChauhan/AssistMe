
package com.example.assistme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {

    static ListView notesListView;
    static ListAdapter arrayAdapter;
    static ArrayList<HashMap<String, String>> notesList;

    Intent intent;
    DbHandler db;
    String from[];
    int to[];

    public void addFirstNote() {

        String title = "My First Note";
        String body = "Tap to add your first entry...";
        db.insertNoteDetails(title,body);
    }

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
            intent = new Intent(getApplicationContext(), Notes2Activity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.deleteAllId) {
            new AlertDialog.Builder(NotesActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete Notes")
                    .setMessage("Do you really want to delete all the saved notes?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            db.deleteAllNotes();
                            addFirstNote();

                            notesList = db.getNotes();
                            arrayAdapter = new SimpleAdapter(NotesActivity.this, notesList, R.layout.notes_list_row, from, to);
                            notesListView.setAdapter(arrayAdapter);
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
        setContentView(R.layout.activity_notes);

        notesListView = (ListView)findViewById(R.id.ListViewId);
        db = new DbHandler(NotesActivity.this);
        from = new String[]{"title", "body"};
        to = new int[]{R.id.noteTitle, R.id.noteBody};
        notesList = db.getNotes();
        if(notesList == null || notesList.size() == 0)
        {
            addFirstNote();
        }

        notesList = db.getNotes();
        arrayAdapter = new SimpleAdapter(this, notesList, R.layout.notes_list_row, from, to);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            intent = new Intent(getApplicationContext(), Notes2Activity.class);
            intent.putExtra("listPosition", position);
            startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                 new AlertDialog.Builder(NotesActivity.this)
                         .setIcon(android.R.drawable.ic_dialog_alert)
                         .setTitle("Delete Note")
                         .setMessage("Do you really want to delete this note?")
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                                 notesList = db.getNotes();
                                 HashMap<String, String> note = notesList.get(position);
                                 int noteid = Integer.parseInt(note.get("id"));
                                 db.deleteNote(noteid);

                                 notesList = db.getNotes();
                                 if(notesList.size()==0) {
                                     addFirstNote();
                                 }

                                 notesList = db.getNotes();
                                 arrayAdapter = new SimpleAdapter(NotesActivity.this, notesList, R.layout.notes_list_row, from, to);
                                 notesListView.setAdapter(arrayAdapter);
                             }
                         })
                         .setNegativeButton("No", null)
                         .show();
                 return true;
             }
        });
    }
}
