package com.idisfkj.shanbaywork;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class App extends Application {
    public static Context mContext;
    public static SharedPreferences sp;
    public static final String FIRST = "fistOpenInitData";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sp = getSharedPreferences("shanBay", MODE_PRIVATE);
    }
}
