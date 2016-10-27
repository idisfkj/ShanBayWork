package com.idisfkj.shanbaywork.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.idisfkj.shanbaywork.App;
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
        //第一次启动初始化数据
        if (!App.sp.getBoolean(App.FIRST, false))
            firstOpenInitData(this);
    }

    /**
     * 第一次安装开启时初始化数据
     */
    public void firstOpenInitData(final Context context) {
        final ProgressDialog dialog = showProgressDialog();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final ArticleDataHelper articleHelper = new ArticleDataHelper(dataBaseHelper);
        final WordsListDataHelper wordsHelper = new WordsListDataHelper(dataBaseHelper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Article> list = ParseData.parseArticles(context);
                List<WordsList> wordsLists = ParseData.parseWords(context);
                for (Article article : list)
                    articleHelper.insert(article);
                for (WordsList word : wordsLists) {
                    wordsHelper.insert(word);
                }
                App.sp.edit().putBoolean(App.FIRST, true).commit();
                dialog.cancel();
            }
        }).start();
    }

    public ProgressDialog showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_message));
        dialog.setTitle(getString(R.string.dialog_title));
        dialog.show();
        return dialog;
    }
}
