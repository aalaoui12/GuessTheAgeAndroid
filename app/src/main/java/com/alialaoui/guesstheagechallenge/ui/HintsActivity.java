package com.alialaoui.guesstheagechallenge.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alialaoui.guesstheagechallenge.R;

public class HintsActivity extends AppCompatActivity {

    Button exposeNum;
    Button removeNums;
    Button solveQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        exposeNum = (Button) findViewById(R.id.exposeNum);
        removeNums = (Button) findViewById(R.id.removeNums);
        solveQuestion = (Button) findViewById(R.id.solveQuestion);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GillSansC-Bold.ttf");
        exposeNum.setTypeface(font);
        removeNums.setTypeface(font);
        solveQuestion.setTypeface(font);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        exposeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getString(R.string.hint_code), 0);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        removeNums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getString(R.string.hint_code), 1);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        solveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getString(R.string.hint_code), 2);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
