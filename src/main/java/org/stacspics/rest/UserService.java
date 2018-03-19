package org.stacspics.rest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Path("/users")
public class UserService {


    private final CopyOnWriteArrayList<User> uList = UserList.getInstance();

    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllUsers(){
        return "---User List---\n"
                +uList.stream()
                .map(c->c.toString())
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUser(@PathParam("id")long id){
        Optional<User> match
                = uList.stream()
                .filter(c->c.getId() == id)
                .findFirst();
        if(match.isPresent()){
            return "---User---\n"
                    +match.get()
                    .toString();
        }else{
            return "User not found";
        }
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteUser(@PathParam("id")long id){
        Optional<User>match
                =uList.stream()
                .filter(c->c.getId()==id)
                .findFirst();
        if(match.isPresent()){
            uList.remove(
                    uList.stream()
                            .filter(c->c.getId() ==id)
                            .findFirst()
                            .get()
            );
            return "User deleted.";
        }else{
            return "User not found, no user deleted.";
        }
    }

    @POST
    @Path("/addUser")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUser(InputStream is){
        User user;
        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(is));

        boolean hasName = jsonE.getAsJsonObject().has("name");
        boolean hasEmail = jsonE.getAsJsonObject().has("email");


        if(!hasName){
            return "User must have a name";
        }else if(!hasEmail){
            return "Please enter an email address";
        }

            String userEmail = jsonE.getAsJsonObject().getAsJsonPrimitive("email").getAsString();
            Optional<User> existingEmail
                    = uList.stream()
                    .filter(c -> c.getEmail().equals(userEmail) )
                    .findFirst();

            if(existingEmail.isPresent()){
                return "Email has already been used.";
            }else{

                user = new User.UserBuilder()
                        .id()
                        .name(jsonE.getAsJsonObject().getAsJsonPrimitive("name").getAsString())
                        .email(userEmail)
                        .numberOfComments(0)
                        .notifications(null)
                        .build();
                uList.add(user);
            }




        return "Created user";


    }

    @GET
    @Path("{id}/notification")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUserNotification(@PathParam("id")long id){
        Optional<User> match
                = uList.stream()
                .filter(c->c.getId() == id)
                .filter(c->c.getNotifications().size()>0)
                .findFirst();

        if(match.isPresent()){

            String message = match.get()
                    .getNotifications()
                    .stream()
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));
            match.get().getNotifications().removeAll(match.get().getNotifications());
            return message;
        }else{
            return "User not found or no notifications";
        }
    }
}
