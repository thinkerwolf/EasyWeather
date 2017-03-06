package org.lgd.view.evaluator;

import android.animation.TypeEvaluator;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce Wu on 2016/4/1.
 */
public class TempEvaluator implements TypeEvaluator<List<Integer>> {

    //private static final int INCRESE = 1;

    @Override
    public List<Integer> evaluate(float fraction, List<Integer> startValue, List<Integer> endValue) {
        //Log.i("fraction", fraction + "");
        List<Integer> target = new ArrayList<>();
        for (int i = 0; i < startValue.size(); i++) {
            float temp = endValue.get(i);
            target.add((int) (fraction * temp));
        }
        //Log.i("target", target.toString());

        return target;
    }
}
