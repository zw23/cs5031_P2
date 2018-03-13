package org.stacspics.rest;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Comment {
    private final long id;
    private final String time;
    private final long userId;
    private final String content;



    private ArrayList<Comment> replies;
    private static final AtomicLong counter = new AtomicLong(0);


    private Comment(CommentBuilder builder){
        this.id = builder.id;
        this.time = builder.time;
        this.userId = builder.userId;
        this.content = builder.content;
        this.replies = builder.replies;
    }

    public Comment(){
        Comment cmt = new Comment.CommentBuilder().id().build();
        this.id = cmt.getId();
        this.time = cmt.getTime();
        this.userId = cmt.getUserId();
        this.content = cmt.getContent();
        this.replies = cmt.getReplies();
    }

    public Comment(long userId, String content, Comment reply){
        Comment cmt = new CommentBuilder().id()
                .time()
                .userId(userId)
                .content(content)
                .replies(reply)
                .build();
        this.id = cmt.getId();
        this.time = cmt.getTime();
        this.userId = cmt.getUserId();
        this.content = cmt.getContent();
        this.replies = cmt.getReplies();
    }


    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public static AtomicLong getCounter() {
        return counter;
    }

    public ArrayList<Comment> getReplies() {
        return replies;
    }

    @Override
    public String toString(){
        return "Comment ID: "+id
                +"\nComment time: "+time
                +"\nUser ID: "+ userId
                +"\nContent: "+ content;
    }
    public static class CommentBuilder{
        private long id;
        private String time;
        private long userId;
        private String content = "";
        private ArrayList<Comment> replies = new ArrayList<>();

        public CommentBuilder id(){
            this.id = Comment.counter.getAndIncrement();
            return this;
        }

        public CommentBuilder id(long id){
            this.id = id;
            return this;
        }

        public CommentBuilder time(){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.time = LocalDateTime.now().format(formatter);
            return this;
        }

        public CommentBuilder userId(long userId){
            this.userId = userId;
            return this;
        }

        public CommentBuilder content(String content){
            this.content = content;
            return this;
        }

        public CommentBuilder replies(Comment reply){
            if (reply == null)
                return this;

            this.replies.add(reply);
            return this;
        }

        public Comment build(){
            return new Comment(this);
        }
    }
}
