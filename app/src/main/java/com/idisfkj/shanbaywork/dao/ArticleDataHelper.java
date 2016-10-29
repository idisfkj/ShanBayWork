package com.idisfkj.shanbaywork.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.idisfkj.shanbaywork.entity.Article;

import java.util.List;

/**
 * 文章数据库操作类
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class ArticleDataHelper implements EntityDataHelper<Article> {
    private DataBaseHelper helper;

    public ArticleDataHelper(DataBaseHelper helper) {
        this.helper = helper;
    }

    public static class ArticleInfo implements BaseColumns {
        public ArticleInfo() {
        }

        public static final String TABLE_NAME = "article";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String NEWWORDS = "newWords";
        public static final String TRANSLATION = "translation";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(TITLE, Column.DataType.TEXT)
                .addColumn(CONTENT, Column.DataType.TEXT)
                .addColumn(NEWWORDS, Column.DataType.TEXT)
                .addColumn(TRANSLATION, Column.DataType.TEXT);
    }

    @Override
    public ContentValues getContentValues(Article article) {
        ContentValues values = new ContentValues();
        values.put(ArticleInfo.TITLE, article.getTitle());
        values.put(ArticleInfo.CONTENT, article.getContent());
        values.put(ArticleInfo.NEWWORDS, article.getNewWords());
        values.put(ArticleInfo.TRANSLATION, article.getTranslation());
        return values;
    }

    @Override
    public Cursor query(int id) {
            String[] colunms = new String[]{ArticleInfo._ID
                    ,ArticleInfo.TITLE
                    , ArticleInfo.CONTENT
                    , ArticleInfo.NEWWORDS
                    , ArticleInfo.TRANSLATION
            };
            Cursor cursor = helper.getReadableDatabase().query(ArticleInfo.TABLE_NAME
                    , colunms,null
                    , null, null, null, ArticleInfo._ID);
            return cursor;
    }

    @Override
    public void bulkInsert(List<Article> articles) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Article article : articles)
            db.insert(ArticleInfo.TABLE_NAME, null, getContentValues(article));
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e("TAG", e.getMessage());
        }finally {
            db.endTransaction();
        }
    }
}

