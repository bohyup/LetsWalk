package com.hyupb.letswalk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by alfo6-18 on 2018-04-25.
 */

public class MyAdapter extends FragmentPagerAdapter {

    Fragment[] frags = new Fragment[4];

    public MyAdapter(FragmentManager fm) {
        super(fm);

        frags[0] = new Today();
        frags[1] = new Report();
        frags[2] = new WalkingTrails();
        frags[3] = new More();
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
