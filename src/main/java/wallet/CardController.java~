package wallet;

import javax.validation.Valid;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;  
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
public class CardController
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
private int card_number=randomGenerator.nextInt(20000);


//Add card details
@RequestMapping(value="/api/v1/users/{user_id}/idcards", method=RequestMethod.POST)
public Card addCard(@Valid @RequestBody Card card, @PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= CardController.getAuth();
card_number= card_number+51;
String card_id= "c-"+Integer.toString(card_number);	
Calendar date = Calendar.getInstance();
date.setTime(new Date());
date.add(Calendar.YEAR,1);
Format f = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
String expiration_date=f.format(date);
Card new_card=new Card(card_id, card.getCard_name(), card.getCard_number(), card.getExpiration_date());


BasicDBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBCursor cursor =  coll.find(query);
while(cursor.hasNext()){
BasicDBObject push = new BasicDBObject();
push.put("$push",new BasicDBObject("card_info",new BasicDBObject("card_id", card_id)
.append("card_name", card.getCard_name())
.append("card_number", card.getCard_number())
.append("expiration_date", card.getExpiration_date())));
coll.update(query, push);
break;
}

return new_card;
}


//Get list of cards
@RequestMapping(value="/api/v1/users/{user_id}/idcards", method=RequestMethod.GET)
public Object viewAllCards(@PathVariable("user_id") String user_id) throws MongoException, UnknownHostException{
DBCollection coll= CardController.getAuth();
DBObject query = new BasicDBObject();
query.put("user_id", user_id);
DBObject query1 = new BasicDBObject();
query1.put("card_info", true);
DBCursor cursor =  coll.find(query,query1);
while(cursor.hasNext()){
DBObject query2 = cursor.next();
return query2.get("card_info"); 
}
return null;
}

//delete card details
@RequestMapping(value="/api/v1/users/{user_id}/idcards/{card_id}", method=RequestMethod.DELETE)
@ResponseStatus(HttpStatus.NO_CONTENT)
 public String deleteCard(@PathVariable("user_id") String user_id, @PathVariable("card_id") String card_id) throws MongoException, UnknownHostException{
DBCollection coll= LoginController.getAuth();
   BasicDBObject sq = new BasicDBObject("user_id", user_id);
      BasicDBObject idoc=new BasicDBObject("card_id",card_id);
      BasicDBObject odoc =new BasicDBObject("card_info",idoc);
      BasicDBObject delq=new BasicDBObject("$pull",odoc);
      coll.update(sq, delq);
return "Deleted";

}
}
