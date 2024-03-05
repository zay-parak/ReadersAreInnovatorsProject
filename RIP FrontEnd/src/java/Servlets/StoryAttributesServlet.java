/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import models.Comment;
import models.Data;
import models.Genre;
import models.Story;
import service.RipServiceInterface;
import service.Service;

@WebServlet(name = "StoryAttributesServlet", urlPatterns = {"/StoryAttributesServlet"})
public class StoryAttributesServlet extends HttpServlet {

    RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "getRating":
                Integer storyID2 = Integer.valueOf(request.getParameter("storyID"));
                Double rating = service.getRating(storyID2);
                request.setAttribute("result", rating);
                request.getRequestDispatcher("index.jsp").forward(request, response);

                break;

            case "getNumReads":
                Integer storyID3 = Integer.valueOf(request.getParameter("storyID"));
                Integer reads = service.getNumReads(storyID3);
                request.setAttribute("result", reads);
                request.getRequestDispatcher("index.jsp").forward(request, response);

                break;

            case "searchName":

                String storyName = request.getParameter("searchName");
                List<Story> stories = service.searchName(storyName);
                request.setAttribute("result", stories);
                request.getRequestDispatcher("home.jsp").forward(request, response);
                break;

            case "getGenres":
                List<Genre> genres = service.getGenres();
                request.setAttribute("result", genres);
                request.getRequestDispatcher("index.jsp").forward(request, response);

                break;

            case "deleteComment":
                Integer commentID = Integer.valueOf(request.getParameter("commentID"));
                String deleteComment = service.deleteComment(commentID);
                request.setAttribute("result", deleteComment);
                break;

            case "getAllComments":
                Integer storyID = 2;
                List<Comment> commentsList = service.getAllComments(storyID);
                request.setAttribute("result", commentsList);
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;

            case "setStoryData":
                Integer index = Integer.valueOf(request.getParameter("index"));
                stories = (List<Story>) request.getSession(false).getAttribute("stories");
                Story story = stories.get(index);
                if (story.getData() == null) {
                    Integer data = service.setStoryData(story.getStoryID());
                    stories.get(index).setData(new Data(false, 0));
                }
                request.getSession(false).setAttribute("stories", stories);
                request.setAttribute("index", index);
                request.getRequestDispatcher("read.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {

            case "addComment":
                String comment = request.getParameter("comment");
                Integer storyID = Integer.valueOf(request.getParameter("storyID"));
                Comment comments = new Comment(0, comment, "");
                service.addComment(comments, storyID);
                request.getRequestDispatcher("home.jsp").forward(request, response);
                break;

            case "likeRating":
                Integer id = Integer.valueOf(request.getParameter("id"));

                Boolean fav = false;
                String check = request.getParameter("liked");
                if (check != null) {
                    fav = true;
                }
                service.likeStory(fav, id);

                String ph = request.getParameter("rating");
                Integer rate = Integer.valueOf(ph);
                if (rate != 0) {
                    service.rateStory(rate, id);
                }
                request.getRequestDispatcher("read.jsp?index=" + request.getParameter("index")).forward(request, response);

                break;

            case "hide":
                Integer hide = Integer.valueOf(request.getParameter("hide"));
                Boolean h=hide==1?Boolean.valueOf(request.getParameter("hideStory")):Boolean.valueOf(request.getParameter("hideComment"));
                h=!h; //inverter

                storyID = Integer.valueOf(request.getParameter("storyID"));

                service.hide(storyID, hide, h);
                request.getRequestDispatcher("my-stories.jsp").forward(request, response);
                break;

        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
