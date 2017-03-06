package org.lgd.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import org.lgd.fragment.MainFragment;

import java.util.List;

/**
 * Created by wukai on 2016/4/25.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * 数据源
     */
    private List<MainFragment> listFragment;

    public MainViewPagerAdapter(FragmentManager fm, List<MainFragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setArguments(List<MainFragment> listFragment) {
        this.listFragment = listFragment;
        this.notifyDataSetChanged();
    }
}
