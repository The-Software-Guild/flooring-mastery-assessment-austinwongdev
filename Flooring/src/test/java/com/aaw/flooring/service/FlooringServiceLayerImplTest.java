/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.flooring.service;

import com.aaw.flooring.dao.OrderDaoFileImpl;
import com.aaw.flooring.dao.OrderPersistenceException;
import com.aaw.flooring.dao.ProductDaoFileImpl;
import com.aaw.flooring.dao.StateTaxDaoFileImpl;
import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Austin Wong
 */
public class FlooringServiceLayerImplTest {
    
    private FlooringServiceLayer testService;
    
    public FlooringServiceLayerImplTest() {
        this.testService = new FlooringServiceLayerImpl(
                new OrderDaoFileImpl("src/test/resources/TestOrders/"), 
                new ProductDaoFileImpl("src/test/resources/TestData/Products.txt"), 
                new StateTaxDaoFileImpl("src/test/resources/TestData/Taxes.txt"));
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
    public void testLoadAllOrdersOnDateExistent() throws NoOrdersOnDateException, OrderPersistenceException{
        LocalDate orderDate = LocalDate.parse("2013-06-02");
        int expectedNumOrders = 2;
        
        testService.loadAllOrders();
        List<Order> ordersOnDate = testService.getAllOrdersOnDate(orderDate);
        
        assertEquals(expectedNumOrders, ordersOnDate.size(), 
                "Should have loaded 2 orders from 6/2/2013");
    }
    
    @Test
    public void testLoadAllOrdersOnDateNonExistent(){
        LocalDate orderDate = LocalDate.parse("1990-01-01");
        
        assertThrows(NoOrdersOnDateException.class,
                () -> testService.getAllOrdersOnDate(orderDate),
                "Should throw NoOrdersOnDateException");
    }
    
    @Test
    public void testSaveAddedOrders() throws IOException, OrderNotFoundException, OrderPersistenceException, NoOrdersOnDateException{
        // Clear file
        new FileWriter("src/test/resources/TestOrders/Orders_06042013.txt");
        // Add order
        Product product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-04");
        Order expectedOrder = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, new BigDecimal("100.00"));
        testService.addOrder(expectedOrder);
        
        // Save and load order
        testService.saveOrders();
        testService.loadAllOrders();
        Order returnedOrder = testService.getOrder(expectedOrder.getOrderNumber(), order1Date);
        
        assertNotNull(returnedOrder, "Returned order should not be null");
        assertEquals(expectedOrder, returnedOrder, "Saved order should be same as loaded order");
    }
    
    @Test
    public void testGetOrderNonExistent(){
        int nonExistentOrderId = 25;
        LocalDate nonExistentOrderDate = LocalDate.parse("1990-01-01");
        
        assertThrows(OrderNotFoundException.class,
                () -> testService.getOrder(nonExistentOrderId, nonExistentOrderDate),
                "Should throw OrderNotFoundException");
    }
    
    @Test
    public void testCreateGetOrder() throws OrderNotFoundException{
        Product product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        
        Order expectedOrder = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, new BigDecimal("100.00"));
        testService.addOrder(expectedOrder);
        Order returnedOrder = testService.getOrder(expectedOrder.getOrderNumber(), order1Date);
        
        assertNotNull(returnedOrder, "Returned order should not be null");
        assertEquals(expectedOrder, returnedOrder, "Returned order should be same as created order");
    }
    
    @Test
    public void testEditOrderNoChange(){
        Product product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        
        Order order1 = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, new BigDecimal("100.00"));
        testService.addOrder(order1);
        Order returnedOrder = testService.editOrder(order1, "Rick Moranis", stateTax1, product1, new BigDecimal("100.00"));
        
        assertNull(returnedOrder, "Returned order should be null");
    }
    
    @Test
    public void testEditOrderChange(){
        Product product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        
        String newName = "Rick Moranis III";
        StateTax newStateTax = new StateTax("OR", "Oregon", new BigDecimal("100.00"));
        Product newProduct = new Product("Wood", new BigDecimal("2.00"), new BigDecimal("2.00"));
        BigDecimal newArea = new BigDecimal("300.00");
        
        Order order1 = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, new BigDecimal("100.00"));
        testService.addOrder(order1);
        Order returnedOrder = testService.editOrder(order1, newName, newStateTax, newProduct, newArea);
        
        assertNotNull(returnedOrder, "Returned order should not be null");
        assertEquals(newArea, returnedOrder.getArea(), "Area should be 300.00");
        assertEquals(newProduct, returnedOrder.getProduct(), "Product should be Wood");
        assertEquals(newStateTax, returnedOrder.getStateTax(), "State Tax should be OR");
        assertEquals(newName, returnedOrder.getCustomerName(), "Customer name should be Rick Moranis III");
    }
    
    @Test
    public void testRemoveOrderExistent(){
        Product product1 = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("25.00"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        
        Order order1 = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, new BigDecimal("100.00"));
        testService.addOrder(order1);
        Order removedOrder = testService.removeOrder(order1.getOrderNumber(), order1Date);
        
        assertNotNull(removedOrder, "Should return an Order");
        assertEquals(order1, removedOrder, "Removed order should be same as created order.");
        assertThrows(OrderNotFoundException.class,
                () -> testService.getOrder(order1.getOrderNumber(), order1Date),
                "Should throw OrderNotFoundException");
    }
    
    @Test
    public void testRemoveOrderNonExistent(){
        int nonExistentOrderId = 25;
        LocalDate nonExistentOrderDate = LocalDate.parse("1990-01-01");
        
        assertThrows(OrderNotFoundException.class,
                () -> testService.getOrder(nonExistentOrderId, nonExistentOrderDate),
                "Should throw OrderNotFoundException");
    }
    
    @Test
    public void testGetAllProducts() throws OrderPersistenceException{
        List<Product> expectedProducts = new ArrayList<>();
        Product carpetProduct = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        Product laminateProduct = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        expectedProducts.add(carpetProduct);
        expectedProducts.add(laminateProduct);
        
        testService.loadAllProducts();
        List<Product> returnedProducts = testService.getAllProducts();
        
        assertNotNull(returnedProducts, "Should return 2 products");
        assertEquals(expectedProducts, returnedProducts, "Should return list of products with carpet and laminate");
    }
    
    
    @Test
    public void testGetAllStateTaxes() throws OrderPersistenceException{
        List<StateTax> expectedStateTaxes = new ArrayList<>();
        StateTax texas = new StateTax("TX", "Texas", new BigDecimal("4.45"));
        StateTax washington = new StateTax("WA", "Washington", new BigDecimal("9.25"));
        expectedStateTaxes.add(texas);
        expectedStateTaxes.add(washington);
        
        testService.loadAllStateTaxes();
        List<StateTax> returnedStateTaxes = testService.getAllStateTaxes();
        
        assertNotNull(returnedStateTaxes, "Should return 2 state taxes");
        assertEquals(expectedStateTaxes, returnedStateTaxes, "Should return list of state taxes with texas and washington");
    }
    
    @Test
    public void testCalculateOrder(){
        Product product1 = new Product("Tile", new BigDecimal("5.15"), new BigDecimal("4.75"));
        StateTax stateTax1 = new StateTax("CA", "California", new BigDecimal("9.25"));
        LocalDate order1Date = LocalDate.parse("2013-06-01");
        BigDecimal area = new BigDecimal("243.00");
        Order order1 = testService.createOrder(order1Date, "Rick Moranis", 
                stateTax1, product1, area);
        testService.addOrder(order1);
        
        BigDecimal expectedMaterialCost = new BigDecimal("1251.45");
        BigDecimal expectedLaborCost = new BigDecimal("1154.25");
        BigDecimal expectedTax = new BigDecimal("216.51");
        BigDecimal expectedTotal = new BigDecimal("2622.21");
        
        testService.calculateOrder(order1);
        
        assertEquals(expectedMaterialCost, order1.getMaterialCost(), "Material Cost should be 1251.45");
        assertEquals(expectedLaborCost, order1.getLaborCost(), "Labor Cost should be 1154.25");
        assertEquals(expectedTax, order1.getTax(), "Tax should be 216.51");
        assertEquals(expectedTotal, order1.getTotal(), "Total should be 2622.21");
    }
}
