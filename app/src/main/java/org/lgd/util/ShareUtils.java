package org.lgd.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ScrollView;

/**
 * 分享工具和截屏工具 调用系统的分享工具
 * Created by wukai on 2016/5/14.
 */
public class ShareUtils {


    /**
     * 获取当前ScrollView的全部内容并返回一个bitmap
     *
     * @param scrollView :要从中获取内容的ScrollView
     * @return bitmap
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * @param scrollView
     * @param color
     * @param text
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView, int color, String text) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        if (color > 0) {
            canvas.drawColor(color);
        }
        if (text != null) {
            Paint paint = new Paint();
            Rect rect = new Rect();
            paint.setColor(Color.WHITE);
            paint.setTextSize(150f);
            paint.getTextBounds(text, 0, text.length(), rect);
            float x = scrollView.getWidth() / 2 - rect.width() / 2;
            float y = 300 + rect.height();
            canvas.drawText(text, x, y, paint);
        }
        scrollView.draw(canvas);
        return bitmap;
    }

}
