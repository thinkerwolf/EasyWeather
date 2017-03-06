package org.lgd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lgd.activity.MyAppllication;
import org.lgd.adapter.CitiesAddAdapter;
import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.DBUtils;
import org.lgd.util.FileUtils;
import org.lgd.view.SideBar;
import org.lgd.view.TopView;

import java.util.List;

import lgd.org.R;

/**
 * <p>
 * ContentDescription:添加城市Fragment
 * Copyright
 * </p>
 * Created by wukai on 2016/5/2.
 */
public class CityAddFragment extends Fragment {

    /**
     * 当前城市的字母
     */
    @InjectView(value = R.id.id_city_add_current_letter)
    private TextView mTextCurrentLetter;
    /**
     * 所有的城市列表 包括热门城市和普通城市
     */
    @InjectView(value = R.id.id_listview_allcity)
    private ListView mAllCitiesView;
    /**
     * 滑动字母选择时当前选中的字母
     */
    @InjectView(value = R.id.id_city_add_touch_letter)
    private TextView mTextTouchLetter;
    /**
     * 字母滑动区
     */
    @InjectView(value = R.id.id_city_add_sidebar)
    private SideBar mSiderBar;

    private String[] cityChar = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    //Gson数据解析
    private Gson mGson;
    //城市数据的adapter
    private CitiesAddAdapter addAdapter;
    //所有的城市数据
    private List<AddCityBean> allCities;

    private List<CityEntry> choosedCities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_add, container, false);
        InjectUtils.injectView(this, view);
        init();
        return view;
    }

    private void init() {
        mSiderBar.setTextViewDialog(mTextTouchLetter);
        mSiderBar.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter, int index) {
                if (addAdapter != null) {
                    //Log.i("CityAddFragment - >letterPos:", addAdapter.getLetterPosMap().toString());
                    Integer pos = addAdapter.getLetterPosMap().get(letter);
                    if (pos != null) {
                        mAllCitiesView.setSelection(pos.intValue() + 1);
                        mTextCurrentLetter.setText(cityChar[index]);
                    }
                }
            }
        });

        mAllCitiesView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mTextCurrentLetter.setVisibility(View.GONE);
                    mTextCurrentLetter.setText("A");
                } else {
                    mTextCurrentLetter.setVisibility(View.VISIBLE);
                    mTextCurrentLetter.setText(String.valueOf(allCities.get(firstVisibleItem - 1).getPinyin().charAt(0)));
                }

            }
        });

        /**
         * add by wukai on 20160512
         * 用于处理城市添加点击事件
         */
        mAllCitiesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前点击的城市是否已经是选择的
                if (choosedCities != null && allCities != null) {
                    AddCityBean addBean = allCities.get(position - 1);
                    String choosedStrs = choosedCities.toString();
                    if (choosedStrs.contains(addBean.getName())) {
                        //如果存在直接退出
                        getActivity().finish();
                    } else {
                        //如果不存在 ，添加城市
                        CityEntry entry = new CityEntry();
                        entry.setCityName(addBean.getName());
                        entry.setSortId(0);
                        entry.setLocationOrNot(0);
                        MyAppllication.cities.add(entry);
                        MyAppllication.weatherDatas.add(null);
                        Intent intent = new Intent();
                        getActivity().setResult(Contacts.ADD_CITY, intent);
                        getActivity().finish();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String allJson = FileUtils.fromFile(getContext(), R.raw.all_cities_data);
        if (allJson != null) {
            allCities = mGson.fromJson(allJson, new TypeToken<List<AddCityBean>>() {

            }.getType());
            choosedCities = MyAppllication.cities;
            addAdapter = new CitiesAddAdapter(getContext(), choosedCities, allCities);
            mAllCitiesView.setAdapter(addAdapter);
            TopView topView = new TopView(getContext());
            mAllCitiesView.addHeaderView(topView);
        }
    }

}
