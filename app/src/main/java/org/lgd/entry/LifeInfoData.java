package org.lgd.entry;

import java.util.ArrayList;
import java.util.List;

import lgd.org.R;

/**
 * 初始化生活指数的数据
 * Created by Bruce Wu on 2016/4/15.
 */
public class LifeInfoData {

    /**
     * 生活指数名字
     */
    private List<String> lifeIndexNames;

    /**
     * 生活指数的图片
     */
    private List<Integer> lifeIndexImgs;
    /**
     * 生活指数的结果
     */
    private List<String> lifeIndexRes;
    /**
     * 生活指数建议
     */
    private List<String> lifeIndexSuggess;

    public LifeInfoData(Life life) {
        init(life);
    }

    private void init(Life life) {
        lifeIndexNames = new ArrayList<>();
        lifeIndexImgs = new ArrayList<>();
        lifeIndexRes = new ArrayList<>();
        lifeIndexSuggess = new ArrayList<>();

        lifeIndexNames.add("运动指数");
        lifeIndexNames.add("洗车指数");
        lifeIndexNames.add("穿衣指数");
        lifeIndexNames.add("感冒指数");
        lifeIndexNames.add("污染指数");
        lifeIndexNames.add("空调指数");

        lifeIndexImgs.add(R.drawable.icon_sport_index);
        lifeIndexImgs.add(R.drawable.icon_wash_car_index);
        lifeIndexImgs.add(R.drawable.icon_shirt_index);
        lifeIndexImgs.add(R.drawable.icon_comfort_index);
        lifeIndexImgs.add(R.drawable.icon_pollution_index);
        lifeIndexImgs.add(R.drawable.icon_air_conditioning_index);

        lifeIndexRes.add(life.getYundong().get(0));
        lifeIndexSuggess.add(life.getYundong().get(1));

        lifeIndexRes.add(life.getXiche().get(0));
        lifeIndexSuggess.add(life.getXiche().get(1));

        lifeIndexRes.add(life.getChuanyi().get(0));
        lifeIndexSuggess.add(life.getChuanyi().get(1));

        lifeIndexRes.add(life.getGanmao().get(0));
        lifeIndexSuggess.add(life.getGanmao().get(1));

        lifeIndexRes.add(life.getWuran().get(0));
        lifeIndexSuggess.add(life.getWuran().get(1));

        lifeIndexRes.add(life.getKongtiao().get(0));
        lifeIndexSuggess.add(life.getKongtiao().get(1));

    }


    public List<String> getLifeIndexNames() {
        return lifeIndexNames;
    }

    public List<Integer> getLifeIndexImgs() {
        return lifeIndexImgs;
    }

    public List<String> getLifeIndexRes() {
        return lifeIndexRes;
    }

    public List<String> getLifeIndexSuggess() {
        return lifeIndexSuggess;
    }
}
