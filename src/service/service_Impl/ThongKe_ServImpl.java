/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import java.util.List;
import repository.ThongKe_Resp;
import service.ThongKe_Serv;
import viewModel.ThongKe_ViewModel;
import viewModel.TT_Thongke_ViewModel;

/**
 *
 * @author NHH
 */
public class ThongKe_ServImpl implements ThongKe_Serv {

    @Override
    public List<ThongKe_ViewModel> getAll() {
        return ThongKe_Resp.getAll();
    }

    @Override
    public List<ThongKe_ViewModel> getMaxSoLuongBan() {
        return ThongKe_Resp.getMaxSoLuongBan();
    }

    @Override
    public List<ThongKe_ViewModel> getMaxDoanhThu() {
        return ThongKe_Resp.getMaxDoanhThu();
    }

    @Override
    public List<ThongKe_ViewModel> getMinSoLuongTon() {
        return ThongKe_Resp.getMinSoLuongTon();
    }

    @Override
    public TT_Thongke_ViewModel getDoanhThuSoLuong() {
        return ThongKe_Resp.getDoanhThuSoLuong();
    }

    @Override
    public TT_Thongke_ViewModel getTienBoRa() {
        return ThongKe_Resp.getTienBoRa();
    }

    @Override
    public TT_Thongke_ViewModel getTienThuVe() {
        return ThongKe_Resp.getTienThuVe();
    }

    @Override
    public TT_Thongke_ViewModel getDoanhThuSoLuong2(String n1, String n2) {
        return ThongKe_Resp.getDoanhThuSoLuong2(n1, n2);
    }

    @Override
    public List<ThongKe_ViewModel> getAllTheoNgayHomNay() {
        return ThongKe_Resp.getAllTheoNgayHomNay();
    }

    @Override
    public List<ThongKe_ViewModel> getAllTheoKhoangNgay(String n1, String n2) {
        return ThongKe_Resp.getAllTHeoKhoangNgay(n1, n2);
    }

}
