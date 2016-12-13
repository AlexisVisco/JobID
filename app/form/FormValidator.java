package form;

import form.typevalidator.FormError;
import form.typevalidator.FormSuccess;
import form.typevalidator.Validator;
import play.Logger;
import play.data.DynamicForm;
import util.ReCaptchaVerifier;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Par Alexis le 06/11/2016.
 */

public class FormValidator
{

    private DynamicForm df;

    public FormValidator(DynamicForm df)
    {
        this.df = df;
    }

    public String get(String key)
    {
        return df.get(key);
    }

    public Validator validate(String... fieldsRequired)
    {
        for (String s : fieldsRequired) {
            Logger.info(s + "  " + df.get(s));
            if (df.get(s) == null || df.get(s).isEmpty())
                return new FormError("Les champs du formulaire ne sont pas tous remplis.");
        }
        return new FormSuccess();
    }

    public boolean isSet(String s)
    {
        return  (df.get(s) != null && !df.get(s).isEmpty());
    }

    public Validator isValidEmail(String field)
    {
        InternetAddress emailAddr = null;
        try {
            emailAddr = new InternetAddress(df.get(field));
            emailAddr.validate();
            return new FormSuccess();
        } catch (AddressException e) {
            return new FormError("L'adresse email est incorrecte.");
        }

    }

    public boolean isValidURL(String pUrl) {

        URL u = null;
        try {
            u = new URL(pUrl);
        } catch (MalformedURLException e) {
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }


    public Validator verifyCaptcha()
    {
        return ReCaptchaVerifier.verify(df.get("g-recaptcha-response"))
                ? new FormSuccess()
                : new FormError("Le captcha est incorrect !");
    }

    public boolean isValidRegex(String regex, String field)
    {
        Pattern p = Pattern.compile(regex);
        return notNull(field) && p.pattern().matches(df.get(field));
    }

    public boolean isValidazAZ(String field)
    {
        return notNull(field) && isValidRegex("[a-zA-Z]", df.get(field));
    }

    public boolean isValidMaxLenght(int max, String field)
    {
        return notNull(field) && df.get(field).length() <= max;
    }

    public boolean isValidMinbLenght(int min, String field)
    {
        return notNull(field) && df.get(field).length() >= min;
    }

    public boolean notNull(String field)
    {
        return df.get(field) != null;
    }
}
