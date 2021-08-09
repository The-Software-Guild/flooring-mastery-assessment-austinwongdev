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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Austin Wong
 */
public class OrderDaoFileImpl implements OrderDao {
    
    private final Map<LocalDate, Map<Integer, Order>> orderStore = new HashMap<>();
    private final String FIELD_DELIMITER = "::";
    private final String ORDER_FOLDER_PATH;
    private int nextAvailableOrderNumber = 1;

    public OrderDaoFileImpl(){
        this.ORDER_FOLDER_PATH = "src/main/resources/Orders/";
    }
    
    public OrderDaoFileImpl(String ORDER_FOLDER_PATH) {
        this.ORDER_FOLDER_PATH = ORDER_FOLDER_PATH;
    }
    
    /**
     * Takes in an order number and date, searches for an order that matches
     * those parameters, and returns that order.
     * @param orderNumber - Integer representing the unique order number
     * @param orderDate - LocalDate representing the date of order fulfillment, 
     * not the date the order was placed
     * @return - Order object
     * @throws OrderNotFoundException 
     */
    @Override
    public Order getOrder(int orderNumber, LocalDate orderDate) throws OrderNotFoundException {
        Map<Integer, Order> ordersOnDate = orderStore.get(orderDate);
        if (ordersOnDate == null || ordersOnDate.isEmpty()){
            throw new OrderNotFoundException("Order not found");
        }
        Order order = ordersOnDate.get(orderNumber);
        return order;
    }

    /**
     * Takes in a date and returns all orders that will be fulfilled on that date
     * @param orderDate - LocalDate representing the date of order fulfillment
     * @return List of Order objects, sorted by order #
     */
    @Override
    public List<Order> getAllOrdersOnDate(LocalDate orderDate) throws NoOrdersOnDateException {
        Map<Integer, Order> ordersOnDate = orderStore.get(orderDate);
        if (ordersOnDate == null || ordersOnDate.isEmpty()){
            throw new NoOrdersOnDateException("No orders found on that date");
        }
        return ordersOnDate.values().stream()
                .sorted(Comparator.comparingInt(Order::getOrderNumber))
                .collect(Collectors.toList());
    }

    /**
     * Takes in an order and order date and adds that order to in-memory store
     * @param order - Order object to be added
     * @return - Order object previously set for that order number, or 
     * null if no object was previously set
     */
    @Override
    public Order addOrder(Order order) {
        LocalDate orderDate = order.getOrderDate();
        Map<Integer, Order> ordersOnDate = orderStore.get(orderDate);
        if (ordersOnDate == null){
            ordersOnDate = new HashMap<>();
        }
        order = ordersOnDate.put(order.getOrderNumber(), order);
        orderStore.put(orderDate, ordersOnDate);
        return order;
    }
    
    @Override
    public Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax,
            Product product, BigDecimal area){
        return new Order(orderDate, getNextAvailableOrderNumber(), customerName, stateTax, product, area);
    }
    
    /**
     * Creates and returns a new Order object
     * @param orderDate - Date order will be fulfilled
     * @param customerName - String name of customer
     * @param stateTax - StateTax object containing info on order's state and tax rate
     * @param product - Product object containing info on order's product and product costs
     * @param area - Flooring area in square feet
     * @param materialCost - Total cost of materials
     * @param laborCost - Total cost of labors
     * @param tax - Total tax
     * @param total - Total cost of order
     * @return - Order object
     */
    @Override
    public Order createOrder(LocalDate orderDate, String customerName, StateTax stateTax, Product product, 
            BigDecimal area, BigDecimal materialCost, BigDecimal laborCost, 
            BigDecimal tax, BigDecimal total){
        return new Order(orderDate, getNextAvailableOrderNumber(), customerName, stateTax, 
                product, area, materialCost, laborCost, tax, total);
    }

    /**
     * Edits order information and returns order if different from original order
     * @param orderToEdit - Order object to be edited
     * @param newCustomerName - String of new customer name value
     * @param newStateTax - New StateTax object
     * @param newProduct - New Product object
     * @param newArea - BigDecimal of new flooring area in square feet
     * @return - New order object if edits were made, null if input was the same
     * as object in memory
     */
    @Override
    public Order editOrder(Order orderToEdit, String newCustomerName, StateTax newStateTax, Product newProduct, BigDecimal newArea) {
        if (newCustomerName.equals(orderToEdit.getCustomerName()) &&
                newStateTax.equals(orderToEdit.getStateTax()) &&
                newProduct.equals(orderToEdit.getProduct()) &&
                newArea.equals(orderToEdit.getArea())){
            return null;
        }
        
        orderToEdit.setCustomerName(newCustomerName);
        orderToEdit.setStateTax(newStateTax);
        orderToEdit.setProduct(newProduct);
        orderToEdit.setArea(newArea);

        return orderToEdit;
    }

    /**
     * Removes an order based on order number and date
     * @param orderNumber - Integer representing order number
     * @param orderDate - LocalDate representing order fulfillment date
     * @return - Removed Order object if found, null otherwise
     */
    @Override
    public Order removeOrder(int orderNumber, LocalDate orderDate) {
        Map<Integer, Order> ordersOnDate = orderStore.get(orderDate);
        if (ordersOnDate == null || ordersOnDate.isEmpty()){
            return null;
        }
        return ordersOnDate.remove(orderNumber);
    }

    /**
     * Loads all order files in Orders folder to memory
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadAllOrders() throws OrderPersistenceException {
        
        // Get list of .txt files starting with "Orders_" in Order folder
        File directoryPath = new File(ORDER_FOLDER_PATH);
        FilenameFilter textFilefilter = (File dir, String name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".txt") &&
                    name.startsWith("Orders_");
        };
        String[] filesList = directoryPath.list(textFilefilter);
        
        // Load each file
        for (String file : filesList){
            loadOrder(file);
        }
        
    }
    
    
    private void loadOrder(String orderFileName) throws OrderPersistenceException{
        Scanner scanner;
        
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FOLDER_PATH + orderFileName)));
        }
        catch (FileNotFoundException e){
            throw new OrderPersistenceException(
                    "Could not load order data into memory.", e);
        }
        
        // Skip header
        if (scanner.hasNextLine()){
            scanner.nextLine();
        }
        
        // Don't store an empty hashmap to orderStore if file doesn't have data
        if (!scanner.hasNextLine()){
            return;
        }
        
        // Parse and store data
        String currentLine;
        Order currentOrder;
        Map<Integer, Order> ordersOnDate = new HashMap<>();
        LocalDate orderDate = LocalDate.parse(orderFileName.split("_")[1].replace(".txt", ""), 
                DateTimeFormatter.ofPattern("MMddyyyy"));
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine, orderDate);
            ordersOnDate.put(currentOrder.getOrderNumber(), currentOrder);
        }
        orderStore.put(orderDate, ordersOnDate);
        
        scanner.close();
    }
    
    /**
     * Takes a String and LocalDate and converts it to an Order object
     * @param orderAsText - String representation of Order object
     * @param orderDate - LocalDate of order fulfillment date
     * @return - Order object
     */
    private Order unmarshallOrder(String orderAsText, LocalDate orderDate){
        String[] orderFields = orderAsText.split(FIELD_DELIMITER);
        
        int orderNumber = Integer.parseInt(orderFields[0]);
        String customerName = orderFields[1];
        String stateAbbreviation = orderFields[2];
        BigDecimal taxRate = new BigDecimal(orderFields[3]);
        String productType = orderFields[4];
        BigDecimal area = new BigDecimal(orderFields[5]);
        BigDecimal costPerSquareFoot = new BigDecimal(orderFields[6]);
        BigDecimal laborCostPerSquareFoot  = new BigDecimal(orderFields[7]);
        BigDecimal materialCost = new BigDecimal(orderFields[8]);
        BigDecimal laborCost = new BigDecimal(orderFields[9]);
        BigDecimal tax = new BigDecimal(orderFields[10]);
        BigDecimal total = new BigDecimal(orderFields[11]);
        
        Product orderProduct = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
        StateTax orderStateTax = new StateTax(stateAbbreviation, taxRate);
        
        Order order = new Order(orderDate, orderNumber, customerName, 
                orderStateTax, orderProduct, area, materialCost, laborCost, tax, 
                total);
        
        return order;
    }
    
    /**
     * Takes in an Order object and converts it to a String for file storage
     * @param order - Order object
     * @return String representing Order object
     */
    private String marshallOrder(Order order){
        String orderAsText = order.getOrderNumber() + FIELD_DELIMITER;
        orderAsText += order.getCustomerName() + FIELD_DELIMITER;
        orderAsText += order.getStateTax().getStateAbbreviation() + FIELD_DELIMITER;
        orderAsText += order.getStateTax().getTaxRate() + FIELD_DELIMITER;
        orderAsText += order.getProduct().getProductType() + FIELD_DELIMITER;
        orderAsText += order.getArea() + FIELD_DELIMITER;
        orderAsText += order.getProduct().getCostPerSquareFoot() + FIELD_DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSquareFoot() + FIELD_DELIMITER;
        orderAsText += order.getMaterialCost() + FIELD_DELIMITER;
        orderAsText += order.getLaborCost() + FIELD_DELIMITER;
        orderAsText += order.getTax() + FIELD_DELIMITER;
        orderAsText += order.getTotal();
        
        return orderAsText;
    }
    
    /**
     * Takes in an order date and generates an order filepath.
     * @param orderDate - LocalDate representing order date
     * @return - String of order filepath
     */
    private String generateFilePath(LocalDate orderDate){
        String fileName = ORDER_FOLDER_PATH + "Orders_";
        fileName += orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        fileName += ".txt";
        return fileName;
    }

    /**
     * Takes in an order date and overwrites file with in-memory order data for 
     * that date.
     * @param orderDate - LocalDate of orders to save.
     * @throws OrderPersistenceException 
     */
    @Override
    public void saveOrder(LocalDate orderDate) throws NoOrdersOnDateException, OrderPersistenceException {
        List<Order> ordersOnDate = getAllOrdersOnDate(orderDate);
        PrintWriter out;
        
        try{
            out = new PrintWriter(new FileWriter(generateFilePath(orderDate)));
        } catch (IOException e){
            throw new OrderPersistenceException(
                    "Could not save order data.", e);
        }
        
        // Write header
        String header = "OrderNumber"+FIELD_DELIMITER+
                "CustomerName"+FIELD_DELIMITER+
                "State"+FIELD_DELIMITER+
                "TaxRate"+FIELD_DELIMITER+
                "ProductType"+FIELD_DELIMITER+
                "Area"+FIELD_DELIMITER+
                "CostPerSquareFoot"+FIELD_DELIMITER+
                "LaborCostPerSquareFoot"+FIELD_DELIMITER+
                "MaterialCost"+FIELD_DELIMITER+
                "LaborCost"+FIELD_DELIMITER+
                "Tax"+FIELD_DELIMITER+
                "Total";
        out.println(header);
        out.flush();
        
        // Write each order on new line
        ordersOnDate.stream()
                .map((order) -> marshallOrder(order))
                .forEach((orderStr) -> {
                    out.println(orderStr);
                    out.flush();
                });
        out.close();
    }

    /**
     * Saves all orders stored in memory
     * @throws NoOrdersOnDateException
     * @throws OrderPersistenceException 
     */
    @Override
    public void saveAllOrders() throws NoOrdersOnDateException, OrderPersistenceException {
        Set<LocalDate> orderDates = orderStore.keySet();
        for (LocalDate orderDate : orderDates){
            saveOrder(orderDate);
        }
    }

    /**
     * Iterates over all orders and returns the integer after the highest order
     * number
     * @return - Integer of the next available order number
     */
    @Override
    public int getNextAvailableOrderNumber() {
        int maxOrderNumber = 0;
        for (Map<Integer, Order> orderMap : orderStore.values()){
            int maxOrderOnDate = orderMap.values().stream()
                    .map((order) -> order.getOrderNumber())
                    .max(Comparator.naturalOrder()).get();
            if (maxOrderOnDate > maxOrderNumber){
                maxOrderNumber = maxOrderOnDate;
            }
        }
        return maxOrderNumber+1;
    }
}
