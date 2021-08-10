/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Used to interact with Product DTOs in file format.
 * @author Austin Wong
 */
public class ProductDaoFileImpl implements ProductDao {

    private final String PRODUCT_FILE;
    private final String DELIMITER = "::";
    private final Map<String, Product> productMap = new HashMap<>();
    
    public ProductDaoFileImpl(){
        this.PRODUCT_FILE = "src/main/resources/Data/Products.txt";
    }
    
    public ProductDaoFileImpl(String productFilePath){
        this.PRODUCT_FILE = productFilePath;
    }
    
    @Override
    public Product getProduct(String productType){
        return productMap.get(productType);
    }
    
    @Override
    public List<Product> getAllProducts(){
        return productMap.values().stream().sorted(Comparator.comparing(Product::getProductType)).collect(Collectors.toList());
    }
    
    @Override
    public Product addProduct(Product product){
        return productMap.put(product.getProductType(), product);
    }
    
    /**
     * Loads Products from each line of file. Each line is assumed to be a
     * separate product and a header line is assumed.
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadProducts() throws OrderPersistenceException{
        try{
            Scanner scan = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
            
            // Skip header
            if (scan.hasNextLine()){
                scan.nextLine();
            }
            
            // Parse file contents
            while (scan.hasNextLine()){
                Product nextProduct = unmarshallProduct(scan.nextLine());
                addProduct(nextProduct);
            }
            
            scan.close();
        } catch (FileNotFoundException ex){
            throw new OrderPersistenceException("Could not load products", ex);
        }
    }
    
    /**
     * Takes in a String and converts it to a Product object. Each field of 
     * productAsText is assumed to be separated by the DELIMITER variable 
     * assigned to this class.
     * @param productAsText - Product object in text form
     * @return - Product object
     */
    private Product unmarshallProduct(String productAsText){
        String[] productFields = productAsText.split(DELIMITER);
        String productType = productFields[0];
        BigDecimal costPerSquareFoot = new BigDecimal(productFields[1]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(productFields[2]);
        Product newProduct = new Product(productType, costPerSquareFoot,
                                         laborCostPerSquareFoot);
        return newProduct;
    }
}
