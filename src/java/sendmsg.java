/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Base.PaillierCryptoSystem;
import Helpers.DB;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/sendmsg"})
public class sendmsg extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            Connection con = DB.GetCon();  
            Statement st = con.createStatement();
            String query = "SELECT * FROM `simplechat`.`users` WHERE id = '" + session.getAttribute("id").toString() + "'";
            java.sql.ResultSet rs = st.executeQuery(query);
            PaillierCryptoSystem P = new PaillierCryptoSystem();
            PaillierCryptoSystem Q = new PaillierCryptoSystem();
            if (rs.next()) { P = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
            query = "SELECT * FROM `simplechat`.`users` WHERE id = '" + request.getParameter("receiver").toString() + "'";
            rs = st.executeQuery(query);
            if (rs.next()) { Q = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
            BigInteger n = Q.GetPublicKey();
            query = "INSERT INTO `simplechat`.`messages` (`sender`, `receiver`, `message`) VALUES ( '" + session.getAttribute("id").toString() + "', '" + request.getParameter("receiver") + "', '" + P.Encrypt(request.getParameter("msg").toString(), n, n.add(BigInteger.ONE)).toString() + "' )";
            st.executeUpdate(query);
            out.println("<h2>YES</h2>");
        } catch (Exception e) { out.println(e.toString()); }
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
