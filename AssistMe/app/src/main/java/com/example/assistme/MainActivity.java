package com.example.assistme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Intent intent;
    ArrayAdapter<String> arrayAdapter;
    List<String> mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listViewId);
        mainList = new ArrayList<String>();
        mainList.add("Attendance");
        mainList.add("Speech to Text");
        mainList.add("Text to Speech");
        mainList.add("Notes");
        mainList.add("Random Quote");
        mainList.add("Brain Trainer");
        mainList.add("Disco Lights");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), SpeechToTextActivity.class );
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), TextToSpeechActivity.class );
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), DatesGoalsNotesActivity.class );
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), QuoteActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(), BrainTrainerActivity.class);
                        break;
                    case 6:
                        intent = new Intent(getApplicationContext(), DiscoLightsActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
