/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.sql.Date;
import java.util.List;
import models.Genre;
import models.User;
import service.RipServiceInterface;
import service.Service;

/**
 *
 * @author jonty
 */
@Path("/topController")
public class RestControllerTops {
    RipServiceInterface service=Service.getService(); 
    
    //Done
    
    @GET
    @Path("/topGenre/{amount}/{start}/{end}")
    public List<Genre> getTopGenre(@PathParam("amount")Integer amount,@PathParam("start") Date start,@PathParam("end") Date end){
        return service.topGenre(amount, start, end);
    }
    
    @GET
    @Path("/topUsers/{amount}/{type}/{start}/{end}")
    public List<User> getTopApprovingEditors(@PathParam("amount")Integer amount,@PathParam("type")Integer type,@PathParam("start") Date start,@PathParam("end") Date end){
        return service.topUsers(amount, type, start, end);
    }
    
    //Need to test
}
