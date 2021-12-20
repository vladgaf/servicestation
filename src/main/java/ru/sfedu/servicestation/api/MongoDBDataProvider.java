package ru.sfedu.servicestation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.DateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.joda.time.DateTime;
import ru.sfedu.servicestation.beans.HistoryContent;
import ru.sfedu.servicestation.utils.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MongoDBDataProvider {

    private static final Logger log = LogManager.getLogger(DataProviderXML.class);
    private MongoClient client=null;
    private MongoDatabase database;

    private  <T> Map<String, Object> convertEntityToMap(T entity) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(entity, Map.class);
    }

    public HistoryContent initHistoryContentTrue(Object object,String objectName,String className,String methodName){
        DateTime dateTime = new DateTime();
        Map<String,Object> map = new HashMap<>();
        map.put(objectName,convertEntityToMap(object));
        HistoryContent historyContent = new HistoryContent();
        historyContent.setId(new Random().nextLong());
        historyContent.setClassName(className);
        historyContent.setCreatedDate(dateTime.toString());
        historyContent.setActor(Constants.SYSTEM);
        historyContent.setMethodName(methodName);
        historyContent.setObject(map);
        historyContent.setStatus(Constants.HC_STATUS_OK);
        return historyContent;
    }
    public HistoryContent initHistoryContentFalse(String string,String className,String methodName){
        DateTime dateTime = new DateTime();
        Map<String,Object> map = new HashMap<>();
        map.put(Constants.NULL,Constants.NULL);
        HistoryContent historyContent = new HistoryContent();
        historyContent.setId(new Random().nextLong());
        historyContent.setClassName(className);
        historyContent.setCreatedDate(dateTime.toString());
        historyContent.setActor(Constants.SYSTEM);
        historyContent.setMethodName(methodName);
        historyContent.setObject(map);
        historyContent.setStatus(Constants.HC_STATUS_FAIL);
        return historyContent;
    }
    public List<Document> readAll()  {
        try {
            initConnection(Constants.MONGODB_TEST_SERVER);
            MongoCollection<Document> mongoCollection = database.getCollection(Constants.MONGODB_TEST_SERVER);
            FindIterable<Document> documents = mongoCollection.find();
            return documents.into(new ArrayList<Document>());
        }catch (MongoException | IOException e){
            log.info(e);
            return null;
        }
    }
    private MongoClientOptions getMongoClientOptions() {
        return MongoClientOptions
                .builder()
                .serverSelectionTimeout(Constants.MONGODB_TIMEOUT)
                .build();
    }

    public void initConnection(String string) throws IOException {
        try {
            ServerAddress serverAddress = new ServerAddress(ConfigurationUtil.getConfigurationEntry(Constants.MONGODB));
            this.client = new MongoClient(serverAddress, getMongoClientOptions());
            this.database = client.getDatabase(string);
            Bson command = new BsonDocument(Constants.MONGO_PING, new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            log.info(Constants.MONGODB_SUCCESSFUL_CONNECTION);
        } catch (Exception e) {
            log.info(Constants.MONGODB_FAULT_CONNECTION);
        }
    }

    public void closeConnection(){
        try {
            this.client.close();
        } catch (MongoSocketOpenException | NullPointerException e){
            log.info(Constants.MONGODB_CLOSE);
        }
    }
    public void insertRecord(HistoryContent historyContent,String string) throws IOException {
        initConnection(string);
        try {
            MongoCollection<Document> mongoCollection = database.getCollection(string);
            mongoCollection.insertOne(new Document(this.convertEntityToMap(historyContent)));
            closeConnection();
        } catch (NullPointerException e){
            log.error(Constants.MONGODB_FAULT_CONNECTION);
        } catch (MongoTimeoutException e) {
            log.info(e);
        }


    }
}
