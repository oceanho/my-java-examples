package com.oceanho.demo.repository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public boolean checkUser(String userName, String password){
        return true;
    }
}
