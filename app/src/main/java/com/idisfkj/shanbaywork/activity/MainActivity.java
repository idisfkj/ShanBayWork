package com.idisfkj.shanbaywork.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.parse.ParseData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseData.parseArticles(this);
        ParseData.parseArticles(this);
    }
}
