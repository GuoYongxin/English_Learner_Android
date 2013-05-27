/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.guoyongxin.R;
import com.guoyongxin.activity.MainActivity;
import com.guoyongxin.activity.MusicSelectActivity;
import com.guoyongxin.db.DBHelper;


public class EditFragment extends Fragment implements OnClickListener
{
    private Button mAddBtn;
    private Button mEditBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.main_header, container, false);
        mAddBtn = (Button) root.findViewById(R.id.add_media_btn);
        mEditBtn = (Button) root.findViewById(R.id.edit_media_btn);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mAddBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.add_media_btn:
            Intent intent = new Intent();
            intent.setClass(getActivity(), MusicSelectActivity.class);
            startActivityForResult(intent, 1);
            break;
        case R.id.edit_media_btn:
            new DBHelper(getActivity()).eraseDB();
            ((MusicListFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(MainActivity.MAIN)).refreshPlayList();
            break;
        }
        // Intent intent = new Intent();
        // intent.setClass(getActivity(), MusicSelectActivity.class);
        //
        // startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // refreshPlayList();
    }

}
