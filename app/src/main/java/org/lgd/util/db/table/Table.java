package org.lgd.util.db.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 表的信息
 * Created by Bruce Wu on 2016/4/3.
 */
public class Table {
    //TODO 表结构完善
    private String tableName;   //表名
    private static int i = 0;

    //所有的列  包括列的信息
    private List<Column> columns = new ArrayList<>();

    public Table(Class<?> clazz) {
        org.lgd.util.db.annotation.Table tablename = clazz.getAnnotation(org.lgd.util.db.annotation.Table.class);
        if (tablename != null) {
            setTableName(tablename.value());
        } else {
            setTableName("table" + i);
        }
        i++;
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().contains("$")) {
                continue;
            }
            Column column = new Column(field);
            this.columns.add(column);
        }
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
