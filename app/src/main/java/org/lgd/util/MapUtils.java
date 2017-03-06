package org.lgd.util;

import java.util.List;
import java.util.Map;

/**
 * map工具
 * Created by Bruce Wu on 2016/4/2.
 */
public class MapUtils {

    /**
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return null;
        }
    }

    /**
     * 获取List
     *
     * @param map
     * @param key
     * @return
     */
    public static List<Integer> getList(Map<String, ? extends Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof List) {
            return (List) obj;
        } else {
            return null;
        }
    }
}
