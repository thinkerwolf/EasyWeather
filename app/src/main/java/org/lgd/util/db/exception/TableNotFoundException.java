package org.lgd.util.db.exception;

/**
 * Created by Bruce Wu on 2016/4/10.
 */
public class TableNotFoundException extends Exception {

    public TableNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TableNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public TableNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
