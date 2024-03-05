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
import java.util.ArrayList;
import java.util.List;
import models.Genre;
import models.Story;
import service.RipServiceInterface;
import service.Service;

/**
 *
 * @author jonty
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    private RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Boolean isGenre = false;
        String name = request.getParameter("searchName");
        List<Genre> genres = (List<Genre>) request.getSession(false).getAttribute("genres");
        List<Story> stories = new ArrayList<>();

        for (Genre g : genres) {
            if (name.equalsIgnoreCase(g.getName())) {
                isGenre = true;
                stories = service.searchGenre(g.getGenreID());
            }
        }
        if (isGenre == false) {
            stories = service.searchName(name);
        }

        request.setAttribute("search", stories);
        request.setAttribute("heading", "Search results for: " + name);
        request.getRequestDispatcher("home.jsp?list=8").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
