import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPass=request.getParameter("cpassword");
         PrintWriter out = response.getWriter();
        ResultSet rs = null;

        try {
           
             DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            Connection conn=DriverManager.getConnection("jdbc:derby://localhost:1527/TripEasy","root","123");
               String checkSql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(checkSql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Username already exists, redirect with error message
                HttpSession session = request.getSession();
                session.setAttribute("email", username);
                 out.println("<script type=\"text/javascript\">");
                out.println("alert('Login Successful');");
                out.println("window.location.href='index.html';");
                out.println("</script>");
            }
            else
            {
                   out.println("<script type=\"text/javascript\">");
                out.println("alert('No such account exists');");
                out.println("window.location.href='signUp.html';");
                out.println("</script>");
            }
//            if (rowsAffected > 0) {
//                  response.sendRedirect("login.html");
//           } else {
//                   response.sendRedirect("login.html?error=signup");
//                }
           
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
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
