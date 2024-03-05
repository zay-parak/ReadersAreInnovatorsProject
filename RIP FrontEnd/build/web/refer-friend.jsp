<%-- 
    Document   : refer-friend
    Created on : 03 Jul 2023, 01:15:47
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
        <link rel="stylesheet" href="style.css" type="text/css">
        <title>Refer a friend</title>
    </head>
    <body>
        <header class="draft-header">
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1>Refer a friend</h1>
        </header>

        <div class="draft-content">
            <form action="ReferFriend" method="get">
                <div class="form-group">
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" placeholder="Enter phone number" required>
                </div>
                <div class="form-group">
                    <label for="message">Message:</label>
                    <textarea id="message" name="message" placeholder="Enter your message" required></textarea>
                </div>
                <div class="form-group">
                    <button type="submit">Send SMS</button>
                </div>
            </form>
            
            <p>${result}</p>
        </div>

    </body>
</html>
