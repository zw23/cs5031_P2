package org.stacspics.rest;

import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserList {

    private static final CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Comment> cmtList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Photo> ptList = new CopyOnWriteArrayList<>();
    static {
        User user1 = new User.UserBuilder()
                .id()
                .name("user1")
                .numberOfComments(0)
                .email("user1@mockmail.com")
                .notifications(null)
                .build();

        User user2 = new User.UserBuilder()
                .id()
                .name("user2")
                .numberOfComments(0)
                .email("user2@mockmail.com")
                .notifications(null)
                .build();

        User user3 = new User.UserBuilder()
                .id()
                .name("user3")
                .numberOfComments(0)
                .email("user3@mockmail.com")
                .notifications(null)
                .build();

        User user4 = new User.UserBuilder()
                .id()
                .name("user4")
                .numberOfComments(0)
                .email("user4@mockmail.com")
                .notifications(null)
                .build();


        Photo mockPhoto0 = new Photo.PhotoBuilder()
                .id()
                .name("Photo0")
                .description("It is the first photo on the server")
                .postedBy(user1.getId())
                .comments(null)
                .build();

        Photo mockPhoto1 = new Photo.PhotoBuilder()
                .id()
                .name("Photo 1")
                .description("Me and my friends!!")
                .postedBy(user2.getId())
                .comments(null)
                .build();

        Photo mockPhoto2 = new Photo.PhotoBuilder()
                .id()
                .name("Photo 2")
                .description("Some animals!!!")
                .postedBy(user3.getId())
                .comments(null)
                .build();


        Comment mockComment1 = new Comment.CommentBuilder().id()
                .time()
                .userId(user1.getId())
                .content("This is user1 comment")
                .replies(null)
                .upvote()
                .downvote()
                .isReply(false)
                .originalPostId(mockPhoto0.getId())
                .build();



        Comment mockComment2 = new Comment.CommentBuilder().id()
                .time()
                .userId(user2.getId())
                .content("This is user2 comment on user1 !!!")
                .replies(null)
                .upvote()
                .downvote()
                .originalPostId(mockComment1.getId())
                .isReply(true)
                .build();



        Comment mockComment3 = new Comment.CommentBuilder().id()
                .time()
                .userId(user3.getId())
                .content("This is user3 comment on user2 !!!")
                .replies(null)
                .upvote()
                .downvote()
                .originalPostId(mockComment2.getId())
                .isReply(true)
                .build();

        Comment mockComment4 = new Comment.CommentBuilder().id()
                .time()
                .userId(user4.getId())
                .content("This is user4 !!!")
                .replies(null)
                .upvote()
                .downvote()
                .isReply(false)
                .originalPostId(mockPhoto0.getId())
                .build();
        Comment mockComment5 = new Comment.CommentBuilder().id()
                .time()
                .userId(user3.getId())
                .content("This is user3 comment !!!")
                .replies(null)
                .upvote()
                .downvote()
                .isReply(false)
                .originalPostId(mockPhoto0.getId())
                .build();

        Comment mockComment6 = new Comment.CommentBuilder().id()
                .time()
                .userId(user4.getId())
                .content("This is user4 reply on comment 1!!!")
                .replies(null)
                .upvote()
                .downvote()
                .isReply(true)
                .originalPostId(mockComment1.getId())
                .build();



        Notification nt1 = new Notification.NotificationBuilder().id()
                .userId(user1.getId())
                .isReply(false)
                .originalId(mockPhoto1.getId())
                .build();

        Notification nt2 = new Notification.NotificationBuilder().id()
                .userId(user2.getId())
                .isReply(true)
                .originalId(mockComment1.getId())
                .build();

        Notification nt3 = new Notification.NotificationBuilder().id()
                .userId(user3.getId())
                .isReply(true)
                .originalId(mockComment2.getId())
                .build();

        Notification nt4 = new Notification.NotificationBuilder().id()
                .userId(user4.getId())
                .isReply(false)
                .originalId(mockPhoto0.getId())
                .build();

        Notification nt5 = new Notification.NotificationBuilder().id()
                .userId(user3.getId())
                .isReply(false)
                .originalId(mockPhoto0.getId())
                .build();

        Notification nt6 = new Notification.NotificationBuilder().id()
                .userId(user4.getId())
                .isReply(true)
                .originalId(mockComment6.getId())
                .build();




        user1.getNotifications().add(nt2);
        user1.getNotifications().add(nt4);
        user1.getNotifications().add(nt5);

        user2.getNotifications().add(nt1);
        user2.getNotifications().add(nt3);
        user2.getNotifications().add(nt6);

        mockComment1.getReplies().add(mockComment2);

        mockComment2.getReplies().add(mockComment3);
        mockComment2.getReplies().add(mockComment6);

        mockPhoto0.getComments().add(mockComment4);
        mockPhoto0.getComments().add(mockComment5);
        mockPhoto1.getComments().add(mockComment1);


        cmtList.add(mockComment1);
        cmtList.add(mockComment2);
        cmtList.add(mockComment3);
        cmtList.add(mockComment4);
        cmtList.add(mockComment5);
        cmtList.add(mockComment6);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        ptList.add(mockPhoto0);
        ptList.add(mockPhoto1);
        ptList.add(mockPhoto2);

    }

    private UserList(){}

    public static CopyOnWriteArrayList<User> getInstance(){
        return userList;
    }
    public static CopyOnWriteArrayList<Comment> getCommentList(){ return cmtList; }
    public static CopyOnWriteArrayList<Photo> getPhotoList(){return ptList;}
}
