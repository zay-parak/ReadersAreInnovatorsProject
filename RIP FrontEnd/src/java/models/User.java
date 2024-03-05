package models;

import java.util.List;

public class User {
    private Integer userID;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Character type;
    private String phoneNumber;
    private List<Genre> favourites;

    public User(Integer userID, String name, String surname, String username, String email, Character type, String phoneNumber) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }
    
    public List<Genre> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Genre> favourites) {
        this.favourites = favourites;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
