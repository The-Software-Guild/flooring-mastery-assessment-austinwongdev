/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring;

import com.aaw.flooring.controller.FlooringController;
import com.aaw.flooring.dao.OrderDao;
import com.aaw.flooring.dao.OrderDaoFileImpl;
import com.aaw.flooring.dao.ProductDao;
import com.aaw.flooring.dao.ProductDaoFileImpl;
import com.aaw.flooring.dao.StateTaxDao;
import com.aaw.flooring.dao.StateTaxDaoFileImpl;
import com.aaw.flooring.service.FlooringServiceLayer;
import com.aaw.flooring.service.FlooringServiceLayerImpl;
import com.aaw.flooring.view.FlooringView;
import com.aaw.flooring.view.UserIO;
import com.aaw.flooring.view.UserIOConsoleImpl;

/**
 *
 * @author Austin Wong
 */
public class App {

    public static void main(String[] args){
        OrderDao orderDao = new OrderDaoFileImpl();
        ProductDao productDao = new ProductDaoFileImpl();
        StateTaxDao stateTaxDao = new StateTaxDaoFileImpl();
        FlooringServiceLayer serviceLayer = new FlooringServiceLayerImpl(orderDao, productDao, stateTaxDao);
        UserIO io = new UserIOConsoleImpl();
        FlooringView view = new FlooringView(io);
        
        FlooringController controller = new FlooringController(serviceLayer, view);
        controller.run();
    }
}
