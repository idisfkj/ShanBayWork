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
 * 文章译文界面
 * Created by idisfkj on 16/10/27.
 * Email : idisfkj@qq.com.
 */
public class TranslationFragment extends Fragment {
    private TextView articleTranslation;

    public static TranslationFragment getInstance(String translation) {
        Bundle bundle = new Bundle();
        bundle.putString("translation", translation);
        TranslationFragment fragment = new TranslationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, null);
        articleTranslation = (TextView) view.findViewById(R.id.article_translation);
        String translation = getArguments().getString("translation", "");
        articleTranslation.setText(translation);
        return view;
    }
}
