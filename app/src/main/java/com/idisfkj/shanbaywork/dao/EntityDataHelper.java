package com.idisfkj.shanbaywork.dao;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public interface EntityDataHelper<T> {
    /**
     * 查询
     *
     * @param id
     * @return
     */
    Cursor query(int id);

    /**
     * 插入
     *
     * @param t
     */
    void insert(T t);

    /**
     * 获取ContentValues
     *
     * @param t
     * @return
     */
    ContentValues getContentValues(T t);
}
