/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guoyongxin.R;
import com.guoyongxin.activity.MediaPlayerActivity;
import com.guoyongxin.activity.MusicSelectActivity;
import com.guoyongxin.db.DBHelper;


public class MusicListFragment extends Fragment
{
    private ListView mMusicList;
    private TextView mNoMusicText;
    private boolean mIsEditMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.main_container, container, false);
        mMusicList = (ListView) root.findViewById(R.id.play_list);
        mNoMusicText = (TextView) root.findViewById(R.id.no_content_txt);
        refreshPlayList();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshPlayList()
    {
        Cursor cs = new DBHelper(getActivity()).getMusic();
        mMusicList.setAdapter(new MusicListAdapter(getActivity(), cs, true));
        if (mMusicList.getCount() > 0)
        {
            mNoMusicText.setVisibility(View.GONE);
        }
        else
        {
            mNoMusicText.setVisibility(View.VISIBLE);
        }
        ((BaseAdapter) mMusicList.getAdapter()).notifyDataSetChanged();

    }

    private class MusicListAdapter extends CursorAdapter
    {

        private OnClickListener mClickListener;

        public MusicListAdapter(Context context, Cursor c, boolean autoRequery)
        {
            super(context, c, autoRequery);
            mClickListener = new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    String path = (String) v.getTag(R.layout.item_music_pick);
                    String name = (String) v.getTag(R.id.music_name);
                    Intent intent = new Intent();
                    intent.putExtra("PATH", path);//FIXME hardcode string
                    intent.putExtra("NAME", name);
                    intent.setClass(getActivity(), MediaPlayerActivity.class);
                    startActivity(intent);
                }
            };
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.item_music_pick, parent, false);
            Holder holder = new Holder(view);
            view.setTag(holder);
            view.setOnClickListener(mClickListener);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            Holder holder = (Holder) view.getTag();
            if (mIsEditMode)
            {
                holder.checkBox.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.checkBox.setVisibility(View.GONE);
            }
            int columIndex = cursor
                    .getColumnIndex(MusicSelectActivity.MUSIC_ALBUM);
            holder.album.setText(cursor.getString(columIndex));
            columIndex = cursor.getColumnIndex(MusicSelectActivity.MUSIC_NAME);
            holder.name.setText(cursor.getString(columIndex));
            view.setTag(R.id.music_name, holder.name.getText());
            columIndex = cursor.getColumnIndex(MusicSelectActivity.MUSIC_PATH);
            String path = cursor.getString(columIndex);
            view.setTag(R.layout.item_music_pick, path);
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
