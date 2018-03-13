package org.stacspics.rest;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserList {
    private static final CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();

    static {
        userList.add(
                new User.UserBuilder()
                        .id()
                        .name("user1")
                        .numberOfComments(0)
                        .email("user1@mockmail.com")
                        .build()
        );

        userList.add(
                new User.UserBuilder()
                        .id()
                        .name("user2")
                        .numberOfComments(0)
                        .email("user2@mockmail.com")
                        .build()
        );

        userList.add(
                new User.UserBuilder()
                        .id()
                        .name("user3")
                        .numberOfComments(0)
                        .email("user3@mockmail.com")
                        .build()
        );

        userList.add(
                new User.UserBuilder()
                        .id()
                        .name("user4")
                        .numberOfComments(0)
                        .email("user4@mockmail.com")
                        .build()
        );
    }

    private UserList(){}

    public static CopyOnWriteArrayList<User> getInstance(){
        return userList;
    }
    public static void testList(){
        CopyOnWriteArrayList<User> list = UserList.getInstance();

        list.stream()
                .forEach(i-> System.out.println(i));

    }
    public static void main(String[] args) {
        UserList.testList();
    }
}
