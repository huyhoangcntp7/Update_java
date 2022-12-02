/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Unility.JDBC_Helper_1;
import viewModel.HoaDonIn;

/**
 *
 * @author Tuan1
 */
public class HoaDonInRepo {

    public List getAll(String id) {
        List<HoaDonIn> list = new ArrayList<>();
        String sql = "select chitietgiay.tenGiay,HoaDonChiTiet.soluong,donGia,tienGiamGia\n"
                + "from HoaDon\n"
                + "join HoaDonChiTiet on HoaDonChiTiet.idHoaDon = HoaDon.id\n"
                + "join ChiTietGiay on ChiTietGiay.id = HoaDonChiTiet.idChiTietGiay where HoaDonChiTiet.idHoaDon = ?";
        ResultSet rs = JDBC_Helper_1.selectTong(sql,id);
        try {
            while (rs.next()) {
                list.add(new HoaDonIn(rs.getString(1),
                        rs.getInt(2),
                        rs.getDouble(3),
                        rs.getDouble(4)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        HoaDonInRepo d =new HoaDonInRepo();
        System.out.println(d.getAll("f1df19b7-d0f9-4520-98db-c17e68a33f8f"));
    }
}
