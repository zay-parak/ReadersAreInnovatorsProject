package passwordEncryption;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PassBasedEnc {
    private static final Random random = new SecureRandom();
    private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int iterations = 10000;
    private static final int keylength = 256;

    //Generates the salt first, associating it with the user
    public static String getSaltValue(int length){
        StringBuilder finalval = new StringBuilder(length);
        for (int i=0;i<length;i++){
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }
        return new String(finalval);
    }
    //Hashes the
    public static byte[] hash(char[] password,byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password,salt,iterations,keylength);
        Arrays.fill(password,Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }finally {
            spec.clearPassword();
        }

    }
    //Takes in the salt from getSaltValue() and the password from the user, hashes them using hash()
    public static String generateSecurePassword(String password,String salt){
        String finalval = null;
        byte[] securePassword = hash(password.toCharArray(),salt.getBytes());
        finalval = Base64.getEncoder().encodeToString(securePassword);
        return finalval;
    }
    //User provides providedPassword, securePassword and salt come from the database
    public static boolean verifyUserPassword(String providedPassword,String securePassword,String salt){
        boolean finalval = false;
        String newSecurePassword = generateSecurePassword(providedPassword,salt);
        finalval = newSecurePassword.equalsIgnoreCase(securePassword);
        return finalval;
    }
}
