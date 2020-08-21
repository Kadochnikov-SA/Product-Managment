package com.sergey.app.storage.dao;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSONParseException;
import com.sergey.app.storage.Storage;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Accumulators.*;


@Component
public class ShopDAOImpl implements ShopDAO {

    @Autowired
    private Storage storage;

    @Override
    public void addShop(String shopName) {
        List<Document> goods = new ArrayList<>();
        Document shop = new Document()
                .append("name", shopName)
                .append("listOfGoods", goods);
        storage.getShopMongoCollection().insertOne(shop);
    }

    @Override
    public void addProductToShop(String productName, String shopName) {
        Document product = null;
        try {
            BsonDocument bsonDocument = BsonDocument.parse("{name : \"" + productName + "\"}");
            product = storage
                    .getGoodsMongoCollection()
                    .find(bsonDocument)
                    .first();
        } catch (JSONParseException e) {
            e.printStackTrace();
        }
        if (product != null) {
            updateShop(product, shopName);
        } else {
            System.out.println("Product named " + productName + " not found");
        }
    }

    @Override
    public void getStatistics() {
        List<Document> statisticsList = storage.getShopMongoCollection().aggregate((Arrays.asList(
                Aggregates.lookup("products", "products", "name", "product_list"),
                Aggregates.unwind("$listOfGoods"),
                Aggregates.sort(Sorts.descending("listOfGoods.price")),
                Aggregates.group("$name",
                        sum("productCount", 1),
                        avg("avgPrice", "$listOfGoods.price"),
                        first("theMostExpensiveProduct", "$listOfGoods.name"),
                        last("theCheapestProduct", "$listOfGoods.name"),
                        sum("lessThen100Count", BsonDocument.parse("{$cond: [ { $lt: [ \"$listOfGoods.price\", 100 ] }, 1, 0 ]}"))
                )))).into(new ArrayList<>());
        statisticsList.forEach(System.out::println);
    }


    private void updateShop(Document product, String shopName) {
        BsonDocument bsonDocument = BsonDocument.parse("{name :\"" + shopName + "\"}");
        Document shop = null;
        try {
            shop = storage
                    .getShopMongoCollection()
                    .find(bsonDocument)
                    .first();
        } catch (JSONParseException e) {
            e.printStackTrace();
        }
        if (shop != null) {
            Document updatedShop = getUpdatedShop(shop, product);
            storage.getShopMongoCollection().deleteOne(shop);
            storage.getShopMongoCollection().insertOne(updatedShop);
        } else {
            System.out.println("Shop named " + shopName + " not found");
        }
    }


    private Document getUpdatedShop(Document shop, Document product) {
        List<Document> goods = new ArrayList<>((List<Document>) shop.get("listOfGoods"));
        goods.add(product);
        Document newShop = new Document()
                .append("name", shop.get("name"))
                .append("listOfGoods", goods);
        return newShop;
    }
}
