package models;

import form.FormApplier;
import form.FormChecker;
import mongo.Model;
import org.bson.types.ObjectId;
import play.data.DynamicForm;
import play.mvc.Controller;
import util.CryptUtil;
import util.MailSender;
import util.PathTranslator;


/**
 * Par Alexis le 05/11/2016.
 */
public class User extends Model<User>
{

    public static final User I = new User();

    private ObjectId _id;

    public String email;
    public String newEmail = "";
    public String firstName;
    public String lastName;
    private String pwd;

    /*
    0 = no verified
    1 = lamda user
    2 = moderator user
    3 = admin user
     */
    public int permission = 0;

    private String avatarLoc = "";
    private String coverLoc = "";
    public String jobTitle = "";
    public String website = "";
    public String paypalEmail = "";
    public String facebookURL = "";
    public String twitterURL = "";
    public String linkedinURL = "";

    public Long firstConnection;


    private User()
    {
        super(User.class, "users");
    }

    public User(String email, String firstName, String lastName, String password)
    {
        super(User.class, "users");
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firstConnection = System.currentTimeMillis();
        this.pwd = CryptUtil.generateSHA256(password + firstConnection);
        this.permission = 0;
        this._id = new ObjectId();
    }

    @Override
    public User setModel()
    {
        return this;
    }

    @Override
    public void buildJson()
    {
        put("email", email);
        put("firstname", firstName);
        put("lastName", lastName);
        put("firstConnection", firstConnection);
    }

    @Override
    public ObjectId getId()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return getFormattedObject();
    }

    public String getPath(String category)
    {
        return "public/upload/" + category + "/" + getId().toString() + ".";
    }

    public boolean samePwd(String s)
    {
        return pwd.equals(CryptUtil.generateSHA256(s + firstConnection));
    }

    public void sendMail(String subject, String content)
    {
        MailSender mailSender = new MailSender(email, subject, content);
        mailSender.sendEmail();
    }

    public void mailOnRegister()
    {
        CodeVerification c = new CodeVerification(CodeVerification.Action.EMAIL_VERIFICATION, this);
        c.setSuccessMessage("Vous avez validé votre code, vous pouvez maintenant vous connecter à votre compte !");
        sendMail("Confirmer votre email",
                 "<h3>Bienvenue chez JobID !<h3>" +
                 "<br>Pour te connecter tu dois confirmer ton adresse email." +
                 "Veuillez confirmer votre compte en allant sur ce lien : <a href=\"http://localhost:81/validate/code\">ici</a><br>" +
                 "Votre code de confirmation d'email est le : <br><strong>" + c.code  + "</strong>"
        );
    }

    public void mailOnChange()
    {
        CodeVerification c = new CodeVerification(CodeVerification.Action.EMAIL_VERIFICATION, this);
        c.setSuccessMessage("Vous pouvez maintenant vous connecter avec votre nouvelle adresse !");
        sendMail("Confirmer votre email",
                 "Pour utiliser votre nouvelle adresse email confirmez votre code <a href=\"http://localhost:81/validate/code\">ici</a><br>" +
                 "Votre code est : <br><strong>" + c.code  + "</strong>"
        );
    }

    public void setPassword(String password)
    {
        this.pwd = CryptUtil.generateSHA256(password + firstConnection);
    }

    public String getAvatarLoc()
    {
        return avatarLoc;
    }

    public void setAvatarLoc(String avatarLoc)
    {
        this.avatarLoc = PathTranslator.getPathForAsset(avatarLoc);
    }

    public String getCoverLoc()
    {
        return PathTranslator.getPathForAsset(coverLoc);
    }

    public void setCoverLoc(String coverLoc)
    {
        this.coverLoc = coverLoc;
    }

    /********************
     STATIC METHODS 
     ********************/

    public static User getByMail(String mail)
    {
        return User.I.get("email", mail);
    }

    public static User getById(String id)
    {
        return User.I.getById(new ObjectId(id));
    }

    private static User getByOldEmail(String email)
    {
        return User.I.get("oldEmail", email);
    }


    public static class DelegatedUserForm extends FormApplier
    {
        public DelegatedUserForm(DynamicForm d)
        {
            super(d);
        }

        public FormChecker registerForm()
        {
            User u = User.getByMail(v.get("email"));
            return new FormChecker(
                    () -> v.validate("email", "password", "repPassword", "firstName", "lastName"),
                    () -> v.isValidEmail("email"),
                    () -> u == null
                            ? success
                            : error.message("L'email est déjà associé a un compte."),
                    () -> v.isValidMinbLenght(5, "password")
                            ? success
                            : error.message("Le mot de passe doit contenir au moins 5 caractères."),
                    () -> v.get("password").equals(v.get("repPassword"))
                            ? success
                            : error.message("Les mots de passes ne sont pas identiques.")
            );
        }

        public FormChecker loginForm()
        {
            User u = User.getByMail(v.get("email"));

            return new FormChecker(
                    () -> v.validate("email", "password", "login"),
                    () -> v.isValidEmail("email"),
                    () -> u == null
                            ? error.message("Adresse email non reconnu.")
                            : success,
                    () -> u != null && u.permission > 0
                            ? success
                            : error.message("L'adresse email n'a pas été vérifié !"),
                    () -> u != null && !u.samePwd(v.get("password"))
                            ? error.message("Combinaison mot de passe/email incorrect")
                            : success.message("Vous êtes connecté"),
                    () -> v.verifyCaptcha()
            );
        }

        public FormChecker changePassword()
        {
            User u = User.getByMail(Controller.session("email"));
            return new FormChecker(
                    () -> v.validate("nowPassword", "newPassword", "repNewPassword"),
                    () -> u.samePwd(v.get("nowPassword"))
                            ? success
                            : error.message("Votre mot de passe actuel ne correspond pas."),
                    () -> v.isValidMinbLenght(5, "newPassword")
                            ? success
                            : error.message("Le nouveau mot de passe doit contenir au moins 5 caractères."),
                    () -> v.get("newPassword").equals(v.get("repNewPassword"))
                            ? success
                            : error.message("Les mots de passes ne sont pas identiques.")

            ).messageSuccess("Vous avez changé votre mot de passe");
        }
    }




}
