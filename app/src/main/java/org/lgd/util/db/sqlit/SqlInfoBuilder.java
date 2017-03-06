package org.lgd.util.db.sqlit;

import android.util.Log;

import org.lgd.util.StringUtils;
import org.lgd.util.db.table.Column;
import org.lgd.util.db.table.ColumnType;
import org.lgd.util.db.table.ColumnUtils;
import org.lgd.util.db.table.Table;
import org.lgd.util.db.table.TableUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建新的sql语句
 * Created by Bruce Wu on 2016/4/3.
 */
public class SqlInfoBuilder {


    /**
     * 创建新表的信息
     *
     * @return 返回sqlInfo
     */
    public static SqlInfo buildCreateTableIfNotExist(Class<?> clazz) {
        SqlInfo info = new SqlInfo();
        StringBuffer sb = new StringBuffer();
        Table table = new Table(clazz);
        String tableName = table.getTableName();
        if (StringUtils.isEmpty(tableName)) {
            return null;
        }
        sb.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        int size = table.getColumns().size();
        for (int i = 0; i < size; i++) {
            Column column = table.getColumns().get(i);
            sb.append(column.getColumnName() + " ");
            sb.append(column.getColumnType().name() + " ");
//            if (column.isUnsigned()) {
//                sb.append(" UNSIGNED ");
//            }
            if (column.isPrimaryKey()) {
                sb.append(" PRIMARY KEY ");
            }
            if (column.isAutoIncreament()) {
                sb.append(" AUTOINCREMENT ");
            }
            sb.append(",");

        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" )");
        info.setSql(sb.toString());
        return info;
    }

    /**
     * 往数据库中插入数据
     *
     * @return ：返回sqlinfo
     */
    public static SqlInfo saveToTable(Object obj) {
        SqlInfo sqlInfo = new SqlInfo();
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            StringBuffer sb = new StringBuffer();
            StringBuffer sb1 = new StringBuffer();
            sb.append("INSERT INTO ");
            sb.append(TableUtils.getTableName(clazz));
            sb.append(" (");
            sb1.append(" VALUES ( ");
            int len = fields.length;
            for (int i = 0; i < len; i++) {
                Field field = fields[i];
                //Class<?> t = field.getType();
                field.setAccessible(true);
                if (field.getName().contains("$")) {
                    continue;
                }
                if (ColumnUtils.isAutoIncrease(field)) {
                    continue;
                }
                sb1.append(" ");
                sb.append(field.getName());
                sb1.append(" ");
                if (field.get(obj) instanceof String) {
                    sb1.append("\"" + field.get(obj) + "\"");
                } else {
                    sb1.append(field.get(obj));
                }
                sb.append(" ,");
                sb1.append(" ,");

            }
            sb1.deleteCharAt(sb1.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") ");
            sb1.append(")");
            sb.append(sb1);
            sqlInfo.setSql(sb.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return sqlInfo;
    }

    /**
     * 从数据库中删除删除一条数据
     *
     * @param obj : 删除的对象
     * @return : 返回sqlInfo
     */
    public static SqlInfo deleteFromDb(Object obj) {
        SqlInfo sqlInfo = new SqlInfo();
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        WhereBuilder whereBuilder = null;
        List<String> list = new ArrayList<>();
        try {
            if (fields != null) {
                sb.append("DELETE FROM ");
                sb.append(TableUtils.getTableName(clazz));
                int i = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().contains("$")) {
                        continue;
                    }
                    if (null == field.get(obj)) {
                        continue;
                    }
                    Object value = null;
                    if (field.get(obj) instanceof String) {
                        value = "\"" + field.get(obj) + "\"";
                    } else {
                        value = field.get(obj);
                    }
                    if (i == 0) {
                        whereBuilder = WhereBuilder.b(field.getName(), " = ", value);
                    } else {
                        whereBuilder.and(field.getName(), " = ", value);
                    }
                    i++;
                }

                sb.append(whereBuilder.toString());

                sqlInfo.setSql(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("deleteSql", sqlInfo.getSql());
        return sqlInfo;
    }

    /**
     * @param obj :最新的数据 需要更新
     * @return
     */
    public static SqlInfo updateDb(Object obj, WhereBuilder whereBuilder) {
        SqlInfo sqlInfo = new SqlInfo();
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            if (fields != null) {
                StringBuffer sb = new StringBuffer();
                sb.append("UPDATE ");
                sb.append(TableUtils.getTableName(clazz));
                sb.append(" SET ");
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
                    Object value = null;
                    if (field.get(obj) instanceof String) {
                        value = "\"" + field.get(obj) + "\"";
                    } else {
                        value = field.get(obj);
                    }
                    sb.append(field.getName() + " = " + value);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1).append(whereBuilder.toString());
                sqlInfo.setSql(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("update_sql", sqlInfo.getSql());
        return sqlInfo;
    }

    /**
     * 删除指定对象表中所有的数据
     * add by wukai on 20160511
     * @param clazz : 指定的数据对象
     * @return
     */
    public static SqlInfo deleteAll(Class<?> clazz) {
        SqlInfo sqlInfo = new SqlInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(TableUtils.getTableName(clazz));
        sqlInfo.setSql(sb.toString());
        return sqlInfo;
    }

}
