package Unility;

import Unility.DBContext;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC_HELPER {

    public static ResultSet selectTongQuat(String sql, Object... prams) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBContext.getConnection();
            stm = con.prepareStatement(sql);
            for (int i = 0; i < prams.length; i++) {
                stm.setObject(i + 1, prams[i]);
            }
            rs = stm.executeQuery();
            return rs;

        } catch (Exception ex) {
            
            ex.printStackTrace();
            return null;
        }
    }

    public static int updateTongQuat(String sql, Object... params) {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBContext.getConnection();
            stm = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i+1, params[i]);
            }
            return stm.executeUpdate();

        } catch (Exception ex) {
            
            ex.printStackTrace();
            return -1;
        }
    }

}
