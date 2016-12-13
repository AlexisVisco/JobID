package util;

/**
 * Par Alexis le 05/11/2016.
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtil
{

    private CryptUtil() {
    }

    public static String generateSHA256(String message){
        try
        {
            return hashString(message, "SHA-256");
        } catch (HashGenerationException e)
        {
            e.printStackTrace();
        }
        return "Error";
    }

    private static String hashString(String message, String algorithm)
            throws HashGenerationException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashGenerationException(
                    "Could not generatePage hash from String", ex);
        }
    }

    private static class HashGenerationException extends Throwable
    {
        public HashGenerationException(String s, Exception ex)
        {
        }
    }


    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    private static boolean isSamePwd(String defaultHash, String enteredPassword)
    {
        String s = generateSHA256(enteredPassword);
        return s.equals(defaultHash);
    }




}
