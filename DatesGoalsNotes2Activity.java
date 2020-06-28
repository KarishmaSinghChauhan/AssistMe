package com.example.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DatesGoalsNotes2Activity extends AppCompatActivity {

    Intent intent;
    int pos;

    /*public void updateNotes(View view)
    {
        Toast.makeText(this, "updateNotes() function called!", Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_goals_notes2);

        intent = getIntent();
        String notes = intent.getStringExtra("notesMaterial");
        final EditText notesEditText = (EditText)findViewById(R.id.EditTextId);
        // notesEditText.setText(notes);

        DatesGoalsNotesActivity.sharedPreferences = this.getSharedPreferences("com.example.karishma.assistme", Context.MODE_PRIVATE);

        try{
            DatesGoalsNotesActivity.sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(DatesGoalsNotesActivity.notesList)).apply();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        pos = intent.getIntExtra("listPosition",-1);
        if (pos != -1) {
            notesEditText.setText(notes);
        }
        else {

            DatesGoalsNotesActivity.notesList.add("");
            pos = DatesGoalsNotesActivity.notesList.size() - 1;
            DatesGoalsNotesActivity.arrayAdapter.notifyDataSetChanged();
        }

        notesEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

                DatesGoalsNotesActivity.notesList.set(pos, notesEditText.getText().toString());
                DatesGoalsNotesActivity.notesListView.setAdapter(DatesGoalsNotesActivity.arrayAdapter);
                try {
                    DatesGoalsNotesActivity.sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(DatesGoalsNotesActivity.notesList)).apply();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
