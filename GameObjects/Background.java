package com.capstone.challengeweek.GameObjects;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;

import com.capstone.challengeweek.R;

public class Background {
    public Bitmap bigStar, smallStarOne, smallStarTwo, smallStarThree, smallStarFour, smallStarFive;
    private Bitmap alt_two;
    int x, y, width, height;
    int bigStarX, bigStarY, smallStarX, starVelocity;
    int smallStarOneY, smallStarTwoY, smallStarThreeY, smallStarFourY, smallStarFiveY;
    OurView view;
    Bitmap alt_one;
    Display display;
    Point size;
    Rect rect;
    public Background(OurView view, Resources resources){
        this.view = view;
        alt_one = BitmapFactory.decodeResource(resources, R.drawable.part_three_background_two);
        alt_two = BitmapFactory.decodeResource(resources, R.drawable.part_two_background_draft_two);
        if (view.button.equals(OurView.GAME_OPTION_3)){
            alt_one = BitmapFactory.decodeResource(resources, R.drawable.part_one_background_two);
        }
        //stars
        bigStar = BitmapFactory.decodeResource(resources, R.drawable.star_big);
        smallStarOne = BitmapFactory.decodeResource(resources, R.drawable.star_small);
        smallStarTwo = BitmapFactory.decodeResource(resources, R.drawable.star_small_two);
        smallStarThree = BitmapFactory.decodeResource(resources, R.drawable.star_small);
        smallStarFour = BitmapFactory.decodeResource(resources, R.drawable.star_small);
        smallStarFive = BitmapFactory.decodeResource(resources, R.drawable.star_small);

        starVelocity = ((int) (Math.random() * 5) + 1);

        display = ((Activity)view.getContext()).getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        rect = new Rect(0,0, width, height);
        alt_one = Bitmap.createBitmap(alt_one, x, y, alt_one.getWidth(), alt_one.getHeight());
        alt_two = Bitmap.createBitmap(alt_two, x, y, alt_two.getWidth(), alt_two.getHeight());
        //stars
        bigStarY = (int) (Math.random() * view.screenY) + 100;
        smallStarOneY = (int) (Math.random() * view.screenY) + 100;
        smallStarTwoY = (int) (Math.random() * view.screenY) + 100;
        smallStarThreeY = (int) (Math.random() * view.screenY) + 100;
        smallStarFourY = (int) (Math.random() * view.screenY) + 100;
        smallStarFiveY = (int) (Math.random() * view.screenY) + 100;

    }


    public Bitmap getAlt_one() {
        return alt_one;
    }

    public Bitmap getAlt_two() {
        return alt_two;
    }

    public Bitmap getBigStar() {
        return bigStar;
    }

    public int getBigStarX() {

        if (bigStarX >= view.screenX) {
            if (view.button.equals(OurView.GAME_OPTION_1)){
                bigStarX = 0;
                setBigStarY((int) (Math.random() * view.screenY) + 100);
            }
            else if (view.button.equals(OurView.GAME_OPTION_2)){
                bigStarX = 0;
                setBigStarY((int) (Math.random() * view.screenY) + 100);
            }
            else if (view.button.equals(OurView.GAME_OPTION_3)){
                bigStarX = 0;
                setBigStarY((int) (Math.random() * view.screenY) + 100);
            }
        }
        else {
            bigStarX+=starVelocity;
        }
        return bigStarX;
    }

    public int getBigStarY() {
        //let the star wander around the Y axis a little bit to make it more real
        bigStarY = (bigStarY - 5) + (int)(Math.random() * ((bigStarY + 5) -  (bigStarY - 5)) + 1);
        return bigStarY;
    }

    public int getSmallStarX() {
        if (smallStarX >= view.screenX) {
            //display stars at certain times
            if (view.button.equals(OurView.GAME_OPTION_1)){
                if (view.getEnemies().size() % 4 == 0){
                    smallStarX = 0;
                    //change the horizontal axis
                    smallStarOneY = (int) (Math.random() * view.screenY) + 100;
                    smallStarTwoY = (int) (Math.random() * view.screenY) + 100;
                    smallStarThreeY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFourY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFiveY = (int) (Math.random() * view.screenY) + 100;
                }
            }
            else if (view.button.equals(OurView.GAME_OPTION_2)){
                if (view.getEnemies().size() % 4 == 0){
                    smallStarX = 0;
                    //change the horizontal axis
                    smallStarOneY = (int) (Math.random() * view.screenY) + 100;
                    smallStarTwoY = (int) (Math.random() * view.screenY) + 100;
                    smallStarThreeY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFourY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFiveY = (int) (Math.random() * view.screenY) + 100;
                }
            }
            else if (view.button.equals(OurView.GAME_OPTION_3)){
                if (view.getAllies().size() % 4 == 0){
                    smallStarX = 0;
                    //change the horizontal axis
                    smallStarOneY = (int) (Math.random() * view.screenY) + 100;
                    smallStarTwoY = (int) (Math.random() * view.screenY) + 100;
                    smallStarThreeY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFourY = (int) (Math.random() * view.screenY) + 100;
                    smallStarFiveY = (int) (Math.random() * view.screenY) + 100;
                }
            }

        }
        else {
            smallStarX+=starVelocity;
        }
        return smallStarX;
    }

    public int getSmallStarOneY() {
        smallStarOneY = (smallStarOneY - 5) + (int)(Math.random() * ((smallStarOneY + 5) -  (smallStarOneY - 5)) + 1);

        return smallStarOneY;
    }
    public int getSmallStarTwoY() {
        smallStarTwoY = (smallStarTwoY - 5) + (int)(Math.random() * ((smallStarTwoY + 5) -  (smallStarTwoY - 5)) + 1);

        return smallStarTwoY;
    }
    public int getSmallStarThreeY() {
        smallStarThreeY = (smallStarThreeY - 5) + (int)(Math.random() * ((smallStarThreeY + 5) -  (smallStarThreeY - 5)) + 1);

        return smallStarThreeY;
    }
    public int getSmallStarFourY() {
        smallStarFourY = (smallStarFourY - 5) + (int)(Math.random() * ((smallStarFourY + 5) -  (smallStarFourY - 5)) + 1);

        return smallStarFourY;
    }
    public int getSmallStarFiveY() {
        smallStarFiveY = (smallStarFiveY - 5) + (int)(Math.random() * ((smallStarFiveY + 5) -  (smallStarFiveY - 5)) + 1);

        return smallStarFiveY;
    }


    public void setBigStarY(int bigStarY) {
        this.bigStarY = bigStarY;
    }



    public Bitmap getSmallStarOne() {
        return smallStarOne;
    }

    public Bitmap getSmallStarTwo() {
        return smallStarTwo;
    }

    public Bitmap getSmallStarThree() {
        return smallStarThree;
    }

    public Bitmap getSmallStarFour() {
        return smallStarFour;
    }

    public Bitmap getSmallStarFive() {
        return smallStarFive;
    }
}
