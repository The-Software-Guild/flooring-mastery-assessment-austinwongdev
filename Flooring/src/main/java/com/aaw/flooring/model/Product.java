/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Austin Wong
 */
public class Product {

    private final String productType;
    private final BigDecimal costPerSquareFoot;
    private final BigDecimal laborCostPerSquareFoot;
    
    public Product(String productType,
                   BigDecimal costPerSquareFoot,
                    BigDecimal laborCostPerSquareFoot){
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }
    
    public String getProductType(){
        return this.productType;
    }
    
    public BigDecimal getCostPerSquareFoot(){
        return this.costPerSquareFoot;
    }
    
    public BigDecimal laborCostPerSquareFoot(){
        return this.laborCostPerSquareFoot;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.productType);
        hash = 53 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 53 * hash + Objects.hashCode(this.laborCostPerSquareFoot);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSquareFoot, other.laborCostPerSquareFoot)) {
            return false;
        }
        return true;
    }
    
    
    
}
