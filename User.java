// User class
import java.sql.*;
abstract class User implements UserImpl{
    
    private int ID;
    private String name;
    private String surname;
    private String email;
    private String role;
    
    // (CONSTR, USER, PASSWORD)
    public final String CONSTR = "jdbc:mysql://localhost:3306/resultprocessing";
    public final String USER = "root";
    public final String PASSWORD = "isuckatfort@01";

   
    public User()
    { 
    }
    public User(int ID, String name, String surname, String email, String role) 
    {
        setID(ID);
        setRole(role);
        setName(name);
        setSurname(surname);
        setEmail(email);
    }
    //Abstract method to be overrided in lecturer and student class and to display abstraction;
    public abstract String displayCourses();
    
    public int getID() {
        return this.ID;
    }
    public String getRole()
    {
        return this.role;
    }
    public String getName()
    {
        return this.name;
    }
    public String getSurname()
    {
        return this.surname;
    }
    public String getEmail()
    {
        return this.email;
    }
 
    public void setID(int ID)
    {
        this.ID = ID;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public void setName(String name)
    {
        this.name =name;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    public void setEmail(String email)
    {
         this.email = email;
    }
    @Override 
    public String toString()
    {
        String str = String.format("%-10d %-2s", this.getID(),this.getRole());
        return str;
    }
    public boolean changePassword(String newPassword)
    {
        boolean works = false;
        try
        {
            Connection connection = DriverManager.getConnection(CONSTR, USER, PASSWORD);
             // Prepare SQL statement
            String query = "UPDATE Users SET Password = ? WHERE UserID = ?";
            LoginSecurity check = new LoginSecurity();
            String encrypterpassword = check.encryptKey(this.getID(),newPassword);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, encrypterpassword);
            statement.setInt(2,this.getID());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                    System.out.println("Password successfully updated.");
                    works = true;
                } else {
                    System.out.println("Failed to update password.");
                    works = false;
                }
            
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        
        return works;
    }
}
