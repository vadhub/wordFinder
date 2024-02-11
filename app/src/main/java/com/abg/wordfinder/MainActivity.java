package com.abg.wordfinder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements WordSearchGenerator.Listener, WinDialogFragment.Listener {

    private WordSearchGenerator wordSearchGenerator;
    private WordSearchView wordsGrid;
    private Map<String, TextView> map;
    private Configuration configuration;
    private LinearLayout linearForText;
    private List<String> words;
    private String[] temp;
    private final int row = 10;
    private final int col = 10;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerAdView mBanner = (BannerAdView) findViewById(R.id.adView);
        mBanner.setAdUnitId("R-M-5962296-1");
        mBanner.setAdSize(getAdSize(mBanner));

        configuration = new Configuration(this);
        LocaleChange.setLocale(this, configuration.getLocale());

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        temp = getResources().getStringArray(R.array.words);
        wordsGrid = findViewById(R.id.wordsGrid);

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
        } else if (item.getItemId() == R.id.hint) {
            wordsGrid.hint();
        } else if (item.getItemId() == R.id.refresh) {
            setUpWordSearch();
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
        settingsDialogFragment.show(this.getSupportFragmentManager(), "DialogFragment");
    }

    private void createDialogWin() {
        WinDialogFragment winDialogFragment = new WinDialogFragment();
        winDialogFragment.setListener(this);
        winDialogFragment.show(this.getSupportFragmentManager(), "WinFragment");
    }

    private void generateWord() {
        words = null;
        words = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String s = temp[(int) (Math.random() * temp.length)];
            if (!words.contains(s) && s.length() < row) {
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
        wordsGrid.setLetters(wordSearchGenerator.getBoard());
        wordsGrid.setWords(wordSearchGenerator.getWords());
        wordsGrid.setOnWordSearchedListener((word, wordSearchCount) -> {
            Log.d("ddd", wordSearchCount +" " + wordSearchGenerator.getWords().size());
            if (wordSearchCount == wordSearchGenerator.getWords().size()) {
                createDialogWin();
                wordsGrid.refresh();
            }
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
    }

    @Override
    public void restart() {
        wordsGrid.refresh();
        setUpWordSearch();
    }
}