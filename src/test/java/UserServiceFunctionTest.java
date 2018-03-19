import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.stacspics.rest.User;
import org.stacspics.rest.UserService;

import javax.ws.rs.core.Application;



import javax.ws.rs.client.Entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserServiceFunctionTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(UserService.class);
    }

    @Test
    public void UserNotFoundTest() {
        String response = target("users/4").request().get(String.class);
        assertTrue(response.contains("User not found"));
    }

    @Test
    public void findSingleUser(){
        String response = target("users/3").request().get(String.class);
        assertTrue(response.contains("Name: user4"));
    }

    @Test
    public void getallUsers(){
        String response = target("users/all").request().get(String.class);
        assertTrue(response.contains("ID: 0\nName: user1"));
        assertTrue(response.contains("ID: 1\nName: user2"));
        assertTrue(response.contains("ID: 2\nName: user3"));
        assertTrue(response.contains("ID: 3\nName: user4"));
    }

    @Test
    public void getUserNumber(){
        String response = target("users/all").request().get(String.class);
        String[] users = response.split("ID");
        int userNumber = 0;
        for (int i = 0; i < users.length; i++) {
            if(users[i].contains("Name")) userNumber++;
        }

        assertEquals(4,userNumber);
    }

    @Test
    public void getNotification(){
        String response = target("users/0/notification").request().get(String.class);
        assertTrue(response.contains("Notification ID: 1"));
        assertTrue(response.contains("Notification ID: 3"));
        assertTrue(response.contains("Notification ID: 4"));
    }

    @Test
    public void addUser(){

        Gson gson = new Gson();
        User user = new User.UserBuilder()
                .name("Tom")
                .email("tom@mockmail.com")
                .numberOfComments(0)
                .notifications(null)
                .build();

        String json = gson.toJson(user);

        String response = target("users/addUser")
                .request()
                .post(Entity.json(json),String.class);

        assertEquals("Created user",response);
    }

    @Test
    public void addInvalidUser(){
        Gson gson = new Gson();
        User user = new User.UserBuilder()
                .name("Sam")
                .email("Sam@mockmail.com")
                .numberOfComments(0)
                .notifications(null)
                .build();

        String json = gson.toJson(user);

        String response = target("users/addUser")
                .request()
                .post(Entity.json(json),String.class);

        assertEquals("Created user",response);

        Gson gson2 = new Gson();
        User user2 = new User.UserBuilder()
                .name("Sam")
                .email("Sam@mockmail.com")
                .numberOfComments(0)
                .notifications(null)
                .build();
        String json2 = gson2.toJson(user2);
        String response2 = target("users/addUser")
                .request()
                .post(Entity.json(json2),String.class);

        assertEquals("Email has already been used.",response2);
        assertTrue(!response2.contains("Created user"));

        Gson gson3 = new Gson();
        User user3 = new User.UserBuilder()
                .name("")
                .email("Sam@mockmail.com")
                .numberOfComments(0)
                .notifications(null)
                .build();
        String json3 = gson3.toJson(user3);
        String response3 = target("users/addUser")
                .request()
                .post(Entity.json(json3),String.class);
        assertEquals("Please enter a valid name.",response3);

    }

    @Test
    public void deleteUser(){
        String response = target("users/delete/2")
                .request()
                .delete(String.class);

        assertEquals("User deleted.",response);

    }

    @Test
    public void deleteNoneExistingUser(){
        String response = target("users/delete/1")
                .request()
                .delete(String.class);

        assertEquals("User deleted.",response);

        String response2 = target("users/delete/1")
                .request()
                .delete(String.class);

        assertEquals("User not found, no user deleted.",response2);
    }
}


