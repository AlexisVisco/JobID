package form.typevalidator;

/**
 * Par Alexis le 29/11/2016.
 */

public class Validator
{

    public String value = "";

    Validator(String message)
    {
        this.value = message;
    }

    Validator()
    {
        //define in value(str)
    }

    public Validator message(String message)
    {
        this.value = message;
        return this;
    }

    public boolean isError()
    {
        return isError(this);
    }

    public static boolean isError(Validator t)
    {
        return (t instanceof FormError);
    }

    public static boolean isSuccess(Validator t)
    {
        return (t instanceof FormSuccess);
    }

}