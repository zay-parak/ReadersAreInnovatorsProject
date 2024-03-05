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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.Approval;
import models.Genre;
import models.Story;
import models.User;
import service.RipServiceInterface;
import service.Service;

@WebServlet(name = "EditorServlet", urlPatterns = {"/EditorServlet"})
public class EditorServlet extends HttpServlet {

    RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "getWaitApprovalWriters":
                List<Approval> waitList = service.getWaitListAppprovals();
                request.setAttribute("result", waitList);
                request.getRequestDispatcher("waitListApproval.jsp").forward(request, response);
                break;
            case "getEditor":
                Character type = request.getParameter("type").charAt(0);
                List<User> users = service.getSpecificUser(type);
                request.setAttribute("result", users);
                request.getRequestDispatcher("addEditor.jsp").forward(request, response);
                break;
            case "getTopGenre":
                Integer amount = Integer.valueOf(request.getParameter("amount"));
                Date start = Date.valueOf(request.getParameter("dateStart"));
                Date end = Date.valueOf(request.getParameter("dateEnd"));
                List<Genre> genres = service.getTopGenre(amount, start, end);
                request.setAttribute("result", genres);
                request.getRequestDispatcher("Top.jsp").forward(request, response);
                break;
            case "getTopUsers":
                amount = Integer.valueOf(request.getParameter("amount"));
                Integer choice = Integer.valueOf(request.getParameter("type"));
                start = Date.valueOf(request.getParameter("dateStart"));
                end = Date.valueOf(request.getParameter("dateEnd"));

                List<User> topUsers = service.getTopUsers(amount, choice, start, end);
                request.setAttribute("result", topUsers);
                request.getRequestDispatcher("topStories.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Boolean yesNo;
        switch (request.getParameter("submit")) {

            case "approveWriter":
                Integer userID = Integer.valueOf(request.getParameter("userID"));
                yesNo = Boolean.valueOf(request.getParameter("approval"));
                String result = service.approveWriter(userID, yesNo);
                request.setAttribute("result", result);
                request.getRequestDispatcher("approve-writer.jsp").forward(request, response);
                break;

            case "blockWriter":
                Character type;
                Integer id = Integer.valueOf(request.getParameter("userID"));
                if (request.getParameter("type").equalsIgnoreCase("W")) {
                    type = 'B';
                } else {
                    type = 'W';
                }
                result = service.blockWriter(id, type);
                request.setAttribute("result", result);
                request.getRequestDispatcher("block-writer.jsp").forward(request, response);
                break;

            case "addEditor":
                id = Integer.valueOf(request.getParameter("userID"));
                result = service.addEditor(id);
                request.setAttribute("result", result);
                request.getRequestDispatcher("add-editor.jsp").forward(request, response);
                break;

            case "reviewStory":
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String fullStory = request.getParameter("story");
                String picture = request.getParameter("picture");
                String author = request.getParameter("author");
                List<Genre> genres = new ArrayList<>();
                Story ph = (Story) request.getSession(false).getAttribute("approveStory");
                request.getSession(false).removeAttribute("approveStory");
                if (ph.getGenres() != null) {
                    genres = ph.getGenres();
                }
                yesNo = Boolean.valueOf(request.getParameter("approve"));
                Story story = new Story(0, fullStory, description, title, null, null, author, 0, picture, genres);
                String s = service.reviewStory(story, yesNo);
                request.setAttribute("result", s);
                request.getRequestDispatcher("approve-stories.jsp").forward(request, response);
                break;

        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
