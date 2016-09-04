package com.qwwuyu.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrConfig;

public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        Slidr.SlidrInterface attach = Slidr.attached(this, new SlidrConfig.Builder().build());
    }
}
