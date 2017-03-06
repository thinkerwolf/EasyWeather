package com.wukai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wukai.game.R;

/**
 * 小滑块View
 * Created by Bruce Wu on 2016/10/31.
 */

public class SlideBlockView extends View {

    private String number ="2";

    private int numberColor;

    private Rect mNumRect;

    private RectF mRect;

    private int backColor;

    private Paint mRectPaint, mNumPaint;

    private float numSize = 0,dx = 0, dy = 0;


    public SlideBlockView(Context context) {
        this(context, null);
    }

    public SlideBlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlideBlockView);
        int a;
        for(int i = 0; i < array.getIndexCount(); i++) {
            a = array.getIndex(i);
            switch (a) {
                case R.styleable.SlideBlockView_number :
                    if(array.getString(a) == null) {
                        this.number = " ";
                    } else {
                        this.number = array.getString(a);
                    }
                break;
                case R.styleable.SlideBlockView_numberColor :
                    this.numberColor = array.getColor(a, Color.LTGRAY);
                    break;

                case R.styleable.SlideBlockView_backColor:
                    this.backColor = array.getColor(a,Color.BLUE);
                    break;

                case R.styleable.SlideBlockView_numberSize:
                    this.numSize = array.getDimension(a, 10);
                    break;
                case R.styleable.SlideBlockView_dx :
                    this.dx = array.getDimension(a, 10);
                    break;
                case R.styleable.SlideBlockView_dy :
                    this.dy = array.getDimension(a, 10);
                    break;

            }
        }
        array.recycle();
        init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置绘制大小
        int drawWidth = Math.min(this.getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), this.getMeasuredHeight() - getPaddingBottom() - getPaddingTop());
        int left = getMeasuredWidth()/2 - drawWidth/2;
        int top = getMeasuredHeight()/2 - drawWidth/2;
        this.mRect = new RectF(left, top, left + drawWidth, top + drawWidth);
       // this.mCircleRect = new RectF(left, top, left + drawWidth, top + drawWidth);
        if(this.numSize == 0) {
            this.numSize = 0.5f;
        }
        if(this.dx == 0) {
            this.dx = drawWidth / 10;
        }
        if(this.dy == 0) {
            this.dy = drawWidth / 10;
        }

    }

    private void init() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);  //消除锯齿
        mNumPaint = new Paint();
        mNumPaint.setAntiAlias(true);
        mNumRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("number",number);


        super.onDraw(canvas);

       // Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);  
      //  Canvas circleCanvas = new Canvas();
      //  mCirclePaint.setColor(backColor);
      //  mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
      //  circleCanvas.drawOval(mCircleRect, mCirclePaint);


        mRectPaint.setColor(backColor);
        canvas.drawRoundRect(mRect, dx, dy, mRectPaint);

        mNumPaint.setColor(numberColor);
        mNumPaint.setTextSize(numSize);
        mNumPaint.getTextBounds(number, 0, number.length(), mNumRect);
        canvas.drawText(number, getMeasuredWidth()/2 - mNumRect.width()/2, getMeasuredHeight()/2 + mNumRect.height()/4 + 10,mNumPaint);
       // canvas.drawT
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
