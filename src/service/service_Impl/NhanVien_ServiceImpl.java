/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ChucVuDomain;
import DomainModel.NhanVien;
import java.util.List;
import repository.NhanVien_Repository;
import service.NhanVien_Service;
import Unility.JDBC_HELPER;
import viewModel.NhanVienViewModel;

/**
 *
 * @author Admin
 */
public class NhanVien_ServiceImpl implements NhanVien_Service {

    @Override
    public List<NhanVien> getAllNhanVien() {
        return NhanVien_Repository.getAllNhanVien();
    }

    @Override
    public int them(NhanVienViewModel sp) {
        return NhanVien_Repository.them(sp);
    }

    @Override
    public int sua(NhanVienViewModel sp) {
        return NhanVien_Repository.sua(sp);
    }

    @Override
    public int xoa(NhanVienViewModel sp) {
        return NhanVien_Repository.xoa(sp);
    }

}
