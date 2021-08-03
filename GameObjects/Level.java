package com.capstone.challengeweek.GameObjects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

//This class is responsible for displaying game levels
public class Level extends AsyncTask<String, String, String> {
    @SuppressLint("StaticFieldLeak")
    private final OurView view;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private  AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public Level(OurView view, Context context){
        this.context = context;
        this.view = view;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    public void display() {
        builder = new AlertDialog.Builder(context);
        if (!view.getGameComplete()) {
            if (view.button.equals(OurView.GAME_OPTION_1)) {
                builder.setTitle("Level " + view.getCurrentLevel() +"/5");
                builder.setMessage("Objective:\nScore " + (view.getObjective()) + " pts");
            } else if (view.button.equals(OurView.GAME_OPTION_2)) {
                builder.setTitle("Progress");
                builder.setMessage("Round " + view.getCurrentLevel());
            }
            else if (view.button.equals(OurView.GAME_OPTION_3)) {
                builder.setTitle("Level " + view.getCurrentLevel()+"/5");
                builder.setMessage("Mission: Kill Enemy before " + (view.getObjective()) + " pts");
            }
            execute();
        }
    }
    public void dismiss(){
        alertDialog.dismiss();
    }

}
