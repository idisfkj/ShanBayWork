package com.idisfkj.shanbaywork.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.idisfkj.shanbaywork.App;
import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.adapter.ArticleListAdapter;
import com.idisfkj.shanbaywork.dao.ArticleDataHelper;
import com.idisfkj.shanbaywork.dao.DataBaseHelper;
import com.idisfkj.shanbaywork.parse.ParseData;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArticleListAdapter adapter;
    private Cursor cursor;
    private final int FINISH = 1;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        //第一次启动初始化数据
        if (!App.sp.getBoolean(App.FIRST, false)) {
            firstOpenInitData(this);
        }
    }

    public void initData() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ArticleDataHelper helper = new ArticleDataHelper(new DataBaseHelper(this));
        cursor = helper.query(1);
        adapter = new ArticleListAdapter(this, cursor);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FINISH)
                mRecyclerView.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 第一次安装开启时初始化数据
     */
    public void firstOpenInitData(final Context context) {
        mRecyclerView.setVisibility(View.GONE);
        final ProgressDialog dialog = showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseData.parseArticles(context);
                ParseData.parseWords(context);
                App.sp.edit().putBoolean(App.FIRST, true).commit();
                dialog.cancel();
                mHandler.sendEmptyMessage(FINISH);
            }
        }).start();
    }

    public ProgressDialog showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.dialog_message));
        dialog.setTitle(getString(R.string.dialog_title));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 2000) {
                time = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.key_back), Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }
}
