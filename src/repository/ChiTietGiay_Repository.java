/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import DomainModel.ChiTietGiay;
import Unility.JDBC_Helper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import viewModel.ChiTietGiay_View;

/**
 *
 * @author ACER
 */
public class ChiTietGiay_Repository {

    public static List<ChiTietGiay_View> getAllChiTietGiayView() {
        List<ChiTietGiay_View> listGV = new ArrayList<>();
        String sql = "select maGiay,tenGiay,maCode, tenMauSac, Size, tenChatLieu, tenHangGiay, namBaoHanh, soLuong, giaNhap ,giaBan, anh\n"
                + "from ChiTietGiay join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "				 join Size on ChiTietGiay.idSize=Size.id\n"
                + "				 join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "				 join HangGiay on ChiTietGiay.idHangGiay=HangGiay.id";
        ResultSet rs = JDBC_Helper.selectTongQuat(sql);
        try {
            while (rs.next()) {
                String maG = rs.getString("maGiay");
                String tenG = rs.getString("tenGiay");
                String tenM = rs.getString("tenMauSac");
                float tenS = rs.getFloat("Size");
                String tenCL = rs.getString("tenChatLieu");
                String tenHG = rs.getString("tenHangGiay");
                int namBaoHanh = rs.getInt("namBaoHanh");
                int soLuong = rs.getInt("soLuong");
                double giaNhap = rs.getDouble("giaNhap");
                double giaBan = rs.getDouble("giaBan");
                String anh = rs.getString("anh");
                String maCode = rs.getString("maCode");
                ChiTietGiay_View ctg = new ChiTietGiay_View(maG, tenCL, tenG, tenS, tenM, tenHG, namBaoHanh, soLuong, giaNhap, giaBan, anh,maCode);
                listGV.add(ctg);
            }
            return listGV;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public static ChiTietGiay_View getAllChiTietGiayViewByMa(String ma) {
        List<ChiTietGiay_View> listGV = new ArrayList<>();
        ChiTietGiay_View ctg = null;
        String sql = "select maGiay,tenGiay,maCode, tenMauSac, Size, tenChatLieu, tenHangGiay, namBaoHanh, soLuong, giaNhap ,giaBan, anh\n"
                + "from ChiTietGiay join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "				 join Size on ChiTietGiay.idSize=Size.id\n"
                + "				 join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "				 join HangGiay on ChiTietGiay.idHangGiay=HangGiay.id where maGiay=?";
        ResultSet rs = JDBC_Helper.selectTongQuat(sql, ma);
        try {
            while (rs.next()) {
                String maG = rs.getString("maGiay");
                String tenG = rs.getString("tenGiay");
                String tenM = rs.getString("tenMauSac");
                float tenS = rs.getFloat("Size");
                String tenCL = rs.getString("tenChatLieu");
                String tenHG = rs.getString("tenHangGiay");
                int namBaoHanh = rs.getInt("namBaoHanh");
                int soLuong = rs.getInt("soLuong");
                double giaNhap = rs.getDouble("giaNhap");
                double giaBan = rs.getDouble("giaBan");
                String anh = rs.getString("anh");
                String maCode = rs.getString("maCode");
                ctg = new ChiTietGiay_View(maG, tenCL, tenG, tenS, tenM, tenHG, namBaoHanh, soLuong, giaNhap, giaBan, anh,maCode);
            }
            return ctg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ChiTietGiay> getAllChiTietGiay() {
        List<ChiTietGiay> listG = new ArrayList<>();
        String sql = "select maGiay,tenGiay ,macode, ChiTietGiay.id idChiTietG, ChatLieu.id idCL, MauSac.id idMS, Size.id idSize, HangGiay.id idH, namBaoHanh, soLuong, giaNhap ,giaBan, anh\n"
                + "from ChiTietGiay join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "				 join Size on ChiTietGiay.idSize=Size.id\n"
                + "				 join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "				 join HangGiay on ChiTietGiay.idHangGiay=HangGiay.id";
        ResultSet rs = JDBC_Helper.selectTongQuat(sql);
        try {
            while (rs.next()) {
                String maG = rs.getString("maGiay");
                String tenG = rs.getString("tenGiay");
                String maCode = rs.getString("maCode");
                String idChiTietG = rs.getString("idChiTietG");
                String idCL = rs.getString("idCL");
                String idMS = rs.getString("idMS");
                String idSize = rs.getString("idSize");
                String idH = rs.getString("idH");
                int namBaoHanh = rs.getInt("namBaoHanh");
                int soLuong = rs.getInt("soLuong");
                double giaNhap = rs.getDouble("giaNhap");
                double giaBan = rs.getDouble("giaBan");
                String anh = rs.getString("anh");
                ChiTietGiay ctg = new ChiTietGiay(maG, tenG,maCode, idChiTietG, idMS, idSize, idCL, idH, namBaoHanh, soLuong, giaNhap, giaBan, anh);
                listG.add(ctg);
            }
            return listG;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIdGiay(String ma) {
        String sql = "select id from ChiTietGiay where maGiay = ?";
        ResultSet rs = JDBC_Helper.selectTongQuat(sql, ma);
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
    
    
    

    public static int themChiTietGiay(ChiTietGiay ct) {
        String sql = "INSERT INTO [dbo].[ChiTietGiay]\n"
                + "           ([maGiay]\n"
                + "           ,[tenGiay]\n"
                + "           ,[maCode]\n"
                + "           ,[idChatLieu]\n"
                + "           ,[idSize]\n"
                + "           ,[idMauSac]\n"
                + "           ,[idHangGiay]\n"
                + "           ,[namBaoHanh]\n"
                + "           ,[soLuong]\n"
                + "           ,[giaNhap]\n"
                + "           ,[giaBan]\n"
                + "           ,[anh])\n"
                + "     VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        return JDBC_Helper.updateTongQuat(sql, ct.getMaGiay(), ct.getTenGiay(),ct.getMaBarCode() ,ct.getIdChatLieu(), ct.getIdSize(),
                ct.getIdMauSac(), ct.getIdHang(), ct.getNamBaoHanh(), ct.getSoLuong(), ct.getGiaNhap(), ct.getGiaBan(),ct.getAnh());
    }

    public static int suaChiTietGiay(ChiTietGiay ct) {
        String sql = "UPDATE [dbo].[ChiTietGiay]\n"
                + "   SET maGiay = ?"
                + "      , tenGiay = ?"
                + "      ,[maCode] = ?\n"
                + "      ,[idChatLieu] = ?\n"
                + "      ,[idSize] = ?\n"
                + "      ,[idMauSac] = ?\n"
                + "      ,[idHangGiay] = ?\n"
                + "      ,[namBaoHanh] = ?\n"
                + "      ,[soLuong] = ?\n"
                + "      ,[giaNhap] = ?\n"
                + "      ,[giaBan] = ?\n"
                + "      ,[anh] = ?\n"
                + " WHERE id = ?";
        return JDBC_Helper.updateTongQuat(sql, ct.getMaGiay(), ct.getTenGiay(),ct.getMaBarCode(), ct.getIdChatLieu(), ct.getIdSize(),
                ct.getIdMauSac(), ct.getIdHang(), ct.getNamBaoHanh(), ct.getSoLuong(), ct.getGiaNhap(), ct.getGiaBan(), ct.getAnh(), ct.getId());
    }

    public static int xoaChiTietGiay(ChiTietGiay ct) {
        String sql = "delete from ChiTietGiay where id = ?";
        return JDBC_Helper.updateTongQuat(sql, ct.getId());
    }
}
