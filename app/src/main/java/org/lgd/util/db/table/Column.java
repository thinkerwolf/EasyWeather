package org.lgd.util.db.table;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 所有数据库的列
 * Created by Bruce Wu on 2016/4/3.
 */
public class Column {

    /**
     * 创建一个新列
     */
    public Column(Field field) {

        init(field);
    }

    public Column(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            String value = String.valueOf(fieldName.charAt(0));
            StringBuffer sb = new StringBuffer();
            for (Method method : clazz.getDeclaredMethods()) {
                sb.append(method.getName() + "\n");
            }
            Log.i("AllMethods", sb.toString());
            Log.i("field",field.getName());
            this.setMethod = clazz.getDeclaredMethod("set" + fieldName.replaceFirst(value, value.toUpperCase()), field.getType());
            this.getMethod = clazz.getDeclaredMethod("get" + fieldName.replaceFirst(value, value.toUpperCase()));
            init(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public void setValue2Entity(Object entity, Object value) {
        if (this.setMethod != null) {
            try {
                setMethod.invoke(entity, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(Field field) {
        /**
         * 获取所有自定义的属性变量
         */
        setColumnName(ColumnUtils.columnName(field));
        setNull(ColumnUtils.isNoNull(field));
        setAutoIncreament(ColumnUtils.isAutoIncrease(field));
        setPrimaryKey(ColumnUtils.isPrimaryKey(field));
        setUnsigned(ColumnUtils.isUnsigned(field));
        setColumnType(ColumnUtils.columnType(field));
    }


    /**
     * 数据库类型
     * 列类型
     */
    private ColumnType columnType;

    /**
     * 列名
     */
    private String columnName;
    /**
     * 是否为空
     */
    private boolean isNull;
    /**
     * 是否是主键
     */
    private boolean isPrimaryKey;
    /**
     * 是否是无符号的
     */
    private boolean isUnsigned;
    /**
     * 能否自增
     */
    private boolean isAutoIncreament;

    private Method setMethod;

    private Method getMethod;

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
    }

    public boolean isAutoIncreament() {
        return isAutoIncreament;
    }

    public void setAutoIncreament(boolean autoIncreament) {
        isAutoIncreament = autoIncreament;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }
}
