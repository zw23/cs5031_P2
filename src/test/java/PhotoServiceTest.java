import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.stacspics.rest.Comment;
import org.stacspics.rest.Photo;
import org.stacspics.rest.PhotoService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoServiceTest extends JerseyTest{
    @Override
    protected Application configure() {
        return new ResourceConfig(PhotoService.class);
    }

    @Test
    public void findAllPhoto(){
        String response = target("photo/all").request().get(String.class);
        assertTrue(response.contains("---Photo List---"));
        assertTrue(response.contains("Photo ID: 1"));
        assertTrue(response.contains("Photo ID: 2"));
    }

    @Test
    public void findPhotoById(){
        String response = target("photo/1").request().get(String.class);
        assertTrue(response.contains("----Photo with id: 1-----"));
        assertTrue(response.contains("Photo ID: 1"));
        assertTrue(response.contains("Photo name: Photo 1"));

    }

    @Test
    public void invalidFindPhotoByid(){
        String response = target("photo/909").request().get(String.class);
        assertTrue(response.contains("Photo with such id not found!"));
    }

    @Test
    public void getCommentsUnderPhotoById(){
        String response = target("photo/0/comments").request().get(String.class);
        assertTrue(response.contains("---Comments on photo 0---"));
        assertTrue(response.contains("Comment ID: 1"));
        assertTrue(response.contains("Comment ID: 2"));
        assertTrue(response.contains("Comment ID: 3"));

        String response2 = target("photo/99/comments").request().get(String.class);

        assertTrue(response2.contains("No Photo found with such id."));
    }

    @Test
    public void addPhoto(){
        Gson gson = new Gson();
        Photo pt = new Photo.PhotoBuilder().id()
                .name("new photo")
                .description("just testing...")
                .postedBy(3)
                .build();

        String json = gson.toJson(pt);

        String response = target("photo/addPhoto")
                .request()
                .post(Entity.json(json),String.class);

        assertTrue(response.contains("Successfully added photo!"));
    }

    @Test
    public void makeCommentOnPhoto(){
        Gson gson = new Gson();
        Comment cmt = new Comment.CommentBuilder().id()
                .content("This is comment test")
                .userId(3)
                .build();

        String json = gson.toJson(cmt);



        String response = target("photo/2/makeComment")
                .request()
                .post(Entity.json(json),String.class);

        assertTrue(response.contains("Commented on photo id: 2 with content :This is comment test"));

        String response2 = target("photo/2/comments").request().get(String.class);

        assertTrue(response2.contains("---Comments on photo 2---"));
        assertTrue(response2.contains("Content: This is comment test"));
        assertTrue(response2.contains("Upvote: 0"));
        assertTrue(response2.contains("Downvote: 0"));

    }
}
