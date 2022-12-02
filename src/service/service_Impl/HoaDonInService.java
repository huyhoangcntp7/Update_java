/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import java.util.List;
import repository.HoaDonInRepo;
import viewModel.HoaDonIn;

/**
 *
 * @author Tuan1
 */
public class HoaDonInService {
    HoaDonInRepo _DonInRepo = new HoaDonInRepo();
    public List<HoaDonIn> getAll(String id){
        return _DonInRepo.getAll(id);
    }
    public static void main(String[] args) {
        HoaDonInService d =new HoaDonInService();
        for (var x : d.getAll("f1df19b7-d0f9-4520-98db-c17e68a33f8f")) {
            System.out.println(x.getTienGiamGia());
        }
    }
  
}
