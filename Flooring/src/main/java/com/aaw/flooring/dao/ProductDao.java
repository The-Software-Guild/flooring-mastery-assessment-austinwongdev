/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.Product;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface ProductDao {
    
    Product getProduct(String productType);
    
    List<Product> getAllProducts();

    Product addProduct(Product product);
    
    void loadProducts() throws OrderPersistenceException;
    
}
