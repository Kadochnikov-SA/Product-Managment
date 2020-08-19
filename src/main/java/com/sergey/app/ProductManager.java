package com.sergey.app;

import com.sergey.app.storage.dao.ProductDAO;
import com.sergey.app.storage.dao.ShopDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ProductManager {

    private ShopDAO shopDAO;
    private ProductDAO productDAO;

    @Autowired
    public ProductManager(@Qualifier("shopDAOImpl") ShopDAO shopDAO,
                          @Qualifier("productDAOImpl") ProductDAO productDAO) {
        this.shopDAO = shopDAO;
        this.productDAO = productDAO;
    }

    public void addShop(String shopName) {
        shopDAO.addShop(shopName);
    }

    public void addProduct(String productName, int price) {
        productDAO.addProduct(productName, price);
    }

    public void addProductToShop(String productName, String shopName) {
        shopDAO.addProductToShop(productName, shopName);
    }

    public void getStatistics() {
        shopDAO.getStatistics();
    }



}
