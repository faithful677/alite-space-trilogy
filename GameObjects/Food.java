package com.capstone.challengeweek.GameObjects;

/*This class represents the Food that the user eats in the game.
* The food is represented as a purple circle in the game*/
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.capstone.challengeweek.R;

import java.util.Random;

public class Food {
    //the following x & y coordinates represent the position of the food
    int x, y;
    //height and width are the dimensions of the food
    int height, width;
    //The OurView class represents the game engine where all the game processing takes place
    private OurView ourView;
    Bitmap coin_one, coin_two, coin_three, coin_four, coin_five, coin_six, coin_seven;
    int coinCounter;
    //random is used to select a new coordinate of the food after the user has eaten
    Random random;
    int relocateX, relocateY;
    public Food(OurView ourView, Resources resources) {
        this.ourView = ourView;
        //Coin bitmap
        coin_one = BitmapFactory.decodeResource(resources, R.drawable.coin_one);
        coin_two = BitmapFactory.decodeResource(resources, R.drawable.coin_two);
        coin_three = BitmapFactory.decodeResource(resources, R.drawable.coin_three);
        coin_four = BitmapFactory.decodeResource(resources, R.drawable.coin_four);
        coin_five = BitmapFactory.decodeResource(resources, R.drawable.coin_five);
        coin_six = BitmapFactory.decodeResource(resources, R.drawable.coin_six);
        coin_seven = BitmapFactory.decodeResource(resources, R.drawable.coin_seven);

        x = (int) (Math.random() * this.ourView.screenX);
        y = (int) (Math.random() * this.ourView.screenY);
        random = new Random();
    }
    //This method updates the position of the food
    public void update() {
        relocateX = random.nextInt((ourView.screenX - 150) + 1)+100 ;
        relocateY = random.nextInt((ourView.screenY - 150) + 1)+100 ;
        x = relocateX;
        y = relocateY;

    }

    public Bitmap getCoin(){
        if (coinCounter == 0){
            width = coin_one.getWidth();
            height = coin_one.getHeight();
            coinCounter++;
            return coin_one;
        }
        else if (coinCounter == 1){
            width = coin_two.getWidth();
            height = coin_two.getHeight();
            coinCounter++;
            return coin_two;
        }
        else if (coinCounter == 2){
            width = coin_three.getWidth();
            height = coin_three.getHeight();
            coinCounter++;
            return coin_three;
        }
        else if (coinCounter == 3){
            width = coin_four.getWidth();
            height = coin_four.getHeight();
            coinCounter++;
            return coin_four;
        }
        else if (coinCounter == 4){
            width = coin_five.getWidth();
            height = coin_five.getHeight();
            coinCounter++;
            return coin_five;
        }
        else if (coinCounter == 5){
            width = coin_six.getWidth();
            height = coin_six.getHeight();
            coinCounter++;
            return coin_six;
        }
        else if (coinCounter == 6){
            width = coin_seven.getWidth();
            height = coin_seven.getHeight();
            coinCounter -=6;
            return coin_seven;
        }
        return coin_seven;
    }
}
