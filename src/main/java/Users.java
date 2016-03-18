/**
 * Created by Grv on 3/17/2016.
 */

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.after;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.*;
import com.mongodb.util.JSONSerializers;

import java.util.UUID;

public class Users {

    private static DBCollection collection;

    public Users(){
        try {
            System.out.println("Start Connection");
            // Connect to MongoDB
            MongoClient mongoClient = new MongoClient("localhost");
            DB db                   = mongoClient.getDB("UserDb");
            collection              = db.getCollection("Users");

            System.out.println("Connected to MongoDB@" + mongoClient.getAllAddress());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args){

        new Users();

    //Route for getAllUsers
        get("/getAllUsers", (request, response) -> {
            DBCursor cursor = collection.find();
            BasicDBList ulist = new BasicDBList();
            try{
                while(cursor.hasNext()) {
                    ulist.add(cursor.next());
                }
                return ulist;
            }
            finally{
                cursor.close();
            }

        });

    //Route for createUser - Takes JSON as input
        post("/createUser/:user", (request, response) -> {
            //Read JSON
            JsonObject jsonObject = new JsonParser().parse(request.params(":user")).getAsJsonObject();
            String str= jsonObject.get("id").getAsString();
            //Check if already present
            BasicDBObject query = new BasicDBObject("id", str);
            DBCursor cursor = collection.find(query);
            try{

                if (cursor.hasNext()) {
                        return "User already exists";
                }
                else {
                //Create the document
                BasicDBObject doc = new BasicDBObject();
                //Random generate UUID
                doc.put("id", UUID.randomUUID().toString());
                doc.put("firstName", jsonObject.get("firstName").getAsString());
                doc.put("lastName", jsonObject.get("lastName").getAsString());
                doc.put("email", jsonObject.get("email").getAsString());
                //Address
                DBObject newadd = new BasicDBObject();
                newadd.put("street",jsonObject.get("address").getAsJsonObject().get("street").getAsString());
                newadd.put("city",jsonObject.get("address").getAsJsonObject().get("city").getAsString());
                newadd.put("zip",jsonObject.get("address").getAsJsonObject().get("zip").getAsString());
                newadd.put("state",jsonObject.get("address").getAsJsonObject().get("state").getAsString());
                newadd.put("country",jsonObject.get("address").getAsJsonObject().get("country").getAsString());
                doc.append("address", newadd);
                doc.put("dateCreated",new java.util.Date().toString());
                //Company
                DBObject newcomp = new BasicDBObject();
                newcomp.put("name",jsonObject.get("company").getAsJsonObject().get("name").getAsString());
                newcomp.put("website",jsonObject.get("company").getAsJsonObject().get("website").getAsString());
                doc.append("company", newcomp);
                doc.append("profilePic", jsonObject.get("profilePic").getAsString());
                //Insert Document
                collection.insert(doc);
                response.status(201);
                return doc.toString();
                }
            }
            finally{
                cursor.close();
            }
        });

    //Route for updateUser - Takes JSON as input
        put("/updateUser/:user", (request, response) -> {
            //Read JSON

            JsonObject jsonObject = new JsonParser().parse(request.params(":user")).getAsJsonObject();
            String str= jsonObject.get("id").getAsString();
            BasicDBObject query = new BasicDBObject("id", str);
            DBCursor cursor = collection.find(query);

            try{
                //Update document if found
                if(cursor.hasNext()){
                    BasicDBObject doc = (BasicDBObject) cursor.next();
                    doc.put("firstName",jsonObject.get("firstName").getAsString());
                    doc.put("lastName",jsonObject.get("lastName").getAsString());
                    collection.save(doc);
                    return doc;
                }
                else{
                //If not found
                response.status(404); // 404 Not found
                return "User Not found";
                }
            }
            finally{
                cursor.close();
            }
        });

    //Set response type to JSON
        after((req, res) -> {
            res.type("application/json");
        });
    }

}
