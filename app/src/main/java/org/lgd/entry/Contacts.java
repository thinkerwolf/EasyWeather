package org.lgd.entry;

/**
 * 各种变量
 * Created by WuKai on 2016/4/1.
 */
public class Contacts {

    /**
     * 晴
     */
    public static final int WEATHER_SUNNY = 0;
    /**
     * 多云
     */
    public static final int WEATHER_CLOUDY = 1;
    /**
     * 阴天
     */
    public static final int WEATHER_DULL = 2;
    /**
     * 阵雨
     */
    public static final int WEATHER_RAINDY_SHOWER = 3;
    /**
     * 小雨
     */
    public static final int WEATHER_RAINDY_SMALL = 7;
    /**
     * 获取天气信息的URL
     */
    public static final String WEATHER_INFO_URL = "http://api.avatardata.cn/Weather/Query?key=652f1221e46a4756b4905fd55d055ecf&cityname=%s";

    /**
     * 一系列关于Json字符串的字段
     * 成功与否
     */
    public static final String ERROR_CODE = "error_code";
    /**
     * 成功后的结果
     */
    public static final String RESULT = "result";
    /**
     * 成功后的realtime
     */
    public static final String REALTIME = "realtime";
    /**
     * 以后后的天气情况
     */
    public static final String WEATHER = "weather";
    /**
     * 指数控制
     */
    public static final String LIFE = "life";
    /**
     * 信息
     */
    public static final String INFO = "info";
    /**
     * pm25指数
     */
    public static final String PM25 = "pm25";
    /**
     * 优
     */
    public static final String GOOD_AIR = "优";
    /**
     * 良
     */
    public static final String GOOD_AIR_LIA = "良";
    /**
     * 轻度污染
     */
    public static final String LIGHT_POLLUTION = "轻度污染";
    /**
     * 重度污染
     */
    public static final String HEAVEY_PLOOUTION = "重度污染";
    /**
     * 数据库名
     */
    public static final String DB_NAME = "weather.db";
    /**
     * 广播Action名称
     */
    public static final String MAINACTIVITY_UI_ACTION = "org.lgd.receiver";


    /**
     * 城市项目交换类型的key
     */
    public static final String CITIES_CHANGE_TYPE = "change_type";
    /**
     * 城市调换标志
     */
    public static final int SWAP_CITIES = 20;
    /**
     * 城市调换顺序from index名称
     */
    public static final String ITEM_FROM_NAME = "from";
    /**
     * 城市调换顺序to index名称
     */
    public static final String ITEM_TO_NAME = "to";
    /**
     * 删除城市标识
     */
    public static final int DELETE_CITY = 21;
    /**
     * 删除城市index的key
     */
    public static final String DELETE_INDEX = "delete_index";

    public static final String ADD_CITY_KEY = "add_city";
    /**
     * 添加城市标识
     */
    public static final int ADD_CITY = 22;


}
