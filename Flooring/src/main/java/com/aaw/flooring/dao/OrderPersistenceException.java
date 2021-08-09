/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

/**
 *
 * @author Austin Wong
 */
public class OrderPersistenceException extends Exception{

    public OrderPersistenceException(String message){
        super(message);
    }
    
    public OrderPersistenceException(String message, Throwable cause){
        super(message, cause);
    }
}
