package models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Par Alexis le 03/12/2016.
 */

public class Chat
{

    public static List<Message> messages = new ArrayList<>();

    public static void addMessage(User author, String content)
    {
        messages.add(new Message(author, content));
    }

    public static JSONArray get()
    {
        JSONArray j = new JSONArray();
        messages.forEach(e -> j.put(e.toJson()));
        return j;
    }



    public static class Message {

        static int id_ = 0;

        String author;
        String imageLoc;
        String content;
        String date;
        int id;

        Message(User author, String content)
        {
            this.author = author.firstName;
            this.imageLoc = author.getAvatarLoc();
            this.content = content;
            this.date = new SimpleDateFormat("hh:mm:ss").format(new Date());
            this.id = id_;
            id_++;
        }

        public JSONObject toJson()
        {
            JSONObject j = new JSONObject();
            try {
                j.put("author", author)
                 .put("content", content)
                 .put("imageLoc", imageLoc)
                 .put("id", id)
                 .put("date", date);
            } catch (JSONException e) {
                return new JSONObject();
            }
            return j;
        }

    }

}
