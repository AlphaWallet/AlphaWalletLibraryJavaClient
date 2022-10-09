package com.alphawallet.alphawalletlibraryjavaclient;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alphawallet.app.ui.DappBrowserFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyBrowserActivity extends AppCompatActivity
{
    public MyBrowserActivity()
    {
        super(R.layout.browser);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("some_int", 0);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_browser, DappBrowserFragment.class, bundle)
                    .commit();
        }
    }
}
