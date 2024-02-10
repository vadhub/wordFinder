package com.abg.wordfinder;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.abg.wordfinder.model.Word;
import com.abg.wordfinder.view.WordSearchView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String[] words = {"василий", "андроид", "java", "анонимус", "cell", "fog", "long"};
        Map<String, TextView> map = new HashMap<>();

        WordSearchGenerator wordSearchGenerator = new WordSearchGenerator(10, 10, words);

        wordSearchGenerator.generateBoard();
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
}