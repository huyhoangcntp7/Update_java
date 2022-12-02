/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Unility;

import Unility.DBContext_1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author DeLL
 */
public class JDBC_Helper_1 {
    public static ResultSet selectTong(String sql, Object...params){
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBContext.getConnection();
            pstm = con.prepareStatement(sql);
            for(int i=0; i<params.length;i++){
                pstm.setObject(i+1, params[i]);
            }
            rs = pstm.executeQuery();
            return  rs;
        } catch (Exception e) {
            e.printStackTrace();
            close(pstm, con, rs);
            return null;
        }
    }
    public static void close(PreparedStatement pstm, Connection con){
        try {
            pstm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static void close(PreparedStatement pstm, Connection con, ResultSet rs){
        try {
            close(pstm, con);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
          public static int updateTong(String sql, Object...params){
        PreparedStatement pstm = null;
        Connection con = null;
        try {
            con = DBContext.getConnection();
            pstm = con.prepareStatement(sql);
            for(int i=0; i<params.length;i++){
                pstm.setObject(i+1, params[i]);
            }
            return pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally{
            close(pstm, con);
        }
    }  
}
