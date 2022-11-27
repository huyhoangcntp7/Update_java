package repository;

import Unility.JDBC_HELPER;
import java.util.ArrayList;
import java.util.List;
import viewModel.ThongKe_ViewModel;
import java.sql.*;

import viewModel.TT_Thongke_ViewModel;

public class ThongKe_Resp {

    public static List<ThongKe_ViewModel> getAllTheoNgayHomNay() {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select Giay.maGiay,tenGiay,ChiTietGiay.soLuong,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu', ngayTao from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "               					  inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "                					  inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "               						  inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "                						  inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "										  inner join HoaDon on HoaDonChiTiet.idHoaDon=HoaDon.id\n"
                + "where ngayTao=CONCAT(Year(GETDATE()),'-',Month(GETDATE()),'-',DAY(GETDATE()))"
                + "                Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,ngayTao";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setSlTon(rs.getInt(3));
                tk.setSlBan(rs.getInt(4));
                tk.setDoanhThu(rs.getDouble(5));
                tk.setNgaytao(rs.getString(6));
                list.add(tk);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getAll() {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "	 select Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu',ngayTao from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "               			  inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "               					  inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "                						  inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "                						  inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "										  inner join HoaDon on HoaDonChiTiet.idHoaDon=HoaDon.id\n"
                + "                Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,ngayTao";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setMau(rs.getString(3));
                tk.setSize(rs.getString(4));
                tk.setChatLieu(rs.getString(5));
                tk.setSlTon(rs.getInt(6));
                tk.setDonGia(rs.getDouble(7));
                tk.setSlBan(rs.getInt(8));
                tk.setDoanhThu(rs.getDouble(9));
                tk.setNgaytao(rs.getString(10));
                list.add(tk);

            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getMaxSoLuongBan() {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select top 5 Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu' from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "						  inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "						  inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "						  inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "						  inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia\n"
                + "Order by [So luong ban] desc";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setMau(rs.getString(3));
                tk.setSize(rs.getString(4));
                tk.setChatLieu(rs.getString(5));
                tk.setSlTon(rs.getInt(6));
                tk.setDonGia(rs.getDouble(7));
                tk.setSlBan(rs.getInt(8));
                tk.setDoanhThu(rs.getDouble(9));
                list.add(tk);

            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getMaxDoanhThu() {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select top 5 Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu' from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "						  inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "						  inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "						  inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "						  inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia\n"
                + "Order by [Doanh thu] desc";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setMau(rs.getString(3));
                tk.setSize(rs.getString(4));
                tk.setChatLieu(rs.getString(5));
                tk.setSlTon(rs.getInt(6));
                tk.setDonGia(rs.getDouble(7));
                tk.setSlBan(rs.getInt(8));
                tk.setDoanhThu(rs.getDouble(9));
                list.add(tk);

            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getMinSoLuongTon(int soLuong) {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select  Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong as 'Sl Ton',donGia,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu' from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "						  inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "						  inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "						  inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "						  inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "						  where ChiTietGiay.soLuong<=?\n"
                + "Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia order by [Sl Ton]\n";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, soLuong);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setMau(rs.getString(3));
                tk.setSize(rs.getString(4));
                tk.setChatLieu(rs.getString(5));
                tk.setSlTon(rs.getInt(6));
                tk.setDonGia(rs.getDouble(7));
                tk.setSlBan(rs.getInt(8));
                tk.setDoanhThu(rs.getDouble(9));
                list.add(tk);

            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static TT_Thongke_ViewModel getDoanhThuSoLuong() {

        TT_Thongke_ViewModel tk = new TT_Thongke_ViewModel();
        String sql = "select COUNT(idHoadon), sum(soLuong*donGia) from HoaDonChiTiet inner join HoaDon on "
                + "HoaDon.id=HoaDonChiTiet.idHoaDon where ngayTao=CONCAT(Year(GETDATE()),'-',Month(GETDATE()),'-',DAY(GETDATE()))";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {

                tk.setSoDonHang(rs.getInt(1));
                tk.setDoanhthu(rs.getDouble(2));

            }
            return tk;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static TT_Thongke_ViewModel getDoanhThuSoLuong2(String n1, String n2) {

        TT_Thongke_ViewModel tk = new TT_Thongke_ViewModel();
        String sql = "select COUNT(idHoadon), sum(soLuong*donGia) from HoaDonChiTiet inner join HoaDon on "
                + "HoaDon.id=HoaDonChiTiet.idHoaDon  where ngayTao>=? and ngayTao <=?";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, n1, n2);

        try {
            while (rs.next()) {
                tk.setSoDonHang(rs.getInt(1));

                tk.setDoanhthu(rs.getDouble(2));

            }
            return tk;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getAllTHeoKhoangNgay(String n1, String n2) {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select Giay.maGiay,tenGiay,ChiTietGiay.soLuong,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu', ngayTao,count(HoaDon.id) as'Số đơn hàng' from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "               					inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "                				inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "               					inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "                				inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "								inner join HoaDon on HoaDonChiTiet.idHoaDon=HoaDon.id\n"
                + "			    where ngayTao>=? and ngayTao <=?\n"
                + "                Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,ngayTao";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, n1, n2);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setSlTon(rs.getInt(3));
                tk.setSlBan(rs.getInt(4));
                tk.setDoanhThu(rs.getDouble(5));
                tk.setNgaytao(rs.getString(6));

                list.add(tk);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<ThongKe_ViewModel> getAllTHeoNam(String nam) {
        List<ThongKe_ViewModel> list = new ArrayList<>();
        String sql = "select Giay.maGiay,tenGiay,ChiTietGiay.soLuong,sum(HoaDonChiTiet.soLuong) as'So luong ban',sum(donGia*HoaDonChiTiet.soLuong) as 'Doanh thu', ngayTao,count(HoaDon.id) as'Số đơn hàng' from ChiTietGiay inner join ChatLieu on ChiTietGiay.idChatLieu=ChatLieu.id\n"
                + "                         					inner join Size on ChiTietGiay.idSize=Size.id\n"
                + "                           				inner join Giay on ChiTietGiay.idGiay=Giay.id\n"
                + "                           					inner join MauSac on ChiTietGiay.idMauSac=MauSac.id\n"
                + "                              				inner join HoaDonChiTiet on ChiTietGiay.id=HoaDonChiTiet.idChiTietGiay\n"
                + "               						inner join HoaDon on HoaDonChiTiet.idHoaDon=HoaDon.id\n"
                + "              		    where YEAR(ngayTao)=?\n"
                + "                             Group by Giay.maGiay,tenGiay,tenMauSac,Size,tenChatLieu,ChiTietGiay.soLuong,donGia,ngayTao";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql, nam);
        try {
            while (rs.next()) {
                ThongKe_ViewModel tk = new ThongKe_ViewModel();
                tk.setMaSp(rs.getString(1));
                tk.setTenSp(rs.getString(2));
                tk.setSlTon(rs.getInt(3));
                tk.setSlBan(rs.getInt(4));
                tk.setDoanhThu(rs.getDouble(5));
                tk.setNgaytao(rs.getString(6));

                list.add(tk);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static TT_Thongke_ViewModel getTienBoRa() {

        TT_Thongke_ViewModel tk = new TT_Thongke_ViewModel();
        String sql = "select SUM(soLuong*gianhap) from ChiTietGiay";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {

                tk.setTienDauTu(rs.getInt(1));

            }
            return tk;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static TT_Thongke_ViewModel getTienThuVe() {

        TT_Thongke_ViewModel tk = new TT_Thongke_ViewModel();
        String sql = "select sum(soLuong*donGia) from HoaDonChiTiet";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {

                tk.setTienThuve(rs.getInt(1));

            }
            return tk;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int getSoGiayKD() {

        int sum = 0;
        String sql = "	select count(id) as N'Số giày hd' from ChiTietGiay\n";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            return sum;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static int getSoGiaySapHet() {

        int sum = 0;
        String sql = "select count(id) as N'Số giày hd' from ChiTietGiay where soLuong>0 and soLuong<=10";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            return sum;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static int getSoGiayHet() {

        int sum = 0;
        String sql = "select count(id) as N'Số giày hd' from ChiTietGiay where soLuong=0";
        ResultSet rs = JDBC_HELPER.selectTongQuat(sql);
        try {
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            return sum;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

}
