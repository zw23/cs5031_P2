package org.stacspics.rest;

import java.util.concurrent.atomic.AtomicLong;

public class User {


    private final long id;
    private final String name;
    private final int numberOfComments;
    public static final AtomicLong counter = new AtomicLong(0);

    private User(UserBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.numberOfComments = builder.numberOfComments;
    }

    public User(){
        User user = new User.UserBuilder().id().build();
        this.id = user.getId();
        this.name = user.getName();
        this.numberOfComments = user.getNumberOfComments();
    }

    public User(long id, String name, int comments) {
        User user = new User.UserBuilder().id().name(name).numberOfComments(comments).build();

        this.id = user.getId();
        this.name = user.getName();
        this.numberOfComments = user.getNumberOfComments();
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public static AtomicLong getCounter() {
        return counter;
    }

    @Override
    public String toString(){
        return "ID: "+id
                +"\nName: "+name
                +"\nNumber of comments: "+ numberOfComments;
    }
    public static class UserBuilder {
        private long id;
        private String name = "";
        private int numberOfComments;

        public UserBuilder id(){
            this.id = User.counter.getAndIncrement();
            return this;

        }    public UserBuilder id(long id){
            this.id = id;
            return this;
        }

        public UserBuilder name(String name){
            this.name = name;
            return this;
        }

        public UserBuilder numberOfComments(int comments){
            this.numberOfComments = 0;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
