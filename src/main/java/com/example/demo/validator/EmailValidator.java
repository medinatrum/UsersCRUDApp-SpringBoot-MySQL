package com.example.demo.validator;

import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean validate(String email) {

        String emailRegex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(emailRegex);

        return (email != null && pattern.matcher(email).matches());
    }
}
