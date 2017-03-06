package org.lgd.util;

/**
 * 字符串工具类
 * Created by Bruce Wu on 2016/4/1.
 */
public class StringUtils {

    /**
     * str转化为Int
     *
     * @param str
     * @return
     */
    public static int str2Int(String str) {
        if (str.equals("")) {
            return 0;
        } else {
            int res = Integer.parseInt(str);
            return res;
        }

    }

    /**
     * 将2016-04-01转化为4/1格式
     *
     * @param date:如2016-04-01
     * @return 4/1
     */
    public static String transDate(String date) {
        String[] strs = date.split("-");
        return Integer.parseInt(strs[1]) + "/" + Integer.parseInt(strs[2]);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null != str) {
            if ("".endsWith(str)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 从相应的字符串中删除相应的str
     *
     * @param str
     * @param del
     * @return
     */
    public static String delString(String str, String del) {
        if (str.contains(del)) {
            int len = str.length();
            int begin = str.indexOf(del);
            int end = begin + del.length();
            str = str.substring(0, begin) + str.substring(end, len);
        }
        return str;
    }
}
