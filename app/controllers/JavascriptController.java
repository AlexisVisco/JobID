package controllers;

import form.FormChecker;
import models.CodeVerification;
import models.User;
import play.Logger;
import play.mvc.Result;
import util.Http;

/**
 * Par Alexis le 09/12/2016.
 */

public class JavascriptController extends Controller
{


    @Http.Post
    public Result codeVerification()
    {
        initForm();
        final FormChecker checker = new CodeVerification.CodeVerificationFormDelegated(d).verificationCode();
        checker.caseSuccess("code", (a) -> {
            String code = d.get("code");
            final CodeVerification byCode = CodeVerification.getByCode(code);
            final User byId = User.getById(byCode.user);
            checker.messageSuccess(byCode.successMessage);
            if(byCode.action.equals(CodeVerification.Action.EMAIL_VERIFICATION.name()))
            {
                Logger.debug("Ok");
                new UserController().confirmUser(byId);
            }
            byCode.remove();
        });
        return ok(checker.getResponseToJson());
    }

}
