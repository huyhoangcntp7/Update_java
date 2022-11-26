/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ThuocTinh;
import java.util.List;
import repository.MauSac_Repository;
import service.MauSac_Service_impl;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public class MauSac_Service implements MauSac_Service_impl{
    MauSac_Repository mauSacRP = new MauSac_Repository();
    
    @Override
    public List<ThuocTinh_View> getAllMauView() {
        return mauSacRP.getAllGiayView();
    }

    @Override
    public int themMauSac(ThuocTinh tt) {
        return mauSacRP.themMauSac(tt);
    }

    @Override
    public int suaMauSac(ThuocTinh tt, String id) {
        return mauSacRP.suaMauSac(tt, id);
    }

    @Override
    public int xoaMauSac(ThuocTinh tt, String id) {
        return mauSacRP.xoaMauSac(tt, id);
    }

    @Override
    public String getIdMauSac(String ma) {
        return mauSacRP.getIdMauSac(ma);
    }

    @Override
    public ThuocTinh getAllMauSacById(String id) {
        return mauSacRP.getAllMauSacById(id);
    }

    @Override
    public String getIdMauSacByTen(String ten) {
        return mauSacRP.getIdMauSacByTen(ten);
    }
    
}
