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
public class MauSac_Repository {
    public static List<ThuocTinh_View> getAllGiayView(){
        List<ThuocTinh_View> listGV = new ArrayList<>();
        String sql = "select maMauSac, tenMauSac,trangThai from MauSac";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        
        try {
            while(rs.next()){
                String maM = rs.getString("maMauSac");
                String tenM = rs.getString("tenMauSac");
                int tinhTrang = rs.getInt("trangThai");
                ThuocTinh_View GV = new ThuocTinh_View(maM, tenM, tinhTrang);
                listGV.add(GV);
            }
            return listGV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ThuocTinh getAllMauSacById(String id) {
        ThuocTinh tt = null;
        String sql = "select id, maMauSac, tenMauSac,trangThai from MauSac where maMauSac = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, id);

        try {
            while (rs.next()) {
                String id1 = rs.getString("id");
                String maMauSac = rs.getString("maMauSac");
                String tenMauSac = rs.getString("tenMauSac");
                int tinhTrang = rs.getInt("trangThai");
                tt = new ThuocTinh(id1, maMauSac, tenMauSac, tinhTrang);
            }
            return tt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIdMauSac(String ma) {
        String sql = "select id from MauSac where maMauSac = ?";
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
    
    public static String getIdMauSacByTen(String ten) {
        String sql = "select id from MauSac where tenMauSac = ?";
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
    
    public static int themMauSac(ThuocTinh tt) {
        String sql = "insert into MauSac(maMauSac, tenMauSac, trangThai) values (?,?,?)";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai());
    }

    public static int suaMauSac(ThuocTinh tt, String id) {
        String sql = "UPDATE [dbo].[MauSac]\n"
                + "   SET [maMauSac] = ?\n"
                + "      ,[tenMauSac] = ?\n"
                + "      ,[trangThai] = ?\n"
                + " WHERE id = ?";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai(), id);
    }
    
    public static int xoaMauSac(ThuocTinh tt, String id) {
        String sql = "delete from MauSac where id = ?";
        return JDBC_HELPER.updateTongQuat(sql, id);
    }
}
