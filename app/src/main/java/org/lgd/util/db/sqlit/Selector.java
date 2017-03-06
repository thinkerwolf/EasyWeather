package org.lgd.util.db.sqlit;

import org.lgd.util.db.exception.TableNotFoundException;
import org.lgd.util.db.table.TableUtils;

import java.util.List;

/**
 * 查询时的选择器
 * where如何设置
 * Created by Bruce Wu on 2016/4/4.
 */
public class Selector {
    /**
     * 查询时的显示多少
     */
    private int limit = -1;
    /**
     * 偏移量
     */
    private int offset = -1;
    /**
     * 从某张表获取数据
     */
    private String tableName;
    /**
     * 数据类
     */
    private Class<?> classEntity;

    private WhereBuilder whereBuilder;

    /**
     * 新建clazz
     *
     * @param clazz
     */
    public Selector(Class<?> clazz) {
        this.classEntity = clazz;
        this.tableName = TableUtils.getTableName(clazz);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * 静态方法
     *
     * @param clazz
     * @return
     */
    public static Selector from(Class<?> clazz) {
        return new Selector(clazz);
    }

    /**
     * where操作
     *
     * @param columnName:列名
     * @param op            ：操作  =  !=
     * @param value：值
     * @return
     */
    public Selector where(String columnName, String op, Object value) {
        this.whereBuilder = WhereBuilder.b(columnName, op, value);
        return this;
    }

    public Selector where(WhereBuilder builder) {
        this.whereBuilder = builder;
        return this;
    }

    public Selector limit(int num) {
        this.limit = num;
        return this;
    }

    public Selector offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 返回相应的sql语句
     *
     * @return
     */
    public String toString() {
        //转化成SQl语言
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM ");
        if (tableName != null) {
            sb.append(tableName);
        } else {
            return null;
        }
        if (whereBuilder != null) {
            sb.append(whereBuilder.toString());
        }
        if (limit > 0) {
            sb.append(" limit " + limit);
        }
        if (offset > 0) {
            sb.append(" offset " + offset);
        }

        return sb.toString();
    }
}
