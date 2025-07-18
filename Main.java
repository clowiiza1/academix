import java.util.Scanner;
import java.sql.*;
import javax.swing.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

// Main class
public class Main extends Application {
    @Override      
    public void start(Stage stage)
    {
        try{
        Parent root = FXMLLoader.load(getClass().getResource("loginnew.fxml"));
        Scene login = new Scene(root);
        stage.setScene(login);
        stage.show();
        stage.setMaximized(true);
        }
        catch(Exception e)
        {
        e.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    /*public static void main(String[] args) {
        Connection currentConnection ;
        Scanner scanner = new Scanner(System.in);
        // Prompt the user to enter their details
        System.out.println("Enter your User ID:");
        int ID = scanner.nextInt();
        //issue with new line character
        scanner.nextLine();
        
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        
        LoginSecurity checking = new LoginSecurity();
        String display = checking.encryptKey(ID, password);
        
        //Logging user in using login class & login security class
        try
        {
            currentConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/resultprocessing","root","cHLOE2003@#");
            Login currentLogin = new Login(ID, password, currentConnection);
            //checking whether password and username is correct and returning a boolean
            boolean flag = currentLogin.authenticateUser();
            while(!flag)
            {
                System.out.println("Enter your User ID press -1 to exit:");
                ID = scanner.nextInt();
                scanner.nextLine();
                if(ID == -1)
                {
                    System.exit(0);
                }
                else
                {
                   System.out.println("Enter your password:");
                   password = scanner.nextLine(); 
                   currentLogin = new Login(ID, password, currentConnection);
                   flag = currentLogin.authenticateUser();
                }
    
            }
            //Closing the connection
            currentConnection.close();
            
            User currentUser = activatingUser(ID);
            if(currentUser instanceof Student)
            {
                //Options would you like to change password, view results, add or drop a module
                Scanner sc = new Scanner(System.in);
                System.out.println("What would you like to do: 1) View results 2) Change password 3) Manage additional modules");
                int choice = sc.nextInt();
                while(choice < 0 || choice > 3)
                {
                   System.out.println("What would you like to do: 1) View results 2) Change password 3) Manage additional modules");
                    choice = sc.nextInt(); 
                    if(choice == -1)
                    {
                        System.exit(0);
                    }
                }
                if( choice == 1) //Viewing all current results
                {
                    ((Student)currentUser).viewResults();
                }
                else if(choice == 2) //Changing password
                {
                    ((Student)currentUser).changePassword();   
                }
                else if(choice == 3) //Managing courses i.e adding additional course and deleting additional course
                {
                    System.out.println("What would you like to do: 1) Add an additional course 2) Delete an additional course (-1 to exit)");
                    choice = sc.nextInt(); 
                    if(choice == -1)
                    {
                        System.exit(0);
                    }
                    else if(choice ==1)
                    {
                        ((Student)currentUser).addCourse();
                    }
                     else if (choice == 2)
                     {
                         ((Student)currentUser).dropCourse();
                     }
                }
            }
            else if(currentUser instanceof Admin)
            {
                //maintain users, maintain courses
                //maintain users entails update,delete,add user
                //maintain users entails update,delete, add course
            }
            else if(currentUser instanceof Lecturer)
            {
                //maintain results, mantain attendance
                //maintain  
            }
            
        }
        catch( SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: "+ex);
        }
    }*/
     //Creating user class i.e. Student,Admin, Lecturer depending on username and password
    
}


