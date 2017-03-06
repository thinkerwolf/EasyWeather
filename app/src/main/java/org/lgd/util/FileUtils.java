package org.lgd.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import lgd.org.R;

/**
 * 文件工具类，用来解析读取文件中的数据，并进行解析
 * Created by wukai on 2016/5/7.
 */
public class FileUtils {

    /**
     * 从相应文件路径中去除字符串
     * 文件不存在 返回null
     *
     * @param path :文件路径
     * @return ：字符串
     */
    public static String fromFile(String path) {
        String res = null;
        File file = new File(path);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(fis));
                StringBuilder sb = new StringBuilder();
                String b;
                while ((b = lnr.readLine()) != null) {
                    sb.append(b);
                }
                res = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 直接利用文件对象想获取字符串
     * 如果不存在返回null
     *
     * @param file :文件对象
     * @return ：字符串
     */
    public static String fromFile(File file) {
        String res = null;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(fis));
                StringBuilder sb = new StringBuilder();
                String b;
                while ((b = lnr.readLine()) != null) {
                    sb.append(b);
                }
                res = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 直接利用文件对象想获取字符串
     * 如果不存在返回null
     *
     * @param context :context
     * @param id      :资源ID
     * @return ：字符串
     */
    public static String fromFile(Context context, int id) {
        String res = null;
        InputStream is = context.getResources().openRawResource(id);
        if (is != null) {
            try {
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String b;
                while ((b = lnr.readLine()) != null) {
                    sb.append(b);
                }
                res = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }



}
