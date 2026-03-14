package com.cts.eventsphere.exception.user;

public class RefreshFailedException extends RuntimeException {
    public RefreshFailedException(String userId){
        super(String.format("Failed to authenticate user: %s, please login again", userId));
    }
}
