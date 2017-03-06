package org.lgd.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.lgd.util.db.exception.WhereBuilderException;
import org.lgd.util.db.sqlit.CursorUtil;
import org.lgd.util.db.sqlit.DaoConfig;
import org.lgd.util.db.sqlit.Selector;
import org.lgd.util.db.sqlit.SqlInfo;
import org.lgd.util.db.sqlit.SqlInfoBuilder;
import org.lgd.util.db.sqlit.WhereBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据库帮助类，可以创建多张表
 * Created by Bruce Wu on 2016/4/3.
 */
public class DBUtils {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "weather.db";
    public static int DB_VERSION = 1;

    /**
     * 数据库类
     */
    private SQLiteDatabase sqLiteDatabase;

    private static DBUtils mInstence;
    private boolean alllowTransaction = true;

    public DBUtils(DaoConfig daoConfig) {
        this.sqLiteDatabase = createDatabase(daoConfig);
    }

    /**
     * 创建SQLiteDateBase
     * 并返回SQLiteDatabase类
     *
     * @param daoConfig : 数据库的配置类
     * @return sqlitedatabase
     */
    private SQLiteDatabase createDatabase(DaoConfig daoConfig) {
        SQLiteDatabase database = null;
        Context context = daoConfig.getContext();
        String dbName = daoConfig.getDbName();
        int version = daoConfig.getVersion();
        String dir = daoConfig.getDir();

        if (StringUtils.isEmpty(dbName)) {
            dbName = DB_NAME;
        }
        if (version < 0) {
            version = DB_VERSION;
        }
        if (!TextUtils.isEmpty(dir)) {
            File file = new File(dir, dbName);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
        } else {
            database = context.openOrCreateDatabase(dbName, SQLiteDatabase.OPEN_READWRITE, null);
        }
        return database;
    }


    public synchronized static DBUtils getInstence(DaoConfig daoConfig) {
        if (mInstence == null) {
            mInstence = new DBUtils(daoConfig);
        }
        return mInstence;
    }

    public static DBUtils create(@NonNull Context context) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        return getInstence(config);
    }

    public static DBUtils create(@NonNull Context context, @Nullable String dbName) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        return getInstence(config);
    }

    public static DBUtils create(@NonNull Context context, @Nullable String dbName, int version) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        config.setVersion(version);
        return getInstence(config);
    }

    public static DBUtils create(Context context, String dbName, String dir) {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        config.setDir(dir);
        return getInstence(config);
    }

    /**
     * 保存或者更新数据
     *
     * @param obj          : 插入或者更新对象的数据类
     * @param selectArgs   : 修改那一条数据的str
     * @param selectValues :
     */
    public void insertOrUpdate(Object obj, String[] selectArgs, String[] selectValues) {
        if (findOneExits(obj)) {
            update(obj, selectArgs, selectValues);
        } else {
            insert(obj);
        }

    }

    /**
     * 更新数据
     *
     * @param obj
     */
    public void update(Object obj, String[] selectArgs, String[] selectValues) {
        try {
            beginTransaction();
            WhereBuilder whereBuilder = WhereBuilder.b(selectArgs, selectValues);
            SqlInfo sqlInfo = SqlInfoBuilder.updateDb(obj, whereBuilder);
            exeNonQuery(sqlInfo);
            setSuccessfulTransaction();
        } catch (WhereBuilderException e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }
    }

    public void save(Object entry) {
        try {
            beginTransaction();
            createTableIfNotExist(entry.getClass());
            setSuccessfulTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }

    }

    /**
     * 查询数据库中所有的数据
     * <T> List<T>指添加数据的时候只能指定一种类型的数据
     * 利用安卓的API进行查询
     * 自己去拼装sql语句
     *
     * @param clazz
     * @return
     */
    public <T> List<T> findAll(Class<T> clazz) {
        try {
            beginTransaction();
            //sqLiteDatabase.query()
            Selector selector = Selector.from(clazz);
            List<T> list = this.find(selector, clazz);
            if (list != null) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }

        return null;
    }

//    /**
//     * 测试
//     *
//     * @return
//     */
//    public List<CityEntry> test() {
//        try {
//            beginTransaction();
//            List<CityEntry> list = new ArrayList<>();
//            Cursor cursor = this.sqLiteDatabase.query("cities", null, null, null, null, null, null);
//            while (cursor.moveToNext()) {
//                CityEntry entry = new CityEntry();
//                entry.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
//                list.add(entry);
//            }
//            return list;
//
//        } finally {
//            endTransaction();
//        }
//    }

    /**
     * 查询第一个
     *
     * @param clazz
     * @param <T>
     */
    public <T> T findFirst(Class<T> clazz) {
        List list = findAll(clazz);
        if (null == list || list.size() <= 0) {
            return null;
        } else {
            return (T) list.get(0);
        }
    }

    /**
     * 通过Selector来查找
     * 这水好深啊
     *
     * @param selector : SELECT * FROM映射类对象
     * @param <T>      : 数据库数据映射类
     * @return
     */
    public <T> List<T> find(Selector selector, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            beginTransaction();
            Cursor cursor = sqLiteDatabase.rawQuery(selector.toString(), null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(CursorUtil.getEntity(cursor, clazz));
                }
                return list;
            }

        } finally {
            endTransaction();
        }
        return null;
    }

    /**
     * 判断某一个对象是否存在
     *
     * @param obj
     * @return
     */
    private boolean findOneExits(Object obj) {
        WhereBuilder whereBuilder = WhereBuilder.b(obj);
        Selector selector = Selector.from(obj.getClass()).where(whereBuilder);
        List<?> list = find(selector, obj.getClass());
        if (null == list || list.size() <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 往数据库中插入数据
     * 插入成功
     *
     * @return
     */
    public void insert(Object obj) {
        try {
            beginTransaction();
            SqlInfo sqlInfo = SqlInfoBuilder.saveToTable(obj);
            exeNonQuery(sqlInfo);
            setSuccessfulTransaction();
        } finally {
            endTransaction();
        }
        //return true;
    }

    /**
     * 删除一条数据
     *
     * @param obj : 删除数据的对象
     */
    public void delete(Object obj) {
        try {
            beginTransaction();
            SqlInfo sqlInfo = SqlInfoBuilder.deleteFromDb(obj);
            exeNonQuery(sqlInfo);
            setSuccessfulTransaction();
        } finally {
            endTransaction();
        }
    }

    /**
     * 删除指定数据库的所有数据
     * add by wukai on 20160511
     *
     * @param clazz :数据类型
     */
    public void deleteAll(Class<?> clazz) {
        try {
            beginTransaction();
            exeNonQuery(SqlInfoBuilder.deleteAll(clazz));
            setSuccessfulTransaction();
        } finally {
            endTransaction();
        }

    }

    /**
     * 创建表
     *
     * @param aClass
     */
    private void createTableIfNotExist(Class<?> aClass) {
        //TODO 创建新表
        SqlInfo info = SqlInfoBuilder.buildCreateTableIfNotExist(aClass);
        exeNonQuery(info);
    }

    /**
     * 判断表是否存在
     *
     * @param clazz : 数据类
     * @return
     */
    private boolean tableIsExistsOrNot(Class<?> clazz) {
        //TODO 判断表是否存在
        return true;
    }

    /**
     * @param info
     */
    private void exeNonQuery(SqlInfo info) {
        try {
            sqLiteDatabase.execSQL(info.getSql());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param sql :根据SQl语句查询当前
     * @return
     */
    private Cursor exeQuery(String sql) {
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //这两个货什么意思
    private Lock wiriteLock = new ReentrantLock();
    private volatile boolean writeLocked = false;

    /***
     * private tools
     ****/

    /**
     * 开启事务
     */
    private void beginTransaction() {
        if (alllowTransaction) {
            this.sqLiteDatabase.beginTransaction();
        } else {
            wiriteLock.lock();
            writeLocked = true;
        }
    }


    /**
     * 关闭事务
     */
    private void endTransaction() {
        if (alllowTransaction) {
            this.sqLiteDatabase.endTransaction();
        }
        if (writeLocked) {   //只要有结束，就释放当前对象
            wiriteLock.unlock();
            writeLocked = false;
        }
    }

    private void setSuccessfulTransaction() {
        if (alllowTransaction) {
            this.sqLiteDatabase.setTransactionSuccessful();
        }
    }

    /**
     * 配置此Util是否可以开启事务
     *
     * @param b 配置数据库事务是否开启 true开启 false关闭 无法进行插入和修改操作
     * @return 返回此DBUtils对象
     */
    public DBUtils configTransaction(boolean b) {
        this.alllowTransaction = b;
        return this;
    }
}
