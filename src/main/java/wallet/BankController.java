package wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
import java.lang.Math;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
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
public class BankController
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
private int login_id=randomGenerator.nextInt(35000);




//Addbank
@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts", method=RequestMethod.POST)
public Bank addbank(@Valid @RequestBody Bank bank, @PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= BankController.getAuth();
login_id= login_id+11;
String bank_id= "b-"+Integer.toString(login_id);
Bank new_bank=new Bank(bank_id, bank.getAccount_name(), bank.getRouting_number(), bank.getAccount_number());
RestTemplate restTemplate=new RestTemplate();
ResponseEntity<String> entity = restTemplate.getForEntity("http://www.routingnumbers.info/api/data.json?rn="+bank.getRouting_number(), String.class);
JsonParser jsonParser = new JacksonJsonParser();
Map<String,Object> resbody = jsonParser.parseMap(entity.getBody());
if((resbody.get("code").toString().equals("200"))){
bank.setAccount_name(resbody.get("customer_name").toString());
new_bank.setAccount_name(resbody.get("customer_name").toString());
}
BasicDBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBCursor cursor =  coll.find(query);
while(cursor.hasNext()){
BasicDBObject push = new BasicDBObject();
push.put("$push",new BasicDBObject("bank_info",new BasicDBObject("bank_id", bank_id)
.append("account_name", bank.getAccount_name())
.append("routing_number", bank.getRouting_number())
.append("account_number", bank.getAccount_number())));
coll.update(query, push);
break;
}

return new_bank;
}

//list bank
@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts", method=RequestMethod.GET)
public Object viewAllBanks(@PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= BankController.getAuth();
DBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBObject query1 = new BasicDBObject();
query1.put("bank_info", true);
DBCursor cursor =  coll.find(query,query1);
while(cursor.hasNext()){
DBObject query2 = cursor.next();
return query2.get("bank_info"); 
}
return null;
}

//Delete a bank login
@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts/{bank_id}", method=RequestMethod.DELETE)
@ResponseStatus(HttpStatus.NO_CONTENT)
public String deleteBank(@PathVariable("user_id") String user_id, @PathVariable("bank_id") String bank_id) throws MongoException, UnknownHostException{
DBCollection coll= LoginController.getAuth();
   BasicDBObject sq = new BasicDBObject("user_id", user_id);
      BasicDBObject idoc=new BasicDBObject("bank_id",bank_id);
      BasicDBObject odoc =new BasicDBObject("bank_info",idoc);
      BasicDBObject delq=new BasicDBObject("$pull",odoc);
      coll.update(sq, delq);

return "deleted";
}
}
