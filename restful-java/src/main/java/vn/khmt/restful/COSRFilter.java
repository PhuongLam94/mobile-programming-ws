/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.khmt.restful;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Phuo
 */
@Provider
public class COSRFilter implements ContainerResponseFilter{
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext){
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:8383");
        headers.add("Access-Control-Allow-Method", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, Authorization");
        
    }
}
