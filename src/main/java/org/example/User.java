package org.example;

import java.util.ArrayList;

class User {
    private int userID;
    private int age;
    private String gender;
    private ArrayList<User> Users=new ArrayList<>();

    public User(int userID,  int age, String gender) {
        this.userID = userID;

        this.age = age;
        this.gender = gender;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUserID() { return userID; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public void AddUser(User user){
        this.Users.add(user);
    }
    public ArrayList<User> getList(){return this.Users;}
    public String toString(){
        return getUserID()+","+getAge()+","+getGender();
    }
}