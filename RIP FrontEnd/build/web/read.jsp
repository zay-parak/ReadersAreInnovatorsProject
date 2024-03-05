<%-- 
    Document   : read
    Created on : 30 Jun 2023, 12:03:16
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Story"%>
<%@page import="java.util.List"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="service.Service"%>
<%@page import="models.Story"%>
<%@page import="models.Comment"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
        <link rel="stylesheet" href="style.css" type="text/css">
        <title>Read</title>

        <style>
            html::-webkit-scrollbar{
                width: 1.5rem;
            }

            html::-webkit-scrollbar-track{
                background: var(--black);
            }

            html::-webkit-scrollbar-thumb{
                background: transparent;
                opacity: 0.5;
            }

            .draft-content textarea{
                width: 1320px;
                height: 500px;
            }

            /* Style for the form container */
            .form-container {
                max-width: 400px;
                margin: 0 auto;
                padding: 20px;
                background-color: #f2f2f2;
                border-radius: 5px;
            }

            /* Style for form labels */
            label {
                display: block;
                font-size: 20px;
                margin-bottom: 10px;
                font-weight: bold;
            }

            /* Style for form inputs */
            input[type="number"],
            input[type="checkbox"] {
                margin-bottom: 10px;
            }

            /* Style for the submit button */
            input[type="submit"] {
                background-color: #4CAF50;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 4px;
                cursor: pointer;
            }

            /* Style for the submit button on hover */
            input[type="submit"]:hover {
                background-color: #45a049;
            }

            /* Style for the comments section */
            h1 {
                font-size: 20px;
                margin-bottom: 10px;
            }

            ul#list {
                list-style-type: none;
                padding: 0;
            }

            ul#list li {
                margin-bottom: 5px;
            }

            /* Style for form inputs */
            input[type="text"] {
                margin-bottom: 10px;
                width: 100%;
                padding: 10px;
                border-radius: 4px;
                border: 1px solid #ccc;
            }

            /* Style for the submit button */
            button[type="submit"] {
                background-color: var(--black);
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 4px;
                cursor: pointer;
            }

            /* Style for the submit button on hover */
            button[type="submit"]:hover {
                background-color: #45a049;
            }

        </style>
    </head>
    <body>
        <%
                RipServiceInterface rsi = Service.getService();
                List<Story> allStories = (List<Story>) request.getSession(false).getAttribute("stories");
                Story s = allStories.get(Integer.parseInt(request.getParameter("index")));
        %>
        <header class="draft-header">
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1 class="favorites-heading"><%=s.getTitle()%></h1>
        </header>

        <div class="draft-content">
            <textarea readonly><%= s.getStory()%></textarea>
        </div>

            
        <div class="form-container">

            <label for="rate">Rating:</label>

            <form action="StoryAttributesServlet" method="post">
                <input type="number" name="rating" min="0" max="5" value="<%= s.getData().getRating() %>">
                <label><input type="checkbox" <%if(s.getData().getLiked()){%>checked=""<%}%> name="liked">Like story</label>
                <input type="hidden" name="id" value="<%= s.getStoryID() %>">
                <input type="hidden" name="index" value="<%= request.getParameter("index") %>">
                <button type="submit" name="submit" value="likeRating">Rate and Like</button>
            </form>
        <br><br>

        <%if(!s.getHideComment()){%>
            <form action="StoryAttributesServlet" method="post">            
                <%
                    List<Comment>myList = rsi.getAllComments(s.getStoryID());
                %>
                <h1>Comments:</h1>
                <ul id="list">
                    <% for (Comment item : myList) { %>
                    <li><%= item.getUserName()  + "  -  " + item.getComment() %></li>
                        <% } %>
                </ul>
                <input type="hidden" name="storyID" value="<%= s.getStoryID()%>" >
                <input type="text" name="comment" placeholder="Enter Comment" required>
                <button type="submit" name="submit" value="addComment" onclick="addItem()">Add Comment</button> 
            </form>
                <%}%>
            
        </div>
       
        <br><br><br><br><br>

        <style>
            .scrollable-div {
                max-height: 200px; /* Set the maximum height for the scrollable div */
                overflow-y: auto; /* Enable vertical scrolling */
            }
            
        </style>
        <script>
            function addItem() {
                var input = document.getElementById("itemInput");
                var item = input.value;
                input.value = "";
                if (item.trim() !== "") {
                    var ul = document.getElementById("list");
                    var li = document.createElement("li");
                    li.appendChild(document.createTextNode(item));
                    ul.appendChild(li);
                }
            }
        </script>

    </body>
</html>
