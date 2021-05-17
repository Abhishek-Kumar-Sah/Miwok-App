package com.avi.in.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("One","lutti",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("Two", "otiiko",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("Three", "tolookosu",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("Four", "oyyisa",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("Five", "massokka",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("Six","temmokka",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("Seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("Eight" , "kawinta",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("Nine", "wo'e",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("Ten", "na'aacha",R.drawable.number_ten,R.raw.number_ten));


        WordAdapter itemsAdapter = new WordAdapter( this, words,R.color.category_numbers);


        ListView listView =  findViewById(R.id.wordList);

        listView.setAdapter(itemsAdapter);

    }
}