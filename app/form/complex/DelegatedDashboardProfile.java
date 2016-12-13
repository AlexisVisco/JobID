package form.complex;

import form.FormApplier;
import form.FormChecker;
import form.typevalidator.FormError;
import form.typevalidator.FormSkip;
import form.typevalidator.FormSuccess;
import form.typevalidator.Validator;
import form.FormValidator;
import javaslang.control.Try;
import models.CodeVerification;
import static models.CodeVerification.Action.*;
import models.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Http;
import util.CodeGenerator;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * Par Alexis le 27/11/2016.
 */

public class DelegatedDashboardProfile extends FormApplier
{
    private FormValidator f;
    private User user;
    private Http.MultipartFormData<File> body;

    public DelegatedDashboardProfile(DynamicForm form, User who, Http.MultipartFormData<File> body)
    {
        super(form);
        this.f = new FormValidator(form);
        this.user = who;
        this.body = body;
    }


    public FormChecker profileForm()
    {
        return new FormChecker(
                this::uploadAvatar,
                this::changePaypalEmail,
                this::changeEmail,
                this::changeJob,
                this::changeFacebook,
                this::changeLinkedin,
                this::changeTwitter,
                this::changeLinkedin
        ).messageSuccess("Le profil a été actualisé");
    }

    private Validator changeJob()
    {
        String jobTitle = "jobTitle";
        if(!f.isSet(jobTitle))
            return new FormSkip();
        String value = f.get(jobTitle);
        if(!f.isValidMinbLenght(3, value))
            return error.message("Le job doit contenir minimum 3 caractères.");
        if(!f.isValidazAZ(jobTitle))
            return error.message("Le métier ne peut pas comporter que des lettres.");
        return new FormSuccess("Votre métier a été actualisé.");
    }

    private Validator changeFacebook()
    {
        Validator v = websiteChecker("facebookURL", (a) -> a.contains("facebook"));
        if(v.isError())
            return v.message("L'url facebook est invalide");
        user.facebookURL = f.get("facebookURL");
        user.update();
        return success.message("L'url facebook a été changé");
    }

    private Validator changeTwitter()
    {
        Validator v = websiteChecker("twitterURL", (a) -> a.contains("twitter"));
        if(v.isError())
            return v.message("L'url twitter est invalide");
        user.twitterURL = f.get("twitterURL");
        user.update();
        return success.message("L'url twitter a été changé");
    }

    private Validator changeLinkedin()
    {
        Validator v = websiteChecker("linkedinURL", (a) -> a.contains("linkedin"));
        if(v.isError())
            return v.message("L'url linkedin est invalide");
        user.linkedinURL = f.get("linkedinURL");
        user.update();
        return success.message("L'url linkedin a été changé");
    }

    private Validator websiteChecker(String name, Predicate<String>... filter)
    {
        if(!f.isSet(name))
            return new FormSkip();
        if(!f.isValidURL(f.get(name)))
            return error.message("Votre site n'est pas valide.");
        for (Predicate<String> predicate : filter) {
            if(!predicate.test(f.get(name)))
                return error.message("Votre site n'est pas valide.");
        }
        return new FormSuccess("Vous site a été actualisé.");
    }

    private Validator changeEmail()
    {
        String email = "paypalEmail";
        if(!f.isSet(email))
            return new FormSkip();
        if(Validator.isError(f.isValidEmail(email)))
            return f.isValidEmail(email);
        if(user.email.equalsIgnoreCase(f.get(email)))
            return error.message("Ta ouvelle adresse email ne doit pas être la même que l'actuel.");
        user.newEmail = f.get(email);
        user.mailOnChange();
        user.update();
        return new FormSuccess("Un email a votre nouvelle adresse vous a été envoyé !");
    }

    private Validator changePaypalEmail()
    {
        String email = "paypalEmail";
        if(!f.isSet(email))
            return new FormSkip();
        if(Validator.isError(f.isValidEmail(email)))
            return f.isValidEmail(email).message("L'adresse email paypal est incorrect.");
        user.paypalEmail = email;
        user.update();
        return new FormSuccess("Vous avez actualisé votre adresse paypal !");
    }

    private Validator uploadAvatar()
    {
        FormError error = new FormError();

        if(body == null)
            return new FormSkip();

        Http.MultipartFormData.FilePart<File> file = body.getFile("avatar");
        if(file == null)
            return error.message("Le fichier est vide ou n'existe pas.");

        String filename = file.getFilename();
        final File f = file.getFile();
        Try.of(f::createNewFile);

        if(!file.getContentType().contains("image") || !fileIsPicture(f))
            return error.message("Le fichier n'est pas une image !");

        try {
            deleteOtherAvatars();
            FileCopyUtils.copy(f, getFileUser("avatars", filename));
        } catch (IOException e) {
            return error.message("Erreur lors du téléchargement.");
        }
        Logger.debug("" + getFileUser("avatars", filename).getTotalSpace());
        user.setAvatarLoc(getFileUser("avatars", filename).getPath());
        user.update();
        return new FormSuccess("Votre avatar va être actualisé !");
    }

    private File getFileUser(String cat, String fileName)
    {
        return getFileWithExtension(cat, FilenameUtils.getExtension(fileName));
    }

    private File getFileWithExtension(String cat, String extension)
    {
        return new File(user.getPath(cat)  + extension);
    }

    private void deleteOtherAvatars()
    {
        Try.of(getFileWithExtension("avatar", ".jpg")::delete)
           .andThen(getFileWithExtension("avatar", ".png")::delete);
    }

    private boolean fileIsPicture(final File f)
    {
        return Try.of(() -> ImageIO.read(f)).get() != null;
    }


}
