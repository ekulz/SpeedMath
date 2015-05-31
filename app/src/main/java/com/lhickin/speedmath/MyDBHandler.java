
package com.lhickin.speedmath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A DB manager/handler
 * A class specifically for working with the database (i.e a file we save on the users device)
 * responsible for creating, adding, updating database (trying to do something with the database)
 */
public class MyDBHandler extends SQLiteOpenHelper {

    //our first database version. needs to be updated each time database is changed
    private static final int DATABASE_VERSION = 1;
    //the filename for the device
    private static final String DATABASE_NAME = "highscores.db";
    //name of tables
    public static final String TABLE_1 = "Player";
    public static final String TABLE_2 = "Game";
    public static final String TABLE_3 = "Equation";
    //name of table_1 columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";
    //name of table_2 columns
    public static final String COLUMN_PLAYERID = "_playerId";
    public static final String COLUMN_GAMEID = "_gameId";
    public static final String COLUMN_SCORE = "_scoreInSeconds";
    public static final String COLUMN_DATE = "_date";
    //name of table_3 columns
    public static final String COLUMN_FROMGAMEID = "_fromGameId";
    public static final String COLUMN_EQUATIONID = "_equationId";
    public static final String COLUMN_EQUATION = "_equation";
    public static final String COLUMN_ANSWER = "_answer";
    public static final String COLUMN_REALANSWER = "_realAnswer";

    int lastid, lastGameId;


    //constructor for housekeeping
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    //called when running first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        //make a query
        //creates a table and includes the attributes for the table
        String query = "CREATE TABLE " + TABLE_1 + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT"
                + ");";
        String query2 = "CREATE TABLE " + TABLE_2 + "(" +
                COLUMN_GAMEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                //+ COLUMN_PLAYERID + " INTEGER REFERENCES " + TABLE_1 + "(" + COLUMN_ID + "), "
                + COLUMN_PLAYERID + " INTEGER, "
                + COLUMN_SCORE + " INTEGER, "
                + COLUMN_DATE + " DATE"
                + ");";
        String query3 = "CREATE TABLE " + TABLE_3 + "(" +
                COLUMN_EQUATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                //+ COLUMN_FROMGAMEID + " INTEGER REFERENCES " + TABLE_2 + "(" + COLUMN_GAMEID + "), "
                + COLUMN_FROMGAMEID + " INTEGER, "
                + COLUMN_EQUATION + " TEXT, "
                + COLUMN_ANSWER + " INTEGER, "
                + COLUMN_REALANSWER + " INTEGER"
                + ");";
        //execute the query
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }

    //called when upgrading version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_3);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_1);
        onCreate(db);
    }

    //exists method for adding a player
    public boolean playerExists(String name) {
        String playerExistsQuery = "select 1 from " + TABLE_1 + " where " + COLUMN_NAME  + "=\"" + name + "\";";
        SQLiteDatabase mdb = getWritableDatabase();
        Cursor cursor = mdb.rawQuery(playerExistsQuery, null);
        boolean exists = (cursor.getCount() > 0);
        Log.e("PLAYER EXISTS", "" + playerExistsQuery);
        Log.e("PLAYER:", " " + name + " exists is: " + exists);
        cursor.close();
        return exists;
    }

    //add a new player to database
    public void addPlayer(Player player) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, player.getName());
        SQLiteDatabase db = getWritableDatabase();
        //insert the player into the table
        db.insert(TABLE_1, null, values);
        db.close();
    }



    //add a new game to database
    public void addGame(Game game, Player player) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, game.getScore());
        values.put(COLUMN_DATE, game.getDate());
        SQLiteDatabase db = getWritableDatabase();
        //String queryForId = "SELECT " + COLUMN_ID + " FROM " + TABLE_1 + " ORDER BY " + COLUMN_ID + " DESC limit 1";
        String queryForId = " SELECT " + COLUMN_ID + " FROM " + TABLE_1 + " WHERE " + COLUMN_NAME + "=\"" + player.getName().toString() + "\";";
        Cursor c = db.rawQuery(queryForId, null);
        if (c != null && c.moveToFirst()) {
            lastid = c.getInt(0);
            Log.e("LASTID", " equals: " + lastid);
        }
        values.put(COLUMN_PLAYERID, lastid);
        //insert values into table
        db.insert(TABLE_2, null, values);
        db.close();
    }

    public void addEquation(Equation equation) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EQUATION, equation.getEquation());
        values.put(COLUMN_ANSWER, equation.getAnswer());
        values.put(COLUMN_REALANSWER, equation.isCorrect());
        SQLiteDatabase db = getWritableDatabase();
        String queryForId = "SELECT " + COLUMN_GAMEID + " FROM " + TABLE_2 + " ORDER BY " + COLUMN_GAMEID + " DESC LIMIT 1";
        Cursor c = db.rawQuery(queryForId, null);
        if (c != null && c.moveToFirst()) {
            lastGameId = c.getInt(0);
        }
        values.put(COLUMN_FROMGAMEID, lastGameId + 1);
        //insert values into table
        db.insert(TABLE_3, null, values);
        db.close();
    }



    //print database as a string for highscores page
    //gets the names for first textview
    public String databaseToStringNames() {
        String dbString = "";
        int place = 0;
        //reference to database
        SQLiteDatabase db = getReadableDatabase();
        //String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_SCORE + " FROM " + TABLE_1 + " INNER JOIN " + TABLE_2 + " ORDER BY " + COLUMN_SCORE + " ASC LIMIT 20";
        String query = "SELECT " + "p." + COLUMN_NAME + ", " + "g." + COLUMN_SCORE + " FROM " + TABLE_1  + " p, "
                + TABLE_2 + " g " + "WHERE g." + COLUMN_PLAYERID + " = p." + COLUMN_ID + " order by "
                + COLUMN_SCORE + " ASC LIMIT 20";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);

        //scan through the table returned by query and add to our string to be displayed
        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    place++;
                    dbString += place + ".      ";
                    dbString += c.getString(0) + "      ";
                    dbString += "\n";
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return dbString;
    }

    //print database as a string for highscores page
    //gets the scores for second textview
    public String databaseToStringScores() {
        String dbString2 = "";
        //reference to database
        SQLiteDatabase db = getReadableDatabase();
        //String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_SCORE + " FROM " + TABLE_1 + " INNER JOIN " + TABLE_2 + " ORDER BY " + COLUMN_SCORE + " ASC LIMIT 20";
        String query = "SELECT " + "p." + COLUMN_NAME + ", " + "g." + COLUMN_SCORE + " FROM " + TABLE_1  + " p, "
                + TABLE_2 + " g " + "WHERE g." + COLUMN_PLAYERID + " = p." + COLUMN_ID + " order by "
                + COLUMN_SCORE + " ASC LIMIT 20";
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);

        //scan through the table returned by query and add to our string to be displayed
        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    dbString2 += c.getString(1);
                    dbString2 += " seconds\n";
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return dbString2;
    }



}
