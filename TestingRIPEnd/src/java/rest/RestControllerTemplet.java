/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import models.User;
import models.Approval;
import models.Data;
import models.Story;
import service.RipServiceInterface;
import service.Service;

/**
 *
 * @author jonty
 */
@Path("/templetController")
public class RestControllerTemplet {
    RipServiceInterface service=Service.getService();
    
    // User class
    @GET
    @Path("/UserTemplet")  
    public Response u(){
        User user= service.loginUser("jonathan@gmail.com", "Jonathan","");
        return Response.ok().entity(user).build();
    }   
    
    // WaitApproval class
    @GET
    @Path("/ApprovalTemplet")  
    public Response a(){
        Approval a= new Approval(0,"Username","Argument");
        a.setYesNo(false);
        return Response.ok().entity(a).build();
    }
    
    // WaitApproval class
    @GET
    @Path("/StoryTemplet")  
    public Response story(){
        Story story=service.getAllStories(1).get(0);
        return Response.ok().entity(story).build();
    }
    
    // WaitApproval class
    @GET
    @Path("/DataTemplet")  
    public Response data(){
        Data data=new Data(true,5);
        return Response.ok().entity(data).build();
    }
    
    // WaitApproval class
    @GET
    @Path("/WaitApprovalTemplet")  
    public Response wA(){
        Approval wA= new Approval(0,"Username","Argument");
        return Response.ok().entity(wA).build();
    }
}
