package wallet;

import javax.validation.Valid;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
import java.lang.Math;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.ServerAddress;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.*;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import java.net.UnknownHostException;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.ParallelScanOptions;




@RestController
public class UserController {

public static DBCollection getAuth() throws MongoException, UnknownHostException{
String uri="mongodb://ds043200.mongolab.com:43200/";
MongoCredential credential=MongoCredential.createMongoCRCredential("nishantjha", "cmpe273", "Nisan1,.,".toCharArray());
MongoClientURI uri1=new MongoClientURI(uri);
MongoClient mongoClient = new MongoClient(uri1);
DB db=mongoClient.getDB("cmpe273");
db.authenticate("nishantjha","Nisan1,.,".toCharArray());
DBCollection coll1=db.getCollection("user");
return coll1;
}
Random randomGenerator = new Random();
private int id_no=randomGenerator.nextInt(1000);

//Add User
@RequestMapping(value="/api/v1/users", method=RequestMethod.POST)
public User update(@Valid @RequestBody User user1) throws MongoException, UnknownHostException {
DBCollection coll= UserController.getAuth();
id_no= id_no+17;
String user_id= "u-"+Integer.toString(id_no);	
Date date = new Date();
Format f= new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
String created_at= f.format(date);
String updated_at= f.format(date);
User new_user=new User(user_id, user1.getEmail(), user1.getPassword(), user1.getName(), created_at, updated_at);
BasicDBObject doc = new BasicDBObject("user_id", user_id)
.append("email", user1.getEmail())
.append("password", user1.getPassword())
.append("name", user1.getName())
.append("created_at", created_at)
.append("updated_at", updated_at);
coll.insert(doc);
return new_user;
}

//Get User details
@RequestMapping(value="/api/v1/users/{user_id}",method=RequestMethod.GET)
public DBObject view(@PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{

DBCollection coll= UserController.getAuth();
DBObject query=BasicDBObjectBuilder.start().add("user_id", user_id).get();
DBCursor cursor = coll.find(query);
while(cursor.hasNext()){
query = cursor.next();
query.removeField("_id");
query.removeField("bank_info");
query.removeField("card_info");
query.removeField("login_info");
return query; 
}
return null;
}


//Update User Details
@RequestMapping(value="/api/v1/users/{user_id}",method=RequestMethod.PUT)
public User edit(@Valid @RequestBody User user,@PathVariable("user_id") String user_id) throws MongoException, UnknownHostException {
System.out.println(user_id);
Date date = new Date();
Format f= new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
String updated_at= f.format(date);

DBCollection coll= UserController.getAuth();
User new_user=new User(user_id, user.getEmail(), user.getPassword(), user.getName(), updated_at);
BasicDBObject dbObject = new BasicDBObject("user_id", user_id);

// your update condition
DBObject newObject ;
DBCursor cursor =  coll.find(dbObject);
while(cursor.hasNext()){newObject = cursor.next();
newObject.put("email", user.getEmail());
newObject.put("password", user.getPassword());
newObject.put("name", user.getName());
newObject.put("updated_at", updated_at);
coll.findAndModify(dbObject, newObject);	}

//add field, either a new field or any existing field

return new_user;
}

}
