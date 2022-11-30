package view;

import DomainModel.ChiTietGiay;
import DomainModel.ChucVuDomain;
import DomainModel.NhanVien;
import DomainModel.ThuocTinh;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.ThongKe_Serv;
import service.service_Impl.ChatLieu_Service;
import service.service_Impl.ChiTietGiay_Service;
import service.service_Impl.Hang_Service;
import service.service_Impl.MauSac_Service;
import service.service_Impl.Size_Service;
import service.service_Impl.ThongKe_ServImpl;
import viewModel.ChiTietGiay_View;
import viewModel.ThongKe_ViewModel;
import viewModel.ThuocTinh_View;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import service.ChucVu_Service;
import service.NhanVien_Service;
import service.service_Impl.ChucVu_ServiceImpl;
import service.service_Impl.NhanVien_ServiceImpl;
import viewModel.NhanVienViewModel;

public class Menu extends javax.swing.JFrame implements Runnable, ThreadFactory {
    
    DefaultTableModel dtm = null;
    ThongKe_Serv thongKe_Serv = new ThongKe_ServImpl();
    List<ThongKe_ViewModel> listTKDoanhThu = new ArrayList<>();
    List<ThongKe_ViewModel> listTKSanPham = new ArrayList<>();
    String pattern = "#.###";
    DecimalFormat dcf = (DecimalFormat) NumberFormat.getNumberInstance();
    List<NhanVien> list;
    NhanVien_Service chService = new NhanVien_ServiceImpl();
    ChucVu_Service cvService = new ChucVu_ServiceImpl();
    List<ChucVuDomain> listCV;
    
    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private Executor exe = Executors.newSingleThreadExecutor(this);
    
    String anhStr = null;
    List<ChiTietGiay_View> listCTG;
    List<ChiTietGiay> listChiTiet;
    List<ThuocTinh_View> listTT;
    DefaultTableModel model;
    
    MauSac_Service mauSV = new MauSac_Service();
    Size_Service sizeSV = new Size_Service();
    ChatLieu_Service chatLieuSV = new ChatLieu_Service();
    Hang_Service hangSV = new Hang_Service();
    ChiTietGiay_Service chiTietGiaYSV = new ChiTietGiay_Service();
    
    public Menu() {
        initComponents();
        dcf.applyPattern(pattern);
        this.setLocationRelativeTo(null);
        pnlTimkiemTong.setVisible(false);
        NgayBatDau.setDate(new Date());
        NgayKetThuc.setDate(new Date());
        setTable();
        listTKSanPham = thongKe_Serv.getAll();
        loadTableSanPham(listTKSanPham);
        pnlSoLuong.setVisible(false);
        listTKDoanhThu = thongKe_Serv.getAllTheoNgayHomNay();
        loadTableThongKe(listTKDoanhThu);
        setPanelThongKe(thongKe_Serv.getDoanhThuSoLuong().getSoDonHang(), thongKe_Serv.getDoanhThuSoLuong().getDoanhthu());
        setPanelSanPham();
        
        rdoLam.setSelected(true);       
        cboChucVu.removeAllItems();
        listCV = cvService.getAllChucVu();
        for (int i = 0; i < listCV.size(); i++) {
            cboChucVu.addItem(String.valueOf(listCV.get(i).getTen()));
        }
        
        fillCbbSize();
        fillCbbChatLieu();
        fillCbbHangGiay();
        fillCbbMauSac();
        
    }
    //nhanvien-----------------------------------------------------------------------
    public void fillTableNV() {
        list = chService.getAllNhanVien();
        dtm = (DefaultTableModel) tblNhanvien.getModel();
        dtm.setRowCount(0);
        String t;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTrangThai() == 1) {
                t = "Đang làm";
            } else {
                t = "Đang nghỉ";
            }

            Object[] data = new Object[]{
                list.get(i).getMa(),
                list.get(i).getHoTen(),
                list.get(i).getChucVu().getTen(),
                t
            };
            dtm.addRow(data);
        }
    }

    //sanpham------------------------------------------------------------------------
    private void fillTableChiTietGiay() {
        listCTG = chiTietGiaYSV.getAllChiTietGiayView();
        model = (DefaultTableModel) tblGiay.getModel();
        model.setRowCount(0);
        for (ChiTietGiay_View ct : listCTG) {
            Object[] data = {
                ct.getTenGiay(),
                ct.getTenMauSac(),
                ct.getSize(),
                ct.getChatLieu(),
                ct.getTenHang(),
                ct.getNamBaoHanh(),
                ct.getSoLuong(),
                ct.getGiaBan(),
                ct.getAnh()
            };
            model.addRow(data);
        }
    }
    
    public void setPanelSanPham() {
        
        lblSoGiayKd.setText(String.valueOf(thongKe_Serv.getSoGiayKD()));
        lblSoGiaySapHet.setText(String.valueOf(thongKe_Serv.getSoGiaySapHet()));
        lblSoGiayHet.setText(String.valueOf(thongKe_Serv.getSoGiayHet()));
        
    }
    
    public ImageIcon resizeImage(String url) {
        ImageIcon imageIcon = new ImageIcon(url);
        Image img = imageIcon.getImage();
        Image newimg = img.getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon imageIcon1 = new ImageIcon(newimg);
        return imageIcon1;
        
    }

//    private void fillCbbGiay() {
//        cbbGiay.removeAllItems();
//        listTT = giaySV.getAllGiayView();
//        for (ThuocTinh_View tt : listTT) {
//            cbbGiay.addItem(tt.getTen());
//        }
//    }
    private void fillCbbMauSac() {
        cbbMauSac.removeAllItems();
        listTT = mauSV.getAllMauView();
        for (ThuocTinh_View tt : listTT) {
            cbbMauSac.addItem(tt.getTen());
        }
    }
    
    private void fillCbbSize() {
        cbbSize.removeAllItems();
        listTT = sizeSV.getAllSizeView();
        for (ThuocTinh_View tt : listTT) {
            cbbSize.addItem(tt.getTen());
        }
    }
    
    private void fillCbbChatLieu() {
        cbbChatLieu.removeAllItems();
        listTT = chatLieuSV.getAllChatLieuView();
        for (ThuocTinh_View tt : listTT) {
            cbbChatLieu.addItem(tt.getTen());
        }
    }
    
    private void fillCbbHangGiay() {
        cbbHang.removeAllItems();
        listTT = hangSV.getAllHangView();
        for (ThuocTinh_View tt : listTT) {
            cbbHang.addItem(tt.getTen());
        }
    }
    
    private void fillTableThuocTinhMau() {
        listTT = mauSV.getAllMauView();
        model = (DefaultTableModel) tblThuocTinh.getModel();
        model.setRowCount(0);
        for (ThuocTinh_View gv : listTT) {
            Object[] data = {
                gv.getMa(),
                gv.getTen(),
                gv.getTrangThai()
            };
            model.addRow(data);
        }
    }
    
    private void fillTableThuocTinhSize() {
        listTT = sizeSV.getAllSizeView();
        model = (DefaultTableModel) tblThuocTinh.getModel();
        model.setRowCount(0);
        for (ThuocTinh_View gv : listTT) {
            Object[] data = {
                gv.getMa(),
                gv.getTen(),
                gv.getTrangThai()
            };
            model.addRow(data);
        }
    }
    
    private void fillTableThuocTinhChatLieu() {
        listTT = chatLieuSV.getAllChatLieuView();
        model = (DefaultTableModel) tblThuocTinh.getModel();
        model.setRowCount(0);
        for (ThuocTinh_View gv : listTT) {
            Object[] data = {
                gv.getMa(),
                gv.getTen(),
                gv.getTrangThai()
            };
            model.addRow(data);
        }
    }
    
    private void fillTableThuocTinhHang() {
        listTT = hangSV.getAllHangView();
        model = (DefaultTableModel) tblThuocTinh.getModel();
        model.setRowCount(0);
        for (ThuocTinh_View gv : listTT) {
            Object[] data = {
                gv.getMa(),
                gv.getTen(),
                gv.getTrangThai()
            };
            model.addRow(data);
        }
    }
    //SanPham---------------------------------------------------------------------------------

    public void loadTableThongKe(List<ThongKe_ViewModel> list) {
        dtm = (DefaultTableModel) tblThongKe.getModel();
        dtm.setRowCount(0);
        for (ThongKe_ViewModel t : list) {
            Object rowData[] = {
                t.getMaSp(),
                t.getTenSp(),
                t.getSlTon(),
                t.getSlBan(),
                t.getDoanhThu(),
                t.getNgaytao()
            };
            dtm.addRow(rowData);
        }
        
    }
    
    public void loadTableSanPham(List<ThongKe_ViewModel> list) {
        dtm = (DefaultTableModel) tblThongKe1.getModel();
        dtm.setRowCount(0);
        for (ThongKe_ViewModel t : list) {
            Object rowData[] = {
                t.getMaSp(),
                t.getTenSp(),
                t.getMau(),
                t.getSize(),
                t.getChatLieu(),
                t.getSlTon(),
                t.getSlBan(),
                t.getDonGia(),
                t.getDoanhThu(),};
            dtm.addRow(rowData);
        }
        
    }
    
    public void setPanelThongKe(int soDOnHang, double doanhThu) {
        
        lblTongDonHang.setText(String.valueOf(soDOnHang));
        lblDoanhthu.setText(dcf.format(doanhThu));
        lblTongTienDautu.setText(dcf.format(thongKe_Serv.getTienBoRa().getTienDauTu()));
        lblTongTienThuVe.setText(dcf.format(thongKe_Serv.getTienThuVe().getTienThuve()));
        lblTienLai.setText(dcf.format(thongKe_Serv.getTienThuVe().getTienThuve() - thongKe_Serv.getTienBoRa().getTienDauTu()));
        
    }
    
    private void setTable() {
        tblGiay.getTableHeader().setBackground(new Color(122, 181, 236));
        tblGiay.getTableHeader().setOpaque(false);
        tblGioHang.getTableHeader().setBackground(new Color(122, 181, 236));
        tblSanPham2.getTableHeader().setBackground(new Color(122, 181, 236));
        tblHoaDonHoanThanh.getTableHeader().setBackground(new Color(122, 181, 236));
        tblThongKe.getTableHeader().setBackground(new Color(122, 181, 236));
//        tblNhanvien.getTableHeader().setBackground(new Color(122, 181, 236));
        tblGiamgia.getTableHeader().setBackground(new Color(122, 181, 236));
        tblHoaDonCho.getTableHeader().setBackground(new Color(122, 181, 236));
        tblHoaDonGiao.getTableHeader().setBackground(new Color(122, 181, 236));
        tblThuocTinh.getTableHeader().setBackground(new Color(122, 181, 236));
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        lblBanHang = new javax.swing.JLabel();
        lblSanpham = new javax.swing.JLabel();
        pnlTong = new javax.swing.JPanel();
        pnlBanHang = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblHoaDonCho = new javax.swing.JLabel();
        lblHoaDonGiao = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHoaDonGiao = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHoaDonHoanThanh = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        pnlSanPham = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cbbMauSac = new javax.swing.JComboBox<>();
        cbbSize = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        cbbChatLieu = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cbbHang = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtMaGiay = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblAnh = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        pnlWebCam = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtTenGiay = new javax.swing.JTextField();
        txtNamBaoHanh = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        txtGiaNhap = new javax.swing.JTextField();
        txtGiaBan = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtMaBarCode = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblGiay = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnHienThi = new javax.swing.JButton();
        btnThemExcel = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        rdoMauSac = new javax.swing.JRadioButton();
        rdoSize = new javax.swing.JRadioButton();
        rdoChatLieu = new javax.swing.JRadioButton();
        rdoHang = new javax.swing.JRadioButton();
        btnThemThuocTinh = new javax.swing.JButton();
        btnSuaThuocTinh = new javax.swing.JButton();
        btnXoaThuocTinh = new javax.swing.JButton();
        ckbTrangThai = new javax.swing.JCheckBox();
        btnLamMoi = new javax.swing.JButton();
        txtMaThuocTinh = new javax.swing.JTextField();
        txtTenThuocTinh = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblThuocTinh = new javax.swing.JTable();
        pnlThongKe = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        cbbBoloc = new javax.swing.JComboBox<>();
        jPanel27 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        lblTongDonHang = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        lblDoanhthu = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        lblTongTienDautu = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        lblTongTienThuVe = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        lblTienLai = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        btnXuatFile = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblThongKe = new javax.swing.JTable();
        pnlTimkiemTong = new javax.swing.JPanel();
        pnlTheoNgay = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        NgayBatDau = new com.toedter.calendar.JDateChooser();
        NgayKetThuc = new com.toedter.calendar.JDateChooser();
        pnlTheoNam = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        btnTimKiemTheoNam = new javax.swing.JButton();
        cbbNam = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        btnMailDoanhThu = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblThongKe1 = new javax.swing.JTable();
        cbbThongKeSanPham = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        btnXuatFile1 = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        lblSoGiayKd = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        lblSoGiayHet = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        lblSoGiaySapHet = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        btnMailSanPham = new javax.swing.JButton();
        pnlSoLuong = new javax.swing.JPanel();
        txtSoluong = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblLoi = new javax.swing.JLabel();
        pnlGiamGia = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblGiamgia = new javax.swing.JTable();
        jTextField10 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jTextField11 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        pnlNhanvien = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        btnThemNV = new javax.swing.JButton();
        btnSuaNV = new javax.swing.JButton();
        btnXoaNV = new javax.swing.JButton();
        btnResetNV = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblNhanvien = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        txtNgaySinh = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        rdoLam = new javax.swing.JRadioButton();
        rdoNghi = new javax.swing.JRadioButton();
        cboChucVu = new javax.swing.JComboBox<>();
        txtEmail = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        btnLoadNV = new javax.swing.JButton();
        jLabel76 = new javax.swing.JLabel();
        lblThoat = new javax.swing.JLabel();
        lblNhanvien = new javax.swing.JLabel();
        lblGiaoCa = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
        lblThongKe = new javax.swing.JLabel();
        lblDangXuat = new javax.swing.JLabel();
        lblThoiGian = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtNhanvien = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Hover_BanHang.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 720));

        lblBanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBanHangMouseClicked(evt);
            }
        });
        getContentPane().add(lblBanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 180, 60));

        lblSanpham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSanphamMouseClicked(evt);
            }
        });
        getContentPane().add(lblSanpham, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 180, 50));

        pnlTong.setLayout(new java.awt.CardLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Giỏ Hàng"));

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên SP", "Số Lượng", "Đơn Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHang.setFocusable(false);
        tblGioHang.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblGioHang.setShowGrid(false);
        tblGioHang.setShowHorizontalLines(true);
        tblGioHang.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblGioHang);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Sản Phẩm"));

        tblSanPham2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Hãng", "Size", "Màu", "Đơn giá", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham2.setFocusable(false);
        tblSanPham2.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblSanPham2.setShowGrid(false);
        tblSanPham2.setShowHorizontalLines(true);
        tblSanPham2.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblSanPham2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Hóa Đơn"));

        jLabel3.setText("Hóa đơn đang giao");

        jLabel4.setText("Hóa đơn chờ:");

        lblHoaDonCho.setText("0");

        lblHoaDonGiao.setText("0");

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonCho.setFocusable(false);
        tblHoaDonCho.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonCho.setShowGrid(false);
        tblHoaDonCho.setShowHorizontalLines(true);
        jScrollPane4.setViewportView(tblHoaDonCho);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("HĐ Chờ", jPanel5);

        tblHoaDonGiao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonGiao.setFocusable(false);
        tblHoaDonGiao.setSelectionBackground(new java.awt.Color(255, 204, 204));
        jScrollPane5.setViewportView(tblHoaDonGiao);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("HĐ Đang Giao", jPanel6);

        tblHoaDonHoanThanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonHoanThanh.setFocusable(false);
        tblHoaDonHoanThanh.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonHoanThanh.setShowGrid(false);
        tblHoaDonHoanThanh.setShowHorizontalLines(true);
        tblHoaDonHoanThanh.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblHoaDonHoanThanh);

        jTabbedPane1.addTab("HĐ Đã Hoàn Thành", jScrollPane3);

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(lblHoaDonGiao, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHoaDonCho, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblHoaDonGiao)
                    .addComponent(jLabel3)
                    .addComponent(lblHoaDonCho)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("HÓA ĐƠN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Mã HĐ");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Hình thức bán hàng");

        jLabel8.setText("Ngày tạo");

        jRadioButton1.setText("Tại quầy");

        jRadioButton2.setText("Giao hàng");

        jLabel9.setText("Hình thức thanh toán");

        jRadioButton3.setText("Tiền mặt");

        jRadioButton4.setText("Chuyển khoản");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jRadioButton5.setText("Cả hai");

        jLabel10.setText("Tên KH");

        jLabel11.setText("SĐT");

        jLabel13.setText("Tổng tiền ");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel14.setText("Tiền mặt");

        jLabel15.setText("Tiền CK");

        jLabel16.setText("Tiền dư");

        jButton3.setText("Tạo đơn");

        jButton4.setText("Thanh toán");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Hủy đơn");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField9)
                            .addComponent(jTextField8))
                        .addGap(74, 74, 74))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(11, 11, 11)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel8))
                                    .addComponent(jRadioButton1))
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(57, 57, 57)
                                        .addComponent(jRadioButton2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))
                            .addComponent(jLabel7)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton4)
                                .addGap(16, 16, 16)
                                .addComponent(jRadioButton5)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, Short.MAX_VALUE))
                .addGap(403, 403, 403))
        );

        javax.swing.GroupLayout pnlBanHangLayout = new javax.swing.GroupLayout(pnlBanHang);
        pnlBanHang.setLayout(pnlBanHangLayout);
        pnlBanHangLayout.setHorizontalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBanHangLayout.setVerticalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTong.add(pnlBanHang, "card2");

        jPanel10.setPreferredSize(new java.awt.Dimension(1025, 754));

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel2.setText("Năm bảo hành");

        jLabel12.setText("Màu sắc");

        cbbMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbbSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setText("Size");

        cbbChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setText("Chất liệu");

        cbbHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel19.setText("Hãng");

        jLabel20.setText("Số lượng");

        jLabel21.setText("Giá nhập");

        jLabel22.setText("Giá bán");

        txtMaGiay.setBackground(new java.awt.Color(238, 238, 238));
        txtMaGiay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaGiay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        jLabel23.setText("Mã giày");

        jLabel24.setText("Tên giày");

        lblAnh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, null, java.awt.Color.lightGray));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("WedCam"));
        jPanel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel12MouseClicked(evt);
            }
        });
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlWebCam.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        pnlWebCam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlWebCamMouseClicked(evt);
            }
        });
        pnlWebCam.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(pnlWebCam, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 200, 140));

        jLabel25.setText("Mã barCode");

        txtTenGiay.setBackground(new java.awt.Color(238, 238, 238));
        txtTenGiay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTenGiay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        txtNamBaoHanh.setBackground(new java.awt.Color(238, 238, 238));
        txtNamBaoHanh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamBaoHanh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        txtSoLuong.setBackground(new java.awt.Color(238, 238, 238));
        txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSoLuong.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        txtGiaNhap.setBackground(new java.awt.Color(238, 238, 238));
        txtGiaNhap.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiaNhap.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        txtGiaBan.setBackground(new java.awt.Color(238, 238, 238));
        txtGiaBan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiaBan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        jLabel51.setText("Ảnh");

        txtMaBarCode.setBackground(new java.awt.Color(238, 238, 238));
        txtMaBarCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaBarCode.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(250, 250, 250))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtMaGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTenGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNamBaoHanh, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(80, 80, 80)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHang, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaBarCode, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaBarCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaGiay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTenGiay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbbSize, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNamBaoHanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbHang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                        .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55))))))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        tblGiay.setBackground(new java.awt.Color(242, 242, 242));
        tblGiay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Giày", "Màu sắc", "Size", "Chất liệu", "Hãng", "Năm bảo hành", "Số lượng", "Giá Bán", "Ảnh"
            }
        ));
        tblGiay.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblGiay.setShowGrid(false);
        tblGiay.setShowHorizontalLines(true);
        tblGiay.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                tblGiayComponentRemoved(evt);
            }
        });
        tblGiay.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblGiayAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblGiay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGiayMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGiayMouseReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tblGiay);

        jLabel26.setText("Tìm kiếm sản phẩm");

        txtTimKiem.setBackground(new java.awt.Color(238, 238, 238));
        txtTimKiem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimKiem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1117, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addGap(32, 32, 32)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
        );

        btnThem.setBackground(new java.awt.Color(220, 220, 220));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Them.png"))); // NOI18N
        btnThem.setBorder(null);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Sua.png"))); // NOI18N
        btnSua.setBorder(null);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Xoa.png"))); // NOI18N
        btnXoa.setBorder(null);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Moi.png"))); // NOI18N
        btnMoi.setBorder(null);
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnHienThi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/HienThi.png"))); // NOI18N
        btnHienThi.setBorder(null);
        btnHienThi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiActionPerformed(evt);
            }
        });

        btnThemExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ThemExcel.png"))); // NOI18N
        btnThemExcel.setBorder(null);
        btnThemExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(btnThem)
                        .addGap(59, 59, 59)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXoa)
                        .addGap(70, 70, 70)
                        .addComponent(btnMoi)
                        .addGap(70, 70, 70)
                        .addComponent(btnThemExcel)
                        .addGap(66, 66, 66)
                        .addComponent(btnHienThi)
                        .addGap(136, 136, 136))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThem)
                        .addComponent(btnSua)
                        .addComponent(btnMoi)
                        .addComponent(btnHienThi)
                        .addComponent(btnThemExcel)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Sản phẩm", jPanel10);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý thuộc tính", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel33.setText("Mã thuộc tính");

        jLabel34.setText("Tên thuộc tính");

        rdoMauSac.setText("Màu sắc");
        rdoMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMauSacActionPerformed(evt);
            }
        });

        rdoSize.setText("Size");
        rdoSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSizeActionPerformed(evt);
            }
        });

        rdoChatLieu.setText("Chất liệu");
        rdoChatLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChatLieuActionPerformed(evt);
            }
        });

        rdoHang.setText("Hãng");
        rdoHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHangActionPerformed(evt);
            }
        });

        btnThemThuocTinh.setText("Thêm");
        btnThemThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemThuocTinhActionPerformed(evt);
            }
        });

        btnSuaThuocTinh.setText("Sửa");
        btnSuaThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaThuocTinhActionPerformed(evt);
            }
        });

        btnXoaThuocTinh.setText("Xóa");
        btnXoaThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaThuocTinhActionPerformed(evt);
            }
        });

        ckbTrangThai.setText("Trạng thái");

        btnLamMoi.setText("Mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        txtMaThuocTinh.setBackground(new java.awt.Color(238, 238, 238));
        txtMaThuocTinh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaThuocTinh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        txtTenThuocTinh.setBackground(new java.awt.Color(238, 238, 238));
        txtTenThuocTinh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTenThuocTinh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new javax.swing.ImageIcon(getClass().getResource("/img/Line 1 (1).png")))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(34, 266, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addComponent(rdoMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(76, 76, 76)
                                .addComponent(rdoSize, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(rdoChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57)))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(rdoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(ckbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(195, 195, 195))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(btnThemThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(btnSuaThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(btnXoaThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(264, 264, 264))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoMauSac)
                    .addComponent(rdoSize)
                    .addComponent(rdoChatLieu)
                    .addComponent(rdoHang))
                .addGap(22, 22, 22)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemThuocTinh)
                    .addComponent(btnSuaThuocTinh)
                    .addComponent(btnXoaThuocTinh)
                    .addComponent(btnLamMoi))
                .addGap(19, 19, 19))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin thuộc tính", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        tblThuocTinh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã thuộc tính", "Tên thuộc tính", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThuocTinh.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblThuocTinh.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblThuocTinh.setShowGrid(false);
        tblThuocTinh.setShowHorizontalLines(true);
        tblThuocTinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThuocTinhMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblThuocTinh);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1111, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Chi tiết giày", jPanel14);

        javax.swing.GroupLayout pnlSanPhamLayout = new javax.swing.GroupLayout(pnlSanPham);
        pnlSanPham.setLayout(pnlSanPhamLayout);
        pnlSanPhamLayout.setHorizontalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlSanPhamLayout.setVerticalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
        );

        pnlTong.add(pnlSanPham, "card3");

        jPanel25.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel27.setText("Bộ lọc: ");

        cbbBoloc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Theo ngày", "Theo năm" }));
        cbbBoloc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbBolocItemStateChanged(evt);
            }
        });

        jPanel27.setBackground(new java.awt.Color(255, 204, 204));
        jPanel27.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel44.setText("Tổng đơn hàng");

        lblTongDonHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongDonHang.setForeground(new java.awt.Color(0, 51, 51));
        lblTongDonHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bill.png"))); // NOI18N

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel44)
                    .addComponent(lblTongDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTongDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel36)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 153));
        jPanel28.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel45.setText("Doanh thu");

        lblDoanhthu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDoanhthu.setForeground(new java.awt.Color(0, 51, 51));
        lblDoanhthu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel46.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(102, 102, 102));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("VND");

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cart-9-48.png"))); // NOI18N

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel45)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(lblDoanhthu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel45)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDoanhthu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel37)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46))
        );

        jPanel29.setBackground(new java.awt.Color(153, 255, 153));
        jPanel29.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel47.setText("Tổng tiền đầu tư");

        lblTongTienDautu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongTienDautu.setForeground(new java.awt.Color(0, 51, 51));
        lblTongTienDautu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel48.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(102, 102, 102));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("VND");

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download__1_-removebg-preview.png"))); // NOI18N

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTongTienDautu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47))))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(lblTongTienDautu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel48))
        );

        jPanel30.setBackground(new java.awt.Color(153, 153, 255));
        jPanel30.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel49.setText("Tổng tiền thu về");

        lblTongTienThuVe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongTienThuVe.setForeground(new java.awt.Color(0, 51, 51));
        lblTongTienThuVe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel50.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(102, 102, 102));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("VND");

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download__2_-removebg-preview.png"))); // NOI18N

        lblTienLai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 0, 0));
        jLabel57.setText("Lãi:");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTienLai, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTongTienThuVe, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(81, 81, 81))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(lblTongTienThuVe, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTienLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel57)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addContainerGap())))
        );

        btnXuatFile.setBackground(new java.awt.Color(102, 255, 0));
        btnXuatFile.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnXuatFile.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnXuatFile.setText("Xuat File Excel");
        btnXuatFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatFileActionPerformed(evt);
            }
        });

        jPanel17.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã giày", "Tên giày", "Số lượng bán", "Doanh thu", "Ngày bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKe.setFocusable(false);
        tblThongKe.setRowHeight(25);
        tblThongKe.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblThongKe.setSelectionForeground(new java.awt.Color(255, 255, 51));
        tblThongKe.setShowGrid(false);
        tblThongKe.setShowHorizontalLines(true);
        tblThongKe.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(tblThongKe);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTimkiemTong.setLayout(new java.awt.CardLayout());

        pnlTheoNgay.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel42.setText("Từ:");

        jLabel43.setText("Đến: ");

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        NgayBatDau.setDateFormatString("dd-MM-yyyy");

        NgayKetThuc.setDateFormatString("dd-MM-yyyy");

        javax.swing.GroupLayout pnlTheoNgayLayout = new javax.swing.GroupLayout(pnlTheoNgay);
        pnlTheoNgay.setLayout(pnlTheoNgayLayout);
        pnlTheoNgayLayout.setHorizontalGroup(
            pnlTheoNgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTheoNgayLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlTheoNgayLayout.setVerticalGroup(
            pnlTheoNgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTheoNgayLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlTheoNgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTimKiem)
                    .addComponent(NgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlTheoNgayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel42)
                        .addComponent(jLabel43)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlTimkiemTong.add(pnlTheoNgay, "card2");

        pnlTheoNam.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel59.setText("Năm:");

        btnTimKiemTheoNam.setText("Tìm kiếm");
        btnTimKiemTheoNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemTheoNamActionPerformed(evt);
            }
        });

        cbbNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2019", "2020", "2021", "2022" }));

        javax.swing.GroupLayout pnlTheoNamLayout = new javax.swing.GroupLayout(pnlTheoNam);
        pnlTheoNam.setLayout(pnlTheoNamLayout);
        pnlTheoNamLayout.setHorizontalGroup(
            pnlTheoNamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTheoNamLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(cbbNam, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(btnTimKiemTheoNam)
                .addGap(119, 119, 119))
        );
        pnlTheoNamLayout.setVerticalGroup(
            pnlTheoNamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTheoNamLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlTheoNamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiemTheoNam)
                    .addComponent(jLabel59)
                    .addComponent(cbbNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlTimkiemTong.add(pnlTheoNam, "card2");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 102, 204));
        jLabel61.setText("Bảng doanh thu");

        btnMailDoanhThu.setBackground(new java.awt.Color(255, 0, 102));
        btnMailDoanhThu.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnMailDoanhThu.setForeground(new java.awt.Color(255, 255, 255));
        btnMailDoanhThu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/email.png"))); // NOI18N
        btnMailDoanhThu.setText("Gửi mail");
        btnMailDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMailDoanhThuActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 102, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("RESET");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel61)
                                .addGap(202, 202, 202)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(37, 37, 37)
                                .addComponent(btnMailDoanhThu)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuatFile, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbbBoloc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(174, 174, 174)
                        .addComponent(pnlTimkiemTong, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(cbbBoloc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTimkiemTong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMailDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXuatFile, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel61))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Doanh thu", jPanel25);

        jPanel21.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblThongKe1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã giày", "Tên giày", "Màu sắc", "Size", "Chất liệu", "Số lượng tồn", "Số lượng bán", "Đơn giá", "Doanh thu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKe1.setFocusable(false);
        tblThongKe1.setRowHeight(25);
        tblThongKe1.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblThongKe1.setShowGrid(false);
        tblThongKe1.setShowHorizontalLines(true);
        tblThongKe1.getTableHeader().setReorderingAllowed(false);
        jScrollPane13.setViewportView(tblThongKe1);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        cbbThongKeSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tat ca", "Top 5 san pham ban nhieu nhat", "Top 5 san pham doanh thu cao nhat", "Danh sach san pham co so luong ton thap" }));
        cbbThongKeSanPham.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbThongKeSanPhamItemStateChanged(evt);
            }
        });

        jLabel52.setText("Thống kê theo:");

        btnXuatFile1.setBackground(new java.awt.Color(102, 255, 0));
        btnXuatFile1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnXuatFile1.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatFile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnXuatFile1.setText("Xuat File Excel");
        btnXuatFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatFile1ActionPerformed(evt);
            }
        });

        jPanel32.setBackground(new java.awt.Color(255, 204, 204));
        jPanel32.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel53.setText("Số giày đang kinh doanh");

        lblSoGiayKd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSoGiayKd.setForeground(new java.awt.Color(0, 51, 51));
        lblSoGiayKd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bill.png"))); // NOI18N

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblSoGiayKd, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel53)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSoGiayKd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel40)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(255, 255, 153));
        jPanel33.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel54.setText("Số giày đã hết hàng");

        lblSoGiayHet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSoGiayHet.setForeground(new java.awt.Color(0, 51, 51));
        lblSoGiayHet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cart-9-48.png"))); // NOI18N

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSoGiayHet, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jLabel54)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addGap(18, 18, 18)
                        .addComponent(lblSoGiayHet, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel41)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel34.setBackground(new java.awt.Color(153, 255, 153));
        jPanel34.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel56.setText("Số giày sắp hết hàng");

        lblSoGiaySapHet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSoGiaySapHet.setForeground(new java.awt.Color(0, 51, 51));
        lblSoGiaySapHet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download__1_-removebg-preview.png"))); // NOI18N

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel56)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSoGiaySapHet, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSoGiaySapHet, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel60.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 102, 204));
        jLabel60.setText("Bảng sản phẩm");

        btnMailSanPham.setBackground(new java.awt.Color(255, 0, 102));
        btnMailSanPham.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnMailSanPham.setForeground(new java.awt.Color(255, 255, 255));
        btnMailSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/email.png"))); // NOI18N
        btnMailSanPham.setText("Gửi mail");
        btnMailSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMailSanPhamActionPerformed(evt);
            }
        });

        pnlSoLuong.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel55.setText("<=");

        jButton1.setText("Tìm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblLoi.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLoi.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout pnlSoLuongLayout = new javax.swing.GroupLayout(pnlSoLuong);
        pnlSoLuong.setLayout(pnlSoLuongLayout);
        pnlSoLuongLayout.setHorizontalGroup(
            pnlSoLuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSoLuongLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(pnlSoLuongLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblLoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlSoLuongLayout.setVerticalGroup(
            pnlSoLuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSoLuongLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(pnlSoLuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLoi, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(cbbThongKeSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel60)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnMailSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuatFile1))
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(32, 32, 32))))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(cbbThongKeSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXuatFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMailSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(pnlSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel60)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(61, Short.MAX_VALUE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(21, 21, 21))))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1120, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(0, 16, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 16, Short.MAX_VALUE)))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane4.addTab("Sản phẩm", jPanel18);

        javax.swing.GroupLayout pnlThongKeLayout = new javax.swing.GroupLayout(pnlThongKe);
        pnlThongKe.setLayout(pnlThongKeLayout);
        pnlThongKeLayout.setHorizontalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongKeLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThongKeLayout.setVerticalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );

        pnlTong.add(pnlThongKe, "card2");

        tblGiamgia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã", "Id Nhân Viên", "Ngày bắt đầu", "Ngày Kết Thúc", "Trạng Thái"
            }
        ));
        tblGiamgia.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblGiamgia.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tblGiamgia);

        jTextField10.setText("Tìm kiếm...");

        jButton6.setText("Sửa");

        jButton7.setText("Thêm");

        jButton8.setText("Xóa");

        jLabel28.setText("Ngày Bắt Đầu");

        jLabel29.setText("Ngày Kết Thúc");

        jLabel30.setText("Mã");

        jLabel31.setText("Trạng Thái");

        jLabel32.setBackground(new java.awt.Color(0, 0, 255));
        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 255));
        jLabel32.setText("Quản lí giảm giá");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(313, 313, 313)
                .addComponent(jButton7)
                .addGap(119, 119, 119)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(376, 376, 376))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(31, 31, 31)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(jTextField14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addGap(66, 66, 66)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(39, 39, 39)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton6)
                    .addComponent(jButton8))
                .addGap(46, 46, 46)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(80, 80, 80))
        );

        javax.swing.GroupLayout pnlGiamGiaLayout = new javax.swing.GroupLayout(pnlGiamGia);
        pnlGiamGia.setLayout(pnlGiamGiaLayout);
        pnlGiamGiaLayout.setHorizontalGroup(
            pnlGiamGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGiamGiaLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 41, Short.MAX_VALUE))
        );
        pnlGiamGiaLayout.setVerticalGroup(
            pnlGiamGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlTong.add(pnlGiamGia, "card2");

        btnThemNV.setText("Thêm");
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNVbtnThem1ActionPerformed(evt);
            }
        });

        btnSuaNV.setText("Sửa");
        btnSuaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNVbtnSua1ActionPerformed(evt);
            }
        });

        btnXoaNV.setText("Xoá");
        btnXoaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNVbtnXoa1ActionPerformed(evt);
            }
        });

        btnResetNV.setText("Làm mới");
        btnResetNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetNVbtnResetActionPerformed(evt);
            }
        });

        tblNhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanvien.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblNhanvien.setShowGrid(false);
        tblNhanvien.setShowHorizontalLines(true);
        tblNhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanvientblNhanvienMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tblNhanvien);

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin"));

        jLabel77.setText("Mã nhân viên");

        jLabel78.setText("Tên nhân viên");

        jLabel79.setText("Ngày sinh");

        jLabel80.setText("Số điện thoại");

        jLabel81.setText("Địa chỉ");

        jLabel82.setText("Email");

        jLabel83.setText("Trạng thái");

        jLabel84.setText("Chức vụ");

        buttonGroup1.add(rdoLam);
        rdoLam.setText("Đang làm");

        buttonGroup1.add(rdoNghi);
        rdoNghi.setText("Đang nghỉ");

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel62.setText("Mật khẩu");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addGap(18, 18, 18)
                        .addComponent(txtMa))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(15, 15, 15)
                        .addComponent(txtTen))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel79)
                        .addGap(37, 37, 37)
                        .addComponent(txtNgaySinh))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addGap(21, 21, 21)
                        .addComponent(txtSDT))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addGap(54, 54, 54)
                        .addComponent(txtDiaChi))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addGap(61, 61, 61)
                        .addComponent(txtEmail))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel83)
                            .addComponent(jLabel84)
                            .addComponent(jLabel62))
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(rdoLam)
                                        .addGap(18, 18, 18)
                                        .addComponent(rdoNghi)))
                                .addGap(19, 19, 19))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(txtMatKhau)))))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addGap(33, 33, 33)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(rdoLam)
                    .addComponent(rdoNghi))
                .addGap(21, 21, 21)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        btnLoadNV.setText("Hiển thị");
        btnLoadNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadNVActionPerformed(evt);
            }
        });

        jLabel76.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel76.setText("Quản Lý Nhân Viên");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(460, 460, 460)
                        .addComponent(jLabel76)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThemNV, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(btnSuaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(btnXoaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(btnResetNV)
                .addGap(105, 105, 105)
                .addComponent(btnLoadNV)
                .addGap(163, 163, 163))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel76)
                .addGap(12, 12, 12)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane14))
                .addGap(46, 46, 46)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNV)
                    .addComponent(btnSuaNV)
                    .addComponent(btnXoaNV)
                    .addComponent(btnResetNV)
                    .addComponent(btnLoadNV))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlNhanvienLayout = new javax.swing.GroupLayout(pnlNhanvien);
        pnlNhanvien.setLayout(pnlNhanvienLayout);
        pnlNhanvienLayout.setHorizontalGroup(
            pnlNhanvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNhanvienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pnlNhanvienLayout.setVerticalGroup(
            pnlNhanvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNhanvienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlTong.add(pnlNhanvien, "card2");

        getContentPane().add(pnlTong, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 50, 1140, 670));

        lblThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThoatMouseClicked(evt);
            }
        });
        getContentPane().add(lblThoat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 180, 50));

        lblNhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNhanvienMouseClicked(evt);
            }
        });
        getContentPane().add(lblNhanvien, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 180, 50));

        lblGiaoCa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGiaoCaMouseClicked(evt);
            }
        });
        getContentPane().add(lblGiaoCa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 180, 50));

        lblGiamGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblGiamGiaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblGiamGiaMouseEntered(evt);
            }
        });
        getContentPane().add(lblGiamGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 180, 50));

        lblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeMouseClicked(evt);
            }
        });
        getContentPane().add(lblThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 180, 60));

        lblDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDangXuatMouseClicked(evt);
            }
        });
        getContentPane().add(lblDangXuat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 180, 50));

        lblThoiGian.setText("ThoiGianODayNhe");
        getContentPane().add(lblThoiGian, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 210, -1));

        jLabel35.setText("Xin Chào:");
        getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 11, 80, -1));

        txtNhanvien.setEditable(false);
        getContentPane().add(txtNhanvien, new org.netbeans.lib.awtextra.AbsoluteConstraints(377, 8, 178, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblBanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBanHangMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_BanHang.png/"));
        jLabel1.setIcon(img);
        pnlBanHang.setVisible(true);
        pnlSanPham.setVisible(false);
        pnlNhanvien.setVisible(false);
        pnlThongKe.setVisible(false);
        pnlGiamGia.setVisible(false);

    }//GEN-LAST:event_lblBanHangMouseClicked

    private void lblSanphamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSanphamMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_SanPham.png/"));
        jLabel1.setIcon(img);
        pnlBanHang.setVisible(false);
        pnlThongKe.setVisible(false);
        pnlGiamGia.setVisible(false);
        pnlNhanvien.setVisible(false);
        pnlSanPham.setVisible(true);
    }//GEN-LAST:event_lblSanphamMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_Thoat.png/"));
        jLabel1.setIcon(img);
        System.exit(0);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void lblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_ThongKe.png/"));
        jLabel1.setIcon(img);
        pnlBanHang.setVisible(false);
        pnlSanPham.setVisible(false);
        
        pnlGiamGia.setVisible(false);
        pnlNhanvien.setVisible(false);
        pnlThongKe.setVisible(true);
    }//GEN-LAST:event_lblThongKeMouseClicked

    private void lblGiamGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGiamGiaMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_GiamGia.png/"));
        jLabel1.setIcon(img);
        pnlBanHang.setVisible(false);
        pnlSanPham.setVisible(false);
        pnlThongKe.setVisible(false);
        pnlNhanvien.setVisible(false);
        
        pnlGiamGia.setVisible(true);
    }//GEN-LAST:event_lblGiamGiaMouseClicked

    private void lblNhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNhanvienMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_Nhanvien.png/"));
        jLabel1.setIcon(img);
        pnlBanHang.setVisible(false);
        pnlSanPham.setVisible(false);
        pnlThongKe.setVisible(false);
        pnlGiamGia.setVisible(false);
        pnlNhanvien.setVisible(true);
    }//GEN-LAST:event_lblNhanvienMouseClicked

    private void lblGiaoCaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGiaoCaMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_GiaoCa.png/"));
        jLabel1.setIcon(img);
        new QLGCA().setVisible(true);
    }//GEN-LAST:event_lblGiaoCaMouseClicked

    private void lblGiamGiaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblGiamGiaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblGiamGiaMouseEntered

    private void lblDangXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDangXuatMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_DangXuat.png/"));
        jLabel1.setIcon(img);
    }//GEN-LAST:event_lblDangXuatMouseClicked

    private void btnThemNVbtnThem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNVbtnThem1ActionPerformed
        NhanVienViewModel nv = getFormNV();
        chService.them(nv);
        fillTableNV();
    }//GEN-LAST:event_btnThemNVbtnThem1ActionPerformed

    private void btnSuaNVbtnSua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNVbtnSua1ActionPerformed
        NhanVienViewModel nv = getFormNV();
        chService.sua(nv);
        fillTableNV();
    }//GEN-LAST:event_btnSuaNVbtnSua1ActionPerformed

    private void btnXoaNVbtnXoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNVbtnXoa1ActionPerformed
        NhanVienViewModel nv = getFormNV();
        chService.xoa(nv);
        fillTableNV();
    }//GEN-LAST:event_btnXoaNVbtnXoa1ActionPerformed

    private void btnResetNVbtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetNVbtnResetActionPerformed
        txtMa.setText("");
        txtTen.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        rdoLam.setSelected(true);
        cboChucVu.setSelectedIndex(0);
        txtMatKhau.setText("");
    }//GEN-LAST:event_btnResetNVbtnResetActionPerformed

    private void tblNhanvientblNhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanvientblNhanvienMouseClicked
        int row = tblNhanvien.getSelectedRow();
        NhanVien ch = list.get(row);
        txtMa.setText(ch.getMa());
        txtTen.setText(ch.getHoTen());
        txtDiaChi.setText(ch.getDiaChi());
        txtEmail.setText(ch.getEmail());
        txtSDT.setText(ch.getSoDienThoai());
        txtNgaySinh.setText(ch.getNgaySinh());
        cboChucVu.setSelectedItem(ch.getChucVu().getTen());
        if(ch.getTrangThai()==1){
            rdoLam.setSelected(true);
        }else{
            rdoNghi.setSelected(true);
        }
        txtMatKhau.setText(ch.getMatKhau());
    }//GEN-LAST:event_tblNhanvientblNhanvienMouseClicked

    private void btnLoadNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadNVActionPerformed
        fillTableNV();
    }//GEN-LAST:event_btnLoadNVActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        // TODO add your handling code here:
        try {
            JFileChooser fc = new JFileChooser("");
            fc.showOpenDialog(null);
            File f = fc.getSelectedFile();
            //            Image img = ImageIO.read(f);
            //            anhStr = f.getName();
            anhStr = f.getAbsolutePath();
            lblAnh.setText("");
            //            int width = lblAnh.getWidth();
            //            int height = lblAnh.getHeight();
            //            lblAnh.setIcon(new ImageIcon(img.getScaledInstance(width, height, 0)));
            lblAnh.setIcon(resizeImage(String.valueOf(anhStr)));
            System.out.println(anhStr);
        } catch (Exception ex) {
            
            ex.printStackTrace();
        }
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnThemExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemExcelActionPerformed
        // TODO add your handling code here:
        try {
            ArrayList<ChiTietGiay> list = new ArrayList<>();
            JFileChooser fc = new JFileChooser("E:\\ki_4\\block2\\Da1\\excel");
            fc.showOpenDialog(null);
            File f = fc.getSelectedFile();
            //            exelStr = f.getAbsolutePath();
            //            lblAnh.setIcon(ResizeImage(String.valueOf(exelStr)));
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemExcelActionPerformed

    private void tblGiayComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_tblGiayComponentRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGiayComponentRemoved

    private void tblGiayAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblGiayAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGiayAncestorAdded

    private void tblGiayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGiayMouseClicked
        // TODO add your handling code here:
        int row = tblGiay.getSelectedRow();
        ChiTietGiay_View ct = listCTG.get(row);
        showDeTaiChiTietGiay(ct);
    }//GEN-LAST:event_tblGiayMouseClicked

    private void tblGiayMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGiayMouseReleased
        // TODO add your handling code here:
//        if (evt.isPopupTrigger()) {
//            jPopupMenu1.show(this, 500, 430 + (tblGiay.getSelectedRow() * 25));
//        }
    }//GEN-LAST:event_tblGiayMouseReleased

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        btnThem.setBackground(new Color(242, 242, 242));
        ChiTietGiay ct = getFormChiTietGiay();
        chiTietGiaYSV.themChiTietGiay(ct);
        fillTableChiTietGiay();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        ChiTietGiay ct = getFormChiTietGiay();
        int row = tblGiay.getSelectedRow();
        //        listChiTiet.get(row).getId();
        String id = chiTietGiaYSV.getIdGiay(txtMaGiay.getText());
        ct.setId(id);
        
        int row1 = chiTietGiaYSV.suaChiTietGiay(ct);
        if (row1 > 0) {
            JOptionPane.showMessageDialog(this, "Update thanh cong");
            fillTableChiTietGiay();
        } else {
            JOptionPane.showMessageDialog(this, "Update that bai");
        }

        //        ChiTietGiay ct = getFormChiTietGiay();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        ChiTietGiay ct = getFormChiTietGiay();
        //        int row = tblGiay.getSelectedRow();
        //        listChiTiet.get(row).getId();chiTietGiaYSV.getIdGiay(txtMaGiay.getText());chiTietGiaYSV.getIdGiay();
        String id = chiTietGiaYSV.getIdGiay(txtMaGiay.getText());
        ct.setId(id);
        
        int row1 = chiTietGiaYSV.xoaChiTietGiay(ct);
        if (row1 > 0) {
            JOptionPane.showMessageDialog(this, "Update thanh cong");
            fillTableChiTietGiay();
        } else {
            JOptionPane.showMessageDialog(this, "Update that bai");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        lamMoi();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnHienThiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiActionPerformed
        // TODO add your handling code here:
        fillTableChiTietGiay();
        fillCbbSize();
        fillCbbChatLieu();
        fillCbbHangGiay();
        fillCbbMauSac();
    }//GEN-LAST:event_btnHienThiActionPerformed

    private void rdoMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMauSacActionPerformed
        fillTableThuocTinhMau();
    }//GEN-LAST:event_rdoMauSacActionPerformed

    private void rdoSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSizeActionPerformed
        // TODO add your handling code here:
        fillTableThuocTinhSize();
    }//GEN-LAST:event_rdoSizeActionPerformed

    private void rdoChatLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChatLieuActionPerformed
        // TODO add your handling code here:
        fillTableThuocTinhChatLieu();
    }//GEN-LAST:event_rdoChatLieuActionPerformed

    private void rdoHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHangActionPerformed
        // TODO add your handling code here:
        fillTableThuocTinhHang();
    }//GEN-LAST:event_rdoHangActionPerformed

    private void btnThemThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemThuocTinhActionPerformed
        if (rdoMauSac.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            mauSV.themMauSac(tt);
            fillTableThuocTinhMau();
        } else if (rdoSize.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            sizeSV.themSize(tt);
            fillTableThuocTinhSize();
        } else if (rdoChatLieu.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            chatLieuSV.themChatLieu(tt);
            fillTableThuocTinhChatLieu();
        } else if (rdoHang.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            hangSV.themHangGiay(tt);
            fillTableThuocTinhHang();
        }
    }//GEN-LAST:event_btnThemThuocTinhActionPerformed

    private void btnSuaThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaThuocTinhActionPerformed
        // TODO add your handling code here:
        String idMau = mauSV.getIdMauSac(txtMaThuocTinh.getText());
        String idSize = sizeSV.getIdSize(txtMaThuocTinh.getText());
        String idChatLieu = chatLieuSV.getIdChatLieu(txtMaThuocTinh.getText());
        String idHang = hangSV.getIdHangGiay(txtMaThuocTinh.getText());
        if (rdoMauSac.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            mauSV.suaMauSac(tt, idMau);
            fillTableThuocTinhMau();
        } else if (rdoSize.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            sizeSV.suaSize(tt, idSize);
            fillTableThuocTinhHang();
        } else if (rdoChatLieu.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            chatLieuSV.suaChatLieu(tt, idChatLieu);
            fillTableThuocTinhChatLieu();
        } else if (rdoHang.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            hangSV.suaHangGiay(tt, idHang);
            fillTableThuocTinhHang();
        }
    }//GEN-LAST:event_btnSuaThuocTinhActionPerformed

    private void btnXoaThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaThuocTinhActionPerformed
        // TODO add your handling code here:
        String idMau = mauSV.getIdMauSac(txtMaThuocTinh.getText());
        String idSize = sizeSV.getIdSize(txtMaThuocTinh.getText());
        String idChatLieu = chatLieuSV.getIdChatLieu(txtMaThuocTinh.getText());
        String idHang = hangSV.getIdHangGiay(txtMaThuocTinh.getText());
        if (rdoMauSac.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            mauSV.xoaMauSac(tt, idMau);
            fillTableThuocTinhMau();
        } else if (rdoSize.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            sizeSV.xoaSize(tt, idSize);
            fillTableThuocTinhSize();
        } else if (rdoChatLieu.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            chatLieuSV.xoaChatLieu(tt, idChatLieu);
            fillTableThuocTinhChatLieu();
        } else if (rdoHang.isSelected()) {
            ThuocTinh tt = getFormThuocTinh();
            hangSV.xoaHangGiay(tt, idHang);
            fillTableThuocTinhHang();
        }
    }//GEN-LAST:event_btnXoaThuocTinhActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        txtMaThuocTinh.setText("");
        txtTenThuocTinh.setText("");
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void tblThuocTinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThuocTinhMouseClicked
        // TODO add your handling code here:
        int index = tblThuocTinh.getSelectedRow();
        model = (DefaultTableModel) tblThuocTinh.getModel();
        String id = tblThuocTinh.getValueAt(index, 0).toString();
        if (rdoMauSac.isSelected()) {
            ThuocTinh tt = mauSV.getAllMauSacById(id);
            showDeTaiThuocTinh(tt);
        } else if (rdoSize.isSelected()) {
            ThuocTinh tt = sizeSV.getAllSizeById(id);
            showDeTaiThuocTinh(tt);
        } else if (rdoChatLieu.isSelected()) {
            ThuocTinh tt = chatLieuSV.getAllChatLieuById(id);
            showDeTaiThuocTinh(tt);
        } else if (rdoHang.isSelected()) {
            ThuocTinh tt = hangSV.getAllHangGiayById(id);
            showDeTaiThuocTinh(tt);
        }
    }//GEN-LAST:event_tblThuocTinhMouseClicked

    private void cbbThongKeSanPhamItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbThongKeSanPhamItemStateChanged
        if (cbbThongKeSanPham.getSelectedIndex() == 0) {
            listTKSanPham = thongKe_Serv.getAll();
            loadTableSanPham(listTKSanPham);
        } else if (cbbThongKeSanPham.getSelectedIndex() == 1) {
            listTKSanPham = thongKe_Serv.getTop5SoLuongBan();
            loadTableSanPham(listTKSanPham);
        } else if (cbbThongKeSanPham.getSelectedIndex() == 2) {
            listTKSanPham = thongKe_Serv.getTop5DoanhThu();
            loadTableSanPham(listTKSanPham);
        } else {
            pnlSoLuong.setVisible(true);
            
        }
    }//GEN-LAST:event_cbbThongKeSanPhamItemStateChanged

    private void btnXuatFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatFile1ActionPerformed
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("danh sach");
        //
        XSSFRow row = null;
        Cell cell = null;
        row = sheet.createRow(2);
        
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Mã giày");
        
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Tên giày");
        
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Màu sắc");
        
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Size");
        
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Chất liệu");
        
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Số lượng tồn");
        
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Số lượng bán");
        
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Đơn giá");
        
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Doanh thu");
        
        row = sheet.createRow(0);
        cell = row.createCell(3, CellType.STRING);
        
        JFileChooser f = new JFileChooser("");
        f.setDialogTitle("Mở  file");
        f.showSaveDialog(null);
        
        File file = new File(f.getSelectedFile() + ".xlsx");
        
        if (f.getSelectedFile() == null) {
            return;
        }
        for (int i = 0; i < listTKSanPham.size(); i++) {
            //Modelbook book =listTK.get(i);
            row = sheet.createRow(3 + i);
            
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getMaSp());
            
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getTenSp());
            
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getMau());
            
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getSize());
            
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getChatLieu());
            
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getSlTon());
            
            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getSlBan());
            
            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getDonGia());
            
            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue(listTKSanPham.get(i).getDoanhThu());
            
        }
        
        try {
            FileOutputStream fis = new FileOutputStream(file);
            workbook.write(fis);
            JOptionPane.showMessageDialog(this, "in thanh cong D:\\danhsach");
            fis.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        

    }//GEN-LAST:event_btnXuatFile1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String sl = txtSoluong.getText();
        int slInt = 0;
        if (sl.trim().length() == 0) {
            lblLoi.setText("Vui lòng nhập số lượng mong muốn!");
            return;
        } else {
            lblLoi.setText("");
        }
        try {
            slInt = Integer.parseInt(sl);
            lblLoi.setText("");
        } catch (NumberFormatException numberFormatException) {
            lblLoi.setText("Vui lòng nhập số nguyen!");
            return;
        }
        
        if (slInt <= 0) {
            lblLoi.setText("Vui lòng nhập số lớn hơn 0!");
            return;
        } else {
            lblLoi.setText("");
        }
        
        listTKSanPham = thongKe_Serv.getMinSoLuongTon(slInt);
        loadTableSanPham(listTKSanPham);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbbBolocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbBolocItemStateChanged
        if (cbbBoloc.getSelectedIndex() == 0) {
            pnlTimkiemTong.setVisible(false);
            listTKDoanhThu = thongKe_Serv.getAllTheoNgayHomNay();
            loadTableThongKe(listTKDoanhThu);
            setPanelThongKe(thongKe_Serv.getDoanhThuSoLuong().getSoDonHang(), thongKe_Serv.getDoanhThuSoLuong().getDoanhthu());
        } else if (cbbBoloc.getSelectedIndex() == 1) {
            pnlTimkiemTong.setVisible(true);
            pnlTheoNam.setVisible(false);
            pnlTheoNgay.setVisible(true);
            
        } else {
            pnlTimkiemTong.setVisible(true);
            pnlTheoNgay.setVisible(false);
            pnlTheoNam.setVisible(true);
        }
    }//GEN-LAST:event_cbbBolocItemStateChanged

    private void btnXuatFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatFileActionPerformed
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("danh sach");
        //
        XSSFRow row = null;
        Cell cell = null;
        row = sheet.createRow(2);
        
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Mã giày");
        
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Tên giày");
        
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Số lượng bán");
        
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Doanh thu");
        
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Ngày bán");
        
        row = sheet.createRow(0);
        cell = row.createCell(3, CellType.STRING);
        
        for (int i = 0; i < listTKDoanhThu.size(); i++) {
            //Modelbook book =listTK.get(i);
            row = sheet.createRow(3 + i);
            
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(listTKDoanhThu.get(i).getMaSp());
            
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(listTKDoanhThu.get(i).getTenSp());
            
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(listTKDoanhThu.get(i).getSlBan());
            
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(listTKDoanhThu.get(i).getDoanhThu());
            
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(listTKDoanhThu.get(i).getNgaytao());
            
        }
        
        JFileChooser f = new JFileChooser("D:\\");
        f.setDialogTitle("Mở  file");
        f.showSaveDialog(null);
        
        File file = new File(f.getSelectedFile() + ".xlsx");
        if (f.getSelectedFile() == null) {
            return;
        }
        
        try {
            FileOutputStream fis = new FileOutputStream(file);
            workbook.write(fis);
            JOptionPane.showMessageDialog(this, "in thanh cong D:\\danhsach");
            fis.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_btnXuatFileActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        Date batDau = NgayBatDau.getDate();
        Date KetThuc = NgayKetThuc.getDate();
        String ngayBD = null;
        String ngayKT = null;
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        ngayBD = sdf.format(batDau);
        ngayKT = sdf.format(KetThuc);
        if (ngayKT.compareTo(ngayBD) < 0) {
            JOptionPane.showMessageDialog(this, "Ngày tìm kiếm phải lớn hơn ngày bắt đầu");
            return;
        }
        System.out.println(ngayBD);
        listTKDoanhThu = thongKe_Serv.getAllTheoKhoangNgay(ngayBD, ngayKT);
        loadTableThongKe(listTKDoanhThu);
        setPanelThongKe(thongKe_Serv.getDoanhThuSoLuong2(ngayBD, ngayKT).getSoDonHang(), thongKe_Serv.getDoanhThuSoLuong2(ngayBD, ngayKT).getDoanhthu());
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnTimKiemTheoNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemTheoNamActionPerformed
        String nam = cbbNam.getSelectedItem().toString();
        listTKDoanhThu = thongKe_Serv.getAllTHeoNam(nam);
        loadTableThongKe(listTKDoanhThu);
        setPanelThongKe(thongKe_Serv.getDoanhThuSoLuong2(nam + "-1-1", nam + "-12-30").getSoDonHang(), thongKe_Serv.getDoanhThuSoLuong2(nam + "-1-1", nam + "-12-30").getDoanhthu());
    }//GEN-LAST:event_btnTimKiemTheoNamActionPerformed

    private void btnMailDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMailDoanhThuActionPerformed
        String input = null;
        input = JOptionPane.showInputDialog(this, "Nhập địa chỉ email cần gửi", "Địa chỉ", JOptionPane.YES_NO_OPTION);
        if (input == null) {
            return;
        }
        if (input != null && input.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ mail không hợp lệ");
            return;
        }
        
        final String username = "hoangnhph24464@fpt.edu.vn";
        final String password = "Hh30052002";
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("hoangnhph24464@fpt.edu.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(input)
            );
            message.setSubject("Xin chào bạn");
            message.setText("Số đơn hàng: " + lblTongDonHang.getText() + "\n"
                    + "Doanh thu: " + lblDoanhthu.getText() + "\n"
                    + "Tiền đầu tư: :" + lblTongTienDautu.getText() + "\n"
                    + "Tiền thu về: " + lblTongTienThuVe.getText());
            
            try {
                Transport.send(message);
            } catch (MessagingException messagingException) {
                JOptionPane.showMessageDialog(this, "Địa chỉ email không đúng");
                
                return;
                
            }
            
            JOptionPane.showMessageDialog(this, "Gửi thành công");
            
        } catch (AddressException ex) {
            System.out.println("Địa chỉ email không đúng");
            return;
            
        } catch (MessagingException e) {
            System.out.println("ahihi3");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMailDoanhThuActionPerformed

    private void btnMailSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMailSanPhamActionPerformed
        String input = null;
        input = JOptionPane.showInputDialog(this, "Nhập địa chỉ email cần gửi", "Địa chỉ", JOptionPane.YES_NO_OPTION);
        if (input == null) {
            return;
        }
        if (input != null && input.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ mail không hợp lệ");
            return;
        }
        
        final String username = "hoangnhph24464@fpt.edu.vn";
        final String password = "Hh30052002";
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("hoangnhph24464@fpt.edu.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(input)
            );
            message.setSubject("Xin chào bạn");
            message.setText("Số giày đang kinh doanh: " + lblSoGiayKd.getText() + "\n"
                    + "Số giày đã hết hàng: " + lblSoGiayHet.getText() + "\n"
                    + "Số giày sắp hết hàng: :" + lblSoGiaySapHet.getText() + "\n");
            
            try {
                Transport.send(message);
            } catch (MessagingException messagingException) {
                JOptionPane.showMessageDialog(this, "Địa chỉ email không đúng");
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Gửi thành công");
            
        } catch (AddressException ex) {
            JOptionPane.showMessageDialog(this, "Địa chỉ email không đúng");
            
            ex.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("ahihi3");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMailSanPhamActionPerformed

    private void pnlWebCamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWebCamMouseClicked
        
        initWebcam();

    }//GEN-LAST:event_pnlWebCamMouseClicked

    private void jPanel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseClicked

    }//GEN-LAST:event_jPanel12MouseClicked

    //sanPham--------------------------------------------------------------------------------------
    private void lamMoi() {
        txtMaGiay.setText("");
        txtTenGiay.setText("");
        txtNamBaoHanh.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        cbbChatLieu.setSelectedIndex(0);
        cbbHang.setSelectedIndex(0);
        cbbSize.setSelectedIndex(0);
        cbbMauSac.setSelectedIndex(0);
        lblAnh.setIcon(null);
        txtMaBarCode.setText("");
    }
    
    private void showDeTaiChiTietGiay(ChiTietGiay_View ct) {
        txtMaGiay.setText(ct.getMaGiay());
        txtTenGiay.setText(ct.getTenGiay());
        cbbMauSac.setSelectedItem(ct.getTenMauSac());
        cbbSize.setSelectedItem(ct.getSize());
        cbbChatLieu.setSelectedItem(ct.getChatLieu());
        cbbHang.setSelectedItem(ct.getTenHang());
        txtNamBaoHanh.setText(String.valueOf(ct.getNamBaoHanh()));
        txtSoLuong.setText(String.valueOf(ct.getSoLuong()));
        txtGiaNhap.setText(String.valueOf(ct.getGiaNhap()));
        txtGiaBan.setText(String.valueOf(ct.getGiaBan()));
//        lblAnh.setText("");
        int row = tblGiay.getSelectedRow();
        String icon = tblGiay.getValueAt(row, 8).toString();
        lblAnh.setIcon(resizeImage(String.valueOf(icon)));
    }
    
    private void showDeTaiThuocTinh(ThuocTinh tt) {
//        txtIdThuocTinh.setText(tt.getId());
        txtMaThuocTinh.setText(tt.getMa());
        txtTenThuocTinh.setText(tt.getTen());
        if (tt.getTrangThai() == 1) {
            ckbTrangThai.setSelected(true);
        } else {
            ckbTrangThai.setSelected(false);
        }
    }
    
    private ThuocTinh getFormThuocTinh() {
        String ma = txtMaThuocTinh.getText();
        String ten = txtTenThuocTinh.getText();
        int trangThai = ckbTrangThai.isSelected() ? 1 : 2;
        
        ThuocTinh tt = new ThuocTinh(null, ma, ten, trangThai);
        return tt;
    }
    
    private void initWebcam() {
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        try {
            webcam.setViewSize(size);
        } catch (Exception e) {
            return;
        }
        
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);
        
        pnlWebCam.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 280));
        exe.execute(this);
    }
    
    private ChiTietGiay getFormChiTietGiay() {
//        String maG = txtMaGiay.getText();
//        String tenG = txtTenGiay.getText();
//        String idCL = chatLieuSV.getIdChatLieuByTen(cbbChatLieu.getSelectedItem().toString());
//        String idMS = mauSV.getIdMauSacByTen(cbbMauSac.getSelectedItem().toString());
//        String idS = sizeSV.getIdSizeByTen(cbbSize.getSelectedItem().toString());
//        String idH = hangSV.getIdHangByTen(cbbHang.getSelectedItem().toString());
//        int namBH = Integer.parseInt(txtNamBaoHanh.getText());
//        int soLuong = Integer.parseInt(txtSoLuong.getText());
//        double giaNhap = Double.parseDouble(txtGiaNhap.getText());
//        double giaBan = Double.parseDouble(txtGiaBan.getText());
//        String anh = lblAnh.getText();
//
//        ChiTietGiay ct = new ChiTietGiay(null, maG, tenG, idMS, idS, idCL, idH, namBH, soLuong, giaNhap, giaBan, anh);
//        return ct;

        ChiTietGiay ct = new ChiTietGiay();
        ct.setMaGiay(txtMaGiay.getText());
        ct.setTenGiay(txtTenGiay.getText());
        ct.setIdMauSac(mauSV.getIdMauSacByTen(cbbMauSac.getSelectedItem().toString()));
        ct.setIdSize(sizeSV.getIdSizeByTen(cbbSize.getSelectedItem().toString()));
        ct.setIdChatLieu(chatLieuSV.getIdChatLieuByTen(cbbChatLieu.getSelectedItem().toString()));
        ct.setIdHang(hangSV.getIdHangByTen(cbbHang.getSelectedItem().toString()));
        ct.setNamBaoHanh(Integer.parseInt(txtNamBaoHanh.getText()));
        ct.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        ct.setGiaNhap(Double.parseDouble(txtGiaNhap.getText()));
        ct.setGiaBan(Double.parseDouble(txtGiaBan.getText()));
        ct.setMaBarCode(txtMaBarCode.getText());
        ct.setAnh(anhStr);
        return ct;
    }
    
    public NhanVienViewModel getFormNV(){
        String ma = txtMa.getText();
        String ten = txtTen.getText();
        String dc = txtDiaChi.getText();
        String em = txtEmail.getText();
        String ns = txtNgaySinh.getText();
        String sdt = txtSDT.getText();
        int tt;
        if(rdoLam.isSelected()){
            tt=1;
        }else{
            tt=0;
        }
        String cv = String.valueOf(cboChucVu.getSelectedItem());
        String mk = txtMatKhau.getText();
        ChucVuDomain idcv = cvService.layIdChucVu(cv);
        return new NhanVienViewModel(ma, ten, ns, sdt, dc, em, tt, mk, idcv);
    }
    
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
            
            Result rs = null;
            BufferedImage image = null;
            
            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }
            
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            
            try {
                rs = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException notFoundException) {
//                notFoundException.printStackTrace();
            }
            
            if (rs != null) {
                txtMaBarCode.setText(rs.getText());
                txtTimKiem.setText(rs.getText());
            }
            
        } while (true);
    }
    
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Menu().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser NgayBatDau;
    private com.toedter.calendar.JDateChooser NgayKetThuc;
    private javax.swing.JButton btnHienThi;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLoadNV;
    private javax.swing.JButton btnMailDoanhThu;
    private javax.swing.JButton btnMailSanPham;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnResetNV;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSuaNV;
    private javax.swing.JButton btnSuaThuocTinh;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemExcel;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnThemThuocTinh;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimKiemTheoNam;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaNV;
    private javax.swing.JButton btnXoaThuocTinh;
    private javax.swing.JButton btnXuatFile;
    private javax.swing.JButton btnXuatFile1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbBoloc;
    private javax.swing.JComboBox<String> cbbChatLieu;
    private javax.swing.JComboBox<String> cbbHang;
    private javax.swing.JComboBox<String> cbbMauSac;
    private javax.swing.JComboBox<String> cbbNam;
    private javax.swing.JComboBox<String> cbbSize;
    private javax.swing.JComboBox<String> cbbThongKeSanPham;
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JCheckBox ckbTrangThai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblBanHang;
    private javax.swing.JLabel lblDangXuat;
    private javax.swing.JLabel lblDoanhthu;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblGiaoCa;
    private javax.swing.JLabel lblHoaDonCho;
    private javax.swing.JLabel lblHoaDonGiao;
    private javax.swing.JLabel lblLoi;
    private javax.swing.JLabel lblNhanvien;
    private javax.swing.JLabel lblSanpham;
    private javax.swing.JLabel lblSoGiayHet;
    private javax.swing.JLabel lblSoGiayKd;
    private javax.swing.JLabel lblSoGiaySapHet;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JLabel lblThoiGian;
    private javax.swing.JLabel lblThongKe;
    private javax.swing.JLabel lblTienLai;
    private javax.swing.JLabel lblTongDonHang;
    private javax.swing.JLabel lblTongTienDautu;
    private javax.swing.JLabel lblTongTienThuVe;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlGiamGia;
    private javax.swing.JPanel pnlNhanvien;
    private javax.swing.JPanel pnlSanPham;
    private javax.swing.JPanel pnlSoLuong;
    private javax.swing.JPanel pnlTheoNam;
    private javax.swing.JPanel pnlTheoNgay;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JPanel pnlTimkiemTong;
    private javax.swing.JPanel pnlTong;
    private javax.swing.JPanel pnlWebCam;
    private javax.swing.JRadioButton rdoChatLieu;
    private javax.swing.JRadioButton rdoHang;
    private javax.swing.JRadioButton rdoLam;
    private javax.swing.JRadioButton rdoMauSac;
    private javax.swing.JRadioButton rdoNghi;
    private javax.swing.JRadioButton rdoSize;
    private javax.swing.JTable tblGiamgia;
    private javax.swing.JTable tblGiay;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblHoaDonGiao;
    private javax.swing.JTable tblHoaDonHoanThanh;
    private javax.swing.JTable tblNhanvien;
    private javax.swing.JTable tblSanPham2;
    private javax.swing.JTable tblThongKe;
    private javax.swing.JTable tblThongKe1;
    private javax.swing.JTable tblThuocTinh;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMaBarCode;
    private javax.swing.JTextField txtMaGiay;
    private javax.swing.JTextField txtMaThuocTinh;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtNamBaoHanh;
    private javax.swing.JTextField txtNgaySinh;
    public static javax.swing.JTextField txtNhanvien;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtSoluong;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTenGiay;
    private javax.swing.JTextField txtTenThuocTinh;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
