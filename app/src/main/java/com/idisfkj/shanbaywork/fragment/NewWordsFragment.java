package com.idisfkj.shanbaywork.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idisfkj.shanbaywork.R;

/**
 * 文章生词界面
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class NewWordsFragment extends Fragment {
    private TextView articleNewWords;

    public static NewWordsFragment getInstance(String newWords) {
        Bundle bundle = new Bundle();
        bundle.putString("newWords", newWords);
        NewWordsFragment fragment = new NewWordsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_words, null);
        articleNewWords = (TextView) view.findViewById(R.id.article_new_words);
        String newWords = getArguments().getString("newWords", "");
        articleNewWords.setText(newWords);
        return view;
    }
}
