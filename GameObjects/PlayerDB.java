package com.capstone.challengeweek.GameObjects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//This class handles storing and retrieving game data
public class PlayerDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "player.db";
    private static final int DB_VERSION = 1;
    public static final String ID = "id";
    public static final String ROUND = "round";
    public static final String LEVEL = "level";
    public static final String GAME_VARIATION = "game_variation";
    public static final String SCORE = "score";
    private static final String AI_SCORE = "ai_score";
    private static final String NUM_OF_ENEMIES = "number_of_enemies";
    private static final String TABLE_NAME_DEFAULT = "default_table";
    private static final String TABLE_NAME_FOOD_FEST = "food_fest_table";
    private static final String TABLE_NAME_INVERTED_DEFAULT = "inverted_default_table";
    private static final String TIME = "time";
    private static final String NUM_OF_ALLIES = "number_of_allies";
    private String CREATE_TABLE;
    public OurView view;

    private SQLiteDatabase db;
    private ContentValues contentValues;
    private Cursor cursor;
    private int size;

    public PlayerDB(Context context, OurView view) {
        super(context, DB_NAME, null, DB_VERSION);
        this.view = view;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableDefault());
        sqLiteDatabase.execSQL(createTableFoodFest());
        sqLiteDatabase.execSQL(createTableInverted());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop older table if exists
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME_DEFAULT);
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME_FOOD_FEST);
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME_INVERTED_DEFAULT);
        onCreate(sqLiteDatabase);
    }
    //create default game DB table
    private String createTableDefault(){
        CREATE_TABLE = "CREATE TABLE "+TABLE_NAME_DEFAULT + "("+
                ID + " INT,"
                + LEVEL + " INT,"
                + GAME_VARIATION + " TEXT,"
                + SCORE + " INT,"
                + NUM_OF_ENEMIES + " INT" + ")";
        return CREATE_TABLE;
    }
    //create food fest DB table
    private String createTableFoodFest(){
        CREATE_TABLE = "CREATE TABLE "+TABLE_NAME_FOOD_FEST + "("+
                ID + " INT,"
                + ROUND + " INT,"
                + GAME_VARIATION + " TEXT,"
                + SCORE + " INT,"
                + AI_SCORE + " INT,"
                + TIME + " INT"+ ")";
        return CREATE_TABLE;
    }
    private String createTableInverted(){
        CREATE_TABLE = "CREATE TABLE "+TABLE_NAME_INVERTED_DEFAULT + "("+
                ID + " INT,"
                + LEVEL + " INT,"
                + GAME_VARIATION + " TEXT,"
                + AI_SCORE + " INT,"
                + NUM_OF_ALLIES + " INT" + ")";
        return CREATE_TABLE;
    }

    //write to database
    public void write(){
        try {
            db = this.getWritableDatabase();
            contentValues = new ContentValues();
            long rowNumber;
            if (view.button.equals(OurView.GAME_OPTION_1)) {
                contentValues.put(ID, 1000);
                contentValues.put(LEVEL, view.getCurrentLevel());
                contentValues.put(GAME_VARIATION, OurView.GAME_OPTION_1);
                contentValues.put(SCORE, view.getScore());
                contentValues.put(NUM_OF_ENEMIES, view.getEnemies().size());
                rowNumber = db.insert(TABLE_NAME_DEFAULT, null, contentValues);
            }
            //Food fest table
            else if (view.button.equals(OurView.GAME_OPTION_2)) {
                contentValues.put(ID, 1000);
                contentValues.put(ROUND, view.getCurrentLevel());
                contentValues.put(GAME_VARIATION, OurView.GAME_OPTION_2);
                contentValues.put(SCORE, view.getScore());
                contentValues.put(AI_SCORE, view.getAIScore());
                contentValues.put(TIME, (60 + (30 * view.getCurrentLevel())));
                rowNumber = db.insert(TABLE_NAME_FOOD_FEST, null, contentValues);
            }
            //Inverted Default Table
            else if (view.button.equals(OurView.GAME_OPTION_3)) {
                contentValues.put(ID, 1000);
                contentValues.put(LEVEL, view.getCurrentLevel());
                contentValues.put(GAME_VARIATION, OurView.GAME_OPTION_3);
                contentValues.put(AI_SCORE, view.getAIScore());
                contentValues.put(NUM_OF_ALLIES, view.getAllies().size());
                rowNumber = db.insert(TABLE_NAME_INVERTED_DEFAULT, null, contentValues);
            }

        }
        catch (Exception e){
            System.out.println("write() operation failed: Failed to insert values into database");
            System.out.println(e.getMessage());
        }
    }
//    //read from DB
    public Cursor read(){
        try {
            db = this.getReadableDatabase();
            if (view.button.equals(OurView.GAME_OPTION_1)){
                cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_DEFAULT, null);
            }
            else if (view.button.equals(OurView.GAME_OPTION_2)){
                cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_FOOD_FEST, null);
            }
            else if (view.button.equals(OurView.GAME_OPTION_3)){
                cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_INVERTED_DEFAULT, null);
            }
            //traverse DB
            if (cursor.moveToFirst()) {
                size = 0;
                while (cursor.move(size)) {
                    size++;
                }
                setSize(size);
            }
            return cursor;
        }
        catch (Exception e){
            System.out.println("read() operation failed: Failed to read from Database");
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void close(){
        db.close();
    }

    //Length of the table
    public void setSize(int size) {
        this.size = size;
    }

    public long getSize() {
        //table size = number of levels
        return size;
    }

    public boolean isEmpty(){
        db = this.getReadableDatabase();
        try {
            if (view.button.equals(OurView.GAME_OPTION_1)) {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_DEFAULT, null);
            }
            else if (view.button.equals(OurView.GAME_OPTION_2)) {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_FOOD_FEST, null);
            }
            else if (view.button.equals(OurView.GAME_OPTION_3)) {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_INVERTED_DEFAULT, null);
            }
            //
            if (cursor.moveToFirst()) {
                size = 0;
                while (cursor.move(size)) {
                    size++;
                }
            }
            else{
                return true;
            }
            return size <= 0;
        }
        catch (Exception e){
            System.out.println("IsEmpty() Operation failed: "+e.getMessage());
            return false;
        }
    }
    void clearDefaultTable(SQLiteDatabase db){
        db.execSQL("delete from "+TABLE_NAME_DEFAULT);
    }
    void clearFoodFestTable(SQLiteDatabase db){
        db.execSQL("delete from "+TABLE_NAME_FOOD_FEST);;
    }
    void clearInvertedTable(SQLiteDatabase db){
        db.execSQL("delete from "+TABLE_NAME_INVERTED_DEFAULT);;
    }
}
