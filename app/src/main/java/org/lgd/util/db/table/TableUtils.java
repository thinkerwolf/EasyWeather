package org.lgd.util.db.table;


import org.lgd.util.db.annotation.Table;

/**
 * 表信息工具类
 * 改表操作
 * Created by Bruce Wu on 2016/4/3.
 */
public class TableUtils {

    /**
     * 获取表的名称
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {

        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno != null) {
            return tableAnno.value();
        }else {
            return null;
        }


    }


}
