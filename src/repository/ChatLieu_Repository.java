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
public class ChatLieu_Repository {
    public static List<ThuocTinh_View> getAllGiayView(){
        List<ThuocTinh_View> listGV = new ArrayList<>();
        String sql = "select maChatLieu, tenChatLieu,trangThai from ChatLieu";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        
        try {
            while(rs.next()){
                String maChatLieu = rs.getString("maChatLieu");
                String tenChatLieu = rs.getString("tenChatLieu");
                int tinhTrang = rs.getInt("trangThai");
                ThuocTinh_View GV = new ThuocTinh_View(maChatLieu, tenChatLieu, tinhTrang);
                listGV.add(GV);
            }
            return listGV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ThuocTinh getAllChatLieuById(String id) {
        ThuocTinh tt = null;
        String sql = "select id, maChatLieu, tenChatLieu,trangThai from ChatLieu where maChatLieu = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, id);

        try {
            while (rs.next()) {
                String id1 = rs.getString("id");
                String maChatLieu = rs.getString("maChatLieu");
                String tenChatLieu = rs.getString("tenChatLieu");
                int tinhTrang = rs.getInt("trangThai");
                tt = new ThuocTinh(id1, maChatLieu, tenChatLieu, tinhTrang);
            }
            return tt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIdChatLieu(String ma) {
        String sql = "select id from ChatLieu where maChatLieu = ?";
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
    public static String getIdChatLieuByTen(String ten) {
        String sql = "select id from ChatLieu where tenChatLieu = ?";
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
    
    
    public static int themChatLieu(ThuocTinh tt) {
        String sql = "insert into ChatLieu(maChatLieu, tenChatLieu, trangThai) values (?,?,?)";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai());
    }

    public static int suaChatLieu(ThuocTinh tt, String id) {
        String sql = "UPDATE [dbo].[ChatLieu]\n"
                + "   SET [maChatLieu] = ?\n"
                + "      ,[tenChatLieu] = ?\n"
                + "      ,[trangThai] = ?\n"
                + " WHERE id = ?";
        return JDBC_HELPER.updateTongQuat(sql, tt.getMa(), tt.getTen(), tt.getTrangThai(), id);
    }
    
    public static int xoaChatLieu(ThuocTinh tt, String id) {
        String sql = "delete from ChatLieu where id = ?";
        return JDBC_HELPER.updateTongQuat(sql, id);
    }
}
