package com.capstone.challengeweek;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

/*This class allows users to choose from
 different variations of the game */
public class Options extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    VideoView videoView;
    private Uri uri;
    private TextView fadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);
        //Music
        mediaPlayer = MediaPlayer.create(this, R.raw.intro_music);
        mediaPlayer.setVolume(80, 80);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    public void gameOption(View view){
        int id = view.getId();
        Intent intent = new Intent(this, Game.class);
        if (findViewById(id) ==findViewById(R.id.default_game)) {
            Log.d("button", "default");
            intent.putExtra("button",  "default_game");

        }
        if (findViewById(id) ==findViewById(R.id.food_fest)) {
            Log.d("button", "food_fest");
            intent.putExtra("button",  "food_fest");
        }
        if (findViewById(id) ==findViewById(R.id.checkers)) {
            Log.d("button", "checkers");
            intent.putExtra("button",  "checkers");
        }
        startActivity(intent);

    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }





}