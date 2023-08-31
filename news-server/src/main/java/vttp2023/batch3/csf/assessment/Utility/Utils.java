package vttp2023.batch3.csf.assessment.Utility;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;

public class Utils {


    public static JsonObject readAngularOutput (String json){
        JsonReader jr = Json.createReader(new StringReader(json));
        return jr.readObject();
    }

    public static News jsonToNews (JsonObject jo){
        News n = new News();
        n.setTitle(jo.getString("title"));
        n.setDescription(jo.getString("description"));
        n.setPostDate(jo.getJsonNumber("postDate").longValue());
        JsonArray ja = jo.getJsonArray("tags");
        n.setImage(jo.getString("imageUrl"));
        n.setTags(ja.stream().map(j -> j.toString()).collect(Collectors.toList()));
        return n;
    }


    
    public static JsonObject newsToJson (News n){
        JsonArrayBuilder jab = Json.createArrayBuilder(n.getTags());
        return Json.createObjectBuilder()
                    .add("postDate", n.getPostDate())
                    .add("title", n.getTitle())
                    .add("description", n.getDescription())
                    .add("image", n.getImage())
                    .add("tags", jab.build())
                    .build();
    }

    public static JsonObject tagCountToJson (TagCount tc){
        return Json.createObjectBuilder()
                    .add("tag", tc.tag())
                    .add("count", tc.count())
                    .build();
    }
}
