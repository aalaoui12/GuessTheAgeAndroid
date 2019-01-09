package com.alialaoui.guesstheagechallenge.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alialaoui.guesstheagechallenge.R;

public class LevelCompletedActivity extends AppCompatActivity {

    TextView wellDone;
    TextView coinsGained;
    TextView numAge;
    ImageView person;

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_completed);

        wellDone = (TextView) findViewById(R.id.wellDone);
        coinsGained = (TextView) findViewById(R.id.coinsGained);
        numAge = (TextView) findViewById(R.id.numAge);
        person = (ImageView) findViewById(R.id.person);

        continueButton = (Button) findViewById(R.id.continueButton);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GillSansC-Bold.ttf");
        wellDone.setTypeface(font);
        numAge.setTypeface(font);

        final Intent intent = getIntent();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), intent.getIntExtra(getString(R.string.image_view_id), -1));
        person.setImageDrawable(drawable);

        numAge.setText(String.valueOf(intent.getIntExtra(getString(R.string.level_answer), -1)));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getString(R.string.num_level_key), intent.getIntExtra(getString(R.string.num_level_key), -1));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
