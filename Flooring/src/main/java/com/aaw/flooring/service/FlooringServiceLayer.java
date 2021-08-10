/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.service;

import com.aaw.flooring.dao.OrderNotFoundException;
import com.aaw.flooring.dao.NoOrdersOnDateException;
import com.aaw.flooring.dao.OrderPersistenceException;
import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface FlooringServiceLayer {
    
    void loadAllProducts() throws OrderPersistenceException;
    void loadAllStateTaxes() throws OrderPersistenceException;
    void loadAllOrders() throws OrderPersistenceException;
    void saveOrder(LocalDate orderDate) throws NoOrdersOnDateException, OrderPersistenceException;
    void saveAllOrders() throws NoOrdersOnDateException, OrderPersistenceException;
    
    Product getProduct(String productType);
    List<Product> getAllProducts();
    
    StateTax getStateTax(String stateAbbreviation);
    List<StateTax> getAllStateTaxes();
    
    Order getOrder(int orderNumber, LocalDate orderDate) throws NoOrdersOnDateException, OrderNotFoundException;
    List<Order> getAllOrdersOnDate(LocalDate orderDate) throws NoOrdersOnDateException;
    Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax,
            Product product, BigDecimal area);
    Order addOrder(Order order);
    Order editOrder(Order orderToEdit, String newCustomerName, StateTax newStateTax,
            Product newProduct, BigDecimal newArea);
    Order removeOrder(int orderNumber, LocalDate orderDate) throws NoOrdersOnDateException, OrderNotFoundException;
    Order calculateOrder(Order order);
    
    BigDecimal getMinimumArea();
    boolean isValidCustomerName(String customerName);
}
