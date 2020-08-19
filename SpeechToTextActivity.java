package com.example.assistme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity {

    EditText editText;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    Locale loc;
    Button saveButton2;

    public void save(View view) {

        saveButton2 = (Button)findViewById(R.id.saveButton2Id);
        saveButton2.setEnabled(false);
        editText = (EditText)findViewById(R.id.editText2Id);

        DatesGoalsNotesActivity.sharedPreferences = this.getSharedPreferences("com.example.karishma.assistme", Context.MODE_PRIVATE);

        try {
            DatesGoalsNotesActivity.sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(DatesGoalsNotesActivity.notesList)).apply();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        DatesGoalsNotesActivity.notesList.add(editText.getText().toString());
        DatesGoalsNotesActivity.notesListView.setAdapter(DatesGoalsNotesActivity.arrayAdapter);

        try {
            DatesGoalsNotesActivity.sharedPreferences.edit().putString("NotesList", ObjectSerializer.serialize(DatesGoalsNotesActivity.notesList)).apply();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enable(View view) {
        saveButton2 = (Button)findViewById(R.id.saveButton2Id);
        saveButton2.setEnabled(true);
    }

    public void langRadioGroupMethod() {

        RadioGroup langRadioGroup = (RadioGroup)findViewById(R.id.radioGroupId);
        RadioButton selectedLang = (RadioButton)findViewById(langRadioGroup.getCheckedRadioButtonId());
        String lang = (String)selectedLang.getText();

        switch(lang) {
            case "English" :
                loc = Locale.ENGLISH;
                break;
            case "French" :
                loc = Locale.FRENCH;
                break;
            case "German" :
                loc = Locale.GERMAN;
                break;
            case "Italian" :
                loc = Locale.ITALIAN;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        checkPermission();
        langRadioGroupMethod();

        editText = (EditText)findViewById(R.id.editText2Id);
        ImageButton speakButton = (ImageButton)findViewById(R.id.speakImageButtonId);
        saveButton2 = (Button)findViewById(R.id.saveButton2Id);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, loc);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {


            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null) {

                    editText.setText(matches.get(0));
                    saveButton2.setEnabled(true);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speakButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN :
                        saveButton2.setEnabled(true);
                        editText.setText("");
                        editText.setHint("Listening..");
                        speechRecognizer.startListening(speechRecognizerIntent);

                        break;

                    case MotionEvent.ACTION_UP :
                        speechRecognizer.stopListening();
                        editText.setHint("The translated speech text will appear here..");

                        break;
                }

                return false;
            }
        });
    }

    public void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
