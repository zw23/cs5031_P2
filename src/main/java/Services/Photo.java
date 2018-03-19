package Services;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Photo {
    private final long id;
    private final String description;
    private final String name;
    private final long postedBy;
    private ArrayList<Comment> comments;

    private static final AtomicLong counter = new AtomicLong(0);

    private Photo(PhotoBuilder builder){
        this.id = builder.id;
        this.description = builder.description;
        this.name = builder.name;
        this.postedBy = builder.postedBy;
        this.comments = builder.comments;

    }

    public Photo(){
        Photo photo = new PhotoBuilder()
                .id().build();

        this.id = photo.getId();
        this.description = photo.getDescription();
        this.name = photo.getName();
        this.postedBy = photo.getPostedBy();
        this.comments = photo.getComments();

    }

    public Photo(String description, String name, long postedBy, Comment comment){
        Photo photo = new PhotoBuilder()
                .id()
                .description(description)
                .name(name)
                .postedBy(postedBy)
                .comments(comment)
                .build();
        this.id = photo.getId();
        this.description = photo.getDescription();
        this.name = photo.getName();
        this.postedBy = photo.getPostedBy();
        this.comments = photo.getComments();
    }
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public long getPostedBy() {
        return postedBy;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString(){
        return "Photo ID: "+id
                +"\nPhoto name: "+ name
                +"\nPhoto description: "+ description
                +"\nPhoto posted by user id: "+ postedBy
                +"\nComments on photo: " + comments.size()
                +"\n---------------------";
    }
    public static class PhotoBuilder{
        private  long id;
        private  String description = "";
        private  String name = "";
        private  long postedBy;
        private ArrayList<Comment> comments = new ArrayList<>();

        public PhotoBuilder id(){
            this.id = Photo.counter.getAndIncrement();
            return this;
        }

        public PhotoBuilder id(long id){
            this.id = id;
            return this;
        }

        public PhotoBuilder description(String description){
            this.description = description;
            return this;
        }

        public PhotoBuilder name(String name){
            this.name = name;
            return this;
        }

        public PhotoBuilder postedBy(long postedBy){
            this.postedBy = postedBy;
            return this;
        }

        public PhotoBuilder comments(Comment comment){
            if(comment == null){
                return this;
            }
            this.comments.add(comment);
            return this;
        }

        public Photo build(){
            return new Photo(this);
        }
    }
}
