package com.example.demo.validator;

public class PasswordValidator {

    public static boolean arePasswordAndRePasswordSame(String password, String rePassword) {
        return password.equals(rePassword);
    }
}
