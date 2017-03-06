package org.lgd.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.lgd.util.NumberUtils;
import org.lgd.view.evaluator.TempEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lgd.org.R;

/**
 * 温度趋势图
 * 最高温度和最低温度趋势
 * Created by Bruce Wu on 2016/3/31.
 */
public class TempTrendView extends View {

    private int tempSize = 37;  //绘制温度的尺寸
    private int tempColor = -1; //绘制温度的颜色
    private int pointSize = 20; //绘制点的大小
    private int pointColor = -1; //绘制点的颜色
    private int trendColor = -1; //趋势填充颜色
    private int trendAlpha = 120; //[0-255]范围内

    private List<Integer> highTemps; //一周高温集合
    private List<Integer> lowTemps;  //一周低温集合

    private int mPointDis;  //每个点之间的间距
    private Paint mPointsPaint;  //所有高温点的画笔

    private Paint mTempPaint;    //所有温度的画笔
    private Rect mTempRect;     //所有温度的Rect

    private Paint mTrendPaint;  //画区域图

    public int REFERENCE_HIGH_TEMP = 47;  //温度参考线
    public int REFERENCE_LOW_TEMP = -40;

    public TempTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempTrendView(Context context) {
        this(context, null);
    }

    public TempTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TempTrendView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TempTrendView_tempSize:
                    tempSize = (int) a.getDimension(attr, 35);
                    break;
                case R.styleable.TempTrendView_tempColor:
                    tempColor = a.getColor(attr, -1);
                    break;
                case R.styleable.TempTrendView_pointSize:
                    pointSize = (int) a.getDimension(attr, 20);
                    break;
                case R.styleable.TempTrendView_pointColor:
                    pointColor = a.getColor(attr, 8);
                    break;
                case R.styleable.TempTrendView_trendColor:
                    trendColor = a.getColor(attr, 8);
                    break;
            }
        }
        init();

    }

    public int getTempSize() {
        return tempSize;
    }

    public void setTempSize(int tempSize) {
        this.tempSize = tempSize;
        invalidateView();
    }

    public int getTempColor() {
        return tempColor;
    }

    public void setTempColor(int tempColor) {
        this.tempColor = tempColor;
        invalidateView();
    }

    public int getPointSize() {
        return pointSize;
    }

    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
        invalidateView();
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
        invalidateView();
    }

    public int getTrendColor() {
        return trendColor;
    }

    public void setTrendColor(int trendColor) {
        this.trendColor = trendColor;
        invalidateView();
    }

    public List<Integer> getHighTemps() {
        return highTemps;
    }

    public void setHighTemps(List<Integer> highTemps) {
        this.highTemps = highTemps;
        //设置相应的温度参考线
        Collections.sort(highTemps);
        REFERENCE_HIGH_TEMP = highTemps.get(highTemps.size() - 1) + 10;
        invalidateView();
    }

    public List<Integer> getLowTemps() {
        return lowTemps;
    }

    public void setLowTemps(List<Integer> lowTemps) {
        this.lowTemps = lowTemps;
        Collections.sort(lowTemps);
        REFERENCE_LOW_TEMP = lowTemps.get(0) - 10;
        invalidateView();
    }

    public void setTemps(List<Integer> highTemps, List<Integer> lowTemps) {
        this.highTemps = highTemps;
        this.lowTemps = lowTemps;
        Collections.sort(highTemps);
        REFERENCE_HIGH_TEMP = highTemps.get(highTemps.size() - 1) + 10;
        Collections.sort(lowTemps);
        REFERENCE_LOW_TEMP = lowTemps.get(0) - 10;
        invalidateView();
    }

    /**
     * 初始化温度
     */
    private void init() {
//        List<Integer> highTemp = new ArrayList<>();
//        List<Integer> lowTemp = new ArrayList<>();
        highTemps = new ArrayList<>();
//        highTemps.add(19);
//        highTemps.add(20);
//        highTemps.add(15);
//        highTemps.add(14);
//        highTemps.add(17);
//        highTemps.add(16);
//        highTemps.add(15);
//
        lowTemps = new ArrayList<>();
//        lowTemps.add(8);
//        lowTemps.add(2);
//        lowTemps.add(2);
//        lowTemps.add(2);
//        lowTemps.add(3);
//        lowTemps.add(4);
//        lowTemps.add(5);
//
//        highTemp.addAll(highTemps);
//        lowTemp.addAll(lowTemps);
//        Collections.sort(highTemp);
//        REFERENCE_HIGH_TEMP = highTemps.get(highTemps.size() - 1) + 10;
//        Collections.sort(lowTemp);
//        REFERENCE_LOW_TEMP = lowTemps.get(0) - 10;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.先绘制点，先计算点的个数
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int points = Math.max(highTemps.size(), lowTemps.size());
        if (points <= 0) {
            return;
        }
        mPointDis = getMeasuredWidth() / points;
        drawPoints(canvas);
        //startAnimation();
    }

    /**
     * 画出所有的点
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        //先画高温点
        if (highTemps.size() <= 0 && lowTemps.size() <= 0) {
            return;
        }
        mPointsPaint = new Paint();
        mPointsPaint.setColor(pointColor);
        mPointsPaint.setStyle(Paint.Style.FILL);
        mPointsPaint.setStrokeWidth(pointSize);
        mPointsPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointsPaint.setAntiAlias(true);
        //计算高温点的坐标
        float[] highPoints = drawTempPoints(highTemps, canvas, true);
        float[] lowPoints = drawTempPoints(lowTemps, canvas, false);
        drawTrend(canvas, highPoints, lowPoints);
    }

    /**
     * 画出中间区域
     *
     * @param highPoints
     * @param lowPoints
     */
    private void drawTrend(Canvas canvas, float[] highPoints, float[] lowPoints) {
        //利用drawPath来绘制多边形
        int len = Math.max(highPoints.length, lowPoints.length);
        mTrendPaint = new Paint();
        mTrendPaint.setStyle(Paint.Style.FILL);
        mTrendPaint.setColor(trendColor);
        mTrendPaint.setAlpha(trendAlpha);
        mTrendPaint.setAntiAlias(true);

        for (int i = -2; i < len; i += 2) {
            Path p = new Path();
            if (i + 3 < len && i >= 0) {
                p.moveTo(highPoints[i], highPoints[i + 1]);
                p.lineTo(highPoints[i + 2], highPoints[i + 3]);
                p.lineTo(lowPoints[i + 2], lowPoints[i + 3]);
                p.lineTo(lowPoints[i], lowPoints[i + 1]);
                p.close();
                //canvas.drawPath(p, mTrendPaint);
            } else if (i < 0) {
                p.moveTo(0, highPoints[i + 3] + 10);
                p.lineTo(highPoints[i + 2], highPoints[i + 3]);
                p.lineTo(lowPoints[i + 2], lowPoints[i + 3]);
                p.lineTo(0, lowPoints[i + 3] + 10);
                //canvas.drawPath(p, mTrendPaint);
            } else if (i + 3 >= len) {
                int width = getMeasuredWidth();
                p.moveTo(highPoints[len - 2], highPoints[len - 1]);
                p.lineTo(width, highPoints[len - 1] + 10);
                p.lineTo(width, lowPoints[len - 1] + 10);
                p.lineTo(lowPoints[len - 2], lowPoints[len - 1]);
            }
            canvas.drawPath(p, mTrendPaint);
        }

    }

    /**
     * 计算所有坐标的点,并顺便把每个温度值给画出来
     *
     * @param temps
     * @return
     */
    private float[] drawTempPoints(List<Integer> temps, Canvas canvas, boolean isHigh) {
        List<Integer> tPoints = new ArrayList<>();
        for (int i = 0; i < temps.size(); i++) {
            int x = mPointDis / 2 + i * mPointDis;
            int y = getMeasuredHeight() - getMeasuredHeight() * (temps.get(i) - REFERENCE_LOW_TEMP) / (REFERENCE_HIGH_TEMP - REFERENCE_LOW_TEMP);
            tPoints.add(x);
            tPoints.add(y);
            String temp = String.valueOf(temps.get(i));
            mTempPaint = new Paint();
            mTempRect = new Rect();
            mTempPaint.setColor(tempColor);
            mTempPaint.setTextSize(tempSize);
            mTempPaint.setAntiAlias(true);
            mTempPaint.getTextBounds(temp + "°", 0, temp.length(), mTempRect);
            if (isHigh) {
                canvas.drawText(temp + "°", x - mTempRect.width() / 2, y - 20, mTempPaint);
            } else {
                canvas.drawText(temp + "°", x - mTempRect.width() / 2, y + 20 + mTempRect.height(), mTempPaint);
            }

        }
        float[] points = NumberUtils.intsToFloats(tPoints.toArray());
        if (mPointsPaint != null)
            canvas.drawPoints(points, 0, points.length, mPointsPaint);
        return points;
    }

    /**
     * 开始动画
     *
     * @param time:动画时长
     */
    public void startAnimation(long time) {
        //Object就是找到TempTrendView的get和set方法不断的重置
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "highTemps",
                new TempEvaluator(), highTemps, highTemps);
        animator.setDuration(time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                highTemps = (List<Integer>) animation.getAnimatedValue();
                invalidateView();
            }
        });
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofObject(this, "lowTemps",
                new TempEvaluator(), lowTemps, lowTemps);
        animator1.setDuration(1000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lowTemps = (List<Integer>) animation.getAnimatedValue();
                invalidateView();
            }
        });
        animator1.start();
    }

    /**
     * 重绘视图
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
