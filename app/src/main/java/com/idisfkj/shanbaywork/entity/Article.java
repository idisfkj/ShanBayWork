package com.idisfkj.shanbaywork.entity;

import java.io.Serializable;

/**
 * 文章实体类
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class Article implements Serializable{
    private String title;
    private String content;
    private String newWords;
    private String translation;

    public Article(String title, String content, String newWords, String translation) {
        this.title = title;
        this.content = content;
        this.newWords = newWords;
        this.translation = translation;
    }

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewWords() {
        return newWords;
    }

    public void setNewWords(String newWords) {
        this.newWords = newWords;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
