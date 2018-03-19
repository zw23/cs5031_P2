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

@Path("/comments")
public class CommentService {

    private final CopyOnWriteArrayList<Comment> cmtList = UserList.getCommentList();
    private final CopyOnWriteArrayList<User> uList = UserList.getInstance();
    private final CopyOnWriteArrayList<Photo> ptList = UserList.getPhotoList();
    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllComments(){
        return "---Comment List---\n"
                +cmtList.stream()
                .map(c->c.toString())
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCommentById(@PathParam("id")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c->c.getId() == id)

                .findFirst();
        if(match.isPresent()){
            return "---Comment---\n"
                    +match.get()
                    .toString();
        }else{
            return "Comment with id not found";
        }
    }

    @GET
    @Path("/user/{UserId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCommentByUserId(@PathParam("UserId")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c ->c.getUserId() == id)
                .findFirst();

        if(match.isPresent()){
            return "---User id "+ id +" comment(s)--- \n"+
            cmtList.stream()
                    .filter(c->c.getUserId() == id)
                    .map(c->c.toString())
                    .collect(Collectors.joining("\n"));
        }else{
            return "User not found or user dose not have any comments";
        }
    }

    @POST
    @Path("{CommentId}/upvote")
    @Produces(MediaType.TEXT_PLAIN)
    public String upvote(@PathParam("CommentId")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if(match.isPresent()){
            match.get().addUpvote();
            return "Successfully upvoted.";
        }else{
            return "Upvote unsuccessful.";
        }

    }

    @POST
    @Path("{CommentId}/downvote")
    @Produces(MediaType.TEXT_PLAIN)
    public String downvote(@PathParam("CommentId")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if(match.isPresent()){
            match.get().addDownvote();
            return "Successfully downvoted.";
        }else{
            return "Downvote unsuccessful.";
        }

    }

    @GET
    @Path("{CommentId}/replies")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRepliesByCommentId(@PathParam("CommentId")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c -> c.getId() == id)
                .filter(c -> c.getReplies() != null)
                .findFirst();
        if(match.isPresent()){

            Integer i = (int)(long) id;
            return "---Replies of comment id: " + id +"---\n"
                    +cmtList
                    .get(i)
                    .getReplies()
                    .stream()
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));

        }else{
            return "Sorry no comment with such id found";
        }
    }

    @POST
    @Path("{CommentId}/makeComment")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String makeAComment(@PathParam("CommentId")long CommentId, InputStream is){

        Comment cmt;
        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(is));
        Gson gson = new Gson();
        // getting the poster id in the json block for user check.
        long userID = jsonE.getAsJsonObject().getAsJsonPrimitive("userId").getAsLong();
        String content = jsonE.getAsJsonObject().getAsJsonPrimitive("content").getAsString();

        Optional<User> match
                =uList.stream()
                .filter(c ->c.getId() == userID)
                .findFirst();

        Optional<Comment> findUserOfOriginalComment
                = cmtList.stream()
                .filter(c -> c.getId() == CommentId)
                .findFirst();

        long originalPhotoId = findUserOfOriginalComment.get().getPhotoId();

            //check if user exist.
            if(match.isPresent()){

                cmt = new Comment.CommentBuilder()
                        .id()
                        .time()
                        .userId(userID)
                        .content(content)
                        .replies(null)
                        .upvote()
                        .downvote()
                        .isReply(true)
                        .originalPostId(CommentId)
                        .photoId(originalPhotoId)
                        .build();

                cmtList.add(cmt);

            Notification nt = new Notification.NotificationBuilder()
                    .id()
                    .isReply(true)
                    .originalId(CommentId)
                    .userId(userID)
                    .build();

            Notification ntPhotoPoster = new Notification.NotificationBuilder()
                    .id()
                    .isReply(false)
                    .originalId(originalPhotoId)
                    .userId(userID)
                    .build();

                //Because this comment id must exist, so skip checking.
                long originalPosterId = findUserOfOriginalComment.get().getUserId();

                if(originalPosterId != userID){
                    //Because the comment above exist, so user id must be real.
                    Optional<User> originalCommentPoster
                            =uList.stream()
                            .filter(c ->c.getId() == originalPosterId)
                            .findFirst();
                    //adding notification to the original poster.
                    originalCommentPoster.get().getNotifications().add(nt);
                }



                Optional<Photo> originalPhoto
                        =ptList.stream()
                        .filter(c -> c.getId() == originalPhotoId)
                        .findFirst();

                long originalPhotoPosterId = originalPhoto.get().getPostedBy();

                if(originalPhotoPosterId != userID){

                    Optional<User> originalPhotoPoster
                            =uList.stream()
                            .filter(c -> c.getId() == originalPhotoPosterId)
                            .findFirst();
                    originalPhotoPoster.get().getNotifications().add(ntPhotoPoster);
                }


                return "Commented on comment id: "+ CommentId +" with content :" + content + ".\n";
            }else{
                return "User does not exist";
            }


    }

}
