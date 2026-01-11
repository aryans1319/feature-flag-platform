package com.aryan.featureflags.exception;

public class FeatureAlreadyExistsException  extends RuntimeException  {
    public FeatureAlreadyExistsException(String message){
        super(message);
    }
}
