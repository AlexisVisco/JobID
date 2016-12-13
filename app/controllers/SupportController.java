package controllers;

import com.google.inject.Inject;
import form.FormChecker;
import models.contact.Contact;
import models.contact.ContactDepartment;
import org.json.JSONArray;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Result;
import util.Http;
import views.html.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Par Alexis le 06/11/2016.
 */

public class SupportController extends Controller
{

    public SupportController()
    {
    }

    @Http.Get
    public Result index()
    {
        return ok(contact.render(getDepartments()));
    }

    @Http.Get
    public Result department()
    {
        return ok(department.render(getDepartments()));
    }

    @Http.Get
    public Result contactList()
    {
        Collection<Contact> all = Contact.I.getAll();
        JSONArray ja = new JSONArray();
        StringBuilder sb = new StringBuilder();
        all.forEach(e -> ja.put(e.getObject()));
        return ok(ja.toString());
    }

    @Http.Post
    public Result postDepartment()
    {
        initForm();
        FormChecker errors = new ContactDepartment.DelegatedDepartmentForm(d).departmentForm();
        if(errors.hasError())
            return errors.putError("department").render(department());

        ContactDepartment department = new ContactDepartment(d.get("department"));
        department.insert();
        return department();
    }

    @Http.Post
    public Result postContact()
    {
        initForm();
        FormChecker errors = new Contact.DelegatedContactForm(d).contactForm();
        if(errors.hasError())
            return errors.putError("contact").render(contactList());
        Contact support = new Contact
        (d.get("lastName"), d.get("email"), d.get("content"), d.get("subject"), d.get("website"), d.get("department"));
        support.insert();
        return contactList();
    }

    private List<ContactDepartment> getDepartments()
    {
        List<ContactDepartment> list = new ArrayList<>();
        new ContactDepartment().getAll().forEach(list::add);
        return list;
    }

}
