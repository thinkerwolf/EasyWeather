package org.lgd.util.db.exception;

/**
 * Created by Bruce Wu on 2016/4/12.
 */
public class WhereBuilderException extends Exception {

    public WhereBuilderException() {

    }

    public WhereBuilderException(String detailMessage) {
        super(detailMessage);
    }

    public WhereBuilderException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WhereBuilderException(Throwable throwable) {
        super(throwable);
    }
}
