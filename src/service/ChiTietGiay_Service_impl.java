/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DomainModel.ChiTietGiay;
import java.util.List;
import viewModel.ChiTietGiay_View;

/**
 *
 * @author ACER
 */
public interface ChiTietGiay_Service_impl {
    List<ChiTietGiay_View> getAllChiTietGiayView();
    
    List<ChiTietGiay> getAllChiTietGiay();
    
    String getIdGiay(String ma);
    
    List<ChiTietGiay_View> getAllGiay(String input);
    
    ChiTietGiay_View getAllChiTietGiayViewByMa(String ma);
            
    int themChiTietGiay(ChiTietGiay ct);
    
    int suaChiTietGiay(ChiTietGiay ct);
    
    int xoaChiTietGiay(ChiTietGiay ct);
}
