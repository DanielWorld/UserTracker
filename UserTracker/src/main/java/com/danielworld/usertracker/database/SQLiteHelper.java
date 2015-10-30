package com.danielworld.usertracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danielpark.androidlibrary.log.Logger;
import com.danielworld.usertracker.model.TrackingModel;
import com.danielworld.usertracker.util.JsonUtil;
import com.danielworld.usertracker.variables.TrackingMapKey;

import java.util.List;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private final String TAG = SQLiteHelper.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TEMPORARY = "temporary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";

    public SQLiteHelper(Context context) {
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
    synchronized public void putAllTrackingsToDatabase(List<List<TrackingModel>> allTrackingPackage){
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

    /**
     * Clear certain trackings (rows)
     * @param allTrackingInfo
     */
    synchronized public void clearTracking(List<List<TrackingModel>> allTrackingInfo){

        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();

            TrackingModel aaa = new TrackingModel();

            for(int i=0; i <allTrackingInfo.size(); i++) {
                aaa.setTrackingList(allTrackingInfo.get(i));
                db.delete(SQLiteHelper.TABLE_TEMPORARY, SQLiteHelper.COLUMN_ID + "= ?", new String[]{aaa.getValuePair(TrackingMapKey.TRACKING_ROW_ID)});
            }

            LOG.d(TAG, "tracking data has deleted");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db != null)
                db.close();
        }

    }
}
