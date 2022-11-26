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
public interface MauSac_Service_impl {
    List<ThuocTinh_View> getAllMauView();
    
    String getIdMauSac(String ma);
    
    ThuocTinh getAllMauSacById(String id);
    
    String getIdMauSacByTen(String ten);
    
    int themMauSac(ThuocTinh tt);

    int suaMauSac(ThuocTinh tt, String id);
    
    int xoaMauSac(ThuocTinh tt, String id);
}
