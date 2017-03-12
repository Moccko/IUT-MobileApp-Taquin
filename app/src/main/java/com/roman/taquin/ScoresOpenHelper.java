package com.roman.taquin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roman on 11/03/17.
 */

class ScoresOpenHelper extends SQLiteOpenHelper {
    static final String SCORES_TABLE = "Scores";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_DB =
            "CREATE TABLE " + SCORES_TABLE + " (" +
                    "Date DATE, " +
                    "Time TEXT, " +
                    "Moves INT);";

    ScoresOpenHelper(Context context) {
        super(context, "Taquin", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + SCORES_TABLE + ";");
        onCreate(db);
    }
}
