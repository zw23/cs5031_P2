package org.stacspics.rest;

import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserList {

    private static final CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Comment> cmtList = new CopyOnWriteArrayList<>();

    static {
        User user1 = new User.UserBuilder()
                .id()
                .name("user1")
                .numberOfComments(0)
                .email("user1@mockmail.com")
                .build();



        User user2 = new User.UserBuilder()
                .id()
                .name("user2")
                .numberOfComments(0)
                .email("user2@mockmail.com")
                .build();

        User user3 = new User.UserBuilder()
                .id()
                .name("user3")
                .numberOfComments(0)
                .email("user3@mockmail.com")
                .build();

        User user4 = new User.UserBuilder()
                .id()
                .name("user4")
                .numberOfComments(0)
                .email("user4@mockmail.com")
                .build();




        Comment mockComment1 = new Comment.CommentBuilder().id()
                .time()
                .userId(user1.getId())
                .content("This is user1 comment")
                .replies(null)
                .build();
        Comment mockComment2 = new Comment.CommentBuilder().id()
                .time()
                .userId(user2.getId())
                .content("This is user2 comment on user1 !!!")
                .replies(null)
                .build();
        Comment mockComment3 = new Comment.CommentBuilder().id()
                .time()
                .userId(user3.getId())
                .content("This is user3 comment on user2 !!!")
                .replies(null)
                .build();
        Comment mockComment4 = new Comment.CommentBuilder().id()
                .time()
                .userId(user4.getId())
                .content("This is user4 !!!")
                .replies(null)
                .build();

        mockComment1.getReplies().add(mockComment2);
        mockComment2.getReplies().add(mockComment3);



        cmtList.add(mockComment1);
        cmtList.add(mockComment2);
        cmtList.add(mockComment3);
        cmtList.add(mockComment4);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

    }

    private UserList(){}

    public static CopyOnWriteArrayList<User> getInstance(){
        return userList;
    }

}
