package Client;

import Services.Comment;
import Services.Photo;
import Services.User;
import com.google.gson.Gson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Scanner;

import static java.lang.System.exit;

public class SimpleClient {


    static Gson gson = new Gson();
    static Client client = ClientBuilder.newClient();
    static WebTarget webTarget = client.target("http://localhost:8080/myapp/");

    static Scanner in = new Scanner(System.in, "UTF-8");


    public static void main(String[] arge){


    mainMenu();


    }
    public static void mainMenu(){
        String mainMenu = "1.User action\n"
                +"2.Photo action\n"
                +"3.Comment action\n"
                +"4.Quit";

        System.out.println(mainMenu);
        int quitNumber = 0;
        while(quitNumber  != -1){

            quitNumber = in.nextInt();

            switch (quitNumber){

                case 1 :
                    userMenu();
                    break;
                case 2 :
                    photoMenu();
                    break;
                case 3 :
                    commentMenu();
                    break;
                case 4 :
                    exit(0);
                default:
                    System.out.println("Please enter an valid number.");


            }
        }
    }

    public static void userMenu(){
        int quitNumber = 0;
        String userMenu =
                "1.Check all users\n"
                        +"2.Find user by id\n"
                        +"3.Check notification by id\n"
                        +"4.Add user\n"
                        +"5.Delete user\n"
                        +"9.back to main menu";
        System.out.println(userMenu);

        while(quitNumber != 9){
            quitNumber = in.nextInt();
            String response;
            switch (quitNumber){

                case 1 :
                    response = webTarget.path("/users/all").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(userMenu);
                    break;
                case 2 :
                    System.out.println("Please enter user id:");
                    int idNeedToBeFound = in.nextInt();
                    response = webTarget.path("/users/"+idNeedToBeFound).request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(userMenu);
                    break;
                case 3:
                    System.out.println("Please enter user id:");
                    idNeedToBeFound = in.nextInt();
                    response = webTarget.path("/users/"+idNeedToBeFound+"/notification").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(userMenu);
                    break;
                case 4:
                    Gson gson = new Gson();
                    System.out.println("Please enter user name:");
                    in.nextLine();
                    String name = in.nextLine();

                    System.out.println("Please enter a email for this user");
                    String email = in.next();

                    System.out.println("Is the user admin?(y/n)");

                    String answer = in.nextLine();
                    System.out.println(answer);
                    boolean admin = answer.equalsIgnoreCase("y");


                    User user = new User.UserBuilder()
                            .name(name)
                            .email(email)
                            .notifications(null)
                            .isAdmin(admin)
                            .build();

                    String json = gson.toJson(user);

                    response = webTarget.path("users/addUser")
                            .request()
                            .post(Entity.json(json),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(userMenu);
                    break;
                case 5:
                    System.out.println("Please enter the user id that u want to delete:");
                    idNeedToBeFound = in.nextInt();
                    in.nextLine();
                    System.out.println("Please enter the user who is doing this action:");
                    int adminId = in.nextInt();

                    response = webTarget.path("/users/delete/"+idNeedToBeFound+"/"+adminId).request().accept(MediaType.TEXT_PLAIN).delete(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(userMenu);
                    break;
                case 9:
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid number");
            }
        }

    }

    public static void photoMenu(){
        int quitNumber = 0;
        String photoMenu =
                "1.Check all photos\n"
                        +"2.Find photo by id\n"
                        +"3.Check comments on a photo\n"
                        +"4.Add photo\n"
                        +"5.Comment on a photo\n"
                        +"9.back to main menu";
        System.out.println(photoMenu);
        while(quitNumber != 9){
            quitNumber = in.nextInt();
            String response;
            switch (quitNumber){
                case 1:
                    response = webTarget.path("/photo/all").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(photoMenu);
                    break;
                case 2:
                    System.out.println("Please enter the photo id: ");
                    int idToBeFound = in.nextInt();
                    response = webTarget.path("/photo/"+idToBeFound).request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(photoMenu);
                    break;
                case 3:
                    System.out.println("Please enter the photo id: ");
                    idToBeFound = in.nextInt();
                    response = webTarget.path("/photo/"+idToBeFound+"/comments").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(photoMenu);
                    break;
                case 4:
                    Gson gson = new Gson();
                    in.nextLine();
                    System.out.println("Please enter a photo name:");
                    String name = in.nextLine();

                    System.out.println("Please enter a description for this photo");
                    String description = in.nextLine();

                    System.out.println("Please enter the id of the user who is adding this photo: ");

                    long id = in.nextLong();

                    Photo photo = new Photo.PhotoBuilder()
                            .id()
                            .name(name)
                            .description(description)
                            .postedBy(id)
                            .comments(null)
                            .build();

                    String json = gson.toJson(photo);

                    response = webTarget.path("photo/addPhoto")
                            .request()
                            .post(Entity.json(json),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(photoMenu);
                    break;

                case 5:
                    Gson gson1 = new Gson();
                    in.nextLine();
                    System.out.println("Please enter a photo id that you want to comment on:");
                    int Photoid = in.nextInt();
                    in.nextLine();

                    System.out.println("Please enter the content:");
                    String content = in.nextLine();

                    System.out.println("Please enter the user id of who is commenting: ");
                    long uid = in.nextLong();


                    Comment cmt = new Comment.CommentBuilder()
                            .id()
                            .content(content)
                            .isReply(false)
                            .originalPostId(Photoid)
                            .downvote()
                            .replies(null)
                            .upvote()
                            .photoId(Photoid)
                            .userId(uid)
                            .build();

                    json = gson1.toJson(cmt);

                    response = webTarget.path("photo/"+Photoid+"/makeComment")
                            .request()
                            .post(Entity.json(json),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(photoMenu);
                    break;
                case 9:
                    mainMenu();
                    break;
                default:
                    System.out.println("Please enter a valid number.");
                    break;
            }
        }
    }

    public static void commentMenu(){
        int quitNumber = 0;
        String commentMenu =
                "1.Check all comments\n"
                        +"2.Find a comment by id\n"
                        +"3.Check comment's reply\n"
                        +"4.Make a comment\n"
                        +"5.Delete a comment\n"
                        +"6.Upvote a comment\n"
                        +"7.Downvote a comment\n"
                        +"9.back to main menu";
        System.out.println(commentMenu);
        while(quitNumber != 9){
            quitNumber = in.nextInt();
            String response;
            switch (quitNumber){
                case 1:
                    response = webTarget.path("/comments/all").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                case 2:
                    System.out.println("Please enter the comment id: ");
                    int idToBeFound = in.nextInt();
                    response = webTarget.path("/comments/"+idToBeFound).request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                case 3:
                    System.out.println("Please enter the comment id: ");
                    in.nextLine();
                    idToBeFound = in.nextInt();
                    response = webTarget.path("/comments/"+idToBeFound+"/replies").request().accept(MediaType.TEXT_PLAIN).get(String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                case 4:
                    Gson gson1 = new Gson();
                    in.nextLine();
                    System.out.println("Please enter a comment id that you want to comment on:");
                    long commentid = in.nextLong();

                    in.nextLine();
                    System.out.println("Please enter the content:");
                    String content = in.nextLine();

                    System.out.println("Please enter the user id of who is commenting: ");
                    long uid = in.nextLong();


                    Comment cmt = new Comment.CommentBuilder()
                            .id()
                            .content(content)
                            .isReply(true)
                            .originalPostId(commentid)
                            .downvote()
                            .replies(null)
                            .upvote()
                            .userId(uid)
                            .build();

                    String json = gson1.toJson(cmt);

                    response = webTarget.path("comments/"+commentid+"/makeComment")
                            .request()
                            .post(Entity.json(json),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                case 5:
                    in.nextLine();
                    System.out.println("Please enter a comment id that you want to delete:");
                    commentid = in.nextLong();
                    in.nextLine();
                    System.out.println("Please enter the user id of who is commenting: ");
                    uid = in.nextLong();

                    response = webTarget.path("/comments/"+commentid+"/delete/"+uid).request().accept(MediaType.TEXT_PLAIN).post(Entity.text(null),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;

                case 6 :
                    in.nextLine();
                    System.out.println("Please enter a comment id that you want to upvote:");
                    commentid = in.nextLong();

                    response = webTarget.path("/comments/"+commentid+"/upvote").request().accept(MediaType.TEXT_PLAIN).post(Entity.text(null),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                case 7 :
                    in.nextLine();
                    System.out.println("Please enter a comment id that you want to delete:");
                    commentid = in.nextLong();

                    response = webTarget.path("/comments/"+commentid+"/downvote").request().accept(MediaType.TEXT_PLAIN).post(Entity.text(null),String.class);
                    System.out.println(response);
                    System.out.println("------------------------");
                    System.out.println(commentMenu);
                    break;
                    
                case 9 :
                    mainMenu();
                    break;

                default:
                    System.out.println("Please enter a vliad number.");
                    break;
            }
        }
    }
}
