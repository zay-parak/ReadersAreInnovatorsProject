/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rest;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import models.Approval;
import models.Comment;
import models.Genre;
import models.Story;
import service.RipServiceInterface;
import service.Service;

/**
 *
 * @author jonty
 */
@Path("/storyController")
public class RestControllerStories {

    RipServiceInterface service = Service.getService();

    //Done
    @GET
    @Path("/getAllComments/{id}")
    public List<Comment> getAllComments(@PathParam("id") Integer storyID) {
        return service.getAllComments(storyID);
    }

    @GET
    @Path("/searchGenre/{id}")
    public List<Story> searchGenre(@PathParam("id") Integer genreID) {
        return service.searchGenre(genreID);
    }

    @GET
    @Path("/searchName/{name}")
    public List<Story> searchName(@PathParam("name") String storyName) {
        return service.searchName(storyName);
    }

    @GET
    @Path("/getAlldrafts")
    public List<Story> getAllDrafts() {
        return service.getAllDrafts();
    }

    @POST
    @Path("/getRating")
    public Response getRating(Integer storyID) {
        Double rating = service.getRating(storyID);
        return Response.ok().entity(rating).build();
    }

    @POST
    @Path("/GetNumberReads")
    public Response getNumReads(Integer storyID) {
        Integer reads = service.getNumReads(storyID);
        return Response.ok().entity(reads).build();
    }

    @GET
    @Path("/getWaitListStories")
    public List<Story> getWaitListStories() {
        return service.getWaitListStories();
    }

    @POST
    @Path("/readStory")
    public Response readStory(Integer storyID) {
        Integer reads = service.getNumReads(storyID);
        return Response.ok().entity(reads).build();
    }

    @POST
    @Path("/addComment")
    public Response addComment(Comment comment) {
        service.addComment(comment, comment.getStoryID());
        return Response.ok().entity(null).build();
    }

    @POST
    @Path("/hideComment")
    public Response hideComment(Approval a) {
        service.hide(a.getId(), 2, a.getYesNo());
        return Response.ok().entity(null).build();
    }

    @POST
    @Path("/hideStory")
    public Response hideStory(Approval a) {
        service.hide(a.getId(), 1, a.getYesNo());
        return Response.ok().entity(null).build();
    }

    @POST
    @Path("/rateStory")
    public Response rateStory(Story story) {
        Integer value = service.rateStory(story.getData().getRating(), story.getStoryID());
        return Response.ok().entity(value).build();
    }

    @POST
    @Path("/likeStory")
    public Response likeStory(Story story) {
        Integer value = service.likeStory(story.getData().getLiked(), story.getStoryID());
        return Response.ok().entity(value).build();
    }

    @GET
    @Path("/getGenres")
    public List<Genre> getGenres() {
        return service.getGenres();
    }

    @GET
    @Path("/getStories/{limite}")
    public List<List<Story>> getStories(@PathParam("limite") Integer limite) {
        LocalDateTime time = LocalDateTime.now();
        int year = time.getYear(), month = time.getMonthValue(), day = time.getDayOfMonth();
        String start = year + "-" + month + "-01";
        String end = year + "-" + (month + 1) + "-01";

        String startDay = year + "-" + month + "-" + day;
        String endDay = year + "-" + month + "-" + (day + 1);

        List<List<Story>> stories = new ArrayList<>();

        stories.add(service.getRecommended(limite));
        stories.add(service.getAllStories(limite));
        stories.add(service.getAllDrafts());
        stories.add(service.getFavourites(limite));
        // Top liked
        stories.add(service.topStories(10, 1, Date.valueOf(start), Date.valueOf(end)));
        // Top rating
        stories.add(service.topStories(10, 2, Date.valueOf(start), Date.valueOf(end)));
        // Top read
        stories.add(service.topStories(10, 3, Date.valueOf(start), Date.valueOf(end)));
        // Daily top read
        stories.add(service.topStories(1, 3, Date.valueOf(startDay), Date.valueOf(endDay)));

        return stories;
    }

    @GET
    @Path("/getAllStories/{limite}")
    public List<Story> getAllStories(@PathParam("limite") Integer limite) {
        return service.getAllStories(limite);
    }

    @GET
    @Path("/getRecommended/{limite}")
    public List<Story> getRecommended(@PathParam("limite") Integer limite) {
        List<Story> stories = service.getRecommended(limite);
        return stories;
    }

    @GET
    @Path("/getTopStories/{amount}/{type}/{start}/{end}")
    public List<Story> topStories(@PathParam("amount") int amount, @PathParam("type") int type, @PathParam("start") Date start, @PathParam("end") Date end) {
        return service.topStories(amount, type, start, end);
    }

    @GET
    @Path("/getFavourites/{limite}")
    public List<Story> getFavourites(@PathParam("limite") Integer limite) {
        return service.getFavourites(limite);
    }

    @POST
    @Path("/saveDraft")
    public Response saveDraft(Story story) {
        String response = service.saveDraftAndWait(story, 2);
        return Response.ok().entity(response).build();
    }

    @POST
    @Path("/submitStory")
    public Response submitStory(Story story) {
        String response = service.saveDraftAndWait(story, 3);
        return Response.ok().entity(response).build();
    }

    @POST
    @Path("/reviewStory/{yesNo}")
    public Response reviewStory(Story story, @PathParam("yesNo") Boolean acceptance) {
        String response = service.reviewStory(story, acceptance);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/setStoryData/{id}")
    public Response setStoryData(@PathParam("id") Integer id) {
        Integer response = service.setStoryData(id);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/deleteComment/{id}")
    public Response deleteComment(@PathParam("id") Integer id) {
        String response = service.deleteComment(id);
        return Response.ok().entity(response).build();

    }

    @GET
    @Path("/deleteDraft/{id}")
    public Response deleteDraft(@PathParam("id") Integer id) {
        String response = service.deleteDraft(id);
        return Response.ok().entity(response).build();

    }
//Need to add

    @GET
    @Path("/getUserStories")
    public List<Story> getUserStories() {
        return service.getUserStories();

    }

    @GET
    @Path("/sendSMS/{phoneNumber}/{message}")
    public Response sensSMS(@PathParam("phoneNumber") String phoneNumber, @PathParam("message") String message) {
        String response = service.sendSms(phoneNumber, message);
        return Response.ok().entity(response).build();
    }


}
