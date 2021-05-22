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
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NumbersActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer ;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                releaseMediaPlayer();

                //Request Audio Focus For Playback
                int res = audioManager.requestAudioFocus(focusRequest);
                synchronized(focusLock) {
                    if (res == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                        playbackNowAuthorized = false;
                    } else if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        playbackNowAuthorized = true;
                        mediaPlayer = MediaPlayer.create(NumbersActivity.this,word.getAudioID());
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


                listView.setHapticFeedbackEnabled(true);
                listView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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