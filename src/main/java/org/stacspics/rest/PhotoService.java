package org.stacspics.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Path("/photo")
public class PhotoService {
    private final CopyOnWriteArrayList<Photo> ptList = UserList.getPhotoList();
    private final CopyOnWriteArrayList<Comment> cmtList = UserList.getCommentList();
    private final CopyOnWriteArrayList<User> uList = UserList.getInstance();
    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllPhotos(){
        return "---Photo List---"
                +ptList.stream()
                .map(c->c.toString())
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("{PhotoId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPhotoById(@PathParam("PhotoId")long PhotoId){
        Optional<Photo> match
                = ptList.stream()
                .filter(c -> c.getId() == PhotoId)
                .findFirst();
        if(match.isPresent()){
            return "----Photo with id: "+PhotoId+"-----\n"
                    + match.get().toString();
        }else{
            return "Photo with such id not found!";
        }
    }

    @GET
    @Path("{PhotoId}/comments")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPhotoCommentByPhotoId(@PathParam("PhotoId")long PhotoId){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c -> c.getPhotoId() == PhotoId)
                .findFirst();
        if(match.isPresent()){
            Integer i = (int)(long) PhotoId;
            return "---Comments on photo " + PhotoId + "---\n"
                    +cmtList.stream()
                    .filter(c ->c.getPhotoId() == PhotoId)
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));
        }else{
            return "---No Photo found with such id.";
        }
    }

    @POST
    @Path("/addPhoto")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPhoto(InputStream in){

        Photo photo;

        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(in));
        Gson gson = new Gson();


        long userID = jsonE.getAsJsonObject().getAsJsonPrimitive("postedBy").getAsLong();
        String name = jsonE.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
        String description = jsonE.getAsJsonObject().getAsJsonPrimitive("description").getAsString();
        Optional<User> checkUser
                = uList.stream()
                .filter(c -> c.getId() == userID)
                .findFirst();
        if(checkUser.isPresent()){
            photo = new Photo.PhotoBuilder()
                    .id()
                    .name(name)
                    .description(description)
                    .postedBy(userID)
                    .comments(null)
                    .build();
            ptList.add(photo);
            return "Successfully added photo!";
        }


        return Response.status(201).build();
    }
}
