/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ChiTietGiay;
import java.util.ArrayList;
import java.util.List;
import repository.ChiTietGiay_Repository;
import service.ChiTietGiay_Service_impl;
import viewModel.ChiTietGiay_View;

/**
 *
 * @author ACER
 */
public class ChiTietGiay_Service implements ChiTietGiay_Service_impl{
    ChiTietGiay_Repository chiTietGiayRP = new ChiTietGiay_Repository();
    
    @Override
    public List<ChiTietGiay_View> getAllChiTietGiayView() {
        return chiTietGiayRP.getAllChiTietGiayView();
    }

    @Override
    public int themChiTietGiay(ChiTietGiay ct) {
        return chiTietGiayRP.themChiTietGiay(ct);
    }

    @Override
    public int suaChiTietGiay(ChiTietGiay ct) {
        int row = chiTietGiayRP.suaChiTietGiay(ct);
        return row;
    }

    @Override
    public List<ChiTietGiay> getAllChiTietGiay() {
        return chiTietGiayRP.getAllChiTietGiay();
    }

    @Override
    public String getIdGiay(String ma) {
        return chiTietGiayRP.getIdGiay(ma);
    }

    @Override
    public List<ChiTietGiay_View> getAllGiay(String input) {
        if(input==null){
            return chiTietGiayRP.getAllChiTietGiayView();
        }
        List<ChiTietGiay_View> l = new  ArrayList<>();
        for (var x : chiTietGiayRP.getAllChiTietGiayView()) {
            if(x.getMaGiay().toLowerCase().contains(input.toLowerCase())||
                    x.getTenGiay().toLowerCase().contains(input.toLowerCase())||
                    x.getChatLieu().toLowerCase().contains(input.toLowerCase())||
                    x.getTenMauSac().toLowerCase().contains(input.toLowerCase())||
                    String.valueOf(x.getSize()).toLowerCase().contains(input.toLowerCase())|| 
                    String.valueOf(x.getNamBaoHanh()).toLowerCase().contains(input.toLowerCase())|| 
                    String.valueOf(x.getSoLuong()).toLowerCase().contains(input.toLowerCase())|| 
                    String.valueOf(x.getGiaBan()).toLowerCase().contains(input.toLowerCase())|| 
                    x.getMaCode().toLowerCase().contains(input.toLowerCase())||
                    x.getTenHang().toLowerCase().contains(input.toLowerCase())){
                l.add(x);
            }
        }
        return l;
    }

    @Override
    public ChiTietGiay_View getAllChiTietGiayViewByMa(String ma) {
        return chiTietGiayRP.getAllChiTietGiayViewByMa(ma);
    }

    @Override
    public int xoaChiTietGiay(ChiTietGiay ct) {
        return chiTietGiayRP.xoaChiTietGiay(ct);
    }
    
}
