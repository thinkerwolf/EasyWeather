package org.lgd.activity;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.fragment.MainFragment;
import org.lgd.util.DBUtils;
import org.lgd.util.SDCardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wukai on 2016/4/22.
 */
public class MyAppllication {
    /**
     * 全局的天气数据
     */
    public static List<String> weatherDatas = new ArrayList<>();
    /**
     * 全局的城市数据
     */
    public static List<CityEntry> cities = new ArrayList<>();

    public static DBUtils getDBUtils(Context context) {
        DBUtils dbUtils = null;
        String path = SDCardUtil.getStorepath(context);
        if (null != path) {
            dbUtils = DBUtils.create(context, Contacts.DB_NAME, path);
        } else {
            dbUtils = DBUtils.create(context, Contacts.DB_NAME);
        }
        return dbUtils;
    }


    public static RequestQueue getRequestQueue(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        return queue;
    }

    public static List<String> getDatas() {
        return weatherDatas;
    }

    /**
     * 初始化cities和weatherDatas数据
     *
     * @param num
     */
    public static void setCityNum(int num) {
        weatherDatas.clear();
        cities.clear();
        if (num == 0) {
            weatherDatas.add(null);
            cities.add(null);
        } else {
            for (int i = 0; i < num; i++) {
                weatherDatas.add(null);
                cities.add(null);
            }
        }

    }

    /**
     * 在指定位置添加数据
     *
     * @param json     :json数据
     * @param position : 指定位置
     */
    public static void saveData(String json, int position) {
        //代替
        weatherDatas.set(position, json);
    }

    /**
     * 替换整体的城市数据
     *
     * @param datas
     */
    public synchronized static void saveData(List<String> datas) {
        weatherDatas = datas;
    }

    /**
     * 在指定的位置设置城市数据
     *
     * @param city     ：城市名称
     * @param position :位置
     */
    public static void saveCity(CityEntry city, int position) {
        cities.set(position, city);
    }

    /**
     * 在指定的位置设置城市数据
     */
    public static void saveCity(List<CityEntry> cities) {
        MyAppllication.cities = cities;
    }

    /**
     * 移除指定位置的数据
     *
     * @param position
     */
    public static void deleteData(int position) {
        if (position >= 0 || position < weatherDatas.size()) {
            weatherDatas.remove(position);
        }
    }
}
