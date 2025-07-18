import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

class Lecturers extends User {
    private Course[] arrLCourses = new Course[10];
    private int count ;
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";

    public Lecturers(int ID, String name, String surname, String email, String role) {
        super(ID, name, surname, email, role);
    }
    
    public void populateLecturerCourses()
    {
        try {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            // Prepare SQL statement
        
            String query = "SELECT * FROM Courses WHERE UserID = ?";
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

                arrLCourses[count] = new Course(courseID,courseName,userID,credits,compulsary);
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
    //Polymorphism method to display the courses that the lecturer teaches
     public String displayCourses(){
         populateLecturerCourses();
            String str = "Courses that the lecturer teaches: ";
        for(int i = 0; i < count;i++)
        {
           str= str+ arrLCourses[i].getCourseID()+", "; 
        }
        
        return str;
    }

    public  ObservableList<String> viewResults() {
        ObservableList<String> viewResults = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String query = "SELECT * FROM Results  WHERE CourseID IN (Select CourseID FROM Courses WHERE userID = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, super.getID());
            ResultSet resultSet = statement.executeQuery();
            String viewAllResults = null;
            
            viewResults.add(String.format("%-20s%-20s%-30s%-30s%-30s", "Student ID", "Course ID", "Participation Mark", "Exam Mark", "Final Mark"));
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String courseID = resultSet.getString("CourseID");
                double participationMark = resultSet.getDouble("ParticipationMark");
                double examMark = resultSet.getDouble("ExamMark");
                double finalGrade = resultSet.getDouble("FinalGrade");

                viewAllResults = String.format("%-20s%-20s%-35.2f%-35.2f%-35.2f", userID, courseID, participationMark, examMark, finalGrade);
                viewResults.add(viewAllResults);
            }

            connection.close();
            resultSet.close();
            statement.close();
            return viewResults;
        } catch (SQLException e) {
            e.printStackTrace();
            return viewResults;
        }
    }

    public boolean addResults( int studentID, String courseID, double participationMark, double examMark) {
        boolean flag = false;
        try {
            
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String query = "INSERT INTO results (UserID, CourseID, ParticipationMark, ExamMark, FinalGrade) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentID);
            statement.setString(2, courseID);
            statement.setDouble(3, participationMark);
            statement.setDouble(4, examMark);
            double finalGrade = ((participationMark * 0.4) + (examMark * 0.6));
            statement.setDouble(5, finalGrade);
            statement.executeUpdate();

            statement.close();
            connection.close();
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            return flag;
        }
    }

    public ObservableList<String>  getResults(int studentID) {
            ObservableList<String> viewSearchedResults = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String query = "SELECT * FROM results WHERE UserID = ? AND CourseID IN (Select CourseID FROM Courses WHERE userID = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentID);
            statement.setInt(2, super.getID());
            ResultSet resultSet = statement.executeQuery();
            String viewSearch = null;
            viewSearchedResults.add(String.format("%-20s%-20s%-30s%-30s%-30s", "Student ID", "Course ID", "Participation Mark", "Exam Mark", "Final Mark"));
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String courseID = resultSet.getString("CourseID");
                double participationMark = resultSet.getDouble("ParticipationMark");
                double examMark = resultSet.getDouble("ExamMark");
                double finalGrade = resultSet.getDouble("FinalGrade");

                viewSearch = String.format("%-20s%-20s%-35.2f%-35.2f%-35.2f", userID, courseID, participationMark, examMark, finalGrade);
                viewSearchedResults.add(viewSearch);
            }
            connection.close();
            statement.close();
            resultSet.close();
            return viewSearchedResults;
        } catch (SQLException e) {
            e.printStackTrace();
            return viewSearchedResults;
        }
    }

    public boolean updateResults(String studentID, String courseID, double participationMark, double examMark) {
        try (Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD)) {
            String query = "UPDATE results SET ParticipationMark = ?, ExamMark = ?, FinalGrade = ? WHERE UserID = ? AND CourseID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, participationMark);
                statement.setDouble(2, examMark);
                double finalGrade = ((participationMark * 0.4) + (examMark * 0.6));
                statement.setDouble(3, finalGrade);
                statement.setInt(4, Integer.parseInt(studentID));
                statement.setString(5, courseID);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return true; // Update successful
                } else {
                    return false; // No rows affected, update failed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during update
        }
    }


    public boolean deleteResults(int studentID, String courseID) {
        try (Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD)) {
            String query = "DELETE FROM results WHERE UserID = ? AND CourseID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentID);
                statement.setString(2, courseID);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return true; // Deletion successful
                } else {
                    return false; // No rows affected, deletion failed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during deletion
        }
    }
}
