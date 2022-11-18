/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import viewModel.DangNhap_ViewModel;

/**
 *
 * @author NHH
 */
public interface DangNhap_Serv {
    boolean checkDN(String email, String mk);
    DangNhap_ViewModel getThongTin(String email, String mk);
    
}
