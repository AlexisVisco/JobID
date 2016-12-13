package controllers;

import play.mvc.Result;
import views.html.*;

/**
 * Par Alexis le 09/12/2016.
 */

public class CodeController extends Controller
{

    public Result index()
    {
        return ok(code.render());
    }

}
