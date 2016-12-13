package mongo;


import org.bson.types.ObjectId;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Par Alexis le 02/11/2016.
 *
 */
public interface IMongoModel<T>
{

    ObjectId getId();
}
