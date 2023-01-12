package com.clickup.common.object;

import com.clickup.common.configs.UserConfiguration;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.aeonbits.owner.ConfigFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User {

    String fullName;
    String email;
    String password;
    String token;

    public User(String fullName, String email, String password, String token) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String getFullName() {
        if (fullName == null)
            throw new NullPointerException("Full name equals null. Use empty constructor for user generation");
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        if (email == null)
            throw new NullPointerException("Email equals null. Use empty constructor for user generation");
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        if (password == null)
            throw new NullPointerException("Password equals null. Use empty constructor for user generation");
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        if (token == null)
            throw new NullPointerException("Token equals null. Use empty constructor for user generation");
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toString() {
        return String.format("\nUser: {\n" +
                        "\"fullName\": \"%s\",\n" +
                        "\"email\": \"%s\",\n" +
                        "\"password\": \"%s\",\n" +
                        "\"token\": \"%s\"\n" + "}\n",
                fullName, email, password, token);
    }

}
