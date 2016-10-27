package com.idisfkj.shanbaywork.entity;

/**
 * 单词列表实体类
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class WordsList {
    private String word;
    private int level;

    public WordsList(String word, int level) {
        this.word = word;
        this.level = level;
    }

    public WordsList() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
