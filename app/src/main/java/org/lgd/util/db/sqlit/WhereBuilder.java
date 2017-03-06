package org.lgd.util.db.sqlit;

import android.util.Log;

import org.lgd.util.db.annotation.AutoIncrease;
import org.lgd.util.db.annotation.NotNull;
import org.lgd.util.db.exception.WhereBuilderException;
import org.lgd.util.db.table.ColumnUtils;
import org.lgd.util.db.table.TableUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库where创建
 * Created by Bruce Wu on 2016/4/6.
 */
public class WhereBuilder {

    private List<String> whereItems;

    public WhereBuilder(List<String> whereItems) {
        this.whereItems = whereItems;
    }

    public WhereBuilder() {
        whereItems = new ArrayList<>();
    }

    public List<String> getWhereItems() {
        return whereItems;
    }

    public void setWhereItems(List<String> whereItems) {
        this.whereItems = whereItems;
    }

    /**
     * 创建一个Wherebuilder
     *
     * @param columnName : 列名
     * @param op         ：操作方式
     * @param value      ：操作值
     * @return
     */
    public static WhereBuilder b(String columnName, String op, Object value) {
        StringBuffer sb = new StringBuffer();
        sb.append(columnName);
        sb.append(" ");
        sb.append(op);
        sb.append(" ");
        sb.append(value);
        sb.append(" ");
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.whereItems.add(sb.toString());
        return whereBuilder;
    }

    /**
     * where表达式
     *
     * @param exp
     * @return
     */
    public static WhereBuilder b(String exp) {
        StringBuffer sb = new StringBuffer();
        sb.append(exp);
        sb.append(" ");
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.whereItems.add(sb.toString());
        return whereBuilder;
    }

    /**
     * and语句
     *
     * @param columnName
     * @param op
     * @param value
     * @return
     */
    public WhereBuilder and(String columnName, String op, Object value) {
        StringBuffer sb = new StringBuffer();
        sb.append(" AND ");
        sb.append(columnName + " ");
        sb.append(op);
        sb.append(" ");
        sb.append(String.valueOf(value));
        sb.append(" ");
        this.whereItems.add(sb.toString());
        return this;
    }

    public WhereBuilder and(String exp) {
        StringBuffer sb = new StringBuffer();
        sb.append(" AND ");
        sb.append(exp);
        sb.append(" ");
        this.whereItems.add(sb.toString());
        return this;
    }

    /**
     * @return
     */
    public WhereBuilder or(String columnName, String op, Object value) {
        StringBuffer sb = new StringBuffer();
        sb.append(" or ");
        sb.append(columnName + " ");
        sb.append(op);
        sb.append(" ");
        sb.append(String.valueOf(value));
        sb.append(" ");
        this.whereItems.add(sb.toString());

        return this;
    }

    public WhereBuilder or(String exp) {
        StringBuffer sb = new StringBuffer();
        sb.append(exp);
        sb.append(" ");
        this.whereItems.add(sb.toString());
        return this;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" WHERE ");
        for (String item : whereItems) {
            sb.append(item);
        }
        sb.append(" ");
        Log.i("whereBuilder:", sb.toString());
        return sb.toString();
    }

    /**
     * 利用对象创建WhereBuilder
     * 主要用于删除数据或者检查数据是否存在
     *
     * @param obj
     * @return
     */
    public static WhereBuilder b(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        WhereBuilder whereBuilder = new WhereBuilder();
        Field[] fields = clazz.getDeclaredFields();
        try {
            if (fields != null) {
                int i = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().contains("$")) {
                        continue;
                    }
                    if (null == field.get(obj)) {
                        continue;
                    }
                    if (ColumnUtils.isAutoIncrease(field)) {
                        continue;
                    }
                    if (ColumnUtils.isIgnore(field)) {
                        continue;
                    }
                    Object value = null;
                    if (field.get(obj) instanceof String) {
                        value = "\"" + field.get(obj) + "\"";
                    } else {
                        value = field.get(obj);
                    }
                    if (i == 0) {
                        whereBuilder.getWhereItems().add(field.getName() + " = " + value);
                    } else {
                        whereBuilder.and(field.getName() + " = " + value);
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return whereBuilder;
    }

    /**
     * 利用数组来创建Wherebuilder
     *
     * @param selectArgs
     * @param selectValues
     * @return
     */
    public static WhereBuilder b(String[] selectArgs, String[] selectValues) throws WhereBuilderException {
        if (selectArgs.length != selectValues.length) {
            throw new WhereBuilderException("selectArgs.length must equal with selectionValues.length");
        } else {
            WhereBuilder whereBuilder = new WhereBuilder();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < selectArgs.length; i++) {
                if (i == 0) {
                    whereBuilder.getWhereItems().add(selectArgs[i] + " = " + selectValues[i]);
                } else {
                    whereBuilder.and(selectArgs[i], " = ", selectValues[i]);
                }
            }
            return whereBuilder;
        }


    }
}
