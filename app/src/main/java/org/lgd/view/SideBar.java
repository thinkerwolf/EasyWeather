package org.lgd.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.lgd.util.db.annotation.NotNull;

import lgd.org.R;

/**
 * 城市选择侧边栏
 * 能根据
 * Created by wukai on 2016/5/2.
 */
public class SideBar extends View {

    /**
     * 旁边字母正常颜色
     */
    private int sideCharNormalColor = Color.LTGRAY;
    /**
     * 旁边字母按下颜色
     */
    private int sideCharPressColor = Color.WHITE;
    /**
     * 旁边字幕大小
     */
    private int sideCharSize = 30;
    /**
     * 弹出字母颜色
     */
    private int peekCharColor = Color.WHITE;
    /**
     * 弹出字母大小
     */
    private int peekCharSize = 55;
    private String[] cityChar = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    /**
     * 画笔
     */
    private Paint paint = new Paint();
    /**
     * 弹出的TextView
     */
    private TextView textViewDialog;
    /**
     * 当前选择的字幕index
     */
    private int choose = -1;

    private OnLetterChangeListener onLetterChangeListener;

    public void setTextViewDialog(TextView textViewDialog) {
        this.textViewDialog = textViewDialog;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        if (listener != null)
            this.onLetterChangeListener = listener;
    }

    public void setCityChar(String[] cityChar) {
        this.cityChar = cityChar;
        needInvalidate();
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SideBar_sidebar_char_color_normal:
                    this.sideCharNormalColor = a.getColor(attr, Color.LTGRAY);
                    break;
                case R.styleable.SideBar_sidebar_char_color_pressed:
                    this.sideCharNormalColor = a.getColor(attr, Color.LTGRAY);
                    break;
                case R.styleable.SideBar_sidebar_char_size:
                    this.sideCharSize = (int) a.getDimension(attr, 30);
                    break;
                case R.styleable.SideBar_peek_char_size:
                    this.peekCharSize = (int) a.getDimension(attr, 55);
                    break;
                case R.styleable.SideBar_peek_char_color:
                    this.sideCharNormalColor = a.getColor(attr, Color.LTGRAY);
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (textViewDialog != null) {
            textViewDialog.setTextColor(peekCharColor);
            //textViewDialog.setTextSize(peekCharSize);
        }

        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        int signleHeight = mHeight / cityChar.length;
        paint.setTextSize(sideCharSize);
        for (int i = 0; i < cityChar.length; i++) {  //挨个画字母
            paint.setColor(sideCharNormalColor);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            String letter = cityChar[i];
            float posX = mWidth / 2 - paint.measureText(letter) / 2;
            float posY = signleHeight * (i + 1) - (signleHeight / 2 - paint.measureText(letter) / 2);
            if (i == choose) {
                paint.setColor(sideCharPressColor);
                paint.setFakeBoldText(true);
            }
            canvas.drawText(letter, posX, posY, paint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int oldChoose = choose;
        float touY = event.getY();
        //根据触摸到的y的高度比例来判断当前触摸的是第几个字母
        int index = (int) (touY / getMeasuredHeight() * cityChar.length);
        Log.i("dispatchTouchEvent-> ", "Y:" + touY + "\t" + "index:" + index);
        switch (action) {
            case MotionEvent.ACTION_UP:
                choose = -1;
                if (textViewDialog != null) {
                    textViewDialog.setVisibility(GONE);
                    textViewDialog.setText("");
                }
                needInvalidate();
                break;

            default:   //除了抬起，其他事件另作处理
                if (index != oldChoose) {
                    if (index >= 0 && index < cityChar.length) {
                        if (textViewDialog != null) {
                            textViewDialog.setText(cityChar[index]);
                            textViewDialog.setVisibility(VISIBLE);
                        }
                        if (onLetterChangeListener != null)
                            onLetterChangeListener.onLetterChange(cityChar[index], index);
                        choose = index;
                        needInvalidate();
                    }
                }
                break;
        }


        return true;   //点击后拦截所有的触摸事件
    }

    /**
     * 字母改变监听接口
     */
    public interface OnLetterChangeListener {
        void onLetterChange(String letter, int index);
    }

    public void needInvalidate() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
