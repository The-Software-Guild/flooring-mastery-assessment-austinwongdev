/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import com.aaw.flooring.service.NoOrdersOnDateException;
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
    List<Order> getAllOrdersOnDate(LocalDate orderDate) throws NoOrdersOnDateException;
    
    Order addOrder(Order order);
    Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax, Product product,
            BigDecimal area);
    Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax, Product product, 
            BigDecimal area, BigDecimal materialCost, BigDecimal laborCost, 
            BigDecimal tax, BigDecimal total);
    
    Order editOrder(Order order, String newCustomerName,
                    StateTax newStateTax, Product newProduct, BigDecimal newArea);
    
    Order removeOrder(int orderNumber, LocalDate orderDate);
    
    void loadAllOrders() throws OrderPersistenceException;
    void saveOrder(LocalDate orderDate) throws NoOrdersOnDateException, OrderPersistenceException;
    void saveAllOrders() throws NoOrdersOnDateException, OrderPersistenceException;
    
    int getNextAvailableOrderNumber();
}
