package com.kenzie.appserver.service;

public class PetNotFoundException extends Throwable {
    public PetNotFoundException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
        System.out.println(s);


    }
}
