/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.StateTax;
import java.util.List;

/**
 *
 * @author Austin Wong
 */
public interface StateTaxDao {

    StateTax getStateTax(String stateAbbreviation);
    
    List<StateTax> getAllStateTaxes();
    
    StateTax addStateTax(StateTax stateTax);
    
    void loadStateTaxes() throws OrderPersistenceException;
    
}
