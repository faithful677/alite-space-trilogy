package com.capstone.challengeweek.GameObjects;

/*This class represents the Enemy in the game*/
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.capstone.challengeweek.R;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {

    //This class represents the Enemy object
    public int enemyDeath;
    private Paint silver, red;
    private int id;
    //OurView represents the game engine
    OurView view;
    //These values represent the dimensions of the object
    int playerHeight, playerWidth;
    int x, y;
    //These values represent the velocity/speed of the object
    public int velX;
    public int velY;
    Sprite sprite;
    Food food;
    private boolean bodyContact;
    private String direction;
    private int damageX, damageY;
    private boolean isEnemyDetected;
    //speed of the game object
    private static int SPEED;
    private Bitmap greenUFO, greenSpark, blueUFO, beigeUFO, yellowUFO, pinkUFO, blueSpark, beigeSpark;
    private Bitmap bitmapUp, bitmapDown, bitmapLeft, bitmapRight;
    public boolean isGreenUFO, isBlueUFO, isBeigeUFO, isYellowUFO, isPinkUFO;

    private boolean declareEnemyDeath;

    public Enemy(OurView ourView, Sprite sprite, Food food, Resources resources, int id) {
        this.view = ourView;
        this.id = id;
        greenUFO = BitmapFactory.decodeResource(resources, R.drawable.green_ufo);
        blueUFO = BitmapFactory.decodeResource(resources, R.drawable.blue_ufo);
        beigeUFO = BitmapFactory.decodeResource(resources, R.drawable.beige_ufo);
        yellowUFO = BitmapFactory.decodeResource(resources, R.drawable.yellow_ufo);
        pinkUFO = BitmapFactory.decodeResource(resources, R.drawable.pink_ufo);
        greenSpark = BitmapFactory.decodeResource(resources, R.drawable.green_spark);
        blueSpark = BitmapFactory.decodeResource(resources, R.drawable.blue_spark);
        beigeSpark = BitmapFactory.decodeResource(resources, R.drawable.beige_spark);
        playerHeight = greenUFO.getHeight();
        playerWidth =  greenUFO.getWidth();
        isBeigeUFO = isBlueUFO = isGreenUFO = false;
        declareEnemyDeath = false;
        greenUFO = Bitmap.createScaledBitmap(greenUFO, playerWidth, playerHeight,false);
        x = new Random().nextInt((ourView.screenX - 100) + 1) + 100;
        y = new Random().nextInt((ourView.screenX - 100) + 1) + 100;
        this.sprite = sprite;
        this.food = food;
        direction = null;
        damageX = damageY = 0;
        bodyContact = false;
        isEnemyDetected = false;
        if (view.button.equals(OurView.GAME_OPTION_3)){
            SPEED = 9;
            bitmapUp = BitmapFactory.decodeResource(resources, R.drawable.ai_space_ship_up);
            bitmapDown = BitmapFactory.decodeResource(resources, R.drawable.ai_space_ship_down);
            bitmapLeft = BitmapFactory.decodeResource(resources, R.drawable.ai_space_ship_left);
            bitmapRight = BitmapFactory.decodeResource(resources, R.drawable.ai_space_ship_right);
        }
        else {
            SPEED = 6;
            silver = new Paint();
            red = new Paint();
            silver.setColor(Color.argb(200, 220, 220, 220));
            red.setColor(Color.argb(250, 170, 34, 34));
        }
        velX = SPEED;
        velY = SPEED;
        //set default direction
        direction = "up";
    }


    public int getId() {
        return id;
    }

    //method to return green UFO enemy
    public Bitmap getGreenUFO() {
        isGreenUFO = true;
        isBlueUFO = false;
        isBeigeUFO = false;
        playerHeight = greenUFO.getHeight();
        playerWidth = greenUFO.getWidth();
        if (!declareEnemyDeath) {
            enemyDeath = playerWidth;
            declareEnemyDeath = true;
        }
        return greenUFO;
    }
    Bitmap getBlueUFO() {
        isBlueUFO = true;
        isGreenUFO = false;
        isBeigeUFO = false;
        playerHeight = blueUFO.getHeight();
        playerWidth =  blueUFO.getWidth();
        if (!declareEnemyDeath) {
            enemyDeath = playerWidth;
            declareEnemyDeath = true;
        }
        return blueUFO;
    }
    Bitmap getBeigeUFO() {
        isBeigeUFO = true;
        isBeigeUFO = false;
        isGreenUFO = false;
        playerHeight = beigeUFO.getHeight();
        playerWidth =  beigeUFO.getWidth();
        if (!declareEnemyDeath) {
            enemyDeath = playerWidth;
            declareEnemyDeath = true;
        }
        beigeUFO = Bitmap.createScaledBitmap(beigeUFO, playerWidth, playerHeight,false);
        return beigeUFO;
    }
    Bitmap getYellowUFO() {
        isYellowUFO = true;
        isBeigeUFO = false;
        isGreenUFO = false;
        isPinkUFO = false;
        playerHeight = yellowUFO.getHeight();
        playerWidth =  yellowUFO.getWidth();
        if (!declareEnemyDeath) {
            enemyDeath = playerWidth;
            declareEnemyDeath = true;
        }
        yellowUFO = Bitmap.createScaledBitmap(yellowUFO, playerWidth, playerHeight,false);
        return yellowUFO;
    }
    Bitmap getPinkUFO() {
        isPinkUFO = true;
        isBeigeUFO = false;
        isGreenUFO = false;
        isYellowUFO = false;
        playerHeight = pinkUFO.getHeight();
        playerWidth =  pinkUFO.getWidth();
        if (!declareEnemyDeath) {
            enemyDeath = playerWidth;
            declareEnemyDeath = true;
        }
        pinkUFO = Bitmap.createScaledBitmap(pinkUFO, playerWidth, playerHeight,false);
        return pinkUFO;
    }


    /*update enemy direction in Part I
     * 'i' stands for enemy id*/
    void update(int i) {
        if (x < sprite.x && x < (sprite.x + sprite.width)) {
            if (Math.abs(x - sprite.x + sprite.width) < Math.abs(y - sprite.y + sprite.height)) {
                if (y <= sprite.y && y < sprite.y + sprite.height) {
                    velY = SPEED;
                    velX = 0;
                    direction = "up";
                } else if (y >= sprite.y && y > sprite.y + sprite.height) {
                    velY = -SPEED;
                    velX = 0;
                    direction = "down";
                } else {
                    velY = 0;
                    velX = 0;
                }
            }
            velX = SPEED;
            velY = 0;
            direction = "right";

        }
        //Make every 2nd enemy to Get close to the food horizontally to improve social presence
        else if (x < food.x && (i % 2 == 0 && view.getEnemies().size() >= 2)
                && (y + velY != food.y || y + velY != food.y + food.height)) {
            if (y <= food.y && y < food.y+food.height) {
                velY = SPEED;
                velX = 0;
                direction = "up";
            }
            else if (y >= food.y && y >= food.y+food.height ) {
                velY = -SPEED;
                velX = 0;
                direction = "down";
            }
            else {
                velY = 0;
                velX = 0;
                direction = "halt";
            }
        } else if (x > sprite.x && x > sprite.x + sprite.width) {
            if (Math.abs(x - sprite.x + sprite.width) > Math.abs(y - sprite.y + sprite.height)) {
                if (y <= sprite.y && y < sprite.y + sprite.height) {
                    velY = SPEED;
                    velX = 0;
                    direction = "up";
                } else if (y >= sprite.y && y > sprite.y + sprite.height) {
                    velY = -SPEED;
                    velX = 0;
                    direction = "down";
                } else {
                    velY = 0;
                    velX = 0;
                }
            }
            velX = -SPEED;
            velY = 0;
            direction = "left";
        } else {
            velX = 0;
            if (y <= sprite.y && y < sprite.y + sprite.height) {
                if (Math.abs(x - sprite.x + sprite.width) < Math.abs(y - sprite.y + sprite.height)) {
                    if (x <= sprite.x && x < sprite.x + sprite.width) {
                        velX = SPEED;
                        velY = 0;
                        direction = "right";
                    } else if (y >= sprite.y && y > sprite.y + sprite.height) {
                        velX = -SPEED;
                        velY = 0;
                        direction = "left";
                    } else {
                        velY = 0;
                        velX = 0;
                    }
                }
                velY = SPEED;
                velX = 0;
                direction = "up";
            } else if (y >= sprite.y && y > sprite.y + sprite.height) {
                if (Math.abs(x - sprite.x + sprite.width) < Math.abs(y - sprite.y + sprite.height)) {
                    if (x <= sprite.x && x < sprite.x + sprite.width) {
                        velX = SPEED;
                        velY = 0;
                        direction = "right";
                    } else if (y >= sprite.y && y > sprite.y + sprite.height) {
                        velX = -SPEED;
                        velY = 0;
                        direction = "left";
                    } else {
                        velY = 0;
                        velX = 0;
                    }
                }
                velY = -SPEED;
                velX = 0;
                direction = "down";
            }
            //Make every other enemy get close to the food horizontally to improve social presence
            else if (y < food.y && (i % 2 == 0 && view.getEnemies().size() >= 2)
                    && (x + velX != food.x
                    || x + velX != food.x + food.width)) {
                if (x <= food.x && x < food.x + food.width) {
                    velX = SPEED;
                    velY = 0;
                    direction = "right";
                }
                else if (x >= food.x && x > food.x+food.width) {
                    velX = -SPEED;
                    velY = 0;
                    direction = "left";
                }
                else {
                    velX = 0;
                    velY = 0;
                }
            }
        }
        x += (int) (velX);;
        y += (int) (velY);
    }

    //method which updates the direction of the enemies in Part II - Food Fest
    void updateFoodFest() {
        if (x <= food.x && x < food.x + food.width) {
            if (Math.abs(x - food.x + food.width) < Math.abs(y - food.y + food.height)) {
                direction = "down";
                velY = SPEED;
                velX = 0;
            } else if (y >= food.y && y > food.y + food.height) {
                direction = "up";
                velY = -SPEED;
                velX = 0;
            }
            else {
                direction = "halt";
                velY = 0;
                velX = 0;
            }
            velX = SPEED;
            velY = 0;
            direction = "right";
        } else if (x >= food.x && x > food.x + food.width) {
            if (Math.abs(x - food.x + food.width) > Math.abs(y - food.y + food.height)) {
                if (y <= food.y && y < food.y + food.height) {
                    direction = "down";
                    velY = SPEED;
                    velX = 0;
                } else if (y >= food.y && y > food.y + food.height) {
                    direction = "up";
                    velY = -SPEED;
                    velX = 0;
                }
                else {
                    velY = 0;
                    velX = 0;
                }
            }
            direction = "left";
            velX = -SPEED;
            velY = 0;
        } else {
            velX = 0;
            if (y <= food.y && y < food.y + food.height) {
                if (Math.abs(x - food.x + food.width) < Math.abs(y - food.y + food.height)) {
                    if (x <= food.x && x < food.x + food.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= food.y && y > food.y + food.height) {
                        direction = "left";
                        velX = -SPEED;
                        velY = 0;
                    }
                    else {
                        velY = 0;
                        velX = 0;
                    }
                }
                direction = "down";
                velY = SPEED;
                velX = 0;
            } else if (y >= food.y && y > food.y + food.height) {
                if (Math.abs(x - food.x + food.width) < Math.abs(y - food.y + food.height)) {
                    if (x <= food.x && x < food.x + food.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= food.y && y > food.y + food.height) {
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
        if (!isBodyContact()) {
            x += (int) velX;
            y += (int) velY;
        } else if (isBodyContact()) {
            reaction();
            x += velX + damageX;
            y += velY + damageY;
            setBodyContact(false);
        }
    }

    //This method updates the direction of the enemy for Part III
    public void updateInverted(ArrayList<Sprite> allies) {
        //make the enemy to pass through the walls
        if (x > view.screenX) {
            x = 0;
        } else if (y > view.screenY) {
            y = 0;
        } else if (x < 0) {
            x = view.screenX;
        } else if (y < 0) {
            y = view.screenY;
        }

        if (view.isHealthDetected()){
            locateHealth();
            view.setHealthDetected(false);
            x += velX;
            y += velY;
        }
        if (view.isLaserDetected()){
            replenishBullets();
            view.setLaserDetected(false);
            x += velX;
            y += velY;
        }
        if (isEnemyDetected){
            enemyDetection(allies);
            isEnemyDetected = false;
            x += velX;
            y += velY;
        }
        else {
            velX = 0;
            velY = 0;
            updateFoodFest();
        }
        enemyDetection(allies);
    }

    public void setBodyContact(boolean bodyContact) {
        this.bodyContact = bodyContact;
    }

    public boolean isBodyContact() {
        return bodyContact;
    }

    void reaction(){
        velX = 0;
        velY = 0;
        for (int i = 0; i<view.getAllies().size(); i++) {
            if (view.getAllies().get(i).getDirection().equals("left")
                    && !direction.equals("right")) {
                damageX = -20;

            } else if (view.getAllies().get(i).getDirection().equals("right")
                    && !direction.equals("left")) {
                damageX = 20;

            } else if (view.getAllies().get(i).getDirection().equals("up")
                    && !direction.equals("down")) {
                damageY = 20;

            } else if (view.getAllies().get(i).getDirection().equals("down")
                    && !direction.equals("up")) {
                damageY = -20;
            } else {
                /* Head-to-head collision with Player; they win since they are the actor
                 * Calculation: take the difference between both velocities on the X & Y*/
                if (view.getAllies().get(i).getDirection().equals("left")
                        && direction.equals("right")) {
                    damageX = Math.abs(velX - view.getAllies().get(i).velX + view.getAllies().get(i).damageX);
                } else if (direction.equals("right")
                        && view.getAllies().get(i).getDirection().equals("left")) {
                    damageX = -Math.abs(velX - view.getAllies().get(i).velX + view.getAllies().get(i).damageX);
                } else if (direction.equals("up")
                        && view.getAllies().get(i).getDirection().equals("down")) {
                    damageY = -Math.abs(velY - view.getAllies().get(i).velY + view.getAllies().get(i).damageY);
                } else if (direction.equals("down")
                        && view.getAllies().get(i).getDirection().equals("up")) {
                    damageY = Math.abs(velY - view.getAllies().get(i).velY + view.getAllies().get(i).damageY);
                }
            }
        }
    }

    void enemyDetection(ArrayList<Sprite> allies){
        for (int i = 0; i<allies.size(); i++) {
            //Runaway from Ally in the horizontal (X) direction
            if (enemyIsNearerToFoodHorizontally(allies, i)
                    && ((Math.abs(x - allies.get(i).x) >= Math.abs(y - allies.get(i).y)))
            ) {
                enemyDetectedX(allies);
                isEnemyDetected = true;
            }
            //Runaway from Ally in the vertical (Y) direction
            else if (((Math.abs(y - (allies.get(i).y)) < Math.abs(y - (food.y)))
                    && (Math.abs(y - (allies.get(i).y + allies.get(i).height))
                    < Math.abs(y - (food.y + food.height))

                    && (Math.abs(x - allies.get(i).x) <= Math.abs(y - allies.get(i).y)
                    && (Math.abs(x - allies.get(i).x+allies.get(i).height)
                    <= Math.abs(y - allies.get(i).y+allies.get(i).height) )
                    ))

                    )
            ) {
                enemyDetectedY(allies);
                isEnemyDetected = true;
            }
            else {
                isEnemyDetected = false;
            }
        }
    }

    private boolean enemyIsNearerToFoodHorizontally(ArrayList<Sprite> allies, int i) {
        return (Math.abs(x - (allies.get(i).x)) < Math.abs(x - (food.x)))
                && (Math.abs(x - (allies.get(i).x + allies.get(i).width))
                < Math.abs(x - (food.x + food.width)));
    }

    void enemyDetectedX(ArrayList<Sprite> allies){
        for (int i = 0; i<allies.size(); i++) {
            if (Math.abs(x - allies.get(i).x) >= Math.abs(y - allies.get(i).y)) {
                if (x < allies.get(i).x && x < (allies.get(i).x + allies.get(i).width)) {
                    if (Math.abs(y - allies.get(i).y) <= playerHeight){
                        //run upwards/downwards if trapped by two allies vertically
                        if (Math.abs(y - view.screenY) <= y) {
                            velY = SPEED;
                            direction = "up";
                        }
                        else {
                            velY = -SPEED;
                            direction = "down";
                        }
                        velX = 0;
                    }
                    if (view.numberOfBullets > 0) {
                        //shoot any ally standing in the way of the food
                        view.setLaserShotRight(true);
                        view.setLaserShotUp(false);
                        view.setLaserShotDown(false);
                        view.setLaserShotLeft(false);
                    }
                    else {
                        //Otherwise run sideways to the left!
                        velX = -SPEED;
                        velY = 0;
                        direction = "left";
                    }
                } else if (x > allies.get(i).x && x > (allies.get(i).x + allies.get(i).width)) {
                    if (Math.abs(y - allies.get(i).y) <= playerHeight){
                        if (Math.abs(y - view.screenY) < y) {
                            if (view.numberOfBullets > 0){
                                view.setLaserShotDown(true);
                            }
                            else {
                                velY = SPEED;
                                direction = "up";
                            }
                        }
                        else{
                            if (view.numberOfBullets > 0){
                                view.setLaserShotUp(true);
                            }
                            else {
                                velY = -SPEED;
                                direction = "down";
                            }
                        }
                        velX = 0;
                    }
                    if (view.numberOfBullets > 0 ) {
                        view.setLaserShotLeft(true);
                        view.setLaserShotUp(false);
                        view.setLaserShotDown(false);
                        view.setLaserShotRight(false);
                    }
                    else {
                        velX = SPEED;
                        velY = 0;
                        direction = "right";
                    }
                }
            }
        }
    }


    void enemyDetectedY(ArrayList<Sprite> allies){
        for (int i = 0; i<allies.size(); i++) {
            if (Math.abs(x - allies.get(i).x) <= Math.abs(y - allies.get(i).y)) {
                if (y < allies.get(i).y && y < (allies.get(i).y + allies.get(i).height)) {
                    //run side ways if the trapped by two allies vertically
                    if (Math.abs(x - allies.get(i).x) <= playerWidth) {
                        if (Math.abs(x - view.screenX) < x) {
                            if (view.numberOfBullets > 0){
                                view.setLaserShotLeft(true);
                            }
                            else {
                                velX = SPEED;
                                direction = "right";
                            }
                        } else {
                            if (view.numberOfBullets > 0){
                                view.setLaserShotRight(true);
                            }
                            else {
                                velX = -SPEED;
                                direction = "left";
                            }
                        }
                        velY = 0;
                    }
                    if (view.numberOfBullets > 0
                    && hasSameVerticalAxis(allies, i)){
                        view.setLaserShotUp(false);
                        view.setLaserShotDown(true);
                        view.setLaserShotLeft(false);
                        view.setLaserShotRight(false);
                    }
                    else {
                        if (view.numberOfBullets > 0){
                            view.setLaserShotDown(true);
                        }
                        else {
                            velY = -SPEED;
                            velX = 0;
                            direction = "up";
                        }
                    }

                }
            }
            else if (y > allies.get(i).y && y > (allies.get(i).y + allies.get(i).height)) {
                //run side ways if the trapped by two allies vertically
                if (Math.abs(x - allies.get(i).x) <= playerWidth){
                    if (Math.abs(x - view.screenX) < x) {
                        if (view.numberOfBullets > 0){
                            view.setLaserShotLeft(true);
                        }
                        else {
                            velX = SPEED;
                            direction = "right";
                        }
                    }
                    else{
                        if (view.numberOfBullets > 0){
                            view.setLaserShotRight(true);
                        }
                        else {
                            velX = -SPEED;
                            direction = "left";
                        }
                    }
                    velY = 0;
                }
                if (view.numberOfBullets > 0
                        && hasSameVerticalAxis(allies, i)){

                    view.setLaserShotDown(false);
                    view.setLaserShotUp(true);
                    view.setLaserShotLeft(false);
                    view.setLaserShotRight(false);
                }
                else {
                    if (view.numberOfBullets > 0){
                        view.setLaserShotUp(true);
                    }
                    else {
                        velY = SPEED;
                        velX = 0;
                        direction = "down";
                    }
                }
            }
        }
    }

    private boolean hasSameVerticalAxis(ArrayList<Sprite> allies, int i) {
        return x >= allies.get(i).x
                && x <= (allies.get(i).x
                + allies.get(i).width);
    }

    /* This method returns the enemy facing upwards;
     * mainly for navigation*/
    Bitmap getUp(){
        playerHeight = bitmapUp.getHeight();
        playerWidth = bitmapUp.getWidth();
        return bitmapUp;
    }
    Bitmap getDown(){
        playerHeight = bitmapDown.getHeight();
        playerWidth = bitmapDown.getWidth();
        return bitmapDown;
    }
    Bitmap getLeft(){
        playerHeight = bitmapLeft.getHeight();
        playerWidth = bitmapLeft.getWidth();
        return bitmapLeft;
    }
    Bitmap getRight(){
        playerHeight = bitmapRight.getHeight();
        playerWidth = bitmapRight.getWidth();
        return bitmapRight;
    }

    public Bitmap getBlueSpark() {
        playerHeight = blueSpark.getHeight();
        playerWidth =  blueSpark.getWidth();
        blueSpark = Bitmap.createScaledBitmap(blueSpark, playerWidth, playerHeight,false);
        return blueSpark;
    }

    public Bitmap getBeigeSpark() {
        playerHeight = beigeSpark.getHeight();
        playerWidth =  beigeSpark.getWidth();
        beigeSpark = Bitmap.createScaledBitmap(beigeSpark, playerWidth, playerHeight,false);
        return beigeSpark;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }
    public void stop(){
        velX = 0;
        velY = 0;
    }

    public Rect src() {
        return new Rect(0,0, playerWidth, playerHeight);
    }
    public Rect scaleFactor(){
        return new Rect(x, y, x+playerWidth, y+playerHeight);
    }


    //returns effect when shot or hit by the allies
    public Bitmap getGreenSpark() {
        return greenSpark;
    }

    public Paint getSilver() {
        return silver;
    }

    public Paint getRed() {
        return red;
    }

    void locateHealth() {
        view.setHealthDetected(true);
        if (x <= view.getNavigation().healthX && x < view.getNavigation().healthX + Navigation.width) {
            if (Math.abs(x - view.getNavigation().healthX + Navigation.width) < Math.abs(y - view.getNavigation().healthY + Navigation.height)) {
                direction = "down";
                velY = SPEED;
                velX = 0;
            } else if (y >= view.getNavigation().healthY && y > view.getNavigation().healthY + Navigation.height) {
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
        } else if (x >= view.getNavigation().healthX && x > view.getNavigation().healthX + Navigation.width) {
            if (Math.abs(x - view.getNavigation().healthX + Navigation.width) > Math.abs(y - view.getNavigation().healthY + Navigation.height)) {
                if (y <= view.getNavigation().healthY && y < view.getNavigation().healthY + Navigation.height) {
                    direction = "down";
                    velY = SPEED;
                    velX = 0;
                } else if (y >= view.getNavigation().healthY && y > view.getNavigation().healthY + Navigation.height) {
                    direction = "up";
                    velY = -SPEED;
                    velX = 0;
                } else if (view.button.equals(OurView.GAME_OPTION_3)) {
                    enemyDetection(view.getAllies());
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
            if (y <= view.getNavigation().healthY && y < view.getNavigation().healthY + Navigation.height) {
                if (Math.abs(x - view.getNavigation().healthX + Navigation.width) < Math.abs(y - view.getNavigation().healthY + Navigation.height)) {
                    if (x <= view.getNavigation().healthX && x < view.getNavigation().healthX + Navigation.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getNavigation().healthY && y > view.getNavigation().healthY + Navigation.height) {
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
            } else if (y >= view.getNavigation().healthY && y > view.getNavigation().healthY + Navigation.height) {
                if (Math.abs(x - view.getNavigation().healthX + Navigation.width) < Math.abs(y - view.getNavigation().healthY + Navigation.height)) {
                    if (x <= view.getNavigation().healthX && x < view.getNavigation().healthX + Navigation.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getNavigation().healthY && y > view.getNavigation().healthY + Navigation.height) {
                        direction = "left";
                        velX = -SPEED;
                        velY = 0;
                    } else if (view.button.equals(OurView.GAME_OPTION_3)) {
                        enemyDetection(view.getAllies());
                    } else {
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
    }

    void replenishBullets() {
        view.setLaserDetected(true);
        if (x <= view.getNavigation().bulletX && x < view.getNavigation().bulletX + Navigation.width) {
            if (Math.abs(x - view.getNavigation().bulletX + Navigation.width) < Math.abs(y - view.getNavigation().bulletY + Navigation.height)) {
                direction = "down";
                velY = SPEED;
                velX = 0;
            } else if (y >= view.getNavigation().bulletY && y > view.getNavigation().bulletY + Navigation.height) {
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
        } else if (x >= view.getNavigation().bulletX && x > view.getNavigation().bulletX + Navigation.width) {
            if (Math.abs(x - view.getNavigation().bulletX + Navigation.width) > Math.abs(y - view.getNavigation().bulletY + Navigation.height)) {
                if (y <= view.getNavigation().bulletY && y < view.getNavigation().bulletY + Navigation.height) {
                    direction = "down";
                    velY = SPEED;
                    velX = 0;
                } else if (y >= view.getNavigation().bulletY && y > view.getNavigation().bulletY + Navigation.height) {
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
            if (y <= view.getNavigation().bulletY && y < view.getNavigation().bulletY + Navigation.height) {
                if (Math.abs(x - view.getNavigation().bulletX + Navigation.width) < Math.abs(y - view.getNavigation().bulletY + Navigation.height)) {
                    if (x <= view.getNavigation().bulletX && x < view.getNavigation().bulletY + Navigation.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getNavigation().bulletY && y > view.getNavigation().bulletY + Navigation.height) {
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
            } else if (y >= view.getNavigation().bulletY && y > view.getNavigation().bulletY + Navigation.height) {
                if (Math.abs(x - view.getNavigation().bulletX + Navigation.width) < Math.abs(y - view.getNavigation().bulletY + Navigation.height)) {
                    if (x <= view.getNavigation().bulletX && x < view.getNavigation().bulletX + Navigation.width) {
                        direction = "right";
                        velX = SPEED;
                        velY = 0;
                    } else if (y >= view.getNavigation().bulletY && y > view.getNavigation().bulletY + Navigation.height) {
                        direction = "left";
                        velX = -SPEED;
                        velY = 0;
                    } else {
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
    }
}
