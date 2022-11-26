/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Unility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import repository.DB_Context;

/**
 *
 * @author ACER
 */
public class JDBC_Helper {
    public static ResultSet selectTongQuat(String sql, Object... paramas){
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection con = null;
        
        try {
            con = DB_Context.getContext();
            pstm = con.prepareStatement(sql);
            for(int i=0; i<paramas.length; i++){
                pstm.setObject(i+1, paramas[i]);
            }
            rs = pstm.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            close(con, pstm, rs);
            return null;
        }
    }
    
    public static int updateTongQuat(String sql, Object... paramas){
        PreparedStatement pstm = null;
        Connection con = null;
        
        try {
            con = DB_Context.getContext();
            pstm = con.prepareStatement(sql);
            for(int i=0; i<paramas.length; i++){
                pstm.setObject(i+1, paramas[i]);
            }
            return pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            close(con, pstm);
            return 0;
        }
    }
    
    public static void close(Connection con, PreparedStatement pstm){
        try {
            pstm.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void close(Connection con, PreparedStatement pstm, ResultSet rs){
        try {
            rs.close();
            close(con, pstm);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
