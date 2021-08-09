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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Austin Wong
 */
public class OrderDaoFileImpl implements OrderDao {

    private final Map<LocalDate, String> orderFileMap = new HashMap<>();
    private Map<LocalDate, List<Order>> orderStore = new HashMap<>();
    private final String DELIMITER = "::";
    private final String ORDER_FOLDER_PATH;
    private int nextAvailableOrderNumber;

    public OrderDaoFileImpl(){
        this.ORDER_FOLDER_PATH = "src/main/resources/Orders/";
    }
    
    public OrderDaoFileImpl(String ORDER_FOLDER_PATH) {
        this.ORDER_FOLDER_PATH = ORDER_FOLDER_PATH;
    }
    
    @Override
    public Order getOrder(int orderNumber, LocalDate orderDate) throws OrderNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order addOrder(Order order, LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order createOrder(LocalDate orderDate, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order editOrder(int orderNumber, LocalDate orderDate, String newCustomerName, String newState, String newProductType, BigDecimal newArea) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> loadAllOrdersOnDate(LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveOrders(LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> getAllOrdersOnDate(LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNextAvailableOrderNumber(LocalDate orderDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Order unmarshallOrder(String orderAsText){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String marshallOrder(Order order){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String generateFileName(LocalDate orderDate){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
