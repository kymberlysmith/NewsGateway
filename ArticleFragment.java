package com.example.k.newsgateway;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by K on 4/30/2017.
 */

public class ArticleFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_MESSAGE2 = "EXTRA_MESSAGE2";
    public static final String TAG="ArticleFragment";

    public static final ArticleFragment newInstance(String message)
    {
        ArticleFragment f = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        bdl.putString(EXTRA_MESSAGE2, "ok plz");
        f.setArguments(bdl);

        Log.d(TAG, " newInstance, message value: " + message);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);
        String message2 = getArguments().getString(EXTRA_MESSAGE2);

        View v = inflater.inflate(R.layout.article_fragment_layout, container, false);
        TextView title = (TextView)v.findViewById(R.id.title);
        title.setText(message);


        TextView date=(TextView)v.findViewById(R.id.date);
        date.setText("5/5/2017");

        TextView summary=(TextView)v.findViewById(R.id.summary);
        summary.setText(message2);

        return v;
    }

}
