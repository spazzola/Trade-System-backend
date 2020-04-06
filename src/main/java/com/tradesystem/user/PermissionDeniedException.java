package com.tradesystem.user;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {

    }

    public PermissionDeniedException(String message) {
        super(message);
    }

}
