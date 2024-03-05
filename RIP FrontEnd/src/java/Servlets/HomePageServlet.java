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
import models.User;
import service.RipServiceInterface;
import service.Service;

@WebServlet(name = "HomePageServlet", urlPatterns = {"/HomePageServlet"})
public class HomePageServlet extends HttpServlet {

    RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "favourites":
                List<Story> story = service.getFavourites(1);
                request.setAttribute("result", story);
                request.getRequestDispatcher("favourites.jsp").forward(request, response);

                break;
            case "profile":

                User profile = (User) request.getSession().getAttribute("userDetails");
                request.setAttribute("result", profile);
                request.getRequestDispatcher("profile.jsp").forward(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        switch (request.getParameter("submit")) {
            
            case "update":

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");
        String password = request.getParameter("password");
        List<Genre> genres = new ArrayList<>();
        String[] value = request.getParameterValues("choice");
        if (value != null) {
            for (String v : value) {
                genres.add(new Genre(Integer.valueOf(v), ""));
            }
        }
        User user = new User(0, name, surname, username, email, 'R', phoneNumber);
        user.setFavourites(genres);

        user.setPassword(password);

        User userDet = service.updateUserDetails(user);
        request.setAttribute("result", userDet);
        request.getSession(false).setAttribute("user", userDet);
        request.getRequestDispatcher("home.jsp?list=1").forward(request, response);
    }
}
}
