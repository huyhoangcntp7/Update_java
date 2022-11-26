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
public interface Size_Service_impl {
    List<ThuocTinh_View> getAllSizeView();
    
    String getIdSize(String ma);
    
    ThuocTinh getAllSizeById(String id);
    
    String getIdSizeByTen(String ten);
    
    int themSize(ThuocTinh tt);
    
    int suaSize(ThuocTinh tt, String id);
    
    int xoaSize(ThuocTinh tt,String id);
}
