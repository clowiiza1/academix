// Student class inheriting from User
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.*;


class Student extends User
{
    private Course[] arrCourses = new Course[20]; //Courses student is enrolled in
    private int count;
    private Course[] arrNewCourses = new Course[20]; //Additional Courses student can add
    private int newcount;
    private Course[] arrAddCourses = new Course[20]; //Additional courses student have but can drop
    private int addcount;
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";

    
    public Student() 
    {
        super();
        
    }
    public Student(int ID, String name, String surname, String email, String role) 
    {
        super(ID, name, surname, email, role);
        populateCoursesArray();
        populateNewCourses();
        populateAddCourses();
    }
    
    //Polymorphism method to display the courses that the student is current enrolled in
     public String displayCourses(){
        String str = "Courses that the student is enrolled in: ";
        for(int i = 0; i < count;i++)
        {
           str= str+ arrCourses[i].getCourseID()+", "; 
        }
        
        return str;
    }
    
    //array with courses that they can remove
    public Course[] getNewCourses()
    {
        return this.arrNewCourses;
    }
    //array with courses that they can add
    public Course[] getAddCourses()
    {
        return this.arrAddCourses;
    }
    public int getNewCount()
    {
        return this.newcount;
    }
    public int getAddCount()
    {
        return this.addcount;
    }
    
    //populates array waith courses they can add
    public void populateNewCourses()
    {
        try
        {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        
             // Prepare SQL statement
            String query = "SELECT * FROM Courses WHERE Compulsory = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 0);
            // Execute query
            ResultSet resultSet = statement.executeQuery();
            
             newcount=0;
            // Process results
            while (resultSet.next()) {
                String courseID = resultSet.getString("CourseID");
                boolean courseExists = false;
                
                // Check if student is already taking the course
                for (int i = 0; i < count; i++) {
                    if ((arrCourses[i].getCourseID()).equals(courseID)) {
                        courseExists = true;
                        continue;
                    }
                }
                
                if (!courseExists) {
                    String courseName = resultSet.getString("CourseName");
                    int userID = resultSet.getInt("UserID");
                    int credits = resultSet.getInt("Credits");
                    int compulsory = resultSet.getInt("Compulsory");

                    // Add the course to the list of courses
                    arrNewCourses[newcount] = new Course(courseID, courseName, userID, credits, compulsory);
                    newcount++; // Increment count of courses
                }
                
            }
            }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }
    
    //Populates array with additional courses they can remove
    public void populateAddCourses()
    {
              addcount = 0;
            for (int i = 0; i < count; i++) 
                {
                if((arrCourses[i].getCompulsory())==0)
                {
                    arrAddCourses[addcount] = arrCourses[i];
                    System.out.printf("%2s%s\n", addcount+1,arrAddCourses[addcount].toString());
                    addcount++;
                }
              
                }
                System.out.println(addcount);
                
    }
    /* public ObservableList<Course> getAdditionalDatacourses()
    {
        ObservableList<Course> list = FXCollections.observableArrayList();
        try
        {
            
            for(int i=0 ; i < newcount ; i++)
            {
                list.add(arrNewCourses[i]);
            }
        }
        catch(Exception e)
        {
            
        }
        return list;
    }*/
    public void populateCoursesArray()
    {
        try {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            // Prepare SQL statement
        
            String query = "SELECT * FROM Courses WHERE CourseID IN (Select CourseID FROM Results WHERE userID = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, super.getID());
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

                arrCourses[count] = new Course(courseID,courseName,userID,credits,compulsary);
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
    
    
    
    //Method to allow students to add a course that is not compulsory if they are not taking it already
    public boolean addCourse(Course addCourse) 
    {
         boolean result = false;
        try
        {
           
             Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);      
            System.out.println("***List of additional courses available***");
            System.out.printf("%2s%-12s%-25s%-15s\n","","CourseID","Course Name","Credits");
            for (int i = 0; i < newcount; i++) 
            {
              System.out.println((i+1)+" "+arrNewCourses[i].toString());    
            }
            // Close resources
            
    
             String query = "INSERT INTO Results(UserID,CourseID,ParticipationMark,ExamMark,FinalGrade) VALUES (?,?,NULL,NULL,NULL)";
            PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, super.getID());
                //arrCourses[choice-1]).getCourseID()
                statement.setString(2,addCourse.getCourseID());
                 int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Additional course successfully added. The course amount will be added to your student account");
                     populateCoursesArray();
                    populateNewCourses();
                    populateAddCourses();
                    result = true;
                } else {
                    System.out.println("Failed to add additional course.");
                   result = false;
                }
                
            connection.close();
            statement.close();
                        
        }
        catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
           return result;
    }
  
    public boolean dropCourse(Course dropCourse )
    {
        boolean result = false;
        try
        {
    
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            System.out.println("***List of additional courses you can drop***");
            System.out.printf("%2s%-12s%-25s%-15s\n","","CourseID","Course Name","Credits");
            String query = "DELETE FROM Results WHERE UserID = ? AND CourseID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, super.getID());
            statement.setString(2,dropCourse.getCourseID());
            int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Additional course successfully deleted. A representative will reach out to you regarding the financial process");
                    populateCoursesArray();
                    populateNewCourses();
                    populateAddCourses();
                    result = true;
                } else {
                    System.out.println("Failed to remove additional course.");
                    result = false;
                }
                connection.close();
            
                statement.close();
         }  
        catch (SQLException sqle)
        {
            
            sqle.printStackTrace();
            result = false;
        }
        return result;
    }
    
        
    
}