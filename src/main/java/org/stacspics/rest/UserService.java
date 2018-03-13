package org.stacspics.rest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
}
