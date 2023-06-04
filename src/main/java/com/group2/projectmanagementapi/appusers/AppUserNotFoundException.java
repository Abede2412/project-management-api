package com.group2.projectmanagementapi.appusers;

import java.lang.RuntimeException;

public class AppUserNotFoundException extends RuntimeException{
    public AppUserNotFoundException(){
        super("User not found");
    }

}
