/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unility;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Phong
 */
public class DBContext_1 {
    static String url="jdbc:sqlserver://localhost\\DESKTOP-QP2E28T\\SQLEXPRESS:1433;databaseName=DA1_GiayRIU1";
    static String username="sa";
    static String password="hh30052002";
    
    static{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
    }
}
}
