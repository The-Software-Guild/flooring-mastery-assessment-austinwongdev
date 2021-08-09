/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.Product;

/**
 *
 * @author Austin Wong
 */
public interface ProductDao {
    
    Product getProduct(String productType);

    Product addProduct(Product product);
    
    Product removeProduct(String productType);
    
    void loadProducts() throws OrderPersistenceException;
    
}
