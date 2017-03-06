package org.lgd.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import lgd.org.R;

/**
 * 日落和日出自定义View
 * 显示日出和日落的时间位置
 * 显示当前时间在圆圈上的位置
 * <p>
 * 属性：日落日出时间 (String) 5:30-18:30 :转换成分钟
 * 现在时间（String）    10:20     ：转换成分钟
 * 整体时间             24*60
 * 现在的温度：int
 * </p>
 * Created by Bruce Wu on 2016/3/30.
 */
public class SunRiseDownCircleView extends View {

    /**
     * 一些属性的定义
     */
    private String mSunRiseTime = "06:00";  //日出时间格式必须是 XX:XX
    private String mSunDownTime = "18:00";  //日落时间         XX:XX
    private String mCurrentTime = "17:00";  //当前的时间       XX:XX
    private String mTempeture = "--";    //现在的温度
    private int mTotleTime;   //总时间：也就是24小时
    private int mTempSize = 50;    //温度字体大小
    private int mTimeSize = 25;    //时间字体大小
    private int mCircleWidth = 9;  //圆环的宽度
    private float mSweepValue;     //当前时间角度

    public static final int CIRCLE_PADDING = 28;   //最外面大圆的PADDING

    private boolean isFirst = false;

    //绘制最外面圆环
    private RectF mCircleRectF;
    private Paint mCirclePaint;

    //绘制里面当前温度
    private Rect mTempRect;
    private Paint mTempPaint;

    //绘制当前的时间的圆
    private Paint mCurrentCirclePaint;

    //绘制当前时间文字
    private Rect mCurrentTimeRect;
    private Paint mCurrentTimePaint;

    //绘制日落和日出时间
    private Rect mRiseDownTimeRect;
    private Paint mRiseDownTimePaint;

    public String getSunRiseTime() {
        return mSunRiseTime;
    }

    public void setSunRiseTime(String mSunRiseTime) {
        this.mSunRiseTime = mSunRiseTime;
        invalidateView();
    }

    public String getSunDownTime() {
        return mSunDownTime;
    }

    public void setSunDownTime(String mSunDownTime) {
        this.mSunDownTime = mSunDownTime;
        invalidateView();
    }

    public String getTempeture() {
        return mTempeture;
    }

    public void setTempeture(String mTempeture) {
        this.mTempeture = mTempeture;
        invalidateView();
    }

    public int getTotleTime() {
        return mTotleTime;
    }

    public void setTotleTime(int mTotleTime) {
        this.mTotleTime = mTotleTime;
        invalidateView();
    }

    public int getTimeSize() {
        return mTimeSize;
    }

    public void setTimeSize(int mTimeSize) {
        this.mTimeSize = mTimeSize;
        invalidateView();
    }

    public int getTempSize() {
        return mTempSize;
    }

    public void setTempSize(int mTempSize) {
        this.mTempSize = mTempSize;
        invalidateView();
    }

    public void setSunRiseAndDownTime(String mSunRiseTime, String mSunDownTime) {
        this.mSunRiseTime = mSunRiseTime;
        this.mSunDownTime = mSunDownTime;
        invalidateView();
    }

    public SunRiseDownCircleView(Context context) {
        this(context, null);
    }

    public SunRiseDownCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context：上下文
     * @param attrs:xml文件中定义的属性
     * @param defStyleAttr:默认的属性
     */
    public SunRiseDownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将一个AttributeSet属性组转换成一个Array类型的，便于遍历
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SunRiseDownCircleView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SunRiseDownCircleView_risetime:
                    mSunRiseTime = a.getString(attr);
                    break;
                case R.styleable.SunRiseDownCircleView_downtime:
                    mSunDownTime = a.getString(attr);
                    break;
                case R.styleable.SunRiseDownCircleView_tempeture:
                    mTempeture = String.valueOf(a.getInt(attr, 0));
                    break;
                case R.styleable.SunRiseDownCircleView_tempSize:
                    mTempSize = (int) a.getDimension(attr, 50);
                    break;
                case R.styleable.SunRiseDownCircleView_timeSize:
                    mTimeSize = a.getDimensionPixelSize(attr, 8);
                    break;
                case R.styleable.SunRiseDownCircleView_circleWidth:
                    mCircleWidth = (int) a.getDimension(attr, 9);
            }
        }
        a.recycle();
        mTotleTime = 24 * 60;
    }

    /**
     * 测量大小
     *
     * @param widthMeasureSpec  :
     * @param heightMeasureSpec :
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.计算circle的绘制长宽
        int circleWidth = Math.min(getMeasuredHeight() - getPaddingLeft() - getPaddingRight() - mCircleWidth - CIRCLE_PADDING,
                getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - mCircleWidth) - CIRCLE_PADDING;
        //2.设置绘制区域
        int left = getMeasuredWidth() / 2 - circleWidth / 2;
        int top = getMeasuredHeight() / 2 - circleWidth / 2;
        //mCircleRect = new Rect(left, top, left + circleWidth, top + circleWidth);
        mCircleRectF = new RectF(left, top, left + circleWidth, top + circleWidth);

    }

    /**
     * 绘制View
     *
     * @param canvas:画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        drawTemp(canvas);
        drawCircle(canvas);
        if (!isFirst) {
            timeChange();
            isFirst = true;
        }
    }

    /**
     * 每隔30秒重重绘一下视图
     */
    private void timeChange() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30000);
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }


    /**
     * 获取最外面大圆的半径-圆环半径的一半
     *
     * @return
     */
    private double getCicleRadius() {
        double radius = Math.min(getMeasuredHeight() / 2, getMeasuredWidth() / 2) - mCircleWidth / 2 - CIRCLE_PADDING;
        return radius;
    }

    /**
     * 绘制圆里面的温度
     *
     * @param canvas
     */
    private void drawTemp(Canvas canvas) {
        String temp = mTempeture + "℃";
        mTempRect = new Rect();
        mTempPaint = new Paint();
        mTempPaint.setTextSize(mTempSize);
        mTempPaint.setAntiAlias(true);  //消除锯齿
        mTempPaint.setColor(Color.parseColor("#F9FEEA"));
        mTempPaint.getTextBounds(temp, 0, temp.length(), mTempRect);
        int x = getMeasuredWidth() / 2 - mTempRect.width() / 2;
        int y = getMeasuredHeight() / 2 + mTempRect.height() / 2;
        canvas.drawText(temp, x, y, mTempPaint);
    }

    /**
     * 绘制最外面的圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        //先绘制时间
        int sunRise = -1;
        int sunDown = -1;
        try {
            mSunRiseTime = mSunRiseTime.replace(" ", "");
            mSunDownTime = mSunDownTime.replace(" ", "");
            String[] riseTime = mSunRiseTime.split(":");
            String[] downTime = mSunDownTime.split(":");
            int riseHour = Integer.parseInt(riseTime[0]);
            int riseMinute = Integer.parseInt(riseTime[1]);
            int downHour = Integer.parseInt(downTime[0]);
            int downMinute = Integer.parseInt(downTime[1]);
            if (riseHour >= 12 || riseHour < 0 || riseMinute < 0 || riseMinute > 60
                    || downHour < 0 || downHour >= 24 || downMinute < 0 || downMinute > 60) {
                Log.e("Error", "请输入标准的24小时时间格式XX:XX!");
                return;
            }
            sunRise = riseHour * 60 + riseMinute;
            sunDown = downHour * 60 + downMinute;
        } catch (Exception e) {
            Log.e("Error", "时间格式不正确:请输入标准的24小时时间格式XX:XX!\n" + e.getMessage());
        }

        if (sunRise >= 0 && sunDown >= 0) {
            mCirclePaint = new Paint();
            mCirclePaint.setAntiAlias(true);  //消除锯齿
            mCirclePaint.setStyle(Paint.Style.STROKE);
            mCirclePaint.setStrokeWidth(mCircleWidth);
            mCirclePaint.setColor(Color.parseColor("#2664B9"));
            float riseAngle = (sunRise - 360) * 360 / 1440;
            float downAngle = (sunDown - 1080) * 360 / 1440;
            canvas.drawArc(mCircleRectF, downAngle + 5,
                    180 - downAngle + riseAngle - 10,false,mCirclePaint);
            mCirclePaint.setColor(Color.parseColor("#F9FEEA"));
            canvas.drawArc(mCircleRectF, riseAngle + 180 + 5,
                    180 - riseAngle + downAngle - 10,false,mCirclePaint);
            drawSunRiseDown(true, canvas, riseAngle);
            drawSunRiseDown(false, canvas, downAngle);
            drawCurrentTime(canvas, sunRise, sunDown);   //绘制现在的时间
        }
    }

    /**
     * 绘制日落和日出
     *
     * @param isRise true:日出   false:日落
     * @param canvas
     * @param angle  : 角度
     */
    private void drawSunRiseDown(boolean isRise, Canvas canvas, float angle) {
        BitmapDrawable drawable = null;
        Bitmap mSunBitmap = null;
        float left = 0;
        float top = 0;
        if (isRise) {  //日出
            drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.sun_rise);
            mSunBitmap = drawable.getBitmap();
            top = (float) (getMeasuredHeight() / 2 - getCicleRadius() * Math.sin(2 * angle * Math.PI / 360)
                    - mSunBitmap.getHeight() / 2);
            left = (float) (getMeasuredWidth() / 2 - getCicleRadius() * Math.cos(2 * angle * Math.PI / 360)
                    - mSunBitmap.getWidth() / 2);
            mRiseDownTimePaint = new Paint();
            mRiseDownTimeRect = new Rect();
            mRiseDownTimePaint.setTextSize(mTimeSize);
            mRiseDownTimePaint.setAntiAlias(true);
            mRiseDownTimePaint.getTextBounds(mSunRiseTime, 0, mSunRiseTime.length(), mRiseDownTimeRect);
            mRiseDownTimePaint.setColor(Color.parseColor("#F9FEEA"));
            canvas.drawText(mSunRiseTime, left + mSunBitmap.getWidth() + 10,
                    top + mRiseDownTimeRect.height() / 2 + mSunBitmap.getHeight() / 2, mRiseDownTimePaint);

        } else {   //日落
            drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.sun_down);
            mSunBitmap = drawable.getBitmap();
            top = (float) (getMeasuredHeight() / 2 + getCicleRadius() * Math.sin(2 * angle * Math.PI / 360)
                    - mSunBitmap.getHeight() / 2);
            left = (float) (getMeasuredWidth() / 2 + getCicleRadius() * Math.cos(2 * angle * Math.PI / 360)
                    - mSunBitmap.getWidth() / 2);
            mRiseDownTimePaint = new Paint();
            mRiseDownTimeRect = new Rect();
            mRiseDownTimePaint.setTextSize(mTimeSize);
            mRiseDownTimePaint.setAntiAlias(true);
            mRiseDownTimePaint.getTextBounds(mSunDownTime, 0, mSunDownTime.length(), mRiseDownTimeRect);
            mRiseDownTimePaint.setColor(Color.parseColor("#F9FEEA"));
            canvas.drawText(mSunDownTime, left - mRiseDownTimeRect.width() - 10,
                    top + mRiseDownTimeRect.height() / 2 + mSunBitmap.getHeight() / 2, mRiseDownTimePaint);

        }
        canvas.drawBitmap(mSunBitmap, left, top, null);

    }


    /**
     * 绘制当前的时间圆和里面的时间
     *
     * @param canvas
     */
    private void drawCurrentTime(Canvas canvas, int sunRise, int sunDown) {
        //绘画出当前时间的圆圈
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mCurrentTime = format.format(date);
        Log.i("CurrentTime:", mCurrentTime);
        mCurrentCirclePaint = new Paint();
        mCurrentCirclePaint.setAntiAlias(true);
        mCurrentCirclePaint.setStyle(Paint.Style.FILL);
        int currentTime = Integer.parseInt(mCurrentTime.split(":")[0]) * 60 + Integer.parseInt(mCurrentTime.split(":")[1]);
        currentTime = currentTime == 0 ? 1440 : currentTime;
        Log.i("CurrentTime:", currentTime + "");
        if (currentTime >= sunRise && currentTime < sunDown) {   //白天
            mCurrentCirclePaint.setColor(Color.parseColor("#F9FEEA"));
        } else {   //黑夜
            mCurrentCirclePaint.setColor(Color.parseColor("#2664B9"));
        }
        if (currentTime >= 1080 && currentTime <= 1440) {
            mSweepValue = (currentTime - 1080) * 360 / 1440;
        } else {
            mSweepValue = (360 + currentTime) * 360 / 1440;
        }
        Log.i("mSweepValue:", mSweepValue + "");
        int restoreCount = canvas.save();
        //canvas.translate(mCircleRectF.centerX(), mCircleRectF.centerY());   //将画布移动到
        //计算小圆的中心位置
        float y = (float) (Math.sin(2 * mSweepValue * Math.PI / 360) * getCicleRadius() + getMeasuredHeight() / 2);
        float x = (float) (Math.cos(2 * mSweepValue * Math.PI / 360) * getCicleRadius() + getMeasuredWidth() / 2);
        canvas.drawCircle(x, y, mCircleWidth + 20, mCurrentCirclePaint);
        canvas.restoreToCount(restoreCount);

        //绘出当前时间数字在圆的上面
        String hour = mCurrentTime.split(":")[0];
        mCurrentTimePaint = new Paint();
        mCurrentTimeRect = new Rect();
        mCurrentTimePaint.setTextSize(mTimeSize);
        mCurrentTimePaint.getTextBounds(hour, 0, hour.length(), mCurrentTimeRect);
        mCurrentTimePaint.setColor(Color.parseColor("#000000"));
        canvas.drawText(hour, x - mCurrentTimeRect.width() / 2, y + mCurrentTimeRect.height() / 2, mCurrentTimePaint);
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
