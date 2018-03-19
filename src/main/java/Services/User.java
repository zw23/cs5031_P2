package Services;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class User {


    private final long id;
    private final String name;
    private final int numberOfComments;
    private final String email;



    private final boolean isAdmin;



    private ArrayList<Notification> notifications;

    public static final AtomicLong counter = new AtomicLong(0);

    private User(UserBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.numberOfComments = builder.numberOfComments;
        this.email = builder.email;
        this.notifications = builder.notifications;
        this.isAdmin = builder.isAdmin;

    }

    public User(){
        User user = new User.UserBuilder().id().build();
        this.id = user.getId();
        this.name = user.getName();
        this.numberOfComments = user.getNumberOfComments();
        this.email = user.getEmail();
        this.notifications = user.getNotifications();
        this.isAdmin = user.isAdmin();
    }

    public User(long id, String name, int comments, Notification notification,boolean isAdmin) {
        User user = new UserBuilder()
                .id()
                .name(name)
                .numberOfComments(comments)
                .notifications(notification)
                .isAdmin(isAdmin)
                .build();

        this.id = user.getId();
        this.name = user.getName();
        this.numberOfComments = user.getNumberOfComments();
        this.email = user.getEmail();
        this.notifications = user.getNotifications();
        this.isAdmin = user.isAdmin();
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

    public String getEmail() {
        return email;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public static AtomicLong getCounter() {
        return counter;
    }

    @Override
    public String toString(){
        return "ID: "+id
                +"\nName: "+name
                +"\nEmail: "+ email
                +"\nNumber of notifications: "+ notifications.size()
                +"\nAdmin: "+ isAdmin;
    }
    public static class UserBuilder {
        private long id;
        private String name = "";
        private int numberOfComments;
        private String email = "";
        private boolean isAdmin = false;
        private ArrayList<Notification> notifications = new ArrayList<>();

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
        public UserBuilder email(String email){
            this.email = email;
            return this;
        }
        public UserBuilder notifications(Notification notification){
            if(notification == null){
                return this;
            }
            this.notifications.add(notification);
            return this;
        }

        public UserBuilder isAdmin(boolean isAdmin){
            this.isAdmin = isAdmin;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
