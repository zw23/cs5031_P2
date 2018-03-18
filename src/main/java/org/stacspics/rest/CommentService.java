package org.stacspics.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Path("/comments")
public class CommentService {

    private final CopyOnWriteArrayList<Comment> cmtList = UserList.getCommentList();
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


}
