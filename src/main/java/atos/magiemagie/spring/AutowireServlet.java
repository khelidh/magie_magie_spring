/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.magiemagie.spring;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author tom
 */
public class AutowireServlet extends HttpServlet{
    
    @Override
    public void init() throws ServletException {
        
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);
    }
}
