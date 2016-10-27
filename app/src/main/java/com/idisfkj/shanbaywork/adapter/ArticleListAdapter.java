package com.idisfkj.shanbaywork.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.dao.ArticleDataHelper;

/**
 * 文章列表Adapter
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class ArticleListAdapter extends RecyclerViewCursorBaseAdapter<ArticleListAdapter.ArticleListViewHolder> {
    private LayoutInflater mLayoutInflater;

    public ArticleListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(ArticleListViewHolder holder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(ArticleDataHelper.ArticleInfo.TITLE));
        String[] split = title.split("#");
        holder.articleTitle.setText(split[0] + "\n" + split[1] + "\n" + split[2]);
    }

    @Override
    public ArticleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.article_list_item, parent, false);
        return new ArticleListViewHolder(view);
    }

    public static class ArticleListViewHolder extends RecyclerView.ViewHolder {
        private TextView articleTitle;

        public ArticleListViewHolder(View itemView) {
            super(itemView);
            articleTitle = (TextView) itemView.findViewById(R.id.article_list_title);
        }
    }
}
