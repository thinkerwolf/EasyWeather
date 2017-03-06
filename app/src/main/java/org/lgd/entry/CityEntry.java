package org.lgd.entry;

import org.lgd.util.db.annotation.AutoIncrease;
import org.lgd.util.db.annotation.Column;
import org.lgd.util.db.annotation.Ignore;
import org.lgd.util.db.annotation.PrimaryKey;
import org.lgd.util.db.annotation.Table;
import org.lgd.util.db.annotation.Unsigned;

/**
 * 后台存储的城市信息
 * Created by Bruce Wu on 2016/4/4.
 */
@Table(value = "cities")
public class CityEntry implements Comparable {

    @Column(column = "ID")
    @PrimaryKey(value = true)
    @AutoIncrease(value = true)
    @Unsigned(value = true)
    private int id;

    @Ignore(value = true)
    @Column(column = "CITY_NAME")
    private String cityName;

    /**
     * 0.定位城市
     * 1.非定位城市
     */
    @Column(column = "LOCATE_OR_NOT")
    private int locationOrNot;

    @Column(column = "sortId")
    @Unsigned(value = true)
    private int sortId;


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

    public int getLocationOrNot() {
        return locationOrNot;
    }

    public void setLocationOrNot(int locationOrNot) {
        this.locationOrNot = locationOrNot;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Override
    public String toString() {
        return "cityName:" + cityName + "; " + "locationOrNot:" + locationOrNot;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CityEntry) {
            CityEntry entry = (CityEntry) o;
            if (this.cityName.equals(entry.getCityName()) && this.getId() == entry.getId() && this.getSortId() == entry.getSortId()) {
                return true;
            }


        }
        return false;
    }

    @Override
    public int compareTo(Object another) {
        if (another instanceof CityEntry) {
            CityEntry entry = (CityEntry) another;
            return this.getSortId() - entry.getSortId();
        } else {
            return 0;
        }
    }
}
