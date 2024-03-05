<%-- 
    Document   : my-stories
    Created on : 06 Jul 2023, 03:26:27
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Story"%>
<%@page import="models.Genre"%>
<%@page import="java.util.List"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="service.Service"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <link rel="stylesheet" href="style.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Sans&family=Poppins:wght@100;300;400;500;600&display=swap">
    
        <title>My Stories</title>
    </head>
        <style>
        .table-container {
            font-family: Arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
            max-width: 1200px; /* Adjust the value as per your requirements */
            margin: 0 auto;
        }

        .table-container th, .table-container td {
            padding: 8px;
            border: 1px solid #ddd;
        }

        .table-container th {
            background-color: #f2f2f2;
        }
    </style>
    <body>
        <header class="draft-header">
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: var(--green);" ></i> JARZ </a>
            <h1>My stories</h1>
        </header>

        <div class="draft-content">
            <p>${result}</p>


            <%
                   RipServiceInterface rsi=Service.getService();
      List<Story> myStoryList = rsi.getUserStories();

      // Generating the HTML table
      StringBuilder htmlTable = new StringBuilder();
      htmlTable.append("<table class='table-container'>");
      htmlTable.append("<tr><th>Story ID</th><th>Title</th><th>Description</th><th>Is Hidden</th><th>Is Comments Hidden</th><th>Action</th></tr>");

      for (Story story : myStoryList) {
          htmlTable.append("<tr>");
          htmlTable.append("<td>").append(story.getStoryID()).append("</td>");
          htmlTable.append("<td>").append(story.getTitle()).append("</td>");
          htmlTable.append("<td>").append(story.getDescription()).append("</td>");
          htmlTable.append("<td>").append(story.getHideStory()).append("</td>");
          htmlTable.append("<td>").append(story.getHideComment()).append("</td>");
    
          // Add the action button to navigate to another JSP
          htmlTable.append("<td><form action='StoryAttributesServlet' method='post'>");
          htmlTable.append("<input type='hidden' name='submit' value='hide'>");
          htmlTable.append("<input type='hidden' name='storyID' value='").append(story.getStoryID()).append("'>");
          htmlTable.append("<input type='hidden' name='hideStory' value='").append(story.getHideStory()).append("'>");
          htmlTable.append("<input type='hidden' name='hideComment' value='").append(story.getHideComment()).append("'>");
          htmlTable.append("<button type='submit' name='hide' value='1'>Hide Story</button>");
          htmlTable.append("<button type='submit' name='hide' value='2'>Hide Comment</button>");
          htmlTable.append("</form></td>");
          htmlTable.append("</tr>");
      }

      htmlTable.append("</table>");
      out.println(htmlTable.toString());
            %>

        </div>
    </body>
</html>
