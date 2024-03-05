<!DOCTYPE html>
<%@page import="java.util.List,models.Genre,service.Service,service.RipServiceInterface"  %>
<%@page import="service.Service"%>

<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="RegistrationForm.css">

        <title>Registration</title>
    </head>
    <body id="Body">

        <header>
            <h2 class="logo"><ion-icon name="book" style="margin-right: 5px;"></ion-icon></i>JARZ</h2>
            <nav class="FrontPage">
                <a href="http://localhost:8080/RIP_FrontEnd/LoginRegisterServlet?submit=guest">Login as guest</a>
                <a href="#" id="Signup">Sign-Up</a>
                <button id="Login">Login</button>
            </nav>

        </header>
        <%
                String message= (String) request.getAttribute("result");
                if(message!=null){
        %>
        <h2 class="result"> <%= message %> </h2> 
        <%}%>

        <!-- Login Popup -->
        <form class="Register" action="LoginRegisterServlet" method="POST" id="LoginForm">
            <h2>Login</h2>
            <span class="close" onclick="Close()"><ion-icon name="close-outline"></ion-icon></span>
            <div class="Register-Div">
                <label>Email</label>
                <input type="email" required name="email" value="zayboyboy786@gmail.com">
                <span class="icon"><ion-icon name="mail"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Password</label>
                <input type="password" required name="password" value="zayaan" >
                <span class="icon"><ion-icon name="lock-closed"></ion-icon></span>
            </div>
            <div class="Register-Verify">
                <label><input checked="true" type="checkbox" id="Verified">Verified</label>
            </div>
            <div class="Register-Div" id="Code">
                <label>Code</label>
                <input type="text" name="code">
                <span class="icon"><ion-icon name="code-outline"></ion-icon></ion-icon></span>
            </div>
            <input class="button" name="submit" type="submit" value="Login" />
        </form>

        <!-- Sign-Up Popup -->
        <form class="Register" action="LoginRegisterServlet" method="POST" id="SignupForm">
            <br><br><br><br><br><br><br><br><br><br>
            <h2>Sign-Up</h2>
            <span class="close" onclick="Close()"><ion-icon name="close-outline"></ion-icon></span>
            <div class="Register-Div">
                <label>Name</label>
                <input type="text" name="name">
                <span class="icon"><ion-icon name="person"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Surname</label>
                <input type="text" name="surname">
                <span class="icon"><ion-icon name="person"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Username</label>
                <input type="text" required name="username">
                <span class="icon"><ion-icon name="person"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Email</label>
                <input type="email" required name="email">
                <span class="icon"><ion-icon name="mail"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Phone number</label>
                <input type="text" required name="phoneNumber">
                <span class="icon"><ion-icon name="call"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Password</label>
                <input type="password" required name="password" id="psw">
                <span class="icon"><ion-icon name="lock-closed"></ion-icon></span>
            </div>
            <div class="Register-Div">
                <label>Confirm Password</label>
                <input type="password" required id="pswC">
                <span class="icon"><ion-icon name="lock-closed"></ion-icon></span>
            </div>
            <label>Genres (Pick ones that you prefer)</label><br>
            <div class="genre">            
                

                <%
               RipServiceInterface rsi=Service.getService();
                List<Genre> genres=rsi.getGenres();
                for(Genre g:genres){
                %> 
                <label><input type="checkbox" name="choice" value="<%=g.getGenreID()%>"><%=g.getName()%></label>
                    <%}%> 
            </div>

            <input class="button" name="submit" type="submit" value="Sign Up" />
        </form>


    </body>
</html>
<script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
<!-- Script for Code <input> showing and Close Buttons -->
<script>
                var checkbox = document.getElementById("Verified");

                checkbox.addEventListener('change', function () {
                    if (this.checked) {
                        document.getElementById("Code").style.visibility = 'hidden';
                    } else {
                        document.getElementById("Code").style.visibility = 'visible';
                    }
                });

                var Login = document.getElementById("Login");
                Login.addEventListener('click', function () {
                    document.getElementById("SignupForm").style.display = "none";
                    document.getElementById("LoginForm").style.display = "flex";
                });

                var Signup = document.getElementById("Signup");
                Signup.addEventListener('click', function () {
                    document.getElementById("LoginForm").style.display = "none";
                    document.getElementById("SignupForm").style.display = "flex";
                });

                function Close() {
                    document.getElementById("LoginForm").style.display = "none";
                    document.getElementById("SignupForm").style.display = "none";
                }
</script>
<!-- Script for Password match check -->
<script>
    var password = document.getElementById("psw")
            , confirm_password = document.getElementById("pswC");
    function validatePassword() {
        if (password.value !== confirm_password.value) {
            confirm_password.setCustomValidity("Passwords Don't Match");
        } else {
            confirm_password.setCustomValidity('');
        }
    }
    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;
</script>