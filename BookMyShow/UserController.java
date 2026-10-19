package BookMyShow;

import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    List<User> users;

    public UserController(List<User> users){
        this.users = users;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(int id){
        this.users = this.users.stream()
                     .filter(item -> item.userId != id)
                     .toList();
    }

    public List<User> getUsers(){
        return this.users;
    }
}
