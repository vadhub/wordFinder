package com.abg.wordfinder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.abg.wordfinder.datasource.Configuration;
import com.abg.wordfinder.view.WordSearchView;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements WordSearchGenerator.Listener,
        WinDialogFragment.Listener, SettingsDialogFragment.ChangeLanguageListener, SettingsDialogFragment.SettingsDisplayListener {

    private WordSearchGenerator wordSearchGenerator;
    private TextView textViewWordsFound;
    private ImageView ghost;
    private WordSearchView wordsGrid;
    private Chronometer chronometer;
    private Map<String, TextView> map;
    private Configuration configuration;
    private LinearLayout linearForText;
    private List<String> words;
    private String[] temp;
    private final int row = 10;
    private final int col = 10;


//    private char[][] letters  = {
//            "АМСМСМАВКЕ".toCharArray(),
//            "ЙЦУКЕНГШЩМ".toCharArray(),
//            "ЙЦУСБЯЧЮСМ".toCharArray(),
//            "АМСМЛАВВКЕ".toCharArray(),
//            "ЙЦУКЕООШЩМ".toCharArray(),  // for icon ru ;)
//            "ЙЦУСБЯВВСМ".toCharArray(),
//            "АМСМСМАООЕ".toCharArray(),
//            "ЙЦУКЕНГШЩМ".toCharArray(),
//            "ЙЦУСБЯЧЮСМ".toCharArray(),
//            "АМСМСМАВКЕ".toCharArray()
//    };

//        private char[][] letters  = {
//                "ASCDEFGHIJ".toCharArray(),
//                "AECDEFGHIJ".toCharArray(),
//                "QWEWVCXSDF".toCharArray(),
//                "АМСМOEВВКЕ".toCharArray(),
//                "ACCDORGHIJ".toCharArray(), // // for icon en =)
//                "AHCGERDHIJ".toCharArray(),
//                "AICDEFDHIJ".toCharArray(),
//                "ANCDEFGHIJ".toCharArray(),
//                "AGCSOMEHIJ".toCharArray(),
//                "ABCDEFGHIJ".toCharArray()
//    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        SoundManager soundManager = new SoundManager();
        soundManager.setUpSounds(this);

        ghost = findViewById(R.id.ghost);
        textViewWordsFound = findViewById(R.id.wordsFound);
        chronometer = findViewById(R.id.secondomer);
        wordsGrid = findViewById(R.id.wordsGrid);
        configuration = new Configuration(this);

        BannerAdView mBanner = (BannerAdView) findViewById(R.id.adView);
        mBanner.setAdUnitId("R-M-5962296-1");
        mBanner.setAdSize(getAdSize(mBanner));
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);

        changeMode(configuration.getContrast());
        gridChange(configuration.getGrid());
        LocaleChange.setLocale(this, configuration.getLocale());

        temp = getResources().getStringArray(R.array.words);
        setUpWordSearch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            createDialog();
            SoundManager.playSettings();
        } else if (item.getItemId() == R.id.hint) {
            wordsGrid.hint();
            SoundManager.playBell();
        } else if (item.getItemId() == R.id.refresh) {
            SoundManager.playReset();
            setUpWordSearch();
            restart();
        }

        return true;
    }

    private BannerAdSize getAdSize(BannerAdView mBanner) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int adWidthPixels = mBanner.getWidth();

        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = displayMetrics.widthPixels;
        }

        int adWidth = Math.round(adWidthPixels / displayMetrics.density);
        return BannerAdSize.stickySize(this, adWidth);

    }

    private void createDialog() {
        SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
        settingsDialogFragment.setListener(this);
        settingsDialogFragment.setSettingsDisplayListener(this);
        settingsDialogFragment.show(this.getSupportFragmentManager(), "DialogFragment");
    }

    private void createDialogWin() {
        WinDialogFragment winDialogFragment = new WinDialogFragment();
        winDialogFragment.setTime((String) chronometer.getText());
        winDialogFragment.setHints(wordsGrid.getHintsCount() + "");
        winDialogFragment.setListener(this);
        winDialogFragment.show(this.getSupportFragmentManager(), "WinFragment");
    }

    private void generateWord() {
        words = null;
        words = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String s = temp[(int) (Math.random() * temp.length)];
            if (!words.contains(s) && s.length() <= row) {
                words.add(s);
            }
        }
        wordSearchGenerator = new WordSearchGenerator(row, col, words, this);
        wordSearchGenerator.generateBoard(configuration.getLocale());
    }

    @SuppressLint("SetTextI18n")
    private void generateTextView(List<String> words) {
        linearForText = findViewById(R.id.linearForText);
        linearForText.removeAllViews();
        map = null;
        map = new HashMap<>();

        for (String s : words) {
            s = s.toLowerCase();
            TextView textView = new TextView(this);
            textView.setTextSize(25);
            textView.setTextColor(Color.WHITE);
            map.put(s, textView);
            textView.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
            textView.setGravity(Gravity.CENTER);
            linearForText.addView(textView);
        }

    }

    private void setSearchWord() {
        //        ArrayList a = new ArrayList();
//        a.add(new Word("СЛОВО", 2, 3, 6, 7));  // for icon too :-)
        wordsGrid.setLetters(wordSearchGenerator.getBoard());
        wordsGrid.setWords(wordSearchGenerator.getWords());
        wordsGrid.setOnWordSearchedListener((word, wordSearchCount) -> {
            SoundManager.randomAccessOrAccepted();

            if (word.equalsIgnoreCase("призрак") || word.equalsIgnoreCase("ghost")) {
                ghost.setVisibility(View.VISIBLE);
            }
            if (wordSearchCount == wordSearchGenerator.getWords().size()) {
                chronometer.stop();
                createDialogWin();
                SoundManager.playGood();
                wordsGrid.refresh();
            }
            textViewWordsFound.setText((wordSearchGenerator.getWords().size() - wordSearchCount) + "");
            TextView v = map.get(word.toLowerCase());
            v.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            v.setTextColor(Color.GRAY);

        });
    }

    private void setUpWordSearch() {
        generateWord();
    }

    @Override
    public void generated(List<String> words) {
        generateTextView(words);
        setSearchWord();
        textViewWordsFound.setText(wordSearchGenerator.getWords().size() + "");
        chronometer.start();
    }

    @Override
    public void restart() {
        wordsGrid.refresh();
        setUpWordSearch();
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public void change() {
        restart();
    }

    @Override
    public void gridChange(boolean isGrid) {
        wordsGrid.setGrid(isGrid);
    }

    // for start app
    private void changeMode(boolean isContrast) {
        if (isContrast) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}