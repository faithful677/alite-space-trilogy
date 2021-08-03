package com.capstone.challengeweek.GameObjects;

/*This class handles all the game processing and logic*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.capstone.challengeweek.Game;
import com.capstone.challengeweek.Media.SoundPlayer;
import com.capstone.challengeweek.R;

import java.util.ArrayList;
import java.util.Random;


@SuppressLint("ViewConstructor")
public class OurView extends SurfaceView implements Runnable {
    public static final int TOTAL_NUMBER_OF_LEVELS = 5;
    public static final int SCORE_THRESHOLD = 40;
    public static final int TIME_DELAY_BEFORE_GAME_STARTS = 4;
    public static final int TIMEOUT = 0;
    public static final int BULLET_LIMIT = 0;
    public static final int ADD_TIME = 10;
    public static final int ADD_BULLETS = 10;
    public static final String SCORE_HUD = "Score: ";
    public static final String AI_SCORE_HUD = "AI Score: ";
    public static final double A_MEASURE_OF_DEATH = 1.2;
    public static long CURRENT_TIME;
    private static long COPY_END;
    @SuppressLint("StaticFieldLeak")
    private static Context CONTEXT;
    private Game game;
    int screenX, screenY;
    //Variables for drawing to the screen
    private SurfaceHolder surfaceHolder;
    public static Thread thread;
    public static boolean check;//to check if the game is paused
    private Paint paint;
    private Canvas canvas;
    //game objects: cyan represents the enemy, while sprite_sheet, the user
    private Sprite sprite;
    private boolean spriteLoaded;
    private Food food;
    private Enemy newEnemy, enemy;
    //user touch coordinates
    public static float touchX;
    public static float touchY;
    /*a string with values of either left, right, up, or down
     *to indicate the current direction of the user*/
    private String direction;
    //this bitmap (purple) represents the food
    //player score
    private int score;
    private int death;
    private static final int LENGTH_OF_HEALTH_BAR = 450;
    Rect rect;
    private Paint green, red;
    // Font is 5% (1/20th) of screen width
    private float fontSize;
    // Margin is 1.5% (1/75th) of screen width
    public float fontMargin;
    private ArrayList<Enemy> enemies;
    private int enemyID;
    private int allyID;
    //Timer
    long startTime, timeElapsed;
    private static long TIMER;
    //get the button that was clicked in the Options activity
    public String button;
    public static String GAME_OPTION_1 = "default_game";
    //Game option 2
    public static String GAME_OPTION_2 = "food_fest";
    //Game option 3
    public static String GAME_OPTION_3 = "checkers";
    public int AIScore;
    public static long FOOD_TIME_START;
    public static long FOOD_TIME_END;
    private String winner;
    public static boolean endGame = false;
    private boolean displayLevels;
    private int currentLevel;
    private Level level;
    public static boolean startGame = false;
    //Restart game
    public String restart = null;
    //Detect Double tap to halt movement of the user-controlled character
    private boolean firstTouch;
    //Save the time the on-screen button was clicked
    private long firstTouchTime;
    //Navigation items: menu, direction buttons, and cursor button
    private Navigation navigation;
    private boolean pausePressed;

    private long pauseTimer;
    //First-aid kit
    private boolean healthDetected;
    //variable LIFE is used in both the 1st & 3rd game variation
    private int life;
    //dialog box class which gives users the option to restart
    private CustomTask customTask;
    //this thread runs in the custom task class
    private Thread thread1;
    public static long levelTimer, levelTimerEnd;
    private boolean testDialog;
    //Food Fest Database
    private static PlayerDB PLAYER_DB;
    private SQLiteDatabase database;
    private boolean temp;
    private int savedRound;
    private int objective;
    private boolean levelComplete;
    private int savedLevel;
    private boolean currentStageCalled;//check if currentStage() method is called to get the most up to date round
    private ArrayList<Sprite> allies; //for  GAME_OPTION_3; to hold allies
    //change cursor between game objects
    private Sprite cursor;
    private int cursorValue;
    //Game complete: if true, then game complete
    private boolean gameComplete;
    //var to detect whether a new ally has been called in Food Fest: Part II
    private boolean help;
    //counter
    private int k;
    private boolean popUpMenu;
    private float screenRatioX, screenRatioY;
    //Player bullet
    public boolean isLaserShotUp, isLaserShotDown, isLaserShotLeft, isLaserShotRight;
    boolean isShot;
    private Laser laser;
    public int numberOfBullets;
    //Font style
    private Background background;

    //SoundPlayer
    public SoundPlayer soundPlayer;
    private boolean keepRunning;
    private int enemyLife;
    private Paint glow;
    private boolean laserDetected;
    private AlertDialog.Builder testBuilder;


    ArrayList<Bitmap> littleStar;
    private Typeface typeface;

    public OurView(Context context, int screenX, int screenY, String button, Game game) {
        super(context);
        init(context, button, screenX, screenY, game);
    }
    public void init(Context context, String button, int screenX, int screenY, Game game) {
        CONTEXT = context;
        this.game = game;
        this.button = button;
        this.screenX = screenX;
        this.screenY = screenY;
        background = new Background(this, getResources());
        check = false;
        thread = null;
        customTask = null;
        thread1 = null;
        littleStar = new ArrayList<>();
        littleStar.add(background.getSmallStarOne());
        littleStar.add(background.getSmallStarTwo());
        littleStar.add(background.getSmallStarThree());
        littleStar.add(background.getSmallStarFour());
        littleStar.add(background.getSmallStarFive());
        //get number of pixels from the screen
        surfaceHolder = getHolder();
        touchX = touchY = 0;
        direction = "null";
        //font size & font margin
        fontSize = (float) (this.screenX / 20.0);
        fontMargin = (float) (this.screenX / 75.0);
        //paint for the HUD
        paint = new Paint();
        glow = new Paint();
        typeface = ResourcesCompat.getFont(CONTEXT, R.font.kenvector_future_thin);
        paint.setTypeface(typeface);
        //Choose the font size for the HUD
        paint.setTextSize(fontSize);
        paint.setColor(Color.argb(255, 250, 240, 245));

        //initialise SoundPlayer
        soundPlayer = new SoundPlayer(CONTEXT);
        //Enemies
        enemies = new ArrayList<>();
        newEnemy = null;
        enemyID = 0;
        //User Score
        allies = new ArrayList<>();
        allyID = 0;
        score = 0;
        currentStageCalled = false;
        //Player's life
        life = screenX - 350;
        death = 0;
        //Game complete
        gameComplete = false;
        PLAYER_DB = new PlayerDB(CONTEXT, this);
        database = PLAYER_DB.getWritableDatabase();

        popUpMenu = false;

        //display levels
        displayLevels = true;


        food = new Food(this, getResources());

        //Default game mode: Part I
        if (button.equals(GAME_OPTION_1)) {
            load_database_game_option_one();
        }
        else if (button.equals(GAME_OPTION_2)) {
            load_database_game_option_two();
        }
        else if (button.equals(GAME_OPTION_3)) {
            load_database_game_option_three();
        }

        //Check if game objects are loaded
        spriteLoaded = false;
        endGame = false;
        //firstTouch is used when the user wants to pause motion
        firstTouch = false;
        pausePressed = false;
        pauseTimer = 0;
        //Health bar; mainly used for 1st & 3rd game variation
        green = new Paint();
        red = new Paint();
        rect = new Rect(1000, 550, 400, 150);
        healthDetected = false;
        laserDetected = false;
        //object used to add navigation icons
        navigation = new Navigation(this, getResources());
        isLaserShotUp = isLaserShotDown = isLaserShotRight = isLaserShotLeft = false;
        //check if enemy is shot
        isShot = false;
        laser = new Laser(this, getResources(), sprite);

        level = new Level(this, CONTEXT);
        setLevelComplete(false);
    }

    private void load_database_game_option_three() {
        AIScore = 0;
        if (!currentStageCalled) {
            currentStage();
        }
        if (!PLAYER_DB.isEmpty()) {
            if (savedLevel + 1 > TOTAL_NUMBER_OF_LEVELS) {
                setCurrentLevel(TOTAL_NUMBER_OF_LEVELS);
                gameComplete = true;
            } else {
                gameComplete = false;
                setCurrentLevel(savedLevel + 1);
                objective = (120 - (20 * savedLevel));
                levelTimer = (System.currentTimeMillis() / 1000) + TIME_DELAY_BEFORE_GAME_STARTS;
            }
        } else {
            setCurrentLevel(1);
            objective = (120 - (20 * savedLevel));
            levelTimer = (System.currentTimeMillis() / 1000) + TIME_DELAY_BEFORE_GAME_STARTS;
        }
        numberOfBullets = 30;
        enemy = new Enemy(this, sprite, food, getResources(), (enemyID += 1));
        sprite = new Sprite(this, getResources(), (allyID += 1));
        //allies represent user-controlled game characters
        allies.add(sprite);
        cursorValue = 0;
        cursor = sprite;

    }

    private void load_database_game_option_two() {
        startTime = System.nanoTime();
        AIScore = 0;
        help = false;
        cursor = sprite;
        FOOD_TIME_START = System.currentTimeMillis();
        if (!currentStageCalled) {
            currentStage();
        }
        if (!PLAYER_DB.isEmpty()) {
            TIMER = ((FOOD_TIME_START / 1000) + 44 + (20 * PLAYER_DB.getSize()));
            if (savedRound + 1 > TOTAL_NUMBER_OF_LEVELS) {
                gameComplete = true;
                setCurrentLevel(TOTAL_NUMBER_OF_LEVELS);
            } else {
                setCurrentLevel((savedRound + 1));
            }
        } else {
            TIMER = ((FOOD_TIME_START / 1000) + 44 + (20 * savedRound));
            setCurrentLevel(1);
        }
        levelTimer = (System.currentTimeMillis() / 1000) + TIME_DELAY_BEFORE_GAME_STARTS;
        winner = "null";
        temp = false;

        sprite = new Sprite(this, getResources(), (allyID += 1));
        cursor = sprite;
        allies.add(sprite);
        cursorValue = 0;
        enemies.add(new Enemy(this, sprite, food, getResources(), (enemyID += 1)));

    }

    private void load_database_game_option_one() {
        if (!currentStageCalled) {
            currentStage();
        }
        if (!PLAYER_DB.isEmpty()) {
            //detect game completion
            if (savedLevel + 1 > TOTAL_NUMBER_OF_LEVELS) {
                setCurrentLevel(TOTAL_NUMBER_OF_LEVELS);
                gameComplete = true;
            } else {
                gameComplete = false;
                setCurrentLevel(savedLevel + 1);
                //Level objectives
                objective = (SCORE_THRESHOLD + (20 * savedLevel));
                levelTimer = (System.currentTimeMillis() / 1000) + TIME_DELAY_BEFORE_GAME_STARTS;
            }
        } else {
            setCurrentLevel(1);
            //Level objectives
            objective = (SCORE_THRESHOLD + (20 * savedLevel));
            levelTimer = (System.currentTimeMillis() / 1000) + TIME_DELAY_BEFORE_GAME_STARTS;
        }
        numberOfBullets = 30;
        sprite = new Sprite(this, getResources(), (allyID += 1));
        enemies.add(new Enemy(this, sprite, food, getResources(), (enemyID += 1)));
    }

    /*Getters*/

    public Game getGame() {
        return game;
    }

    public String getWinner() {
        return winner;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getObjective() {
        return objective;
    }


    public String getRestart() {
        return restart;
    }



    /* This method returns the cursor which is used to determine
     * which game object the user is controlling at the moment*/
    public Sprite getCursor() {
        return cursor;
    }

    public boolean getGameComplete() {
        return gameComplete;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    //display Game levels as boolean values: useful for determining whether a level is over or not
    public boolean isDisplayLevels() {
        return displayLevels;
    }

    //display Game levels as a String: useful for determining the current game level

    public int getCurrentLevel() {
        return currentLevel;
    }
    public boolean getPopUpMenu() {
        return popUpMenu;
    }

    public int getAIScore() {
        return AIScore;
    }

    public int getScore() {
        return score;
    }

    public Food getFood() {
        return food;
    }

    public ArrayList<Sprite> getAllies() {
        return allies;
    }

    public Navigation getNavigation() {
        return navigation;
    }
    public Sprite getSprite() {
        return this.sprite;
    }

    //This method identifies an enemy who is being shot & decreases its life
    void getWoundedEnemy(Enemy enemy) {
        if (numberOfBullets > 0) {
            enemy.enemyDeath /= 2;
            isShot = false;
            //only allow one bullet to affect one enemy
            isLaserShotUp = false;
            isLaserShotDown = false;
            isLaserShotLeft = false;
            isLaserShotRight = false;
        }
    }


    /*Predicates*/
    public boolean isPausePressed() {
        return pausePressed;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public boolean isHealthDetected() {
        return healthDetected;
    }

    public boolean isLaserDetected() {
        return laserDetected;
    }
    public boolean isLaserShotUp() {
        return isLaserShotUp;
    }

    public boolean isLaserShotDown() {
        return isLaserShotDown;
    }

    public boolean isLaserShotLeft() {
        return isLaserShotLeft;
    }

    public boolean isLaserShotRight() {
        return isLaserShotRight;
    }

    /*Setters*/
    public void setRestart(String value) {
        restart = value;
    }

    public void setDisplayLevels(boolean displayLevels) {
        this.displayLevels = displayLevels;
    }

    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    public void setPausePressed(boolean pausePressed) {
        this.pausePressed = pausePressed;
    }

    public void setWinner(String value) {
        winner = value;
    }
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }


    public void setLaserShotUp(boolean laserShot) {
        isLaserShotUp = laserShot;
    }

    public void setLaserShotDown(boolean laserShotDown) {
        isLaserShotDown = laserShotDown;
    }

    public void setLaserShotLeft(boolean laserShotLeft) {
        isLaserShotLeft = laserShotLeft;
    }

    public void setLaserShotRight(boolean laserShotRight) {
        isLaserShotRight = laserShotRight;
    }
    public void setHealthDetected(boolean healthDetected) {
        this.healthDetected = healthDetected;
    }

    public void setLaserDetected(boolean laserDetected) {
        this.laserDetected = laserDetected;
    }

    @Override
    public void run() {
        keepRunning = true;
        while (keepRunning) {
            check = true;
            if (button.equals(GAME_OPTION_2)) {
                if (PLAYER_DB.isEmpty()) {
                    FOOD_TIME_START = (System.currentTimeMillis());
                }
            }
            while (check) {
                //perform canvas drawing
                if (surfaceHolder.getSurface().isValid()) {
                    canvas = surfaceHolder.lockCanvas();
                    draw();
                    //unlock canvas so that it becomes available to other threads
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                sleep();
            }

            //busy-wait until a response is received
            while (true) {
                if (hasUserDecidedToRestartOrNot()) break;
            }
            if (this.getRestart().trim().compareToIgnoreCase("true") == 0) {
                check = true;
                if (gameComplete) {
                    clearTable();
                }
                restartLevelOrRound();
            } else {
                closeResources();
            }
            exitGame();
        }
    }

    private boolean hasUserDecidedToRestartOrNot() {
        if (this.getRestart() != null && this.getRestart().trim().compareToIgnoreCase("true") == 0) {
            return true;
        }
        return this.getRestart() != null && this.getRestart().trim().compareToIgnoreCase("false") == 0;
    }

    private void restartLevelOrRound() {
        //init := initialise
        init(CONTEXT, button, screenX, screenY, game);
    }

    public void exitGame() {
        endGame = true;
        //clear the Activity stack: https://stackoverflow.com/questions/7075349/android-clear-activity-stack
        new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setRestart("null");
    }

    public void closeResources() {
        thread = null;
        keepRunning = false;
        PLAYER_DB.close();
        database.close();
        soundPlayer = null;
        //go back to Options activity
        game.transition();
    }

    private void clearTable() {
        PLAYER_DB = new PlayerDB(CONTEXT, this);
        if (button.equals(GAME_OPTION_1)) PLAYER_DB.clearDefaultTable(database);
        else if (button.equals(GAME_OPTION_2)) PLAYER_DB.clearFoodFestTable(database);
        else if (button.equals(GAME_OPTION_3)) PLAYER_DB.clearInvertedTable(database);
    }

    //Determine the number of frames per second
    private void sleep() {
        try {
            Thread.sleep(27);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //The following method draws the game objects
    public void draw() {

        //draw background bitmaps for each Part
        drawGameBackground();
        displayStarsToScreen();

        //Navigation icons: cursor (on the RHS of the screen) & menu
        displayMenuBitmap();

        displayCursorBitmap();
        displayNavigationButtons();

        displayLevelsDialogBox();


        spawnNewEnemy();
        canvas.drawBitmap(food.getCoin(), food.x, food.y, null);


        /* Initialise game objects && detect game or
         * level completion for each game variation */
        if (button.equals(GAME_OPTION_1)) {
            canvas.drawText(SCORE_HUD + (score), fontMargin, 70, paint);
            if (numberOfBullets <= BULLET_LIMIT){
                canvas.drawText(""+0, screenX - 100, 70, paint);
            }
            else {
                canvas.drawText(""+numberOfBullets, screenX - 100, 70, paint);
            }
            //Draw the number of bullets the user has to the screen
            canvas.drawBitmap(laser.getBulletBitmap(),screenX - 200, 25, null);

            //change the tone of green colour as player loses life
            green.setColor(Color.argb(200, 230, 30, 60));
            red.setColor(Color.argb(200, 30 + (death / 15), 230, 0));
            canvas.drawRect(  screenX - 350, 20, LENGTH_OF_HEALTH_BAR, 80, green);
            /*LENGTH_OF_HEALTH_BAR is the right coordinate of the previous canvas.drawRect(...)
            a.k.a the least amount of green equal to red*/
            if ((life - death) >= LENGTH_OF_HEALTH_BAR) {
                canvas.drawRect(life - death, 20, LENGTH_OF_HEALTH_BAR, 80, red);
                if (gameComplete) {
                    //detect game completion
                    savedLevel = 0;
                    endGame = true;
                    check = false;
                    setLevelComplete(true);
                    end(CONTEXT);
                }
                else if (score >= objective){
                    setLevelComplete(true);
                    endGame = true;
                    check = false;
                    end(CONTEXT);
                }
            } else if ((life - death) < LENGTH_OF_HEALTH_BAR) {
                if (!gameComplete) {
                    //game level complete
                    endGame = true;
                    check = false;
                    end(CONTEXT);
                }
            }
        }
        else if (button.equals(GAME_OPTION_2)) {
            CURRENT_TIME = (TIMER - FOOD_TIME_END);
            canvas.drawText(SCORE_HUD + (score), fontMargin, 70, paint);
            canvas.drawText(AI_SCORE_HUD + (AIScore), (screenX - ((fontMargin + fontSize) * 5)), 70, paint);

            if (!isPausePressed()) {
                if (temp) {
                    TIMER = (TIMER + (System.currentTimeMillis() / 1000 - COPY_END));
                }
                temp = false;
            }
            if (isPausePressed()) {
                if (!temp) {
                    COPY_END = FOOD_TIME_END;
                }
                temp = true;
            }
            FOOD_TIME_END = System.currentTimeMillis() / 1000;
            if (gameComplete) {
                savedRound = 0;
                endGame = true;
                check = false;
                setLevelComplete(true);
                end(CONTEXT);
            } else {
                if (isTimeElapsed()) {
                    if (AIScore > score)
                        setWinner("computer");
                    else if (AIScore < score) {
                        setWinner("user");
                        setLevelComplete(true);
                    } else {
                        setWinner("draw");
                    }
                    end(CONTEXT);
                } else {
                    if (CURRENT_TIME > 60) {
                        if (isTimeElapsedMoreThanOneMinute()) {
                            canvas.drawText("" + (int) (CURRENT_TIME / 60) + ":" + CURRENT_TIME % 60,(float)(((screenX - (fontMargin + AI_SCORE_HUD.length()) + (fontMargin))/2.5  )), 70, paint);
                        } else {
                            canvas.drawText("" + (int) (CURRENT_TIME / 60) + ":0" + CURRENT_TIME % 60, (float)(((screenX - (fontMargin + AI_SCORE_HUD.length()) + (fontMargin))/2.5  )), 70, paint);
                        }
                    } else {
                        canvas.drawText("" + CURRENT_TIME,(float)(((screenX - (fontMargin + AI_SCORE_HUD.length()) + (fontMargin))/2  )), 70, paint);
                    }
                }
            }
        }
        else if (button.equals(GAME_OPTION_3)) {
            drawAIHUD();
            detectGameCompletion();

            //cursor represents the game object that you control
            if (allies.size() > 0 && allies.size() >= cursorValue + 1) {
                cursor = allies.get(cursorValue);
            }

            displayProtagonistDirections();
            displayAndUpdateEnemyHealth();

            //Update ally direction
            if (!isPausePressed()) {
                updateAIMovement();

                updateAntagonistMovement();
            }
            //Collision detection between Enemy & food
            detectMoneyAcquisitionByProtagonist();
            //detect collision between Enemy & User: Enemy's life decreases ten-fold
            detectAntagonistAttack();
            //Draw the laser on the enemy space ship
            detectLaserCollisionWithAntagonists();

            addBonusHealthAtSpecificTimes();
        }
        /*Program the logic of each game variation*/
        if (button.equals(GAME_OPTION_1)
                || button.equals(GAME_OPTION_2)) {
            if (!isPausePressed()) {
                drawEnemiesOnScreenAndUpdate();
                drawEnemyHealthBar();
            }
            if (button.equals(GAME_OPTION_1)) {
                if (!isPausePressed()) {
                    addBonusHealthAtSpecificTimes();
                    addExtraBulletsForAlly();
                    updateAndDisplayUserMovement();

                    detectCollisionBetweenEnemiesAndUser();
                    detectFoodAcquisition(sprite);
                }
                if (numberOfBullets > 0) {
                    if (direction.equals("up") && !isLaserShotUp) {
                        canvas.drawBitmap(laser.getLaserUp(), laser.laserUpX, laser.laserUpY, null);
                    } else if (direction.equals("down") && !isLaserShotDown) {
                        canvas.drawBitmap(laser.getLaserDown(), laser.laserDownX, laser.laserDownY, null);
                    } else if (direction.equals("left") && !isLaserShotLeft) {
                        canvas.drawBitmap(laser.getLaserLeft(), laser.laserLeftX, laser.laserLeftY, null);
                    } else if (direction.equals("right") && !isLaserShotRight) {
                        canvas.drawBitmap(laser.getLaserRight(), laser.laserRightX, laser.laserRightY, null);
                    } else if (direction.equals("halt")) {
                        canvas.drawBitmap(laser.getLaserUp(), laser.laserUpX, laser.laserUpY, null);
                    }
                    if (isLaserShotUp) {
                        canvas.drawBitmap(laser.getLaserUp(), laser.laserUpX, laser.laserUpY, null);
                        for (int i = 0; i < enemies.size(); i++) {
                            if (laser.laserUpX >= enemies.get(i).x
                                    && laser.laserUpX <= (enemies.get(i).x
                                    + enemies.get(i).playerWidth)
                                    && enemies.get(i).y <= sprite.y){
                                getWoundedEnemy(enemies.get(i));
                                isShot = true;
                                canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                                break;
                            }
                        }
                    } else if (isLaserShotDown) {
                        canvas.drawBitmap(laser.getLaserDown(), laser.laserDownX, laser.laserDownY, null);
                        for (int i = 0; i < enemies.size(); i++) {
                            if (laser.laserDownX >= enemies.get(i).x
                                    && laser.laserDownX <= enemies.get(i).x
                                    + enemies.get(i).playerWidth
                                    && enemies.get(i).y >= sprite.y){
                                getWoundedEnemy(enemies.get(i));
                                isShot = true;
                                canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                                break;
                            }
                        }
                    } else if (isLaserShotLeft) {
                        canvas.drawBitmap(laser.getLaserLeft(), laser.laserLeftX, laser.laserLeftY, null);
                        //Detect collision between laser bullet and enemies
                        for (int i = 0; i < enemies.size(); i++) {
                            if (laser.laserLeftY >= enemies.get(i).y
                                    && laser.laserLeftY <= enemies.get(i).y
                                    + enemies.get(i).playerHeight
                                    && enemies.get(i).x <= sprite.x){
                                getWoundedEnemy(enemies.get(i));
                                isShot = true;
                                canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                                break;
                            }
                        }
                    } else if (isLaserShotRight) {
                        canvas.drawBitmap(laser.getLaserRight(), laser.laserRightX, laser.laserRightY, null);
                        for (int i = 0; i < enemies.size(); i++) {
                            if (laser.laserRightY >= enemies.get(i).y
                                    && laser.laserRightY <= enemies.get(i).y
                                    + enemies.get(i).playerHeight
                                    && direction.equals("right")
                                    && enemies.get(i).x >= sprite.x){
                                getWoundedEnemy(enemies.get(i));
                                isShot = true;
                                canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                                break;
                            }
                        }
                    }
                }
            }
            else if (button.equals(GAME_OPTION_2)) {
                //detect collision between food and Enemies;
                for (k = 0; k < enemies.size(); k++) {
                    if ((enemies.get(k).x >= food.x && enemies.get(k).x <= food.x + food.width) &&
                            (enemies.get(k).y >= food.y && enemies.get(k).y <= food.y + food.height)) {
                        food.update();
                        AIScore += 10;
                        soundPlayer.playFoodSound();
                    } else if ((food.x >= enemies.get(k).x && food.x <= enemies.get(k).x + enemies.get(k).playerWidth) &&
                            (food.y >= enemies.get(k).y && food.y <= enemies.get(k).y + enemies.get(k).playerHeight)) {
                        food.update();
                        AIScore += 10;
                        soundPlayer.playFoodSound();
                    }
                }
                //Add new Players (AI Users) to help the User when the User gets overwhelmed
                if (enemies.size() >= 3 && enemies.size() % 3 == 0) {
                    if (!help) {
                        //add a new ally when they are multiples of 2 enemies
                        allies.add(new Sprite(this, getResources(), (allyID += 1)));
                        help = true;
                    }
                } else {
                    help = false;
                }
                if (allies.size() > 0) {
                    cursor = allies.get(cursorValue);
                }

                //update direction for user-controlled ally;
                if (!isPausePressed()){
                    drawAndUpdateAllyFoodFest();
                    bodyContact();
                }
            }
        }
    }

    private void displayAndUpdateEnemyHealth() {
        for (int i = 0; i < allies.size(); i++) {
            //draw health bar
            canvas.drawRect(allies.get(i).getX() + 5, allies.get(i).getY() - 40, (allies.get(i).getX() + allies.get(i).width)
                    , allies.get(i).getY() - 10, allies.get(i).getSilver());


            // Determine whether enemy is alive or dead
            enemyLife = (int) (allies.get(i).getX() + 5);

            if ((enemyLife + allies.get(i).enemyDeath) > (enemyLife)) {
                canvas.drawRect(enemyLife + allies.get(i).enemyDeath, allies.get(i).getY() - 40, (allies.get(i).getX() + allies.get(i).width)
                        , allies.get(i).getY() - 10, allies.get(i).getRed());
            } else {
                //remove any enemy that is dead from the array list
                allies.remove(allies.get(i));
                soundPlayer.playEnemyHitSound();
            }
        }
    }

    private void detectGameCompletion() {
        if ((life - death) >= LENGTH_OF_HEALTH_BAR) {
            if (gameComplete) {
                //detect game completion
                savedLevel = 0;
                endGame = true;
                check = false;
                setLevelComplete(true);
                end(CONTEXT);
            }
            else if (AIScore >= objective){
                endGame = true;
                check = false;
                end(CONTEXT);
            }
            canvas.drawRect(life - death, 20, LENGTH_OF_HEALTH_BAR, 80, red);
        } else if ((life - death) < LENGTH_OF_HEALTH_BAR) {
            if (!gameComplete) {
                //game level complete
                setLevelComplete(true);
                endGame = true;
                check = false;
                gameComplete = false;
                end(CONTEXT);
            }
        }
    }

    private void drawAIHUD() {
        canvas.drawText(AI_SCORE_HUD + (AIScore), fontMargin, 70, paint);
        //Draw the number of bullets the user has to the screen
        canvas.drawBitmap(laser.getBulletBitmap(),screenX - 200, 25, null);
        canvas.drawText(""+numberOfBullets, screenX - 100, 70, paint);
        //change the tone of green colour as player loses life
        green.setColor(Color.argb(200, 230, 30, 60));
        red.setColor(Color.argb(200, 30 + (death / 15), 230, 0));
        canvas.drawRect( (screenX - 350), 20, LENGTH_OF_HEALTH_BAR, 80, green);
    }

    private void updateAndDisplayUserMovement() {
        sprite.update(direction);
        switch (direction) {
            case "up":
                canvas.drawBitmap(sprite.getUp(), sprite.x, sprite.y, null);
                break;
            case "down":
                canvas.drawBitmap(sprite.getDown(), sprite.x, sprite.y, null);
                break;
            case "right":
                canvas.drawBitmap(sprite.getRight(), sprite.x, sprite.y, null);
                break;
            case "left":
                canvas.drawBitmap(sprite.getLeft(), sprite.x, sprite.y, null);
                break;
            default:
                canvas.drawBitmap(sprite.getUp(), sprite.src(), sprite.scaleFactor(), null);
                break;
        }
    }

    private void displayLevelsDialogBox() {
        if (!gameComplete) {
            if (isDisplayLevels()) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("DISPLAY LEVELS (A)");
                level.display();
                testDialog = true;
                setDisplayLevels(false);
            }
            if (((levelTimer - levelTimerEnd) <= 0)
                    && (testDialog)) {
                level.dismiss();
                System.out.println("DISMISS LEVELS (B)");
                testDialog = false;
            }
            if (testDialog) {
                levelTimerEnd = System.currentTimeMillis() / 1000;
            }
        }
    }

    private void detectLaserCollisionWithAntagonists() {
        if (numberOfBullets > 0) {
            displayBulletsOnProtagonistShip();

            //control the number of bullets
            if (isLaserShotUp) {
                canvas.drawBitmap(laser.getLaserUp(), laser.laserUpX, laser.laserUpY, null);
                for (int i = 0; i < allies.size(); i++) {
                    if (laser.laserUpX >= allies.get(i).x
                            && laser.laserUpX <= (allies.get(i).x
                            + allies.get(i).width)
                            && allies.get(i).y <= enemy.y){
                        getWoundedEnemy(allies.get(i));
                        isShot = true;
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        break;
                    }
                }
            }
            else if (isLaserShotDown) {
                canvas.drawBitmap(laser.getLaserDown(), laser.laserDownX, laser.laserDownY, null);
                for (int i = 0; i < allies.size(); i++) {
                    if (laser.laserDownX >= allies.get(i).x
                            && laser.laserDownX <= allies.get(i).x
                            + allies.get(i).width
                            && allies.get(i).y >= enemy.y){
                        getWoundedEnemy(allies.get(i));
                        isShot = true;
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        break;
                    }
                }
            } else if (isLaserShotLeft) {
                canvas.drawBitmap(laser.getLaserLeft(), laser.laserLeftX, laser.laserLeftY, null);
                //Detect collision between laser bullet and enemies
                for (int i = 0; i < allies.size(); i++) {
                    if (laser.laserLeftY >= allies.get(i).y
                            && laser.laserLeftY <= allies.get(i).y
                            + allies.get(i).height
                            && allies.get(i).x <= enemy.x){
                        getWoundedEnemy(allies.get(i));
                        isShot = true;
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        break;
                    }
                }
            } else if (isLaserShotRight) {
                canvas.drawBitmap(laser.getLaserRight(), laser.laserRightX, laser.laserRightY, null);
                for (int i = 0; i < allies.size(); i++) {
                    if (laser.laserRightY >= allies.get(i).y
                            && laser.laserRightY <= allies.get(i).y
                            + allies.get(i).height
                            && direction.equals("right")
                            && allies.get(i).x >= enemy.x){
                        getWoundedEnemy(allies.get(i));
                        isShot = true;
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        break;
                    }
                }
            }
        }
    }

    private void displayProtagonistDirections() {
        switch (enemy.getDirection()) {
            case "up":
                canvas.drawBitmap(enemy.getUp(), enemy.x, enemy.y, null);
                break;
            case "down":
                canvas.drawBitmap(enemy.getDown(), enemy.x, enemy.y, null);
                break;
            case "right":
                canvas.drawBitmap(enemy.getRight(), enemy.x, enemy.y, null);
                break;
            case "left":
                canvas.drawBitmap(enemy.getLeft(), enemy.x, enemy.y, null);
                break;
            default:
                canvas.drawBitmap(enemy.getUp(), enemy.src(), enemy.scaleFactor(), null);
                break;
        }
    }

    private void displayCursorBitmap() {
        if (navigation.isCursorBitmapTap()){
            canvas.drawBitmap(navigation.getCursorBitmapTap(), navigation.cursorX, navigation.cursorY, null);
            navigation.setCursorBitmapTap(false);
        }
        else {
            if ( button.equals(GAME_OPTION_2)){
                if (allies.size() > 1) {
                    canvas.drawBitmap(navigation.cursor(), navigation.cursorX, navigation.cursorY, null);
                }
            }
            else {
                canvas.drawBitmap(navigation.cursor(), navigation.cursorX, navigation.cursorY, null);
            }
        }
    }

    private void displayMenuBitmap() {
        if (navigation.isMenuTap()){
            canvas.drawBitmap(navigation.getMenuTap(), navigation.menuX, navigation.menuY, null);
            navigation.setMenuTap(false);
        }
        else {
            canvas.drawBitmap(navigation.menu(), navigation.menuX, navigation.menuY, null);
        }
    }

    private void drawGameBackground() {
        if (button.equals(GAME_OPTION_1)
                || button.equals(GAME_OPTION_3)) {
            canvas.drawBitmap(background.getAlt_one(), null, background.rect, null);
        }
        else {
            canvas.drawBitmap(background.getAlt_two(), null, background.rect, null);
        }
    }

    private void drawAndUpdateAllyFoodFest() {
        for (int i = 0; i < allies.size(); i++) {
            if (i == cursorValue) {
                switch (direction) {
                    case "down":
                        canvas.drawBitmap(allies.get(i).getDown(), allies.get(i).x, allies.get(i).y, paint);
                        break;
                    case "right":
                        canvas.drawBitmap(allies.get(i).getRight(), allies.get(i).x, allies.get(i).y, paint);
                        break;
                    case "left":
                        canvas.drawBitmap(allies.get(i).getLeft(), allies.get(i).x, allies.get(i).y, paint);
                        break;
                    default:
                        canvas.drawBitmap(allies.get(i).getUp(), allies.get(i).x, allies.get(i).y, paint);
                        break;
                }
            } else {
                switch (drawBitmapDirection(allies.get(i))) {
                    case "down":
                        canvas.drawBitmap(allies.get(i).getDown(), allies.get(i).x, allies.get(i).y, null);
                        break;
                    case "right":
                        canvas.drawBitmap(allies.get(i).getRight(), allies.get(i).x, allies.get(i).y, null);
                        break;
                    case "left":
                        canvas.drawBitmap(allies.get(i).getLeft(), allies.get(i).x, allies.get(i).y, null);
                        break;
                    default:
                        canvas.drawBitmap(allies.get(i).getUp(), allies.get(i).x, allies.get(i).y, null);
                        break;
                }
            }
            //pause motion for a brief period at the beginning of the game
            if (testDialog) {
                allies.get(i).stop();
            } else {
                if (i != cursorValue) {
                    allies.get(i).updateFoodFest();
                } else {
                    synchronized (allies.get(i)) {
                        allies.get(i).update(direction);
                    }
                }
                detectFoodAcquisition(allies.get(i));
                if (enemies.size() % 3 == 0 && enemies.size()>=3) {
                    increaseTime(i);
                }
            }
        }
    }

    private void displayBulletsOnProtagonistShip() {
        if (enemy.getDirection().equals("up") && !isLaserShotUp) {
            canvas.drawBitmap(laser.getLaserUp(), laser.laserUpX, laser.laserUpY, null);
        } else if (enemy.getDirection().equals("down") && !isLaserShotDown) {
            canvas.drawBitmap(laser.getLaserDown(), laser.laserDownX, laser.laserDownY, null);
        } else if (enemy.getDirection().equals("left") && !isLaserShotLeft) {
            canvas.drawBitmap(laser.getLaserLeft(), laser.laserLeftX, laser.laserLeftY, null);
        } else if (enemy.getDirection().equals("right") && !isLaserShotRight) {
            canvas.drawBitmap(laser.getLaserRight(), laser.laserRightX, laser.laserRightY, null);
        }
    }

    private void detectAntagonistAttack() {
        if (allies.size() > 0) {
            for (int i = 0; i < allies.size(); i++) {
                if ((allies.get(i).x >= enemy.x && allies.get(i).x <= enemy.x + enemy.playerWidth) &&
                        (allies.get(i).y >= enemy.y && allies.get(i).y <= enemy.y + enemy.playerHeight)) {
                    death += 10;
                    //lose sound
                    soundPlayer.playLoseSound();
                    if (allies.get(i).isGreenUFO) {
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    } else if (allies.get(i).isBlueUFO) {
                        canvas.drawBitmap(allies.get(i).getBlueSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    } else {
                        canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    }
                    allies.get(i).enemyDeath /= A_MEASURE_OF_DEATH;
                } else if ((enemy.x >= allies.get(i).x && enemy.x <= allies.get(i).x + allies.get(i).width) &&
                        (enemy.y >= allies.get(i).y && enemy.y <= allies.get(i).y + allies.get(i).height)) {
                    death += 10;
                    //lose sound
                    soundPlayer.playLoseSound();
                    if (allies.get(i).isGreenUFO) {
                        canvas.drawBitmap(allies.get(i).getGreenSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    } else if (allies.get(i).isBlueUFO) {
                        canvas.drawBitmap(allies.get(i).getBlueSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    } else {
                        canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                    }
                    allies.get(i).enemyDeath /= A_MEASURE_OF_DEATH;
                }
            }
        }
    }

    private void detectMoneyAcquisitionByProtagonist() {
        if ((food.x >= enemy.x && food.x <= enemy.x + enemy.playerWidth) &&
                (food.y >= enemy.y && food.y <= enemy.y + enemy.playerHeight)) {
            food.update();
            AIScore += 10;
            soundPlayer.playFoodSound();
        } else if ((enemy.x >= food.x && enemy.x <= food.x + food.width) &&
                (enemy.y >= food.y && enemy.y <= food.y + food.height)) {
            food.update();
            AIScore += 10;
            soundPlayer.playFoodSound();
        }
    }

    private void updateAntagonistMovement() {
        for (int i = 0; i < allies.size(); i++) {
            displayAntagonistsOnScreen(i);
            //pause motion for a brief period at the beginning of the game
            if (testDialog || getPopUpMenu()) {
                allies.get(i).stop();
            } else {
                if (i != cursorValue) {
                    allies.get(i).updateInverted();
                } else {
                    allies.get(i).update(direction);
                    //display cursor indicator
                    canvas.drawBitmap(navigation.getShowCursor(), allies.get(i).getX(), allies.get(i).getY() - 40, null);
                }
            }

            if (popUpMenu) {
                //pause game && display menu
                pauseTimer = System.currentTimeMillis();
                customTask = new CustomTask(CONTEXT, this);
                thread1 = new Thread(customTask);
                thread1.start();
                setPausePressed(true);
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                popUpMenu = false;
            }
            if (isPausePressed()) {
                if (customTask.isResume()) {
                    customTask.dismiss();
                }
            }
        }
    }

    private void displayAntagonistsOnScreen(int i) {
        //draw blue UFO
        if (allies.get(i).getId() == 3 || allies.get(i).getId() == 6
                && (!allies.get(i).isBeigeUFO && !allies.get(i).isGreenUFO
                && !allies.get(i).isPinkUFO && !allies.get(i).isYellowUFO )) {
            canvas.drawBitmap(allies.get(i).getBlueUFO(), allies.get(i).src(), allies.get(i).scaleFactor(), paint);
        }
        //draw beige UFO if the enemy has an id of 4
        else if (allies.get(i).getId() == 4 || allies.get(i).getId() == 8
                && (!allies.get(i).isGreenUFO && !allies.get(i).isBlueUFO)
                && !allies.get(i).isPinkUFO && !allies.get(i).isYellowUFO) {
            canvas.drawBitmap(allies.get(i).getBeigeUFO(), allies.get(i).src(), allies.get(i).scaleFactor(), paint);
        }
        //draw pink UFO
        else if (allies.get(i).getId() == 5 || allies.get(i).getId() == 10
                && (!allies.get(i).isGreenUFO && !allies.get(i).isBlueUFO
                && !allies.get(i).isBeigeUFO  && !allies.get(i).isYellowUFO  )) {
            canvas.drawBitmap(allies.get(i).getPinkUFO(), allies.get(i).src(), allies.get(i).scaleFactor(), paint);
        }
        //draw yellow UFO
        else if (allies.get(i).getId() == 6 || allies.get(i).getId() == 12
                && (!allies.get(i).isGreenUFO && !allies.get(i).isBlueUFO
                && !allies.get(i).isBeigeUFO && !allies.get(i).isPinkUFO)) {
            canvas.drawBitmap(allies.get(i).getYellowUFO(), allies.get(i).src(), allies.get(i).scaleFactor(), paint);
        }
        else {
            canvas.drawBitmap(allies.get(i).getGreenUFO(), allies.get(i).src(), allies.get(i).scaleFactor(), paint);
        }
    }

    private void updateAIMovement() {
        if (testDialog || popUpMenu) {
            enemy.stop();
        } else {
            enemy.updateInverted(allies);
        }
    }


    private void detectCollisionBetweenEnemiesAndUser() {
        for (int i = 0; i < enemies.size(); i++) {
            if ((enemies.get(i).x >= sprite.x && enemies.get(i).x <= sprite.x + sprite.width) &&
                    (enemies.get(i).y >= sprite.y && enemies.get(i).y <= sprite.y + sprite.height)) {
                death += 10;
                enemies.get(i).enemyDeath /= A_MEASURE_OF_DEATH;
                soundPlayer.playLoseSound();
                if (enemies.get(i).isGreenUFO) {
                    canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                } else if (enemies.get(i).isBlueUFO) {
                    canvas.drawBitmap(enemies.get(i).getBlueSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                } else {
                    canvas.drawBitmap(enemies.get(i).getBeigeSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                }

                //draw enemy UFOs
                if (enemies.get(i).getId() == 3 || enemies.get(i).getId() == 6
                        && (!enemies.get(i).isBeigeUFO && !enemies.get(i).isGreenUFO)) {
                    canvas.drawBitmap(enemies.get(i).getBlueUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                }
                else if (enemies.get(i).getId() == 4 || enemies.get(i).getId() == 4
                        && (!enemies.get(i).isGreenUFO && !enemies.get(i).isBlueUFO)) {
                    canvas.drawBitmap(enemies.get(i).getBeigeUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                } else {
                    canvas.drawBitmap(enemies.get(i).getGreenUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                }
            } else if ((sprite.x >= enemies.get(i).x && sprite.x <= enemies.get(i).x + enemies.get(i).playerWidth) &&
                    (sprite.y >= enemies.get(i).y && sprite.y <= enemies.get(i).y + enemies.get(i).playerHeight)) {
                death += 10;
                enemies.get(i).enemyDeath /= A_MEASURE_OF_DEATH;
                //lose sound

                soundPlayer.playLoseSound();
                if (enemies.get(i).isGreenUFO) {
                    canvas.drawBitmap(enemies.get(i).getGreenSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                } else if (enemies.get(i).isBlueUFO) {
                    canvas.drawBitmap(enemies.get(i).getBlueSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                } else {
                    canvas.drawBitmap(enemies.get(i).getBeigeSpark(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
                }
            }
        }
    }

    private void drawEnemiesOnScreenAndUpdate() {
        //draw different types of enemies to the screen
        for (int i = 0; i < enemies.size(); i++) {
            //draw blue UFO
            if (enemies.get(i).getId() == 3 || enemies.get(i).getId() == 6
                    && (!enemies.get(i).isBeigeUFO && !enemies.get(i).isGreenUFO
                    && !enemies.get(i).isPinkUFO && !enemies.get(i).isYellowUFO )) {
                canvas.drawBitmap(enemies.get(i).getBlueUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
            }
            //draw beige UFO
            else if (enemies.get(i).getId() == 4 || enemies.get(i).getId() == 8
                    && (!enemies.get(i).isGreenUFO && !enemies.get(i).isBlueUFO
                    && !enemies.get(i).isPinkUFO && !enemies.get(i).isYellowUFO)) {
                canvas.drawBitmap(enemies.get(i).getBeigeUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
            }
            //draw pink UFO
            else if (enemies.get(i).getId() == 6 || enemies.get(i).getId() == 12
                    && (!enemies.get(i).isGreenUFO && !enemies.get(i).isBlueUFO
                    && !enemies.get(i).isBeigeUFO  && !enemies.get(i).isYellowUFO  )) {
                canvas.drawBitmap(enemies.get(i).getPinkUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
            }
            //draw yellow UFO
            else if (enemies.get(i).getId() == 5 || enemies.get(i).getId() == 10
                    && (!enemies.get(i).isGreenUFO && !enemies.get(i).isBlueUFO
                    && !enemies.get(i).isBeigeUFO && !enemies.get(i).isPinkUFO)) {
                canvas.drawBitmap(enemies.get(i).getYellowUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
            }

            else {
                canvas.drawBitmap(enemies.get(i).getGreenUFO(), enemies.get(i).src(), enemies.get(i).scaleFactor(), null);
            }
            //pause motion for a brief period at the beginning of the game
            if (testDialog) {
                enemies.get(i).stop();
            } else {
                if (button.equals(GAME_OPTION_1)) {
                    enemies.get(i).update(i);
                }
                else if (button.equals(GAME_OPTION_2)){
                    enemies.get(i).updateFoodFest();
                }
            }
            /*handle pop-up menu*/
            if (popUpMenu){
                //pause game && display menu
                pauseTimer = System.currentTimeMillis();
                customTask = new CustomTask(CONTEXT, this);
                thread1 = new Thread(customTask);
                thread1.start();
                setPausePressed(true);
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                popUpMenu = false;
            }
            if (isPausePressed()) {
                if (button.equals(GAME_OPTION_1)) {
                    enemies.get(i).stop();
                    sprite.stop();
                }
                else if (button.equals(GAME_OPTION_2)) {
                    enemies.get(i).stop();
                }
                if (customTask.isResume()){
                    customTask.dismiss();
                }
            }
        }
    }

    private void spawnNewEnemy() {
        timeElapsed = (System.nanoTime());

        if (isThirtySeconds()) {
            if (button.equals(GAME_OPTION_1)
                    || button.equals(GAME_OPTION_2) ) {
                newEnemy = new Enemy(this, sprite, food, getResources(), (enemyID += 1));
                enemies.add(newEnemy);
            }
            else {
                allies.add(new Sprite(this, getResources(), (allyID += 1)));
            }
                try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isThirtySeconds() {
        return ((timeElapsed - startTime) / 1000000000) % 30 == 0 && (timeElapsed - startTime) / 1000000000 >= 30;
    }

    private void addExtraBulletsForAlly() {
        if (enemies.size() % 4 == 0 && enemies.size()  >= 4) {
            canvas.drawBitmap(navigation.getNewLaserBullets(), navigation.bulletX, navigation.bulletY, null);
            if ((navigation.bulletX >= sprite.x && navigation.bulletX <= sprite.x + sprite.width) &&
                    (navigation.bulletY >= sprite.y && navigation.bulletY <= sprite.y + sprite.height)) {
                numberOfBullets += ADD_BULLETS;
                soundPlayer.playHealthSound();
                navigation.bulletY = navigation.bulletX = new Random().nextInt((screenX - 150) + 1) + 100;
                laserDetected = true;
            } else if ((sprite.x >= navigation.bulletX && sprite.x <= navigation.bulletX + Navigation.width) &&
                    (sprite.y >= navigation.bulletY && sprite.y <= navigation.bulletY + Navigation.height)) {
                numberOfBullets += ADD_BULLETS;
                soundPlayer.playHealthSound();
                navigation.bulletY = navigation.bulletX = new Random().nextInt((screenX - 150) + 1) + 100;
                laserDetected = true;
            }
            laserDetected = false;
        }
    }

    private void addBonusHealthAtSpecificTimes() {
        if (button.equals(GAME_OPTION_1)) {
            if ((((enemies.size() % 3 == 0 && enemies.size() >= 3)))) {
                canvas.drawBitmap(navigation.health(), navigation.healthX, navigation.healthY, null);
                if ((navigation.healthX >= sprite.x && navigation.healthX <= sprite.x + sprite.width) &&
                        (navigation.healthY >= sprite.y && navigation.healthY <= sprite.y + sprite.height)) {

                    increaseLife();
                } else if ((sprite.x >= navigation.healthX && sprite.x <= navigation.healthX + Navigation.width) &&
                        (sprite.y >= navigation.healthY && sprite.y <= navigation.healthY + Navigation.height)) {
                    increaseLife();
                }
                healthDetected = true;
            }
        }
        else {
            if ((((allies.size() % 3 == 0 && allies.size() >= 3)))) {
                if ((navigation.healthX >= enemy.x && navigation.healthX <= enemy.x + enemy.playerWidth) &&
                        (navigation.healthY >= enemy.y && navigation.healthY <= enemy.y + enemy.playerHeight)) {
                    increaseLife();
                } else if ((enemy.x >= navigation.healthX && enemy.x <= navigation.healthX + Navigation.width) &&
                        (enemy.y >= navigation.healthY && enemy.y <= navigation.healthY + Navigation.height)) {
                    increaseLife();
                }
                healthDetected = true;
            }
        }
        healthDetected = false;
    }

    private void increaseLife() {
        if ((life - death) + 100 <= LENGTH_OF_HEALTH_BAR) {
            death = death - 100;
        } else if ((life - death) + 30 >= LENGTH_OF_HEALTH_BAR) {
            death = 0;
        }
        soundPlayer.playHealthSound();
        navigation.healthX = navigation.healthY = new Random().nextInt((screenX - 150) + 1) + 100;
    }

    private void displayStarsToScreen() {
        canvas.drawBitmap(background.getBigStar(), background.getBigStarX(), background.getBigStarY(), null);
        if ((button.equals(GAME_OPTION_1) ||
                button.equals(GAME_OPTION_2))
            &&
                        (enemies.size() % 2 == 0 && enemies.size() >= 2)) {
            canvas.drawBitmap(background.getSmallStarOne(), background.getSmallStarX(), background.getSmallStarOneY(), null);
            canvas.drawBitmap(background.getSmallStarTwo(), background.getSmallStarX(), background.getSmallStarTwoY(), null);
            canvas.drawBitmap(background.getSmallStarThree(), background.getSmallStarX(), background.getSmallStarThreeY(), null);
            canvas.drawBitmap(background.getSmallStarFour(), background.getSmallStarX(), background.getSmallStarFourY(), null);
            canvas.drawBitmap(background.getSmallStarFive(), background.getSmallStarX(), background.getSmallStarFiveY(), null);
        }
        else {
            if (allies.size() % 2 == 0 && allies.size() >= 2) {
                canvas.drawBitmap(background.getSmallStarOne(), background.getSmallStarX(), background.getSmallStarOneY(), null);
                canvas.drawBitmap(background.getSmallStarTwo(), background.getSmallStarX(), background.getSmallStarTwoY(), null);
                canvas.drawBitmap(background.getSmallStarThree(), background.getSmallStarX(), background.getSmallStarThreeY(), null);
                canvas.drawBitmap(background.getSmallStarFour(), background.getSmallStarX(), background.getSmallStarFourY(), null);
                canvas.drawBitmap(background.getSmallStarFive(), background.getSmallStarX(), background.getSmallStarFiveY(), null);
            }
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void detectFoodAcquisition(Sprite sprite) {
        if ((food.x >= sprite.x && food.x <= sprite.x + sprite.width) &&
                (food.y >= sprite.y && food.y <= sprite.y + sprite.height)) {
            food.update();
            score += 10;
            soundPlayer.playFoodSound();
        } else if ((sprite.x >= food.x && sprite.x <= food.x + food.width) &&
                (sprite.y >= food.y && sprite.y <= food.y + food.height)) {
            food.update();
            score += 10;
            soundPlayer.playFoodSound();
        }
    }

    private void increaseTime(int i) {
        canvas.drawBitmap(navigation.health(), navigation.healthX, navigation.healthY, null);
        if ((navigation.healthX >= allies.get(i).x && navigation.healthX <= allies.get(i).x + allies.get(i).width) &&
                (navigation.healthY >= allies.get(i).y && navigation.healthY <= allies.get(i).y + allies.get(i).height)) {
            TIMER+=ADD_TIME;
            soundPlayer.playHealthSound();
            navigation.healthX = navigation.healthY = new Random().nextInt((screenX - 150) + 1) + 100;
        } else if ((allies.get(i).x >= navigation.healthX && allies.get(i).x <= navigation.healthX + Navigation.width) &&
                (allies.get(i).y >= navigation.healthY && allies.get(i).y <= navigation.healthY + Navigation.height)) {
            TIMER+=ADD_TIME;
            soundPlayer.playHealthSound();
            navigation.healthX = navigation.healthY = new Random().nextInt((screenX - 150) + 1) + 100;
        }

    }

    private void drawEnemyHealthBar() {
        for (int i = 0; i < enemies.size(); i++) {
            //draw health bar
            canvas.drawRect(enemies.get(i).getX() + 5, enemies.get(i).getY() - 40, (enemies.get(i).getX() + enemies.get(i).playerWidth)
                    , enemies.get(i).getY() - 10, enemies.get(i).getSilver());
            // Determine whether enemy is alive or dead
            enemyLife = (enemies.get(i).getX() + 5);
            if ((enemyLife + enemies.get(i).enemyDeath) > (enemyLife)) {
                canvas.drawRect(enemyLife + enemies.get(i).enemyDeath, enemies.get(i).getY() - 40, (enemies.get(i).getX() + enemies.get(i).playerWidth)
                        , enemies.get(i).getY() - 10, enemies.get(i).getRed());
            } else {
                System.out.println("REMOVED");
                enemies.remove(enemies.get(i));
                soundPlayer.playEnemyHitSound();
            }
        }
    }

    private void displayNavigationButtons() {
        //Draw navigation buttons to the screen
        if (navigation.isTapUp()) {
            canvas.drawBitmap(navigation.getTapUp(), navigation.upX, navigation.upY, null);
            navigation.setTapUp(false);
        }
        else {
            canvas.drawBitmap(navigation.getUp(), navigation.upX, navigation.upY, null);
        }
        if (navigation.isTapDown()){
            canvas.drawBitmap(navigation.getTapDown(), navigation.downX, navigation.downY, null);
            navigation.setTapDown(false);
        }
        else {
            canvas.drawBitmap(navigation.getDown(), navigation.downX, navigation.downY, null);
        }
        if (navigation.isTapRight()){
            canvas.drawBitmap(navigation.getTapRight(), navigation.rightX, navigation.rightY, null);
            navigation.setTapRight(false);
        }
        else {
            canvas.drawBitmap(navigation.getRight(), navigation.rightX, navigation.rightY, null);
        }
        if (navigation.isTapLeft()){
            canvas.drawBitmap(navigation.getTapLeft(), navigation.leftX, navigation.leftY, null);
            navigation.setTapLeft(false);
        }
        else {
            canvas.drawBitmap(navigation.getLeft(), navigation.leftX, navigation.leftY, null);
        }
    }

    private boolean isTimeElapsedMoreThanOneMinute() {
        return CURRENT_TIME % 60 >= 10;
    }

    private boolean isTimeElapsed() {
        return CURRENT_TIME <= TIMEOUT;
    }

    private void getWoundedEnemy(Sprite sprite) {
        if (numberOfBullets > 0) {
            sprite.enemyDeath /= 2;
            isShot = false;
         //only allow one bullet to affect one enemy
            isLaserShotUp = false;
            isLaserShotDown = false;
            isLaserShotLeft = false;
            isLaserShotRight = false;
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        touchX = event.getX();
        touchY = event.getY();
        //action event
        int action = event.getAction();
        /*https://stackoverflow.com/questions/4804798/doubletap-in-android*/
        long DOUBLE_CLICK_TIME = 800;
        if (button.equals(GAME_OPTION_2) | button.equals(GAME_OPTION_1)
                | button.equals(GAME_OPTION_3)) {
            if (action == MotionEvent.ACTION_DOWN) {//DIRECTIONS
                if ((touchY >= navigation.upY && touchY <= navigation.upY + Navigation.height)
                        && (touchX >= navigation.upX && touchX <= navigation.upX + Navigation.width)) {
                    direction = "down";
                    navigation.setTapUp(true);
                    navigation.setTapRight(false);
                    navigation.setTapLeft(false);
                    navigation.setTapDown(false);
                } else if ((touchY >= navigation.downY && touchY <= navigation.downY + Navigation.height)
                        && (touchX >= navigation.downX && touchX <= navigation.downX + Navigation.width)) {
                    direction = "up";
                    navigation.setTapDown(true);
                    navigation.setTapRight(false);
                    navigation.setTapLeft(false);
                    navigation.setTapUp(false);
                } else if ((touchY >= navigation.leftY && touchY <= navigation.leftY + Navigation.height)
                        && (touchX >= navigation.leftX && touchX <= navigation.leftX + Navigation.width)) {
                    direction = "left";
                    navigation.setTapLeft(true);

                    navigation.setTapDown(false);
                    navigation.setTapRight(false);
                    navigation.setTapUp(false);
                } else if ((touchY >= navigation.rightY && touchY <= navigation.rightY + Navigation.height)
                        && (touchX >= navigation.rightX && touchX <= navigation.rightX + Navigation.width)) {
                    direction = "right";
                    navigation.setTapRight(true);

                    navigation.setTapDown(false);
                    navigation.setTapLeft(false);
                    navigation.setTapUp(false);
                }
                if (button.equals(GAME_OPTION_1)) {
                    if ((touchY >= navigation.cursorY && touchY <= navigation.cursorY + Navigation.height)
                            && (touchX >= navigation.cursorX && touchX <= navigation.cursorX + Navigation.width)
                    ) {
                        navigation.setCursorBitmapTap(true);
                        numberOfBullets--;
                        soundPlayer.playLaserSound();
                        if (numberOfBullets > 0) {
                            switch (direction) {
                                case "up":
                                case "halt":
                                    isLaserShotUp = true;
                                    isLaserShotDown = false;
                                    isLaserShotLeft = false;
                                    isLaserShotRight = false;
                                    break;
                                case "down":
                                    isLaserShotDown = true;
                                    isLaserShotUp = false;
                                    isLaserShotLeft = false;
                                    isLaserShotRight = false;
                                    break;
                                case "left":
                                    isLaserShotLeft = true;
                                    isLaserShotUp = false;
                                    isLaserShotDown = false;
                                    isLaserShotRight = false;
                                    break;
                                case "right":
                                    isLaserShotRight = true;
                                    isLaserShotUp = false;
                                    isLaserShotDown = false;
                                    isLaserShotLeft = false;
                                    break;
                            }
                        }
                    }
                }
                if ((touchY >= navigation.menuY && touchY <= navigation.menuY + Navigation.height)
                        && (touchX >= navigation.menuX && touchX <= navigation.menuX + Navigation.width)) {
                    popUpMenu = true;
                    navigation.setMenuTap(true);
                }
                if (button.equals(GAME_OPTION_1)) {
                    /*Idea: When the user touches the game object itself twice within 1 second,
                     *let the object halt*/
                    if (firstTouch && System.currentTimeMillis() - firstTouchTime <= DOUBLE_CLICK_TIME
                            && (touchY >= sprite.y && touchY <= sprite.y + sprite.height)
                            && touchX >= sprite.x && touchX <= sprite.x + sprite.width) {
                        direction = "halt";
                        firstTouch = false;
                    }
                    firstTouch = true;
                    firstTouchTime = System.currentTimeMillis();
                } else {
                    for (int i = 0; i < allies.size(); i++) {
                        /*Idea: When the user touches the game object itself twice within 1 second,
                         *let the object halt*/
                        if (firstTouch && System.currentTimeMillis() - firstTouchTime <= DOUBLE_CLICK_TIME
                                && (touchY >= allies.get(i).y && touchY <= allies.get(i).y + allies.get(i).height)
                                && touchX >= allies.get(i).x && touchX <= allies.get(i).x + allies.get(i).width) {
                            direction = "halt";
                            firstTouch = false;
                        }
                        firstTouch = true;
                        firstTouchTime = System.currentTimeMillis();
                    }
                    /*Change cursor when user touches keystroke on the RHS of the screen;
                     * This only applies to Part II & III */
                    if ((touchY >= navigation.cursorY && touchY <= navigation.cursorY + Navigation.height)
                            && (touchX >= navigation.cursorX && touchX <= navigation.cursorX + Navigation.width)
                            && allies.size() > 0) {
                        navigation.setCursorBitmapTap(true);
                        for (int i = 0; i < allies.size(); i++) {
                            if (!allies.contains(cursor)
                                    || cursor == allies.get(i)) {
                                //cursor placeholder
                                int placeHolder = new Random().nextInt(allies.size());
                                if (allies.size() == 1) {
                                    cursorValue = placeHolder;
                                } else if (allies.size() > 1) {
                                    while (placeHolder == cursorValue) {
                                        placeHolder = new Random().nextInt(allies.size());
                                    }
                                    cursorValue = placeHolder;
                                }
                                direction = "halt";
                                cursor.velX = 0;
                                cursor.velY = 0;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    //Method to pause game when user exits temporarily
    public void pause() {
        check = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to pause game after user exits temporarily
    public void resume() {
        check = true;
        if (!endGame) {
            thread = new Thread(this);
            thread.start();
            startGame = true;
        }
    }

    //Method to end current level/round
    public void end(Context CONTEXT) {
        check = false;
        endGame = true;
        try {
            if (button.equals(GAME_OPTION_1)) {
                if ((score >= objective) && !gameComplete) {
                    PLAYER_DB.write();
//                    currentStage();
                }
            }
            //Write data to food fest database
            else if ((button.equals(GAME_OPTION_2))) {
                if ((AIScore < score) && !gameComplete) {
                    PLAYER_DB.write();
//                    currentStage();
                }
            } else if ((button.equals(GAME_OPTION_3))) {
                if ((AIScore < objective) && !gameComplete) {
                    PLAYER_DB.write();
//                    currentStage();
                }
            }
            if (savedLevel >= TOTAL_NUMBER_OF_LEVELS
                    || savedRound >= TOTAL_NUMBER_OF_LEVELS) {
                //SET Game complete to true
                gameComplete = true;
            }
            thread1 = new Thread(new CustomTask(CONTEXT, this));
            thread1.start();
            thread1.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //method to get current level/round
    public void currentStage() {
        currentStageCalled = true;
        savedRound = 0;
        savedLevel = 0;
        //get the current round/level that was saved
        Cursor cursor = PLAYER_DB.read();
        if (cursor.moveToFirst()) {
            do {
                if (button.equals(GAME_OPTION_1) || button.equals(GAME_OPTION_3)) {
                    savedLevel = Integer.parseInt(cursor.getString(1));
                } else {
                    savedRound = Integer.parseInt(cursor.getString(1));
                }
            }
            while (cursor.moveToNext());
        }
    }
    //This method detects collision between the allies and the enemies
    private void bodyContact() {
        for (k = 0; k < enemies.size(); k++) {
            // Body contact: Collision detection with enemy
            for (int i = 0; i < allies.size(); i++) {
                if ((enemies.get(k).x >= allies.get(i).x && enemies.get(k).x <= allies.get(i).x + allies.get(i).width) &&
                        (enemies.get(k).y >= allies.get(i).y && enemies.get(k).y <= allies.get(i).y + allies.get(i).height)) {
                    //enemy performs collision
                    if (enemies.get(k).getDirection().equals("right")
                            && !allies.get(i).getDirection().equals("left")) {
                        allies.get(i).setBodyContact(true);
                        allies.get(i).reaction(getEnemies());
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                        soundPlayer.playEnemyHitSound();
                    } else if (enemies.get(k).getDirection().equals("left")
                            && !allies.get(i).getDirection().equals("right")) {
                        allies.get(i).setBodyContact(true);
                        allies.get(i).reaction(getEnemies());
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else if (enemies.get(k).getDirection().equals("up")
                            && !allies.get(i).getDirection().equals("down")) {
                        allies.get(i).setBodyContact(true);
                        allies.get(i).reaction(getEnemies());
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else if (enemies.get(k).getDirection().equals("down")
                            && !allies.get(i).getDirection().equals("up")) {
                        allies.get(i).reaction(getEnemies());
                        allies.get(i).setBodyContact(true);
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else {
                        //Head-to-head collision with Player
                        //Enemy wins since they are the actor
                        if (enemies.get(k).getDirection().equals("right")
                                && allies.get(i).getDirection().equals("left")) {
                            allies.get(i).setBodyContact(true);
                            allies.get(i).reaction(getEnemies());
                            soundPlayer.playLoseSound();
                            canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        } else if (enemies.get(k).getDirection().equals("left")
                                && allies.get(i).getDirection().equals("right")) {
                            allies.get(i).setBodyContact(true);
                            allies.get(i).reaction(getEnemies());
                            soundPlayer.playLoseSound();
                            canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        } else if (enemies.get(k).getDirection().equals("up")
                                && allies.get(i).getDirection().equals("down")) {
                            allies.get(i).setBodyContact(true);
                            allies.get(i).reaction(getEnemies());
                            soundPlayer.playLoseSound();
                            canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        } else if (enemies.get(k).getDirection().equals("down")
                                && allies.get(i).getDirection().equals("up")) {
                            allies.get(i).reaction(getEnemies());
                            allies.get(i).setBodyContact(true);
                            soundPlayer.playLoseSound();
                            canvas.drawBitmap(allies.get(i).getBeigeSpark(), allies.get(i).src(), allies.get(i).scaleFactor(), null);
                        }
                    }
                } else if ((allies.get(i).x >= enemies.get(k).x && allies.get(i).x <= enemies.get(k).x + enemies.get(k).playerWidth)
                        && (allies.get(i).y >= enemies.get(k).y && allies.get(i).y <= enemies.get(k).y + enemies.get(k).playerHeight)) {
                    //user performs collision; user wins since they are the actor
                    if (allies.get(i).getDirection().equals("left")
                            && !enemies.get(k).getDirection().equals("right")) {
                        enemies.get(k).setBodyContact(true);
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else if (allies.get(i).getDirection().equals("right")
                            && !enemies.get(k).getDirection().equals("left")) {
                        enemies.get(k).setBodyContact(true);
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else if (allies.get(i).getDirection().equals("up")
                            && !enemies.get(k).getDirection().equals("down")) {
                        enemies.get(k).setBodyContact(true);
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else if (allies.get(i).getDirection().equals("down")
                            && !enemies.get(k).getDirection().equals("up")) {
                        enemies.get(k).setBodyContact(true);
                        soundPlayer.playEnemyHitSound();
                        canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                    } else {
                        //if there is a head to head collision between Enemy & Player
                        //Player wins since they are the one performing the collision
                        if (allies.get(i).getDirection().equals("left")
                                && enemies.get(k).getDirection().equals("right")) {
                            enemies.get(k).setBodyContact(true);
                            soundPlayer.playEnemyHitSound();
                            canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                        } else if (allies.get(i).getDirection().equals("right")
                                && enemies.get(k).getDirection().equals("left")) {
                            enemies.get(k).setBodyContact(true);
                            soundPlayer.playEnemyHitSound();
                            canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                        } else if (allies.get(i).getDirection().equals("up")
                                && enemies.get(k).getDirection().equals("down")) {
                            enemies.get(k).setBodyContact(true);
                            soundPlayer.playEnemyHitSound();
                            canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                        } else if (allies.get(i).getDirection().equals("down")
                                && enemies.get(k).getDirection().equals("up")) {
                            enemies.get(k).setBodyContact(true);
                            soundPlayer.playEnemyHitSound();
                            canvas.drawBitmap(enemies.get(k).getGreenSpark(), enemies.get(k).src(), enemies.get(k).scaleFactor(), null);
                        }
                    }
                }
            }
        }
    }
    //Method returns the direction of the user-controlled bitmap
    String drawBitmapDirection(Sprite sprite) {
        return sprite.getDirection();
    }
}
