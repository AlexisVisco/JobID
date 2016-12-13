package mongo;


import com.mongodb.*;
import java.util.Arrays;

import org.jongo.MongoCollection;
import play.Configuration;
import play.Play;
import uk.co.panaxiom.playjongo.MongoClientFactory;
import uk.co.panaxiom.playjongo.PlayJongo;

public class MongoDB extends MongoClientFactory
{
    private static PlayJongo jongo;

    public MongoDB(Configuration config)
    {
        super(config);
    }

    public MongoClient createClient() throws Exception {
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(100)
                .maxConnectionIdleTime(60000)
                .build();

        return new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)),
                options);
    }

    public String getDBName() {
        String string = config.getString("mongo.dbname");
        return string;
    }

    public static MongoCollection getColl(String coll)
    {
        if(jongo == null)
            jongo = Play.application().injector().instanceOf(PlayJongo.class);
        return jongo.getCollection(coll);
    }

}
