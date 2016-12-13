package util;

/**
 * Par Alexis le 06/12/2016.
 */

import org.json.JSONObject;
import play.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class ReCaptchaVerifier
{


    private static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SECRET = "6LcoJg4UAAAAAPog6eM6KkbOVRCRyK19YtrSfNSf";
    private final static String USER_AGENT = "Mozilla/5.0";

    /**
     *
     * @param gRecaptchaResponse Response of the post param
     * @return a boolean : true if captcha is fully completed and false if the captcha is invalid
     */
    public static boolean verify(final String gRecaptchaResponse)
    {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse))
            return false;

        try {

            final BufferedReader in = new BufferedReader(new InputStreamReader(getUrlConnection(gRecaptchaResponse).getInputStream()));
            final StringBuilder response = new StringBuilder();
            Logger.debug("url " + getUrlConnection(gRecaptchaResponse).getURL().toString());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            return new JSONObject(response.toString()).getBoolean("success");
        } catch (Exception e) {
            return false;
        }
    }

    private static HttpsURLConnection getUrlConnection(final String gRecaptchaResponse) throws Exception
    {
        final URL obj = new URL(URL);
        final HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);
        final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("secret=" + SECRET + "&response=" + gRecaptchaResponse);
        wr.flush();
        wr.close();
        return con;
    }
}

