/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import viewModel.SanPhamBanHang;
import java.sql.*;
import Unility.JDBC_Helper_1;

/**
 *
 * @author DeLL
 */
public class SanPhanBanHangRepository {

    public static List<SanPhamBanHang> getAllSpBh() {
        List<SanPhamBanHang> listSP = new ArrayList<>();
        String sql = "select maGiay, tenGiay, tenHangGiay,  Size,  tenMauSac, giaBan, "
                + "soLuong from ChiTietGiay join HangGiay on ChiTietGiay.idHangGiay = HangGiay.id\n"
                + "join Size on ChiTietGiay.idSize = Size.id\n"
                + "join MauSac on MauSac.id = ChiTietGiay.idMauSac";
        ResultSet rs = JDBC_Helper_1.selectTong(sql);
        try {
            while (rs.next()) {
                String ma = rs.getString("maGiay");
                String ten = rs.getString("tenGiay");
                String hang = rs.getString("tenHangGiay");
                int size = rs.getInt("Size");
                String tenMau = rs.getString("tenMauSac");
                double gia = rs.getDouble("giaBan");
                int sl = rs.getInt("soLuong");
                SanPhamBanHang sp = new SanPhamBanHang(ma, ten, hang, tenMau, size, sl, gia);
                listSP.add(sp);
            }
            return listSP;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIDSpByMaSP(String maSP) {
        String sql = "select id from ChiTietGiay where maGiay = ?";
        ResultSet rs = JDBC_Helper_1.selectTong(sql, maSP);
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

    public static int getSLGiay(String maSP) {
        String sql = " select soLuong from ChiTietGiay where maGiay = ?";
        ResultSet rs = JDBC_Helper_1.selectTong(sql, maSP);
        try {
            while (rs.next()) {
                int soLuong = rs.getInt("soLuong");
                return soLuong;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static int updateSLSP(int sl, String maGiay) {
        String sql = "update ChiTietGiay set soLuong = ? where maGiay = ?";
        return JDBC_Helper_1.updateTong(sql, sl, maGiay);
    }
}
