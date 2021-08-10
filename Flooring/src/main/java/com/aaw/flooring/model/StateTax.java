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
public class StateTax {

    private final String stateAbbreviation;
    private String stateName;
    private final BigDecimal taxRate;
    
    public StateTax(String stateAbbreviation,
                    BigDecimal taxRate){
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
    }
    
    public StateTax(String stateAbbreviation, String stateName, BigDecimal taxRate){
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }
    
    public String getStateAbbreviation(){
        return this.stateAbbreviation;
    }
    
    public String getStateName(){
        return this.stateName;
    }
    
    public void setStateName(String stateName){
        this.stateName = stateName;
    }
    
    public BigDecimal getTaxRate(){
        return this.taxRate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.stateAbbreviation);
        hash = 67 * hash + Objects.hashCode(this.taxRate);
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
        final StateTax other = (StateTax) obj;
        if (!Objects.equals(this.stateAbbreviation, other.stateAbbreviation)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return stateAbbreviation + " (" + stateName + "): $" + taxRate;
    }
        
    
}
