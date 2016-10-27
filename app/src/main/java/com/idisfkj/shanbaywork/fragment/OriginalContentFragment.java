package com.idisfkj.shanbaywork.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.idisfkj.shanbaywork.R;

/**
 * 文章原文界面
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
    }
}
