/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import DomainModel.ThuocTinh;
import Unility.JDBC_HELPER;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public class Hang_Repository {
    public static List<ThuocTinh_View> getAllGiayView(){
        List<ThuocTinh_View> listGV = new ArrayList<>();
        String sql = "select maHangGiay, tenHangGiay,trangThai from HangGiay";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        
        try {
            while(rs.next()){
                String maHang = rs.getString("maHangGiay");
                String tenHang = rs.getString("tenHangGiay");
                int tinhTrang = rs.getInt("trangThai");
                ThuocTinh_View GV = new ThuocTinh_View(maHang, tenHang, tinhTrang);
                listGV.add(GV);
            }
            return listGV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ThuocTinh getAllHangGiayById(String id) {
        ThuocTinh tt = null;
        String sql = "select id, maHangGiay, tenHangGiay,trangThai from HangGiay where maHangGiay = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, id);

        try {
            while (rs.next()) {
                String id1 = rs.getString("id");
                String maG = rs.getString("maHangGiay");
                String tenG = rs.getString("tenHangGiay");
                int tinhTrang = rs.getInt("trangThai");
                tt = new ThuocTinh(id1, maG, tenG, tinhTrang);
            }
            return tt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getIdHangByTen(String ten) {
        String sql = "select id from HangGiay where tenHangGiay = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, ten);
        String id = null;
        try {
            while (rs.next()) {
                id = rs.getString("id");
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIdHangGiay(String ma) {
        String sql = "select id from HangGiay where maHangGiay = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, ma);
        String id = null;
        try {
            while (rs.next()) {
                id = rs.getString("id");
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static int themHangGiay(ThuocTinh tt) {
        String sql = "insert into HangGiay(maHangGiay, tenHangGiay, trangThai) values (?,?,?)";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai());
    }

    public static int suaHangGiay(ThuocTinh tt, String id) {
        String sql = "UPDATE [dbo].[HangGiay]\n"
                + "   SET [maHangGiay] = ?\n"
                + "      ,[tenHangGiay] = ?\n"
                + "      ,[trangThai] = ?\n"
                + " WHERE id = ?";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai(), id);
    }
    
    public static int xoaHangGiay(ThuocTinh tt,String id) {
        String sql = "delete from HangGiay where id = ?";
        return JDBC_HELPER.updateTongQuat(sql, id);
    }
}
