/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.Genre;
import models.Story;
import service.RipServiceInterface;
import service.Service;

@WebServlet(name = "StoryServlet", urlPatterns = {"/StoryServlet"})
@MultipartConfig
public class StoryServlet extends HttpServlet {

    RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "getAllStories":
                List<List<Story>> stories = service.getStories(Integer.valueOf(request.getParameter("limite")));
                request.setAttribute("result", stories);
                request.getRequestDispatcher("allStories.jsp").forward(request, response);
                break;

            case "getRecommended":
                List<Story> recommended = service.getRecommended(Integer.valueOf(request.getParameter("limite")));
                request.setAttribute("result", recommended);
                request.getRequestDispatcher("recommendedStories.jsp").forward(request, response);
                break;

            case "getFavourites":
                List<Story> story = service.getFavourites(Integer.valueOf(request.getParameter("limite")));
                request.setAttribute("result", story);
                request.getRequestDispatcher("allStories.jsp").forward(request, response);
                break;

            case "getTopStories":
                Integer amount = Integer.valueOf(request.getParameter("amount"));
                Integer type = Integer.valueOf(request.getParameter("type"));
                Date start = Date.valueOf(request.getParameter("dateStart"));
                Date end = Date.valueOf(request.getParameter("dateEnd"));

                List<Story> topStories = service.topStories(amount, type, start, end);
                request.setAttribute("result", topStories);
                request.getRequestDispatcher("topStories.jsp").forward(request, response);
                break;

            case "getWaitListStories":
                List<Story> waitStories = service.getWaitListStories();
                request.setAttribute("result", waitStories);
                request.getRequestDispatcher("topStories.jsp").forward(request, response);
                break;

            case "deleteDraft":
                Integer draftID = Integer.valueOf(request.getParameter("draftID"));
                String deleteDraft = service.deleteDraft(draftID);
                request.setAttribute("result", deleteDraft);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "saveDraft":
                Story story = new Story();
                story.setStoryID(0);
                story.setTitle(request.getParameter("title"));
                story.setStory(request.getParameter("story"));
                story.setDescription(request.getParameter("description"));
                story.setPicture(service.downloadImage(request.getPart("file")));
                List<Genre> genres = new ArrayList<>();
                String[] value = request.getParameterValues("choice");
                if (value != null) {
                    for (String v : value) {
                        genres.add(new Genre(Integer.valueOf(v), ""));
                    }
                }
                story.setGenres(genres);
                String s = service.saveDraftAndWait(story, 2);
                request.setAttribute("result", s);
                request.getRequestDispatcher("write-story.jsp").forward(request, response);

                break;

            case "submitStory":
                story = new Story();
                story.setStoryID(0);
                story.setTitle(request.getParameter("title"));
                story.setStory(request.getParameter("story"));
                story.setDescription(request.getParameter("description"));
                story.setPicture(service.downloadImage(request.getPart("file")));
                genres = new ArrayList<>();
                value = request.getParameterValues("choice");
                if (value != null) {
                    for (String v : value) {
                        genres.add(new Genre(Integer.valueOf(v), ""));
                    }
                }
                story.setGenres(genres);
                s = service.saveDraftAndWait(story, 3);
                request.setAttribute("result", s);
                request.getRequestDispatcher("write-story.jsp").forward(request, response);
                break;
        }
    }

}
