package org.lgd.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;

import org.lgd.adapter.MainViewPagerAdapter;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.entry.WeatherCodeWatcher;
import org.lgd.fragment.CityManageFragment;
import org.lgd.fragment.MainFragment;
import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.DBUtils;
import org.lgd.util.LocationUtil;
import org.lgd.util.SDCardUtil;
import org.lgd.util.ShareUtils;
import org.lgd.util.StatusBarUtil;
import org.lgd.util.StringUtils;
import org.lgd.util.TimeUtils;
import org.lgd.util.WeatAnimChangeUtils;
import org.lgd.util.listener.OnLocateFinsihListener;
import org.lgd.view.ObserverScrollView;
import org.lgd.view.SunRiseDownCircleView;

import java.util.ArrayList;
import java.util.List;

import lgd.org.R;

public class MainActivity extends AppCompatActivity implements WeatherCodeWatcher {
    //应用ActionBar
    @InjectView(value = R.id.toolbar)
    private Toolbar mToolbar;
    //TitleBar上的日期城市显示
    @InjectView(value = R.id.id_titlebar_date)
    private TextView titleBarDate;
    @InjectView(value = R.id.id_titlebar_week)
    private TextView titleBarWeek;
    @InjectView(value = R.id.id_main_city_name)
    private TextView titleBarCity;
    @InjectView(value = R.id.id_imgbtn_share)
    private ImageButton titleShare;
    //DrawerLayout
    @InjectView(value = R.id.id_drawlayout)
    private DrawerLayout mDrawerLayout;
    //天气动画遮罩
    @InjectView(value = R.id.id_weather_vedio_shade)
    private FrameLayout mVedioShade;
    //主页面ViewPager
    @InjectView(value = R.id.id_viewPager)
    private ViewPager mViewPager;
    //天气动画
    @InjectView(value = R.id.id_weather_vedio)
    private SurfaceView surfaceView;
    //等待框
    @InjectView(value = R.id.id_wati_dialog)
    private FrameLayout dialog;
    //透明度和状态栏颜色
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;
    //定位工具
    private LocationUtil locationUtil;
    //判断页面定位加载是否为第一次
    private static boolean isFirst = true;
    //判断定位数据是否是第一次插入
    private static boolean dbLocal = true;
    //数据库工具
    private DBUtils dbUtils;
    //viewPager每个城市天气信息页面Fragment
    private List<MainFragment> mFragmentList;
    //ViewPager的FragmentPagerAdapter
    private MainViewPagerAdapter pagerAdapter;
    //ViewPager 老的偏移量
    private float oldOffset;
    //DisplayMetrics
    private DisplayMetrics mMetrics;
    //天气动画播放工具
    private WeatAnimChangeUtils animUtils;
    public static final int REQUEST_CODE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.injectView(this);
        titleBarDate.setText(TimeUtils.getDate(System.currentTimeMillis()));
        titleBarWeek.setText(TimeUtils.getWeek(System.currentTimeMillis()));
        initStatusbar();       //初始化状态栏
        initBackGround();      //初始化背景
        initDbUtils();        //初始化DBUtils
        initViewPager();       //初始化ViewPager
        initLocation();       //初始化定位
        initEvents();         //初始化事件
    }

    /**
     * 初始化事件
     */
    private void initEvents() {

        titleShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.VISIBLE);
                shareWeather(mFragmentList.get(mViewPager.getCurrentItem()).getScrollView());
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                translateToolbar(positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                titleBarCity.setText(MyAppllication.cities.get(position).getCityName());
                if (position == 0) {
                    locationUtil.startLocation(true);      //页面为0时定位
                    mToolbar.setLogo(getResources().getDrawable(R.drawable.icon_location_white));
                } else {
                    locationUtil.stopLocation();
                    mToolbar.setLogo(null);
                }
                int code = mFragmentList.get(position).getCode();
                animUtils.setCode(code);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    mToolbar.setAlpha(1f);
                }
            }
        });
        //为每一页的ScrollView设置监听用来改变背景情况
        for (MainFragment fragment : mFragmentList) {
            fragmentSetListener(fragment);
        }
    }

    /**
     * 为每个Fragment设置监听器
     *
     * @param fragment : 每个fragment
     */
    private void fragmentSetListener(MainFragment fragment) {
        fragment.setOnFragScrollChangeListener(new MainFragment.OnFragScrollChangeListener() {
            @Override
            public void onFragScrollChange(int l, int t, int oldL, int oldT) {
                //根据T来判断如今的高度
                int h = mMetrics.heightPixels - 1000;
                //Log.i("OnFragChange", "h: " + h + "  l:" + l + " ####  t:" + t + "  ###  oldL:" + oldL + "  ###  oldT:" + oldT);
                if (t >= h) {
                    mVedioShade.setAlpha(1f);
                    //surfaceView.setAlpha(0f);
                } else {
                    float x = t;
                    float y = h;
                    //surfaceView.setAlpha(x / y);
                    mVedioShade.setAlpha(x / y);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contacts.MAINACTIVITY_UI_ACTION);
    }

    /**
     * @param item :Recycle侧边栏菜单点击事件
     */
    public void onClickEvents(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_city_manage:   //城市管理
                //suspendFlg = true;
                Intent intent = new Intent(this, CityManageActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_setting:
                Toast.makeText(this, "功能开发中...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_notepad:
                Toast.makeText(this, "功能开发中...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    private boolean suspendFlg = false;   //判断当前是白天还是夜晚的进程
    private Thread t;

    /**
     * 初始化背景颜色
     * 实时监听当前的时间
     */
    private void initBackGround() {
        t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (suspendFlg) {
                            wait();
                        }
                        handler.sendEmptyMessage(0);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        //开始播放后台动画
        animUtils = new WeatAnimChangeUtils(this, surfaceView);
    }

    /**
     * 初始化数据库类
     */
    private void initDbUtils() {
        try {
            CityEntry entry = new CityEntry();
            String path = SDCardUtil.getStorepath(this);
            if (path != null) {
                dbUtils = DBUtils.create(this, Contacts.DB_NAME, path);
            } else {
                dbUtils = DBUtils.create(this, Contacts.DB_NAME);
            }
            dbUtils.save(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主线程调用数据库
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long currentTime = TimeUtils.getTimeMillis(System.currentTimeMillis());
            SunRiseDownCircleView view = mFragmentList.get(mViewPager.getCurrentItem()).getSunRiseDownCircleView();
            if (null != view) {
                long riseTime = TimeUtils.getTimeMillis(view.getSunRiseTime());
                long downTime = TimeUtils.getTimeMillis(view.getSunDownTime());
                if (currentTime >= riseTime && currentTime <= downTime) {  //白天
                    mVedioShade.setBackgroundResource(R.color.sun_day_backcolor);
                    animUtils.setDayOrNight(true);
                } else {  //晚上
                    mVedioShade.setBackgroundResource(R.color.sun_night_backcolor);
                    animUtils.setDayOrNight(false);
                }
            }

        }
    };

    /**
     * 初始化定位
     */
    private void initLocation() {
        locationUtil = new LocationUtil(this);
        locationUtil.startLocation(true);
        locationUtil.setOnLocateFinishListener(new OnLocateFinsihListener() {
            @Override
            public void onLocateFinish(AMapLocation location) {
                String city;
                if (location.getErrorCode() == 0) {
                    Log.i("Info", "定位成功");
                    city = location.getCity();
                } else {
                    Log.i("Location Error", location.getErrorInfo());
                    city = "北京";
                }
                city = StringUtils.delString(city, "市");
                CityEntry entry = new CityEntry();
                entry.setCityName(city);
                entry.setLocationOrNot(1);
                entry.setSortId(0);
                MyAppllication.saveCity(entry, 0);
                if (dbLocal && location.getErrorCode() == 0) {
                    locationUtil.setLocationInterval(30000);
                    dbUtils.insertOrUpdate(entry, new String[]{"locationOrNot"}, new String[]{"1"});
                }
                if (mViewPager.getCurrentItem() != 0) {
                    isFirst = true;
                } else {
                    titleBarCity.setText(city);
                }
                if (isFirst) {
                    MainFragment fragment = (MainFragment) pagerAdapter.getItem(0);
                    fragment.loadOrRefreshData(city);
                }
                isFirst = false;
                dbLocal = false;
            }
        });
    }

    /**
     * 初始化ViewPager
     * 此处应该是从数据库中获取存储的城市信息
     */
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        List<CityEntry> cities = dbUtils.findAll(CityEntry.class);
        if (null != cities && cities.size() > 0) {
            MyAppllication.setCityNum(cities.size());
            MyAppllication.cities = cities;
            mViewPager.setOffscreenPageLimit(cities.size() - 1);
            titleBarCity.setText(cities.get(0).getCityName());
            int i = 0;
            for (CityEntry entry : cities) {
                MainFragment f = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MainFragment.CITY_KEY, entry.getCityName());
                bundle.putInt(MainFragment.LOCATION_OR_NOT,
                        entry.getLocationOrNot());
                bundle.putInt(MainFragment.POSITION, i);
                f.setArguments(bundle);
                f.addWatcher(this);
                mFragmentList.add(f);
                MyAppllication.saveCity(entry, i);
                i++;
            }
        } else {
            MyAppllication.setCityNum(0);
            MainFragment f = new MainFragment();
            mFragmentList.add(f);
        }
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                mFragmentList);
        mViewPager.setAdapter(pagerAdapter);
    }

    /**
     * @param positionOffset       : 偏移百分比
     * @param positionOffsetPixels : 偏移像素值
     */
    private void translateToolbar(float positionOffset, int positionOffsetPixels) {
        if (positionOffset > oldOffset) {   //向左滑动
            if (positionOffset >= 0.5) {   //大于0.5
                mToolbar.setAlpha((float) ((positionOffset - 0.5) * 2));
                mToolbar.setTranslationX(mMetrics.widthPixels - positionOffsetPixels);
            } else {   //小于0.5
                mToolbar.setAlpha((float) ((0.5 - positionOffset) * 2));
                mToolbar.setTranslationX(-positionOffsetPixels);
            }

        } else if (positionOffset < oldOffset) {  //向右滑动
            if (positionOffset >= 0.5) {
                mToolbar.setAlpha((float) ((positionOffset - 0.5) * 2));
                mToolbar.setTranslationX(mMetrics.widthPixels - positionOffsetPixels);
            } else {
                mToolbar.setAlpha((float) ((0.5 - positionOffset) * 2));
                mToolbar.setTranslationX(-positionOffsetPixels);
            }
        }
        if (positionOffset == 0) {
            mToolbar.setAlpha(1f);
        }

        this.oldOffset = positionOffset;
    }

    /**
     * 初始化状态栏
     */
    private void initStatusbar() {

        mToolbar.setLogo(this.getResources().getDrawable(R.drawable.icon_location_white));
        setSupportActionBar(mToolbar);
        /**
         * ActionBarDrawerToggle
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, mDrawerLayout, mAlpha);
    }

    /**
     * 从城市管理页面返回时进行数据的处理
     *
     * @param requestCode : 请求码
     * @param resultCode  : 返回码
     * @param data        ：返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        suspendFlg = false;
//        t.notify();
        Log.i("thread状态:", t.getState().name());
        // t.interrupt();
        injustViewpager(resultCode, data);
    }

    /**
     * add by wukai on 20160512
     * 功能:从城市管理页面返回时进行页面的调整
     */
    private void injustViewpager(int resultCode, Intent data) {
        List<CityEntry> currentCities = MyAppllication.cities;
        if (currentCities != null) {
            int curNums = currentCities.size();
            int fragNums = mFragmentList.size();
            if (fragNums < curNums) {
                for (int i = fragNums; i < curNums; i++) {
                    CityEntry entry = currentCities.get(i);
                    MainFragment fragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(MainFragment.CITY_KEY, entry.getCityName());
                    bundle.putInt(MainFragment.POSITION, i);
                    fragment.setArguments(bundle);
                    fragment.addWatcher(this);
                    fragmentSetListener(fragment);
                    mFragmentList.add(fragment);
                }
                pagerAdapter.notifyDataSetChanged();
            } else if (fragNums > curNums) {
                String newCities = currentCities.toString();
                for (int i = mFragmentList.size() - 1; i >= 0; i--) {  //将主界面的删除的城市删除掉
                    String name = mFragmentList.get(i).getCity();
                    if (!TextUtils.isEmpty(name)) {
                        if (!newCities.contains(name)) {
                            mFragmentList.remove(i);
                        }
                    }
                }
                pagerAdapter.notifyDataSetChanged();
            }
            //查找相应得位置调换顺序加载数据
            for (int i = 0; i < curNums; i++) {
                CityEntry entry = currentCities.get(i);
                String name = entry.getCityName();
                MainFragment fragment = mFragmentList.get(i);
                fragment.setPosition(i);    //对当前的position重新进行调整
                if (!name.equals(fragment.getCity())) {
                    fragment.setCity(name);
                    fragment.loadOrRefreshData(name);
                }
            }
            mViewPager.setOffscreenPageLimit(curNums - 1);
            if (resultCode == Contacts.ADD_CITY) {
                if (data != null) {
                    mViewPager.setCurrentItem(curNums - 1);
                } else {
                    mViewPager.setCurrentItem(0);
                }
            } else if (resultCode == CityManageFragment.MANAGE_TO_MAIN_RESULT_CODE) {
                int position = data.getIntExtra(CityManageFragment.ITEM_CLICK_POSITION, 0);
                titleBarCity.setText(currentCities.get(position).getCityName());
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //setCititesDatabase();
        //对定位的变量进行销毁
        locationUtil.getLocationClient().onDestroy();
        locationUtil.setLocationOption(null);
        locationUtil.setLocationClient(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //更新后台数据库信息
        setCititesDatabase();
    }

    /**
     * alter by wukai on 20160511
     * 设置后台的数据库
     * 在退出城市之前调整数据库中的数据
     * 1.先清空，2.后再将数据填充到数据库中
     */
    private void setCititesDatabase() {
        //根据全局变量来调整数据库
        dbUtils.deleteAll(CityEntry.class);
        List<CityEntry> cityEntries = MyAppllication.cities;
        for (CityEntry entry : cityEntries) {
            dbUtils.insert(entry);
        }
    }

    /**
     * add by wukai on 20160514
     * 功能：调用系统分享功能，进行分享
     */
    private void shareWeather(ObserverScrollView scrollView) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        String text = MyAppllication.cities.get(mViewPager.getCurrentItem()).getCityName();
        Bitmap bitmap = ShareUtils.getBitmapByView(scrollView, Color.parseColor("#509bd1"), text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(SDCardUtil.storeBitmapToFile(bitmap, this, "weather.png")));
        intent.putExtra(Intent.EXTRA_TEXT, "分享自--简约天气");
        startActivity(Intent.createChooser(intent, "分享"));
    }

    @Override
    public void updateWeaAnim(int code, int position) {
        if (mViewPager.getCurrentItem() == position) {
            animUtils.setCode(code);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //当此Activity极易被杀死时保存当前的数据

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int isVisibel = dialog.getVisibility();
        if (isVisibel == View.VISIBLE && !hasFocus) {
            dialog.setVisibility(View.GONE);
        }
    }
}
