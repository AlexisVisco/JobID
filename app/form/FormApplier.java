package form;


import form.typevalidator.FormError;
import form.typevalidator.FormSkip;
import form.typevalidator.FormSuccess;
import play.data.DynamicForm;

/**
 * Par Alexis le 29/11/2016.
 */
public class FormApplier
{

    public FormError error = new FormError();
    public FormSuccess success = new FormSuccess();
    public FormSkip skip = new FormSkip();

    public DynamicForm d;
    public FormValidator v;

    public FormApplier(DynamicForm d)
    {
        this.d = d;
        this.v = new FormValidator(d);
    }
}
