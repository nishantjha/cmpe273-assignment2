package wallet;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
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
import com.mongodb.ParallelScanOptions;

@RestController
public class LoginController
{


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
private int login_no=randomGenerator.nextInt(50000);


//Add Login
@RequestMapping(value="/api/v1/users/{user_id}/weblogins", method=RequestMethod.POST)
public Login addLogin(@Valid @RequestBody Login login1, @PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= LoginController.getAuth();
login_no= login_no+10;
String login_id= "l-"+Integer.toString(login_no);	
Login new_login=new Login(login_id, login1.getUrl(), login1.getLogin(), login1.getPassword());

BasicDBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBCursor cursor =  coll.find(query);
while(cursor.hasNext()){
BasicDBObject push = new BasicDBObject();
push.put("$push",new BasicDBObject("login_info",new BasicDBObject("login_id", login_id)
.append("url", login1.getUrl())
.append("login", login1.getLogin())
.append("password", login1.getPassword())));
coll.update(query, push);
break;
}
return new_login;
}

//list login
@RequestMapping(value="/api/v1/users/{user_id}/weblogins", method=RequestMethod.GET)
public Object viewAllLogins(@PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= LoginController.getAuth();
DBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBObject query1 = new BasicDBObject();
query1.put("login_info", true);
DBCursor cursor =  coll.find(query,query1);
while(cursor.hasNext()){
DBObject query2 = cursor.next();
return query2.get("login_info"); 
}
return null;
}


//delete
@RequestMapping(value="/api/v1/users/{user_id}/weblogins/{login_id}", method=RequestMethod.DELETE)
@ResponseStatus(HttpStatus.NO_CONTENT)
public String deleteLogin(@PathVariable("user_id") String user_id, @PathVariable("login_id") String login_id) throws MongoException, UnknownHostException
{
Login login1=new Login();
DBCollection coll= LoginController.getAuth();

     BasicDBObject sq = new BasicDBObject("user_id", user_id);
      BasicDBObject idoc=new BasicDBObject("login_id",login_id);
      BasicDBObject odoc =new BasicDBObject("login_info",idoc);
      BasicDBObject delq=new BasicDBObject("$pull",odoc);
      coll.update(sq, delq);
return "deleted";



}
}
