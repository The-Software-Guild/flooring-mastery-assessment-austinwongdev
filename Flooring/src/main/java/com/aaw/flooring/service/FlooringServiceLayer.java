/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.service;

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

    void loadAllOrders();
    List<Order> loadAllOrdersOnDate(LocalDate orderDate);
    void saveAddedOrders();
    Order getOrder(int orderNumber, LocalDate orderDate);
    Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax,
            Product product, BigDecimal area);
    Order editOrder(Order orderToEdit, String newCustomerName, StateTax newStateTax,
            Product newProduct, BigDecimal newArea);
    Order removeOrder(int orderNumber, LocalDate orderDate);
    List<Product> getAllProducts();
    List<StateTax> getAllStateTaxes();
    Order calculateOrder(Order order);
}
