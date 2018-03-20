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

@Path("/photo")
public class PhotoService {
    private final CopyOnWriteArrayList<Photo> ptList = MockList.getPhotoList();
    private final CopyOnWriteArrayList<Comment> cmtList = MockList.getCommentList();
    private final CopyOnWriteArrayList<User> uList = MockList.getInstance();

    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllPhotos(){
        return "---Photo List---\n"
                +ptList.stream()
                .map(c->c.toString())
                .collect(Collectors.joining("\n"));
    }

    /**
     * Method checks if there is a photo with given id.
     * Print the photo stats.
     *
     * @param PhotoId The photo id that we need to find.
     * @return
     */
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

    /**
     * In the comment class there is a originalPhotoId value.
     * This method get all the comment with the given originalPhotoId and print them.
     *
     * @param PhotoId The comment that has this id.
     * @return
     */
    @GET
    @Path("{PhotoId}/comments")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPhotoCommentByPhotoId(@PathParam("PhotoId")long PhotoId){
        Optional<Comment> commentsWithThisPhotoId
                = cmtList.stream()
                .filter(c -> c.getPhotoId() == PhotoId)
                .findFirst();
        Optional<Photo> photoFound
                = ptList.stream()
                .filter(c -> c.getId() == PhotoId)
                .findFirst();
        if(!photoFound.isPresent()){
            return "Sorry, no photo with this id.";
        }

        if(commentsWithThisPhotoId.isPresent()){
            Integer i = (int)(long) PhotoId;
            return "---Comments on photo " + PhotoId + "---\n"
                    +cmtList.stream()
                    .filter(c ->c.getPhotoId() == PhotoId)
                    .map(c -> c.toString())
                    .collect(Collectors.joining("\n"));
        }else{
            return "No comments on this photo.";
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

    /**
     * Same as make comment, find the photo first and make a comment on this photo.
     * When comment is made, give notifications to photo poster.
     *
     *
     * @param PhotoId The photo that we are going to comment on.
     * @param is input json
     * @return
     */
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
