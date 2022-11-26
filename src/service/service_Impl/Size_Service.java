/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ThuocTinh;
import java.util.List;
import repository.Size_Repository;
import service.Size_Service_impl;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public class Size_Service implements Size_Service_impl{
    Size_Repository sizeRP = new Size_Repository();
    
    @Override
    public List<ThuocTinh_View> getAllSizeView() {
        return sizeRP.getAllSizeView();
    }

    @Override
    public int themSize(ThuocTinh tt) {
        return sizeRP.themSize(tt);
    }

    @Override
    public int suaSize(ThuocTinh tt, String id) {
        return sizeRP.suaSize(tt, id);
    }

    @Override
    public int xoaSize(ThuocTinh tt, String id) {
        return sizeRP.xoaGiay(tt,id);
    }

    @Override
    public String getIdSize(String ma) {
        return sizeRP.getIdSize(ma);
    }

    @Override
    public ThuocTinh getAllSizeById(String id) {
        return sizeRP.getAllSizeById(id);
    }

    @Override
    public String getIdSizeByTen(String ten) {
        return sizeRP.getIdSizeByTen(ten);
    }
    
}
