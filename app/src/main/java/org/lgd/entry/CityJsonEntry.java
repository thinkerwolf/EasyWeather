package org.lgd.entry;

import org.lgd.util.db.annotation.AutoIncrease;
import org.lgd.util.db.annotation.Column;
import org.lgd.util.db.annotation.PrimaryKey;
import org.lgd.util.db.annotation.Table;
import org.lgd.util.db.annotation.Unsigned;

import java.io.Serializable;

/**
 * 存储的城市信息
 * Created by wukai on 2016/6/18.
 */
@Table(value = "cities_json")
public class CityJsonEntry implements Serializable {

    @Column(column = "ID")
    @AutoIncrease(value = true)
    @Unsigned(value = true)
    @PrimaryKey(value = true)
    private int id;

    @Column(column = "CITY_NAME")
    private String cityName;

    @Column(column = "CITY_JSON")
    private String cityJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityJson() {
        return cityJson;
    }

    public void setCityJson(String cityJson) {
        this.cityJson = cityJson;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
