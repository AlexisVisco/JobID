package controllers;

import form.FormChecker;
import form.FormValidator;
import form.complex.DelegatedDashboardProfile;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import util.Http;

import java.io.File;

/**
 * Par Alexis le 27/11/2016.
 */

public class DashboardController extends Controller
{


    public DashboardController()
    {}

    @Http.Get(desc = "Get the dashboard profile")
    public Result profile()
    {
        return whenConnected((e) -> views.html.dashboard.profile.render());
    }

    @Http.Get(desc = "Get the dashboard change password")
    public Result changePassword()
    {
        return whenConnected((e) -> views.html.dashboard.changePassword.render());
    }

    @Http.Post
    public Result profilePost()
    {
        play.mvc.Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        initForm();

        final DynamicForm form = super.form.form().bindFromRequest();
        FormChecker check = new DelegatedDashboardProfile(form, User.getByMail(session("email")), body).profileForm();
        check.apply("dashboard", (a) -> putSuccess(a, check.finalSuccessMessage));
        placeField();
        return profile();
    }

    @Http.Post
    public Result passwordPost()
    {
        User user = User.getByMail(session("email"));

        DynamicForm d = form.form().bindFromRequest();
        FormChecker check = new User.DelegatedUserForm(d).changePassword();
        check.apply("changePassword", (a) -> {
            user.setPassword(d.get("newPassword"));
            user.update();
            putSuccess(a, check.finalSuccessMessage);
        });
        return changePassword();
    }

}
