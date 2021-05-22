package com.avi.in.miwok;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ColorsActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;

    private AudioManager audioManager;
    private AudioAttributes playbackAttributes;
    private AudioFocusRequest focusRequest;
    private Handler handler = new Handler();


    boolean playbackNowAuthorized = false;


    private Runnable delayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            getMediaController().getTransportControls().stop();
        }
    };

    //Implementing Audio Focus Change Listener

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        mediaPlayer.pause();

                        // Wait 30 seconds before stopping playback
                        handler.postDelayed(delayedStopRunnable,
                                TimeUnit.SECONDS.toMillis(30));
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume, keep playing
                        mediaPlayer.setVolume(50,50);
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mediaPlayer.start();
                    }
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.O)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);


        //initialize audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Set audio manager attributes

        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(afChangeListener, handler)
                .build();
        final Object focusLock = new Object();


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

        colorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word color = colors.get(position);
                releaseMediaPlayer();

                //Request Audio Focus For Playback
                int res = audioManager.requestAudioFocus(focusRequest);
                synchronized(focusLock) {
                    if (res == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                        playbackNowAuthorized = false;
                    } else if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        playbackNowAuthorized = true;
                        mediaPlayer = MediaPlayer.create(ColorsActivity.this,color.getAudioID());
                        mediaPlayer.start();
                    } else if (res == AudioManager.AUDIOFOCUS_REQUEST_DELAYED) {
                        playbackNowAuthorized = false;
                    }
                }

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer();
                    }
                });
                colorsListView.setHapticFeedbackEnabled(true);
                colorsListView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });

    }

    protected void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        audioManager.abandonAudioFocus(afChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}