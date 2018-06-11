package com.sandbox.filestorage.exception;

public class FileAccessForbiddenException extends RuntimeException {

    public FileAccessForbiddenException(String message) {
        super(message);
    }

    public FileAccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAccessForbiddenException(Throwable cause) {
        super(cause);
    }
}

