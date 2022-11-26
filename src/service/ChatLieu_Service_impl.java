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
public interface ChatLieu_Service_impl {
    List<ThuocTinh_View> getAllChatLieuView();
    
    String getIdChatLieu(String ma);
    
    ThuocTinh getAllChatLieuById(String id);
    
    String getIdChatLieuByTen(String ten);
    
    int themChatLieu(ThuocTinh tt);
    
    int suaChatLieu(ThuocTinh tt, String id);
    
    int xoaChatLieu(ThuocTinh tt, String id);
}
