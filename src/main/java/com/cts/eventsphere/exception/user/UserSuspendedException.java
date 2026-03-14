package com.cts.eventsphere.exception.user;

public class UserSuspendedException extends RuntimeException {
    public UserSuspendedException(String userId){
        super(String.format("User with id %s is suspended", userId));
    }
}
