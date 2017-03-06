package org.lgd.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * Created by Bruce Wu on 2016/4/11.
 */
public class SDCardUtil {

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取应用程序包名
     *
     * @return
     */
    public static String getStorepath(Context context) {
        boolean exist = isSDCardExist();
        if (exist) {
            StringBuffer sb = new StringBuffer();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            sb.append(path);
            sb.append("/Android");
            sb.append("/data/");
            sb.append(context.getPackageName());
            sb.append("/");
            File file = new File(sb.toString());
            file.mkdirs();
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * @param bitmap:将bitmap存储到存储卡中
     * @return
     */
    public static File storeBitmapToFile(Bitmap bitmap, Context context, String fileName) {
        boolean exist = isSDCardExist();
        FileOutputStream fos = null;
        File file = null;
        try {
            if (exist) {
                StringBuilder sb = new StringBuilder();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                sb.append(path);
                sb.append("/Android");
                sb.append("/data/");
                sb.append(context.getPackageName());
                sb.append("/" + fileName);
                file = new File(sb.toString());
                if (!file.exists()) {
                    if (!file.isDirectory()) {
                        File par = file.getParentFile();
                        par.mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
