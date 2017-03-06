package org.lgd.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.lgd.activity.MainActivity;
import org.lgd.activity.MyAppllication;
import org.lgd.adapter.LifeIndexAdapter;
import org.lgd.entry.Contacts;
import org.lgd.entry.Life;
import org.lgd.entry.LifeInfoData;
import org.lgd.entry.PM25;
import org.lgd.entry.RealTime;
import org.lgd.entry.Weather;
import org.lgd.entry.WeatherCodeWatched;
import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.MapUtils;
import org.lgd.util.NumberUtils;
import org.lgd.util.StringUtils;
import org.lgd.view.ObserverScrollView;
import org.lgd.view.SunRiseDownCircleView;
import org.lgd.view.TempTrendView;
import org.lgd.view.WeekWeatherResumeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lgd.org.R;

/**
 * 天气信息界面Fragment
 * Created by Bruce Wu on 2016/3/30.
 */
public class MainFragment extends Fragment implements WeatherCodeWatched {
    /**
     * Acticity想Fragment传输的城市名称key
     */
    public static final String CITY_KEY = "city";
    /**
     * 是否是定位的城市key
     */
    public static final String LOCATION_OR_NOT = "locationOrNot";

    public static final String POSITION = "position";
    /**
     *
     */
    public static final int SUN_RISRANDDOWN_INDEX = 5;
    /**
     * 温度曲线图
     */
    @InjectView(value = R.id.id_tempTrendView)
    private TempTrendView tempTrendView;
    /**
     * 日落日出图
     */
    @InjectView(value = R.id.id_sunRiseDownView)
    private SunRiseDownCircleView sunRiseDownCircleView;
    /**
     * 当前温度
     */
    @InjectView(value = R.id.id_text_temp)
    private TextView textNowTemp;
    /**
     * 当前天气
     */
    @InjectView(value = R.id.id_text_weather)
    private TextView textWeather;
    /**
     * 今天最高温和最低温
     */
    @InjectView(value = R.id.id_text_totle_temp)
    private TextView textTotalTemp;
    /**
     * 下拉刷新控件
     */
    @InjectView(value = R.id.id_fragment_swiperefresh)
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 一周天气概述
     */
    @InjectView(value = R.id.id_weekweather)
    private WeekWeatherResumeView weekWeatherResumeView;
    @InjectView(value = R.id.id_totle_content)
    private LinearLayout totalLayout;
    @InjectView(value = R.id.id_fullscreen)
    private LinearLayout linearLayout;
    /**
     * 风向
     */
    @InjectView(value = R.id.other_wind_direct)
    private TextView windDirect;
    /**
     * 风力
     */
    @InjectView(value = R.id.other_wind_power)
    private TextView windPower;
    /**
     * 紫外线强弱
     */
    @InjectView(value = R.id.other_ultraviolet_rays)
    private TextView ultravioletRays;
    /**
     * 湿度
     */
    @InjectView(value = R.id.other_humidity)
    private TextView humidity;
    /**
     * 空气质量的标题和相应的文字
     */
    @InjectView(value = R.id.air_con_title)
    private TextView airConTitle;
    @InjectView(value = R.id.air_con_text)
    private TextView airConText;
    /**
     * PM2.5的ProgressBar和数值
     */
    @InjectView(value = R.id.air_con_pm25)
    private ProgressBar airPm25;
    @InjectView(value = R.id.air_con_pm25_text)
    private TextView airPm25Text;
    /**
     * PM10的ProgressBar和数值
     */
    @InjectView(value = R.id.air_con_pm10)
    private ProgressBar airPm10;
    @InjectView(value = R.id.air_con_pm10_text)
    private TextView airPm10Text;
    /**
     * 生活指数列表
     */
    @InjectView(value = R.id.id_recycle_life_index)
    private RecyclerView mRecycleView;
    /**
     * 生活指数详情
     */
    @InjectView(value = R.id.id_linear_life_index_detail)
    private LinearLayout mLinearIndexDetail;
    /**
     * 指数详情图片
     */
    @InjectView(value = R.id.id_detail_img)
    private ImageView mIndexDetailImg;
    /**
     * 指数详情标题
     */
    @InjectView(value = R.id.id_detail_title)
    private TextView mIndexDetailTitle;
    /**
     * 指数详情信息
     */
    @InjectView(value = R.id.id_detail_info)
    private TextView mIndexDetailInfo;
    /**
     * 指数详情建议
     */
    @InjectView(value = R.id.id_detail_suggesstion)
    private TextView mIndexDetailSugg;
    /**
     * 当前页面ScrollView
     */
    @InjectView(value = R.id.id_scrollView)
    private ObserverScrollView scrollView;

    private String city;
    private int locationOrNot;
    private int position;
    private RequestQueue mQueue;
    private Gson mGson;
    /**
     * 生活指数数据
     */
    private LifeInfoData lifeInfoData;
    /**
     * mRecycleView的Adapter
     */
    private LifeIndexAdapter lifeIndexAdapter;
    /**
     * 是否进行调试
     */
    public boolean Debug = false;
    /**
     * 当前页面的weather的code
     */
    private int code = -1;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * add by wukai on 20160512 Scroll滑动监听器
     */
    public interface OnFragScrollChangeListener {
        void onFragScrollChange(int l, int t, int oldl, int oldt);
    }

    private OnFragScrollChangeListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = MyAppllication.getRequestQueue(getContext().getApplicationContext());
        mGson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        InjectUtils.injectView(this, view);
        View v = totalLayout.getChildAt(0);
        totalLayout.removeViewAt(0);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, metrics.heightPixels - 300);
        totalLayout.addView(v, 0, layoutParams);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        initEvents();
        Bundle b = this.getArguments();
        if (b != null) {
            this.city = b.getString(CITY_KEY);
            this.locationOrNot = b.getInt(LOCATION_OR_NOT);
            this.position = b.getInt(POSITION);
            if (null != this.city) {
                loadOrRefreshData(this.city);
            }
        }

    }

    /**
     * 初始化时间
     */
    private void initEvents() {
        scrollView.setOnScrollChangeListener(new ObserverScrollView.OnObScrollChangeListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {
                if (mListener != null) {
                    mListener.onFragScrollChange(l, t, oldl, oldt);
                }
            }
        });
    }

    /**
     * 初始化控件及下拉刷新以及指数详情
     */
    private void initWidgets() {
        swipeRefreshLayout.setColorSchemeColors(Color.MAGENTA, Color.MAGENTA, Color.WHITE, Color.RED, Color.LTGRAY);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //刷新数据
                loadOrRefreshData(MainFragment.this.city);
            }
        });

        mLinearIndexDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndexWidgetVisibityAndInfo(false, 0);
            }
        });
    }

    /**
     * 加载或者刷新页面
     */
    public void loadOrRefreshData(String city) {
        this.city = city;
        if (mQueue == null) {
            return;
        }
        String url = String.format(Contacts.WEATHER_INFO_URL, city);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyAppllication.saveData(s, position);
                setWidgetData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (Debug) {
                    Log.e("RequestErro", volleyError.getMessage());
                }
            }
        });
        request.setTag(city);
        mQueue.add(request);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * json数据解析并设置到相应的空间上
     *
     * @param json:获取的json数据
     */
    private void setWidgetData(final String json) {
        //先解析数据,再设置数据
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (0 == jsonObject.optInt(Contacts.ERROR_CODE)) {
                        //获取到了正确数据
                        JSONObject jsonObject1 = jsonObject.optJSONObject(Contacts.RESULT);
                        JSONObject jsonObject2 = jsonObject1.optJSONObject(Contacts.REALTIME);
                        //天气信息概括
                        RealTime realTime = mGson.fromJson(jsonObject2.toString(), RealTime.class);
                        textNowTemp.setText(realTime.getWeather().getTemperature() + "°");
                        textWeather.setText(realTime.getWeather().getInfo());
                        sunRiseDownCircleView.setTempeture(realTime.getWeather().getTemperature());
                        windDirect.setText(realTime.getWind().getDirect());
                        windPower.setText(realTime.getWind().getPower());
                        humidity.setText(realTime.getWeather().getHumidity() + "%");
                        code = Integer.parseInt(realTime.getWeather().getImg());
                        //通知主界面进行更新
                        MainFragment.this.notifyWatcherUpdate(code, position);
                        //生活指数
                        JSONObject jsonObject3 = jsonObject1.optJSONObject(Contacts.LIFE).optJSONObject(Contacts.INFO);
                        Life life = mGson.fromJson(jsonObject3.toString(), Life.class);
                        ultravioletRays.setText(life.getZiwaixian().get(0));
                        MainFragment.this.lifeInfoData = new LifeInfoData(life);
                        lifeIndexAdapter = new LifeIndexAdapter(getContext(), lifeInfoData);
                        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        mRecycleView.setAdapter(lifeIndexAdapter);
                        lifeIndexAdapter.setOnItemClickListener(new LifeIndexAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View v, int position) {
                                setIndexWidgetVisibityAndInfo(true, position);
                            }

                            @Override
                            public void onLongItemClickListener(View v, int position) {

                            }
                        });
                        //PM25信息
                        JSONObject jsonObject4 = jsonObject1.optJSONObject(Contacts.PM25);
                        PM25 pm25 = mGson.fromJson(jsonObject4.toString(), PM25.class);
                        PM25.Pm25Bean pm25Bean = pm25.getPm25();
                        int pm25Int = StringUtils.str2Int(pm25Bean.getPm25());
                        int pm10Int = StringUtils.str2Int(pm25Bean.getPm10());
                        int curPmInt = StringUtils.str2Int(pm25Bean.getCurPm());
                        setAirConColor(curPmInt);
                        airPm25.setProgress(pm25Int);
                        airPm10.setProgress(pm10Int);
                        airPm25Text.setText(pm25Int + "");
                        airPm10Text.setText(pm10Int + "");
                        airConText.setText(curPmInt + "  " + pm25Bean.getQuality());

                        List<Weather> weathers = mGson.fromJson(jsonObject1.optJSONArray(Contacts.WEATHER).toString(),
                                new TypeToken<List<Weather>>() {
                                }.getType());
                        weekWeatherResumeView.setWeathers(weathers);
                        Weather.InfoBean infoBean = weathers.get(0).getInfo();
                        sunRiseDownCircleView.setSunRiseAndDownTime(infoBean.getDay().get(SUN_RISRANDDOWN_INDEX),
                                infoBean.getNight().get(SUN_RISRANDDOWN_INDEX));

                        textTotalTemp.setText(infoBean.getDay().get(NumberUtils.WEATHER_TEMP_INDEX) + "°/ " + infoBean.getNight().get(NumberUtils.WEATHER_TEMP_INDEX) + "°C");

                        Map<String, List<Integer>> map = NumberUtils.getTemps(weathers);
                        tempTrendView.setTemps(MapUtils.getList(map, "highTemps"), MapUtils.getList(map, "lowTemps"));
                        //tempTrendView.startAnimation(700);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        t.run();
        handler.post(t);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 根据当前空气指数设置颜色
     */

    private void setAirConColor(int curPm) {
        int color = getContext().getResources().getColor(R.color.good_air);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.bg_progressbar_good_air);
        Drawable drawable1 = getContext().getResources().getDrawable(R.drawable.bg_progressbar_good_air_copy);
        if (curPm >= 0 && curPm <= 100) {   //优良

        } else if (curPm > 100 && curPm <= 300) {  //轻度污染  中度污染
            color = getContext().getResources().getColor(R.color.light_pollution);
            drawable = getContext().getResources().getDrawable(R.drawable.bg_progressbar_light_pollution);
            drawable1 = getContext().getResources().getDrawable(R.drawable.bg_progressbar_light_pollution_copy);
        } else if (curPm > 300) {   //重度污染
            color = getContext().getResources().getColor(R.color.heavy_pollution);
            drawable = getContext().getResources().getDrawable(R.drawable.bg_progressbar_heavy_pollution);
            drawable1 = getContext().getResources().getDrawable(R.drawable.bg_progressbar_light_pollution_copy);
        }
        airPm10.setProgressDrawable(drawable);
        airPm25.setProgressDrawable(drawable1);
        airConTitle.setTextColor(color);
        airConText.setTextColor(color);
    }

    /**
     * @param isDetail :是否切换到了详情   true：详情   false：概述
     */
    private void setIndexWidgetVisibityAndInfo(boolean isDetail, int position) {
        if (isDetail) {
            mLinearIndexDetail.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
            mIndexDetailImg.setImageDrawable(getResources().getDrawable(lifeInfoData.getLifeIndexImgs().get(position)));
            mIndexDetailTitle.setText(lifeInfoData.getLifeIndexNames().get(position));
            mIndexDetailInfo.setText(lifeInfoData.getLifeIndexRes().get(position));
            mIndexDetailSugg.setText(lifeInfoData.getLifeIndexSuggess().get(position));

        } else {
            mLinearIndexDetail.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    public SunRiseDownCircleView getSunRiseDownCircleView() {
        return sunRiseDownCircleView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 为Fragment设置标签
     *
     * @return
     */
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ObserverScrollView getScrollView() {
        return this.scrollView;
    }

    public int getCode() {
        return code;
    }

    public void setOnFragScrollChangeListener(@NonNull OnFragScrollChangeListener mListener) {
        this.mListener = mListener;
    }

    private MainActivity watcher;

    @Override
    public void addWatcher(MainActivity watcher) {
        this.watcher = watcher;
    }

    @Override
    public void removeWatcher(MainActivity watcher) {
        this.watcher = null;
    }

    @Override
    public void notifyWatcherUpdate(int code, int position) {
        if (this.watcher != null) {
            watcher.updateWeaAnim(code, position);
        }
    }

}
