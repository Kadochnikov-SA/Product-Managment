package com.sergey.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Loader {

    private String[] goods = new String[]{"biscuits", "cookies", "sweet pies", "shortbread", "cheesecake", "cream rolls", "doughnuts", "ham", "beef", "pork", "mutton",
            "chicken", "wheat bread", "crispbread", "roll", "bun", "oil", "flour", "cereal", "cheese", "butter"};

    private String[] shops = new String[]{"ABC", "oops", "TheSHOP", "WallStore", "A1"};

    @Autowired
    private ProductManager productManager;

    public void fillDataBase() {
        fillGoodsCollection(productManager);
        fillShopsCollection(productManager);
        addGoodsToShops(productManager);
    }

    private void fillShopsCollection(ProductManager productManager) {
        for (int i = 0; i < shops.length; i++) {
            productManager.addShop(shops[i]);
        }
    }

    private void fillGoodsCollection(ProductManager productManager) {
        for (int i = 0; i < goods.length; i++) {
            productManager.addProduct(goods[i], (int) ((500 * Math.random()) + 10));
        }
    }

    private void addGoodsToShops(ProductManager productManager) {
        for (int i = 0; i < shops.length; i++) {
            for (int j = 0; j < 20; j++) {
                productManager.addProductToShop(goods[getRandomIndex()] ,shops[i]);
            }
        }
    }

    private int getRandomIndex() {
        int index = (int) (Math.random() * (goods.length));
        return index;
    }
}
