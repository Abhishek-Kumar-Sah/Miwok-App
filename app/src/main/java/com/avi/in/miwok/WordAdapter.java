package com.avi.in.miwok;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private int bgColor;

    public WordAdapter(Activity context , ArrayList<Word> words , int bgColorID){
        super(context,0,words);

        bgColor = bgColorID;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_layout,parent,false);
        }

        Word currentWord = getItem(position);

        TextView miwokTextView = convertView.findViewById(R.id.miwok_textView);
        miwokTextView.setText(currentWord.getMiwokTranslation());
        TextView defaultTextView = convertView.findViewById(R.id.default_textView);
        defaultTextView.setText(currentWord.getDefaultTranslatiion());

        // Set the theme color for the list item
        View textContainer = convertView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), bgColor);
       // Log.v("Word Adapter","color int value is : "+color);
        textContainer.setBackgroundColor(color);

        ImageView imageView = convertView.findViewById(R.id.image_view);
        if (currentWord.getImgageID() == -1) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(currentWord.getImgageID());
            imageView.setVisibility(View.VISIBLE);
        }
        convertView.setHapticFeedbackEnabled(true);
        ImageView playIcon = convertView.findViewById(R.id.playIcon);
        playIcon.setBackgroundColor(color);

        return convertView;
    }
}
