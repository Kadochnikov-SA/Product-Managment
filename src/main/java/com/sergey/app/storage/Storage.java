package com.sergey.app.storage;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public
class Storage {

    private MongoDatabase mongoDatabase;
    MongoCollection<Document> shopMongoCollection;
    MongoCollection<Document> goodsMongoCollection;

    @PostConstruct
    private void init() {
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        mongoDatabase = mongoClient.getDatabase("ProductsShopsStorage");
        mongoDatabase.drop();
        shopMongoCollection = mongoDatabase.getCollection("Shops");
        goodsMongoCollection = mongoDatabase.getCollection("Goods");
    }

    @PreDestroy
    private void dropDatabase() {
        mongoDatabase.drop();
    }

    public MongoCollection<Document> getShopMongoCollection() {
        return shopMongoCollection;
    }

    public MongoCollection<Document> getGoodsMongoCollection() {
        return goodsMongoCollection;
    }
}
