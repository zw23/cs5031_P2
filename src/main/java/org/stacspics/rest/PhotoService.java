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
    public String addPhoto(InputStream in){

        Photo photo;

        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(in));



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
        }else{
            return "The user does not exist!";
        }



    }

    @POST
    @Path("{PhotoId}/makeComment")
    @Produces(MediaType.TEXT_PLAIN)
    public String makeAComment(@PathParam("PhotoId")long PhotoId, InputStream is){
        Comment cmt;
        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(is));

        long userID = jsonE.getAsJsonObject().getAsJsonPrimitive("userId").getAsLong();
        String content = jsonE.getAsJsonObject().getAsJsonPrimitive("content").getAsString();

        Optional<User> match
                =uList.stream()
                .filter(c ->c.getId() == userID)
                .findFirst();
        Optional<Photo> photo
                = ptList.stream()
                .filter(c -> c.getId() == PhotoId)
                .findFirst();

        if(match.isPresent() && photo.isPresent()){
            cmt = new Comment.CommentBuilder()
                    .id()
                    .time()
                    .userId(userID)
                    .content(content)
                    .replies(null)
                    .upvote()
                    .downvote()
                    .isReply(false)
                    .originalPostId(PhotoId)
                    .photoId(PhotoId)
                    .build();

            cmtList.add(cmt);
            photo.get().getComments().add(cmt);

            Notification ntPhotoPoster = new Notification.NotificationBuilder()
                    .id()
                    .isReply(false)
                    .originalId(PhotoId)
                    .userId(userID)
                    .build();

            Optional<Photo> originalPhoto
                    =ptList.stream()
                    .filter(c -> c.getId() == PhotoId)
                    .findFirst();

            long originalPhotoPosterId = originalPhoto.get().getPostedBy();

            if(originalPhotoPosterId != userID){

                Optional<User> originalPhotoPoster
                        =uList.stream()
                        .filter(c -> c.getId() == originalPhotoPosterId)
                        .findFirst();
                originalPhotoPoster.get().getNotifications().add(ntPhotoPoster);
            }

            return "Commented on photo id: "+ PhotoId +" with content :" + content + ".\n";
        }else if(!match.isPresent()){
            return "No such a user with this id.";
        }else{
            return "No such photo with this id";
        }
    }
}
