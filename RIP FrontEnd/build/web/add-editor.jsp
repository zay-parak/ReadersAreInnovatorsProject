<%-- 
    Document   : add-editor
    Created on : 26 Jun 2023, 09:58:12
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.User"%>
<%@page import="java.util.List"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="service.Service"%>

<!DOCTYPE html>
<html>
    <head>
        <title>JARZ - Add an editor</title>
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
            <h1>Add an editor</h1>
        </header>

        <div class="draft-content">
                        <p>${result}</p>

            <%
            // Fetching user data from the database
            RipServiceInterface rsi=Service.getService();
            List<User> userList = rsi.getSpecificUser('W');
            // Assuming you have retrieved user data and populated the userList

            // Generating the HTML table
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table class='table-container'>");
            htmlTable.append("<tr><th>User ID</th><th>Name</th><th>Surname</th><th>Username</th><th>Email</th><th>Type</th><th>Phone Number</th><th>Action</th></tr>");

            for (User user : userList) {
                htmlTable.append("<tr>");
                htmlTable.append("<td>").append(user.getUserID()).append("</td>");
                htmlTable.append("<td>").append(user.getName()).append("</td>");
                htmlTable.append("<td>").append(user.getSurname()).append("</td>");
                htmlTable.append("<td>").append(user.getUsername()).append("</td>");
                htmlTable.append("<td>").append(user.getEmail()).append("</td>");
                htmlTable.append("<td>").append(user.getType()).append("</td>");
                htmlTable.append("<td>").append(user.getPhoneNumber()).append("</td>");
                htmlTable.append("<td><form action='EditorServlet' method='post'>");
                htmlTable.append("<input type='hidden' name='submit' value='addEditor'>");
                htmlTable.append("<input type='hidden' name='userID' value='").append(user.getUserID()).append("'>");
                htmlTable.append("<button type='submit'>Add</button>");
                htmlTable.append("</form></td>");
                htmlTable.append("</tr>");
            }

            htmlTable.append("</table>");
            out.println(htmlTable.toString());
            %>

        </div>

        <script>
            window.onpopstate = function (event) {
                window.location.href = "home.jsp";
            };
        </script>

    </body>
</html>
