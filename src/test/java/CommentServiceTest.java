import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.stacspics.rest.CommentService;


import javax.ws.rs.core.Application;

public class CommentServiceTest extends JerseyTest{
    @Override
    protected Application configure() {
        return new ResourceConfig(CommentService.class);
    }

    @Test
    public void getAllComments(){
        String response = target("comments/all").request().get(String.class);
    }
}
