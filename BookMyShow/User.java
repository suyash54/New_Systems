package BookMyShow;

public class User {

    int userId;
    String userName;
    String email;

    public User(int userId, String userName, String email){
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public int getUserId(){
        return this.userId;
    }
    public String getUserName(){
        return this.userName;
    }

    public String getEmail(){
        return this.email;
    }
}
