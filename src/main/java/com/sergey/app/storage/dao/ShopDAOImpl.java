package com.sergey.app.storage.dao;

import com.mongodb.util.JSONParseException;
import com.sergey.app.storage.Storage;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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
        printNumberOfGoods();
        printAveragePrice();
        printMostExpensiveAndCheapestProduct();
        printNumberOfGoodsCheapestOneHundred();
    }

    private void printNumberOfGoods() {
        Iterator<Document> shops = storage.getShopMongoCollection().find().iterator();
        while (shops.hasNext()) {
            Document shop = shops.next();
            List<Document> goods = new ArrayList<>((List<Document>) shop.get("listOfGoods"));
            System.out.println(shop.get("name") + " number of goods: " + goods.size());
        }
        System.out.println("\n");
    }

    private void printAveragePrice() {
        Iterator<Document> shops = storage.getShopMongoCollection().find().iterator();
        while (shops.hasNext()) {
            Document shop = shops.next();
            List<Document> goods = new ArrayList<>((List<Document>) shop.get("listOfGoods"));
            int averagePrice = 0;
            for (Document product : goods) {
                averagePrice += product.getInteger("price");
            }
            System.out.println(shop.get("name") + " average price: " + averagePrice / goods.size());
        }
        System.out.println("\n");
    }

    private void printMostExpensiveAndCheapestProduct() {
        Iterator<Document> shops = storage.getShopMongoCollection().find().iterator();
        while (shops.hasNext()) {
            Document shop = shops.next();
            List<Document> goods = new ArrayList<>((List<Document>) shop.get("listOfGoods"));
            if (goods.size() != 0) {
                String expensiveProduct = goods.get(0).getString("name");
                String cheapestProduct = goods.get(0).getString("name");
                int highPrice = goods.get(0).getInteger("price");
                int lowPrice = goods.get(0).getInteger("price");
                for (Document product : goods) {
                    if (product.getInteger("price") > highPrice) {
                        highPrice = product.getInteger("price");
                        expensiveProduct = product.getString("name");
                    }
                    if (product.getInteger("price") < lowPrice) {
                        lowPrice = product.getInteger("price");
                        cheapestProduct = product.getString("name");
                    }
                }
                System.out.println(shop.get("name") + " cheapest product: " + cheapestProduct + ". Most expensive product: " + expensiveProduct);
            }
        }
        System.out.println("\n");
    }

    private void printNumberOfGoodsCheapestOneHundred() {
        Iterator<Document> shops = storage.getShopMongoCollection().find().iterator();
        while (shops.hasNext()) {
            Document shop = shops.next();
            List<Document> goods = new ArrayList<>((List<Document>) shop.get("listOfGoods"));
            int counter = 0;
            for (Document product : goods) {
                if (product.getInteger("price") < 100) {
                    counter ++;
                }
            }
            System.out.println(shop.get("name") + " number of goods cheapest one hundred is: " + counter);
        }
        System.out.println("\n");
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
