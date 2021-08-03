package com.capstone.challengeweek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.capstone.challengeweek.GameObjects.CustomTask;
import com.capstone.challengeweek.GameObjects.OurView;

/*This class is used to call OurView.class*/

public class Game extends Activity {

    OurView view;
    CustomTask customTask;
    private int x, y;
    long levelTimerEnd;
    private AlertDialog.Builder testBuilder;
    private MediaPlayer mediaPlayer;
    private Display display;
    private Point size;


    //This method is called when the game is launched
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve info about the screen resolution
        display = getWindowManager().getDefaultDisplay();
        customTask = new CustomTask(this, view);
        size = new Point();
        display.getSize(size);
        //retrieve button
        Intent i = getIntent();
        String button = i.getStringExtra("button");
        x = size.x;
        y = size.y;
        if (button != null && button.equals("default_game")) {
            Log.d("button", "default");
            view = new OurView(this, size.x, size.y, button, this);
        } else if (button != null && button.equals("food_fest")) {
            view = new OurView(this, size.x, size.y, button, this);
        } else if (button != null && button.equals("checkers")) {
            Log.d("button", "checkers");
            view = new OurView(this, size.x, size.y, button, this);
        }
        setContentView(view);
        setTitle("Game");


    }

    //Method called when the game is paused temporarily
    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    //Method called after the game have been paused temporarily
    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    public void transition() {
        //reload the current activity
        Intent restart = new Intent(this, Options.class);
        startActivity(restart);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public Display getDisplay() {
        return display;
    }

    public Point getSize() {
        return size;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}