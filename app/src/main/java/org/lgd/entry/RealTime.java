package org.lgd.entry;

/**
 * Created by Bruce Wu on 2016/4/2.
 */
public class RealTime {

    /**
     * windspeed : 32.0
     * direct : 北风
     * power : 2级
     * offset : null
     */

    private WindBean wind;
    /**
     * wind : {"windspeed":"32.0","direct":"北风","power":"2级","offset":null}
     * time : 22:00:00
     * weather : {"humidity":"21","img":"0","info":"晴","temperature":"18"}
     * dataUptime : 1459522262
     * date : 2016-04-01
     * city_code : 101030100
     * city_name : 天津
     * week : 5
     * moon : 二月廿四
     */

    private String time;
    /**
     * humidity : 21
     * img : 0
     * info : 晴
     * temperature : 18
     */

    private WeatherBean weather;
    private String dataUptime;
    private String date;
    private String city_code;
    private String city_name;
    private String week;
    private String moon;

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public WeatherBean getWeather() {
        return weather;
    }

    public void setWeather(WeatherBean weather) {
        this.weather = weather;
    }

    public String getDataUptime() {
        return dataUptime;
    }

    public void setDataUptime(String dataUptime) {
        this.dataUptime = dataUptime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }

    public static class WindBean {
        private String windspeed;
        private String direct;
        private String power;
        private Object offset;

        public String getWindspeed() {
            return windspeed;
        }

        public void setWindspeed(String windspeed) {
            this.windspeed = windspeed;
        }

        public String getDirect() {
            return direct;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public Object getOffset() {
            return offset;
        }

        public void setOffset(Object offset) {
            this.offset = offset;
        }
    }

    public static class WeatherBean {
        private String humidity;
        private String img;
        private String info;
        private String temperature;

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
    }
}
