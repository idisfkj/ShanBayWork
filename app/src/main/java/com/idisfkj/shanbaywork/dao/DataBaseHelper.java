package com.idisfkj.shanbaywork.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATA_NAME = "shanbay.db";
    private static final int VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATA_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArticleDataHelper.ArticleInfo.TABLE.create(db);
        WordsListDataHelper.WordsListInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
