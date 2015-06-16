package com.namgyuworld.usertracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.util.JsonUtil;

import java.util.List;

/**
 * Created by danielpark on 6/16/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TEMPORARY = "temporary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TEMPORARY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY ASC, " + COLUMN_DATA + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_TEMPORARY);
        onCreate(db);
    }

    /**
     * Put on tracking into temporary database
     * @param tracking
     */
    synchronized public void putTemporary(List<TrackingModel> tracking){
        SQLiteDatabase db = null;
        ContentValues cv;

        try{
            db = getWritableDatabase();
            cv = new ContentValues();
            cv.put(SQLiteHelper.COLUMN_DATA, new JsonUtil().toJson(tracking));
            db.insert(SQLiteHelper.TABLE_TEMPORARY, null, cv);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    /**
     * Put all trackings into temporary database
     * @param allTrackingPackage
     */
    synchronized public void putTrackingToDatabase(List<List<TrackingModel>> allTrackingPackage){
        SQLiteDatabase db = null;
        ContentValues cv;

        try{
            db = getWritableDatabase();
            for(List<TrackingModel> tracking : allTrackingPackage){
                cv = new ContentValues();
                cv.put(SQLiteHelper.COLUMN_DATA, new JsonUtil().toJson(tracking));
                db.insert(SQLiteHelper.TABLE_TEMPORARY, null, cv);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    /**
     * Clear temporary database
     */
    synchronized public void clearTemporaryDB(){
        SQLiteDatabase db = null;

        try{
            db = getWritableDatabase();
            db.execSQL("DROP TABLE " + TABLE_TEMPORARY);
            db.execSQL("CREATE TABLE " + TABLE_TEMPORARY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY ASC, " + COLUMN_DATA + " TEXT NOT NULL)");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }
}
