package vn.khmt.restful;

import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author TheNhan
 */
@Path("xinchao")
public class XinChaoService {
    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {

        String output = "Xin chao " + msg;
        
        return Response.status(200).entity(output).build();
    }
    
    
}