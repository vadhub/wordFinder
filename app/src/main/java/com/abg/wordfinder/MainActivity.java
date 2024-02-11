package com.abg.wordfinder;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.abg.wordfinder.model.Word;
import com.abg.wordfinder.view.WordSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String[] temp = getResources().getStringArray(R.array.words);

        List<String> words = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            String s = temp[(int) (Math.random() * temp.length)];
            if (!words.contains(s)) {
                words.add(s);
            }

        }

        Map<String, TextView> map = new HashMap<>();

        WordSearchGenerator wordSearchGenerator = new WordSearchGenerator(10, 10, words);

        wordSearchGenerator.generateBoard("RU");
        wordSearchGenerator.printBoard();

        for (Word c: wordSearchGenerator.getWords()) {
            Log.d("cc", c.toString());
        }

        LinearLayout linearForText = (LinearLayout) findViewById(R.id.linearForText);

        for (String s : words) {
            TextView textView = new TextView(this);
            textView.setTextSize(25);
            textView.setTextColor(Color.WHITE);
            textView.setText(s);
            linearForText.addView(textView);
            map.put(s, textView);
        }

        WordSearchView wordsGrid = findViewById(R.id.wordsGrid);
        wordsGrid.setLetters(wordSearchGenerator.getBoard());
        wordsGrid.setWords(wordSearchGenerator.getWords());
        wordsGrid.setOnWordSearchedListener(word -> {
            Objects.requireNonNull(map.get(word)).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {

        }
        return true;
    }
}