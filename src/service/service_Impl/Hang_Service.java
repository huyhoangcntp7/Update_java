/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ThuocTinh;
import java.util.List;
import repository.Hang_Repository;
import service.Hang_Service_impl;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public class Hang_Service implements Hang_Service_impl{
    Hang_Repository hangRP = new Hang_Repository();

    @Override
    public List<ThuocTinh_View> getAllHangView() {
        return hangRP.getAllGiayView();
    }

    @Override
    public int themHangGiay(ThuocTinh tt) {
        return hangRP.themHangGiay(tt);
    }

    @Override
    public int suaHangGiay(ThuocTinh tt, String id) {
        return hangRP.suaHangGiay(tt, id);
    }

    @Override
    public int xoaHangGiay(ThuocTinh tt, String id) {
        return hangRP.xoaHangGiay(tt, id);
    }

    @Override
    public String getIdHangGiay(String ma) {
        return hangRP.getIdHangGiay(ma);
    }

    @Override
    public ThuocTinh getAllHangGiayById(String id) {
        return hangRP.getAllHangGiayById(id);
    }

    @Override
    public String getIdHangByTen(String ten) {
        return hangRP.getIdHangByTen(ten);
    }
    
    
}
