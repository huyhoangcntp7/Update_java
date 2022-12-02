/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import DomainModel.HoaDonCT;
import Unility.JDBC_Helper_1;
import java.sql.*;
import viewModel.HoaDonCTBanHang;

/**
 *
 * @author DeLL
 */
public class HoaDonCTRepository {

    public static int addHDCT(HoaDonCT hdct) {
        String sql = "insert into HoaDonChiTiet(idHoaDon, idChiTietGiay, soLuong, donGia) values(?,?,?,?)";
        return JDBC_Helper_1.updateTong(sql, hdct.getIdhd(), hdct.getIdctg(), hdct.getSl(), hdct.getDongia());
    }

    public static double getSumTT(String mahd) {
        String sql = "  select SUM(soLuong* donGia) as 'tt' from HoaDonChiTiet join HoaDon on HoaDon.id = HoaDonChiTiet.idHoaDon\n"
                + "	where maHoaDon = ?\n"
                + "	group by idHoaDon ";
        ResultSet rs = JDBC_Helper_1.selectTong(sql, mahd);
        try {
            while (rs.next()) {
                double tt = rs.getDouble("tt");
                return tt;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static int updateHDCT(int sl, String idhd, String idctg) {
        String sql = "update HoaDonChiTiet set soLuong = ? where idHoaDon = ? and idChiTietGiay = ?";
        return JDBC_Helper_1.updateTong(sql, sl, idhd, idctg);
    }

    public static int deleteDHCT(String idctg, String idhd) {
        String sql = "delete from HoaDonChiTiet where idChiTietGiay = ? and idHoaDon = ?";
        return JDBC_Helper_1.updateTong(sql, idctg, idhd);
    }

    public static int updateSLHDCT(int sl, String idctg, String idhd) {
        String sql = "update HoaDonChiTiet set soLuong = ? where idHoaDon = ? and idChiTietGiay  = ? ";
        return JDBC_Helper_1.updateTong(sql, sl, idhd, idctg);
    }

    public static int deleteSPHDCT(String idhd, String idsp) {
        String sql = "delete from HoaDonChiTiet where idChiTietGiay = ? and idHoaDon = ?";
        return JDBC_Helper_1.updateTong(sql, idhd, idsp);
    }

    public static int deleteAllGH(String idhd) {
        String sql = " delete from HoaDonChiTiet where idHoaDon= ?";
        return JDBC_Helper_1.updateTong(sql, idhd);
    }
}
