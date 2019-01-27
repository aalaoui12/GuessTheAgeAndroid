package com.alialaoui.guesstheagechallenge.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;

import com.alialaoui.guesstheagechallenge.R;
import com.alialaoui.guesstheagechallenge.model.Game;
import com.alialaoui.guesstheagechallenge.model.Level;



public class MainActivity extends AppCompatActivity {

    private Game game = new Game();

    AdView mAdView;

    private InterstitialAd mInterstitialAd;

    private ImageView person;
    private ImageView bar1;
    private ImageView bar2;
    private TextView questionView;
    private TextView numCoinsView;
    private TextView firstNum;
    private TextView secondNum;
    private TextView wrongText;
    private Button startHints;
    private Button levels;
    private Button nums[];

    private boolean isFirstLocked; //expose letter hint
    private boolean isSecondLocked;

    private boolean isWrong = false; //for "wrong view"

    private String privacyPolicy = "https://docs.google.com/document/d/1CNUR6psJrcuV5HTFpkHqusIN44f-i2hUbLz6cpPBe0c/edit?usp=sharing";

    //get sound files
    MediaPlayer chooseLetter1Sound;
    MediaPlayer chooseLetter2Sound;
    MediaPlayer removeLetterSound;
    MediaPlayer wrongAnswer;
    MediaPlayer levelDone;

    private boolean isSoundToggled = true;


    //you should store these in values -> int soon
    public static final int LEVELSACTIVITYCODE = 0;
    public static final int HINTSACTIVITYCODE = 1;
    public static final int LEVELCOMPLETEDACTIVITYCODE = 2;
    public static final int GAMECOMPLETEDACTIVITYCODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.admob_app_id)); //initialize SDK
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest); //load ad

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.official_interstitial_id));

        person = (ImageView) findViewById(R.id.girlView);
        bar1 = (ImageView) findViewById(R.id.bar1);
        bar2 = (ImageView) findViewById(R.id.bar2);
        questionView = (TextView) findViewById(R.id.questionView);
        numCoinsView = (TextView) findViewById(R.id.coinsView);
        firstNum = (TextView) findViewById(R.id.firstNum);
        secondNum = (TextView) findViewById(R.id.secondNum);
        wrongText = (TextView) findViewById(R.id.wrongView);
        startHints = (Button) findViewById(R.id.startHints);
        levels = (Button) findViewById(R.id.levelsButton);

        //set font for everything
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GillSansC-Bold.ttf");
        firstNum.setTypeface(font);
        secondNum.setTypeface(font);
        wrongText.setTypeface(font);
        questionView.setTypeface(font);
        numCoinsView.setTypeface(font);
        startHints.setTypeface(font);
        levels.setTypeface(font);

        nums = new Button[14];
        nums[0] = (Button) findViewById(R.id.num1);;
        nums[1] = (Button) findViewById(R.id.num2);
        nums[2] = findViewById(R.id.num3);;
        nums[3] = findViewById(R.id.num4);
        nums[4] = findViewById(R.id.num5);
        nums[5] = findViewById(R.id.num6);
        nums[6] = findViewById(R.id.num7);
        nums[7] = findViewById(R.id.num8);
        nums[8] = findViewById(R.id.num9);
        nums[9] = findViewById(R.id.num10);
        nums[10] = findViewById(R.id.num11);
        nums[11] = findViewById(R.id.num12);
        nums[12] = findViewById(R.id.num13);
        nums[13] = findViewById(R.id.num14);

        for(int i = 0; i < nums.length; i++) {
            nums[i].setTypeface(font);
        }

        chooseLetter1Sound = MediaPlayer.create(getApplicationContext(), R.raw.choose_letter_01);
        chooseLetter2Sound = MediaPlayer.create(getApplicationContext(), R.raw.choose_letter_02);
        removeLetterSound = MediaPlayer.create(getApplicationContext(), R.raw.remove_letter);
        wrongAnswer = MediaPlayer.create(getApplicationContext(), R.raw.wrong_word_long);
        levelDone = MediaPlayer.create(getApplicationContext(), R.raw.level_done);


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int numLevel = sharedPref.getInt(getString(R.string.num_level_key), 0);
        Level loadLevel = game.getLevel(numLevel);

        int numCoins = sharedPref.getInt(getString(R.string.num_coins_key), 25);
        makeLevel(loadLevel, numCoins);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //getting activity results. request code is code to launch activity, differentiating which activities are returning.

        if(requestCode == LEVELSACTIVITYCODE) {
            if(resultCode == Activity.RESULT_OK) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                int level = data.getIntExtra(getString(R.string.num_level_key), -1); //should never reach default value
                makeLevel(game.getLevel(level), sharedPref.getInt(getString(R.string.num_coins_key), 25));
            }
        }
        if(requestCode == HINTSACTIVITYCODE) {
            if(resultCode == Activity.RESULT_OK) {
                int hintsCode = data.getIntExtra(getString(R.string.hint_code), -1); //should never reach default value
                if(hintsCode == 0) {
                    exposeNum();
                }
                else if(hintsCode == 1) {
                    removeNums();
                }
                else if(hintsCode == 2) {
                    solveLevel();
                }
            }
        }
        if(requestCode == LEVELCOMPLETEDACTIVITYCODE) {
            if(resultCode == Activity.RESULT_OK) {
                loadNewLevel(data.getIntExtra(getString(R.string.num_level_key), -1));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sound_toggle:
                if(isSoundToggled) {
                    isSoundToggled = false;
                    item.setTitle("Toggle Sound On");
                }
                else {
                    isSoundToggled = true;
                    item.setTitle("Toggle Sound Off");
                }

                return true;
            case R.id.privacy_policy:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicy));
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startLevelDoneActivity(int numLevel) {
        levelDone.start();
        mInterstitialAd.show(); //shows ad, this can be changed to appear once out of every two times (rand)
        //takes CURRENT LEVEL, also starts GameCompleted Activity if 59th level
        if(numLevel == 59) { //make sure to change this with new levels, or make a constant for that

            Intent intent = new Intent(getApplicationContext(), GameCompleted.class);
            intent.putExtra(getString(R.string.image_view_id), game.getLevel(numLevel).getGirl());
            intent.putExtra(getString(R.string.level_answer), game.getLevel(numLevel).getAge());
            intent.putExtra(getString(R.string.num_level_key), numLevel);

            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LevelCompletedActivity.class);
            intent.putExtra(getString(R.string.image_view_id), game.getLevel(numLevel).getGirl());
            intent.putExtra(getString(R.string.level_answer), game.getLevel(numLevel).getAge());
            intent.putExtra(getString(R.string.num_level_key), numLevel);

            startActivityForResult(intent, LEVELCOMPLETEDACTIVITYCODE);
        }
    }



    public void makeLevel(Level level, int numCoins) { //catching a bug where getlevel method in game returns null
        final Level thisLevel = level;
        game.setThisLevel(level);

        mInterstitialAd.loadAd(new AdRequest.Builder().build()); //prepare interstitial

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE); //storing latest level number
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.getInt(getString(R.string.num_level_key), 0) <= thisLevel.getNumLevel()) {
            editor.putInt(getString(R.string.num_level_key), thisLevel.getNumLevel());
            editor.apply();
        }
        editor.putInt(getString(R.string.num_coins_key), numCoins);
        editor.apply();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), thisLevel.getGirl());
        person.setImageDrawable(drawable);

        isFirstLocked = false; //resets locked numbers, this is triggered by hints
        isSecondLocked = false;
        firstNum.setTextColor(Color.WHITE);
        secondNum.setTextColor(Color.WHITE);
        bar1.setColorFilter(Color.WHITE);
        bar2.setColorFilter(Color.WHITE);


        numCoinsView.setText(numCoins + " coins");
        levels.setText("Level " + (level.getNumLevel() + 1));
        firstNum.setText("");
        secondNum.setText("");

        randomizeButtons(thisLevel);

        levels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                int levelLimit = sharedPref.getInt(getString(R.string.num_level_key), 0);
                Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
                intent.putExtra(getString(R.string.num_level_key), levelLimit);
                startActivityForResult(intent, LEVELSACTIVITYCODE);
            }
        });

        startHints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HintsActivity.class);
                startActivityForResult(intent, HINTSACTIVITYCODE);
            }
        });

        firstNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFirstLocked) {
                    if(isWrong)
                        toggleWrongView();

                    int num = Integer.parseInt(firstNum.getText().toString());
                    firstNum.setText("");
                    for (int i = 0; i < nums.length; i++) {
                        if (Integer.parseInt(nums[i].getText().toString()) == num &&
                                nums[i].getVisibility() == View.INVISIBLE) {
                            nums[i].setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    if(isSoundToggled)
                        removeLetterSound.start();
                }
            }
        });
        secondNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSecondLocked) {
                    if(isWrong)
                        toggleWrongView();
                    int num = Integer.parseInt(secondNum.getText().toString());
                    secondNum.setText("");
                    for (int i = 0; i < nums.length; i++) {
                        if (Integer.parseInt(nums[i].getText().toString()) == num &&
                                nums[i].getVisibility() == View.INVISIBLE) {
                            nums[i].setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    if(isSoundToggled)
                        removeLetterSound.start();
                }
            }
        });

        nums[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[0].getText().toString());
                    nums[0].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[0].getText().toString());
                    nums[0].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[0].getText().toString());
                    nums[0].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        nums[0].setVisibility(View.INVISIBLE);
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        nums[0].setVisibility(View.INVISIBLE);
                        toggleWrongView();
                    }
                }
            }
        });
        nums[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[1].getText().toString());
                    nums[1].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[1].getText().toString());
                    nums[1].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[1].getText().toString());
                    nums[1].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        nums[1].setVisibility(View.INVISIBLE);
                        toggleWrongView();
                    }
                }
            }
        });
        nums[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[2].getText().toString());
                    nums[2].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[2].getText().toString());
                    nums[2].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[2].getText().toString());
                    nums[2].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[3].getText().toString());
                    nums[3].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[3].getText().toString());
                    nums[3].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[3].getText().toString());
                    nums[3].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[4].getText().toString());
                    nums[4].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[4].getText().toString());
                    nums[4].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[4].getText().toString());
                    nums[4].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        nums[4].setVisibility(View.INVISIBLE);
                        toggleWrongView();
                    }
                }
            }
        });
        nums[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[5].getText().toString());
                    nums[5].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[5].getText().toString());
                    nums[5].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        nums[5].setVisibility(View.INVISIBLE);
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[5].getText().toString());
                    nums[5].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        nums[5].setVisibility(View.INVISIBLE);
                        toggleWrongView();
                    }
                }
            }
        });
        nums[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[6].getText().toString());
                    nums[6].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[6].getText().toString());
                    nums[6].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[6].getText().toString());
                    nums[6].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[7].getText().toString());
                    nums[7].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[7].getText().toString());
                    nums[7].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[7].getText().toString());
                    nums[7].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[8].getText().toString());
                    nums[8].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[8].getText().toString());
                    nums[8].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[8].getText().toString());
                    nums[8].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[9].getText().toString());
                    nums[9].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[9].getText().toString());
                    nums[9].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[9].getText().toString());
                    nums[9].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[10].getText().toString());
                    nums[10].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[10].getText().toString());
                    nums[10].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[10].getText().toString());
                    nums[10].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[11].getText().toString());
                    nums[11].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[11].getText().toString());
                    nums[11].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[11].getText().toString());
                    nums[11].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[12].getText().toString());
                    nums[12].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[12].getText().toString());
                    nums[12].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[12].getText().toString());
                    nums[12].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
        nums[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNum.getText().toString().matches("") && secondNum.getText().toString().matches("")) {
                    firstNum.setText(nums[13].getText().toString());
                    nums[13].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                }
                else if(secondNum.getText().toString().matches("")) {
                    secondNum.setText(nums[13].getText().toString());
                    nums[13].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter2Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
                else if(firstNum.getText().toString().matches("")) {
                    firstNum.setText(nums[13].getText().toString());
                    nums[13].setVisibility(View.INVISIBLE);
                    if(isSoundToggled)
                        chooseLetter1Sound.start();
                    //if right answer, load new level
                    if(Integer.parseInt(firstNum.getText().toString()) == thisLevel.firstNum() &&
                            Integer.parseInt(secondNum.getText().toString()) == thisLevel.secondNum()) {
                        //update coins
                        startLevelDoneActivity(thisLevel.getNumLevel());
                    }
                    else {
                        toggleWrongView();
                    }
                }
            }
        });
    }
    public void randomizeButtons(Level level) { //randomizes answer buttons while *making sure right answer is possible*
        for(int i = 0; i < nums.length; i++) {
            nums[i].setVisibility(View.VISIBLE);
            nums[i].setText(String.valueOf((int) (Math.random() * 9)));
        }

        int firstNumLoc = (int) (Math.random() * 14);
        nums[firstNumLoc].setText(String.valueOf(level.firstNum()));

        int secondNumLoc = (int) (Math.random() * 14);
        while(secondNumLoc == firstNumLoc) {
            secondNumLoc = (int) (Math.random() * 14);
        }
        nums[secondNumLoc].setText(String.valueOf(level.secondNum()));
    }


    public void loadNewLevel(int numLevel) { //takes CURRENT level, generates next level
        firstNum.setText("");
        secondNum.setText("");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(sharedPref.getInt(getString(R.string.num_level_key), 0) == numLevel) {
            int numCoins = sharedPref.getInt(getString(R.string.num_coins_key), 25);
            editor.putInt(getString(R.string.num_coins_key), numCoins + 1);
            editor.apply();
        }


        Level level = game.getLevel(numLevel + 1);
        makeLevel(level, sharedPref.getInt(getString(R.string.num_coins_key), 0)); //gives user 3 coins when appropriate
    }

    public void exposeNum() { //hint that shows one digit of the answer
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int currentCoins = sharedPref.getInt(getString(R.string.num_coins_key), 0);
        if(currentCoins < 15) {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.num_coins_key), currentCoins - 15);
            editor.apply();

            numCoinsView.setText((sharedPref.getInt(getString(R.string.num_coins_key), 0)) + " coins");

            if(firstNum.getText().toString().matches("")) {
                if(!secondNum.getText().toString().matches("")) {
                    secondNum.setText("");
                    for(int i = 0; i < nums.length; i++) {
                        if(nums[i].getVisibility() == View.INVISIBLE) {
                            nums[i].setVisibility(View.VISIBLE);
                        }
                    }
                }
                firstNum.setText(String.valueOf(game.getThisLevel().firstNum()));
                for(int i = 0; i < nums.length; i++) {
                    if(nums[i].getText().toString().equals(firstNum.getText().toString())) {
                        nums[i].setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                isFirstLocked = true;
                firstNum.setTextColor(Color.RED);
                bar1.setColorFilter(Color.RED);

            }
            else if(secondNum.getText().toString().matches("")) {
                firstNum.setText("");
                for(int i = 0; i < nums.length; i++) {
                    if(nums[i].getVisibility() == View.INVISIBLE)
                        nums[i].setVisibility(View.VISIBLE);
                }
                secondNum.setText(String.valueOf(game.getThisLevel().secondNum()));
                for(int i = 0; i < nums.length; i++) {
                    if(nums[i].getText().toString().equals(secondNum.getText().toString())) {
                        nums[i].setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                isSecondLocked = true;
                secondNum.setTextColor(Color.RED);
                bar2.setColorFilter(Color.RED);
            }
            else {
                secondNum.setText("");
                for(int i = 0; i < nums.length; i++) {
                    if(nums[i].getVisibility() == View.INVISIBLE)
                        nums[i].setVisibility(View.VISIBLE);
                }
                firstNum.setText(String.valueOf(game.getThisLevel().firstNum()));
                for(int i = 0; i < nums.length; i++) {
                    if(nums[i].getText().toString().equals(firstNum.getText().toString())) {
                        nums[i].setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                isFirstLocked = true;
                firstNum.setTextColor(Color.RED);
                bar1.setColorFilter(Color.RED);
            }
        }
    }
    public void removeNums() { //hint that removes potential answer choices
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int currentCoins = sharedPref.getInt(getString(R.string.num_coins_key), 0);

        if (currentCoins < 15) {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.num_coins_key), currentCoins - 15);
            editor.apply();

            numCoinsView.setText((sharedPref.getInt(getString(R.string.num_coins_key), 0)) + " coins");

            int firstCounter = 1;
            int secondCounter = 1;

            for (int i = 0; i < nums.length; i++) {
                if (nums[i].getText().toString().equals(String.valueOf(game.getThisLevel().firstNum())) && firstCounter != 0)
                    firstCounter--;
                else if (nums[i].getText().toString().equals(String.valueOf(game.getThisLevel().secondNum())) && secondCounter != 0)
                    secondCounter--;
                else
                    nums[i].setVisibility(View.INVISIBLE);
            }

        }
    }
    public void solveLevel() { //"hint" that actually just passes the level
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int currentCoins = sharedPref.getInt(getString(R.string.num_coins_key), 0);

        if (currentCoins < 30) {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.num_coins_key), currentCoins - 30);
            editor.apply();

            startLevelDoneActivity(game.getThisLevel().getNumLevel());
        }
    }

    public void toggleWrongView() { //toggle the wrong view when answer is wrong, only remove when user takes out number
        if(!isWrong) {
            wrongAnswer.start();
            levels.setVisibility(View.INVISIBLE);
            numCoinsView.setVisibility(View.INVISIBLE);
            wrongText.setVisibility(View.VISIBLE);
            isWrong = true;
        }
        else {
            levels.setVisibility(View.VISIBLE);
            numCoinsView.setVisibility(View.VISIBLE);
            wrongText.setVisibility(View.INVISIBLE);
            isWrong = false;
        }
    }
}
