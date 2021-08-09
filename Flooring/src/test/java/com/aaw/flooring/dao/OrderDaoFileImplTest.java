/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.flooring.dao;

import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import com.aaw.flooring.service.NoOrdersOnDateException;
import com.aaw.flooring.service.OrderNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    private Order order1;
    private Product product1;
    private StateTax stateTax1;
    private final String TEST_FILE_PATH = "src/test/resources/TestOrders/";
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        int orderNumber = 1;
        this.product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        this.stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        this.order1 = new Order(order1Date, orderNumber,"Ada Lovelace", stateTax1, 
                product1, new BigDecimal("249.00"),
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        this.testOrderDao = new OrderDaoFileImpl(TEST_FILE_PATH);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddGetOrder() throws OrderNotFoundException{
        Order expectedOrder = this.order1;
        
        testOrderDao.addOrder(expectedOrder);
        Order returnedOrder = testOrderDao.getOrder(expectedOrder.getOrderNumber(), 
                expectedOrder.getOrderDate());
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Ada's order #1 on 6/1/2013");
    }
    
    @Test
    public void testGetOrderOrderNotFound() throws OrderNotFoundException{
        int orderNumber = 4;
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        
        assertThrows(OrderNotFoundException.class,
                () -> testOrderDao.getOrder(orderNumber, orderDate),
                "Should throw OrderNotFoundException");
    }
    
    @Test
    public void testCreateOrder(){
        int orderNumber = 1;
        
        LocalDate orderDate = LocalDate.parse("2013-06-01");
        Order expectedOrder = new Order(orderDate, orderNumber,"Rick Moranis", 
                this.stateTax1, this.product1, new BigDecimal("249.00"),
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        Order returnedOrder = testOrderDao.createOrder(orderDate,"Rick Moranis", 
                this.stateTax1, this.product1, new BigDecimal("249.00"),
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Rick's order #4 on 6/1/2013");
    }
    
    @Test
    public void testEditOrderNoChanges(){
        Order orderToEdit = this.order1;
        testOrderDao.addOrder(order1);
        
        Order returnedOrder = testOrderDao.editOrder(orderToEdit, 
                orderToEdit.getCustomerName(), orderToEdit.getStateTax(), 
                orderToEdit.getProduct(), orderToEdit.getArea());
        
        assertNull(returnedOrder, "Should return null");
    }
    
    @Test
    public void testEditOrderWithChanges() throws OrderNotFoundException{
        Order orderToEdit = this.order1;
        testOrderDao.addOrder(order1);
        
        String newCustomerName = "Ada Lovelace III";
        Product newProduct = new Product("Wood", new BigDecimal("1.00"), new BigDecimal("1.00"));
        StateTax newStateTax = new StateTax("OR", "Oregon", new BigDecimal("1.00"));
        BigDecimal newArea = new BigDecimal("100.00");
        Order expectedOrder = new Order(orderToEdit.getOrderDate(), 
                orderToEdit.getOrderNumber(), newCustomerName, newStateTax, 
                newProduct, newArea, new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        
        Order returnedOrder = testOrderDao.editOrder(orderToEdit, newCustomerName, newStateTax, newProduct, newArea);
        
        assertNotNull(returnedOrder, "Should return an order");
        assertEquals(expectedOrder, returnedOrder, "Customer name and product should have changed.");
        
        returnedOrder = testOrderDao.getOrder(orderToEdit.getOrderNumber(), orderToEdit.getOrderDate());
        
        assertNotNull(returnedOrder, "Should return an order");
        assertEquals(expectedOrder, returnedOrder, "Edits should be saved");
    }
    
    @Test
    public void testRemoveOrder(){
        Order expectedOrder = this.order1;
        testOrderDao.addOrder(order1);
        int orderNumber = expectedOrder.getOrderNumber();
        LocalDate orderDate = expectedOrder.getOrderDate();
        
        Order returnedOrder = testOrderDao.removeOrder(orderNumber, orderDate);
        
        assertNotNull(returnedOrder, "Should return an order.");
        assertEquals(expectedOrder, returnedOrder, "Should return Ada's order #1 on 6/1/2013");
        
        assertThrows(OrderNotFoundException.class,
                () -> testOrderDao.getOrder(orderNumber, orderDate),
                "Should throw Order Not Found exception");
    }
    
    @Test
    public void testSaveLoadOrder() throws OrderPersistenceException, NoOrdersOnDateException, IOException{
        // Reset file
        new FileWriter(TEST_FILE_PATH + "Orders_06032013.txt");
        
        int orderNumber = 4;
        LocalDate orderDate = LocalDate.parse("2013-06-03");
        Order expectedOrder = new Order(orderDate, orderNumber,"Rick Moranis", 
                this.stateTax1, this.product1, new BigDecimal("249.00"),
                new BigDecimal("871.50"), new BigDecimal("1033.35"), 
                new BigDecimal("476.21"), new BigDecimal("2381.06"));
        assertThrows(NoOrdersOnDateException.class,
                () -> testOrderDao.getAllOrdersOnDate(orderDate),
                "Should throw NoOrdersOnDateException");
        
        testOrderDao.addOrder(expectedOrder);
        testOrderDao.saveOrder(orderDate);
        testOrderDao.loadAllOrders();
        List<Order> ordersOnDateAfter = testOrderDao.getAllOrdersOnDate(orderDate);
        
        assertTrue(ordersOnDateAfter.contains(expectedOrder), "Order #4 should be in list now.");
        
        // Clean-up
        new FileWriter(TEST_FILE_PATH + "Orders_06032013.txt");
    }
    
    @Test
    public void testGetAllOrdersOnDateFileNotFound(){
        LocalDate nonExistentOrderDate = LocalDate.parse("1999-01-01");
        
        assertThrows(NoOrdersOnDateException.class,
                () -> testOrderDao.getAllOrdersOnDate(nonExistentOrderDate),
                "Should throw NoOrdersOnDateException");
    }
    
    @Test
    public void testGetNextAvailableOrderNumber() throws OrderPersistenceException, IOException{
        // Ensure there are only 3 orders, not 4
        new FileWriter(TEST_FILE_PATH + "Orders_06032013.txt");
        
        int expectedOrderNumber = 4;
        
        testOrderDao.loadAllOrders();
        int returnedOrderNumber = testOrderDao.getNextAvailableOrderNumber();
        
        assertEquals(expectedOrderNumber, returnedOrderNumber, "Order # should equal 4");
    }
    
}
