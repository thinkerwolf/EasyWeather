package org.lgd.util.db.sqlit;

import android.database.Cursor;

import org.lgd.util.db.table.Column;

import java.lang.reflect.Field;


/**
 * Created by Bruce Wu on 2016/4/10.
 */
public class CursorUtil {


    /**
     * 获取当前的数据
     *
     * @param cursor
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getEntity(Cursor cursor, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        try {
            if (fields != null) {
                T entity = clazz.newInstance();
                for (Field field : fields) {
                    int index = cursor.getColumnIndex(field.getName());
                    int type = cursor.getType(index);
                    if (field.getName().contains("$")) {
                        continue;
                    }
                    Column column = new Column(clazz, field.getName());
                    switch (type) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            column.setValue2Entity(entity, cursor.getInt(index));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            column.setValue2Entity(entity, cursor.getFloat(index));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            column.setValue2Entity(entity, cursor.getString(index));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            column.setValue2Entity(entity, cursor.getBlob(index));
                            break;
                        default:
                            column.setValue2Entity(entity, null);
                    }

                }
                return entity;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
