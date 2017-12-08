package com.example.marjorie.color_match;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marjorie on 08/12/17.
 */

public class DataBase extends SQLiteOpenHelper {
	DataBase(Context context) {
		super(context, "BestScore"+".db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL("CREATE TABLE " + "BestScore" + " ("
						+"cle"+" INTEGER PRIMARY KEY AUTOINCREMENT,"
						+"name"+" VARCHAR(100) NOT NULL,"
						+"score"+" INTEGER NOT NULL"
						+");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

	public void insert(int score, String name){
		ContentValues newScore = new ContentValues();
		newScore.put("name", name);
		newScore.put("score", score);
		this.getWritableDatabase().insert("BestScore", null, newScore);
	}

	private Cursor selectAll(){
		String colone[] = {"name", "score"};
		return this.getReadableDatabase().query("BestScore", colone, null, null, null, null, "score"+" DESC");
	}

	public String[] StringSelectAll(){
		Cursor cursor = selectAll();
		String myScore[] = new String[cursor.getCount()];
		cursor.moveToFirst();
			String acc = "";
			int i = 0;
			while (cursor.isAfterLast() == false) {
				acc = cursor.getString(cursor.getColumnIndex("name")) + " : ";
				acc += String.valueOf(cursor.getInt(cursor.getColumnIndex("score")));
				cursor.moveToNext();
				myScore[i] = acc;
				i++;
			}
			cursor.close();
		return myScore;
	}

}
