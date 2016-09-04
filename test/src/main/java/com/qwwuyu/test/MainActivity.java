package com.qwwuyu.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.r0adkll.slidr.SliderPanel;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrConfig;

public class MainActivity extends AppCompatActivity {
    public static SliderPanel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        Slidr.SlidrInterface slidr = Slidr.attached(this, new SlidrConfig.Builder().build());
        panel = slidr.getPanel();
        startActivity(new Intent(this, AActivity.class));
        findViewById(R.id.txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AActivity.class));
            }
        });
    }
}
