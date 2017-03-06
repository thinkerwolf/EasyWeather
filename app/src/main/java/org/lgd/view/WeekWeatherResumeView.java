package org.lgd.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lgd.entry.Contacts;
import org.lgd.entry.Weather;
import org.lgd.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lgd.org.R;

/**
 * 一周天气的概况，包括日期 周几 天气情况
 * Created by Bruce Wu on 2016/4/1.
 */
public class WeekWeatherResumeView extends LinearLayout {

    private List<Weather> weathers;  //所有的天气
    private LayoutInflater mInflater;
    private Context mContext;

    private int mWidth;
    private int mHeigth;

    private Paint mLinePaint;

    public WeekWeatherResumeView(Context context) {
        this(context, null);
    }

    public WeekWeatherResumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekWeatherResumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        weathers = new ArrayList<>();
        this.setOrientation(HORIZONTAL);
        //this.setGravity(Gravity.CENTER);
        mLinePaint = new Paint();
        //mLinePaint.setAlpha(200);
        mLinePaint.setColor(Color.parseColor("#ffffff"));
        mLinePaint.setStrokeWidth(10);
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
        init();
        invalidateView();
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    /**
     * 初始化
     */
    private void init() {
        int size = weathers.size();
        if (size <= 0) {
            return;
        }
        int i = 0;
        this.removeAllViews();
        for (Weather weather : weathers) {
            View view = mInflater.inflate(R.layout.dayofweek_resume, null);
            TextView date = (TextView) view.findViewById(R.id.id_dayOfWeek_date);
            TextView week = (TextView) view.findViewById(R.id.id_dayOfWeek);
            ImageView image_day = (ImageView) view.findViewById(R.id.id_day_image);
            ImageView image_night = (ImageView) view.findViewById(R.id.id_night_image);
            date.setText(StringUtils.transDate(weather.getDate()));
            if (i == 0) {
                TextPaint paint = week.getPaint();
                paint.setFakeBoldText(true);  //加粗
                week.setText("今天");
            } else {
                week.setText("周" + weather.getWeek());
            }
            setImageView(image_day, StringUtils.str2Int(weather.getInfo().getDay().get(0)));
            setImageView(image_night, StringUtils.str2Int(weather.getInfo().getNight().get(0)));
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            this.addView(view, layoutParams);
            i++;
        }
    }

    /**
     * 根据天气情况设置图片
     * @param iv
     * @param weat
     */
    private void setImageView(ImageView iv, int weat) {
        switch (weat) {
            //TODO :一些天气的代码不知道
            case Contacts.WEATHER_SUNNY:  //晴天
                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sunny));
                break;
            case Contacts.WEATHER_CLOUDY:  //多云
                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cloudy));
                break;
            case Contacts.WEATHER_RAINDY_SMALL: //小雨
                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.raindy_small));
                break;
            case Contacts.WEATHER_DULL://阴天

                break;
            case Contacts.WEATHER_RAINDY_SHOWER://阵雨

                break;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeigth = getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeigth = getMeasuredHeight();
    }

    /**
     * 主要是画线
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int num = weathers.size();
        int dis = mWidth / num;
        for (int i = 1; i < num; i++) {
            canvas.drawLine(dis * i, 0, dis * i, mHeigth, mLinePaint);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

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
