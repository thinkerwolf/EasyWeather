package org.lgd.view.evaluator;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * 自定义
 * Created by Bruce Wu on 2016/3/31.
 */
public class TimeCircleEvaluator implements TypeEvaluator<Float> {

    /**
     *
     * @param fraction   ：分数，一小部分
     * @param startValue : 开始值
     * @param endValue   ：结束值
     * @return
     */
    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        Log.d("TimeCircleEvaluator","current fraction:" + fraction);
        return fraction*(endValue - startValue);
    }
}
