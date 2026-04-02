package org.example;

import java.util.ArrayList;

public class Reading_Habit {
    private int habitID;
    private String book;
    private int pagesRead;
    private String submissionMoment;
    private int userID;
    private ArrayList<Reading_Habit> Habits=new ArrayList<>();
    public Reading_Habit(int habitID,int userID,int pagesRead, String book,String submissionMoment){
        this.userID=userID;
        this.habitID=habitID;
        this.pagesRead=pagesRead;
        this.book=book;
        this.submissionMoment=submissionMoment;

    }


    public void setHabitID(int habitID) {
        this.habitID = habitID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public void setSubmissionMoment(String submissionMoment) {
        this.submissionMoment = submissionMoment;
    }

    public int getHabitID() {
        return habitID;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public int getUserID() {
        return userID;
    }

    public String getBook() {
        return book;
    }

    public String getSubmissionMoment() {
        return submissionMoment;
    }
    public void Add_Habit(Reading_Habit habit){
        this.Habits.add(habit);
    }
    public ArrayList<Reading_Habit> getList(){return this.Habits;}
    public String toString(){
        return getHabitID()+","+getBook()+","+getPagesRead()+","+getSubmissionMoment()+","+getUserID();
    }

}

