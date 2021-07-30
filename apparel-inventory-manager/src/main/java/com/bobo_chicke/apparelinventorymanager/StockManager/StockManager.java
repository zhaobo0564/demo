package com.bobo_chicke.apparelinventorymanager.StockManager;

import com.bobo_chicke.apparelinventorymanager.Manager;
import com.bobo_chicke.apparelinventorymanager.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

@RestController
@CrossOrigin
@RequestMapping("/StockManager")
public class StockManager extends Manager {
    public StockManager() {
        super();
    }

    @PostMapping(path = "/GetInStock", consumes = "application/json", produces = "application/json")
    public GetStock GetInStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CheckToken checktoken = objectMapper.readValue(str, CheckToken.class);
        String state = super.checkToken(checktoken.getToken(), "user");
        GetStock res = new GetStock();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        MongoCollection<Document> collection;
        collection = db.getCollection("InStock");
        ArrayList<Document> query = collection.find().into(new ArrayList<Document>());
        ArrayList<Stock> result = new ArrayList<Stock>();
        for (Document q:query) {
            q.remove("_id");
            Stock stock = objectMapper.readValue(q.toJson(), new TypeReference<Stock>(){});
            ArrayList<Cargo> cargos = new ArrayList<Cargo>();
            for (Cargo cargo: stock.getCargos()){
                ArrayList<Document> tmp = db.getCollection("cargo").find(new Document("id", cargo.getId())).into(new ArrayList<Document>());
                for(Document doc:tmp) {
                    Cargo t = new Cargo();
                    t.setId(doc.get("id").toString());
                    t.setName(doc.get("name").toString());
                    t.setColor(doc.get("color").toString());
                    t.setMaterial(doc.get("material").toString());
                    t.setSize(doc.get("size").toString());
                    t.setExfactoryprice(doc.get("exfactoryprice").toString());
                    t.setRetailprice(doc.get("retailprice").toString());
                    t.setManufacturer(doc.get("manufacturer").toString());
                    t.setCount(cargo.getCount());
                    cargos.add(t);
                }
            }
            stock.setCargos(cargos);
            stock.setRemarks(stock.getRemarks());
            result.add(stock);
        }

        res.setState("ok");
        res.setStocks(result);
        return res;
    }

    @PostMapping(path = "/AddInStock", consumes = "application/json", produces = "application/json")
    public ReturnState AddInStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AddStock addinstock = objectMapper.readValue(str, new TypeReference<AddStock>(){});

        String state = super.checkToken(addinstock.getToken(), "admin");
        ReturnState res = new ReturnState();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        MongoCollection<Document> collection;
        collection = db.getCollection("InStock");

        IdGenerater idGenerater = new IdGenerater();
        String id = idGenerater.generate(32);

        ArrayList<Document> query = collection.find(eq("id", id)).into(new ArrayList<Document>());
        while (query.size() != 0) {
            id = idGenerater.generate(32);
            query = collection.find(eq("id", id)).into(new ArrayList<Document>());
        }

        Document Insert = new Document();
        ArrayList<Document> cargolist = new ArrayList<Document>();
        for(Cargo cargo:addinstock.getCargos()) {
            Document tmp = new Document();
            tmp.append("id", cargo.getId());
            tmp.append("count", cargo.getCount());
            cargolist.add(tmp);
            Document inc = new Document();
            inc.append("$inc", new Document().append("count", cargo.getCount()));
            db.getCollection("cargo").updateOne(new Document().append("id", cargo.getId()), inc);
        }
        Insert.append("id", id);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        Date date = new Date();
        Insert.append("date", sdf.format(date));
        Insert.append("remarks", addinstock.getRemarks());
        Insert.append("cargos", cargolist);

        db.getCollection("InStock").insertOne(Insert);

        res.setState("ok");
        return res;
    }

    @PostMapping(path = "/GetOutStock", consumes = "application/json", produces = "application/json")
    public GetStock GetOutStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CheckToken checktoken = objectMapper.readValue(str, CheckToken.class);
        String state = super.checkToken(checktoken.getToken(), "user");
        GetStock res = new GetStock();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        MongoCollection<Document> collection;
        collection = db.getCollection("OutStock");
        ArrayList<Document> query = collection.find().into(new ArrayList<Document>());
        ArrayList<Stock> result = new ArrayList<Stock>();
        for (Document q:query) {
            q.remove("_id");
            Stock stock = objectMapper.readValue(q.toJson(), new TypeReference<Stock>(){});
            ArrayList<Cargo> cargos = new ArrayList<Cargo>();
            for (Cargo cargo: stock.getCargos()){
                ArrayList<Document> tmp = db.getCollection("cargo").find(new Document("id", cargo.getId())).into(new ArrayList<Document>());
                for(Document doc:tmp) {
                    Cargo t = new Cargo();
                    t.setId(doc.get("id").toString());
                    t.setName(doc.get("name").toString());
                    t.setColor(doc.get("color").toString());
                    t.setMaterial(doc.get("material").toString());
                    t.setSize(doc.get("size").toString());
                    t.setExfactoryprice(doc.get("exfactoryprice").toString());
                    t.setRetailprice(doc.get("retailprice").toString());
                    t.setManufacturer(doc.get("manufacturer").toString());
                    t.setCount(cargo.getCount());
                    cargos.add(t);
                }
            }
            stock.setCargos(cargos);
            stock.setRemarks(stock.getRemarks());
            result.add(stock);
        }

        res.setState("ok");
        res.setStocks(result);
        return res;
    }

    @PostMapping(path = "/AddOutStock", consumes = "application/json", produces = "application/json")
    public ReturnState AddOutStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AddStock addoutstock = objectMapper.readValue(str, new TypeReference<AddStock>(){});

        String state = super.checkToken(addoutstock.getToken(), "admin");
        ReturnState res = new ReturnState();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        MongoCollection<Document> collection;
        collection = db.getCollection("OutStock");

        IdGenerater idGenerater = new IdGenerater();
        String id = idGenerater.generate(32);

        ArrayList<Document> query = collection.find(eq("id", id)).into(new ArrayList<Document>());
        while (query.size() != 0) {
            id = idGenerater.generate(32);
            query = collection.find(eq("id", id)).into(new ArrayList<Document>());
        }

        Document Insert = new Document();
        ArrayList<Document> cargolist = new ArrayList<Document>();
        for(Cargo cargo:addoutstock.getCargos()) {
            Document tmp = new Document();
            tmp.append("id", cargo.getId());
            tmp.append("count", cargo.getCount());
            cargolist.add(tmp);
            Document inc = new Document();
            inc.append("$inc", new Document().append("count", -cargo.getCount()));
            db.getCollection("cargo").updateOne(new Document().append("id", cargo.getId()), inc);
            Document mx = new Document();
            mx.append("$max", new Document().append("count", 0));
            db.getCollection("cargo").updateOne(new Document().append("id", cargo.getId()), mx);
        }
        Insert.append("id", id);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        Date date = new Date();
        Insert.append("date", sdf.format(date));
        Insert.append("remarks", addoutstock.getRemarks());
        Insert.append("cargos", cargolist);

        db.getCollection("OutStock").insertOne(Insert);

        res.setState("ok");
        return res;
    }

    @PostMapping(path = "/UpdateInStock", consumes = "application/json", produces = "application/json")
    public ReturnState UpdateInStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateRmarks updateremarks = objectMapper.readValue(str, new TypeReference<UpdateRmarks>(){});

        String state = super.checkToken(updateremarks.getToken(), "admin");
        ReturnState res = new ReturnState();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        Document doc = new Document();
        doc.append("$set", new Document("remarks", updateremarks.getRemarks()));
        db.getCollection("InStock").updateOne(new Document("id", updateremarks.getId()), doc);

        res.setState("ok");
        return res;
    }

    @PostMapping(path = "/UpdateOutStock", consumes = "application/json", produces = "application/json")
    public ReturnState UpdateOutStock(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateRmarks updateremarks = objectMapper.readValue(str, new TypeReference<UpdateRmarks>(){});

        String state = super.checkToken(updateremarks.getToken(), "admin");
        ReturnState res = new ReturnState();
        if (!state.equals("ok")) {
            res.setState(state);
            return res;
        }

        Document doc = new Document();
        doc.append("$set", new Document("remarks", updateremarks.getRemarks()));
        db.getCollection("OutStock").updateOne(new Document("id", updateremarks.getId()), doc);

        res.setState("ok");
        return res;
    }
}
