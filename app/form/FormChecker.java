package form;

import form.typevalidator.FormError;
import form.typevalidator.FormSkip;
import form.typevalidator.FormSuccess;
import form.typevalidator.Validator;
import org.json.JSONException;
import org.json.JSONObject;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import play.twirl.api.Html;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Par Alexis le 29/11/2016.
 */

public class FormChecker
{

    private Set<FormError> errors = new HashSet<>();
    private Set<FormSuccess> success = new HashSet<>();
    private Set<FormSkip> passeds = new HashSet<>();
    public String finalSuccessMessage = "";

    private Supplier<Validator>[] toCheck;

    @SafeVarargs
    public FormChecker(Supplier<Validator>... toCheck)
    {
        this.toCheck = toCheck;
    }

    public void reset()
    {
        this.errors = new HashSet<>();
        this.success = new HashSet<>();
        this.passeds = new HashSet<>();
    }

    private FormChecker check()
    {
        reset();
        for (Supplier<Validator> toValidate : toCheck) {
            try {
                Validator t = toValidate.get();

                if (t == null)
                    passeds.add(new FormSkip());
                if (t instanceof FormError)
                    errors.add((FormError) t);
                else if (t instanceof FormSuccess)
                    success.add((FormSuccess) t);
                else if (t instanceof FormSkip)
                    passeds.add((FormSkip) t);

            } catch (Exception ignored) {}
        }
        return this;
    }

    public FormChecker messageSuccess(String s)
    {
        finalSuccessMessage = s;
        return this;
    }

    public boolean hasError()
    {
        check();
        return !errors.isEmpty();
    }

    public boolean apply(String key, Consumer<String> actionWithFlashKey)
    {
        if (hasError())
            putError(key);
        else
            actionWithFlashKey.accept(key);
        return !hasError();
    }

    public boolean caseSuccess(String key, Consumer<String> actionWithFlashKey)
    {
        if(!hasError())
        {
            actionWithFlashKey.accept(key);
            return true;
        }
        return false;
    }

    public String getResponseToJson()
    {
        JSONObject j = new JSONObject();
        try {
            j.put("success", !hasError());
            j.put("message", getMessage());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.debug(j.toString());
        return j.toString();
    }

    public FormChecker putError(String key)
    {
        controllers.Controller.putError(key, getMessage());
        return this;
    }

    public Result render(Html html)
    {
        return Controller.ok(html);
    }

    public Result render(Result result)
    {
        return result;
    }

    String getMessage()
    {
        StringBuilder s = new StringBuilder();
        if(hasError() || !errors.isEmpty())
            errors.forEach(e -> s.append("• ").append(e.value).append("{n}"));
        else
            s.append(finalSuccessMessage);
        Logger.debug("-> " + finalSuccessMessage);
        return s.toString();
    }


}
