/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.Order;
import com.aaw.flooring.service.OrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface OrderDao {

    Order getOrder(int orderNumber, LocalDate orderDate) throws OrderNotFoundException;
    List<Order> getAllOrdersOnDate(LocalDate orderDate);
    
    Order addOrder(Order order, LocalDate orderDate);
    Order createOrder(LocalDate orderDate, String customerName, String state,
            BigDecimal taxRate, String productType, BigDecimal area, 
            BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, 
            BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, 
            BigDecimal total);
    
    Order editOrder(int orderNumber, LocalDate orderDate, String newCustomerName,
                    String newState, String newProductType, BigDecimal newArea);
    
    Order removeOrder(int orderNumber, LocalDate orderDate);
    
    void loadAllOrders() throws OrderPersistenceException;
    void saveOrder(LocalDate orderDate) throws OrderPersistenceException;
    void saveAllOrders() throws OrderPersistenceException;
    
    int getNextAvailableOrderNumber();
}
