package com.example.k.newsgateway;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by K on 4/30/2017.
 */

public class ArticleFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final ArticleFragment newInstance(String message)
    {
        ArticleFragment f = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.article_fragment_layout, container, false);
        TextView messageTextView = (TextView)v.findViewById(R.id.title);
        messageTextView.setText(message);

        return v;
    }

}
