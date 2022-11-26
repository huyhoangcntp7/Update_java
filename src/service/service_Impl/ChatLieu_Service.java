/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ThuocTinh;
import java.util.List;
import repository.ChatLieu_Repository;
import service.ChatLieu_Service_impl;
import viewModel.ThuocTinh_View;

/**
 *
 * @author ACER
 */
public class ChatLieu_Service implements ChatLieu_Service_impl{
    ChatLieu_Repository chatLieuRP = new ChatLieu_Repository();

    @Override
    public List<ThuocTinh_View> getAllChatLieuView() {
        return chatLieuRP.getAllGiayView();
    }

    @Override
    public int themChatLieu(ThuocTinh tt) {
        return chatLieuRP.themChatLieu(tt);
    }

    @Override
    public int suaChatLieu(ThuocTinh tt, String id) {
        return chatLieuRP.suaChatLieu(tt, id);
    }

    @Override
    public int xoaChatLieu(ThuocTinh tt, String id) {
        return chatLieuRP.xoaChatLieu(tt, id);
    }

    @Override
    public String getIdChatLieu(String ma) {
        return chatLieuRP.getIdChatLieu(ma);
    }

    @Override
    public ThuocTinh getAllChatLieuById(String id) {
        return chatLieuRP.getAllChatLieuById(id);
    }

    @Override
    public String getIdChatLieuByTen(String ten) {
        return chatLieuRP.getIdChatLieuByTen(ten);
    }
     
}
