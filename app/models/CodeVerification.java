package models;

import form.FormApplier;
import form.FormChecker;
import mongo.Model;
import org.bson.types.Code;
import org.bson.types.ObjectId;
import play.Logger;
import play.data.DynamicForm;
import util.CodeGenerator;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Par Alexis le 09/12/2016.
 */

public class CodeVerification extends Model
{
    public static final CodeVerification I = new CodeVerification();

    ObjectId _id;

    public String action;
    public String code;
    public String user;
    public String successMessage = "";

    public CodeVerification()
    {
        super(CodeVerification.class, "codeVerification");
    }

    public CodeVerification(Action action, User user)
    {
        this();
        this.action = action.name();
        this.code = new CodeGenerator(8).generate();
        this.user = user.getId().toString();
        insert();
    }

    @Override
    public ObjectId getId()
    {
        return _id;
    }

    @Override
    public Model setModel()
    {
        return this;
    }

    @Override
    public void buildJson()
    {
        put("action", action);
        put("code", code);
        put("user", user);
    }

    public CodeVerification setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
        update();
        return this;
    }

    public enum Action
    {
        EMAIL_VERIFICATION,
        EMAIL_CHANGE,
        PAYPAL_VERIFICATION;

        @Nullable
        static Action getByName(String name)
        {
            for (Action action : Action.values()) {
                if (action.name().equals(name))
                    return action;
            }
            return null;
        }
    }

    public static CodeVerification getByCode(String code)
    {
        return (CodeVerification) I.get("code", code);
    }

    public static class CodeVerificationFormDelegated extends FormApplier
    {

        public CodeVerificationFormDelegated(DynamicForm d)
        {
            super(d);
        }

        public FormChecker verificationCode()
        {
            CodeVerification code = getByCode(d.get("code"));
            return new FormChecker(
                    () -> v.validate("code"),
                    () -> code != null
                            ? success
                            : error.message("Le code est invalide !"),
                    () -> User.getById(code.user) != null
                            ? success
                            : error.message("L'utilisateur du code est invalide.")

            );
        }
    }


}
