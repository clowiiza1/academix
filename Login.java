import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Login {
    private int userID;
    private String password;
    private Connection connection;
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";

    
    public Login()
    {
        
    }
    public Login(int userID, String password, Connection connection)
    {
        setUserID(userID);
        setPassword(password);
        this.connection = connection;
    }
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public int getUserID()
    {
        return this.userID;
    }
    public String getPassword()
    {
        return this.password;
    }
    // Method to authenticate user login
    public boolean authenticateUser() {
        // Check if username and password are not empty
        if (this.getUserID() == 0 || (this.getPassword()).isEmpty() || this.getUserID() > 99999999) {
            System.out.println("UserID and password cannot be empty.");
            return false;
        }

        //Veriyfing if password is correct
        boolean isAuthenticated = authenticateInDatabase();

        if (isAuthenticated) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
        
        return isAuthenticated;
        //Use in while loop in main
    }

    // method to check whether passwords match in database
    public boolean authenticateInDatabase() {
        try
        {
            String databasePassword;
            LoginSecurity login = new LoginSecurity();
            String encryptedPassword = login.encryptKey(this.getUserID(),this.getPassword());
            String sql = "SELECT Password FROM Users WHERE UserID = ?";
            
            
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set parameters for the student's record
            // '1' and '2' refer to the position/order of the placeholders in the SQL string
            statement.setInt(1, userID); // Assuming studentID is the ID of the student whose record you want to retrieve
            // Execute query
            ResultSet resultingRecords = statement.executeQuery();
            if (resultingRecords.next()) 
            {
                databasePassword = resultingRecords.getString("Password");
                if(encryptedPassword.equals(databasePassword))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            
            else
            {
                return false;
            }
        }
        catch (SQLException e) 
        {
                e.printStackTrace();
                return false;
        }
    }
    
   public User activatingUser(int ID)
    {
        User currentUser = null;
        try
        {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
            String role, email, name, surname;
            
            String sql = "SELECT * FROM Users WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ID);
            ResultSet resultingRecords = statement.executeQuery();
            if (resultingRecords.next()) 
            {
                role = resultingRecords.getString("Role");
                name = resultingRecords.getString("Name");
                surname = resultingRecords.getString("Surname");
                email = resultingRecords.getString("Surname");
                if(role.equals("A"))
                {
                    currentUser = new Admin(ID,name,surname,email,role);
                    return currentUser;
                }
                else if(role.equals("S"))
                {
                    currentUser = new Student(ID,name,surname,email,role); 
                    return currentUser;
                }
                else if(role.equals("L"))
                {
                    currentUser = new Lecturers(ID,name,surname,email,role);
                    return currentUser;
                }
                
            }
            
             return currentUser;
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
             return currentUser;
        }
        
    }
}

