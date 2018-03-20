package Services;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Comment {
    private final long id;
    private final String time;
    private final long userId;
    private String content;
    private int upvote;
    private int downvote;
    private final long originalPostId;
    private final boolean isReply;


    private final long photoId;




    private ArrayList<Comment> replies;

    private static final AtomicLong counter = new AtomicLong(0);


    private Comment(CommentBuilder builder){
        this.id = builder.id;
        this.time = builder.time;
        this.userId = builder.userId;
        this.content = builder.content;
        this.replies = builder.replies;
        this.upvote = builder.upvote;
        this.downvote = builder.downvote;
        this.originalPostId = builder.originalPostId;
        this.isReply = builder.isReply;
        this.photoId = builder.photoId;
    }

    public Comment(){
        Comment cmt = new Comment.CommentBuilder().id().build();
        this.id = cmt.getId();
        this.time = cmt.getTime();
        this.userId = cmt.getUserId();
        this.content = cmt.getContent();
        this.replies = cmt.getReplies();
        this.upvote = cmt.getUpvote();
        this.downvote = cmt.getDownvote();
        this.originalPostId = cmt.getOriginalPostId();
        this.isReply = cmt.isReply();
        this.photoId = cmt.getPhotoId();
    }

    public Comment(long userId, String content, Comment reply,long originalPostId,boolean isReply,long photoId){
        Comment cmt = new CommentBuilder().id()
                .time()
                .userId(userId)
                .content(content)
                .replies(reply)
                .upvote()
                .downvote()
                .originalPostId(originalPostId)
                .isReply(isReply)
                .photoId(photoId)
                .build();
        this.id = cmt.getId();
        this.time = cmt.getTime();
        this.userId = cmt.getUserId();
        this.content = cmt.getContent();
        this.replies = cmt.getReplies();
        this.upvote = cmt.getUpvote();
        this.downvote = cmt.getDownvote();
        this.originalPostId = cmt.getOriginalPostId();
        this.isReply = cmt.isReply();
        this.photoId = cmt.getPhotoId();
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

    public int getUpvote() {
        return upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public int addUpvote(){
        return upvote++;
    }

    public int addDownvote(){
        return downvote++;
    }

    public long getOriginalPostId() {
        return originalPostId;
    }

    public boolean isReply() {
        return isReply;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static AtomicLong getCounter() {
        return counter;
    }

    public ArrayList<Comment> getReplies() {
        return replies;
    }

    @Override
    public String toString(){
        if(isReply){
            return "\nComment ID: "+id + ", replied to comment id:"+ originalPostId
                    +"\nComment time: "+time
                    +"\nUser ID: "+ userId
                    +"\nContent: "+ content
                    +"\nUpvote: "+ upvote
                    +"\nDownvote: "+ downvote
                    +"\nReplies on this comment: "+getReplies().size()
                    +"\n----------";
        }else{
            return "Comment ID: "+id + ", commented to photo id:"+ originalPostId
                    +"\nComment time: "+time
                    +"\nUser ID: "+ userId
                    +"\nContent: "+ content
                    +"\nUpvote: "+ upvote
                    +"\nDownvote: "+ downvote
                    +"\nReplies on this comment: "+getReplies().size()
                    +"\n----------";
        }

    }
    public static class CommentBuilder{
        private long id;
        private String time;
        private long userId;
        private String content = "";
        private int upvote = 0;
        private int downvote = 0;
        private long originalPostId;
        private boolean isReply;
        private long photoId;

        private ArrayList<Comment> replies = new ArrayList<>();

        public CommentBuilder id(){
            this.id = Comment.counter.getAndIncrement();
            return this;
        }

        public CommentBuilder id(long id){
            this.id = id;
            return this;
        }

        public CommentBuilder upvote(){
            this.upvote = upvote;
            return this;
        }

        public CommentBuilder downvote(){
            this.downvote = downvote;
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

        public CommentBuilder isReply(boolean isReply){
            this.isReply = isReply;
            return this;
        }

        public CommentBuilder originalPostId(long originalPostId){
            this.originalPostId = originalPostId;
            return this;
        }

        public CommentBuilder replies(Comment reply){
            if (reply == null)
                return this;

            this.replies.add(reply);
            return this;
        }
        public CommentBuilder photoId(long photoId){
            this.photoId = photoId;
            return this;
        }

        public Comment build(){
            return new Comment(this);
        }
    }
}
