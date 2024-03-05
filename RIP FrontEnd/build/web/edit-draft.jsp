<%-- 
    Document   : edit-story
    Created on : 03 Jul 2023, 02:01:08
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
        <title>JARZ - Edit story</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
        <link rel="stylesheet" href="style.css" type="text/css">
    </head>
    <body>
        <header class="draft-header">
            <a href="home.jsp?list=1" class="logo"> <i class="fas fa-book" style="color: var(--green);" ></i> JARZ </a>
            <h1>Edit a story</h1>
        </header>

        <div class="draft-content">
            <p>${result}</p>
            <%
                Story currentApproval = new Story();
                RipServiceInterface rsi = Service.getService();
                List<Story> drafts = rsi.getAllDrafts();
                
                currentApproval = drafts.get(Integer.valueOf(request.getParameter("index")));
                   request.getSession(false).setAttribute("draft", currentApproval);
            %>
            <form action="StoryServlet" method="POST" enctype="multipart/form-data">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title" value="<%=currentApproval.getTitle()%>" readonly>

                <label for="description">Description:</label>
                <input type="text" id="description" name="description" value="<%=currentApproval.getDescription()%>" required>

                <label for="story">Story:</label>
                <textarea id="story" name="story" required><%=currentApproval.getStory()%></textarea>
                
                <label for="author">Author:</label>
                <input type="text" id="author" name="author" value="<%=currentApproval.getAuthor()%>" readonly>

                <label for="genre">Genres:</label>
                  <%
                      StringBuilder genres = new StringBuilder();
          for (Genre genre : currentApproval.getGenres()) {
              genres.append(genre.getName()).append(", ");
          }
          if (genres.length() > 0) {
              genres.delete(genres.length() - 2, genres.length()); // Remove the trailing comma and space
          }%>
          <input type="text" id="genre" name="genre" value="<%=genres.toString()%>" readonly>
          
                <label for="picture">Picture:</label>
                <input type="text" id="picture" name="picture" value="<%=currentApproval.getPicture()%>" readonly>
            
            <button type="submit" name="submit" value="saveDraft">Save Draft</button>
            <button type="submit" name="submit" value="submitStory">Submit Draft</button>

            </form>
        </div>
    </body>
</html>