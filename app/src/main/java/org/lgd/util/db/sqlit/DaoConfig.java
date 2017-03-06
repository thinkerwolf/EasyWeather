package org.lgd.util.db.sqlit;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Context context, String name, CursorFactory factory, int version,
 * DatabaseErrorHandler errorHandler
 * 创建数据库的新配置
 * Created by Bruce Wu on 2016/4/3.
 */
public class DaoConfig {

    @NonNull
    private Context context;   //上下文
    @Nullable
    private String dbName;     //数据库名
    @Nullable
    private SQLiteDatabase.CursorFactory factory;  //工厂
    private int version;       //版本
    @Nullable
    private DatabaseErrorHandler errorHandler;  //错误流程
    @Nullable
    private String dir;     //存储路径

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public SQLiteDatabase.CursorFactory getFactory() {
        return factory;
    }

    public void setFactory(SQLiteDatabase.CursorFactory factory) {
        this.factory = factory;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public DatabaseErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(DatabaseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
