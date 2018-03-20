import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import Services.Comment;
import Services.CommentService;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentServiceTest extends JerseyTest{
    @Override
    protected Application configure() {
        return new ResourceConfig(CommentService.class);
    }

    @Test
    public void getAllComments(){
        String response = target("comments/all").request().get(String.class);
        assertTrue(response.contains("Comment ID: 0"));
        assertTrue(response.contains("Comment ID: 1"));
        assertTrue(response.contains("Comment ID: 2"));
        assertTrue(response.contains("Comment ID: 3"));
        assertTrue(response.contains("Comment ID: 4"));
        assertTrue(response.contains("Comment ID: 5"));
    }

    @Test
    public void getSingleComment(){
        String response = target("comments/0").request().get(String.class);
        assertTrue(response.contains("Comment ID: 0"));
        assertTrue(response.contains("Content: This is user1 comment"));
        assertFalse(response.contains("Comment ID: 1"));
        assertFalse(response.contains("Comment ID: 2"));
    }

    @Test
    public void invalidGetSingleComment(){
        String response = target("comments/6").request().get(String.class);
        assertTrue(response.contains("Comment with id not found"));
    }

    @Test
    public void getCommentByuserId(){
        String response = target("comments/user/3").request().get(String.class);
        assertTrue(response.contains("Comment ID: 3"));
        assertTrue(response.contains("Comment ID: 5"));
        assertTrue(response.contains("User ID: 3"));

    }

    @Test
    public void invalidGetCommentByUserId(){
        String response = target("comments/user/78").request().get(String.class);
        assertTrue(response.contains("User not found or user dose not have any comments"));
        String response2 = target("comments/user/4").request().get(String.class);
        assertTrue(response2.contains("User not found or user dose not have any comments"));
    }

    @Test
    public void commentUpvoteAndDownvote(){
        String response = target("comments/0/upvote").request().post(Entity.text(""),String.class);
        assertTrue(response.contains("Successfully upvoted."));

        String response2 = target("comments/0").request().get(String.class);
        assertTrue(response2.contains("Upvote: 1"));

        String response3 = target("comments/1/downvote").request().post(Entity.text(""),String.class);
        assertTrue(response3.contains("Successfully downvoted."));

        String response4 = target("comments/1").request().get(String.class);
        assertTrue(response4.contains("Downvote: 1"));
    }

    @Test
    public void invalidDownvoteAndUpvote(){
        String response = target("comments/99/upvote").request().post(Entity.text(""),String.class);
        assertTrue(response.contains("Upvote unsuccessful."));

        String response2 = target("comments/99/downvote").request().post(Entity.text(""),String.class);
        assertTrue(response2.contains("Downvote unsuccessful."));
    }

    @Test
    public void getRepliesByCommentId(){
        String response = target("comments/1/replies").request().get(String.class);
        assertTrue(response.contains("---Original comment id: 1---"));
        assertTrue(response.contains("Comment ID: 1"));
        assertTrue(response.contains("Comment ID: 2"));

        String response2 = target("comments/4/replies").request().get(String.class);
        assertTrue(response2.contains("No reply under this comment."));

        String response3 = target("comments/98/replies").request().get(String.class);
        assertTrue(response3.contains("Sorry no comment with such id found"));
    }

    @Test
    public void makeComment(){

        Gson gson = new Gson();
        Comment cmt = new Comment.CommentBuilder().id()
                .content("This is comment test")
                .userId(2)
                .build();

        String json = gson.toJson(cmt);

        String response = target("comments/1/makeComment")
                .request()
                .post(Entity.json(json),String.class);

        assertTrue(response.contains("Commented on comment id: 1 with content :This is comment test"));

        String response2 = target("comments/78/makeComment")
                .request()
                .post(Entity.json(json),String.class);

        assertTrue(response2.contains("No such comment."));
    }



}
