package org.lgd.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import lgd.org.R;

/**
 * 自定义搜索框
 * Created by wukai on 2016/6/11.
 */
public class CleatEditText extends EditText implements TextWatcher, OnFocusChangeListener {

    private Drawable mClearDrawable;

    private OnTextChangeListener listener;

    public CleatEditText(Context context) {
        this(context, null);
    }

    public CleatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.btn_remove_bg);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setIconClearVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 设置清空按钮是否可见
     *
     * @param b
     */
    private void setIconClearVisible(boolean b) {
        Drawable right = b ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x = event.getX();
                if (x > getWidth() / 2 - getPaddingLeft() / 2 && x < getWidth() - getPaddingRight()) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setIconClearVisible(getText().length() > 0);
        } else {
            this.setText("");
            setIconClearVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setIconClearVisible(text.length() > 0);
        if (listener != null) {
            listener.onTextChange(text, text.length() <= 0);
        }
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public interface OnTextChangeListener {
        void onTextChange(CharSequence text, boolean isnull);
    }
}
