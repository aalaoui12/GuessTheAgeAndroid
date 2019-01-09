package com.alialaoui.guesstheagechallenge.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alialaoui.guesstheagechallenge.R;

public class LevelsActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        //data to populate recyclerview with.
        String data[] = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7",
                "Level 8", "Level 9", "Level 10", "Level 11", "Level 12", "Level 13", "Level 14",
                "Level 15", "Level 16", "Level 17", "Level 18", "Level 19", "Level 20", "Level 21",
                "Level 22", "Level 23", "Level 24", "Level 25", "Level 26", "Level 27", "Level 28",
                "Level 29", "Level 30", "Level 31", "Level 32", "Level 33", "Level 34", "Level 35",
                "Level 36", "Level 37", "Level 38", "Level 39", "Level 40", "Level 41", "Level 42",
                "Level 43", "Level 44", "Level 45", "Level 46", "Level 47", "Level 48", "Level 49",
                "Level 50", "Level 51", "Level 52", "Level 53", "Level 54", "Level 55", "Level 56",
                "Level 57", "Level 58", "Level 59", "Level 60"};

        int buttonPics[] = {R.drawable.level1, R.drawable.level2, R.drawable.level3, R.drawable.level4,
                R.drawable.level5, R.drawable.level6, R.drawable.level7, R.drawable.level8, R.drawable.level9,
                R.drawable.level10, R.drawable.level11, R.drawable.level12, R.drawable.level13, R.drawable.level14,
                R.drawable.level15, R.drawable.level16, R.drawable.level17, R.drawable.level18, R.drawable.level19,
                R.drawable.level20, R.drawable.level21, R.drawable.level22, R.drawable.level23, R.drawable.level24,
                R.drawable.level25, R.drawable.level26, R.drawable.level27, R.drawable.level28, R.drawable.level29,
                R.drawable.level30, R.drawable.level31, R.drawable.level32, R.drawable.level33, R.drawable.level34,
                R.drawable.level35, R.drawable.level36, R.drawable.level37, R.drawable.level38, R.drawable.level39,
                R.drawable.level40, R.drawable.level41, R.drawable.level42, R.drawable.level43, R.drawable.level44,
                R.drawable.level45, R.drawable.level46, R.drawable.level47, R.drawable.level48, R.drawable.level49,
                R.drawable.level50, R.drawable.level51, R.drawable.level52, R.drawable.level53, R.drawable.level54,
                R.drawable.level55, R.drawable.level56, R.drawable.level57, R.drawable.level58, R.drawable.level59,
                R.drawable.level60 };

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.levelGrid);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        int currentLevelNum = getIntent().getIntExtra(getString(R.string.num_level_key), 0);
        adapter = new MyRecyclerViewAdapter(this, data, buttonPics, currentLevelNum);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent levelInfo = getIntent();
        int levelLimit = levelInfo.getIntExtra(getString(R.string.num_level_key), 0);
        if(position > levelLimit) {
            Toast.makeText(this, "Level locked!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(getString(R.string.num_level_key), position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
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
