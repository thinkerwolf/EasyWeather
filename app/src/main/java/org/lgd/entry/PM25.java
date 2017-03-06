package org.lgd.entry;

/**
 * Created by Bruce Wu on 2016/4/13.
 */
public class PM25 {


    /**
     * key :
     * show_desc : 0
     * pm25 : {"curPm":"196","pm25":"174","pm10":"196","level":"5","quality":"重度污染","des":"老年人和心脏病、肺病患者应停留在室内，停止户外活动，一般人群减少户外运动。"}
     * dateTime : 2016年04月13日08时
     * cityName : 北京
     */

    private String key;
    private String show_desc;
    /**
     * curPm : 196
     * pm25 : 174
     * pm10 : 196
     * level : 5
     * quality : 重度污染
     * des : 老年人和心脏病、肺病患者应停留在室内，停止户外活动，一般人群减少户外运动。
     */

    private Pm25Bean pm25;
    private String dateTime;
    private String cityName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShow_desc() {
        return show_desc;
    }

    public void setShow_desc(String show_desc) {
        this.show_desc = show_desc;
    }

    public Pm25Bean getPm25() {
        return pm25;
    }

    public void setPm25(Pm25Bean pm25) {
        this.pm25 = pm25;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public static class Pm25Bean {
        private String curPm;
        private String pm25;
        private String pm10;
        private String level;
        private String quality;
        private String des;

        public String getCurPm() {
            return curPm;
        }

        public void setCurPm(String curPm) {
            this.curPm = curPm;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
