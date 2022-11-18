/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import repository.DangNhap_Resp;
import service.DangNhap_Serv;
import viewModel.DangNhap_ViewModel;

/**
 *
 * @author NHH
 */
public class DangNhap_ServImpl implements DangNhap_Serv {

    @Override
    public boolean checkDN(String email, String mk) {
        return DangNhap_Resp.checkDN(email, mk);
    }

    @Override
    public DangNhap_ViewModel getThongTin(String email, String mk) {
        return DangNhap_Resp.getThongTin(email, mk);
    }

}
