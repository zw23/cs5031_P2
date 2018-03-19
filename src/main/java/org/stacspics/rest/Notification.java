package org.stacspics.rest;

import java.util.concurrent.atomic.AtomicLong;

public class Notification {
    private final long id;
    private final long userId;
    private final long originalId;
    private final boolean isReply;



    private final long commentId;


    private static final AtomicLong counter = new AtomicLong(0);

    private Notification(NotificationBuilder builder){
        this.id = builder.id;
        this.userId = builder.userId;
        this.isReply = builder.isReply;
        this.originalId = builder.originalId;
        this.commentId = builder.commentId;

    }

    public Notification(){
        Notification nt = new Notification.NotificationBuilder().id().build();
        this.id = nt.getId();
        this.userId = nt.getUserId();
        this.originalId = nt.getOriginalId();
        this.isReply = nt.isReply();
        this.commentId = nt.commentId;

    }

    public Notification(long userId,long originalId,boolean isReply,long commentId){
        Notification nt = new NotificationBuilder()
                .id()
                .userId(userId)
                .originalId(originalId)
                .isReply(isReply)
                .commentId(commentId)
                .build();

        this.id = nt.getId();
        this.userId = nt.getUserId();
        this.originalId = nt.getOriginalId();
        this.isReply = nt.isReply();
        this.commentId = nt.getCommentId();

    }

    public long getOriginalId() {
        return originalId;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isReply() {
        return isReply;
    }

    public long getCommentId() {
        return commentId;
    }

    @Override
    public String toString(){
        if(isReply){
            return "---Notification--- "
                    +"\nNotification ID: "+ id
                    +"\nUser ID: "+ userId
                    +"\nReplied on your COMMENT(id:"+originalId+"), new comment id: "+commentId
                    +"\n--------------------------";
        }else{
            return "---Notification--- "
                    +"\nNotification ID: "+ id
                    +"\nUser ID: "+ userId
                    +"\nCommented on your PHOTO(id:"+originalId+"), new comment id: "+commentId
                    +"\n--------------------------";
        }
    }

    public static class NotificationBuilder{
        private long id;
        private long userId;
        private boolean isReply;
        private long originalId;
        private long commentId;

        public NotificationBuilder id(){
            this.id = Notification.counter.getAndIncrement();
            return this;

        }

        public NotificationBuilder id(long id){
            this.id = id;
            return this;
        }

        public NotificationBuilder userId(long userId){
            this.userId = userId;
            return this;
        }

        public NotificationBuilder isReply(boolean isReply){
            this.isReply = isReply;
            return this;
        }


        public NotificationBuilder originalId(long originalId){
            this.originalId = originalId;
            return this;

        }

        public NotificationBuilder commentId(long commentId){
            this.commentId = commentId;
            return this;
        }

        public Notification build(){
            return new Notification(this);
        }

    }
}
