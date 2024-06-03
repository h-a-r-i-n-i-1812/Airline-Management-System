/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author HP
 */
public class CancelTicket extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String name=request.getParameter("name");
        String email=request.getParameter("email");
        int ticket=Integer.parseInt(request.getParameter("ticket"));
        String src=request.getParameter("source");
           String dest=request.getParameter("dest");
           String cls=request.getParameter("clas");
        String time=request.getParameter("time");
        String hourAndMin[]=time.split(":");
        String date=request.getParameter("date");
        Connection conn = null;
    PreparedStatement selectFlightPs = null;
    PreparedStatement pstmt = null;
    PreparedStatement updateFlightPs = null;
    ResultSet rs = null;
    int  rowsUpdated =0;
       PrintWriter out = response.getWriter();
      
      try {
      
        
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        conn = DriverManager.getConnection("jdbc:derby://localhost:1527/TripEasy", "root", "123");
        if(cls.equalsIgnoreCase("Economy"))
        {
          String updateFlightQuery = "UPDATE flight SET economy = economy + ? WHERE source = ? AND destination = ? ";
            pstmt = conn.prepareStatement(updateFlightQuery);
            pstmt.setInt(1, ticket);
            pstmt.setString(2, src);
            pstmt.setString(3, dest);
//            pstmt.setString(4, hourAndMin[0]);
//            pstmt.setString(5, hourAndMin[1]);
//            pstmt.setString(6, date);
            rowsUpdated = pstmt.executeUpdate();
      
        }
        
        else
        {
   
            String updateFlightQuery = "UPDATE flight SET business = business + ? WHERE source = ? AND destination = ? ";
            pstmt = conn.prepareStatement(updateFlightQuery);
            pstmt.setInt(1, ticket);
            pstmt.setString(2, src);
            pstmt.setString(3, dest);
//            pstmt.setString(4, hourAndMin[0]);
//            pstmt.setString(5, hourAndMin[1]);
//            pstmt.setString(6, date);
            rowsUpdated = pstmt.executeUpdate();
        }
            if (rowsUpdated > 0) {
                String deletePassengerQuery = "DELETE FROM passenger WHERE  name= ? and email=? ";
                pstmt = conn.prepareStatement(deletePassengerQuery);
                pstmt.setString(1,name);
                pstmt.setString(2,email);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                      out.println("<script type=\"text/javascript\">");
                out.println("alert('Tickets cancelled successfully');");
                out.println("window.location.href='index.html';");
                out.println("</script>");
                } else {
                    out.println("Failed to cancel ticket. Passenger ID not found.");
                }
            } 
            else {
                out.println("Failed to cancel ticket. Flight details not found.");
            }
                   conn.commit();
        
    } catch (SQLException e) {}
          /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CancelTicket</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CancelTicket at " + time + "</h1>");
            out.println("</body>");
            out.println("</html>");
        
    
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
