package com.example.assignment.service.ex;

public class UserNameDuplicatedExecption extends ServiceException {
    public UserNameDuplicatedExecption() {
        super();
    }

    public UserNameDuplicatedExecption(String message) {
        super(message);
    }

    public UserNameDuplicatedExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameDuplicatedExecption(Throwable cause) {
        super(cause);
    }

    protected UserNameDuplicatedExecption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
