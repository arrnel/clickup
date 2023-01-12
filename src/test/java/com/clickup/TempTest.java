package com.clickup;

import com.clickup.common.object.User;
import com.clickup.common.object.UsersList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class TempTest {

//    @Test
    void test(){
        UsersList usersList = new UsersList();
        User user = usersList.getRandomUser();
        System.out.println(user.toString());
        System.out.println("\n");
        System.out.println(usersList);
    }
}
