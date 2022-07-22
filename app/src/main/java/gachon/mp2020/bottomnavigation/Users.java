package gachon.mp2020.bottomnavigation;
import java.util.ArrayList;

public class Users {
    public String userName;
    public String userEmail;
    public ArrayList bookmarkList;
    public ArrayList currentPosition;
    public String profileImage;
    public Boolean notification;

    public Users(String userName, String userEmail, ArrayList bookmarkList, ArrayList currentPosition, String profileImage, Boolean notification) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookmarkList = bookmarkList;
        this.currentPosition = currentPosition;
        this.profileImage = profileImage;
        this.notification = notification;
    }

}