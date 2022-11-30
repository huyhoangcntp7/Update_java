/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import DomainModel.ChucVuDomain;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Unility.JDBC_HELPER;

/**
 *
 * @author Admin
 */
public class ChucVu_Repository {
    public static ChucVuDomain layIdChucVu(String ten1){
        String sql = "select id from ChucVu where tenChucVu = ?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, ten1);
        ChucVuDomain sp = null;
        try {
            while(rs.next()){
                String id = rs.getString("id");
                sp = new ChucVuDomain(id, null, null, 0);
            }
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<ChucVuDomain> getAllChucVu(){
        List<ChucVuDomain> listNV = new ArrayList<>();
        ResultSet rs;
        String sql = "select id, maChucVu, tenChucVu, trangThai from ChucVu";
        rs = JDBC_HELPER.selectTongQuat(sql);
        
        try {
            while(rs.next()){
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String ten = rs.getString(3);
                int tt = rs.getInt(4);
                ChucVuDomain nv = new ChucVuDomain(id, ma, ten, tt);
                listNV.add(nv);
            }
            return listNV;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static int ThemChucVu(ChucVuDomain ms){
        String sql = "insert into ChucVu(maChucVu, tenChucVu, trangThai) values (?,?,?) ";
        return JDBC_HELPER.updateTongQuat(sql, ms.getMa(), ms.getTen(), ms.getTrangThai());
    }
}
