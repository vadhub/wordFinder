package com.abg.wordfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.abg.wordfinder.model.Word;
import com.abg.wordfinder.view.WordSearchView;

public class MainActivity extends AppCompatActivity {

    private WordSearchView wordsGrid;

    private char[][] letters  = {
            "ASCDEFGHIJ".toCharArray(),
            "AECDEFGHIJ".toCharArray(),
            "AACDEFGHIJ".toCharArray(),
            "ARCWEFGHIJ".toCharArray(),
            "ACCDOFGHIJ".toCharArray(),
            "AHCGERGHIJ".toCharArray(),
            "AICDEFDHIJ".toCharArray(),
            "ANCDEFGHIJ".toCharArray(),
            "AGCSOMEHIJ".toCharArray(),
            "ABCDEFGHIJ".toCharArray()
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        TextView tvStrikethrough = findViewById(R.id.tv_strikethrough);
        tvStrikethrough.setText("Word\nSome\nSearching\nFog");
        tvStrikethrough.setPaintFlags(tvStrikethrough.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        wordsGrid = findViewById(R.id.wordsGrid);

        wordsGrid.setLetters(letters);

        wordsGrid.setWords(
                new Word("WORD", false, 3, 3, 6, 6),
                new Word("SOME", false, 8, 3, 8, 6),
                new Word("SEARCHING", false, 0, 1, 8, 1),
                new Word("FOG", false, 3, 5, 5, 3));

        wordsGrid.setOnWordSearchedListener(new WordSearchView.OnWordSearchedListener() {
            @Override
            public void wordFound(String word) {
                Toast.makeText(MainActivity.this, word + " found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}