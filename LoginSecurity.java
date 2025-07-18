import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;
import java.io.Console;
import java.security.MessageDigest;
import java.util.Arrays;

public class LoginSecurity {
    // Algorithm used for encryption and decryption
    private static final String ALGORITHM = "AES";
    public LoginSecurity(){
        
    }
    // Method to encrypt the password
    public String encryptKey(int userID, String password) {
        String encryptedPassword = "";
        try {
            // Generate a key based on the username
            Key key = generateKey((userID));
            // Create a cipher instance for AES algorithm
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Initialize cipher for encryption mode with the generated key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // Encrypt the password
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            // Encode the encrypted password to Base64
            encryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
            // Print the encrypted password
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    // Preferably don't want to use this rather encrypt password when logging in and checking whether it matches password in database

    // Method to generate a secret key based on the username
    private static Key generateKey(int userID) {
        try {
        // Convert the userID to bytes
        byte[] userIDBytes = Integer.toString(userID).getBytes("UTF-8");
        // Generate a secure hash of the userIDBytes using SHA-256
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = sha.digest(userIDBytes);
        // Truncate the hash to the appropriate key length (e.g., 128 bits)
        byte[] truncatedBytes = Arrays.copyOf(hashedBytes, 16); // 16 bytes = 128 bits
        // Create a secret key using the truncated bytes and AES algorithm
        return new SecretKeySpec(truncatedBytes, ALGORITHM);
        } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }

    // Method to hide the user input for password
    /*private static String hidePasswordInput() {
        Console console = System.console();
        if (console == null) {
            // If console is not available, use Scanner to read password
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter password: ");
            return scanner.nextLine();
        } else {
            char[] passwordChars = console.readPassword("Enter password: ");
            return new String(passwordChars);
        }*/
    //}

   
}