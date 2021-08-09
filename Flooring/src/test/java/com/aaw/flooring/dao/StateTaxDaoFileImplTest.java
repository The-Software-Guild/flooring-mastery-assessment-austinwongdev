/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaw.flooring.dao;

import com.aaw.flooring.model.StateTax;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Austin Wong
 */
public class StateTaxDaoFileImplTest {
    
    private final String TEST_STATE_TAX_FILE = "src/test/resources/TestData/Taxes.txt";
    private final StateTaxDao testStateTaxDao;
    
    public StateTaxDaoFileImplTest() {
        this.testStateTaxDao = new StateTaxDaoFileImpl(TEST_STATE_TAX_FILE);
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
    public void testLoadGetStateTaxes() throws OrderPersistenceException {
        StateTax expectedStateTax = new StateTax("TX", 
                                                 new BigDecimal("4.45"));
        
        testStateTaxDao.loadStateTaxes();
        
        StateTax returnedStateTax = testStateTaxDao.getStateTax("TX");
        assertNotNull(returnedStateTax, "Should return a StateTax");
        assertEquals(expectedStateTax, returnedStateTax, "Should return Texas's State Tax");
    
    }
    
    @Test
    public void testLoadStateTaxesFileNotFound(){
        StateTaxDao badDao = new StateTaxDaoFileImpl("NonExistentFile.txt");
        
        assertThrows(OrderPersistenceException.class, 
                     () -> badDao.loadStateTaxes(),
                     "Should throw OrderPersistenceException");
    }
    
    @Test
    public void testAddGetStateTax() throws OrderPersistenceException{
        StateTax expectedStateTax = new StateTax("TX", "Texas", 
                                                 new BigDecimal("4.45"));
        
        testStateTaxDao.addStateTax(expectedStateTax);
        
        StateTax returnedStateTax = testStateTaxDao.getStateTax("TX");
        assertNotNull(returnedStateTax, "Should return a StateTax");
        assertEquals(expectedStateTax, returnedStateTax, "Should return Texas's State Tax");
    }
    
}
