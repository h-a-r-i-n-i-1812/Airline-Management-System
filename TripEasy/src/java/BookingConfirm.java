
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

public class BookingConfirm extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int id = Integer.parseInt(request.getParameter("flightId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        long phone = Long.parseLong(request.getParameter("phone"));
        int num = Integer.parseInt(request.getParameter("numTickets"));
        String tclass = request.getParameter("tclass");

        Flight flight = fetchFlightFromDatabase(id, name, num, tclass, email, phone, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private Flight fetchFlightFromDatabase(int flightId, String name, int numTickets, String ticketClass, String email, long phone, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement selectFlightPs = null;
        PreparedStatement insertPassengerPs = null;
        PreparedStatement updateFlightPs = null;
        ResultSet rs = null;
        Flight flight = null;
        try {
            // Establish database connection
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/TripEasy", "root", "123");
            conn.setAutoCommit(false); // Start transaction

            // Select flight row for updating
            String selectFlightQuery = "SELECT * FROM flight WHERE id = ? FOR UPDATE";
            selectFlightPs = conn.prepareStatement(selectFlightQuery);
            selectFlightPs.setInt(1, flightId);
            rs = selectFlightPs.executeQuery();

            // Check if the result set contains a row
            if (rs.next()) {
                int id = rs.getInt("id");
                String airline = rs.getString("airline");
                String source = rs.getString("source");
                String destination = rs.getString("destination");
                int economyTickets = rs.getInt("economy");
                int businessTickets = rs.getInt("business");
                int economyPrice = rs.getInt("economyprice");
                int businessPrice = rs.getInt("businessprice");
                Timestamp timestamp = rs.getTimestamp("departure");
                Date date = new Date(timestamp.getTime());

                // Format the Date to retrieve only the date part
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(date);
                flight = new Flight(id, airline, source, destination, economyTickets, businessTickets, economyPrice, businessPrice);

                // Check ticket availability
                boolean isAvailable = false;
                int ticketPrice = 0;
                if ("economy".equalsIgnoreCase(ticketClass) && numTickets <= economyTickets) {
                    isAvailable = true;
                    ticketPrice = numTickets * economyPrice;
                } else if ("business".equalsIgnoreCase(ticketClass) && numTickets <= businessTickets) {
                    isAvailable = true;
                    ticketPrice = numTickets * businessPrice;
                }

                if (isAvailable) {
                    // Insert passenger details into the passenger table
                    String insertPassengerQuery = "INSERT INTO passenger  VALUES (?, ?, ?, ?,?,?,?,?)";
                    insertPassengerPs = conn.prepareStatement(insertPassengerQuery);
                    insertPassengerPs.setString(1, name);
                    insertPassengerPs.setString(2, email);
                    insertPassengerPs.setLong(3, phone);
                    insertPassengerPs.setInt(4, numTickets);
                    insertPassengerPs.setString(5, ticketClass);
                    insertPassengerPs.setString(6, source);
                    insertPassengerPs.setString(7, destination);
                    insertPassengerPs.setString(8, formattedDate);
                    insertPassengerPs.executeUpdate();

                    // Update flight table to decrement available tickets
                    String updateFlightQuery = "";
                    if ("economy".equalsIgnoreCase(ticketClass)) {
                        updateFlightQuery = "UPDATE flight SET economy = economy - ? WHERE id = ?";
                    } else if ("business".equalsIgnoreCase(ticketClass)) {
                        updateFlightQuery = "UPDATE flight SET business = business - ? WHERE id = ?";
                    }
                    updateFlightPs = conn.prepareStatement(updateFlightQuery);
                    updateFlightPs.setInt(1, numTickets);
                    updateFlightPs.setInt(2, flightId);
                    updateFlightPs.executeUpdate();

                    // Commit transaction
                    conn.commit();
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Booking Confirmation</title>");
                        out.println("<style>");
                        out.println("body { font-family: Arial, sans-serif; background-color: #f2f2f2;}");
                        out.println("h1 { color: #333; text-align: center;}");
                        out.println(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}");
                        out.println(".confirmation-message { text-align: center; margin-top: 20px;}");
                        out.println(".button { display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px;}");
                        out.println(".button:hover { background-color: #45a049;}");
                        out.println("</style>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<div class='container'>");
                        out.println("<h1>Booking Confirmation</h1>");
                        out.println("<div class='confirmation-message'>");
                        out.println("<p>Dear " + name + ",</p>");
                        out.println("<p>Your booking has been confirmed for the following details:</p>");
                        out.println("<p><strong>Flight ID:</strong> " + id + "</p>");
                        out.println("<p><strong>Name:</strong> " + name + "</p>");
                        out.println("<p><strong>Email:</strong> " + email + "</p>");
                        out.println("<p><strong>Phone:</strong> " + phone + "</p>");
                        out.println("<p><strong>Source:</strong> " + source + "</p>");
                        out.println("<p><strong>Destination:</strong> " + destination + "</p>");
                        out.println("<p><strong>Date:</strong> " + date + "</p>");
                        out.println("<p><strong>Number of Tickets:</strong> " + numTickets + "</p>");
                        out.println("<p><strong>Class:</strong> " + ticketClass + "</p>");
                        out.println("<p><strong>Amount: ₹</strong> " + ticketPrice + "</p>");
                        out.println("<p>Thank you for booking with us!</p>");
                        out.println("<a href='index.html' class='button'>Go Back</a>");
                        out.println("<button class ='button' onclick=\"window.print()\">Print this page</button>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                } else {
                    // If tickets are not available, redirect to checkAvailability.html with an alert
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Requested number of tickets is not available.');");
                        out.println("window.location.href = 'checkAvailability.html';");
                        out.println("</script>");
                    }
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (selectFlightPs != null) {
                    selectFlightPs.close();
                }
                if (insertPassengerPs != null) {
                    insertPassengerPs.close();
                }
                if (updateFlightPs != null) {
                    updateFlightPs.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flight;
    }

    class Flight {
        private int id;
        private String airline

;
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
}
