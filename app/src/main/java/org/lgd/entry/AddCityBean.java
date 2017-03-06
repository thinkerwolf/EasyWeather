package org.lgd.entry;

/**
 * Created by wukai on 2016/5/7.
 */
public class AddCityBean {


    /**
     * label : 阿里Ali0897
     * name : 阿里
     * pinyin : Ali
     * zip : 0897
     * pos : 1
     */

    private String label;
    private String name;
    private String pinyin;
    private String zip;
    /**
     * pos : 1
     */

    private int pos;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AddCityBean) {
            AddCityBean bean = (AddCityBean) o;
            if (bean.getName().equals(this.getName()) && bean.getPinyin().equals(this.getPinyin())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getName() + ":" + this.getPinyin();
    }
}
