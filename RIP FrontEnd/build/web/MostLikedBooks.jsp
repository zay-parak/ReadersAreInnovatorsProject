<%--
  Created by IntelliJ IDEA.
  User: TKS
  Date: 29/06/2023
  Time: 22:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<%@page import = "java.util.List"%>
<%@page import = "service.RipServiceInterface" %>
<%@page import = "service.Service" %>
<%@page import = "models.Story" %>
<%@page import = "java.sql.Date" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Most Liked Books Page</title>
        <link rel="stylesheet" href="style.css">
        <!-- Fontawesome CDN Link -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <style>
            .card {
                box-sizing: content-box;
                width: 700px;
                height: 2600px;
                padding: 30px;
                border: 1px solid black;
                background-color: #f0f0f0;
            }
        </style>
    </head>
    <body style="overflow-x: scroll;">
        <header class="draft-header">
            <a href="#" class="logo"> <i class="fas fa-book" style="color: green;"></i> JARZ </a>
            <h1>View statistics</h1>
        </header>
        <div class="container">

            <button id="pdfButton"><b>Click here to Generate report PDF</b></button>
            <div class="card" id="generatePDF">
                <div id="piechart"></div><br>
                <div id="barchart"></div><br>
                <div id="tablechart"></div>
            </div>
        </div>
        <script>
            var button = document.getElementById("pdfButton");
            var makepdf = document.getElementById("generatePDF");
            button.addEventListener("click", function () {
                var mywindow = window.open("", "DOWNLOAD", "height=700,width=700");        //
                mywindow.document.write(makepdf.innerHTML);
                mywindow.document.close();
                mywindow.focus();
                mywindow.print();
                return true;
            });
        </script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

        <script type="text/javascript">
            // Load google charts
            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(drawPieChart);

            // Draw the chart and set the chart values
            function drawPieChart() {
            <%  
                    Integer type=1;
                    String name=type==2?"Rated":"Read";
                    RipServiceInterface service=Service.getService();
                          List<Story> stories = service.topStories(10,type,Date.valueOf("2023-01-01"),Date.valueOf("2023-12-01")) ;
            %>
                var data = google.visualization.arrayToDataTable([['Title', '<%= name %>'],
            <%
                    int size=stories.size();
                    int value;
                    
                    for(int i=0;i<size;i++) {
                    Story story=stories.get(i);
                    
                       if(type==2){value=story.getRating();}
                       else{value=story.getNumOfReads();} %>
                    ["<%= story.getTitle() %>",<%= value %>],
            <% } %>



                ]);
                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Most <%= name %> Books', 'width': 700, 'height': 700};

                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.PieChart(document.getElementById('piechart'));
                chart.draw(data, options);
            }
        </script>
        <script type="text/javascript">
            // Load google charts
            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(drawBarChart);

            // Draw the chart and set the chart values
            function drawBarChart() {

                var data = google.visualization.arrayToDataTable([['Title', '<%= name %>'],
            <%
                    for(int i=0;i<size;i++) {
                    Story story=stories.get(i);
                    
                       if(2==2){value=story.getRating();}
                       else{value=story.getNumOfReads();} 
                        if(2==2){value=story.getRating();}
                       else{value=story.getNumOfReads();} %>
                    ["<%= story.getTitle() %>",<%= value %>],
            <% } %>

                ]);

                // Optional; add a title and set the width and height of the chart
                var options = {'title': 'Most <%= name %> Books', 'width': 700, 'height': 1000};

                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.BarChart(document.getElementById('barchart'));
                chart.draw(data, options);
            }
        </script>
        <script type="text/javascript">
            // Load google charts
            google.charts.load('current', {'packages': ['table']});
            google.charts.setOnLoadCallback(drawTableChart);

            // Draw the chart and set the chart values
            function drawTableChart() {


                var data = google.visualization.arrayToDataTable([['Title', '<%= name %>'],
            <%
                    for(int i=0;i<size;i++) {
                    Story story=stories.get(i);
                    
                       if(2==2){value=story.getRating();}
                       else{value=story.getNumOfReads();} 
                        if(2==2){value=story.getRating();}
                       else{value=story.getNumOfReads();} %>
                    ["<%= story.getTitle() %>",<%= value %>],
            <% } %>

                ]);

                // Optional; add a title and set the width and height of the chart
                var options = {
                    showRowNumber: true,
                    width: '100%',
                    height: '100%'
                };

                // Display the chart inside the <div> element with id="piechart"
                var chart = new google.visualization.Table(document.getElementById('tablechart'));
                chart.draw(data, options);
            }
        </script>
    </body>
</html>
