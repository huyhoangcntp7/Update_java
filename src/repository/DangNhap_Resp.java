/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Unility.JDBC_HELPER;
import viewModel.DangNhap_ViewModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NHH
 */
public class DangNhap_Resp {

    public static DangNhap_ViewModel getThongTin(String email, String mk) {
        DangNhap_ViewModel dn = null;
        String sql = "select hoTenNhanVien,tenChucVu,email,matkhau from NhanVien inner join ChucVu on ChucVu.id=NhanVien.idChucVu where email=? and matkhau=?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, email, mk);
        try {
            while (rs.next()) {
                dn = new DangNhap_ViewModel();
                dn.setTenNV(rs.getString(1));
                dn.setTenCV(rs.getString(2));
                dn.setEmail(rs.getString(3));
                dn.setMk(rs.getString(4));

            }
            return dn;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static boolean checkDN(String email, String mk) {
        System.out.println(getThongTin(email, mk));
        if (getThongTin(email, mk) == null) {
            return false;
        }
        return true;
    }

}
