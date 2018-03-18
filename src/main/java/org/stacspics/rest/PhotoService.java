package org.stacspics.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
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
}
