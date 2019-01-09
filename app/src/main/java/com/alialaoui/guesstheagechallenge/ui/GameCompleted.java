package com.alialaoui.guesstheagechallenge.ui;

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

public class GameCompleted extends AppCompatActivity {

    TextView congratsText;
    TextView congratsText2;
    ImageView person;

    Button levelsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_completed);

        congratsText = (TextView) findViewById(R.id.congratsText);
        congratsText2 = (TextView) findViewById(R.id.congratsText2);
        person = (ImageView) findViewById(R.id.person);

        levelsButton = (Button) findViewById(R.id.levelsButton);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GillSansC-Bold.ttf");
        congratsText.setTypeface(font);
        congratsText2.setTypeface(font);

        final Intent intent = getIntent();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), intent.getIntExtra(getString(R.string.image_view_id), -1));
        person.setImageDrawable(drawable);

        levelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
