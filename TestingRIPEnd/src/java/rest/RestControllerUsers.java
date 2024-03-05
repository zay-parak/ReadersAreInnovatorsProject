package rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import models.User;
import models.Approval;
import service.RipServiceInterface;
import service.Service;

@Path("/userController")
public class RestControllerUsers {
    RipServiceInterface service=Service.getService();
    
    //Done
    
    @POST
    @Path("/login")
    public Response login(User values){
        User user= service.loginUser(values.getEmail(),values.getPassword(),values.getSalt());
        return Response.ok().entity(user).build();
    }
    
    @POST
    @Path("/signup")
    public Response signup(User user){
        String response=service.register(user);
        return Response.ok().entity(response).build();
    }
    
    @GET
    @Path("/getWaitListApprovals")
    public List<Approval> getWaitListAppprovals(){
        return service.getWaitListAppprovals();
    }
    
     @POST
    @Path("/addWaitApproval")
    public Response addWaitApproval(Approval wA){
        service.addWaitApproval(wA.getId(),wA.getArgument());
        return Response.ok().entity(null).build();
    }
    
    @POST
    @Path("/approveWriter")
    public Response approveWriter(Approval wA) {
        String response = service.approveWriter(wA.getId(),wA.getYesNo());
        return Response.ok().entity(response).build();
    }
    
    @POST
    @Path("/blockWriter")
    public Response blockWriter(User users) {
        String response = service.blockWriter(users.getUserID(),users.getType());
        return Response.ok().entity(response).build();
    }
    
    @POST
    @Path("/addEditor")
    public Response addEditor(Integer id ) {
        String response = service.addEditor(id);
        return Response.ok().entity(response).build();
    }
    
    @GET
    @Path("/getSpercificUser/{type}")
    public List<User> getSpecificUser(@PathParam("type") Character type) {
        return service.getSpecificUser(type);
    }
    
    //Need to do
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateUserDetails")
    public Response updateUserDetails(User user) {
        User user1 = service.updateUserDetails(user);
        return Response.ok().entity(user1).build();
    }
}
