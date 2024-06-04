
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PassengerDetails extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve flight ID parameter from request
        int flightId = Integer.parseInt(request.getParameter("flightId"));

        // Display booking form
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        
        out.println("<style>");
        // CSS styles
        out.println("body { font-family: Arial, sans-serif; background-color: #f0f7ff;}");
        out.println("h2 { color: #333;}");
        out.println("form { max-width: 400px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);}");
        out.println("input[type='text'], input[type='number'] { width: 100%; padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px;}");
        out.println("select { width: 100%; padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px;}");
        out.println("input[type='submit'] { width: 100%; background-color: #4CAF50; color: white; padding: 14px 20px; margin: 8px 0; border: none; border-radius: 4px; cursor: pointer;}");
        out.println("input[type='submit']:hover { background-color: #45a049;}");
        // End of CSS styles
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
       out.print("<b><p style='text-align:center;font-size:30px'> Book Now!🎫</p></b>");
        out.println("<form action='BookingConfirm'>");

        out.println("Passenger Name: <input type='text' name='name'><br>");
        out.println("Email: <input type='text' name='email'><br>");
        out.println("Phone number: <input type='number' name='phone'><br>");
        out.println("Number of Tickets: <input type='number' name='numTickets'><br>");
        out.println("<select name='tclass'><option value='business'>Business</option><option value='economy'>Economy</option></select><br><br>");
        out.println("<input type='hidden' name='flightId' value='" + flightId + "'>");
        out.println("<input type='submit' value='Book Ticket'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

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
}
