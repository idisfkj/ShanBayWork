package com.idisfkj.shanbaywork.parse;

import android.content.Context;

import com.idisfkj.shanbaywork.App;
import com.idisfkj.shanbaywork.dao.ArticleDataHelper;
import com.idisfkj.shanbaywork.dao.DataBaseHelper;
import com.idisfkj.shanbaywork.dao.WordsListDataHelper;
import com.idisfkj.shanbaywork.entity.Article;
import com.idisfkj.shanbaywork.entity.WordsList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本解析类
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class ParseData {
    private static DataBaseHelper dataBaseHelper = new DataBaseHelper(App.mContext);

    private ParseData() {
    }

    /**
     * 解析文章
     *
     * @param context
     * @return
     */
    public static void parseArticles(Context context) {
        ArticleDataHelper articleHelper = new ArticleDataHelper(dataBaseHelper);
        List<Article> articles = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("nce4_content.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuffer title = new StringBuffer();
            StringBuffer content = new StringBuffer();
            StringBuffer newWords = new StringBuffer();
            StringBuffer translation = new StringBuffer();
            boolean titleFlag = false;
            boolean contentFlag = false;
            boolean newWordsFlag = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("LESSON START")) {
                    titleFlag = true;
                } else if (line.startsWith("First listen")) {
                    content.append(line).append("\n");
                    contentFlag = true;
                    titleFlag = false;
                } else if (line.startsWith("New words and expression")) {
                    newWordsFlag = true;
                    contentFlag = false;
                } else if (line.startsWith("参考译文")) {
                    newWordsFlag = false;
                } else if (line.startsWith("LESSON END")) {
                    //保存数据
                    Article article = new Article();
                    article.setTitle(title.toString());
                    article.setContent(content.toString());
                    article.setNewWords(newWords.toString());
                    article.setTranslation(translation.toString());
                    articles.add(article);
//                    Log.v("TAG",article.toString());

                    //清空数据缓存
                    title = new StringBuffer();
                    content = new StringBuffer();
                    newWords = new StringBuffer();
                    translation = new StringBuffer();
                } else {
                    if (titleFlag) {
                        //标题
                        if (!String.valueOf(title).equals("")) {
                            title.append("#");
                        }
                        title.append(line);
//                        Log.v("TAG", "title:" + title.toString());
                    } else if (contentFlag) {
                        //原文
                        content.append(line);
                        content.append("\n");
                    } else if (newWordsFlag) {
                        //新词
                        newWords.append(line);
                        newWords.append("\n");
                    } else {
                        //翻译
                        translation.append(line);
                        translation.append("\n");
                    }
                }
            }
            //插入到数据库
            articleHelper.bulkInsert(articles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析单词列表
     *
     * @param context
     */
    public static void parseWords(Context context) {
        WordsListDataHelper wordsHelper = new WordsListDataHelper(dataBaseHelper);
        List<WordsList> wordsLists = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("nce4_words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            //跳过第一行数据
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] str = line.split("\t");
                WordsList wordsList = new WordsList();
                wordsList.setWord(str[0]);
                wordsList.setLevel(Integer.valueOf(str[1]));
                wordsLists.add(wordsList);
//                Log.v("TAG",wordsList.toString());
            }
            //插入到数据库
            wordsHelper.bulkInsert(wordsLists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
