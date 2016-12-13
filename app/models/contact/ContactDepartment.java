package models.contact;

import form.FormApplier;
import form.FormChecker;
import mongo.Model;
import org.bson.types.ObjectId;
import form.FormValidator;
import play.data.DynamicForm;

/**
 * Par Alexis le 06/11/2016.
 */

public class ContactDepartment extends Model<ContactDepartment>
{

    public final static ContactDepartment I = new ContactDepartment();

    ObjectId _id;
    public String department;

    public ContactDepartment()
    {
        super(ContactDepartment.class, "supportDepartment");
    }

    public ContactDepartment(String department)
    {
        super(ContactDepartment.class, "supportDepartment");
        this.department = department;
    }

    @Override
    public ContactDepartment setModel()
    {
        return this;
    }

    @Override
    public void buildJson()
    {
        put("department", department);
    }

    @Override
    public ObjectId getId()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return getFormattedObject();
    }

    public static class DelegatedDepartmentForm extends FormApplier
    {
        public DelegatedDepartmentForm(DynamicForm d)
        {
            super(d);
        }

        public FormChecker departmentForm()
        {
            return new FormChecker(
                    () -> v.validate("department"),
                    () -> I.get("department", v.get("department")) == null
                            ? success
                            : error.message("Le département existe déjà."),
                    () -> v.isValidMinbLenght(3, "department")
                            ? success
                            : error.message("Le département doit faire minimum 3 caractères")
            );
        }
    }
}
