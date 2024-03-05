<%-- 
    Document   : home
    Created on : 28 Jun 2023, 14:54:30
    Author     : Zayaan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Story"%>
<%@page import="models.Genre"%>
<%@page import="service.RipServiceInterface"%>
<%@page import="models.User"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="service.Service"%>
<%@page import="java.sql.Date"%>
<%@page import="java.time.LocalDateTime"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JARZ RIP PROGRAM</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600&display=swap">
        <link rel="stylesheet" href="style.css" type="text/css">
    </head>
    <body>
        <div class="main-content">
            <header class="header">
                <div class="header-1">
                    <a href="home.jsp" class="logo"> <i class="fas fa-book"></i> JARZ </a>

                    <form class="search-form" action="SearchServlet" method="get">
                        <input type="text" name="searchName" placeholder="search here..." id="search-box" required>
                        <button type="submit" value="searchName" class="fas fa-search"></button>
                    </form>

                    <% 
                      User guestCheck = (User)request.getSession(false).getAttribute("user");
                      Boolean hide = false;
                        if(guestCheck.getType() == 'G'){%>
                    <div class="icons">
                        <div id="search-btn" class="fas fa-search" style="pointer-events: none" title="You need to register to read stories"></div>
                        <a href="home.jsp?list=3" class="fa fa-heart" title="Favorites" style="pointer-events: none" title="You need to register to read stories"></a>
                        <div id="profile-btn" class="fas fa-user" title="Profile" onclick="populateProfileFields()" style="pointer-events: none" title="You need to register to read stories"></div>
                        <div id="menu-btn" class="fas fa-bars" title="Menu" style="pointer-events: none"  title="You need to register to read stories"></div>
                    </div>
                </div>
                <%}else{%>

                <div class="icons">
                    <div id="search-btn" class="fas fa-search"></div>
                    <a href="home.jsp?list=3" class="fa fa-heart" title="Favorites"></a>
                    <div id="profile-btn" class="fas fa-user" title="Profile" onclick="populateProfileFields()"></div>
                    <div id="menu-btn" class="fas fa-bars" title="Menu"></div>
                </div>
        </div>
        <%}%>

        <div class="header-2">
            <nav class="navigationbar">
                <a href="home.jsp?list=0">home</a>
                <a href="home.jsp?list=1">explore</a>
                <a href="home.jsp?list=7">daily read</a>
                <a href="home.jsp?list=4">top 10 liked</a>
                <a href="home.jsp?list=5">top 10 rated</a>
                <a href="home.jsp?list=6">top 10 read</a>
            </nav>
        </div>
    </header>   

    <section  class="home" id="home" >
                            <%
                        RipServiceInterface rsi = Service.getService();
                        String sectionName = null;
          LocalDateTime time=LocalDateTime.now();
        int year=time.getYear(), month=time.getMonthValue(), day=time.getDayOfMonth();
        String start=year+"-"+month+"-01";
        String end=year+"-"+(month+1)+"-01";

        String startDay=year+"-"+month+"-"+day;
        String endDay=year+"-"+month+"-"+(day+1);
                
        Integer limite=1;
        String value=request.getParameter("limite");
        if(value!=null){limite=Integer.valueOf(value);}
        String listPH=request.getParameter("list");
        Integer list;
        if(listPH!=null){list=Integer.valueOf(listPH);}else{list=1;}
        List<Story> stories=new ArrayList<>();
        switch(list){
            case 0:stories=rsi.getRecommended(limite);
                sectionName = "Recommended";
            break;
            case 1:stories=rsi.getAllStories(limite);
                sectionName = "Explore";
            break;
            case 2:stories=rsi.getAllDrafts();
                sectionName = "My Drafts";
            break;
            case 3:stories=rsi.getFavourites(limite);
                sectionName = "My Favourites";
            break;
            case 4:stories=rsi.topStories(10, 1, Date.valueOf(start), Date.valueOf(end));
                sectionName = "Top 10 liked stories for the month";
            break;
            case 5:stories=rsi.topStories(10, 2, Date.valueOf(start), Date.valueOf(end));
                sectionName = "Top 10 rated stories for the month";
            break;
            case 6:stories=rsi.topStories(10, 3, Date.valueOf(start), Date.valueOf(end));
                sectionName = "Top 10 read stories for the month";
            break;
            case 7:stories=rsi.topStories(1, 3, Date.valueOf(startDay), Date.valueOf(endDay));
                sectionName = "Daily Read";
            break;
            case 8:stories=(List<Story>)request.getAttribute("search");
            sectionName = (String)request.getAttribute("heading");
            break;
        }%>
                            <h1 class="heading"><span><%=sectionName%></span></h1>

        <div class="row" >
            <div class="book-slider">
                <div class="wrapper" id="image-wrapper">
                   <%
     request.getSession(false).setAttribute("stories",stories);
             
    for (int i = 0; i < stories.size(); i++) {
    Story story = stories.get(i);
    hide = story.getHideStory();
                    %>
                    
                    <a href="#" class="book-link"
                       onclick="displayPopupForm('<%= i %>', '<%= story.getPicture() %>', '<%= story.getTitle() %>','<%= story.getDescription() %>', '<%= story.getAuthor() %>', '<%= story.getRating() %>', '<%= story.getNumOfReads() %>')">
                        <img src="books/<%= story.getPicture() %>" alt="hello">
                    </a>
                    <%
                        }
                    %>
                </div>
            </div>                
        </div>
        <div class="page">
            <%List<Integer> allow= List.of(0,1,3);
                 if(allow.contains(list)){%>
            <a <% if (limite > 1) { %>href="home.jsp?list=<%=list%>&limite=<%=limite-1%>"<% } %> class="arrow-link"<% if (limite <= 0) { %> disabled<% } %>>Previous</a>
            <a href="home.jsp?list=<%=list%>&limite=<%=limite+1%>" class="arrow-link" >Next</a>
            <%}%>
        </div>    
    </section>



    <div class="popup-form" id="popup-form">

        <div class="form-content">
            <div class="image-container">
                <img src="" alt="" style="width:175px;height:175px" id="popup-image">
            </div>
            <div class="heading-container">
                <h1 id="popup-title"></h1>
                <p id="popup-author"></p>

                <div class="ratingLike">
                    <div class="rounded-block">
                        <h3>Rating</h3>
                        <p id="rating-value"></p>
                    </div>

                    <div class="rounded-block">
                        <h3>Likes</h3>
                        <p id="reads-value"></p>
                    </div>
                </div>
            </div>

            <span class="form-close-btn" onclick="closePopupForm()">x</span>
        </div>
        <div class="text-container">
            <textarea readonly id="popup-description"></textarea><br>
        </div>
    
        <a class="read-btn" id="read-button">Read</a>
        
        <%
         if((guestCheck.getType() == 'G')||(hide)){%>

         <a class="read-btn" id="read-button" title="You need to register to read stories"disabled>Read</a>
        <% } %>
    </div>



    <div class="side-menu" id="side-menu">
        <ul> 
            <% 
                User u  = rsi.getUser();
                 if (u.getType() == 'A') { %>
            <li><a href="my-stories.jsp">My stories</a></li>
            <li><a href="home.jsp?list=2">My drafts</a></li>
            <li><a href="write-story.jsp">Write a story</a></li>
            <li><a href="apply-writer.jsp">Apply to be a writer</a></li>
            <li><a href="approve-writer.jsp">Approve writer</a></li>
            <li><a href="block-writer.jsp">Block writer</a></li>
            <li><a href="approve-stories.jsp">Approve stories</a></li>
            <li><a href="add-editor.jsp">Add editor</a></li>
            <li><a href="AnalyticChoice.jsp">View stats</a></li>
            <li><a href="LoginRegisterServlet?submit=logout">Logout</a></li>
                <% } else if (u.getType() == 'R') { %>
            <li><a href="apply-writer.jsp">Apply to be a writer</a></li>
            <li><a href="LoginRegisterServlet?submit=logout">Logout</a></li>
                <% } else if (u.getType() == 'W') { %>
            <li><a href="my-stories.jsp">My stories</a></li>
            <li><a href="home.jsp?list=2">My drafts</a></li>
            <li><a href="write-story.jsp">Write a story</a></li>
            <li><a href="LoginRegisterServlet?submit=logout">Logout</a></li>
                <% } else if (u.getType() == 'E') { %>
            <li><a href="approve-writer.jsp">Approve writer</a></li>
            <li><a href="block-writer.jsp">Block writer</a></li>
            <li><a href="approve-stories.jsp">Approve stories</a></li>
            <li><a href="add-editor.jsp">Add editor</a></li>
            <li><a href="AnalyticChoice.jsp">View stats</a></li>
            <li><a href="LoginRegisterServlet?submit=logout">Logout</a></li>
                <% } else if (u.getType() == 'B') { %>
            <li><a href="drafts.jsp">My drafts</a></li>
            <li><a href="my-stories.jsp">My stories</a></li>
            <li><a href="LoginRegisterServlet?submit=logout">Logout</a></li>
                <% } else { %>
            <li><a href="LoginRegisterServlet?submit=logout">Logout Guest</a></li>
                <% } %>
        </ul>
    </div>

    <div id="overlay" style="display: none">
        <div id="overlay-content"></div>
    </div>

    <div class="overlay"></div>

    <div class="modal" id="modal">
        <div class="modal-content">
            <div class="profile-form" id="profile-form">
                <h2>Profile</h2>
                <form class="edit-profile-form" action="HomePageServlet" method="post">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" required><br><br>

                    <label for="surname">Surname:</label>
                    <input type="text" id="surname" name="surname" required><br><br>

                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required><br><br>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required><br><br>

                    <label for="phone">Phone Number:</label>
                    <input type="tel" id="phone" name="phone" required><br><br>

                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required><br><br>
                    
                    <label for="genre"> Your Genres:</label>
                    <div class="genre">
                        <%
                        List<Genre> genres=rsi.getGenres();
                        request.getSession(false).setAttribute("genres",genres);
                        Boolean hasGenre;
                        for(Genre g:genres){
                        hasGenre=false;
                            for(Genre user:u.getFavourites()){
                                if(g.getGenreID()==user.getGenreID()){
                                hasGenre=true;
                            }}
                        %> 
                        <label><input type="checkbox" name="choice"<% if(hasGenre){%>checked=""<%}%>  value="<%=g.getGenreID()%>"><%=g.getName()%></label>
                            <%}%> 
                    </div>
                    <br><br><br><br><br>

                    <button type="submit" name="submit" value="update" id="update-btn" >Update details</button>
                    <a href="refer-friend.jsp" type="button" id="refer-btn">Refer a friend</a> 
                    <button type="button" id="close-btn">Back</button>
                </form>
            </div>
        </div>
    </div>   
</div>   
<script>


    function displayPopupForm(id, picture, title,description, author, rating, numReads) {
        // Show the pop-up form
        var popupForm = document.getElementById('popup-form');
        var popupImage = document.getElementById('popup-image');
        var popupTitle = document.getElementById('popup-title');
        var popupDescription = document.getElementById('popup-description');
        var popupAuthor = document.getElementById('popup-author');
        var popupRating = document.getElementById('rating-value');
        var popupReads = document.getElementById('reads-value');
        var readButton = document.getElementById('read-button');


        popupImage.src = 'books/' + picture;
        popupTitle.textContent = title;
        popupDescription.textContent = description;
        popupAuthor.textContent = 'By: ' + author;
        popupRating.textContent = rating;
        popupReads.textContent = numReads;

        popupForm.style.display = 'block';
        var imageWrap = document.getElementById('image-wrapper');
        imageWrap.style.pointerEvents = 'none';
        disableScrolling();

        readButton.addEventListener('click', function (event) {
            event.preventDefault();
            window.location.href = "http://localhost:8080/RIP_FrontEnd/StoryAttributesServlet?submit=setStoryData&index=" + id;
        });
    };

    function closePopupForm() {
        // Close the pop-up form
        var popupForm = document.getElementById('popup-form');
        var imageWrap = document.getElementById('image-wrapper');

        popupForm.style.display = 'none';
        imageWrap.style.pointerEvents = 'all';
        enableScrolling();
    }

    function disableScrolling() {
        var x = window.scrollX;
        var y = window.scrollY;
        window.onscroll = function () {
            window.scrollTo(x, y);
        };
    }

    function enableScrolling() {
        window.onscroll = function () {};
    }


    // Side Menu Functionality
    var menuBtn = document.getElementById('menu-btn');
    var sideMenu = document.getElementById('side-menu');

    var isMenuOpen = false; // Track the menu state

    menuBtn.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent the click event from bubbling up

        isMenuOpen = !isMenuOpen; // Toggle the menu state
        updateMenuVisibility();
        // disableScrolling();
    });

    // Close the menu when clicking outside the side menu
    document.addEventListener('click', function () {
        if (isMenuOpen) {
            isMenuOpen = false;
            updateMenuVisibility();
        }
        //  enableScrolling();
    });

    function updateMenuVisibility() {
        if (isMenuOpen) {
            sideMenu.classList.add('open');
            disableScrolling();
        } else {
            sideMenu.classList.remove('open');
            enableScrolling();
        }
    }

    var overlay = document.querySelector('.overlay');
    var modal = document.getElementById('modal');

// Function to display the modal
    function showModal() {
        overlay.style.display = 'block';
        modal.style.display = 'block';
    }

// Function to close the modal
    function closeModal() {
        overlay.style.display = 'none';
        modal.style.display = 'none';
    }

// Example usage
    var profileBtn = document.getElementById('profile-btn');
    profileBtn.addEventListener('click', function () {
        disableScrolling();
        showModal();
    });

    var closeBtn = document.getElementById('close-btn');
    closeBtn.addEventListener('click', function () {
        closeModal();
        enableScrolling();
    });

    var profileBtn = document.getElementById('profile-btn');
    var profileForm = document.getElementById('profile-form');
    var editProfileForm = document.getElementById('edit-profile-form');

    profileBtn.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent the click event from bubbling up
        displayProfileForm();
    });


    editProfileForm.addEventListener('submit', function (event) {
        event.preventDefault();
        saveProfile();
    });

    function displayProfileForm() {
        // Show the profile form
        populateProfileFields();
        profileForm.style.display = 'block';
        disableScrolling();
    }

    function closeProfileForm() {
        // Close the profile form
        profileForm.style.display = 'none';
        enableScrolling();
    }

    function populateProfileFields() {
    <%
    User updateUser = rsi.getUser();
                    
    %>
        // Populate the profile form fields with user details
        document.getElementById('name').value = "<%=updateUser.getName()%>";
        document.getElementById('surname').value = "<%=updateUser.getSurname()%>";
        document.getElementById('username').value = "<%=updateUser.getUsername()%>";
        document.getElementById('email').value = "<%=updateUser.getEmail()%>";
        document.getElementById('phone').value = "<%=updateUser.getPhoneNumber()%>";
    }

</script>
</body>
</html>
