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
import service.RipServiceInterface;
import service.Service;
import models.User;



@WebServlet(name = "ReaderServlet", urlPatterns = {"/ReaderServlet"})
public class ReaderServlet extends HttpServlet {
        RipServiceInterface service=Service.getService();
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = (User)request.getSession(false).getAttribute("user");
        Integer id  = u.getUserID();
        String argument = request.getParameter("argument");
        service.addWaitApproval(id, argument);
        
        request.getRequestDispatcher("apply-writer.jsp").forward(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
