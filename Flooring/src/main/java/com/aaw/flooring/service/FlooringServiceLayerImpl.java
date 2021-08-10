/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.service;

import com.aaw.flooring.dao.OrderDao;
import com.aaw.flooring.dao.OrderPersistenceException;
import com.aaw.flooring.dao.ProductDao;
import com.aaw.flooring.dao.StateTaxDao;
import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public class FlooringServiceLayerImpl implements FlooringServiceLayer {
    
    private OrderDao orderDao;
    private ProductDao productDao;
    private StateTaxDao stateTaxDao;
    
    public FlooringServiceLayerImpl(OrderDao orderDao, ProductDao productDao, StateTaxDao stateTaxDao){
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.stateTaxDao = stateTaxDao;
    }
    
    /**
     * Returns a list of Orders on a given date
     * @param orderDate - LocalDate of order fulfillment date
     * @return - List of Order objects on given date
     * @throws NoOrdersOnDateException 
     */
    @Override
    public List<Order> getAllOrdersOnDate(LocalDate orderDate) throws NoOrdersOnDateException {
        return orderDao.getAllOrdersOnDate(orderDate);
    }

    /**
     * Saves new and edited orders to file.
     * @throws NoOrdersOnDateException
     * @throws OrderPersistenceException 
     */
    @Override
    public void saveOrders() throws NoOrdersOnDateException, OrderPersistenceException {
        orderDao.saveAllOrders();
    }

    /**
     * Retrieves an Order object based on order number and order date
     * @param orderNumber - Integer representing order number
     * @param orderDate - LocalDate representing order fulfillment date
     * @return - Order object
     * @throws OrderNotFoundException 
     */
    @Override
    public Order getOrder(int orderNumber, LocalDate orderDate) throws OrderNotFoundException {
        Order order = orderDao.getOrder(orderNumber, orderDate);
        if (order == null){
            throw new OrderNotFoundException("Could not find order");
        }
        return order;
    }

    /**
     * Creates a new Order object, calculates its costs, 
     * and returns newly created Order object.
     * @param orderDate - LocalDate representing order fulfillment date
     * @param customerName - String of customer's name
     * @param stateTax - StateTax object for order
     * @param product - Product for order
     * @param area - BigDecimal representing square feet of flooring area
     * @return - Newly created Order object
     */
    @Override
    public Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax, Product product, BigDecimal area) {
        area = area.setScale(2, RoundingMode.HALF_UP);
        Order newOrder = orderDao.createOrder(orderDate, customerName, stateTax, product, area);
        calculateOrder(newOrder);
        return newOrder;
    }
    
    /**
     * Adds order to memory.
     * @param order - Order object
     * @return - Order object of added order
     */
    @Override
    public Order addOrder(Order order){
        return orderDao.addOrder(order);
    }

    /**
     * Edits an existing order
     * @param orderToEdit - Order object to edit
     * @param newCustomerName - String representing new customer name on order
     * @param newStateTax - New StateTax object for order
     * @param newProduct - New Product object for order
     * @param newArea - BigInteger representing new flooring area in square feet
     * @return - Edited order object
     */
    @Override
    public Order editOrder(Order orderToEdit, String newCustomerName, StateTax newStateTax, Product newProduct, BigDecimal newArea) {
        return orderDao.editOrder(orderToEdit, newCustomerName, newStateTax, newProduct, newArea);
    }
    
    /**
     * Removes an order from memory
     * @param orderNumber - Integer representing the order number
     * @param orderDate - LocalDate representing the order fulfillment date
     * @return - Removed Order object
     */
    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        return orderDao.removeOrder(orderNumber, orderDate);
    }

    /**
     * Returns a list of all Products in memory
     * @return - List of Product objects
     */
    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    /**
     * Returns a list of all StateTaxes in memory
     * @return - List of StateTax objects
     */
    @Override
    public List<StateTax> getAllStateTaxes() {
        return stateTaxDao.getAllStateTaxes();
    }

    /**
     * Calculates material cost, labor cost, tax, and total for a given order.
     * Rounds to 2 decimal places using HALF_UP rounding.
     * @param order - Order object to calculate costs
     * @return - Order object after updating calculated fields
     */
    @Override
    public Order calculateOrder(Order order) {
        
        // Get necessary objects for calculation
        BigDecimal area = order.getArea();
        Product product = order.getProduct();
        StateTax stateTax = order.getStateTax();
        
        // Calculate costs
        BigDecimal materialCost = area.multiply(product.getCostPerSquareFoot());
        materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCost = area.multiply(product.getLaborCostPerSquareFoot());
        laborCost = laborCost.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal tax = (materialCost.add(laborCost)).multiply((stateTax.getTaxRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)));
        tax = tax.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal total = materialCost.add(laborCost).add(tax);
        total = total.setScale(2, RoundingMode.HALF_UP);
        
        // Set costs in order object
        order.setLaborCost(laborCost);
        order.setMaterialCost(materialCost);
        order.setTax(tax);
        order.setTotal(total);
        
        return order;
    }

    /**
     * Loads all orders from file into memory
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadAllOrders() throws OrderPersistenceException{
        orderDao.loadAllOrders();
    }
    
    /**
     * Loads all products from file into memory
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadAllProducts() throws OrderPersistenceException{
        productDao.loadProducts();
    }
    
    /**
     * Loads all state taxes from file into memory
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadAllStateTaxes() throws OrderPersistenceException{
        stateTaxDao.loadStateTaxes();
    }

    /**
     * Returns a Product object given a product name
     * @param productType - String of product name
     * @return - Product object
     */
    @Override
    public Product getProduct(String productType) {
        return productDao.getProduct(productType);
    }

    /**
     * Returns a StateTax object given a state abbreviation
     * @param stateAbbreviation - String of state abbreviation
     * @return - StateTax object
     */
    @Override
    public StateTax getStateTax(String stateAbbreviation) {
        return stateTaxDao.getStateTax(stateAbbreviation);
    }
    
    /**
     * Returns minimum flooring area required for orders
     * @return - BigDecimal of minimum flooring area in square feet
     */
    @Override
    public BigDecimal getMinimumArea(){
        return new BigDecimal("100.00");
    }

    /**
     * Determines whether a String is a valid customer name defined by the 
     * following rules: May not be blank and may contain alphanumeric characters,
     * periods, and commas.
     * @return - True if valid, False if invalid
     */
    @Override
    public boolean isValidCustomerName(String customerName){
        if (customerName.isBlank() || customerName.isEmpty()){
            return false;
        }
        return customerName.matches("^[a-zA-Z0-9,. ]+$");
    }
}
