package fr.labri.docclone.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.labri.docclone.colibri.lib.Relation;
import fr.labri.docclone.models.FcaElement;
import fr.labri.docclone.models.Method;
import fr.labri.docclone.models.Tag;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by oumaziz on 02/03/2016.
 */
public class ContextBuilder {

    private JsonObject json;
    private Set<FcaElement> elements;

    public ContextBuilder(JsonObject json){
        this.json = json;
    }

    public void generateFromMethods(Relation rel){
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();

        for(Map.Entry<String, JsonElement> entry : entrySet) {

            JsonArray methods = json.getAsJsonObject(entry.getKey()).getAsJsonArray("methods");

            for (int i = 0; i < methods.size(); i++) {
                JsonArray m = methods.get(i).getAsJsonArray();
                this.elements = new HashSet<>();

                FcaElement fcaMethod = new FcaElement(new Method(entry.getKey(), m.get(0).toString()));

                JsonObject tags = m.get(1).getAsJsonObject();
                FcaElement fcaTag = null;

                if(tags.get("main").toString().length() > 2){
                    fcaTag = new FcaElement(new Tag("@description", tags.get("main").toString()));
                    elements.add(fcaTag);
                }

                if(tags.get("return").toString().length() > 2) {
                    fcaTag = new FcaElement(new Tag("@return", tags.get("return").toString()));
                    elements.add(fcaTag);
                }

                JsonObject params = tags.get("params").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> paramsSet = params.entrySet();

                for(Map.Entry<String, JsonElement> param : paramsSet)  {
                    fcaTag = new FcaElement(new Tag("@param", param.getKey() +
                            params.get(param.getKey()).getAsJsonArray().get(0).toString()));
                    elements.add(fcaTag);
                }

                JsonObject cthrows = tags.get("throws").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> throwsSet = cthrows.entrySet();

                for(Map.Entry<String, JsonElement> cthrow : throwsSet)  {
                    fcaTag = new FcaElement(new Tag("@throws", cthrow.getKey() +
                            cthrows.get(cthrow.getKey()).toString()));
                    elements.add(fcaTag);
                }

                JsonObject exceptions = tags.get("exceptions").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> exSet = exceptions.entrySet();

                for(Map.Entry<String, JsonElement> ex: exSet)  {
                    fcaTag = new FcaElement(new Tag("@throws", ex.getKey() +
                            exceptions.get(ex.getKey()).toString()));
                    elements.add(fcaTag);
                }

                JsonArray see = tags.get("see").getAsJsonArray();

                for (int j = 0; j < see.size(); j++) {
                    if(see.get(j).toString() != null) {
                        fcaTag = new FcaElement(new Tag("@see", see.get(j).toString()));
                        elements.add(fcaTag);
                    }
                }

                for (FcaElement s : elements){
                    rel.add(fcaMethod, s);
                }
            }
        }
    }


    public void generateFromClasses(Relation rel){
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();

        for(Map.Entry<String, JsonElement> entry : entrySet) {
            this.elements = new HashSet<>();

            JsonObject clazz = json.getAsJsonObject(entry.getKey()).getAsJsonObject("class");
            FcaElement fcaClass = new FcaElement(new Tag("class", entry.getKey()));
            FcaElement fcaTag = null;

            if(clazz.get("main").toString().length() > 2){
                fcaTag = new FcaElement(new Tag("@description", clazz.get("main").toString()));
                elements.add(fcaTag);
            }

            Set<Map.Entry<String, JsonElement>> tagSet = clazz.getAsJsonObject("tags").entrySet();

            for(Map.Entry<String, JsonElement> tags : tagSet) {
                for(JsonElement tag : tags.getValue().getAsJsonArray()){
                    String key = tags.getKey().toLowerCase().trim();
                    if(key.contains("throws") || key.contains("exception")
                            || key.contains("param") || key.contains("return")){
                        fcaTag = new FcaElement(new Tag(key, tag.toString()));
                        elements.add(fcaTag);
                    }
                }
            }

            for (FcaElement s : elements){
                rel.add(fcaClass, s);
            }
        }
    }

    public void generateFromAttributes(Relation rel){
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();

        for(Map.Entry<String, JsonElement> entry : entrySet) {

            JsonObject fields = json.getAsJsonObject(entry.getKey()).getAsJsonObject("fields");
            FcaElement fcaClass = null;
            FcaElement fcaTag = null;

            Set<Map.Entry<String, JsonElement>> fieldSet = fields.entrySet();

            for(Map.Entry<String, JsonElement> field : fieldSet) {
                if(field.getValue().toString().length() > 2) {
                    fcaClass = new FcaElement(new Tag("field", entry.getKey() + "#" + field.getKey()));
                    fcaTag = new FcaElement(new Tag("@description", field.getValue().toString()));
                    rel.add(fcaClass, fcaTag);
                }
            }
        }
    }
}
