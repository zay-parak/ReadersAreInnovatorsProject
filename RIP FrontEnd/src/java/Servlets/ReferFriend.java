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
import service.RipServiceInterface;
import service.Service;


/**
 *
 * @author Zayaan
 */
@WebServlet(name = "ReferFriend", urlPatterns = {"/ReferFriend"})
public class ReferFriend extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RipServiceInterface rsi=Service.getService();
        String phone=request.getParameter("phoneNumber");
        String message="link-http://localhost:8080/RIP_FrontEnd/  "+request.getParameter("message");
        rsi.sendSms(phone, message);
        request.getRequestDispatcher("home.jsp?list=1").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }


}
