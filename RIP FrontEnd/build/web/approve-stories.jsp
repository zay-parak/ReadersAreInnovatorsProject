<%-- 
    Document   : approve-stories
    Created on : 26 Jun 2023, 09:56:17
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Story"%>
<%@page import="models.User"%>
<%@page import="models.Genre"%>
<%@page import="java.util.List"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="service.Service"%>

<!DOCTYPE html>
<html>
    <head>
        <title>JARZ - Approve stories</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="style.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
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
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1>Approve stories</h1>
        </header>

        <div class="draft-content">
            <% String result= (String)request.getAttribute("result");
                if(result!=null){%>
            <p><%= result %></p>
            <% }%>
            
            
            <%
      // Assuming you have a list of Story objects named "storyList"
                   RipServiceInterface rsi=Service.getService();
      List<Story> storyList = rsi.getWaitListStories();

      // Generating the HTML table
      StringBuilder htmlTable = new StringBuilder();
      htmlTable.append("<table class='table-container'>");
      htmlTable.append("<tr><th>Story ID</th><th>Title</th><th>Author</th><th>Genres</th><th>Action</th></tr>");

      for (Story story : storyList) {
          htmlTable.append("<tr>");
          htmlTable.append("<td>").append(story.getStoryID()).append("</td>");
          htmlTable.append("<td>").append(story.getTitle()).append("</td>");
          htmlTable.append("<td>").append(story.getAuthor()).append("</td>");

          // Generating a comma-separated list of genres
          StringBuilder genres = new StringBuilder();
          for (Genre genre : story.getGenres()) {
              genres.append(genre.getName()).append(", ");
          }
          if (genres.length() > 0) {
              genres.delete(genres.length() - 2, genres.length()); // Remove the trailing comma and space
          }
          htmlTable.append("<td>").append(genres.toString()).append("</td>");
    
          // Add the action button to navigate to another JSP
          htmlTable.append("<td><form action='review-story.jsp' method='post'>");
          htmlTable.append("<input type='hidden' name='storyID' value='").append(story.getStoryID()).append("'>");
          htmlTable.append("<button type='submit'>Review</button>");
          htmlTable.append("</form></td>");

          htmlTable.append("</tr>");
      }

      htmlTable.append("</table>");
      out.println(htmlTable.toString());
            %>

        </div>
    </body>
</html>
