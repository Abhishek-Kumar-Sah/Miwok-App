package com.avi.in.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);


        ArrayList<Word> colors = new ArrayList<>();

        colors.add(new Word("red", "wetetti",R.drawable.color_red,R.raw.color_red));
        colors.add(new Word("green", "chokokki",R.drawable.color_green,R.raw.color_green));
        colors.add(new Word("brown", "takaakki",R.drawable.color_brown,R.raw.color_brown));
        colors.add(new Word("gray", "topoppi",R.drawable.color_gray,R.raw.color_gray));
        colors.add(new Word("black", "kululli",R.drawable.color_black,R.raw.color_black));
        colors.add(new Word("white", "kelelli",R.drawable.color_white,R.raw.color_white));
        colors.add(new Word("dusty yellow", "ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        colors.add(new Word("mustard yellow", "chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));


        WordAdapter itemAdapter = new WordAdapter(this,colors,R.color.category_colors);
        ListView colorsListView = findViewById(R.id.wordList);

        colorsListView.setAdapter(itemAdapter);

    }
}