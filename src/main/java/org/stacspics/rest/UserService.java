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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(InputStream in){
        User user;
        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(in));
        Gson gson = new Gson();
        user = gson.fromJson(jsonE,User.class);

        uList.add(user);
        return Response.status(201).build();
    }
}
