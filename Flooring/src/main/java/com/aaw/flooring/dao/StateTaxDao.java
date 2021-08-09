/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.StateTax;

/**
 *
 * @author Austin Wong
 */
public interface StateTaxDao {

    StateTax getStateTax(String stateAbbreviation);
    
    StateTax addStateTax(StateTax stateTax);
    
    StateTax removeStateTax(String stateAbbreviation);
    
    void loadStateTaxes() throws OrderPersistenceException;
    
}
