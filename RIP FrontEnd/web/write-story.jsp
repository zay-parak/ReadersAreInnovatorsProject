<%-- 
    Document   : write-story
    Created on : 26 Jun 2023, 09:16:21
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Story"%>
<%@page import="models.Genre"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="models.User"%>
<%@page import="java.util.List"%>
<%@page import="service.Service"%>
<!DOCTYPE html>
<html>
<head>
    <title>JARZ - Write a story</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
    <link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>
    <header class="draft-header">
        <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: var(--green);" ></i> JARZ </a>
        <h1>Write a story</h1>
    </header>

    <div class="draft-content">
        <form action="StoryServlet" method="POST" enctype="multipart/form-data"> 
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required>

            <label for="description">Description:</label>
            <input type="text" id="description" name="description" required>

            <label for="choice">Genre:</label>
            <div class="genre">
                <% 
                   RipServiceInterface rsi=Service.getService();
                   List<Genre> genres=rsi.getGenres();
                   for(Genre g:genres){
                %> 
                <label><input type="checkbox" name="choice" value="<%=g.getGenreID()%>"><%=g.getName()%></label>
                <% } %> 
            </div>

            <label for="story">Story:</label>
            <textarea id="story" name="story" required></textarea>

            <label for="picture">Picture:</label>
            <input type="file" name="file" id="file" accept="image/*" required>
            
            <br><br><br>
            
            <p>${result}</p>
            
            <button type="submit" name="submit" value="saveDraft">Save Draft</button>
            <button type="submit" name="submit" value="submitStory">Submit Draft</button>  
        </form>
            <br><br><br><br><br><br>
    </div>
</body>
</html>
