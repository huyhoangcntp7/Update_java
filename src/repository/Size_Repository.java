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
public class Size_Repository {
    public static List<ThuocTinh_View> getAllSizeView(){
        List<ThuocTinh_View> listGV = new ArrayList<>();
        String sql = "select maSize, Size,trangThai from Size";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        
        try {
            while(rs.next()){
                String maSize = rs.getString("maSize");
                String Size = rs.getString("Size");
                int tinhTrang = rs.getInt("trangThai");
                ThuocTinh_View GV = new ThuocTinh_View(maSize, Size, tinhTrang);
                listGV.add(GV);
            }
            return listGV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ThuocTinh getAllSizeById(String id) {
        ThuocTinh tt = null;
        String sql = "select id, maSize, Size,trangThai from Size where maSize = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, id);

        try {
            while (rs.next()) {
                String id1 = rs.getString("id");
                String maG = rs.getString("maSize");
                String tenG = rs.getString("Size");
                int tinhTrang = rs.getInt("trangThai");
                tt = new ThuocTinh(id1, maG, tenG, tinhTrang);
            }
            return tt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIdSize(String ma) {
        String sql = "select id from Size where maSize = ?";
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
    
    public static String getIdSizeByTen(String ten) {
        String sql = "select id from Size where Size = ?";
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
    
    public static int themSize(ThuocTinh tt) {
        String sql = "insert into Size(maSize, Size, trangThai) values (?,?,?)";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai());
    }

    public static int suaSize(ThuocTinh tt, String id) {
        String sql = "UPDATE [dbo].[Size]\n"
                + "   SET [maSize] = ?\n"
                + "      ,[Size] = ?\n"
                + "      ,[trangThai] = ?\n"
                + " WHERE id = ?";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai(), id);
    }
    
    public static int xoaGiay(ThuocTinh tt,String id) {
        String sql = "delete from Size where id = ?";
        return JDBC_HELPER.updateTongQuat(sql, id);
    }
}
