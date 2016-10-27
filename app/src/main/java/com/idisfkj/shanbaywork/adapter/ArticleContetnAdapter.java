package com.idisfkj.shanbaywork.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.idisfkj.shanbaywork.entity.Article;
import com.idisfkj.shanbaywork.fragment.NewWordsFragment;
import com.idisfkj.shanbaywork.fragment.OriginalContentFragment;
import com.idisfkj.shanbaywork.fragment.TranslationFragment;

/**
 * 文章内容Fragment Adapter
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class ArticleContetnAdapter extends FragmentStatePagerAdapter {
    private Article article;

    public ArticleContetnAdapter(FragmentManager fm, Article article) {
        super(fm);
        this.article = article;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OriginalContentFragment.getInstance(article.getContent());
            case 1:
                return TranslationFragment.getInstance(article.getTranslation());
            case 2:
                return NewWordsFragment.getInstance(article.getNewWords());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "原文";
            case 1:
                return "译文";
            case 2:
                return "生词";
        }
        return super.getPageTitle(position);
    }
}
