/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.db;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.guoyongxin.activity.MusicSelectActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;


public class DBHelper
{
    public static final String DB_NAME = "playlist.db";
    public static final String DB_TABLE = "PlayListTable";
    private SQLiteDatabase mDB;
    private Context mContext;

    public DBHelper(Context context)
    {
        mContext = context;
        mDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        initDB();
    }

    private void initDB()
    {
        try
        {
            mDB.execSQL("CREATE TABLE " + " IF NOT EXISTS " + DB_TABLE
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " musicName TEXT," + " musicPath TEXT,"
                    + "musicArtist TEXT," + " musicAlbum TEXT)  ;");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            mDB.close();
        }
    }

    public void insertMusic(List<Map<String, Object>> list)
    {
        mDB = mContext
                .openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Iterator<Map<String, Object>> it = list.iterator();
        String musicName;
        String musicPath;
        String musicAlbum;
        String musicArtist;
        int musicID;
        ContentValues cv = new ContentValues();
        try
        {
            while (it.hasNext())
            {
                Map<String, Object> musicMap = it.next();
                musicName = (String) musicMap
                        .get(MusicSelectActivity.MUSIC_NAME);
                musicPath = (String) musicMap
                        .get(MusicSelectActivity.MUSIC_PATH);
                musicAlbum = (String) musicMap
                        .get(MusicSelectActivity.MUSIC_ALBUM);
                musicArtist = (String) musicMap
                        .get(MusicSelectActivity.MUSIC_ARTIST);
                musicID = (Integer) musicMap.get(MusicSelectActivity.MUSIC_ID);
                cv.put(MediaStore.Audio.AudioColumns._ID, musicID);
                cv.put(MusicSelectActivity.MUSIC_NAME, musicName);
                cv.put(MusicSelectActivity.MUSIC_PATH, musicPath);
                cv.put(MusicSelectActivity.MUSIC_ALBUM, musicAlbum);
                cv.put(MusicSelectActivity.MUSIC_ARTIST, musicArtist);
                mDB.insert(DB_TABLE, null, cv);
                // System.out.println(mDB.insert(DB_TABLE, null, cv));
            }
        }
        catch (Exception e)
        {

        }
        finally
        {
            mDB.close();
        }
    }

    public void deleteMusic(List<Map<String, Object>> list)
    {
        mDB = mContext
                .openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Iterator<Map<String, Object>> it = list.iterator();
        int musicID;
        ContentValues cv = new ContentValues();
        while (it.hasNext())
        {
            Map<String, Object> musicMap = it.next();
            musicID = (Integer) musicMap.get(MusicSelectActivity.MUSIC_ID);
            cv.put(MusicSelectActivity.MUSIC_ID, musicID);
            // mDB.delete(DB_TABLE, "_id = ?", new
            // String[]{String.valueOf(musicID)});
            System.out.println(mDB.delete(DB_TABLE, "_id = ?",
                    new String[] { String.valueOf(musicID) }));
        }
        mDB.close();
    }

    public Cursor getMusic()
    {
        mDB = mContext
                .openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        return mDB.rawQuery("select * from " + DB_TABLE, null);
    }

    public void eraseDB()
    {
        mDB = mContext
                .openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
//        mDB.rawQuery("DROP TABLE IF EXISTS " + DB_TABLE + ";", null);
        mDB.execSQL("DROP TABLE IF EXISTS " + DB_TABLE + ";");
    }
}
