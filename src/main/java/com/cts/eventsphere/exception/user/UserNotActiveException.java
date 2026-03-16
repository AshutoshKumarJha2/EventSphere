package com.cts.eventsphere.exception.user;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String userId){
        super(String.format("User with id %s is not active", userId));
    }
}
