package com.qwwuyu.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrConfig;

public class MainActivity extends AppCompatActivity {
    Slidr.SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        slidr = Slidr.attached(this, new SlidrConfig.Builder().build());
        slidr.lock();
        startActivity(new Intent(this, AActivity.class));
        findViewById(R.id.txt).setBackgroundColor(0xffff0000);
        findViewById(R.id.txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        Slidr.detached(slidr);
        super.onDestroy();
    }
}
