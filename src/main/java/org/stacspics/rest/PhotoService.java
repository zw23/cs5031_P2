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
        Optional<Photo> match
                = ptList.stream()
                .filter(c -> c.getId() == PhotoId)
                .filter(c -> c.getComments() != null)
                .findFirst();
        if(match.isPresent()){
            Integer i = (int)(long) PhotoId;
            return "---Comments on photo " + PhotoId + "---\n"
                    +ptList.get(i)
                    .getComments()
                    .stream()
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));
        }else{
            return "---No Photo found with such id.";
        }
    }

    @POST
    @Path("/addPhoto")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(InputStream in){

        Photo photo;

        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(in));
        Gson gson = new Gson();
        photo = gson.fromJson(jsonE,Photo.class);

        ptList.add(photo);
        return Response.status(201).build();
    }
}
