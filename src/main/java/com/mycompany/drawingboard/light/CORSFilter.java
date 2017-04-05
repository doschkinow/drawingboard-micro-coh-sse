/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.drawingboard.light;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author pdos
 */
@Provider
public class CORSFilter implements ContainerResponseFilter{

    /**
     * Add the cross domain data to the output if needed
     * 
     * @param creq The container request (input)
     * @param cres The container request (output)
     * @return The output request with cross domain if needed
     */
    @Override
    public void filter(ContainerRequestContext creq, ContainerResponseContext cres) {
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET");
    }    
}
