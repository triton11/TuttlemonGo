package com.example.tristrum.tuttlemongo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tristrum on 4/22/17.
 */

public class SavedHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Monsters.db";
    public static final String MONSTER_TABLE_NAME = "monstertable";
    public static final String MONSTER_COLUMN_SEARCH = "name";
    public static final String MONSTER_COLUMN_ITEM = "level";
    public static final String MONSTER_COLUMN_URL = "url";
    public static final String MONSTER_COLUMN_ID = "id";


    public SavedHelper(Context context){
        super(context,DATABASE_NAME,null,3);
        System.out.println("ANOTHER ONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MONSTER_TABLE_NAME + " ( " + MONSTER_COLUMN_ID +
                " integer primary key, " + MONSTER_COLUMN_SEARCH + " text, " + MONSTER_COLUMN_ITEM
                + " text, " + MONSTER_COLUMN_URL + " text )"
        );
        System.out.println("CALLED");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS monstertable");
        onCreate(db);
    }

    public boolean insertMonster (String name, String level, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.print((int) DatabaseUtils.queryNumEntries(db, MONSTER_TABLE_NAME));
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("level", level);
        contentValues.put("url", url);

        db.insert("monstertable", null, contentValues);

        return true;
    }


    public Cursor getDataSearch(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("db");
        System.out.println((int) DatabaseUtils.queryNumEntries(db, MONSTER_TABLE_NAME));
        System.out.println("db");
        System.out.println(db.toString());
        Cursor res =  db.rawQuery( "SELECT * FROM monstertable", null );
        return res;
    }

    public int deleteTitle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MONSTER_TABLE_NAME, MONSTER_COLUMN_SEARCH + " =?", new String[]{name});
    }


}
