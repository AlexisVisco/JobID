package controllers;

import form.FormChecker;
import models.CodeVerification;
import models.User;
import play.Logger;
import play.mvc.Result;
import util.Http;
import views.html.*;


/**
 * Par Alexis le 06/11/2016.
 */

public class UserController extends Controller
{


    public UserController()
    {
    }

    @Http.Get
    public Result logout()
    {
        session().put("email", "");
        return connectPage();
    }

    @Http.Get
    public Result connectPage()
    {
        if (!isConnected())
            return ok(connect.render());
        else
            return new DashboardController().profile();
    }

    @Http.Get
    public void confirmUser(User u)
    {
        u.permission++;
        u.update();
    }

    @Http.Post
    public Result register()
    {
        initForm();
        FormChecker checker = new User.DelegatedUserForm(d).registerForm();
        checker.apply("register",
                (a) -> {
                    User user = new User(d.get("email"), d.get("firstName"), d.get("lastName"), d.get("password"));
                    user.mailOnRegister(CodeVerification.Action.EMAIL_VERIFICATION);
                    user.insert();
                    putSuccess("register", "Un email de confirmation vous a été envoyé !");
                });
        Logger.debug("errorRPOST ? " + flash("e-register"));
        return connectPage();

    }

    @Http.Post
    public Result login()
    {
        initForm();
        FormChecker checker = new User.DelegatedUserForm(d).loginForm();
        boolean apply = checker.apply("login", (a) -> session("email", d.get("email")));
        if (apply)
            return new DashboardController().profile();
        else
            return connectPage();

    }
}

    


