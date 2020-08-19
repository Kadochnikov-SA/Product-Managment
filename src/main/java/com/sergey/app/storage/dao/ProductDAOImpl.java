package com.sergey.app.storage.dao;

import com.sergey.app.storage.Storage;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
 class ProductDAOImpl implements ProductDAO  {

    @Autowired
    private Storage storage;


    @Override
    public void addProduct(String productName, int price) {
        Document product = new Document()
                .append("name", productName)
                .append("price", price);
        storage.getGoodsMongoCollection().insertOne(product);
    }
}
