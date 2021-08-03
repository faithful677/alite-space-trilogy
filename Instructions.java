package com.capstone.challengeweek;

/*This class allows the user to adjust certain features of the game:
* sound, background, etc*/
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Instructions extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Instructions");
        setContentView(R.layout.instructions);

    }

    public void backToGame (View v){

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }}