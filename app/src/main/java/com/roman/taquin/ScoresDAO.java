package com.roman.taquin;

/**
 * Created by roman on 12/03/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class ScoresDAO {

    private static final String COL_DATE = "Date";
    private static final int NUM_COL_DATE = 0;
    private static final String COL_TIME = "Time";
    private static final int NUM_COL_TIME = 1;
    private static final String COL_MOVES = "Moves";
    private static final int NUM_COL_MOVES = 2;

    private SQLiteDatabase db;

    private ScoresOpenHelper scoresOpenHelper;

    ScoresDAO(Context context) {
        scoresOpenHelper = new ScoresOpenHelper(context);
    }

    void open() {
        db = scoresOpenHelper.getWritableDatabase();
    }

    void close() {
        db.close();
    }

    long insertScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(COL_DATE, score.getDate());
        values.put(COL_TIME, score.getTime());
        values.put(COL_MOVES, score.getMoves());
        return db.insert(ScoresOpenHelper.SCORES_TABLE, null, values);
    }

    ArrayList<Score> findAll() {
        ArrayList<Score> scores = new ArrayList<>();
        Cursor c = db.query(ScoresOpenHelper.SCORES_TABLE, new String[]{COL_DATE, COL_TIME, COL_MOVES}, null, null, null, null,
                COL_MOVES + " ASC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String date = c.getString(NUM_COL_DATE);
                String time = c.getString(NUM_COL_TIME);
                int moves = c.getInt(NUM_COL_MOVES);
                scores.add(new Score(date, time, moves));
            }
            while (c.moveToNext());
        }
        c.close();
        return scores;
    }

    boolean eraseAll() {
        db.execSQL("delete from " + ScoresOpenHelper.SCORES_TABLE);
        return false;
    }
}