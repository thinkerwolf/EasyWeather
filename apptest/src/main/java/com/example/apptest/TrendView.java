package com.example.apptest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by wukai on 2016/6/22.
 */
public class TrendView extends View {

    private List<Integer> mTempList;

    private Paint mPaint;

    public TrendView(Context context) {
        this(context, null);
    }

    public TrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
    }

    public TrendView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }


    public List<Integer> getmTempList() {
        return mTempList;
    }

    public void setmTempList(List<Integer> mTempList) {
        this.mTempList = mTempList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTempList != null) {
            for (int i = 0; i < mTempList.size(); i++) {
                drawPoints(canvas, i);
            }
        }
    }


    /**
     * @param canvas
     */
    private void drawPoints(Canvas canvas, int i) {
        int width = this.getMeasuredWidth();
        int num = mTempList.size();
        int dis = width / num;
        int temp = mTempList.get(i);
        int height = this.getMeasuredHeight();
        if (i >= 0 && i < num - 1) {
            int tempN = mTempList.get(i + 1);
            float startX = 0 + i * dis;
            float startY = height * temp / 20;
            float endX = 0 + (i + 1) * dis;
            float endY = height * tempN / 20;
            mPaint.setStrokeWidth(20);
            canvas.drawPoint(startX, startY, mPaint);
            mPaint.setStrokeWidth(60);
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }
}
