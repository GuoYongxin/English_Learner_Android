/*
 * Copyright (c) 2013 NeuLion, Inc. All Rights Reserved.
 */
package com.guoyongxin.activity;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.guoyongxin.R;
import com.guoyongxin.util.TimeUtil;


public class MediaPlayerActivity extends Activity implements OnClickListener,
        OnSeekBarChangeListener, OnCompletionListener
{
    private SeekBar mSeekbar;
    private Button mBackBtn;
    private Button mPlayPauseBtn;
    private Button mForwardBtn;
    private TextView mMusicName;
    private TextView mCurrtentTime;
    private TextView mTotalTime;
    private MediaPlayer mPlayer;
    private String mPath;
    private String mName;
    private boolean isPause = false;
    private boolean isInitialize = false;
    private final int FIVE_SECOND = 5 * 1000;
    private Handler mHandler = new Handler();

    Runnable updateThread = new Runnable()
    {
        public void run()
        {
            mSeekbar.setProgress(mPlayer.getCurrentPosition());
            mCurrtentTime
                    .setText(TimeUtil.getTime(mPlayer.getCurrentPosition()));
            mHandler.postDelayed(updateThread, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mSeekbar = (SeekBar) findViewById(R.id.music_seekbar);
        mBackBtn = (Button) findViewById(R.id.music_back_btn);
        mPlayPauseBtn = (Button) findViewById(R.id.music_play_pause_btn);
        mForwardBtn = (Button) findViewById(R.id.music_forward_btn);
        mMusicName = (TextView) findViewById(R.id.music_name);
        mCurrtentTime = (TextView) findViewById(R.id.music_current_time);
        mTotalTime = (TextView) findViewById(R.id.music_total_time);
        readExtras();
        initComponent();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
        case R.id.music_back_btn:
            if (isInitialize)
            {
                mPlayer.seekTo(mPlayer.getCurrentPosition() - FIVE_SECOND);
            }
            break;
        case R.id.music_play_pause_btn:
            if (!isInitialize)
            {
                initComponent();
            }
            if (mPlayer.isPlaying())
            {
                mPlayer.pause();
                mPlayPauseBtn.setText("Play");
                isPause = false;
            }
            else
            {
                mPlayer.start();
                mPlayPauseBtn.setText("Pause");
                isPause = true;
            }
            break;
        case R.id.music_forward_btn:
            if (isInitialize)
            {
                mPlayer.seekTo(mPlayer.getCurrentPosition() + FIVE_SECOND);
            }
            break;
        }
    }

    private void readExtras()
    {
        mPath = getIntent().getStringExtra("PATH");
        mName = getIntent().getStringExtra("NAME");
        // Uri uri = Uri.parse(mPath);
    }

    private void initComponent()
    {
        mPlayer = new MediaPlayer();
        try
        {
            mPlayer.setDataSource(mPath);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mBackBtn.setOnClickListener(this);
        mPlayPauseBtn.setOnClickListener(this);
        mForwardBtn.setOnClickListener(this);
        mSeekbar.setMax(mPlayer.getDuration());
        mSeekbar.setOnSeekBarChangeListener(this);
        mSeekbar.setEnabled(true);
        mMusicName.setText(mName);
        mTotalTime.setText(TimeUtil.getTime(mPlayer.getDuration()));
        mHandler.post(updateThread);
        isInitialize = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser)
    {
        if (fromUser == true && isInitialize)
        {
            mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    protected void onPause()
    {
        if (mPlayer.isPlaying())
        {
            mPlayer.pause();
            isPause = true;
        }
        mHandler.removeCallbacks(updateThread);
        super.onPause();
    }

    protected void onResume()
    {
        if (isPause)
        {
            mPlayer.start();
            isPause = false;
        }
        mHandler.post(updateThread);
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        mPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        mHandler.removeCallbacks(updateThread);
        mPlayPauseBtn.setText("Reset");
        mSeekbar.setEnabled(false);
        mp.release();
        mCurrtentTime.setText("00:00");
        isPause = true;
        isInitialize = false;
    }
}
