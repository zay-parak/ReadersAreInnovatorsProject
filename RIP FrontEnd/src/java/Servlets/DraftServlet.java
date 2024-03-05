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
import jakarta.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Paths;
import java.util.List;
import models.Story;
import service.RipServiceInterface;
import service.Service;


@WebServlet(name = "DraftServlet", urlPatterns = {"/DraftServlet"})
//@MultipartConfig
public class DraftServlet extends HttpServlet {
     private String download(HttpServletRequest request){
         String fileName = null;
        try {
            Part img= request.getPart("file");
            fileName = Paths.get(img.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = img.getInputStream();
            FileOutputStream fis = new FileOutputStream("C:\\Users\\Zayaan\\OneDrive\\Desktop\\RIP FrontEnd\\web\\books\\"+fileName);
        byte[] buffer = new byte[1024];
        int count;
        while((count = fileContent.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        fileContent.close();
        } catch (IOException | ServletException ex) {
            Logger.getLogger(DraftServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;
    }

    RipServiceInterface service=Service.getService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                switch(request.getParameter("submit")){

                    case "getDrafts":
        List<Story> stories = (List<Story>)request.getSession(false).getAttribute("stories");
        
        request.getRequestDispatcher("drafts.jsp").forward(request, response);
    }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        switch(request.getParameter("submit")){
            case "save draft":
                String title = request.getParameter("title");
                String description  = request.getParameter("description");
                String fullStory = request.getParameter("story");
                String picture = download(request);
                
                Story story  = new Story(0,fullStory,description,title,null,null,null,null,picture,null);
                service.saveDraftAndWait(story, 2);
                
                
                break;
            case "submit draft":
                String title2 = request.getParameter("title");
                String description2  = request.getParameter("description");
                String fullStory2 = request.getParameter("story");
                String picture2 = download(request);
                
                Story story2  = new Story(0,fullStory2,description2,title2,null,null,null,null,picture2,null);
                service.saveDraftAndWait(story2, 3);
                
                break;
                
            case "deleteDraft":
                Integer draftID = Integer.valueOf(request.getParameter("draftID"));
                String deleteDraft = service.deleteDraft(draftID);
                request.setAttribute("result", deleteDraft);
                
                break;
               
                
        }
    }
}
