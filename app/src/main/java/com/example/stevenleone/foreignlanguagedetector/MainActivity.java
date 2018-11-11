package com.example.stevenleone.foreignlanguagedetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btnTest;
    private TextView testStringInput;
    private TextView testStringOutput;

    private SourceModel SourceModels[] = new SourceModel[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = (Button) findViewById(R.id.btnTest);
        testStringInput = (EditText) findViewById(R.id.testStringInput);
        testStringOutput = findViewById(R.id.testStringOutput);

        try {
            //Making Source Models
            SourceModel german = new SourceModel(this,"German", "German.corpus");
            SourceModel spanish = new SourceModel(this,"Spanish", "Spanish.corpus");
            SourceModel english = new SourceModel(this,"English", "English.corpus");
            SourceModel french = new SourceModel(this,"French", "French.corpus");

            //Adding Source Models to Array
            SourceModels[0] = german;
            SourceModels[1] = spanish;
            SourceModels[2] = english;
            SourceModels[3] = french;
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "There was an error! Files couldn't be found.",
                    Toast.LENGTH_LONG).show();
        }

        //Begin Here
        btnTest.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SourceModel language = getLikeliest(testStringInput.getText().toString());
                String text = "These words are most likely " + language.getName();
                testStringOutput.setText(text);
            }
        });
    }

    public void determineLanguage() {
        SourceModel language = getLikeliest("This is an english String");
        String text = "These words are most likely " + language.getName();
        testStringOutput.setText(text);
    }

    public SourceModel getLikeliest(String testS) {
        boolean firstRound = true;
        SourceModel likeliest = SourceModels[0];
        for (int i =0; i < SourceModels.length; i++) {
            if (firstRound) {
                likeliest = SourceModels[i];
                firstRound = false;
            } else if (SourceModels[i].probability(testS) > likeliest.probability(testS)){
                likeliest = SourceModels[i];
            }
        }
        return likeliest;
    }
}
