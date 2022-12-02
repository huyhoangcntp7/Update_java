/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import java.util.ArrayList;
import java.util.List;
import repository.SanPhanBanHangRepository;
import viewModel.SanPhamBanHang;

/**
 *
 * @author DeLL
 */
public class chuCuaHangService {

    private String a;
    SanPhanBanHangRepository _BanHangRepository = new SanPhanBanHangRepository();

    public List<SanPhamBanHang> getAll(String input) {
        if (input.isEmpty()) {
            return _BanHangRepository.getAllSpBh();
        }
        List listTimKiem = new ArrayList();
        for (var x : _BanHangRepository.getAllSpBh()) {
            if (x.getMa().toLowerCase().contains(input.toLowerCase())
                    ||x.getTen().toLowerCase().contains(input.toLowerCase())
                    ||x.getHang().toLowerCase().contains(input.toLowerCase())
//                    ||x.getSl()== Integer.parseInt(input)
//                    ||x.getSize()== Integer.parseInt(input)
//                    ||x.getDongia()== Integer.parseInt(input)
                    ||x.getMau().toLowerCase().contains(input.toLowerCase())) {
                listTimKiem.add(x);
            }
        }
        return listTimKiem;
    }

}
