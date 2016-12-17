package com.luclx.myindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by LucLX on 12/17/16.
 */

public class FragmentItem extends Fragment {
    private LinearLayout mMain;
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FragmentItem newInstance(int page, String title) {
        FragmentItem fragmentFirst = new FragmentItem();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        int[] pagerColor = getContext().getResources().getIntArray(R.array.pager);
        LinearLayout mMain = (LinearLayout) view.findViewById(R.id.main);
        mMain.setBackgroundColor(pagerColor[page]);
        TextView tvLabel = (TextView) view.findViewById(R.id.title);
        tvLabel.setText(page + " -- " + title);

        return view;
    }
}
