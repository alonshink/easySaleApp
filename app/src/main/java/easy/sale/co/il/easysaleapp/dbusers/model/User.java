package easy.sale.co.il.easysaleapp.dbusers.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;

    // קונסטרקטור מלא
    public User(int id, String email, String firstName, String lastName, String avatar) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    // קונסטרקטור ריק
    public User() {
        // אפשר להשאיר את השדות כריקים או להגדיר ערכים ברירת מחדל
    }

    public User(String newName, String newEmail, Object o) {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar != null ? avatar : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
