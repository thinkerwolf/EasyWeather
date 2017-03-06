package org.lgd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lgd.adapter.SearchCityAdapter;
import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.fragment.CityAddFragment;
import org.lgd.fragment.CityManageFragment;
import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.FileUtils;
import org.lgd.util.StatusBarUtil;
import org.lgd.view.CleatEditText;

import java.util.List;

import lgd.org.R;

/**
 * 城市管理Activity
 * Created by wukai on 2016/4/18.
 */
public class CityManageActivity extends AppCompatActivity {

    /**
     * 状态栏
     */
    @InjectView(value = R.id.id_city_manage_toolbar)
    private Toolbar mToolbar;

    /**
     * SearchView视图
     */
    @InjectView(value = R.id.id_city_manage_search)
    private CleatEditText mSearchView;
    /**
     * activity名称
     */
    @InjectView(value = R.id.id_city_manage_back_second)
    private TextView mTextTitle;
    /**
     * 添加城市FloatingButton
     */
    @InjectView(value = R.id.id_floating_add_city)
    private FloatingActionButton mFloatBtnAdd;
    /**
     * 搜索结果
     */
    @InjectView(value = R.id.id_city_manage_searchresult)
    private ListView mResListView;
    /**
     * add on 20160512
     * 判断当前页面是第一Fragment还是第二是Fragment
     * true : 第二个Fragment   false：第一个Fragment
     */
    private boolean b = false;

    private Gson mGson;

    private SearchCityAdapter adapter;

    private List<AddCityBean> allCities;

    private List<CityEntry> choosedCities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        InjectUtils.injectView(this);
        mGson = new Gson();
        initStatusBar();
        init();
        initEvents();
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        mSearchView.setOnTextChangeListener(new CleatEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence text, boolean isnull) {
                if (isnull) {
                    mResListView.setVisibility(View.GONE);
                } else {
                    mResListView.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(text);
                }
            }
        });

        mResListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前点击的城市是否已经是选择的
                List<AddCityBean> filterCityBeans = adapter.getCityBeanList();
                if (choosedCities != null && filterCityBeans != null) {
                    if (position >= filterCityBeans.size()) {
                        return;
                    }
                    AddCityBean addBean = adapter.getCityBeanList().get(position);
                    String choosedStrs = choosedCities.toString();
                    if (choosedStrs.contains(addBean.getName())) {
                        //如果存在直接退出
                        CityManageActivity.this.finish();
                    } else {
                        //如果不存在 ，添加城市
                        CityEntry entry = new CityEntry();
                        entry.setCityName(addBean.getName());
                        entry.setSortId(0);
                        entry.setLocationOrNot(0);
                        MyAppllication.cities.add(entry);
                        MyAppllication.weatherDatas.add(null);
                        Intent intent = new Intent();
                        CityManageActivity.this.setResult(Contacts.ADD_CITY, intent);
                        CityManageActivity.this.finish();
                    }
                }
            }
        });
    }

    private void init() {
        cityManageFragment = new CityManageFragment();
        cityAddFragment = new CityAddFragment();
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.id_city_fragment_content, cityManageFragment);
        ft.commit();
        String allJson = FileUtils.fromFile(this, R.raw.all_cities_data);
        if (allJson != null) {
            allCities = mGson.fromJson(allJson, new TypeToken<List<AddCityBean>>() {

            }.getType());
            choosedCities = MyAppllication.cities;
            adapter = new SearchCityAdapter(this, allCities, choosedCities);
            mResListView.setAdapter(adapter);
        }
    }

    private CityManageFragment cityManageFragment;
    private CityAddFragment cityAddFragment;

    /**
     * 切换Fragment
     *
     * @param b true:切换到第二个   false:切换到初始的
     */
    private void switchFragments(boolean b) {
        this.b = b;
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (b) {
            mSearchView.setVisibility(View.VISIBLE);
            mTextTitle.setVisibility(View.GONE);
            mFloatBtnAdd.setVisibility(View.GONE);
            ft.replace(R.id.id_city_fragment_content, cityAddFragment);
        } else {
            ft.replace(R.id.id_city_fragment_content, cityManageFragment);
            mFloatBtnAdd.setVisibility(View.VISIBLE);
            mSearchView.setVisibility(View.GONE);
            mTextTitle.setVisibility(View.VISIBLE);
        }
        ft.commit();
    }

    /**
     * 设置点击事件
     *
     * @param v
     */
    public void onClickEvents(View v) {
        switch (v.getId()) {
            case R.id.id_city_manage_back:
                if (b) {
                    switchFragments(false);
                } else {
                    this.finish();
                }
                break;
            case R.id.id_city_manage_back_second:
                if (b) {
                    switchFragments(false);
                } else {
                    this.finish();
                }
                break;
            case R.id.id_floating_add_city:
                //TODO 调出搜索栏,为FloatingBtn添加动画
                switchFragments(true);
                break;
        }
    }


    /**
     * 初始化状态栏
     */
    private void initStatusBar() {
        setSupportActionBar(mToolbar);
        StatusBarUtil.setTranslucent(this, 0);
    }

    /**
     * 处理系统返回键逻辑
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (b) {
                switchFragments(false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
