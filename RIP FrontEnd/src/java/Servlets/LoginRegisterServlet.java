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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Genre;
import models.User;
import service.RipServiceInterface;
import service.Service;

@WebServlet(name = "LoginRegisterServlet", urlPatterns = {"/LoginRegisterServlet"})
public class LoginRegisterServlet extends HttpServlet {

    RipServiceInterface service = Service.getService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "guest":
                User userDetails;

                //check is user is exist
                userDetails = service.loginUser("guest@gmail.com", "guest", "Guest");

                if (userDetails == null) {
                    request.setAttribute("result", "Login failed");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", userDetails);
                    request.getRequestDispatcher("home.jsp?list=1").forward(request, response);
                }
                request.getRequestDispatcher("home.jsp?list=1").forward(request, response);
                break;

            case "logout":
                request.getSession().invalidate();
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getParameter("submit")) {
            case "Sign Up":
                User user = new User();

                user.setName(request.getParameter("name"));
                user.setSurname(request.getParameter("surname"));
                user.setUsername(request.getParameter("username"));
                user.setEmail(request.getParameter("email"));
                user.setPassword(request.getParameter("password"));
                user.setPhoneNumber(request.getParameter("phoneNumber"));

                List<Genre> genres = new ArrayList<>();
                String[] value = request.getParameterValues("choice");
                if (value != null) {
                    for (String v : value) {
                        genres.add(new Genre(Integer.valueOf(v), ""));
                    }
                }
                user.setFavourites(genres);
                String s = service.register(user);
                if (s.equals("Registration unsuccessful")) {
                    request.setAttribute("result", "Registration unsuccessful. Username or Email already taken");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.setAttribute("result", "Registration successful. Email with verification code sent");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                break;

            case "Login":
                User userDetails;

                //check is user is exist
                userDetails = service.loginUser(request.getParameter("email"), request.getParameter("password"), request.getParameter("code"));

                if (userDetails == null) {
                    request.setAttribute("result", "Login failed. Email or password incorrect or verification code needed");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", userDetails);
                    request.getRequestDispatcher("home.jsp?list=1").forward(request, response);
                }

        }
    }

}
