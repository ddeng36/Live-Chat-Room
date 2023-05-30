package team.shared;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    //The class, which would be written or read by using obj stream, should implement Serializable interface.
    @Serial
    private static final long serialVersionUID = 1L;
    //To improve the compatibility.
    private String userID;
    private String userPassword;


    public User(String userID, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public User() {
    }

    public String getUserID() {

        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
