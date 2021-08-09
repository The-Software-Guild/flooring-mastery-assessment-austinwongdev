/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.flooring.dao;

import com.aaw.flooring.model.Order;
import com.aaw.flooring.service.NoOrdersOnDateException;
import com.aaw.flooring.service.OrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Austin Wong
 */
public class OrderDaoFileImplTest {
    
    private OrderDao testOrderDao;
    
    public OrderDaoFileImplTest() {
        this.testOrderDao = new OrderDaoFileImpl("src/test/resources/TestOrders/");
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
    public void testAddGetOrder() throws OrderNotFoundException{
        int orderNumber = 1;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        Order expectedOrder = new Order(orderNumber,"Ada Lovelace", "CA", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        testOrderDao.addOrder(expectedOrder, orderDate);
        
        Order returnedOrder = testOrderDao.getOrder(orderNumber, orderDate);
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Ada's order #1 on 6/1/2013");
    }
    
    @Test
    public void testGetOrderOrderNotFound() throws OrderNotFoundException{
        int orderNumber = 4;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        
        Order returnedOrder = testOrderDao.getOrder(orderNumber, orderDate);
        
        assertNull(returnedOrder, "Order should be null.");
    }
    
    @Test
    public void testCreateOrder(){
        int orderNumber = 2;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        Order expectedOrder = new Order(orderNumber,"Rick Moranis", "CA", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        Order returnedOrder = testOrderDao.createOrder(orderDate,"Rick Moranis", "CA", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Rick's order #2 on 6/1/2013");
    }
    
    @Test
    public void testEditOrderNoChanges(){
        int orderNumber = 1;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        String newCustomerName = "Ada Lovelace";
        String newState = "CA";
        String newProductType = "Tile";
        BigDecimal newArea = new BigDecimal("249.00");
        
        Order returnedOrder = testOrderDao.editOrder(orderNumber, orderDate, newCustomerName, newState, newProductType, newArea);
        assertNull(returnedOrder, "Should return null");
    }
    
    @Test
    public void testEditOrderWithChanges(){
        int orderNumber = 1;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        String newCustomerName = "Ada Lovelace III";
        String newState = "CA";
        String newProductType = "Wood";
        BigDecimal newArea = new BigDecimal("249.00");
        
        Order expectedOrder = new Order(orderNumber, newCustomerName, "OR", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        Order returnedOrder = testOrderDao.editOrder(orderNumber, orderDate, newCustomerName, newState, newProductType, newArea);
        assertNotNull(returnedOrder, "Should return an order");
        assertEquals(expectedOrder, returnedOrder, "Customer name and product should have changed.");
        
        // Revert changes
        testOrderDao.editOrder(orderNumber, orderDate, "Ada Lovelace", newState, "Tile", newArea);
    }
    
    @Test
    public void testRemoveOrder(){
        int orderNumber = 1;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        Order expectedOrder = new Order(orderNumber,"Ada Lovelace", "CA", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        Order returnedOrder = testOrderDao.removeOrder(orderNumber, orderDate);
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Ada's order #1 on 6/1/2013");
        
        assertThrows(OrderNotFoundException.class,
                () -> testOrderDao.getOrder(orderNumber, orderDate),
                "Should throw Order Not Found exception");
    }
    
    @Test
    public void testSaveLoadOrder() throws OrderPersistenceException{
        int orderNumber = 4;
        LocalDate orderDate = LocalDate.parse("2013-06-02");
        Order expectedOrder = new Order(orderNumber,"Rick Moranis", "CA", 
                new BigDecimal("25.00"), "Tile", new BigDecimal("249.00"), 
                new BigDecimal("3.50"), new BigDecimal("4.15"), 
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        List<Order> ordersOnDateBefore = testOrderDao.getAllOrdersOnDate(orderDate);
        
        testOrderDao.addOrder(expectedOrder, orderDate);
        testOrderDao.saveOrder(orderDate);
        testOrderDao.loadAllOrders();
        List<Order> ordersOnDateAfter = testOrderDao.getAllOrdersOnDate(orderDate);
        
        assertFalse(ordersOnDateBefore.contains(expectedOrder), "Order #4 should not be in list yet.");
        assertTrue(ordersOnDateAfter.contains(expectedOrder), "Order #4 should be in list now.");
    }
    
    @Test
    public void testGetAllOrdersOnDateFileNotFound(){
        LocalDate nonExistentOrderDate = LocalDate.parse("1999-01-01");
        
        assertThrows(NoOrdersOnDateException.class,
                () -> testOrderDao.getAllOrdersOnDate(nonExistentOrderDate),
                "Should throw NoOrdersOnDateException");
    }
    
    @Test
    public void testGetNextAvailableOrderNumber() throws OrderPersistenceException{
        int expectedOrderNumber = 4;
        
        testOrderDao.loadAllOrders();
        int returnedOrderNumber = testOrderDao.getNextAvailableOrderNumber();
        
        assertEquals(expectedOrderNumber, returnedOrderNumber, "Order # should equal 4");
    }
    
}
