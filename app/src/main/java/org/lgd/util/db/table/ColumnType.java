package org.lgd.util.db.table;

/**
 * 所有的列类型枚举类
 * Created by Bruce Wu on 2016/4/3.
 */
public enum ColumnType {
    INTEGER("INTEGER"), REAL("REAL"), TEXT("TEXT"), BLOB("BLOB");

    private String value;

    ColumnType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
