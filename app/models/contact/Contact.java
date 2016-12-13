package models.contact;

import form.FormApplier;
import form.FormChecker;
import mongo.Model;
import org.bson.types.ObjectId;
import form.FormValidator;
import play.data.DynamicForm;

/**
 * Par Alexis le 15/11/2016.
 */

public class Contact extends Model<Contact>
{
    public static final String COLLECTION = "support";
    public static final Contact I = new Contact();

    ObjectId _id;

    public long id;
    public String lastName;
    public String email;
    public String subject;
    public String website;
    public String content;
    public ContactDepartment department;

    public Contact()
    {
        super(Contact.class, COLLECTION);
    }

    public Contact(String lastName, String email, String content,
                   String subject, String website, String department)
    {
        this();
        this.lastName = lastName;
        this.email = email;
        this.content = content;
        this.subject = subject;
        this.website = website;
        this.id = count()+1;
        this.department = new ContactDepartment().get("department", department);
    }

    @Override
    public Contact setModel()
    {
        return this;
    }

    @Override
    public void buildJson()
    {
        put("lastName", lastName);
        put("email", email);
        put("subject", subject);
        put("website", website);
        put("content", content);
        put("department", department.department);
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

    public static class DelegatedContactForm extends FormApplier
    {
        public DelegatedContactForm(DynamicForm d)
        {
            super(d);
        }

        public FormChecker contactForm()
        {
            return new FormChecker(
                    () -> v.validate("lastName", "content", "email", "subject", "website", "department"),
                    () -> v.isValidEmail("email"),
                    () -> v.isValidMinbLenght(5, "subject")
                            ? success
                            : error.message("Le sujet doit avoir une longueur minimum de 5 caractères."),
                    () -> v.isValidMinbLenght(30, "content")
                            ? success
                            : error.message("Le contenu du rapport doit faire minimum 30 caractères."),
                    () -> v.isValidMinbLenght(3, "lastName")
                            ? success
                            : error.message("Votre nom doit avoir une longueur minimum de 5 caractères."),
                    () -> new ContactDepartment().get("department", v.get("department")) != null
                            ? success
                            : error.message("Le département n'éxiste pas.")
            );
        }
    }
}
