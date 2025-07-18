import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.fxml.Initializable;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class SceneController 
{
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int userID;
   public int currentID;
    public User currentUser;
    @FXML
    private TextField userIDField;
    @FXML 
    private TextField passwordField;
    @FXML
    private Label studentResultsName;
    @FXML
    private TextArea studentResults;
    @FXML
    private Button buttonLogout;
    @FXML 
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Button changePassword;
    @FXML
    private TextField addCourseID;
    @FXML
    private Button check;
    //All data necessary for populating course 
    @FXML
    private ListView<String> resultsListView;
    @FXML
    private ListView<String> adminViewCourseList;
    @FXML
    private TextField txtDeleteCourse;
    @FXML 
    private Button buttonViewCourses;
    @FXML
    private ListView listdisplayCourses;
    @FXML
    private ListView listremoveCourses;
    @FXML
    private TextField txtAddCourseCompulsory;
    @FXML
    private TextField txtAddCourseCredits;
    @FXML
    private TextField txtAddCourseID;
    @FXML
    private TextField txtAddCourseName;
    @FXML
    private TextField txtAddCourseUserID;
    @FXML
    private TextField txtUpdateUserRole;
    @FXML
    private TextField txtUpdateUserIDL;
    @FXML
    private TextField txtUpdateUserName;

    @FXML
    private TextField txtUpdateUserSurname;
    
    @FXML
    private TextField txtUpdateUserEmail;
    
    @FXML
    private TextField txtUpdateUserPassword;
    
    @FXML
    private ListView adminViewUsers;

    @FXML
    private TextField txtAddUserEmail;

    @FXML
    private TextField txtAddUserID;

    @FXML
    private TextField txtAddUserName;

    @FXML
    private TextField txtAddUserPassword;

    @FXML
    private TextField txtAddUserRole;

    @FXML
    private TextField txtAddUserSurname;

    @FXML
    private TextField txtDeleteUsers;
    @FXML
    private TextField txtAddCourseIDMark;

    @FXML
    private TextField txtAddExamMark;

    @FXML
    private TextField txtAddPartMark;

    @FXML
    private TextField txtAddResultID;

    @FXML
    private TextField txtAddStudentID;

    @FXML
    private TextField txtDeleteCourseIDMark;

    @FXML
    private TextField txtDeleteStudentIDMark;

    @FXML
    private TextField txtLecturerFindStudentID;

    @FXML
    private TextField txtUpdateCourseIDMark;

    @FXML
    private TextField txtUpdateExamMark;

    @FXML
    private TextField txtUpdatePartMark;

    @FXML
    private TextField txtUpdateResultIDMark;

    @FXML
    private TextField txtUpdateStudentIDMark;
    @FXML
    private ListView listViewLecturerResults;
    @FXML
    private TextField removeCourseID;
    @FXML
    private TextField txtUpdateCourseID;
    @FXML
    private TextField txtUpdateCourseName;
    @FXML
    private TextField txtUpdateCredits;
    @FXML
    private TextField txtUpdateUserID;
    @FXML
    private TextField txtUpdateCompolsury;
    
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";

    
 
 //Method to handle login   
   public void handleLogin(ActionEvent event) throws java.io.IOException {
    
    userID = Integer.parseInt(userIDField.getText());
    System.out.println("UserID: " + userID);
    currentID = userID;
    System.out.println("CurrentID: " + currentID);
    
    String password = passwordField.getText();
    Connection currentConnection = null;

    // Encrypt user ID and password
    LoginSecurity checking = new LoginSecurity();
    String encryptedKey = checking.encryptKey(userID, password);

    try {
        currentConnection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        Login currentLogin = new Login(userID, password, currentConnection);

        // Check if user authentication fails
        boolean flag = currentLogin.authenticateUser();
        if (!flag) {
            // Display error message for incorrect username or password
            showErrorDialog("Incorrect username or password. Please try again.");
        } else{
            // Activate user based on user type
            User currentUser = currentLogin.activatingUser(userID);
            ///////EXTRA
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setUserData(currentUser);
            //////Extra
            // Proceed with appropriate user interface based on user type
            if (currentUser instanceof Student) {
                switchToStudentHomePage(event);
            } else if (currentUser instanceof Admin) {   
                switchToAdminHomePage(event);
            } else if (currentUser instanceof Lecturers) {
                switchToLecturerHomePage(event);
            }
        
            // Close the connection
            currentConnection.close();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle SQL exception
    }
}

//Monday morning added to handle different home page returns for different user types, change password method needs to be adjusted
public void handleReturnToHomePageUsers(ActionEvent event) throws IOException
{
    try{
        
        Connection currentConnection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         User currentUser = (User)stage.getUserData();
         
         
         if (currentUser instanceof Student) {
                switchToStudentHomePage(event);
            } else if (currentUser instanceof Admin) {   
                switchToAdminHomePage(event);
            } else if (currentUser instanceof Lecturers) {
                switchToLecturerHomePage(event);
            }
            
            currentConnection.close();
    }catch (SQLException ex) {
        ex.printStackTrace();
        // Handle SQL exception
    }
}
     
public void populateremoveCourses(ActionEvent event)
{
        Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
        Student currentUser = (Student)stage.getUserData(); //Works
        Course[] arrCourses = currentUser.getAddCourses();
        ObservableList<String> resultsremoveList = FXCollections.observableArrayList();
        resultsremoveList.add(String.format("%-20s%-30s%-10s", "CourseID", "Course Name", "Credits"));
       currentUser.populateAddCourses();
        for(int i = 0 ; i < currentUser.getAddCount(); i ++)
        {
             String resultString = String.format("%s", arrCourses[i].toString());
             System.out.println(resultString);
            resultsremoveList.add(resultString);
        }   
        listremoveCourses.setItems(resultsremoveList);
          
}
    //Method to populate listview on add additional course tab
public void populateaddCourses(ActionEvent event)
{
        Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
        Student currentUser = (Student)stage.getUserData();
        Course[] arrCourses = currentUser.getNewCourses();
        ObservableList<String> resultsList = FXCollections.observableArrayList();
        resultsList.add(String.format("%-20s%-30s%-10s", "CourseID", "Course Name", "Credits"));
        //String str = String.format("%-10s%-30s%-5d",this.getCourseID(),this.getCourseName(),this.getCredits()); 
        for(int i = 0 ; i < currentUser.getNewCount(); i ++)
        {
             String resultString = String.format("%s", arrCourses[i].toString());
            resultsList.add(resultString);
        }    
        listdisplayCourses.setItems(resultsList); 
}
public void handleAddCourse(ActionEvent event)
{
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Student currentUser = (Student)stage.getUserData();
        String courseID = addCourseID.getText();
        addCourseID.clear();
        Course[] arrNewCourses = currentUser.getNewCourses();
        int place=0;
        boolean flag = false;
        for(int i = 0 ; i < currentUser.getNewCount(); i++)
        {
            
            if(courseID.equals(arrNewCourses[i].getCourseID()))
            {
                flag = true;
                place = i;
                break;
            }
        }
        if(flag)
        {
               if(currentUser.addCourse(arrNewCourses[place]))
               {
                   showConfirmDialog("Additional course successfully added. The course amount will be added to your student account.");
                    
                }
               else
               {
                    showErrorDialog("Failed to add additional course.");  
                    
               }
               
        }
        else
        {
            showErrorDialog("Invalid Course ID. Please enter a valid couse ID from the list");
        }
}

public void handleRemoveCourse(ActionEvent event)
{
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Student currentUser = (Student)stage.getUserData();
        String courseID = removeCourseID.getText();
        removeCourseID.clear();
        Course[] arrAddCourses = currentUser.getAddCourses();
        int place=0;
        boolean flag = false;
        for(int i = 0 ; i < currentUser.getAddCount(); i++)
        {
            
            if(courseID.equals(arrAddCourses[i].getCourseID()))
            {
                flag = true;
                place = i;
                break;
            }
        }
        if(flag)
        {
               if(currentUser.dropCourse(arrAddCourses[place]))
               {
                   showConfirmDialog("Additional course successfully deleted. A representative will reach out to you regarding the financial process.");
                    
                }
               else
               {
                    showErrorDialog("Failed to remove additional course.");  
                    
               }
               
        }
        else
        {
            showErrorDialog("Invalid Course ID. Please enter a valid couse ID from the list");
        }
}

public void Exit(ActionEvent event)
{
    System.exit(0);
}
//Sunday night work Joshua works
public void handleAdminRemoveCourse(ActionEvent event)
{
    String deleteID = txtDeleteCourse.getText(); 
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin)stage.getUserData();
    boolean message = currentUser.deleteCourse(deleteID);
    if(message){
    showConfirmDialog("Course removed succesfully");
    }
    else{
    showErrorDialog("Course failed to be removed");
    
    }
    txtDeleteCourse.clear();
    
}
//Sunday night work Joshua works
//FXML thing needed to get the scene to load
@FXML
private void handleAdminAddCourse(ActionEvent event){
    String courseID = txtAddCourseID.getText(); 
    int credits = Integer.parseInt(txtAddCourseCredits.getText());
    String courseName = txtAddCourseName.getText();
    int userID = Integer.parseInt(txtAddCourseUserID.getText());
    //int compulsory = Integer.parseInt(txtAddCourseCompulsory.getText());
    int compulsory = -1;
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin)stage.getUserData();
    if((txtAddCourseCompulsory.getText().toUpperCase()).equals("Y"))
    {
        compulsory = 1;
    }
    else if((txtAddCourseCompulsory.getText().toUpperCase()).equals("N"))
    {
        compulsory = 0;
    }
    if (compulsory != 0 && compulsory != 1) {
        // Show error message
        showErrorDialog("Compulsory field must be either Y or N");
        return; // Exit method
    }
    else
    {
        boolean message = currentUser.addCourse( courseID,  courseName, credits,  userID,  compulsory);
        if(message){
            showConfirmDialog("Course added succesfully");
            }
        else{
            showErrorDialog("Course failed to be added");
    
            }
    }
    txtAddCourseID.clear(); 
    txtAddCourseCredits.clear();  
    txtAddCourseName.clear();
    txtAddCourseUserID.clear();
    txtAddCourseCompulsory.clear();
}
    
@FXML
private void handleAdminUpdateCourse(ActionEvent event) {
    String courseID = txtUpdateCourseID.getText(); 
    int credits = Integer.parseInt(txtUpdateCredits.getText());
    String courseName = txtUpdateCourseName.getText();
    int userID = Integer.parseInt(txtUpdateUserID.getText());
    
    String compulsoryText = txtUpdateCompolsury.getText().toUpperCase();
    int compulsory;
    
    if (compulsoryText.equals("Y")) {
        compulsory = 1;
    } else if (compulsoryText.equals("N")) {
        compulsory = 0;
    } else {
        // Show error message for invalid compulsory field
        showErrorDialog("Compulsory field must be either Y or N");
        return; // Exit method
    }
    
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin) stage.getUserData();

    boolean message = currentUser.updateCourse(courseID, courseName, credits, userID, compulsory);
    if (message) {
        showConfirmDialog("Course updated successfully");
    } else {
        showErrorDialog("Course failed to be updated");
    }
    
    // Clear input fields
    txtUpdateCourseID.clear(); 
    txtUpdateCredits.clear();  
    txtUpdateCourseName.clear();
    txtUpdateUserID.clear();
    txtUpdateCompolsury.clear();
}


//Sunday night work Joshua works
public void handleViewCourses(ActionEvent event){
    try{
    Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);

    
    String query = "SELECT CourseID,CourseName,Credits FROM courses";
    PreparedStatement statement = connection.prepareStatement(query);
    
    
    ObservableList<String> courseList = FXCollections.observableArrayList();
    courseList.add(String.format("%s\t\t\t\t%-30s%-10s","Course ID", "Course Name","Credits"));
    
    ResultSet resultSet = statement.executeQuery();
    
    while(resultSet.next()){
        String courseID = resultSet.getString("CourseID");
        String course = resultSet.getString("CourseName");
        int credits = resultSet.getInt("Credits");
        courseList.add(String.format("%s\t\t\t\t%-30s%-10d",courseID,course,credits));
        
        
    }
    adminViewCourseList.setItems(courseList);
    
    connection.close();
    resultSet.close();
    statement.close();
    }catch (SQLException e) {
        e.printStackTrace();
    }

    
}
//Method to display student results
public void handleViewResults(ActionEvent event) {
    try {
        Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
        // Retrieve current user
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        User currentUser = (User) stage.getUserData();
        // Prepare SQL statement
        String query = "SELECT * FROM results WHERE UserID = ?";
        System.out.println(currentUser.getID());
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, currentUser.getID());
        System.out.println("*******Displaying student results*******");
        // Create ObservableList to hold results
        ObservableList<String> resultsList = FXCollections.observableArrayList();
        // Add headings
        resultsList.add(String.format("%-30s%-30s%-30s%-30s", "Course", "Participation Mark", "Exam Mark", "Final Mark"));
        // Execute query
        ResultSet resultSet = statement.executeQuery();
        // Process results and populate the ObservableList
        while (resultSet.next()) {
            String course = resultSet.getString("CourseID");
            double participationMark = resultSet.getDouble("ParticipationMark");
            double examMark = resultSet.getDouble("ExamMark");
            double finalMark = resultSet.getDouble("FinalGrade");

            // Format result data using String.format()
            String resultString = String.format("%-35s%-32.2f%-32.2f%-32.2f", course, participationMark, examMark, finalMark);

            // Add result to the ObservableList
            resultsList.add(resultString);
        }
        // Set items of ListView to the ObservableList
        resultsListView.setItems(resultsList);
        // Close resources
        connection.close();
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//Monday morning work 
public void handleViewUsers(ActionEvent event)
{
    try{
    Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);

    
    String query = "SELECT UserID,Role, Name, Surname, Email FROM users";
    PreparedStatement statement = connection.prepareStatement(query);
    
    
    ObservableList<String> userList = FXCollections.observableArrayList();
    userList.add(String.format("%-20s%-10s%-15s\t%-15s\t%-30s", "User ID", "Role", "Name", "Surname", "Email"));
    
    ResultSet resultSet = statement.executeQuery();
    
    while(resultSet.next()){
        int userID = resultSet.getInt("UserID");
        String role = resultSet.getString("Role");
        String name = resultSet.getString("Name");
        String surname = resultSet.getString("Surname");
        String email = resultSet.getString("Email");
        
        String userString = String.format("%-20s%-10s%-15s\t%-15s\t%-30s", userID, role, name, surname, email);
        
        userList.add(userString);
    }
    adminViewUsers.setItems(userList);
    
    connection.close();
    resultSet.close();
    statement.close();
    }catch (SQLException e) {
        e.printStackTrace();
    }

    
}
//Monday morning work 
public void handleDeleteUser(ActionEvent event){
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin)stage.getUserData();
    int deleteUser = Integer.parseInt(txtDeleteUsers.getText());
    boolean message = currentUser.deleteUser(deleteUser);
    if(message){
    showConfirmDialog("User removed succesfully");
    }
    else{
    showErrorDialog("User failed to be removed");
    
    }
    txtDeleteUsers.clear();
    }
//Monday morining work 
public void handleAddUser(ActionEvent event){
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin)stage.getUserData();
    String role = txtAddUserRole.getText().toUpperCase();
    int userID = Integer.parseInt(txtAddUserID.getText());
    String name = txtAddUserName.getText();
    String surname = txtAddUserSurname.getText();
    String email = txtAddUserEmail.getText();
    String password = txtAddUserPassword.getText();
    
    if(!role.matches("[ASL]")){
        showErrorDialog("Please enter a valid role A/S/L");
        return;
    }
    
    boolean message = currentUser.addUser(userID,name,surname,email,role,password);
    if(message){
    showConfirmDialog("User added succesfully");
    }
    else{
    showErrorDialog("User failed to be added");
    
    }
    txtAddUserRole.clear();
    txtAddUserID.clear();
    txtAddUserName.clear();
    txtAddUserSurname.clear();
    txtAddUserEmail.clear();
     txtAddUserPassword.clear();
}
@FXML
public void handleUpdateUser(ActionEvent event)
{
    
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Admin currentUser = (Admin)stage.getUserData();
    String role = txtUpdateUserRole.getText().toUpperCase();
    int userID = Integer.parseInt(txtUpdateUserIDL.getText());
    String name = txtUpdateUserName.getText();
    String surname = txtUpdateUserSurname.getText();
    String email = txtUpdateUserEmail.getText();
    String password = txtUpdateUserPassword.getText();
    
    if(!role.matches("[ASL]")){
        showErrorDialog("Please enter a valid role A/S/L");
        return;
    }
    
    boolean message = currentUser.updateUser(userID,name,surname,email,role,password);
    if(message){
        showConfirmDialog("User updated succesfully");
    }
    else{
        showErrorDialog("User failed to be updated");
    
    }
    txtUpdateUserRole.clear(); 
    txtUpdateUserIDL.clear();  
    txtUpdateUserName.clear();
    txtUpdateUserSurname.clear();
       txtUpdateUserEmail.clear();
       txtUpdateUserPassword.clear();
}
public void handleChangePassword(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        User currentUser = (User)stage.getUserData();
        String newpassword = newPassword.getText();
        String confirmpassword = confirmPassword.getText();
        System.out.println(currentUser.toString());
        if(!newPassword.getText().equals(confirmPassword.getText())|| (newPassword.getText().equals("") || confirmPassword.getText().equals("")))
        {
            showErrorDialog("Passwords do not match, please try again");
            System.out.println("Passwords do not match");
        }
        else
        {
            boolean success = currentUser.changePassword(newpassword);
            System.out.println("success");
            if(success)
            {
                showConfirmDialog("Password has been changed successfully");
                try
                {
                    if(currentUser.getRole().equals("S"))
                    {
                        switchToStudentHomePage(event);
                    }
                    
                    else if(currentUser.getRole().equals("A"))
                    {
                        switchToAdminHomePage(event);
                    }
                    else if(currentUser.getRole().equals("L"))
                    {
                        switchToLecturerHomePage(event);
                    }
                }
                catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
            }
            else
            {
                showErrorDialog("Something went wrong");
            }
        }
      
}
//Joshua monday afternoon
public void handleLecturerViewAll(ActionEvent event)
{
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Lecturers currentUser = (Lecturers)stage.getUserData();
    
    ObservableList<String> viewResults = currentUser.viewResults();
    listViewLecturerResults.setItems(viewResults);
}
//Joshua monday afternoon
public void handleLecturerSearchStudent(ActionEvent event)
{
    String text = txtLecturerFindStudentID.getText();
    int studentID = Integer.parseInt(text);
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Lecturers currentUser = (Lecturers)stage.getUserData();
    
    ObservableList<String> viewResults = currentUser.getResults(studentID);
    listViewLecturerResults.setItems(viewResults);
    txtLecturerFindStudentID.clear();
}
//Adding results for a student
public void handleLecturerAddMark(ActionEvent event)
{
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Lecturers currentUser = (Lecturers)stage.getUserData();
    String courseID = txtAddCourseIDMark.getText();
    double examMark = Double.parseDouble(txtAddExamMark.getText());
    int studentID = Integer.parseInt(txtAddStudentID.getText());
    double participationMark = Double.parseDouble(txtAddPartMark.getText());
    if(txtAddCourseIDMark.getText().equals("") || txtAddExamMark.getText().equals("") || txtAddStudentID.getText().equals("") || txtAddPartMark.getText().equals(""))
    {
        showErrorDialog("Please ensure that all fields are filled in");
    }
    else
    {
        boolean message = currentUser.addResults(studentID,  courseID,  participationMark,  examMark);
        if(message){
            showConfirmDialog("Results added succesfully");
        }
        else{
            showErrorDialog("Results failed to be added");
            
        }
    }
    txtAddCourseIDMark.clear();
    txtAddExamMark.clear();
    txtAddPartMark.clear();
    txtAddStudentID.clear();
}
public void handelLLecturerRemoveMark(ActionEvent event)
{
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Lecturers currentUser = (Lecturers)stage.getUserData();
    int studentID = Integer.parseInt(txtDeleteStudentIDMark.getText());
    String courseID = txtDeleteCourseIDMark.getText();
    if(txtDeleteStudentIDMark.getText().equals("") || txtDeleteCourseIDMark.getText().equals(""))
    {
        showErrorDialog("Please ensure that all fields are filled in");
    }
    else
    {
        boolean message = currentUser.deleteResults(studentID, courseID);
        if(message){
            showConfirmDialog("Results removed succesfully");
        }
        else{
            showErrorDialog("Results failed to be removed");
        
        }
    }
    txtDeleteStudentIDMark.clear();
    txtDeleteCourseIDMark.clear();
}
public void handleLecturerUpdateMark(ActionEvent event){
    
    Node node = (Node) event.getSource();
    Stage stage = (Stage) node.getScene().getWindow();
    Lecturers currentUser = (Lecturers)stage.getUserData();
    String courseID = txtUpdateCourseIDMark.getText();
    double examMark = Double.parseDouble(txtUpdateExamMark.getText());
    double participationMark = Double.parseDouble(txtUpdatePartMark.getText());
    String studentID =  txtUpdateStudentIDMark.getText();

    if(txtUpdateCourseIDMark.getText().equals("") || txtUpdateExamMark.getText().equals("") || txtUpdatePartMark.getText().equals("") || txtUpdateStudentIDMark.getText().equals(""))
    {
        showErrorDialog("Please ensure that all fields are filled in");
    }
    else
    {

     boolean message = currentUser.updateResults( studentID,  courseID,  participationMark,  examMark);

        if(message){
            showConfirmDialog("Results updated succesfully");
        }
        else{
        showErrorDialog("Results failed to be updated");
        
        }
    }  
    txtUpdateCourseIDMark.clear();
    txtUpdateExamMark.clear();
    txtUpdatePartMark.clear();
    txtUpdateStudentIDMark.clear();
}

    // Method to display error dialog
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showConfirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        }
    
    
    public void switchToLogin(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginnew.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        
    }
    public void switchToChangePassword(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("changepassword.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        
    }
    public void switchToStudentHomePage(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentHomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        
    }
    public void switchToStudentViewResults(ActionEvent event) throws java.io.IOException {
        //studentResultsName.setText(getName() + getSurname() + " Resuts");
        Parent root = FXMLLoader.load(getClass().getResource("StudentsViewResults.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
    public void switchToStudentAddCourse(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentAddCourse.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLecturerHomePage(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LecturerHomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
        public void switchToLecturerResults(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LecturerViewResults.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    public void switchToAdminHomePage(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminHomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    public void switchToAdminManageCourses(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminManageCourse.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
        public void switchToAdminManageProfiles(ActionEvent event) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminManageProfiles.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    
}
