package com.idisfkj.shanbaywork.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.adapter.ArticleContetnAdapter;
import com.idisfkj.shanbaywork.entity.Article;

/**
 * 文章内容界面
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class ArticleContentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        initData();
    }

    public void initData() {
        Article article = (Article) getIntent().getExtras().getSerializable("article");
        String title = article.getTitle();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ArticleContetnAdapter adapter = new ArticleContetnAdapter(getSupportFragmentManager(), article);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //回退
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
