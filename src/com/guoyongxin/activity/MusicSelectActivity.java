/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guoyongxin.R;
import com.guoyongxin.db.DBHelper;


public class MusicSelectActivity extends Activity
{
    // private static final String MUSIC_ALBUM_IMAGE = "musicAlbumImage";
    public static final String MUSIC_ARTIST = "musicArtist";
    public static final String MUSIC_ALBUM = "musicAlbum";
    public static final String MUSIC_NAME = "musicName";
    public static final String MUSIC_PATH = "musicPath";
    public static final String MUSIC_RATING = "musicRating";
    public static final String MUSIC_SELECTED = "musicCheck";
    public static final String MUSIC_ID = "musicID";
    private Button mAdd;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private List<Map<String, Object>> mList;
    private List<Map<String, Object>> mSelectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_select);
        mAdd = (Button) findViewById(R.id.add_to_playlist);
        mListView = (ListView) findViewById(R.id.select_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        initComponent();
    }

    private void initComponent()
    {
        new FetchMusicTask().execute();
        mAdd.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new DBHelper(MusicSelectActivity.this).insertMusic(getSelectData());
//                new DBHelper(MusicSelectActivity.this).deleteMusic(getSelectData());
//                setResult(1);
            }
        });
        setResult(1);
    }

    private List<Map<String, Object>> getSelectData()
    {
        Iterator<Map<String, Object>> it = mList.iterator();
        mSelectedList = new ArrayList<Map<String,Object>>();
        while(it.hasNext())
        {
            Map<String, Object>  musicMap =  it.next();
            if((Boolean)musicMap.get(MUSIC_SELECTED) == true)
            {
                mSelectedList.add(musicMap);
            }
        }
        return mSelectedList;
    }
    
//    private void addListToDatabase(List<Map<String, Object>> list)
//    {
//        SQLiteDatabase db  =  openOrCreateDatabase(DBHelper.DB_NAME, Context.MODE_PRIVATE, null);
//        SQLiteDatabase db2  =  new DBHelper(this,DBHelper.DB_NAME,null,1).getWritableDatabase();
//        db2.cr
//    }
//    private void getMediaData()
//    {
//
//        ContentResolver cr = getContentResolver();
//
//        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//    }

    // 查找sdcard卡上的所有歌曲信息
    private List<Map<String, Object>> getMultiData()
    {
        ArrayList<Map<String, Object>> musicList = new ArrayList<Map<String, Object>>();

        // 加入封装音乐信息的代码
        // 查询所有歌曲
        ContentResolver musicResolver = this.getContentResolver();
        Cursor musicCursor = musicResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        int musicColumnIndex;
        try
        {
            musicCursor.moveToFirst();
            if (null != musicCursor && musicCursor.getCount() > 0)
            {
                for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor
                        .moveToNext())
                {
                    Map<String, Object> musicDataMap = new HashMap<String, Object>();

                    musicColumnIndex = musicCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns._ID);
                    musicDataMap.put(MUSIC_ID, musicCursor.getInt(musicColumnIndex));
                    
                    musicDataMap.put(MUSIC_SELECTED, false);
                    // 取得音乐播放路径
                    musicColumnIndex = musicCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                    String musicPath = musicCursor.getString(musicColumnIndex);
                    musicDataMap.put(MUSIC_PATH, musicPath);

                    // 取得音乐的名字
                    musicColumnIndex = musicCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
                    String musicName = musicCursor.getString(musicColumnIndex);
                    musicDataMap.put(MUSIC_NAME, musicName);

                    // 取得音乐的专辑名称
                    musicColumnIndex = musicCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
                    String musicAlbum = musicCursor.getString(musicColumnIndex);
                    musicDataMap.put(MUSIC_ALBUM, musicAlbum);

                    // 取得音乐的演唱者
                    musicColumnIndex = musicCursor
                            .getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                    String musicArtist = musicCursor
                            .getString(musicColumnIndex);
                    musicDataMap.put(MUSIC_ARTIST, musicArtist);

                    // // 取得歌曲对应的专辑对应的Key
                    // musicColumnIndex = musicCursor
                    // .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
                    // String musicAlbumKey = musicCursor
                    // .getString(musicColumnIndex);

                    // String[] argArr = { musicAlbumKey };
                    // ContentResolver albumResolver =
                    // this.getContentResolver();
                    // Cursor albumCursor = albumResolver.query(
                    // MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                    // MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?",
                    // argArr, null);
                    //
                    // if (null != albumCursor && albumCursor.getCount() > 0)
                    // {
                    // albumCursor.moveToFirst();
                    // int albumArtIndex = albumCursor
                    // .getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
                    // String musicAlbumArtPath = albumCursor
                    // .getString(albumArtIndex);
                    // if (null != musicAlbumArtPath
                    // && !"".equals(musicAlbumArtPath))
                    // {
                    // musicDataMap.put(MUSIC_ALBUM_IMAGE,
                    // musicAlbumArtPath);
                    // }
                    // else
                    // {
                    // musicDataMap.put(MUSIC_ALBUM_IMAGE,
                    // "file:///mnt/sdcard/alb.jpg");
                    // }
                    // }
                    // else
                    // {
                    // // 没有专辑定义，给默认图片
                    // musicDataMap.put(MUSIC_ALBUM_IMAGE,
                    // "file:///mnt/sdcard/alb.jpg");
                    // }
                    musicList.add(musicDataMap);
                }
            }
        }
        catch (Exception e)
        {

        }
        finally
        {
            musicCursor.close();
        }

        return musicList;
    }

    private class FetchMusicTask extends
            AsyncTask<Void, Void, List<Map<String, Object>>>
    {
        @Override
        protected void onPreExecute()
        {
            System.out.println("pre task");
            mProgressBar.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params)
        {
            System.out.println("doing task");
            return getMultiData();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> result)
        {
            System.out.println("post task");
            mProgressBar.setVisibility(View.GONE);
            mAdd.setVisibility(View.VISIBLE);
            try
            {
                mListView.setAdapter(new MusicListAdapter(result));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

    }

    private class MusicListAdapter extends BaseAdapter
    {
        private OnClickListener mClickListener;

        public MusicListAdapter(List<Map<String, Object>> list)
                throws Exception
        {
            if (null == list)
            {
                throw new Exception("list can't be null");
            }
            mList = list;
            mClickListener = new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    System.out.println("click");
                    int position = (Integer) v.getTag(R.layout.item_music_pick);
                    mList.get(position)
                            .put(MUSIC_SELECTED,
                                    !(Boolean) (mList.get(position)
                                            .get(MUSIC_SELECTED)));
                    ((BaseAdapter) mListView.getAdapter())
                            .notifyDataSetChanged();
                }
            };
        }

        @Override
        public int getCount()
        {
            return mList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentView)
        {
            final Holder holder;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getBaseContext()).inflate(
                        R.layout.item_music_pick, parentView, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
                convertView.setOnClickListener(mClickListener);
            }
            else
            {
                holder = (Holder) convertView.getTag();
            }
            convertView.setTag(R.layout.item_music_pick, position);
            holder.name.setText((String) mList.get(position).get(MUSIC_NAME));
            holder.album.setText((String) mList.get(position).get(MUSIC_ALBUM));
            holder.checkBox.setChecked((Boolean) mList.get(position).get(
                    MUSIC_SELECTED));
            return convertView;
        }
    }

    private static class Holder
    {
        public Holder(View convertView)
        {
            checkBox = (CheckBox) convertView.findViewById(R.id.select_box);
            name = (TextView) convertView.findViewById(R.id.music_name);
            album = (TextView) convertView.findViewById(R.id.music_ablum);
        }

        CheckBox checkBox;
        TextView name;
        TextView album;
    }
}
