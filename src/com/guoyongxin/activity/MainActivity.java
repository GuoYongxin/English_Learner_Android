package com.guoyongxin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.guoyongxin.R;
import com.guoyongxin.fragment.EditFragment;
import com.guoyongxin.fragment.MusicListFragment;


public class MainActivity extends FragmentActivity
{
    private FragmentManager mFraManager;
    public static final String HEADER = "HEADER";
    public static final String MAIN = "MAIN";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFraManager = getSupportFragmentManager();
        initComponent();
    }

    private void initComponent()
    {
        mFraManager
                .beginTransaction()
                .replace(R.id.header_container, new EditFragment(), HEADER)
                .replace(R.id.main_content_container, new MusicListFragment(),
                        MAIN).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // super.onActivityResult(requestCode, resultCode, data);
        System.out.println("on Result");
        if (resultCode == 1)
        {
            ((MusicListFragment) mFraManager.findFragmentByTag(MAIN))
                    .refreshPlayList();
        }

    }

    // private void startFragment()
    // {
    //
    // }
}
