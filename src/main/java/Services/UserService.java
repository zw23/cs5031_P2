package Services;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Path("/delete/{did}/{uid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteUser(@PathParam("did")long did,@PathParam("uid")long uid){
        Optional<User>match
                =uList.stream()
                .filter(c->c.getId()==did)
                .findFirst();
        Optional<User> isAdminExist
                =uList.stream()
                .filter(c->c.getId() ==uid)
                .findFirst();
        Optional<User> isAdmin
                =uList.stream()
                .filter(c->c.getId() ==uid)
                .filter(c->c.isAdmin())
                .findFirst();
        if(!isAdminExist.isPresent()){
            return "Admin does not exist.";
        }else if(!isAdmin.isPresent()){
            return "Sorry, the user who is doing this action is not an admin";
        }

        if(match.isPresent()){
            uList.remove(
                    uList.stream()
                            .filter(c->c.getId() ==did)
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
                boolean isAdmin = jsonE.getAsJsonObject().getAsJsonPrimitive("isAdmin").getAsBoolean();
                user = new User.UserBuilder()
                        .id()
                        .name(jsonE.getAsJsonObject().getAsJsonPrimitive("name").getAsString())
                        .email(userEmail)
                        .numberOfComments(0)
                        .notifications(null)
                        .isAdmin(isAdmin)
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
