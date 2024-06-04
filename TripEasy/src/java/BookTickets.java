

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;
public class BookTickets extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Fetch flight information from database

        String source=request.getParameter("source");
        String dest=request.getParameter("dest");
    
            List<Flight> flights = fetchFlightsFromDatabase(source,dest);
       out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Available filghts✈️</title>");
        out.println("<style>");
        out.println("body { background-color: #f0f7ff; }");
        out.println("table {");
        out.println("border-collapse: collapse;");
        out.println("width: 100%;");
        out.println("}");
        out.println("th, td {");
        out.println("border: 1px solid #dddddd;");
        out.println("padding: 8px;");
        out.println("text-align: left;");
        out.println("}");
        out.println("th {");
        out.println("background-color: #f2f2f2;");
        out.println("}");
        out.println("tr:nth-child(even) {");
        out.println("background-color: #f2f2f2;");
        out.println("}");
        out.println("tr:hover {");
        out.println("background-color: #ddd;");
        out.println("}");
        out.println("h2 {");
        out.println("text-align: center;");
        out.println("}");
        out.println(".container {");
        out.println("width: 80%;");
        out.println("margin: auto;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<b><h2>Available filghts✈</h2></b>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Airline</th>");
        out.println("<th>Source</th>");
        out.println("<th>Destination</th>");
        out.println("<th>Economy Tickets Available</th>");
        out.println("<th>Business Tickets Available</th>");
        out.println("<th>Economy Price</th>");
        out.println("<th>Business Price</th>");
        out.println("<th>Book Now</th>");
        out.println("</tr>");
        for (Flight flight : flights) {
            out.println("<tr>");
            out.println("<td>" + flight.getAirline() + "</td>");
            out.println("<td>" + flight.getSource() + "</td>");
            out.println("<td>" + flight.getDestination() + "</td>");
            out.println("<td>" + flight.getEconomyTickets() + "</td>");
            out.println("<td>" + flight.getBusinessTickets() + "</td>");
            out.println("<td>" + flight.getEconomyPrice() + "</td>");
            out.println("<td>" + flight.getBusinessPrice() + "</td>");
          out.println("<td><form action='PassengerDetails'><input type='hidden' name='flightId' value='" + flight.getId() + "'><input type='submit' value='Buy Ticket'></form></td>");
            //out.println("<td><form action='PassengerDetails'><input type='submit' value='Buy Ticket'></form></td>");
          out.println("</tr>");
        }
        out.println("</table>");
        //out.println("<a href='bookTicket.html'>Book Tickets</a>");
        out.println("</body>");
        out.println("</html>");
    }
private List<Flight> fetchFlightsFromDatabase(String src,String dest) {
        List<Flight> flights = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Establish database connection
             
             DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
             conn=DriverManager.getConnection("jdbc:derby://localhost:1527/TripEasy","root","123");

            // Execute SQL query to fetch flight information
           String query = "SELECT * FROM flight WHERE source = ? AND destination = ?";
                         ps = conn.prepareStatement(query);
                    ps.setString(1, src);
                  ps.setString(2, dest);

           
            rs = ps.executeQuery();
           
            // Iterate through the result set and populate the flights list
            while (rs.next()) {
                int id = rs.getInt("id");
                String airline = rs.getString("airline");
                String source = rs.getString("source");
                String destination = rs.getString("destination");
                int economyTickets = rs.getInt("economy");
                int businessTickets = rs.getInt("business");
                int economyPrice = rs.getInt("economyprice");
               int businessPrice = rs.getInt("businessprice");

                Flight flight = new Flight(id, airline, source, destination, economyTickets, businessTickets, economyPrice, businessPrice);
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flights;
    }

    // Flight class
    class Flight {
        private int id;
        private String airline;
        private String source;
        private String destination;
        private int economyTickets;
        private int businessTickets;
        private double economyPrice;
        private double businessPrice;

        public Flight(int id, String airline, String source, String destination, int economyTickets, int businessTickets, double economyPrice, double businessPrice) {
            this.id = id;
            this.airline = airline;
            this.source = source;
            this.destination = destination;
            this.economyTickets = economyTickets;
            this.businessTickets = businessTickets;
            this.economyPrice = economyPrice;
            this.businessPrice = businessPrice;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getAirline() {
            return airline;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public int getEconomyTickets() {
            return economyTickets;
        }

        public int getBusinessTickets() {
            return businessTickets;
        }

        public double getEconomyPrice() {
            return economyPrice;
        }

        public double getBusinessPrice() {
            return businessPrice;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
