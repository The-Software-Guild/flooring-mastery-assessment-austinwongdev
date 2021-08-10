/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.flooring.dao;

import com.aaw.flooring.model.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Austin Wong
 */
public class ProductDaoFileImplTest {
    
    private final String TEST_PRODUCT_FILE =
            "src/test/resources/TestData/Products.txt";
    private final ProductDao testProductDao;
    
    public ProductDaoFileImplTest() {
        this.testProductDao = new ProductDaoFileImpl(TEST_PRODUCT_FILE);
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testLoadGetProduct() throws OrderPersistenceException{
        Product expectedProduct = new Product("Carpet", new BigDecimal("2.25"), 
                                              new BigDecimal("2.10"));
        
        testProductDao.loadProducts();
        
        Product returnedProduct = testProductDao.getProduct("Carpet");
        assertNotNull(returnedProduct, "Should return a Product");
        assertEquals(expectedProduct, returnedProduct, "Should return Carpet");
    }
    
    @Test
    public void testLoadProductsFileNotFound(){
        ProductDao badDao = new ProductDaoFileImpl("NonExistentFile.txt");
        
        assertThrows(OrderPersistenceException.class, 
                     () -> badDao.loadProducts(),
                     "Should throw OrderPersistenceException");
    }
    
    @Test
    public void testAddGetProduct(){
        Product expectedProduct = new Product("Carpet", new BigDecimal("2.25"), 
                                              new BigDecimal("2.10"));
        
        testProductDao.addProduct(expectedProduct);
        
        Product returnedProduct = testProductDao.getProduct("Carpet");
        assertNotNull(returnedProduct, "Should return a Product");
        assertEquals(expectedProduct, returnedProduct, "Should return Carpet");
    }
    
    @Test
    public void testGetAllProducts(){
        Product expectedProduct1 = new Product("Carpet", new BigDecimal("2.25"), 
                                              new BigDecimal("2.10"));
        Product expectedProduct2 = new Product("Tile", new BigDecimal("1.25"), 
                                              new BigDecimal("3.10"));
        
        testProductDao.addProduct(expectedProduct1);
        testProductDao.addProduct(expectedProduct2);
        
        List<Product> returnedProducts = testProductDao.getAllProducts();
        assertNotNull(returnedProducts, "Should return a list of Products");
        assertTrue(returnedProducts.contains(expectedProduct1), "Should contain Carpet");
        assertTrue(returnedProducts.contains(expectedProduct2), "Should contain Tile");
        assertTrue(returnedProducts.size()==2, "Should contain 2 products");
    }
    
}
