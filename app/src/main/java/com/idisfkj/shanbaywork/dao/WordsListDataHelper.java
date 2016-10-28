package com.idisfkj.shanbaywork.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.idisfkj.shanbaywork.entity.WordsList;

/**
 * 单词列表数据库操作类
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class WordsListDataHelper implements EntityDataHelper<WordsList> {
    private DataBaseHelper helper;
    private Object DBLock = new Object();

    public WordsListDataHelper(DataBaseHelper helper) {
        this.helper = helper;
    }

    public static class WordsListInfo implements BaseColumns {
        public static final String TABLE_NAME = "wordsList";
        public static final String WORD = "word";
        public static final String LEVEL = "level";

        public static SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(WORD, Column.DataType.TEXT)
                .addColumn(LEVEL, Column.DataType.INTEGER);
    }


    @Override
    public Cursor query(int id) {
        synchronized (DBLock) {
            String[] columns = new String[]{WordsListInfo._ID, WordsListInfo.WORD};
            Cursor cursor = helper.getReadableDatabase().query(WordsListInfo.TABLE_NAME
                    , columns, WordsListInfo.LEVEL + "<=?"
                    , new String[]{String.valueOf(id)}
                    , null, null, WordsListInfo._ID);
            return cursor;
        }
    }

    @Override
    public void insert(WordsList wordsList) {
        synchronized (DBLock) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.beginTransaction();
            try {
                db.insert(WordsListInfo.TABLE_NAME, null, getContentValues(wordsList));
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            } finally {
                db.endTransaction();
            }
        }
    }

    @Override
    public ContentValues getContentValues(WordsList wordsList) {
        ContentValues values = new ContentValues();
        values.put(WordsListInfo.WORD, wordsList.getWord());
        values.put(WordsListInfo.LEVEL, wordsList.getLevel());
        return values;
    }
}
