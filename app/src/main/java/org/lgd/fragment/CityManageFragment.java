package org.lgd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.lgd.activity.MyAppllication;
import org.lgd.adapter.CitiesListAdapter;
import org.lgd.adapter.ItemTouchHelperCallback;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.entry.RealTime;
import org.lgd.entry.Weather;
import org.lgd.ioc.InjectUtils;
import org.lgd.ioc.annonation.InjectView;
import org.lgd.util.NumberUtils;

import java.util.List;

import lgd.org.R;

/**
 * 城市管理Fragment
 * Created by wukai on 2016/5/2.
 */
public class CityManageFragment extends Fragment {

    public static final String ITEM_CLICK_POSITION = "click_position";
    public static final int MANAGE_TO_MAIN_RESULT_CODE = 11;

    /**
     * 已选的城市列表
     */
    @InjectView(value = R.id.id_recycle_citylist)
    private RecyclerView mRecycleView;
    private CitiesListAdapter citiesListAdapter;

    private List<String> jsons;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_manage, container, false);
        InjectUtils.injectView(this, view);
        return view;
    }


    private void init() {
        jsons = MyAppllication.getDatas();
        for (int i = 0; i < jsons.size(); i++) {
            CityEntry city = MyAppllication.cities.get(i);
            if (TextUtils.isEmpty(jsons.get(i))) {
                final int in = i;
                StringRequest request = new StringRequest(String.format(Contacts.WEATHER_INFO_URL, city.getCityName()),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                jsons.set(in, s);
                                MyAppllication.saveData(s, in);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                MyAppllication.getRequestQueue(getContext()).add(request);
            }
        }

        citiesListAdapter = new CitiesListAdapter(getContext(), jsons);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(citiesListAdapter);
        //为RecycleView添加Item手势控制方法
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(citiesListAdapter);
        callback.setOnItemTouchHelperListener(citiesListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycleView);
    }

    /**
     * 为RecyclerView头部添加定位城市
     */
    private void initHeader() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cities_list_item, null);
        TextView textCity = (TextView) view.findViewById(R.id.cities_list_item_cityName);
        final TextView textWeather = (TextView) view.findViewById(R.id.cities_list_item_weather);
        final TextView textTemp = (TextView) view.findViewById(R.id.cities_list_item_temp);
        view.findViewById(R.id.cities_list_item_delete).setVisibility(View.GONE);
        LinearLayout linearBg = (LinearLayout) view.findViewById(R.id.cities_list_bg);

        String city = MyAppllication.cities.get(0).getCityName();
        textCity.setText(city);
        if (TextUtils.isEmpty(MyAppllication.getDatas().get(0))) {
            StringRequest request = new StringRequest(String.format(Contacts.WEATHER_INFO_URL, city),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //请求成功,通知Adapter更新视图
                            MyAppllication.saveData(s, 0);
                            try {
                                JSONObject jsonObject = new JSONObject(s);

                                if (0 == jsonObject.optInt(Contacts.ERROR_CODE)) {
                                    //获取到了正确数据
                                    Gson mGson = new Gson();
                                    JSONObject jsonObject1 = jsonObject.optJSONObject(Contacts.RESULT);
                                    JSONObject jsonObject2 = jsonObject1.optJSONObject(Contacts.REALTIME);
                                    //天气信息概括
                                    RealTime realTime = mGson.fromJson(jsonObject2.toString(), RealTime.class);
                                    textTemp.setText(realTime.getWeather().getTemperature() + "℃");
                                    textWeather.setText(realTime.getWeather().getInfo());
                                    List<Weather> weathers = mGson.fromJson(jsonObject1.optJSONArray(Contacts.WEATHER).toString(),
                                            new TypeToken<List<Weather>>() {
                                            }.getType());
                                    Weather.InfoBean infoBean = weathers.get(0).getInfo();
                                    textWeather.setText(realTime.getWeather().getInfo() + " " +
                                            infoBean.getDay().get(NumberUtils.WEATHER_TEMP_INDEX) + "°~" +
                                            infoBean.getNight().get(NumberUtils.WEATHER_TEMP_INDEX) + "°C");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MyAppllication.getRequestQueue(getContext()).add(request);
        }
        mRecycleView.addView(view, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initEvent();
    }

    /**
     * 设置各种事件
     */
    private void initEvent() {
        //初始化RecyclerView的Item点击事件
        citiesListAdapter.setOnItemClickListener(new CitiesListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                //获取现在的城市数据
               // Toast.makeText(getActivity(),"位置："+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(ITEM_CLICK_POSITION, position);
                getActivity().setResult(MANAGE_TO_MAIN_RESULT_CODE, intent);
                getActivity().finish();
            }
            @Override
            public void onLongClick(View v, int position) {

            }
        });
    }
}
