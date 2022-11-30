/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import DomainModel.ChucVuDomain;
import DomainModel.NhanVien;
import java.util.List;
import Unility.JDBC_HELPER;
import viewModel.NhanVienViewModel;

/**
 *
 * @author Admin
 */
public interface NhanVien_Service {

    public List<NhanVien> getAllNhanVien();

    public int them(NhanVienViewModel sp);
    
    public int sua(NhanVienViewModel sp);
    
    public int xoa(NhanVienViewModel sp);
}
