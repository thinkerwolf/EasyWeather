package org.lgd.util.db.table;

import org.lgd.util.StringUtils;
import org.lgd.util.db.annotation.AutoIncrease;
import org.lgd.util.db.annotation.Column;
import org.lgd.util.db.annotation.Ignore;
import org.lgd.util.db.annotation.NotNull;
import org.lgd.util.db.annotation.PrimaryKey;

import java.lang.reflect.Field;

/**
 * 列工具
 * 通过注解得到相应的属性值
 * Created by Bruce Wu on 2016/4/3.
 */
public class ColumnUtils {
    /**
     * 获取列名
     *
     * @return
     */
    public static String columnName(Field field) {
        if (field != null) {
            return field.getName();
        }
        return null;
    }

    /**
     * 是否为空
     *
     * @param field
     * @return
     */
    public static boolean isNoNull(Field field) {

        if (field != null) {
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (notNull != null) {
                return notNull.value();
            }
        }
        return false;
    }

    public static boolean isAutoIncrease(Field field) {
        String name = field.getType().getName();
        if (name.endsWith("Integer") || name.endsWith("int")) {
            AutoIncrease autoIncrease = field.getAnnotation(AutoIncrease.class);
            if (autoIncrease != null) {
                return autoIncrease.value();
            }

        }
        return false;
    }

    public static boolean isUnsigned(Field field) {
        String name = field.getType().getName();
        if (name.endsWith("Integer") || name.endsWith("int")) {
            AutoIncrease autoIncrease = field.getAnnotation(AutoIncrease.class);
            if (autoIncrease != null) {
                return autoIncrease.value();
            }
        }
        return false;

    }

    public static boolean isPrimaryKey(Field field) {
        if (field != null) {
            PrimaryKey key = field.getAnnotation(PrimaryKey.class);
            if (key != null) {
                return key.value();
            }
        }
        return false;
    }

    /**
     * 获取类型
     *
     * @param field
     * @return
     */
    public static ColumnType columnType(Field field) {
        String name = field.getType().getName();
        ColumnType columnType;
        switch (name) {
            case "String":
                columnType = ColumnType.TEXT;
                break;
            case "int":

            case "Integer":
                columnType = ColumnType.INTEGER;
                break;
            case "float":
            case "double":
                columnType = ColumnType.REAL;
                break;
            default:
                columnType = ColumnType.TEXT;
        }
        return columnType;
    }

    /**
     * 是否忽略该行
     * @param field
     * @return
     */
    public static boolean isIgnore(Field field) {
        if (field != null) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null) {
                return ignore.value();
            }
        }
        return false;
    }
}
