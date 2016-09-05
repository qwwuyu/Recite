package com.qwwuyu.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrConfig;

public class AActivity extends AppCompatActivity {
    Slidr.SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        findViewById(R.id.txt).setBackgroundColor(0xff00ff00);
        slidr = Slidr.attached(this, new SlidrConfig.Builder().build());
    }

    @Override
    protected void onDestroy() {
        Slidr.detached(slidr);
        super.onDestroy();
    }
}
