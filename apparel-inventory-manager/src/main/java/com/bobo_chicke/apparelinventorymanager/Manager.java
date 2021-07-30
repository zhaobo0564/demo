package com.bobo_chicke.apparelinventorymanager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Manager {
    protected MongoClient mongo;
    protected MongoDatabase db;
    protected Jedis jedis;
    public Manager(){
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/application.properties"));
            mongo = MongoClients.create(props.getProperty("mongodb_uri", "mongodb://localhost:27017"));
            db = mongo.getDatabase("apparel-inventory-manager");
            JedisPool jedispool = new JedisPool(new JedisPoolConfig(),
                    props.getProperty("redis.host", "localhost"),
                    Integer.parseInt(props.getProperty("redis.port", "6379"))
            );
            jedis = jedispool.getResource();

            ArrayList<Document> doc = db.getCollection("User").find(new Document("username", "admin")).into(new ArrayList<Document>());
            if(doc.size() == 0){
                Document tmp = new Document();
                tmp.append("username", props.getProperty("superadmin.username", "admin"));
                tmp.append("password", props.getProperty("superadmin.password", "6VQ8XsroCGp0c2aVEhMP"));
                tmp.append("auth", "superadmin");
                db.getCollection("User").insertOne(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int getauthsize(String auth){
        if(auth.equals("user")){
            return 0;
        }
        if(auth.equals("admin")){
            return 1;
        }
        return 2;
    }

    protected String checkToken(String token, String auth) {
        if(!jedis.exists(token)) {
            return "no";
        }
        String Auth = jedis.get(token);
        if(getauthsize(Auth) < getauthsize(auth)) {
            return "reject";
        }
        return "ok";
    }
}
