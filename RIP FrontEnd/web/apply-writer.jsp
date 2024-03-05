<%-- 
    Document   : apply-writer
    Created on : 26 Jun 2023, 09:57:13
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JARZ - Apply for writer</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="style.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
    </head>
    <body>
        <header class="draft-header">
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1>Apply to be a writer</h1>
        </header>

        <div class="draft-content">
            <form action="ReaderServlet" method="post">
                <label for="motivation">Motivation to be considered for writer role:</label>
                <textarea id="motivation" name="argument" rows="4" cols="50"></textarea>
                <br>
                <input type="submit" value="argument" class="submit-btn" name='Apply'>
            </form>
        </div>
    </body>
</html>
