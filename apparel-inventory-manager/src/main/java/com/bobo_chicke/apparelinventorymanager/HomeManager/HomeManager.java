package com.bobo_chicke.apparelinventorymanager.HomeManager;

import com.bobo_chicke.apparelinventorymanager.Manager;
import com.bobo_chicke.apparelinventorymanager.util.CheckToken;
import com.bobo_chicke.apparelinventorymanager.util.GetStock;
import com.bobo_chicke.apparelinventorymanager.util.MainData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;

import org.bson.Document;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/HomeManager")
public class HomeManager extends Manager {
    public HomeManager() {
       super();
    }

    @PostMapping(path = "/Get", consumes = "application/json", produces = "application/json")
    public MainData GetMainInformation(@RequestBody String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CheckToken checktoken = objectMapper.readValue(str, CheckToken.class);
        String state = super.checkToken(checktoken.getToken(), "user");
        MainData result = new MainData();
        if (!state.equals("ok")) {
            result.setState(state);
            return result;
        }

        List<Document> list;
        MongoCollection<Document> collection;

        collection = db.getCollection("InStock");
        list = collection.find().into(new ArrayList<>());
        result.setIn_stock_count(list.size());

        collection = db.getCollection("OutStock");
        list = collection.find().into(new ArrayList<>());
        result.setOut_stock_count(list.size());

        collection = db.getCollection("cargo");
        list = collection.find().into(new ArrayList<>());
        result.setCargo_type_count(list.size());

        int total = 0;
        for(Document doc:list) {
            total += Integer.parseInt(doc.get("count").toString());
        }
        result.setCargo_count(total);

        result.setState("ok");

        return result;
    }
}
