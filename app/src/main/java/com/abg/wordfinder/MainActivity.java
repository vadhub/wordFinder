package com.abg.wordfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.abg.wordfinder.model.Word;
import com.abg.wordfinder.view.WordSearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String[] words = {"hello", "world", "java", "android", "cell", "fog", "long"};

        WordSearchGenerator wordSearchGenerator = new WordSearchGenerator(10, 10, words);

        wordSearchGenerator.generateBoard();
        wordSearchGenerator.printBoard();

        for (Word c: wordSearchGenerator.getWords()) {
            Log.d("cc", c.toString());
        }

        TextView tvStrikethrough = findViewById(R.id.tv_strikethrough);
        tvStrikethrough.setText("Word\nSome\nSearching\nFog");
        tvStrikethrough.setPaintFlags(tvStrikethrough.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        WordSearchView wordsGrid = findViewById(R.id.wordsGrid);
        wordsGrid.setLetters(wordSearchGenerator.getBoard());
        wordsGrid.setWords(wordSearchGenerator.getWords());
        wordsGrid.setOnWordSearchedListener(word -> Toast.makeText(MainActivity.this, word + " found", Toast.LENGTH_SHORT).show());
    }
}