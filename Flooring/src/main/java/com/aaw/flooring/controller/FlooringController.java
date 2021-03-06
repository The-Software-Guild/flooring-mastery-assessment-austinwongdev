/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.controller;

import com.aaw.flooring.dao.OrderPersistenceException;
import com.aaw.flooring.model.Order;
import com.aaw.flooring.model.Product;
import com.aaw.flooring.model.StateTax;
import com.aaw.flooring.service.FlooringServiceLayer;
import com.aaw.flooring.dao.NoOrdersOnDateException;
import com.aaw.flooring.dao.OrderNotFoundException;
import com.aaw.flooring.view.FlooringView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Austin Wong
 */
public class FlooringController {
    
    private FlooringServiceLayer service;
    private FlooringView view;
    
    public FlooringController(FlooringServiceLayer serviceLayer, FlooringView view){
        this.service = serviceLayer;
        this.view = view;
    }
    
    /**
     * Runs Flooring program
     */
    public void run(){
        
        // Load files
        try{
            service.loadAllOrders();
            service.loadAllProducts();
            service.loadAllStateTaxes();
        } catch(OrderPersistenceException ex){
            view.displayErrorMessageAndWait(ex.getMessage());
            return;
        }
        
        // Main Menu
        boolean continueMainMenu = true;
        int mainMenuSelection;
        while (continueMainMenu){
            mainMenuSelection = view.displayMainMenuAndGetSelection();
            switch (mainMenuSelection){
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrders();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    saveAddedOrders();
                    continueMainMenu = false;
                    break;
                default:
                    view.displayInvalidSelectionMessage();
            }
        }
        
        // Exit
        view.displayExitMessage();
    }
    
    /**
     * Prompts user for date and displays all orders on that date
     */
    public void displayOrders(){
        view.displayDisplayOrderBanner();
        LocalDate orderDate = view.promptOrderDate();
        List<Order> ordersOnDate;
        try{
            ordersOnDate = service.getAllOrdersOnDate(orderDate);
        } catch(NoOrdersOnDateException ex){
            view.displayErrorMessageAndWait(ex.getMessage());
            return;
        }
        
        // Display one-line mini summaries of each order and pause
        for (Order order : ordersOnDate){
            view.displayMiniOrderSummary(order);
        }
        view.pressEnterToContinue();
        
        // Display full detailed summaries of each order and continue
        for (Order order : ordersOnDate){
            view.displayOrderSummary(order);
        }
    }
    
    /**
     * Prompts user for order fields, displays summary of order, and confirms
     * whether user would like to add order
     */
    private void addOrders(){
        view.displayAddOrderBanner();
        
        // Prompt for order fields
        Product product = getAvailableProduct();
        StateTax stateTax = getStateTax();
        LocalDate orderDate = view.promptFutureOrderDate();
        String customerName;
        do{
            customerName = view.promptCustomerName();
        } while (!service.isValidCustomerName(customerName));
        BigDecimal area = view.promptArea(service.getMinimumArea());
        
        // Create new Order object
        Order order = service.createOrder(orderDate, customerName, stateTax, product, area);
        
        // Display Order Summary
        view.displayOrderSummary(order);
        
        // Confirm Order
        boolean confirmOrder = view.confirmAddOrder();
        if (confirmOrder){
            service.addOrder(order);
            view.displayAddOrderSuccessMessage(order);
        }
        else{
            view.displayCancelAddOrderSuccessMessage();
        }
    }
    
    /**
     * Prompts user for order number and date. If an order is found, prompts
     * user for fields to edit order, displays summary, and asks to confirm edits
     * before saving order.
     */
    private void editOrder(){
        view.displayEditOrderBanner();
        
        // Prompt order number and date, then find order
        Order order;
        try{
            order = findOrder();
        } catch (OrderNotFoundException | NoOrdersOnDateException ex){
            return;
        }
        
        // Prompt for editable fields
        String oldCustomerName = order.getCustomerName();
        String newCustomerName = getNewCustomerName(oldCustomerName);
        StateTax oldStateTax = order.getStateTax();
        StateTax newStateTax = getNewStateTax(oldStateTax);
        Product oldProduct = order.getProduct();
        Product newProduct = getNewOrderProduct(oldProduct);
        BigDecimal oldArea = order.getArea();
        BigDecimal newArea = view.promptArea(service.getMinimumArea(), oldArea);
        if (newArea == null){
            newArea = oldArea;
        }
        
        // Return early if no changes
        if (service.editOrder(order, newCustomerName, newStateTax, newProduct, newArea) == null){
            view.displayCancelEditOrderSuccessMessage();
            return;
        }
        
        // Display Summary
        service.calculateOrder(order);
        view.displayOrderSummary(order);
        
        // Confirm Edits and Save
        if (view.confirmEditOrder()){
            try{
                service.saveOrder(order.getOrderDate());
                view.displayEditOrderSuccessMessage();
            } catch(NoOrdersOnDateException | OrderPersistenceException ex){
                service.editOrder(order, oldCustomerName, oldStateTax, oldProduct, oldArea);
                view.displayErrorMessageAndWait(ex.getMessage());
            }
        }
        // Otherwise, revert changes
        else{
            service.editOrder(order, oldCustomerName, oldStateTax, oldProduct, oldArea);
            service.calculateOrder(order);
            view.displayCancelEditOrderSuccessMessage();
        }
    }
    
    /**
     * Prompts user for date and order number. If a matching order is found,
     * displays order info to user and removes order if user confirms.
     */
    private void removeOrder(){
        view.displayRemoveOrderBanner();
        
        // Prompt order number and date, then find order
        Order order;
        try{
            order = findOrder();
        } catch (OrderNotFoundException | NoOrdersOnDateException ex){
            return;
        }
        
        // Display order
        view.displayOrderSummary(order);
        
        // Confirm removal and save
        if (view.confirmRemoveOrder()){
            try{
                service.removeOrder(order.getOrderNumber(), order.getOrderDate());
                service.saveOrder(order.getOrderDate());
                view.displayRemoveOrderSuccessMessage();
            } catch (NoOrdersOnDateException | 
                    OrderNotFoundException | 
                    OrderPersistenceException ex){
                view.displayErrorMessageAndWait(ex.getMessage());
            }
        }
        // Otherwise don't remove order
        else{
            view.displayCancelRemoveOrderMessage();
        }
    }
    
    /**
     * Saves added orders to file if user provides confirmation.
     */
    private void saveAddedOrders(){
        if (view.promptSaveAddedOrders()){
            try{
                service.saveAllOrders();
            } catch (NoOrdersOnDateException | OrderPersistenceException ex){
                view.displayErrorMessageAndWait(ex.getMessage());
                return;
            }
            view.displaySaveAddedOrdersSuccessMessage();
        }
        else{
            view.displayCancelSaveAllOrdersSuccessMessage();
        }
    }
    
    /**
     * Helper function that prompts user to enter a date and order number,
     * then searches for a matching order.
     * @return - Order that matches user-entered date and order number
     * @throws OrderNotFoundException 
     */
    private Order findOrder() throws OrderNotFoundException, NoOrdersOnDateException{
        LocalDate orderDate = view.promptOrderDate();
        int orderNumber = view.promptOrderNumber();
        Order order;
        try{
            order = service.getOrder(orderNumber, orderDate);
        } catch(OrderNotFoundException | NoOrdersOnDateException ex){
            view.displayErrorMessageAndWait(ex.getMessage());
            throw new OrderNotFoundException(ex.getMessage(), ex.getCause());
        }
        return order;
    }
    
    /**
     * Helper function that prompts user to enter a new customer name
     * @param currentCustomerName - Order's current customer name
     * @return String of new customer name or original customer name user
     * enters nothing
     */
    private String getNewCustomerName(String currentCustomerName){
        String newCustomerName;
        do{
            newCustomerName = view.promptCustomerName(currentCustomerName);
        } while (!service.isValidCustomerName(newCustomerName) && !newCustomerName.isEmpty());
        if (newCustomerName.isEmpty()){
            return currentCustomerName;
        }
        return newCustomerName;
    }
    
    /**
     * Helper function that displays available products and prompts user to
     * select a valid product
     * @return - Product object that user selected
     */
    private Product getAvailableProduct(){
        Set<String> productNameSet = service.getAllProducts().stream()
                .map((product) -> product.getProductType())
                .collect(Collectors.toSet());
        String productType;
        do{
            view.displayAvailableProducts(service.getAllProducts());
            productType = view.promptProductType();
        } while (!productNameSet.contains(productType));
        return service.getProduct(productType);
    }
    
    /**
     * Helper function that displays available products and order's current product
     * and prompts user to select a valid product
     * @param currentProduct - Order's current Product object
     * @return - Product object that user selected or original Product if user
     * enters nothing
     */
    private Product getNewOrderProduct(Product currentProduct){
        Set<String> productNameSet = service.getAllProducts().stream()
                .map((product) -> product.getProductType())
                .collect(Collectors.toSet());
        String productType;
        do{
            view.displayAvailableProducts(service.getAllProducts());
            productType = view.promptProductType(currentProduct.getProductType());
        } while (!productNameSet.contains(productType) && !productType.isEmpty());
        if (productType.isEmpty()){
            return currentProduct;
        }
        return service.getProduct(productType);
    }
    
    /**
     * Helper function that displays available states and prompts user to
     * select a valid state
     * @return - StateTax object based on state user selected
     */
    private StateTax getStateTax(){
        Set<String> stateAbbreviationSet = service.getAllStateTaxes().stream()
                .map((stateTax) -> stateTax.getStateAbbreviation())
                .collect(Collectors.toSet());
        String stateAbbreviation;
        do{
            view.displayAvailableStates(service.getAllStateTaxes());
            stateAbbreviation = view.promptState();
        } while (!stateAbbreviationSet.contains(stateAbbreviation));
        return service.getStateTax(stateAbbreviation);
    }
    
    /**
     * Helper function that displays available states and order's current
     * state and prompts user to select a valid state to replace current state
     * @param - Order's current StateTax object
     * @return - StateTax object based on state user selected or the original
     * StateTax object if user enters nothing
     */
    private StateTax getNewStateTax(StateTax currentStateTax){
        Set<String> stateAbbreviationSet = service.getAllStateTaxes().stream()
                .map((stateTax) -> stateTax.getStateAbbreviation())
                .collect(Collectors.toSet());
        String stateAbbreviation;
        do{
            view.displayAvailableStates(service.getAllStateTaxes());
            stateAbbreviation = view.promptState(currentStateTax.getStateAbbreviation());
        } while (!stateAbbreviationSet.contains(stateAbbreviation) && !stateAbbreviation.isEmpty());
        if (stateAbbreviation.isEmpty()){
            return currentStateTax;
        }
        return service.getStateTax(stateAbbreviation);
    }
    
}
