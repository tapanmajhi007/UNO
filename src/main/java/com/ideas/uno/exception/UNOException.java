package com.ideas.uno.exception;

/**
 * @Author TAPANM
 */
public class UNOException extends Exception {
    public UNOException() {
    }

    public UNOException(String message) {
        super(message);
    }

    public UNOException(String message, Throwable cause) {
        super(message, cause);
    }

    public UNOException(Throwable cause) {
        super(cause);
    }

    public UNOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
