/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.view;

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
public class FlooringView {

    private UserIO io;
    
    public FlooringView(UserIO io){
        this.io = io;
    }
    
    /**
     * Displays Main Menu of Flooring Program and prompts user to select
     * a menu option
     * @return - Integer representing menu selection
     */
    public int displayMainMenuAndGetSelection(){
        io.printWithBanner("Flooring Program");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Quit");
        io.print("");
        return io.readInt("Select from the above options:", 1, 5);
    }
    
    /**
     * Displays Available Products
     * @param availableProducts - List of Products
     */
    public void displayAvailableProducts(List<Product> availableProducts){
        io.printWithBanner("Available Products");
        availableProducts.forEach((product) -> io.print("- " + product.toString()));
        io.print("");
    }
    
    /**
     * Displays Available States
     * @param availableStates - List of StateTax objects
     */
    public void displayAvailableStates(List<StateTax> availableStates){
        io.printWithBanner("Available States");
        String stateListStr = "";
        for (StateTax stateTax : availableStates){
            stateListStr += stateTax.getStateAbbreviation() + ", ";
        }
        stateListStr = stateListStr.substring(0, stateListStr.length()-2);
        io.print(stateListStr);
        io.print("");
    }
    
    /**
     * Displays summary of order
     * @param order - Order object
     */
    public void displayOrderSummary(Order order){
        io.printWithBanner("Order Summary");
        io.print(order.toString());
        io.print("");
    }
    
    public void displayDisplayOrderBanner(){
        io.printWithBanner("Display Order");
    }
    
    public void displayAddOrderBanner(){
        io.printWithBanner("Add Order");
    }
    
    public void displayAddOrderSuccessBanner(Order order){
        io.printWithBanner("Order " + order.getOrderNumber() + " Successfully Added");
    }
    
    public void displayCancelAddOrderSuccessBanner(){
        io.printWithBanner("Order Cancelled");
    }
    
    public void displayEditOrderBanner(){
        io.printWithBanner("Edit Order");
    }
    
    public void displayEditOrderSuccessBanner(){
        io.printWithBanner("Order Successfully Edited");
    }
    
    public void displayCancelEditOrderSuccessBanner(){
        io.printWithBanner("No changes made");
    }
    
    public void displayRemoveOrderBanner(){
        io.printWithBanner("Remove Order");
    }
    
    public void displayRemoveOrderSuccessBanner(){
        io.printWithBanner("Order Successfully Removed");
    }
    
    public void displayCancelRemoveOrderBanner(){
        io.printWithBanner("Cancel Order Removal");
    }
    
    public void displayErrorMessageAndWait(String errorMsg){
        io.printWithBanner("Error");
        io.print(errorMsg);
        io.pressEnterToContinue();
    }
    
    public void displayExitMessage(){
        io.print("Exiting...");
        io.print("");
    }
    
    public int promptOrderNumber(){
        return io.readInt("Enter Order Number:");
    }
    
    public LocalDate promptOrderDate(){
        return io.readDate("Enter Future Order Date: ", LocalDate.now());
    }
    
    public String promptCustomerName(){
        return io.readString("Enter Customer Name: ");
    }
    
    public String promptCustomerName(String currentName){
        return io.readString("Enter Customer Name (" + currentName + "): ");
    }
    
    public String promptState(){
        return io.readString("Enter State: ");
    }
    
    public String promptState(String currentStateAbbreviation){
        return io.readString("Enter State (" + currentStateAbbreviation + "): ");
    }
    
    public String promptProductType(){
        return io.readString("Enter Product: ");
    }
    
    public String promptProductType(String currentProduct){
        return io.readString("Enter Product (" + currentProduct + "): ");
    }
    
    public BigDecimal promptArea(BigDecimal minArea){
        return io.readBigDecimal("Enter Area (minimum "+minArea.toString()+" ft²): ", minArea);
    }
    
    public BigDecimal promptArea(BigDecimal minArea, BigDecimal currentArea){
        return io.readBigDecimalOrEmpty("Enter Area (" + currentArea.toString() + " ft²): ", minArea);
    }
    
    public boolean promptSaveAllOrders(){
        return io.readYesOrNo("Save All Orders? (Y/N)");
    }
    
    public boolean confirmAddOrder(){
        return io.readYesOrNo("Confirm Add Order? (Y/N)");
    }
    
    public boolean confirmEditOrder(){
        return io.readYesOrNo("Confirm Order Edits? (Y/N)");
    }
    
    public boolean confirmRemoveOrder(){
        return io.readYesOrNo("Confirm Remove Order? (Y/N)");
    }
    
    public void displayInvalidSelection(){
        io.print("Invalid selection");
        io.print("");
    }
    
    public void displaySaveAllOrdersSuccessBanner(){
        io.print("Orders Saved");
        io.print("");
    }
    
    public void displayCancelSaveAllOrdersSuccessBanner(){
        io.print("No Added, Edited, or Removed Orders Were Saved");
        io.print("");
    }
}
