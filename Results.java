import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
class Results 
{
    private String UserID;
    private String CourseID;
    private double ParticaptionMark;
    private double ExamMark;
    private double finalMark;
    private List<String> gradeItems;
    private Connection connection;//for the database connection
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";


    public Results(String userID, String CourseID, double particaptionMark, double examMark, double finalMark) {
        this.UserID = userID;
        this.CourseID = CourseID;
        this.ParticaptionMark = particaptionMark;
        this.ExamMark = examMark;
        this.finalMark = finalMark;
        //changed Calulate final markas a static method(could be botj will decide after database implementation)
      //  calculateFinalMark();
        
        try {
            // Connection establishment code here
            connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public double getParticpationMark()
    {
        double participationMark = 0.0;
        try{
                 // Prepare SQL statement
                 // ? helps for sequirity against sql injection attacks ny using the smae quey with different paramters
                 
            String sql = "SELECT ParticipationMark FROM grades  WHERE userID = ? AND courseID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
              // Set parameters for the student's record
            statement.setString(1, UserID); // Assuming studentID is the ID of the student whose record you want to retrieve
            statement.setString(2, CourseID); // Assuming courseID is the ID of the course
            //1 and 2 refrences the place and order of the ? in the sql string
            
            // Execute query
            ResultSet result = statement.executeQuery();
            
            // Check if there's a result
            if (result.next())
            {
                //uses current row and colum (so basically the one we found with the sql statement)
                 participationMark = result.getDouble("ParticipationMark");
            }     
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
   
        return participationMark;
    }    
       public double getExamMark() 
       {
        double examMark = 0.0; // Default value for exam mark
        
        try {
            // Prepare SQL statement
            // The '?' placeholders help prevent SQL injection attacks by allowing the same query to be used with different parameters
            String sql = "SELECT ExamMark FROM Results WHERE userID = ? AND CourseID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set parameters for the student's record
            // '1' and '2' refer to the position/order of the placeholders in the SQL string
            statement.setString(1, UserID); // Assuming studentID is the ID of the student whose record you want to retrieve
            statement.setString(2, CourseID); // Assuming courseID is the ID of the course
            
            // Execute query
            ResultSet result = statement.executeQuery();
            
            // Check if there's a result
            if (result.next()) {
                // Get the exam mark from the current row and column (the one found with the SQL statement)
                examMark = result.getDouble("ExamMark");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an SQL exception occurs
        }
       
        return examMark; // Return the exam mark, which may be the default value or the retrieved value
    }
    
    public double getFinalMark()
    {
            return finalMark;
    }
    
     public static double calculateFinalMark(double particaptionMark, double examMark) {
        return (particaptionMark * 0.4) + (examMark * 0.6);
    }
    
      public void setFinalMark() {
        try {
            // Calculate the final mark using the static method
            double finalMark = calculateFinalMark(ParticaptionMark, ExamMark);
            
            // Prepare SQL statement to insert final mark into the database
            String sql = "INSERT INTO Results(UserID, CourseID, FinalMark) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, UserID); // Set the student ID parameter
            statement.setString(2, CourseID); // Set the course ID parameter
            statement.setDouble(3, finalMark); // Set the final mark parameter
            
            // Execute the insert query
            int rowsInserted = statement.executeUpdate();
            
            // Check if the insertion was successful
            if (rowsInserted > 0) {
                System.out.println("Final mark inserted successfully for student " + UserID + " in course " + CourseID + ".");
                this.finalMark = finalMark; // Update the final mark field in the Results object
            } else {
                System.out.println("Failed to insert final mark.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an SQL exception occurs
        }
    }
    
    public void updateGrade(String userID, String CourseID, String[] gradeItems) {
        try {
            // Prepare SQL statement to update grades in the database
            String sql = "UPDATE Results  SET ParticipationMark = ?, ExamMark = ? WHERE StudentID = ? AND CourseID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set parameters for the SQL statement
            // using arrays to store data locally as well
            statement.setDouble(1, Double.parseDouble(gradeItems[0])); // Assuming gradeItems[0] contains the updated participation mark
            statement.setDouble(2, Double.parseDouble(gradeItems[1])); // Assuming gradeItems[1] contains the updated exam mark
            statement.setString(3, userID); // Set the student ID parameter
            statement.setString(4, CourseID); // Set the course ID parameter
            
            // Execute the update query
            int rowsUpdated = statement.executeUpdate();
            
            // Check if the update was successful
            if (rowsUpdated > 0) {
                // Recalculate final mark and update locally
                this.ParticaptionMark = Double.parseDouble(gradeItems[0]);
                this.ExamMark = Double.parseDouble(gradeItems[1]);
                this.finalMark = calculateFinalMark(ParticaptionMark, ExamMark);
                System.out.println("Results updated successfully.");
            } else {
                System.out.println("Failed to update results.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an SQL exception occurs
        }
    }
}