/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Austin Wong
 */
public class Order {

    private final int orderNumber;
    private String customerName;
    private StateTax stateTax;
    private Product product;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private LocalDate orderDate;

    public Order(LocalDate orderDate, int orderNumber, String customerName, StateTax stateTax,
            Product product, BigDecimal area){
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.stateTax = stateTax;
        this.product = product;
        this.area = area;
    }
    
    public Order(LocalDate orderDate, int orderNumber, String customerName, StateTax stateTax, 
                 Product product, BigDecimal area, BigDecimal materialCost, 
                 BigDecimal laborCost, BigDecimal tax, 
                 BigDecimal total) {
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.stateTax = stateTax;
        this.product = product;
        this.area = area;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public StateTax getStateTax() {
        return stateTax;
    }

    public void setStateTax(StateTax stateTax) {
        this.stateTax = stateTax;
    }

    public Product getProduct(){
        return product;
    }
    
    public void setProduct(Product product){
        this.product = product;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public LocalDate getOrderDate(){
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate){
        this.orderDate = orderDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.orderNumber;
        hash = 71 * hash + Objects.hashCode(this.customerName);
        hash = 71 * hash + Objects.hashCode(this.stateTax);
        hash = 71 * hash + Objects.hashCode(this.product);
        hash = 71 * hash + Objects.hashCode(this.area);
        hash = 71 * hash + Objects.hashCode(this.materialCost);
        hash = 71 * hash + Objects.hashCode(this.laborCost);
        hash = 71 * hash + Objects.hashCode(this.tax);
        hash = 71 * hash + Objects.hashCode(this.total);
        hash = 71 * hash + Objects.hashCode(this.orderDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.stateTax, other.stateTax)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        return true;
    }
    
}
