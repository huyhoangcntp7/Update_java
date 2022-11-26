/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import DomainModel.ThuocTinh;
import java.util.List;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public interface Hang_Service_impl {
    List<ThuocTinh_View> getAllHangView();
    
    String getIdHangGiay(String ma);
    
    ThuocTinh getAllHangGiayById(String id);
    
    String getIdHangByTen(String ten);
    
    int themHangGiay(ThuocTinh tt);
    
    int suaHangGiay(ThuocTinh tt, String id);
    
    int xoaHangGiay(ThuocTinh tt, String id);
}
