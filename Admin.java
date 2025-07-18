//Admin class inheriting from User
import java.sql.*;
import java.util.Scanner;

class Admin extends User 
{
    //Database objects
    private Connection connection;
    private PreparedStatement statement;
    private String query;
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";
    private Course[] arrACourses = new Course[20];
    private int count;
    
    public Admin() 
    {
        
        
    }
    public Admin(int ID, String name, String surname, String email, String role) 
    {
        super(ID, name, surname, email, role);
    }
    
    //Polymorphism method to display the courses that exists at the university for the admin
     public String displayCourses(){
         populateAdminCourses();
            String str = "Courses that exist at the university: ";
        for(int i = 0; i < count;i++)
        {
           str= str+ arrACourses[i].getCourseID()+", "; 
        }
        return str;
    }
    public void populateAdminCourses()
    {
        try {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            // Prepare SQL statement
        
            String query = "SELECT * FROM Courses";
            PreparedStatement statement = connection.prepareStatement(query);
            count = 0;
            // Execute query
            ResultSet resultSet = statement.executeQuery();
            // Process results
            while (resultSet.next()) {
                String courseID = resultSet.getString("CourseID");
                String courseName = resultSet.getString("CourseName");
                int userID = resultSet.getInt("UserID");
                int credits = resultSet.getInt("Credits");
                int compulsary = resultSet.getInt("Compulsory");

                arrACourses[count] = new Course(courseID,courseName,userID,credits,compulsary);
                count++;
            }

            // Close resources
            connection.close();
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Managing users
    public boolean addUser(int ID, String name, String surname, String email, String role, String password)
   {
        
        // Encrypt password
        LoginSecurity encrypt = new LoginSecurity();
        password = encrypt.encryptKey(ID, password);
        
        // Establish database connection and execute insert query
        try 
        {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String query = "INSERT INTO users (UserID, Password, Role, Name, Surname, Email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, ID);
                statement.setString(2, password);
                statement.setString(3, role);
                statement.setString(4, name);
                statement.setString(5, surname);
                statement.setString(6, email);
                statement.executeUpdate();
                statement.close();
                connection.close();
                return true;
                 
         }
         catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(int ID, String name, String surname, String email, String role, String password) 
    {
        // Encrypt password
        LoginSecurity encrypt = new LoginSecurity();
        password = encrypt.encryptKey(ID, password);
        
        // Establish database connection and execute insert query
        try 
        {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String query = "UPDATE Users SET  Password = ?, Role = ?, Name = ?, Surname = ?, Email = ? WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
               
                statement.setString(1, password);
                statement.setString(2, role);
                statement.setString(3, name);
                statement.setString(4, surname);
                statement.setString(5, email);
                 statement.setInt(6, ID);
                statement.executeUpdate();
                statement.close();
                connection.close();
                return true;
                 
         }
         catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
 
    public boolean deleteUser(int ID)
    {
        //ID, name, surname, email,role
       boolean flag = false;
       //error handling for ID variable 
       //error handling if they are sure whether they want to delete
       try
       {       
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            query = "DELETE FROM users WHERE UserID = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, ID);
            statement.executeUpdate();
            connection.close();
            statement.close();
            flag = true;
            return flag;
        }
       catch (SQLException sqle)
       {
           sqle.printStackTrace();
           return flag;
        }

    }
    //Managing courses
public boolean addCourse(String courseID, String courseName,int credits, int userID, int compulsory) {
    
    // Database interaction
    try {
        
        Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        String query = "INSERT INTO courses (CourseID, CourseName, Credits, UserID, Compulsory) VALUES (?, ?, ?, ?, ?)";
          PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, courseID);
            statement.setString(2, courseName);
            statement.setInt(3, credits);
            statement.setInt(4, userID);
            statement.setInt(5, compulsory);
            statement.executeUpdate();
            connection.close();
            statement.close();
            return true;
          }
     catch (SQLException sqle) {
        sqle.printStackTrace();
        return false;
    }
}

    public boolean deleteCourse(String courseID) {
    try {
        Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        query = "DELETE FROM courses WHERE CourseID = ?";
        statement = connection.prepareStatement(query);
        statement.setString(1, courseID);
        int rowsAffected = statement.executeUpdate(); // Get the number of rows affected
        connection.close();
        statement.close();
        
        if (rowsAffected > 0) {
            return true; // Deletion successful
        } else {
            return false; // Course with given courseID does not exist
        }
    } catch (SQLException sqle) {
        sqle.printStackTrace();
        return false; // Exception occurred
    }
}
    
    public boolean updateCourse(String courseID, String courseName,int credits, int userID, int compulsory) {
    
    // Database interaction
    try {
        
        Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        String query = "UPDATE courses SET  CourseName = ?, Credits = ?, UserID = ?, Compulsory = ? WHERE CourseID = ?";
          PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, courseName);
            statement.setInt(2, credits);
            statement.setInt(3, userID);
            statement.setInt(4, compulsory);
            statement.setString(5,courseID);
            statement.executeUpdate();
            connection.close();
            statement.close();
            return true;
          }
     catch (SQLException sqle) {
        sqle.printStackTrace();
        return false;
    }
}
    
}