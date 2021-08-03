package com.capstone.challengeweek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

//This class represents a button on the screen which explains what the game is about
public class About extends AppCompatActivity {

    VideoView videoView;
    private Uri uri;

    //Method called automatically when an object of the class is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

    }

    //return to main

    public void backToGame (View v){

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
