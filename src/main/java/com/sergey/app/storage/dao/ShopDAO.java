package com.sergey.app.storage.dao;

import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public interface ShopDAO {

    void addShop(String shopName);

    void addProductToShop(String productName, String shopName);

    void getStatistics();



}
