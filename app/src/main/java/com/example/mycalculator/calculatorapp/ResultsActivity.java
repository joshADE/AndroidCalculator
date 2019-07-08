

package com.example.mycalculator.calculatorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.EXTRA_MESSAGE)) {
            String result = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

            TextView textView = findViewById(R.id.textView2);
            textView.setText(result);
        }
    }
}
