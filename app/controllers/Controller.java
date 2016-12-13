package controllers;

import com.google.inject.Inject;
import com.nappin.play.recaptcha.RecaptchaVerifier;
import form.FormValidator;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Result;
import play.twirl.api.Content;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

/**
 * Par Alexis le 04/12/2016.
 */

public class Controller extends play.mvc.Controller
{

    @Inject
    public FormFactory form;

    protected DynamicForm d;
    protected FormValidator fv;

    boolean isConnected()
    {
        return session("email") != null && !session("email").isEmpty();
    }

    void initForm()
    {
        if(form != null) {
            this.d = form.form().bindFromRequest();
            this.fv = new FormValidator(d);

        }
    }

    void placeField()
    {
        if(d == null)
            return;
        Map<String, String> o = d.data();

        o.forEach((k, v) ->
        {
            if (v != null && !v.isEmpty())
                flash(k, v);
        });
    }

    Result whenConnected(Function<Void, Content> c)
    {
        if (isConnected())
            return ok(c.apply(null));
        else
            return new UserController().connectPage();

    }

    Result whenConnectedR(Function<Void, Result> c)
    {
        if (isConnected())
            return c.apply(null);
        else
            return new UserController().connectPage();

    }

    public static void putError(String errorName, String content)
    {
        flash("e-" + errorName, content);
    }

    public static void putSuccess(String successName, String content)
    {
        flash("s-" + successName, content);
    }

    public static void putInfo(String infoName, String content)
    {
        flash("s-" + infoName, content);
    }
}
