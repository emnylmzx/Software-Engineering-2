package org.example;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;
public class Main {

    public static void connect() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        // connection string
        var url = "jdbc:sqlite:C:/Users/emnyl/Desktop/ReadingHabits.db";

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void Create_tables(){
        try {
            connect();
            String url="jdbc:sqlite:C:/Users/emnyl/Desktop/ReadingHabits.db";
            try(var conn=DriverManager.getConnection(url)){
                if(conn!=null) {
                    var meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }
                var User_table = "CREATE TABLE IF NOT EXISTS Users ("
                        + "	userID INTEGER PRIMARY KEY,"
                        + "	Name TEXT NOT NULL,"
                        +  "Age INTEGER,"
                        + "	Gender TEXT"
                        + ");";
                var Reading_Habits="CREATE TABLE IF NOT EXISTS ReadingHabit("
                        + "habitID INTEGER PRIMARY KEY,"
                        +"userID INTEGER,"
                        +"book TEXT,"
                        +"pagesRead INTEGER,"
                        +"submissionMoment DATETIME,"
                        +"FOREIGN KEY(userID) REFERENCES Users(userID)"
                        +");";
                conn.createStatement().execute("PRAGMA foreign_keys = ON;");
                conn.createStatement().execute(User_table);
                conn.createStatement().execute(Reading_Habits);
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }




    public static List<List<String>> Csv_to_Array(String file){
        String line;
        String delimiter=",";
        List<List<String>> list=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(file))){
            while ((line=br.readLine())!=null){
                String[] values=line.split(delimiter);
                list.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list.subList(1, list.size());
    }

  public static String AddUser(int userID, String Name,int Age, String Gender){
          User user=new User(userID,Age,Gender);
          user.AddUser(user);
          var Sql_add=
                  """
                  INSERT INTO User_table(UserID,Name,Age,Gender)
                  VALUES(%d,%s,%d,%s)
                  """.formatted(userID,Name,Age,Gender);
          return Sql_add;
  } // add user to database
    public static String User_habit(int userID){
        var Sql= """
                SELECT*
                FROM 
                Reading_Habits
                WHERE
                userID=%d;
                """.formatted(userID);
           return Sql;
    } // it will give you one specific user's habit with their id
    public static String Change_title(String Title, String new_Title){
        var sql= """
                UPDATE Reading_Habits
                SET book='%s'
                WHERE
                book='%s';
                """.formatted(new_Title,Title);
        return sql;
    } // replace the title of a book
    public static String Delete_Readinghabit_record(int HabitID){
        var sql= """
                DELETE FROM Reading_Habits
                WHERE HabitID='%d';
                """.formatted(HabitID);
        return sql;
    } //  delete one specific habit
    public static String Mean_age(){
        var sql= """
                SELECT AVG(Age)
                FROM User_table
                """;
        return sql;
    } // sum(user age)/ Num of users
    public static String Total_readers(String book, int page){
        var sql= """
                SELECT COUNT(*) AS userID FROM Reading_Habits
                WHERE book='%s' AND pagesRead ='%d';
                """.formatted(book,page);
        return sql;
    } // which book which pages: total users
    public static String All_Pages_read(String book){ //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        var sql= """
                SELECT pagesRead  FROM Reading_Habits
                WHERE COUNT (userID)='%d'
                """.formatted(Upload_Readers().size());
        return sql;
    } //
    public static String More_than_one(){
        var sql= """
                SELECT COUNT(*)userID
                FROM Reading_Habits
                WHERE pagesRead>1;
                """;
        return sql;
    } // return total user read >1;
    public static void Upload_Table(){
        var sql= """
                
                """;
    }
    public static ArrayList<User> Upload_Readers(){
        ArrayList<User> users=new ArrayList<>();
        List<List<String>> user_data=Csv_to_Array("C:\\Users\\emnyl\\Desktop\\Software Languages\\Java\\BookTracker\\Data\\users.csv");
        for(int i=0;i<user_data.size();i++){
         int ID=Integer.parseInt(user_data.get(i).getFirst());
         int Age=Integer.parseInt(user_data.get(i).get(1));
         String Gender=user_data.get(i).getLast();
         User user=new User(ID,Age,Gender);
         users.add(new User(ID,Age,Gender));
            user.AddUser(users.get(i));
        }
        return users;

    }
    public static ArrayList<Reading_Habit> Upload_Habits(){
        ArrayList<Reading_Habit> ReadingHabits=new ArrayList<>();
        List<List<String>> Habit_data=Csv_to_Array("C:\\Users\\emnyl\\Desktop\\Software Languages\\Java\\BookTracker\\Data\\reading_habits_dataset.csv");
        for(int i=0;i<Habit_data.size();i++){
            int habitID=Integer.parseInt(Habit_data.get(i).getFirst());
            int UserID=Integer.parseInt(Habit_data.get(i).get(1));
            int pagesRead=Integer.parseInt(Habit_data.get(i).get(2));
            String book=Habit_data.get(i).get(3);
            String submissionMoment=Habit_data.get(i).getLast();
            Reading_Habit habit=new Reading_Habit(habitID,UserID,pagesRead,book,submissionMoment);
            ReadingHabits.add(new Reading_Habit(habitID,UserID,pagesRead,book,submissionMoment));
            habit.Add_Habit(ReadingHabits.get(i));
        }
        return ReadingHabits;
    }
    public static void Load_Content() {


        for (int i = 0; i < Upload_Readers().size(); i++) {
            User user = Upload_Readers().get(i);
            var sql = """
                    INSERT INTO User_table(userID,age,gender)
                    VALUES('%d','%d','%s');
                    """.formatted(user.getUserID(), user.getAge(), user.getGender());
            System.out.println(sql);
        }

        for (int i = 0; i < Upload_Habits().size(); i++) {
            Reading_Habit habit = Upload_Habits().get(i);
            var sql = """
                    INSERT INTO Reading_Habits(habitID,userID,pagesRead,book,submissionMoment)
                    VALUES('%d','%d','%d','%s','%s');
                    """.formatted(habit.getHabitID(), habit.getUserID(), habit.getPagesRead(), habit.getBook(), habit.getSubmissionMoment());
            System.out.println(sql);
        }

    }
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Boolean Runner=true;
        while(Runner){
            System.out.println("Add User:1\nReading habit data for a certain user:2\nChange the title of a book:3\nDelete a record:4\nMean age of the users:5\nTotal number of users that have read pages from a specific book:6\nTotal number of pages read by all users:7\nTotal number of users that have read more than one book:8");
            int Input=scanner.nextInt();
            switch (Input){
                case 1:
                    System.out.println("User ID:");
                    int userID= scanner.nextInt();
                    System.out.println("User name:");
                    String Name=scanner.nextLine();
                    System.out.println("Age:");
                    int Age= scanner.nextInt();
                    System.out.println("Gender:{f/m}");
                    String Gender=scanner.nextLine();
                    System.out.println(AddUser(userID,Name,Age,Gender));
                case 2:
                    System.out.println("Enter User ID:");
                     int ID=scanner.nextInt();
                    System.out.println(User_habit(ID));
                case 3:
                    System.out.println("Current Title:");
                    String Title=scanner.nextLine();
                    System.out.println("New Title:");
                    String new_Title=scanner.nextLine();
                    System.out.println(Change_title(Title,new_Title));
                case 4:
                    System.out.println("Enter ID:");
                    int id=scanner.nextInt();
                    System.out.println(Delete_Readinghabit_record(id));
                case 5:
                    System.out.println(Mean_age());
                case 6:
                    System.out.println("Book Name:");
                    String name=scanner.nextLine();
                    System.out.println("Page:");
                    int page=scanner.nextInt();
                    System.out.println(Total_readers(name,page));
                case 7:
                    System.out.println("Which book:");
                    String book=scanner.nextLine();
                    System.out.println( All_Pages_read(book));
                case 8:
                    System.out.println(More_than_one());
                default:
                    System.out.println("Invalid Input please try again.");

            }
        }
    }
}