package com.sergey.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) {

        /**
         * The Loader class adds 5 stores to the store collection and 20 products to the product collection.
         * Adds 20 products to each store.
         *
         * Name of all products:
         * biscuits, cookies, sweet pies, shortbread, cheesecake, cream rolls, doughnuts, ham, beef, pork, mutton,
         * chicken, wheat bread, crispbread, roll, bun, oil, flour, cereal, cheese, butter;
         *
         * Name of all stores:
         * ABC, oops, TheSHOP, WallStore, A1;
         *
         * Only products that exist in the product collection can be added to the store.
         * You can only add products to an existing store in the collection.
         */

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ProductManager productManager = context.getBean("productManager", ProductManager.class);
        Loader loader = context.getBean("loader",Loader.class);
        loader.fillDataBase();

        productManager.getStatistics();

    }
}
