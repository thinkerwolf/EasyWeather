package org.lgd.util;

import org.lgd.entry.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数字之间转化的工具类
 * Created by Bruce Wu on 2016/4/1.
 */
public class NumberUtils {

    public static final int WEATHER_TEMP_INDEX = 2;

    /**
     * 将int数组转化为float数组
     *
     * @return
     */
    public static float[] intsToFloats(Integer[] ints) {
        int len = ints.length;
        float[] floats = new float[len];
        for (int i = 0; i < len; i++) {
            floats[i] = ints[i];
        }
        return floats;
    }

    public static float[] intsToFloats(Object[] objs) {
        int len = objs.length;
        Integer[] temp = new Integer[len];
        for (int i = 0; i < len; i++) {
            if (objs[i] instanceof Integer) {
                temp[i] = (Integer) objs[i];
            }
        }
        float[] floats = new float[len];
        for (int i = 0; i < len; i++) {
            floats[i] = temp[i];
        }
        return floats;
    }

    /**
     * 处理最高温度和最低温度
     */
    public static Map<String, List<Integer>> getTemps(List<Weather> weathers) {
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> highTemps = new ArrayList<>();
        List<Integer> lowTemps = new ArrayList<>();
        for (Weather weather : weathers) {
            Weather.InfoBean bean = weather.getInfo();
            int temp1 = StringUtils.str2Int(bean.getDay().get(WEATHER_TEMP_INDEX));
            int temp2 = StringUtils.str2Int(bean.getNight().get(WEATHER_TEMP_INDEX));
            if (temp1 > temp2) {
                highTemps.add(temp1);
                lowTemps.add(temp2);
            } else {
                highTemps.add(temp2);
                lowTemps.add(temp1);
            }
        }
        map.put("highTemps", highTemps);
        map.put("lowTemps", lowTemps);
        return map;
    }
}
