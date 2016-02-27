package vn.khmt.restful;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author TheNhan
 */
@Path("user")
public class UserService {
    private ConnectToSQL1 connectToSQL = new ConnectToSQL1(ConnectToSQL1.POSTGRESQL, "ec2-54-227-253-228.compute-1.amazonaws.com", "d8viikojj42e3b", "uzufecmqojhnyx", "WPJGueUbd3npLKslU2BEUOmMHx");
    private AuthenticationService AuthenService = new AuthenticationService();
    public static final String AUTHENTICATION = "authorization";
    @GET
    @Path("/{param}")
    
    public Response getUserInfo(@PathParam("param") String userID, @HeaderParam(AUTHENTICATION)String authString) throws JSONException {
        if(AuthenService.GetUserAuthenticate(authString, userID, connectToSQL)){
            Object[] userInfor=connectToSQL.getUser(Integer.parseInt(userID));
            User user = new User(userInfor[1].toString(), userInfor[3].toString(), userInfor[2].toString(), Integer.parseInt(userInfor[4].toString()), Integer.parseInt(userInfor[0].toString()));
            return Response.status(200).entity(user).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("").build();
        }
    }
    @PUT
    @Path("/setname")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserName (@FormParam("UserID") String userID, @FormParam("Name") String name, @HeaderParam(AUTHENTICATION)String authString){
        System.out.println(userID+" "+name);
        if (AuthenService.GetUserAuthenticate(authString, userID, connectToSQL)){
            if (connectToSQL.changeUserName(userID, name)){
                 Object[] userInfor=connectToSQL.getUser(Integer.parseInt(userID));
                User user = new User(userInfor[1].toString(), userInfor[3].toString(), userInfor[2].toString(), Integer.parseInt(userInfor[4].toString()), Integer.parseInt(userInfor[0].toString()));
                return Response.status(200).entity(user).build();
            } else {
                return Response.status(200).entity("Failed").build();
            }
            
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("").build();
        }
    }
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addUser(@FormParam("UserName") String userName, @FormParam("Email") String email, @FormParam("Name") String name, @FormParam("Status") String status, @FormParam("Password") String password ) throws JSONException{
        JSONObject object = new JSONObject();
        object.put("UserName", userName);
        object.put("Email", email);
        object.put("Name", name);
        object.put("Status", status);
        object.put("Password", password);
        if (connectToSQL.addUser(object)){
            return Response.status(200).entity("User added").build();
        } else {
            return Response.status(200).entity("Failed").build();
        }
    } 
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser (@HeaderParam(AUTHENTICATION)String authString) throws SQLException{
        if (AuthenService.GetAllAuthenticate(authString, connectToSQL)){
            List<User> users=connectToSQL.getUserList();
                            GenericEntity genericEntity = new GenericEntity<List<User>>(users){};
                return Response.status(200).entity(genericEntity).build();
            //return users;
        } else {
            return null;
        }
        
    }
    
}
