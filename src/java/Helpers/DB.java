/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Helpers;

public class DB {
       public static java.sql.Connection GetCon() throws ClassNotFoundException, java.sql.SQLException {
           Class.forName("com.mysql.jdbc.Driver");
           return java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/simplechat", "root", "unisecugtm101");
       } 
}