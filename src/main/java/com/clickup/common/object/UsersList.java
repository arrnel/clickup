package com.clickup.common.object;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UsersList extends ArrayList {

    public UsersList() {
        super();
        readFromCSVToUsersList();
    }

    void readFromCSVToUsersList() {

        List<List<String>> usersDataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/configs/files/users.csv"))) {

            String[] values;
            while ((values = csvReader.readNext()) != null) {

                usersDataList.add(Arrays.asList(values));

                User user = new User(
                        usersDataList.get(0).get(0),
                        usersDataList.get(0).get(1),
                        usersDataList.get(0).get(2),
                        usersDataList.get(0).get(3)
                );

                add(user);
                usersDataList.clear();
            }

        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public User getRandomUser() {
        Collections.shuffle(this);
        return (User) this.get(0);
    }

    public List<User> getAllUsers() {
        return this;
    }


}
