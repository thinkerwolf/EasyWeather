package org.lgd.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import org.lgd.entry.CityEntry;
import org.lgd.fragment.MainFragment;

import java.util.Collections;
import java.util.List;
import java.util.jar.Manifest;

/**
 * 主界面ViewPager的Adapter
 * Created by wukai on 2016/4/24.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    /**
     * 数据源
     */
    private List<MainFragment> listFragment;

    private List<CityEntry> cityEntries;
    /**
     * FragmentManager
     */
    private FragmentManager manager;

    private FragmentTransaction transaction;

    private boolean needReload = false;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        this.manager = fm;
    }

    public MainPagerAdapter(FragmentManager fm, List<MainFragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
        this.manager = fm;
    }

    public MainPagerAdapter(FragmentManager fm, List<MainFragment> listFragment, List<CityEntry> cityEntries) {
        super(fm);
        this.listFragment = listFragment;
        this.cityEntries = cityEntries;
        this.manager = fm;
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
    public long getItemId(int position) {
        return POSITION_NONE;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MainFragment fragment = (MainFragment) super.instantiateItem(container, position);
        if (fragment != null && needReload) {
            //String tag = fragment.getTag();
            FragmentTransaction ft = manager.beginTransaction();
            //ft.remove(fragment);
            fragment.loadOrRefreshData(cityEntries.get(position).getCityName());
            //ft.add(container.getId(), fragment, tag);
            //ft.attach(fragment);
            //ft.commitAllowingStateLoss();
        }

        return fragment;
    }

    /**
     * 移除缓存中的Fragment
     *
     * @param container
     * @param position
     */
    public void removeFragment(ViewGroup container, int position) {
        if (transaction == null) {
            transaction = manager.beginTransaction();
        }
        String name = getFragmentName(container.getId(), position);
        Fragment fragment = manager.findFragmentByTag(name);
        transaction.remove(fragment);
        transaction.commit();
        manager.executePendingTransactions();
    }

    private String getFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    /**
     * 清空数据并替换最新的数据
     *
     * @param fragments :最新的数据
     */
    public void setFragments(@NonNull List<MainFragment> fragments, List<CityEntry> cityEntries) {
        this.listFragment = fragments;
        this.cityEntries = cityEntries;
        notifyDataSetChanged();
    }

    /**
     * 用来删除某个城市
     *
     * @param whereId :需要删除城市的位置
     */
    public void setFragments(int whereId) {

    }

    /**
     * 用于数据交换
     *
     * @param from
     * @param to
     */
    public void setFragments(int from, int to) {
        if (listFragment != null)
            Collections.swap(this.listFragment, from, to);
        if (cityEntries != null)
            Collections.swap(this.cityEntries, from, to);
        notifyDataSetChanged();
    }

    public void setNeedReload(boolean needReload) {
        this.needReload = needReload;
    }
}
