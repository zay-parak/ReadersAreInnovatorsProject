<%-- 
    Document   : AnalyticChoice
    Created on : 23 Jun 2023, 09:22:19
    Author     : matth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Analytic Page</title>
        <link rel="stylesheet" href="style.css">
        <!-- Fontawesome CDN Link -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>

    <style>
        /* CSS for AnalyticChoice page */

        /* Global styles */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f2f2f2;
        }

        h1 {
            color: #333;
        }


        /* Content styles */
        .draft-content {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
        }

        /* Input box styles */
        .input-boxes {
            margin-bottom: 20px;
        }

        .input-box {
            margin-bottom: 10px;
        }

        input[type="number"],
        input[type="date"],
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button[type="submit"] {
            background-color: #4caf50;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        /* Additional styles */
        p {
            margin: 0;
        }

        /* Responsive styles */
        @media screen and (max-width: 600px) {
            .draft-content {
                padding: 10px;
            }
        }

    </style>
    <body>
        <header class="draft-header">
            <a href="home.jsp" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1>View statistics</h1>
        </header>
        <div class="draft-content">
            <h1>Story Analytic's</h1>
            <form action="MostLikedBooks.jsp" method="get">
                <div class="input-boxes">
                    <div class="input-box">
                        <lable>Amount:</lable>
                        <input type="number" name="amount" min="1" max="30" required>
                    </div>
                    <p style="color:black">Top Stories </p><br>
                    <label>Choose type of Story data:</label>
                    <select name="type">
                        <option value="1">Read</option>
                        <option value="2">Rated</option>
                    </select>
                    <p style="color:black">Between: </p><br>
                    <div class="input-box">
                        <input type="date" name="start" required>
                    </div>
                    <p style="color:black">And: </p><br>
                    <div class="input-box">
                        <input type="date" name="end" required>
                    </div>
                    <div class="button input-box">
                        <input type="submit" name="submit" value="SUBMIT STORIES CHOICE">
                    </div>
                </div>
            </form><br>
            <h1>User Analytic's</h1>
            <form action="MostLikedUsers.jsp" method="get">
                <div class="input-boxes">
                    <div class="input-box">
                        <lable>Amount:</lable>
                        <input type="number" name="amount" min="1" max="30" required>
                    </div>
                    <p style="color:black">Top Users </p><br>
                    <label>Choose type of User data:</label>
                    <select name="type">
                        <option value="1">Most accepting editor</option>
                        <option value="2">Most viewed Writer</option>
                    </select>
                    <p style="color:black">Between: </p><br>
                    <div class="input-box">
                        <input type="date" name="start" required>
                    </div>
                    <p style="color:black">And: </p><br>
                    <div class="input-box">
                        <input type="date" name="end" required>
                    </div>
                    <div class="button input-box">
                        <input type="submit" name="submit" value="SUBMIT USER CHOICE">
                    </div>
                </div>
            </form><br>
            <h1>Genre Analytic's</h1>
            <form action="MostLikedGenres.jsp" method="get">
                <div class="input-boxes">
                    <div class="input-box">
                        <lable>Amount:</lable>
                        <input type="number" name="amount" min="1" max="30" required>
                    </div>
                    <p style="color:black">Top Genres </p><br>
                    <p style="color:black">Between: </p><br>
                    <div class="input-box">
                        <input type="date" name="start" required>
                    </div>
                    <p style="color:black">And: </p><br>
                    <div class="input-box">
                        <input type="date" name="end" required>
                    </div>
                    <div class="button input-box">
                        <input type="submit" name="submit" value="SUBMIT GENRE CHOICE">
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
