package com.capstone.challengeweek.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.capstone.challengeweek.R;

/*This class creates a Laser Bullet*/
public class Laser {
    private static final int PADDING = 15;
    private final Bitmap bulletBitmap;
    private boolean lockUp, lockDown, lockLeft, lockRight;
    private int laserVelX, laserVelY;
    private Bitmap laserUp, laserDown, laserLeft, laserRight;
    int width, height;
    OurView view;
    public float laserUpX, laserUpY, lockXPosUp, lockYPosUp;
    public float laserDownX, laserDownY, lockXPosDown, lockYPosDown;
    public float laserLeftY, laserLeftX, lockYPosLeft, lockXPosLeft;
    public float laserRightY, laserRightX, lockYPosRight, lockXPosRight;
    Sprite sprite;
    private boolean releaseLaserUp, releaseLaserDown, releaseLaserRight, releaseLaserLeft;

    public Laser(OurView view, Resources resources, Sprite sprite) {
        this.view = view;
        this.sprite = sprite;
        laserUp = BitmapFactory.decodeResource(resources, R.drawable.laser_green_up);
        laserDown = BitmapFactory.decodeResource(resources, R.drawable.laser_green_down);
        laserLeft = BitmapFactory.decodeResource(resources, R.drawable.laser_green_left);
        laserRight = BitmapFactory.decodeResource(resources, R.drawable.laser_green_right);
        //laser velocity
        laserVelX = 150;
        laserVelY = 150;
        lockUp = false;
        lockDown = false;
        lockLeft = false;
        lockRight = false;
        //red laser to indicate an empty bullet
        bulletBitmap = BitmapFactory.decodeResource(resources, R.drawable.player_bullet);
        //determine if enemy has shot the laser
        releaseLaserUp = releaseLaserDown = releaseLaserLeft = releaseLaserRight = false;
    }


    //This method returns laser facing downwards
    Bitmap getLaserUp() {
        if (view.button.equals(OurView.GAME_OPTION_3)){
            laserUpX = (float) (view.getEnemy().x + (view.getEnemy().playerWidth / alignLaser()));
        }
        else {
            laserUpX = (float) (view.getSprite().x + (view.getSprite().width / alignLaser()));
        }
        if (laserUpY > 0 && view.isLaserShotUp()) {
            releaseLaserUp = true;
            if (!lockUp) {
                lockYPosUp = laserUpY;
                lockXPosUp = laserUpX;
                lockUp = true;
            } else {
                lockYPosUp -= laserVelY;
                laserUpY = lockYPosUp;
                laserUpX = lockXPosUp;
            }
        } else {

            lockUp = false;
            if (view.button.equals(OurView.GAME_OPTION_3)) {
                if (releaseLaserUp){
                    view.numberOfBullets -= 1;
                    view.soundPlayer.playLaserSound();
                    releaseLaserUp = false;
                }
                else {
                    laserUpY = view.getEnemy().y + PADDING;
                }
            }
            else {
                laserUpY = view.getSprite().y;
            }
            view.setLaserShotUp(false);
        }
        return laserUp;
    }

    private double alignLaser() {
        return 2.19;
    }

    public Bitmap getLaserDown() {
        if (view.button.equals(OurView.GAME_OPTION_3)) {
            laserDownX = (float) (view.getEnemy().x + (view.getEnemy().playerWidth / alignLaser()));
        }
        else {
            laserDownX = (float) (view.getSprite().x + (view.getSprite().width / alignLaser()));
        }
        if (laserDownY < view.screenY && view.isLaserShotDown()) {
            releaseLaserDown = true;
            if (!lockDown) {
                lockYPosDown = laserDownY;
                lockXPosDown = laserDownX;
                lockDown = true;
            } else {
                lockYPosDown += laserVelY;
                laserDownY = lockYPosDown;
                laserDownX = lockXPosDown;
            }
        } else {
            lockDown = false;

            if (view.button.equals(OurView.GAME_OPTION_3)) {
                if (releaseLaserDown){
                    view.numberOfBullets -= 1;
                    releaseLaserDown = false;
                    view.soundPlayer.playLaserSound();
                }
                else{
                    laserDownY = (float) ((view.getEnemy().y - PADDING) + view.getEnemy().playerHeight / alignLaser());
                }
            }
            else {
                laserDownY = (float) (view.getSprite().y + view.getSprite().height / alignLaser());
            }
            view.setLaserShotDown(false);
        }
        return laserDown;
    }

    public Bitmap getLaserLeft() {
        if (view.button.equals(OurView.GAME_OPTION_3)) {
            laserLeftY = (float) (view.getEnemy().y + (view.getEnemy().playerHeight / alignLaser()));
        }
        else {
            laserLeftY = (float) (view.getSprite().y + (view.getSprite().height / alignLaser()));
        }
        //check if the laser bullet is less than the screen, and if the enemy is not shot
        if (laserLeftX > 0 && view.isLaserShotLeft()) {
            releaseLaserLeft = true;
            /* Ensure that laser bullet goes straight and does not deviate from the
             * point where it was fired */
            if (!lockLeft) {
                lockXPosLeft = laserLeftX;
                lockYPosLeft = laserLeftY;
                lockLeft = true;
            } else {
                lockXPosLeft -= laserVelX;
                laserLeftX = lockXPosLeft;
                laserLeftY = lockYPosLeft;
            }
        } else {
            if (view.button.equals(OurView.GAME_OPTION_3)) {
                if (releaseLaserLeft){
                    view.numberOfBullets -= 1;
                    releaseLaserLeft = false;
                    view.soundPlayer.playLaserSound();
                }
                else{
                    laserLeftX = (view.getEnemy().x + PADDING);
                }
            }
             else {
                 laserLeftX = (view.getSprite().x);
            }
            view.setLaserShotLeft(false);
            lockLeft = false;
        }
        return laserLeft;
    }

    public Bitmap getLaserRight() {
        if (view.button.equals(OurView.GAME_OPTION_3)) {
            laserRightY = (float) (view.getEnemy().y + (view.getEnemy().playerHeight / alignLaser()));
        }
        else {
            laserRightY = (float) (view.getSprite().y + (view.getSprite().height / alignLaser()));
        }
        if (laserRightX < view.screenX && view.isLaserShotRight()) {
            releaseLaserRight = true;
            if (!lockRight) {
                lockXPosRight = laserRightX;
                lockYPosRight = laserRightY;
                lockRight = true;
            } else {
                lockXPosRight += laserVelX;
                laserRightX = lockXPosRight;
                laserRightY = lockYPosRight;
            }
        } else {
            view.setLaserShotRight(false);

            if (view.button.equals(OurView.GAME_OPTION_3)) {
                laserRightX = (float) (view.getEnemy().x - PADDING + view.getEnemy().playerWidth / alignLaser());
                if (releaseLaserRight){
                    view.numberOfBullets -= 1;
                    releaseLaserRight = false;
                    view.soundPlayer.playLaserSound();
                }
            }
            else{
                laserRightX = (float) ((view.getSprite().x)+ view.getSprite().width / alignLaser());
             }
            lockRight = false;
        }
        return laserRight;
    }


    public Bitmap getBulletBitmap() {
        width = bulletBitmap.getWidth();
        height = bulletBitmap.getHeight();
        return bulletBitmap;
    }
}
