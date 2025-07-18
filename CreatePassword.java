import java.util.Scanner;
import java.sql.*;
import javax.swing.*;
public class CreatePassword
{
    public static void main(String[] args)
    {
        int ID = 0;
        while(ID != -1)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your User ID:");
            ID = scanner.nextInt();
            //issue with new line character
            scanner.nextLine();
            
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            
            LoginSecurity checking = new LoginSecurity();
            String display = checking.encryptKey(ID, password);
            System.out.println(display);
        };
    }
}
