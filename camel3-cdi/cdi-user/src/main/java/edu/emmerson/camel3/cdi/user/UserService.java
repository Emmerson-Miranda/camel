package edu.emmerson.camel3.cdi.user;


import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Named;

/**
 * A {@link org.apache.camel.example.cdi.User} service which we rest enable the routes defined in the XML file.
 */
@Named("userService")
public class UserService {

    // use a tree map so they become sorted
    private final Map<String, User> users = new TreeMap<String, User>();

    public UserService() {
        users.put("123", new User(123, "Cesar Vallejo"));
        users.put("456", new User(456, "Ricardo Palma"));
        users.put("789", new User(789, "Jose Maria Arguedas"));
    }


    /**
     * List all users
     *
     * @return the list of all users
     */
    public Collection<User> listUsers() {
        return users.values();
    }

}
