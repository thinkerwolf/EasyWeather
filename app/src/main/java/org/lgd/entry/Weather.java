package org.lgd.entry;

import java.util.List;

/**
 * 每天的天气概述类
 * Created by Bruce Wu on 2016/4/1.
 */
public class Weather {


    /**
     * date : 2016-04-01
     * week : 五
     * nongli : 二月廿四
     * info : {"dawn":null,"day":["7","小雨","19","西南风","4-5 级","05:41"],"night":["1","多云","2","北风","4-5 级","18:22"]}
     */

    private String date;
    private String week;
    private String nongli;
    /**
     * dawn : null
     * day : ["7","小雨","19","西南风","4-5 级","05:41"]
     * night : ["1","多云","2","北风","4-5 级","18:22"]
     */

    private InfoBean info;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getNongli() {
        return nongli;
    }

    public void setNongli(String nongli) {
        this.nongli = nongli;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private List<String> dawn;
        private List<String> day;
        private List<String> night;

        public List<String> getDawn() {
            return dawn;
        }

        public void setDawn(List<String> dawn) {
            this.dawn = dawn;
        }

        public List<String> getDay() {
            return day;
        }

        public void setDay(List<String> day) {
            this.day = day;
        }

        public List<String> getNight() {
            return night;
        }

        public void setNight(List<String> night) {
            this.night = night;
        }
    }
}
