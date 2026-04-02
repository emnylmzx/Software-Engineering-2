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

    public static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        // connection string
        var url = "jdbc:sqlite:C:/Users/emnyl/Desktop/ReadingHabits.db";

        var conn = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");

        return conn;

    }


    public static void Create_tables(){
        String url="jdbc:sqlite:C:/Users/emnyl/Desktop/ReadingHabits.db";
        try(var conn=DriverManager.getConnection(url)){
            if(conn!=null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
            var User_table = "CREATE TABLE IF NOT EXISTS Users ("
                    + "	userID INTEGER PRIMARY KEY,"
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

  public static void AddUser(int userID,int Age, String Gender) throws SQLException, ClassNotFoundException {
      Connection c=connect();

      User user=new User(userID,Age,Gender);
      user.AddUser(user);

      var Sql_add=
              """
              INSERT INTO users(userID,age,gender)
              VALUES(%d,%d,'%s');
              """.formatted(userID,Age,Gender);
      System.out.println(Sql_add);
       var statement=c.createStatement();
       statement.executeUpdate(Sql_add);

      System.out.println("The user added:"+user.toString());
  }

  // add user to database
    public static void User_habit(int userID) throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var Sql= """
                SELECT*
                FROM 
                reading_habits
                WHERE
                userID=%d;
                """.formatted(userID);
        var statement=c.createStatement();
        ResultSet rs=statement.executeQuery(Sql);
        while (rs.next()) {
            System.out.println("User ID: " + rs.getInt("userID"));
            System.out.println("Habit: " + rs.getString("habitID"));
        }
    } // it will give you one specific user's habit with their id
    public static void Change_title(String Title, String new_Title) throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var sql= """
                UPDATE reading_habits
                SET book='%s'
                WHERE
                book='%s';
                """.formatted(new_Title,Title);
        var statement=c.createStatement();
        int rowsUpdated = statement.executeUpdate(sql);
        System.out.println(rowsUpdated);

    } // replace the title of a book
    public static void Delete_Readinghabit_record(int HabitID) throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var sql= """
                DELETE FROM reading_habits
                WHERE HabitID=%d;
                """.formatted(HabitID);
        var statement=c.createStatement();
        int Delete = statement.executeUpdate(sql);
        System.out.println(Delete+" Deleted");
    } //  delete one specific habit
    public static void Mean_age() throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var sql= """
                SELECT AVG(Age)
                FROM users
                """;
        var statement=c.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println("The Average Age:"+rs.getDouble(1));

    } // sum(user age)/ Num of users
    public static void Total_readers(String book, int page) throws SQLException, ClassNotFoundException {
        Connection c;
        c = connect();
        var sql = """
        SELECT COUNT(*) AS total_readers
        FROM reading_habits
        WHERE book='%s' AND pagesRead = %d;
        """.formatted(book, page);
        var statement=c.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println(rs.getInt("total_readers"));

    } // which book which pages: total users
    public static void All_Pages_read() throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var sql= """
                SELECT pagesRead  FROM reading_habits
                 SUM(pagesRead)
                """;
        var statement=c.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println(rs.getInt(1));
    } //
    public static void More_than_one() throws SQLException, ClassNotFoundException {
        Connection c=connect();
        var sql= """
                SELECT COUNT(*)userID
                FROM reading_habits
                WHERE pagesRead>1;
                """;
        var statement=c.createStatement();
        ResultSet rs=statement.executeQuery(sql);
        System.out.println(rs.getInt(1));
    } // return total user read >1;
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
                    INSERT INTO users(userID,age,gender)
                    VALUES('%d','%d','%s');
                    """.formatted(user.getUserID(), user.getAge(), user.getGender());
            System.out.println(sql);
        }

        for (int i = 0; i < Upload_Habits().size(); i++) {
            Reading_Habit habit = Upload_Habits().get(i);
            var sql = """
                    INSERT INTO reading_habits(habitID,userID,pagesRead,book,submissionMoment)
                    VALUES('%d','%d','%d','%s','%s');
                    """.formatted(habit.getHabitID(), habit.getUserID(), habit.getPagesRead(), habit.getBook(), habit.getSubmissionMoment());
            System.out.println(sql);
        }

    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner scanner=new Scanner(System.in);

            System.out.println("Add User:1\nReading habit data for a certain user:2\nChange the title of a book:3\nDelete a record:4\nMean age of the users:5\nTotal number of users that have read pages from a specific book:6\nTotal number of pages read by all users:7\nTotal number of users that have read more than one book:8");
            int Input=scanner.nextInt();
            switch (Input){
                case 1:
                    System.out.println("User ID:");
                    int userID= scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Age:");
                    int Age= scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Gender:{f/m}");
                    String Gender=scanner.nextLine();
                    AddUser(userID,Age,Gender);
                case 2:
                    System.out.println("Enter User ID:");
                     int ID=scanner.nextInt();
                    User_habit(ID);
                case 3:
                    System.out.println("Current Title:");
                    String Title=scanner.nextLine();
                    scanner.nextLine();
                    System.out.println("New Title:");
                    String new_Title=scanner.nextLine();
                    Change_title(Title,new_Title);
                case 4:
                    System.out.println("Enter ID:");
                    int id=scanner.nextInt();
                   Delete_Readinghabit_record(id);
                case 5:
                    Mean_age();
                case 6:
                    System.out.println("Book Name:");
                    String name=scanner.nextLine();
                    scanner.nextLine();
                    System.out.println("Page:");
                    int page=scanner.nextInt();
                   Total_readers(name,page);
                case 7:
                    All_Pages_read();
                case 8:
                    More_than_one();
                case 9: Create_tables();
                default:
                    System.out.println("Invalid Input please try again.");

            }
        }
}