package com.sergey.app.storage.dao;

import org.springframework.stereotype.Component;

@Component
public interface ProductDAO {

    void addProduct(String productName, int price);
}
