package com.idisfkj.shanbaywork.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.idisfkj.shanbaywork.App;
import com.idisfkj.shanbaywork.R;
import com.idisfkj.shanbaywork.dao.DataBaseHelper;
import com.idisfkj.shanbaywork.dao.WordsListDataHelper;
import com.idisfkj.shanbaywork.view.JustifyTextView;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.TreeMap;

/**
 * 原文界面
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class OriginalContentFragment extends Fragment {
    private JustifyTextView articalOriContent;
    private SeekBar seekBar;
    private Switch aSwitch;
    private TextView textLevel;
    private String originalContent;
    private WordsListDataHelper helper;
    private TreeMap<Integer, Integer> wordList = new TreeMap<>();
    private final int UPDATE = 1;

    public static OriginalContentFragment getInstance(String content) {
        Bundle bundle = new Bundle();
        bundle.putString("originalContent", content);
        OriginalContentFragment fragment = new OriginalContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_original_content, null);
        initData(view);
        //恢复数据
        if (savedInstanceState != null) {
            seekBar.setProgress(savedInstanceState.getInt("level"));
            textLevel.setText(savedInstanceState.getInt("level") + "");
        }
        setListener();
        clickOnTheHighLighted(originalContent, articalOriContent);
        return view;
    }

    public void initData(View view) {
        articalOriContent = (JustifyTextView) view.findViewById(R.id.article_original_content);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        aSwitch = (Switch) view.findViewById(R.id.switch_bar);
        textLevel = (TextView) view.findViewById(R.id.text_level);
        originalContent = getArguments().getString("originalContent", "");
        articalOriContent.setText(originalContent);
        //默认不能点击
        seekBar.setEnabled(false);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textLevel.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                switchHightLighted(progress);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seekBar.setEnabled(true);
                    switchHightLighted(seekBar.getProgress());
                } else {
                    seekBar.setEnabled(false);
                    //还原文本
                    articalOriContent.invalidate();
                    articalOriContent.setWordList(null);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                //更新高亮点击的文本
                articalOriContent.invalidate();
            }
        }
    };


    /**
     * 选择开关文本高亮
     *
     * @param progress
     */
    public void switchHightLighted(final int progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (helper == null)
                    helper = new WordsListDataHelper(new DataBaseHelper(App.mContext));
                //获取单词列表中的单词
                getWordsList(helper.query(progress));
                mHandler.sendEmptyMessage(UPDATE);
            }
        }).start();
    }

    /**
     * 获取单词列表中的单词
     *
     * @param cursor
     */
    public void getWordsList(Cursor cursor) {
        //清除数据,重新提取
        wordList.clear();
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex(WordsListDataHelper.WordsListInfo.WORD));
            int start = originalContent.indexOf(word);
            if (start < 0) {
                //目标文章不存在该词则进行下一个
                continue;
            }
            int end = start + word.length();

            //过滤不完整的单词,例如:目标单词为work,文章中可能存在network单词,但不符合
            if ((Character.toLowerCase(originalContent.charAt(start - 1)) < 'a'
                    || Character.toLowerCase(originalContent.charAt(start - 1)) > 'z')
                    && (Character.toLowerCase(originalContent.charAt(end)) < 'a'
                    || Character.toLowerCase(originalContent.charAt(end)) > 'z')) {
                wordList.put(start, end);
            }
        }
        articalOriContent.setWordList(wordList);
    }

    /**
     * 点击高亮
     *
     * @param content
     * @param textView
     */
    public void clickOnTheHighLighted(CharSequence content, TextView textView) {
        //为了能够响应用户点击,设置成超链接可点击状态
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //对于使用Span的文本前后新插入新的文本,插入的前后都不应用Span的效果
        textView.setText(content, TextView.BufferType.SPANNABLE);

        Spannable spannable = (Spannable) textView.getText();
        //断点遍历
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(content.toString());
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String possibleWord = content.toString().substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan();
                spannable.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //高亮点击事件处理
                TextView textView = (TextView) widget;
                //获取高亮文本
                textView.getText().subSequence(textView.getSelectionStart(), textView.getSelectionEnd()).toString();
//                Log.v("TAG", textView.getSelectionStart() + " "
//                        + textView.getSelectionEnd() + " "
//                        + originalContent.substring(textView.getSelectionStart(), textView.getSelectionEnd()));
                articalOriContent.setHightLightWord(textView.getSelectionStart(), textView.getSelectionEnd());
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //清除下划线
                ds.setUnderlineText(false);
                //设置文本的颜色
                ds.setColor(App.mContext.getResources().getColor(R.color.colorLightBlack));
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //保存数据
        outState.putInt("level", Integer.valueOf(seekBar.getProgress()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        //清除
        articalOriContent.setHightLightWord(Integer.MAX_VALUE, Integer.MAX_VALUE);
        articalOriContent.setWordList(null);
        super.onDestroy();
    }
}
