package models.user;

import lombok.Getter;
import lombok.Setter;

public class UserCreate {
    @Getter
    @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String name;

    public UserCreate(){
    }

    public UserCreate withName(String name) {
        this.name = name;
        return this;
    }

    public UserCreate withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserCreate withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserCreate{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
