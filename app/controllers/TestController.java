package controllers;

import com.google.inject.Inject;
import models.Chat;
import models.User;
import org.apache.commons.lang3.StringEscapeUtils;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.Http;
import views.html.*;


/**
 * Par Alexis le 01/12/2016.
 */

public class TestController extends Controller
{

    private FormFactory f;

    @Inject
    public TestController(final FormFactory f)
    {
        this.f = f;
    }

    public Result index()
    {
        if(session("email") == null || session("email").isEmpty())
            return new UserController().connectPage();
        return ok(test.render());
    }

    @Http.Post
    public Result addMessage()
    {
        String mess = "Une erreur est survenu lors du traitement";
        if(session("email") == null || session("email").isEmpty())
            return new UserController().connectPage();
        DynamicForm dynamicForm = f.form().bindFromRequest();
        User u = User.getByMail(session("email"));

        String message = dynamicForm.get("message");
        Logger.debug(message);
        if(message.isEmpty())
            return ok(mess);

        Chat.addMessage(u, message);
        return chatMessages();
    }

    public Result chatMessages()
    {
       return ok(Chat.get().toString());
    }



}
