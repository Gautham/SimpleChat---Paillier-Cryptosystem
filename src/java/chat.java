/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Base.PaillierCryptoSystem;
import Helpers.DB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gautham
 */
@WebServlet(urlPatterns = {"/chat"})
public class chat extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            if (session.getAttribute("id") != null) {
                       Connection con = DB.GetCon();  
                    Statement st = con.createStatement();
                    String query = "SELECT * FROM `simplechat`.`users` WHERE id = '" + session.getAttribute("id").toString() + "'";
                    java.sql.ResultSet rs = st.executeQuery(query);
                    PaillierCryptoSystem P = new PaillierCryptoSystem();
                    PaillierCryptoSystem Q = new PaillierCryptoSystem();
                    if (rs.next()) { P = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
                    String Me = "<div class='Me'>" + rs.getString(3) + "</div>";
                    if (session.getAttribute("id").equals("00000001")) query = "SELECT * FROM `simplechat`.`users` WHERE id = '00000002'";
                    else query = "SELECT * FROM `simplechat`.`users` WHERE id = '00000001'";
                    rs = st.executeQuery(query);
                    if (rs.next()) { Q = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
                    String OtherGuy = "<div class='OtherGuy'>" + rs.getString(3) + "</div>";
                    query = "SELECT * FROM `simplechat`.`messages` ORDER BY timestamp ASC";
                    rs = st.executeQuery(query);
                    out.println("<div>");
                    while (rs.next()) {
                        out.print("<div class='date'>[" + rs.getString(4) + "]</div>  ");
                        if (session.getAttribute("id").equals(rs.getString(1))) {
                            out.print(Me);
                            out.print(": ");
                            out.print(Q.Decrypt(rs.getString(3)));
                        }
                        else {
                            out.print(OtherGuy);
                            out.print(": ");
                            out.print(P.Decrypt(rs.getString(3)));
                        }
                        out.print("<br/>");
                    }
                    out.println("</div>");
                }
            } catch(Exception e) { out.println(e.toString()); }
                    finally {
            out.close();
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
