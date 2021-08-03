package com.capstone.challengeweek.GameObjects;

/*This class represents the user-controlled sprite*/
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.capstone.challengeweek.R;

import java.util.ArrayList;

public class Sprite {
    private Bitmap greenSpark, blueSpark, beigeSpark;
    public boolean isGreenUFO, isBlueUFO, isBeigeUFO, isYellowUFO, isPinkUFO;
    public int enemyDeath;
    //user-controlled sprite
    private final Bitmap greenUFO;
    private Bitmap blueUFO;
    private Bitmap beigeUFO;
    private Bitmap pinkUFO;
    private Bitmap yellowUFO;
    /*location of the user-controlled sprite, its velocity & its height and width*/
    public int x, y;
    public int velX, velY, damageX, damageY;
    int height, width;
    //OurView class represents the game engine
    OurView view;
    private String direction;
    private boolean bodyContact;
    private static int SPEED;
    //user-controlled sprite
    Bitmap bitmapUp, bitmapDown, bitmapLeft, bitmapRight;
    Bitmap laserUp, laserDown, laserLeft, laserRight;
    public float laserUpX, laserUpY, laserDownX, laserDownY, laserRightX, laserRightY
    , laserLeftX, laserLeftY;
    private Paint silver;
    private Paint red;
    private int id;
    Bitmap alphaGreen, alphaBlue, alphaBeige;
    int glowColor, glowRadius;

    public Sprite(OurView ourView, Resources resources, int id) {
        view = ourView;
        this.id = id;
        //get user-controlled character from drawable folder
        bitmapUp = BitmapFactory.decodeResource(resources, R.drawable.player_space_ship_up);
        bitmapDown = BitmapFactory.decodeResource(resources, R.drawable.player_space_ship_down);
        bitmapLeft = BitmapFactory.decodeResource(resources, R.drawable.player_space_ship_left);
        bitmapRight = BitmapFactory.decodeResource(resources, R.drawable.player_space_ship_right);
        greenUFO = BitmapFactory.decodeResource(resources, R.drawable.green_ufo);
        blueUFO = BitmapFactory.decodeResource(resources, R.drawable.blue_ufo);
        beigeUFO = BitmapFactory.decodeResource(resources, R.drawable.beige_ufo);
        pinkUFO = BitmapFactory.decodeResource(resources, R.drawable.pink_ufo);
        greenSpark = BitmapFactory.decodeResource(resources, R.drawable.green_spark);
        blueSpark = BitmapFactory.decodeResource(resources, R.drawable.blue_spark);
        beigeSpark = BitmapFactory.decodeResource(resources, R.drawable.beige_spark);
        greenSpark = BitmapFactory.decodeResource(resources, R.drawable.green_spark);
        isGreenUFO = isBlueUFO = isBeigeUFO = false;
        //set the location of the sprite to a random location on the screen
        x = (int) (Math.random() * ourView.screenX - 100) + 100;
        y = (int) (Math.random() * ourView.screenY - 100) + 100;
        //initialise the speed of the sprite
        velX = SPEED;
        velY = SPEED;
        this.direction = "halt";
        bodyContact = false;
        damageX = damageY = 0;
        if (view.button.equals(OurView.GAME_OPTION_1)
                || view.button.equals(OurView.GAME_OPTION_2)) {
            SPEED = 10;
        }
        else {
            SPEED = 7;
            silver = new Paint();
            red = new Paint();
            silver.setColor(Color.argb(200, 220, 220, 220));
            //change the tone of green colour as player loses life
            red.setColor(Color.argb(250, 170, 34, 34));
            enemyDeath = greenUFO.getWidth();
        }
        //create a glow around user-controlled bitmap
        glowColor = Color.rgb(200, 200, 200);
        alphaBeige = beigeUFO.extractAlpha();
        alphaBlue = blueUFO.extractAlpha();
        alphaGreen = greenUFO.extractAlpha();
    }

    Bitmap getUp(){
        height = bitmapUp.getHeight();
        width = bitmapUp.getWidth();
        return bitmapUp;
    }
    Bitmap getDown(){
        height = bitmapDown.getHeight();
        width = bitmapDown.getWidth();
        return bitmapDown;
    }
    Bitmap getLeft(){
        height = bitmapLeft.getHeight();
        width = bitmapLeft.getWidth();
        return bitmapLeft;
    }
    Bitmap getRight(){
        height = bitmapRight.getHeight();
        width = bitmapRight.getWidth();
        return bitmapRight;
    }

    public Bitmap getGreenUFO() {
        isGreenUFO = true;
        isBlueUFO = false;
        isBeigeUFO = false;
        glowRadius = greenUFO.getWidth();
        height = greenUFO.getHeight();
        width = greenUFO.getWidth();
        return greenUFO;
    }
    Bitmap getBlueUFO() {
        isBlueUFO = true;
        isGreenUFO = false;
        isBeigeUFO = false;
        glowRadius = blueUFO.getWidth();
        height = blueUFO.getHeight();
        width =  blueUFO.getWidth();
        blueUFO = Bitmap.createScaledBitmap(blueUFO, width, height,false);
        return blueUFO;
    }

    Bitmap getBeigeUFO() {
        isBeigeUFO = true;
        isBlueUFO = false;
        isGreenUFO = false;
        glowRadius = beigeUFO.getWidth();
        height = beigeUFO.getHeight();
        width =  beigeUFO.getWidth();
        beigeUFO = Bitmap.createScaledBitmap(beigeUFO, width, height,false);
        return beigeUFO;
    }

    Bitmap getYellowUFO() {
        isYellowUFO = true;
        isBeigeUFO = false;
        isBeigeUFO = false;
        isGreenUFO = false;
        isPinkUFO = false;
        height = yellowUFO.getHeight();
        width =  yellowUFO.getWidth();
        yellowUFO = Bitmap.createScaledBitmap(yellowUFO, width, height,false);
        return yellowUFO;
    }
    Bitmap getPinkUFO() {
        isPinkUFO = true;
        isBeigeUFO = false;
        isBeigeUFO = false;
        isGreenUFO = false;
        isYellowUFO = false;
        height = pinkUFO.getHeight();
        width =  pinkUFO.getWidth();
        pinkUFO = Bitmap.createScaledBitmap(pinkUFO, width, height,false);
        return pinkUFO;
    }

    public Bitmap getBlueSpark() {
        height = blueSpark.getHeight();
        width =  blueSpark.getWidth();
        blueSpark = Bitmap.createScaledBitmap(blueSpark, width, height,false);
        return blueSpark;
    }

    public Bitmap getBeigeSpark() {
        height = beigeSpark.getHeight();
        width =  beigeSpark.getWidth();
        beigeSpark = Bitmap.createScaledBitmap(beigeSpark, width, height,false);
        return beigeSpark;
    }

    public int getGlowRadius() {
        return glowRadius;
    }

    //method which encapsulates the sprite in a rectangle
    public Rect src() {
        return new Rect(0, 0, width, height);
    }

    //method which scales the sprite
    public Rect scaleFactor() {
        return new Rect(x, y, x + width, y + height);
    }

    //Method to update the location of the sprite depending on the direction
    public void update(String direction) {
        if (view.button.equals(OurView.GAME_OPTION_1) || view.button.equals(OurView.GAME_OPTION_2)) {
            SPEED = 9;
        }
        this.direction = direction;
        //make the sprite to pass through the walls
        if (x > view.screenX) {
            x = 0;
        } else if (y > view.screenY) {
            y = 0;
        } else if (x < 0) {
            x = view.screenX;
        } else if (y < 0) {
            y = view.screenY;
        }
        System.out.println();


        //process movement of the player
        switch (direction) {
            case "right":
                velX = SPEED;
                velY = 0;
                break;
            case "left":
                velX = -SPEED;
                velY = 0;
                break;
            case "up":
                velY = -SPEED;
                velX = 0;
                break;
            case "down":
                velY = SPEED;
                velX = 0;
                break;
            default:
                //condition is met if the user touches the game object itself
                velX = 0;
                velY = 0;
                break;
        }
        if (!bodyContact) {
            x += (velX);
            y += (velY);
        } else {
            x += (velX) + damageX;
            y += (velY) + damageY;
            setBodyContact(false);
        }
    }

    //Motion for GAME_VARIATION_3
    void updateInverted() {
        //make the character to pass through the walls
        if (view.getCursor() != null) {
            if (view.getCursor().x > view.screenX) {
                view.getCursor().x = 0;
            } else if (view.getCursor().y > view.screenY) {
                view.getCursor().y = 0;
            } else if (view.getCursor().x < 0) {
                view.getCursor().x = view.screenX;
            } else if (view.getCursor().y < 0) {
                view.getCursor().y = view.screenY;
            }
        } else {
            if (x >= view.screenX) {
                x = 1;
            } else if (y >= view.screenY) {
                y = 1;
            } else if (x <= 0) {
                x = view.screenX;
            } else if (y <= 0) {
                y = view.screenY;
            }
        }
        if (x < view.getEnemy().getX() && x < (view.getEnemy().getX() + view.getEnemy().playerWidth)) {
            if (Math.abs(x - view.getEnemy().getX() + view.getEnemy().playerWidth) < Math.abs(y - view.getEnemy().getY() + view.getEnemy().playerHeight)) {
                if (y <= view.getEnemy().getY() && y < view.getEnemy().getY() + view.getEnemy().playerHeight) {
                    velY = SPEED;
                    velX = 0;
                } else if (y >= view.getEnemy().getY() && y > view.getEnemy().getY() + view.getEnemy().playerHeight) {
                    velY = -SPEED;
                    velX = 0;
                } else {
                    velY = 0;
                    velX = 0;
                }
            }
            velX = SPEED;
            velY = 0;

        } else if (x > view.getEnemy().getX() && x > view.getEnemy().getX() + view.getEnemy().playerWidth) {
            if (Math.abs(x - view.getEnemy().getX() + view.getEnemy().playerWidth)
                    > Math.abs(y - view.getEnemy().getY() + view.getEnemy().playerHeight)) {
                if (y <= view.getEnemy().getY() && y < view.getEnemy().getY() + view.getEnemy().playerHeight) {
                    velY = SPEED;
                    velX = 0;
                } else if (y >= view.getEnemy().getY() && y > view.getEnemy().getY() + view.getEnemy().playerHeight) {
                    velY = -SPEED;
                    velX = 0;
                } else {
                    velY = 0;
                    velX = 0;
                }
            }
            velX = -SPEED;
            velY = 0;
        } else {
            velX = 0;
            if (y <= view.getEnemy().getY() && y < view.getEnemy().getY() + view.getEnemy().playerHeight) {
                if (Math.abs(x - view.getEnemy().getX() + view.getEnemy().playerHeight) < Math.abs(y - view.getEnemy().getY() + view.getEnemy().playerHeight)) {
                    if (x <= view.getEnemy().getX() && x < view.getEnemy().getX() + view.getEnemy().playerWidth) {
                        velX = 6;
                        velY = 0;
                    } else if (y >= view.getEnemy().getY() && y > view.getEnemy().getY() + view.getEnemy().playerHeight) {
                        velX = -6;
                        velY = 0;
                    } else {
                        velY = 0;
                        velX = 0;
                    }
                }
                velY = 6;
                velX = 0;
            } else if (y >= view.getEnemy().getY() && y > view.getEnemy().getY() + view.getEnemy().playerHeight) {
                if (Math.abs(x - view.getEnemy().getX() + view.getEnemy().playerWidth) < Math.abs(y - view.getEnemy().getY() + view.getEnemy().playerHeight)) {
                    if (x <= view.getEnemy().getX() && x < view.getEnemy().getX() + view.getEnemy().playerWidth) {
                        velX = 6;
                        velY = 0;
                    } else if (y >= view.getEnemy().getY() && y > view.getEnemy().getY() + view.getEnemy().playerHeight) {
                        velX = -6;
                        velY = 0;
                    } else {
                        velY = 0;
                        velX = 0;
                    }
                }
                velY = -6;
                velX = 0;
            } else {
                velY = 0;
            }
        }
        x += (velX);
        y += (velY);
    }

    void reaction(ArrayList<Enemy> enemies) {
        for (int i = 0; i<enemies.size(); i++) {
            enemies.get(i).velX = 0;
            enemies.get(i).velY = 0;
            velX = 0;
            velY = 0;
            if (enemies.get(i).getDirection().equals("right")
                    && !direction.equals("left")) {
                damageX = SPEED - 5;
            } else if (enemies.get(i).getDirection().equals("left")
                    && !direction.equals("right")) {
                damageX = -(SPEED - 5);
            } else if (enemies.get(i).getDirection().equals("up")
                    && !direction.equals("down")) {
                damageY = -(SPEED - 5);
            } else if (enemies.get(i).getDirection().equals("down")
                    && !direction.equals("up")) {
                damageY = SPEED - 5;
            } else {
                /*Head-to-head collision with Enemy; they win since they are the actor
                 *Calculation: take the difference between both velocities on the X & Y*/
                if (enemies.get(i).getDirection().equals("right")
                        && direction.equals("left")) {

                    damageX = Math.abs(velX - enemies.get(i).velX);
                } else if (enemies.get(i).getDirection().equals("left")
                        && direction.equals("right")) {
                    damageX = -Math.abs(velX - enemies.get(i).velX);
                } else if (enemies.get(i).getDirection().equals("up")
                        && direction.equals("down")) {
                    damageY = -Math.abs(velY - enemies.get(i).velY);
                } else if (enemies.get(i).getDirection().equals("down")
                        && direction.equals("up")) {
                    damageY = Math.abs(velY - enemies.get(i).velY);
                }
            }
        }
    }

    public void stop() {
        velX = 0;
        velY = 0;
    }

    public void setBodyContact(boolean bodyContact) {
        this.bodyContact = bodyContact;
    }

    public String getDirection() {
        return direction;
    }

    public void updateFoodFest() {
        if (view.button.equals(OurView.GAME_OPTION_2)) {
            SPEED = 6;
        }
        if (x <= view.getFood().x && x < view.getFood().x + view.getFood().width) {
            if (Math.abs(x - view.getFood().x + view.getFood().width) < Math.abs(y - view.getFood().y + view.getFood().height)) {
                direction = "down";
                velY = SPEED;
                velX = 0;
            } else if (y >= view.getFood().y && y > view.getFood().y + view.getFood().height) {
                direction = "up";
                velY = -SPEED;
                velX = 0;
            } else {
                direction = "halt";
                velY = 0;
                velX = 0;
            }
            velX = SPEED;
            velY = 0;
            direction = "right";
        } else if (x >= view.getFood().x && x > view.getFood().x + view.getFood().width) {
            if (Math.abs(x - view.getFood().x + view.getFood().width) > Math.abs(y - view.getFood().y + view.getFood().height)) {
                if (y <= view.getFood().y && y < view.getFood().y + view.getFood().height) {
                    direction = "down";
                    velY = SPEED;
                    velX = 0;
                } else if (y >= view.getFood().y && y > view.getFood().y + view.getFood().height) {
                    direction = "up";
                    velY = -SPEED;
                    velX = 0;
                } else {
                    velY = 0;
                    velX = 0;
                }
            }
            direction = "left";
            velX = -SPEED;
            velY = 0;
        } else {
            velX = 0;
            if (y <= view.getFood().y && y < view.getFood().y + view.getFood().height) {
                if (Math.abs(x - view.getFood().x + view.getFood().width) < Math.abs(y - view.getFood().y + view.getFood().height)) {
                    if (x <= view.getFood().x && x < view.getFood().x + view.getFood().width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getFood().y && y > view.getFood().y + view.getFood().height) {
                        direction = "left";
                        velX = -SPEED;
                        velY = 0;
                    } else {
                        velY = 0;
                        velX = 0;
                    }
                }
                direction = "down";

                velY = SPEED;
                velX = 0;
            } else if (y >= view.getFood().y && y > view.getFood().y + view.getFood().height) {
                if (Math.abs(x - view.getFood().x + view.getFood().width) < Math.abs(y - view.getFood().y + view.getFood().height)) {
                    if (x <= view.getFood().x && x < view.getFood().x + view.getFood().width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getFood().y && y > view.getFood().y + view.getFood().height) {
                        direction = "left";
                        velX = -SPEED;
                        velY = 0;
                    }
                    else {
                        velY = 0;
                        velX = 0;
                    }
                }
                direction = "up";
                velY = -SPEED;
                velX = 0;
            } else {
                velY = 0;
            }
        }
        if (!bodyContact) {
            x += (velX);
            y += (velY);
        } else {
            reaction(view.getEnemies());
            x += (velX) + damageX;
            y += (velY)+ damageY;
            setBodyContact(false);
        }
    }
    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public Paint getSilver() {
        return silver;
    }

    public Paint getRed() {
        return red;
    }

    public Bitmap getGreenSpark() {
        return greenSpark;
    }

    public int getId() {
        return id;
    }
}

