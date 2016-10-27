package com.idisfkj.shanbaywork.fragment;

import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.idisfkj.shanbaywork.App;
import com.idisfkj.shanbaywork.R;

import java.text.BreakIterator;
import java.util.Locale;

/**
 * 原文界面
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class OriginalContentFragment extends Fragment {
    private TextView articalOriContent;
    private SeekBar seekBar;
    private Switch aSwitch;
    private TextView textLevel;
    private String originalContent;

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
        return view;
    }

    public void initData(View view) {
        articalOriContent = (TextView) view.findViewById(R.id.article_original_content);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        aSwitch = (Switch) view.findViewById(R.id.switch_bar);
        textLevel = (TextView) view.findViewById(R.id.text_level);
        originalContent = getArguments().getString("originalContent", "");
        articalOriContent.setText(originalContent);
        clickOnTheHighLighted(originalContent, articalOriContent);
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
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spannable.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //高亮点击事件处理
                TextView textView = (TextView) widget;
                //获取高亮文本
                textView.getText().subSequence(textView.getSelectionStart(), textView.getSelectionEnd()).toString();
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
}
