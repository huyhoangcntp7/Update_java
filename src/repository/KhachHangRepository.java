/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import DomainModel.KhachHang;
import Unility.JDBC_Helper_1;
import java.sql.*;

/**
 *
 * @author DeLL
 */
public class KhachHangRepository {

    public static int addKh(KhachHang kh) {
        String sql = "insert into KhachHang(maKhachHang,hoTenKhachHang,ngaySinh,soDienThoai,diaChi) values(?,?,?,?,?)";
        return JDBC_Helper_1.updateTong(sql, kh.getMakh(), kh.getTenkh(), kh.getNgaysinh(), kh.getSdt(), kh.getDiachi());
    }

    public static int DemKH() {
        String sql = "select COUNT(*) as 'SLKH' from KhachHang";
        ResultSet rs = JDBC_Helper_1.selectTong(sql);
        try {
            while (rs.next()) {
                int slkh = rs.getInt("SLKH");
                return slkh;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static String getIdKHBySDT(String sdt) {
        String sql = "select id from KhachHang where soDienThoai = ?";
        ResultSet rs = JDBC_Helper_1.selectTong(sql, sdt);
        try {
            while (rs.next()) {
                String id = rs.getString("id");
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
