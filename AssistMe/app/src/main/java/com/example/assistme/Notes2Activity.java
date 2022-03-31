package com.example.assistme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Notes2Activity extends AppCompatActivity {

    EditText notesTitle;
    EditText notesBody;
    Intent intent;
    DbHandler db;
    ArrayList<HashMap<String, String>> notesList;
    String[] from;
    int[] to;
    int pos;

    public void share(View view) {

        Intent it = new Intent(Intent.ACTION_SEND);
        String title = notesTitle.getText().toString();
        String body = notesBody.getText().toString();
        String message = "Note shared from AssistMe App-\nTitle: "+title + " \nBody: "+body;
        it.putExtra(Intent.EXTRA_TEXT, message);
//        it.setType("message/rfc822");
        it.setType("text/plain");
        startActivity(Intent.createChooser(it,"Choose app for sharing"));
    }

    public void setReminder(View view) {

        EditText minsEditText = (EditText)findViewById(R.id.minsEditTextId);
        String minsStr = minsEditText.getText().toString();
        int mins;
        if(minsStr == null || minsStr.length()==0) {
            mins = 60;
        } else {
            mins = (int)(Double.parseDouble(minsStr));
        }

        String title = notesTitle.getText().toString();
        String body = notesBody.getText().toString();
        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis()+mins*60*1000;
        long endTime = startTime + 60*60*1000;
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("allDay", false);
        intent.putExtra("beginTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("title", title);
        intent.putExtra("description", body);
        startActivity(intent);
//        Toast.makeText(Notes2Activity.this,"Reminder set!", Toast.LENGTH_SHORT).show();
    }

    protected void updateNote(int pos) {

        HashMap<String, String> note = notesList.get(pos);
        int noteid = Integer.parseInt(note.get("id"));
        String title = notesTitle.getText().toString();
        String body = notesBody.getText().toString();
        db.updateNoteDetails(title, body, noteid);

        notesList = db.getNotes();
        NotesActivity.arrayAdapter = new SimpleAdapter(Notes2Activity.this, notesList, R.layout.notes_list_row, from, to);
        NotesActivity.notesListView.setAdapter(NotesActivity.arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes2);

        notesTitle = (EditText)findViewById(R.id.EditTextTitleId);
        notesBody = (EditText)findViewById(R.id.EditTextBodyId);
        db = new DbHandler(Notes2Activity.this);
        notesList = db.getNotes();
        from = new String[]{"title", "body"};
        to = new int[]{R.id.noteTitle, R.id.noteBody};
        intent = getIntent();
        pos = intent.getIntExtra("listPosition",-1);
        if (pos != -1) {
            HashMap<String, String> note = notesList.get(pos);
            notesTitle.setText(note.get("title"));
            notesBody.setText(note.get("body"));
        }
        else {
            String title = "Untitled note";
            String body = "Empty body";
            db.insertNoteDetails(title,body);
            notesList = db.getNotes();
            NotesActivity.arrayAdapter = new SimpleAdapter(Notes2Activity.this, notesList, R.layout.notes_list_row, from, to);
            NotesActivity.notesListView.setAdapter(NotesActivity.arrayAdapter);
            pos = notesList.size()-1;
        }


        notesTitle.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                updateNote(pos);
            }
        });

        notesBody.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                updateNote(pos);
            }
        });
    }
}
