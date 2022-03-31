package com.example.assistme;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TextToSpeechActivity extends AppCompatActivity {

    SeekBar speedSeekBar;
    SeekBar pitchSeekBar ;
    TextToSpeech tts;
    Button translateButton;
    Button stopButton;
    Locale loc;
    Voice voice;
    Button saveButton;

    public void save(View view)
    {
        saveButton = (Button)findViewById(R.id.saveButtonId);
        saveButton.setEnabled(false);
        EditText editText = (EditText)findViewById(R.id.editText1Id);

        DbHandler db = new DbHandler(TextToSpeechActivity.this);
        String title = "Saved Note from TextToSpeech";
        String body = editText.getText().toString();
        db.insertNoteDetails(title, body);
    }

    public void stop(View view) {

        stopButton = (Button)findViewById(R.id.stopButtonId);
        tts.stop();
        stopButton.setEnabled(false);
    }

    public void enable(View view) {

        saveButton = (Button)findViewById(R.id.saveButtonId);
        saveButton.setEnabled(true);
    }

    public void translate(View view) {

    }

    public void langRadioGroupMethod() {

        RadioGroup langRadioGroup = (RadioGroup)findViewById(R.id.langRadioGroupId);
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

    public void voiceRadioGroupMethod() {

        RadioGroup voiceRadioGroup = (RadioGroup)findViewById(R.id.voiceRadioGroupId);
        RadioButton selectedVoice = (RadioButton)findViewById(voiceRadioGroup.getCheckedRadioButtonId());

        String voiceString = (String)selectedVoice.getText();
        switch(voiceString) {

            case "Female" :
                voice = null;
                break;
            case "Male" :
                Set<String> a = new HashSet<>();
                a.add("male");
                voice = new Voice("en-gb-x-rjs-network",loc,400,200,true, a);
                break;
        }
    }

    public void speak() {

        EditText editText = (EditText)findViewById(R.id.editText1Id);
        speedSeekBar = (SeekBar)findViewById(R.id.seekBarSpeedId);
        pitchSeekBar = (SeekBar)findViewById(R.id.seekBarPitchId);
        stopButton = (Button)findViewById(R.id.stopButtonId);
        saveButton = (Button)findViewById(R.id.saveButtonId);

        langRadioGroupMethod();
        voiceRadioGroupMethod();

        String text = editText.getText().toString();
        float speed = (float)speedSeekBar.getProgress()/50;
        float pitch = (float)pitchSeekBar.getProgress()/50;
        if(speed < 0.1) {
            speed = 0.1f;
            speedSeekBar.setProgress((int)(speed*50));
        }
        if(pitch < 0.1) {
            pitch = 0.1f;
            pitchSeekBar.setProgress((int)(pitch*50));
        }

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        tts.setLanguage(loc);

        if(voice != null) {
            tts.setVoice(voice);
        }

        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
        stopButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        translateButton = (Button)findViewById(R.id.buttonId);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(TextToSpeechActivity.this, "Language not supported!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        translateButton.setEnabled(true);
                    }
                }
                else {
                    Toast.makeText(TextToSpeechActivity.this, "Invalid text! Please enter again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
