package com.luclx.myindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.luclx.lxindicator.IndicatorView;
import com.luclx.lxindicator.PageLessException;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPage;
    IndicatorView mIndicator;
    MyPagerAdapter mAdapter;
    public int numPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPage = (ViewPager) findViewById(R.id.viewPager);
        mIndicator = (IndicatorView) findViewById(R.id.indicator);

        numPager = 5;
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), numPager);
        mViewPage.setAdapter(mAdapter);
        try {
            mIndicator.setViewPage(mViewPage);
        } catch (PageLessException ex) {
            ex.printStackTrace();
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private int mCount;

        public MyPagerAdapter(FragmentManager fragmentManager, int count) {
            super(fragmentManager);
            this.mCount = count;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mCount;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return FragmentItem.newInstance(position, "Page #" + position);
        }
    }
}
