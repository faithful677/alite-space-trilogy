package com.capstone.challengeweek.GameObjects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.capstone.challengeweek.R;

/*This class is used to display the dialog box after the end
* of the Default Game*/
public class CustomTask extends AsyncTask<String, String, String> implements Runnable {
    private static PlayerDB PLAYER_DB = null;
    private  AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    @SuppressLint("StaticFieldLeak")
    Context CONTEXT;
    @SuppressLint("StaticFieldLeak")
    final
    OurView view;
    TextView textView;
    LayoutInflater inflater;

    //Menu pop up variables
    boolean resume, scoreBoard, instructions, backToMainMenu;

    public CustomTask(Context CONTEXT, OurView view){
        this.CONTEXT = CONTEXT;
        this.view = view;
    }


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        System.out.println("AsyncTask");
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();


    }
    @Override
    public void run() {
        synchronized (view) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (view.button.equals(OurView.GAME_OPTION_1)) defaultOption();
            else if (view.button.equals(OurView.GAME_OPTION_2)) foodFest();
            else if (view.button.equals(OurView.GAME_OPTION_3)) inverted();
            execute();

        }
    }
    //If option 1 is selected
    public void defaultOption(){
        builder = new AlertDialog.Builder(CONTEXT);
        inflater = view.getGame().getLayoutInflater();
        if (view.getPopUpMenu()){
            menu();
        }
        else if (view.getGameComplete()){
            System.out.println("Game complete");
            //game complete
            gameComplete();
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();
                }
            });
        }
        else {
            if (view.isLevelComplete() && !view.getGameComplete()) {
                builder.setTitle("Level " + view.getCurrentLevel() + " Complete");
                builder.setMessage("Click Yes to continue...");
            } else {
                builder.setTitle("Play Again");
                builder.setMessage("Do you want to restart?");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();

                }
            });
            System.out.println("Dialog operation done!");
            System.out.println();
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
        }
    }

    public void foodFest(){
        builder = new AlertDialog.Builder(CONTEXT);
        if (view.getPopUpMenu()){
            menu();
        }
        else if (view.getGameComplete()){
            //game complete
            gameComplete();
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();

                }
            });
        }
        else {
            String result = "";
            if (view.getWinner().equals("computer")) {
                result = "AI wins! ";
                builder.setMessage("Do you want to restart?");
            }
            else if (view.getWinner().equals("user"))
                result = "You win! ";
            else {
                result = "A Draw ";
            }

            if (view.isLevelComplete() && !view.getGameComplete()) {
                builder.setTitle(result);
                builder.setMessage("Click Yes to continue...");
            } else {
                builder.setTitle(result);
                builder.setMessage("Click Yes to restart round");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();

                }
            });
            System.out.println("Dialog operation done!");
            System.out.println();
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
        }
        System.out.println("restart: "+view.getRestart());
    }

    public void inverted(){
        builder = new AlertDialog.Builder(CONTEXT);
        if (view.getPopUpMenu()){
            menu();
        }
        else if (view.getGameComplete()){
            System.out.println("Game complete");
            //game complete
            gameComplete();
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();

                }
            });
        }
        else {
            if (view.isLevelComplete() && !view.getGameComplete()) {
                builder.setTitle("Level " + view.getCurrentLevel() + " Complete");
                builder.setMessage("Click Yes to continue...");
            } else {
                builder.setTitle("Mission Failed!");
                builder.setMessage("Do you want to restart?");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("true");
                    alertDialog.dismiss();

                }
            });
            System.out.println("Dialog operation done!");
            System.out.println();
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    view.setRestart("false");
                    alertDialog.dismiss();

                }
            });
        }
    }
    private void gameComplete(){
        builder.setTitle("Congratulations!");
        builder.setMessage("Game Complete");
    }
    public void menu(){
        builder = new AlertDialog.Builder(CONTEXT);
        builder.setTitle("Menu");
        builder.setItems(R.array.menu, (dialogInterface, i) -> {
            if (i == 0){
                resume = true;
                //resume game
                view.setPausePressed(false);
                alertDialog.dismiss();

            }
        });
    }

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    public boolean isBackToMainMenu() {
        return backToMainMenu;
    }

    public void setBackToMainMenu(boolean backToMainMenu) {
        this.backToMainMenu = backToMainMenu;
    }
    void dismiss(){
        alertDialog.dismiss();
        resume = backToMainMenu = false;
    }
}
