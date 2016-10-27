package com.idisfkj.shanbaywork.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.dao.ArticleDataHelper;
import com.idisfkj.shanbaywork.dao.DataBaseHelper;
import com.idisfkj.shanbaywork.dao.WordsListDataHelper;
import com.idisfkj.shanbaywork.entity.Article;
import com.idisfkj.shanbaywork.entity.WordsList;
import com.idisfkj.shanbaywork.parse.ParseData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArticleDataHelper articleHelper = new ArticleDataHelper(new DataBaseHelper(this));
        WordsListDataHelper wordsHelper = new WordsListDataHelper(new DataBaseHelper(this));
        List<Article> list = ParseData.parseArticles(this);
        List<WordsList> wordsLists = ParseData.parseWords(this);
        for (Article article : list)
            articleHelper.insert(article);
        for (WordsList word : wordsLists) {
            wordsHelper.insert(word);
        }
        Cursor articleCursor = articleHelper.query(1);
        articleCursor.moveToFirst();
        Cursor wordCursor = wordsHelper.query(1);
        wordCursor.moveToFirst();
        Log.v("TAG", "title:" + articleCursor.getString(articleCursor.getColumnIndex(ArticleDataHelper.ArticleInfo.TITLE)));
        Log.v("TAG", "wordsList:" + wordCursor.getString(wordCursor.getColumnIndex(WordsListDataHelper.WordsListInfo.WORD)));
    }
}
