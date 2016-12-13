package mongo;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.javafx.sg.prism.NGShape;
import org.bson.types.ObjectId;
import org.jongo.FindOne;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Par Alexis le 03/11/2016.
 */

public abstract class Model<T extends Model> implements IMongoModel
{

    private transient Class<? extends T> modelClass;
    private transient String collectionName;
    private transient T model;
    private transient JSONObject jo = new JSONObject();

    public Model(Class<? extends T> childClass, String collectionName)
    {
        this.modelClass = childClass;
        this.collectionName = collectionName;
        this.model = setModel();
    }

    /*
    Set model from child class
     */
    public abstract T setModel();

    /*
    Build json with custom put() method
     */
    public abstract void buildJson();

    /*
    Insert the doc from the child
     */
    public void insert()
    {
        MongoDB.getColl(collectionName).insert(model);
    }

    /*
    Remove the doc from the child
     */
    public void remove()
    {
        MongoDB.getColl(collectionName).remove(model.getId());
    }

    /*
    Update any values from the child
     */
    public void update()
    {
        MongoDB.getColl(collectionName).update(model.getId()).with(model);
    }

    /*
    Get all members of the mongodb collection #collectionName
     */
    public Collection<T> getAll()
    {
        List<T> l = new ArrayList<>();
        MongoCursor<? extends T> as = getColl().find().as(modelClass);
        as.forEach(l::add);
        return l;
    }

    /*
    Get an element from a child instance
     */
    public T get(String field, String value)
    {
        FindOne one = MongoDB.getColl(collectionName).findOne("{" + field + ": #}", value);
        if(one == null)
            return null;
        return (one.as(modelClass));
    }

    /*
    Get an element from a child instance
     */
    public T getById(ObjectId value)
    {
        FindOne one = MongoDB.getColl(collectionName).findOne(value);
        if(one == null)
            return null;
        return (one.as(modelClass));
    }

    /*
    Get mongo collection from super mongo class
     */
    private MongoCollection getColl()
    {
        return MongoDB.getColl(collectionName);
    }

    /*
    Count all data in the collection {#collectionName}
     */
    public long count()
    {
        return getColl().count();
    }

    protected void put(String s, Object o)
    {
        try
        {
            jo.put(s, o);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /*
    Return formatted json object into string format
     */
    public String getFormattedObject()
    {
        this.buildJson();
        JsonElement je = new JsonParser().parse(jo.toString());
        return je.toString();
    }


    public JSONObject getObject()
    {
        this.buildJson();
        return jo;
    }


    public static <T, O> T get(Class<? extends T> model, String field, O value, String collection)
    {
        FindOne one = MongoDB.getColl(collection).findOne("{" + field + ": #}", value.toString());
        if(one == null)
            return null;
        return one.as(model);
    }
}
