package Services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Path("/comments")
public class CommentService {

    private final CopyOnWriteArrayList<Comment> cmtList = MockList.getCommentList();
    private final CopyOnWriteArrayList<User> uList = MockList.getInstance();
    private final CopyOnWriteArrayList<Photo> ptList = MockList.getPhotoList();

    /**
     * Method loop the comment list and print all of comments
     *
     * @return returns a string message
     */
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

    /**
     *
     * @param id this is user id, use this to find all comments that was posted by this user.
     * @return
     */
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

    /**
     *
     * @param id find the comment first,
     *           set the upvote value +1.
     * @return
     */
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

    /**
     *
     * @param id find the comment first,
     *           set the downvote value +1.
     * @return
     */
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

    /**
     *
     * @param id
     * There is a nested method to get all the replies under this comment,
     *           the method go through all the replies array to check if the size is bigger than 0
     *           if it is, check deeper.
     *           if not, append all replies that has been found.
     * @return
     */
    @GET
    @Path("{CommentId}/replies")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRepliesByCommentId(@PathParam("CommentId")long id){
        Optional<Comment> match
                = cmtList.stream()
                .filter(c -> c.getId() == id)
                .filter(c -> c.getReplies().size()!= 0)
                .findFirst();
        Optional<Comment> sizeZero
                = cmtList.stream()
                .filter(c -> c.getId() == id)
                .filter(c -> c.getReplies().size() == 0)
                .findFirst();

        if(match.isPresent() && !sizeZero.isPresent()){
            Comment cmt = match.get();
            StringBuilder builder = new StringBuilder();

            builder.append("---Original comment id: "+id+"---\n");
            builder.append(cmt.toString());
            builder.append("------------------------\n");
            builder.append("Replies to this comment:");

            for(int i = 0;i<cmt.getReplies().size();i++){
                builder.append(cmt.getReplies().get(i).toString());
                if(cmt.getReplies().get(i).getReplies().size()!=0){
                    getReplies(cmt.getReplies().get(i).getReplies(),builder);
                }
            }
            return builder.toString();
        }else if(sizeZero.isPresent()){
            return "No reply under this comment.";
        }else{
            return "Sorry no comment with such id found";
        }
    }

    private void getReplies(ArrayList<Comment> reply,StringBuilder builer){
        for(int i = 0;i<reply.size();i++){
            builer.append(reply.get(i).toString());
            if(reply.get(i).getReplies().size() != 0 ){
                getReplies(reply.get(i).getReplies(),builer);
            }
        }
    }

    /**
     * Method checks if the user, original comment exist first.
     * Then add a comment to the original comment,
     * add notification to original comment poster and original photo poster.
     *
     * @param CommentId The original comment
     * @param is input json
     *
     *
     * @return
     */
    @POST
    @Path("{CommentId}/makeComment")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String makeAComment(@PathParam("CommentId")long CommentId, InputStream is) throws UnsupportedEncodingException {

        Comment cmt;
        JsonParser parser = new JsonParser();
        JsonElement jsonE = parser.parse(new InputStreamReader(is, "UTF-8"));

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



        long originalPhotoId;
        if(!findUserOfOriginalComment.isPresent()){
            return "No such comment.";
        }else {
            originalPhotoId  = findUserOfOriginalComment.get().getPhotoId();
        }
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
                findUserOfOriginalComment.get().getReplies().add(cmt);
            Notification nt = new Notification.NotificationBuilder()
                    .id()
                    .isReply(true)
                    .originalId(CommentId)
                    .userId(userID)
                    .commentId(cmt.getId())
                    .build();

            Notification ntPhotoPoster = new Notification.NotificationBuilder()
                    .id()
                    .isReply(false)
                    .originalId(originalPhotoId)
                    .userId(userID)
                    .commentId(cmt.getId())
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

    /**
     * Method checks if there is an comment with given id,
     * checks if the user exist and if the user is an admin or not.
     *
     * When deleted, the content change to :"COMMENT HAS BEEN REMOVED BY AN ADMIN."
     *
     * @param commentId the original comment id.
     * @param uid the user that commenting on the original comment.
     * @return
     */
    @POST
    @Path("{commentId}/delete/{uid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteComment(@PathParam("commentId") long commentId, @PathParam("uid") long uid){

        Optional<Comment> match
                = cmtList.stream()
                .filter(c->c.getId() == commentId)

                .findFirst();
        if(!match.isPresent()){
            return "Comment with id not found";
        }

        Optional<User> userExist
                = uList.stream()
                .filter(c ->c.getId() == uid)
                .findFirst();
        if(!userExist.isPresent()){
            return "No user with such ID";
        }

        Optional<User> adimExist
                = uList.stream()
                .filter(c ->c.getId() == uid)
                .filter(c ->c.isAdmin())
                .findFirst();
        if(adimExist.isPresent()){
            match.get().setContent("COMMENT HAS BEEN REMOVED BY AN ADMIN.");
            return "Comment deleted.";
        }else{
            return "The user is not an admin.";
        }
    }
}
