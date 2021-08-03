package com.capstone.challengeweek.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.capstone.challengeweek.R;
/*This class is used to specify the direction, cursor & menu buttons on the game screen*/

public class Navigation{
    private float showCursorX, showCursorY;
    private Bitmap showCursor;
    private Bitmap menu, health, cursorBitmap, up, down, right, left, replenishLaserBullets, powerUpAlterEgo;
    private Bitmap tapUp, tapDown, tapLeft, tapRight, cursorBitmapTap, menuTap;
    protected OurView view;
    //Initialise the dimensions of the icons
    float menuX, menuY, healthX, healthY;
    float cursorX, cursorY;
    float leftX, leftY, rightX, rightY, upX, upY, downX, downY;
    float bulletX, bulletY;
    private boolean isTapUp, isTapDown, isTapLeft, isTapRight, isMenuTap, isCursorBitmapTap;


    float backgroundX, backgroundY;
    public static float width, height;
    public Navigation(OurView view, Resources resources){
        this.view = view;
        menu = BitmapFactory.decodeResource(resources, R.drawable.menu);
        health = BitmapFactory.decodeResource(resources, R.drawable.more_health);
        cursorBitmap = BitmapFactory.decodeResource(resources, R.drawable.cursor);
        showCursor = BitmapFactory.decodeResource(resources, R.drawable.show_cursor);
        //get directions from Drawable folder
        up = BitmapFactory.decodeResource(resources, R.drawable.direction_up);
        down = BitmapFactory.decodeResource(resources, R.drawable.direction_down);
        left = BitmapFactory.decodeResource(resources, R.drawable.direction_left);
        right = BitmapFactory.decodeResource(resources, R.drawable.direction_right);
        replenishLaserBullets = BitmapFactory.decodeResource(resources, R.drawable.more_bullets);


        tapUp = BitmapFactory.decodeResource(resources, R.drawable.direction_up_tap);
        tapDown = BitmapFactory.decodeResource(resources, R.drawable.direction_down_tap);
        tapLeft = BitmapFactory.decodeResource(resources, R.drawable.direction_left_tap);
        tapRight = BitmapFactory.decodeResource(resources, R.drawable.direction_right_tap);
        cursorBitmapTap = BitmapFactory.decodeResource(resources, R.drawable.cursor_tap);
        menuTap = BitmapFactory.decodeResource(resources, R.drawable.menu_tap);


        bulletX = bulletY = 300;
        healthX = healthY = 200;
        //initialise menu & cursor bitmaps
        menuX = view.fontMargin;
        menuY = view.screenY - menu.getHeight();
        cursorY = view.screenY - cursorBitmap.getHeight();
        cursorX = view.screenX - cursorBitmap.getWidth();
        backgroundX = backgroundY = 500;
        //initialise direction keys
        upX = (float) (view.screenX * 0.46);
        downX = (float) (view.screenX * 0.46);
        upY = view.screenY - down.getHeight();
        downY = (float) (view.screenY - (down.getHeight()*2.05));
        leftX = (float) ((float) ((view.screenX * 0.375)));
        rightX = (float) ((float) ((view.screenX * 0.41))+left.getWidth() * 1.05);
        leftY = rightY = (float) (view.screenY - (up.getHeight() * 2)) + right.getHeight();
        //initialise cursor indicator
        showCursorX = showCursorY = 0;
        isTapDown = isTapLeft = isTapUp = isTapRight = isMenuTap = isCursorBitmapTap = false;
    }
    //Method to return menu icon
    public Bitmap menu(){
        width = menu.getWidth();
        height = menu.getHeight();
        return menu;
    }
    public Bitmap health(){
        width = health.getWidth();
        height = health.getHeight();
        return health;
    }

    public Bitmap getNewLaserBullets() {
        width = replenishLaserBullets.getWidth();
        height = replenishLaserBullets.getHeight();
        return replenishLaserBullets;
    }

    public Bitmap cursor (){
        width = cursorBitmap.getWidth();
        height = cursorBitmap.getHeight();
        return cursorBitmap;
    }

    public Bitmap getUp() {
        width = up.getWidth();
        height = up.getHeight();
        return up;
    }

    public Bitmap getDown() {
        width = down.getWidth();
        height = down.getHeight();
        return down;
    }

    public Bitmap getRight() {
        width = right.getWidth();
        height = right.getHeight();
        return right;
    }

    public Bitmap getLeft() {
        width = left.getWidth();
        height = left.getHeight();
        return left;
    }

    public Bitmap getShowCursor() {
        width = showCursor.getWidth();
        height = showCursor.getHeight();
        return showCursor;
    }

    public Bitmap getTapUp() {
        width = tapUp.getWidth();
        height = tapUp.getHeight();
        return tapUp;
    }

    public Bitmap getTapDown() {
        width = tapDown.getWidth();
        height = tapDown.getHeight();
        return tapDown;
    }

    public Bitmap getTapLeft() {
        width = tapLeft.getWidth();
        height = tapLeft.getHeight();
        return tapLeft;
    }

    public Bitmap getTapRight() {
        width = tapRight.getWidth();
        height = tapRight.getHeight();
        return tapRight;
    }


    public boolean isTapUp() {
        return isTapUp;
    }

    public boolean isTapDown() {
        return isTapDown;
    }

    public boolean isTapLeft() {
        return isTapLeft;
    }

    public boolean isTapRight() {
        return isTapRight;
    }

    public void setTapUp(boolean tapUp) {
        isTapUp = tapUp;
    }

    public void setTapDown(boolean tapDown) {
        isTapDown = tapDown;
    }

    public void setTapLeft(boolean tapLeft) {
        isTapLeft = tapLeft;
    }

    public void setTapRight(boolean tapRight) {
        isTapRight = tapRight;
    }

    public Bitmap getCursorBitmapTap() {
        return cursorBitmapTap;
    }

    public Bitmap getMenuTap() {
        return menuTap;
    }

    public void setMenuTap(boolean menuTap) {
        isMenuTap = menuTap;
    }

    public void setCursorBitmapTap(boolean cursorBitmapTap) {
        isCursorBitmapTap = cursorBitmapTap;
    }

    public boolean isMenuTap() {
        return isMenuTap;
    }

    public boolean isCursorBitmapTap() {
        return isCursorBitmapTap;
    }
}