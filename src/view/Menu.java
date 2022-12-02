package view;

import DomainModel.ChiTietGiay;
import DomainModel.ChucVuDomain;
import DomainModel.HoaDonBanHang;
import DomainModel.HoaDonCT;
import DomainModel.KhachHang;
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
import repository.HoaDonBanHangRepository;
import repository.HoaDonCTRepository;
import repository.KhachHangRepository;
import repository.SanPhanBanHangRepository;
import service.ChucVu_Service;
import service.NhanVien_Service;
import service.service_Impl.ChucVu_ServiceImpl;
import service.service_Impl.HoaDonInService;
import service.service_Impl.NhanVien_ServiceImpl;
import service.service_Impl.chuCuaHangService;
import viewModel.HoaDonCTBanHang;
import viewModel.NhanVienViewModel;
import viewModel.SanPhamBanHang;
import viewModel.ThongTinHoaDonBanHang;

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
    List<HoaDonCTBanHang> listgh = new ArrayList<>();
    List<ThongTinHoaDonBanHang> listhdbh;
    List<SanPhamBanHang> listspbh;
    private ArrayList<HoaDonCTBanHang> listHdct = new ArrayList<>();
    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private Executor exe = Executors.newSingleThreadExecutor(this);
    chuCuaHangService _CuaHangService = new chuCuaHangService();

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
    HoaDonInService _DonInService = new HoaDonInService();

    public Menu() {
        initComponents();
        dcf.applyPattern(pattern);
        this.setLocationRelativeTo(null);

        // Thống kê
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
        // Sản phẩm
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
        // Sản phẩm

        // Bán hàng
        fillHDHT();
        fillSPBH();
        fillHDCho();
        fillHDGiao();
        fillHDTReo();
        String nam = String.valueOf(java.time.LocalDate.now().getYear());
        String thang = String.valueOf(java.time.LocalDate.now().getMonthValue());
        String ngay = String.valueOf(java.time.LocalDate.now().getDayOfMonth());
        String day = String.valueOf(nam + "-" + thang + "-" + ngay);
        txtNgayTao.setText(day);
        // bán hàng

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
                t.getSlBan(),
                t.getDonGia(),
                t.getTienmat(),
                t.getTienkhac(),
                t.getTiengiamgia(),
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
                t.getGianhap(),
                t.getDonGia(),
                t.getDoanhThu(),
                t.getNamBH()};
            dtm.addRow(rowData);
        }

    }

    public void fillHDHT() {
        listhdbh = HoaDonBanHangRepository.getAllHDHT();
        dtm = (DefaultTableModel) tblHoaDonHoanThanh.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
    }

    public void fillHDCho() {
        listhdbh = HoaDonBanHangRepository.getAllHDCho();
        dtm = (DefaultTableModel) tblHoaDonCho.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
    }

    public void fillHDGiao() {
        listhdbh = HoaDonBanHangRepository.getAllHDGiao();
        dtm = (DefaultTableModel) tblHoaDonGiao.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
    }

    public void fillSPBH() {
        listspbh = SanPhanBanHangRepository.getAllSpBh();
        dtm = (DefaultTableModel) tblSanPhamBanHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listspbh.size(); i++) {
            Object[] data = new Object[]{
                listspbh.get(i).getMa(),
                listspbh.get(i).getTen(),
                listspbh.get(i).getHang(),
                listspbh.get(i).getSize(),
                listspbh.get(i).getMau(),
                listspbh.get(i).getDongia(),
                listspbh.get(i).getSl(),};
            dtm.addRow(data);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    public void clearHDTQ() {
        txtTongTienTQ.setText("");
        txtKhuyenMaiThemTQ.setText("");
        txtTienKhachCanTra.setText("");
        txtTienMatTQ.setText("");
        txtTienCKTQ.setText("");
        txtMaGiaoDichTQ.setText("");
        txtTienDuTQ.setText("");
    }

    public void clearHDGH() {
        txtTenKHGH.setText("");
        txtSDTGH.setText("");
        txtNgaySinhKH.setText("");
        txtDiaChiGH.setText("");
        txtTongTienGH.setText("");
        txtGiamGiaGH.setText("");
        txtPhiShip.setText("");
        txtCanTraGH.setText("");
        txtTienMatGH.setText("");
        txtTienCKGH.setText("");
        txtMaGiaoDichGH.setText("");
        txtTienDuGH.setText("");
    }

    public void setPanelThongKe(int soDOnHang, double doanhThu) {

        lblTongDonHang.setText(String.valueOf(soDOnHang));
        lblDoanhthu.setText(dcf.format(doanhThu));
        lblTongTienDautu.setText(dcf.format(thongKe_Serv.getTienBoRa().getTienDauTu()));
        lblTongTienThuVe.setText(dcf.format(thongKe_Serv.getTienThuVe().getTienThuve()));
        lblTienLai.setText(dcf.format(thongKe_Serv.getTienThuVe().getTienThuve() - thongKe_Serv.getTienBoRa().getTienDauTu()));

    }

    public HoaDonBanHang getFormHoaDonBanHangGiao() {

        String magiaodich = txtMaGiaoDichTQ.getText();
        double tienMat = Double.parseDouble(txtTienMatGH.getText());
        double tienDu = Double.parseDouble(txtTienMatGH.getText());
        double tienMatDB = tienMat - tienDu;
        double tienKhac = Double.parseDouble(txtTienCKGH.getText());
        double phiShip = Double.parseDouble(txtPhiShip.getText());
        String ngaythanhtoan = txtNgayTao.getText();
        String nam = String.valueOf(java.time.LocalDate.now().getYear());
        String thang = String.valueOf(java.time.LocalDate.now().getMonthValue());
        String ngay = String.valueOf(java.time.LocalDate.now().getDayOfMonth() + 1);
        String ngaygiao = String.valueOf(nam + "-" + thang + "-" + ngay);
        String sdt = txtSDTGH.getText();
        String idkh = KhachHangRepository.getIdKHBySDT(sdt);
        double giamGia = Double.parseDouble(txtGiamGiaGH.getText());
        System.out.println(idkh);
        return new HoaDonBanHang(null, null, idkh, null, ngaythanhtoan, ngaythanhtoan, ngaygiao, null, null, magiaodich, 3, tienMatDB, tienKhac, giamGia, phiShip);
    }

    public HoaDonBanHang getFormHoaDonBanHang() {

        String magiaodich = txtMaGiaoDichTQ.getText();
        System.out.println(txtKhuyenMaiThemTQ.getText());
        double giamthem = Double.parseDouble(txtKhuyenMaiThemTQ.getText());
        double tienMat = Double.parseDouble(txtTienMatTQ.getText());
        double tienDu = Double.parseDouble(txtTienDuTQ.getText());
        double tienMatDB = tienMat - tienDu;
        double tienKhac = Double.parseDouble(txtTienCKTQ.getText());
        String nam = String.valueOf(java.time.LocalDate.now().getYear());
        String thang = String.valueOf(java.time.LocalDate.now().getMonthValue());
        String ngay = String.valueOf(java.time.LocalDate.now().getDayOfMonth());
        String ngaythanhtoan = String.valueOf(nam + "-" + thang + "-" + ngay);
        return new HoaDonBanHang(null, null, null, null, null, ngaythanhtoan, null, null, null, magiaodich, 1, tienMatDB, tienKhac, giamthem, 0);
    }

    private void setTable() {
        tblGiay.getTableHeader().setBackground(new Color(122, 181, 236));
        tblGiay.getTableHeader().setOpaque(false);
        tblGioHang.getTableHeader().setBackground(new Color(122, 181, 236));
        tblSanPhamBanHang.getTableHeader().setBackground(new Color(122, 181, 236));
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
        jPopupMenu1 = new javax.swing.JPopupMenu();
        popupTreoHD = new javax.swing.JMenuItem();
        popupHoanThanhHD = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        popupHoanThanhHDTreo = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        lblBanHang = new javax.swing.JLabel();
        lblSanpham = new javax.swing.JLabel();
        pnlTong = new javax.swing.JPanel();
        pnlBanHang = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        btnSua1 = new javax.swing.JButton();
        btnXoa1 = new javax.swing.JButton();
        btnXoaAllGH = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPhamBanHang = new javax.swing.JTable();
        txtTimSP = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        tbbTao = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        scpTao = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        scpGiao = new javax.swing.JScrollPane();
        tblHoaDonGiao = new javax.swing.JTable();
        jPanel35 = new javax.swing.JPanel();
        scpTreo = new javax.swing.JScrollPane();
        tblHoaDonTreo = new javax.swing.JTable();
        scpHoanThanh = new javax.swing.JScrollPane();
        tblHoaDonHoanThanh = new javax.swing.JTable();
        txtNgayTim = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        tbbTTHD = new javax.swing.JTabbedPane();
        pnlTaiQuay = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTongTienTQ = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTienMatTQ = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTienCKTQ = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtTienDuTQ = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        txtKhuyenMaiThemTQ = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtTienKhachCanTra = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtMaGiaoDichTQ = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        pnlGiaoHang = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        txtTienDuGH = new javax.swing.JTextField();
        txtTienMatGH = new javax.swing.JTextField();
        txtTongTienGH = new javax.swing.JTextField();
        txtTienCKGH = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txtPhiShip = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txtCanTraGH = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtGiamGiaGH = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        txtMaGiaoDichGH = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtTenKHGH = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        txtSDTGH = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNgaySinhKH = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        txtDiaChiGH = new javax.swing.JTextField();
        btnLuuKH = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnTaoDon = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
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
        lblDongHo = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtTenNhanVien = new javax.swing.JTextField();

        popupTreoHD.setText("Treo HĐ");
        popupTreoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupTreoHDActionPerformed(evt);
            }
        });
        jPopupMenu1.add(popupTreoHD);

        popupHoanThanhHD.setText("Hoàn Thành");
        popupHoanThanhHD.setToolTipText("");
        popupHoanThanhHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupHoanThanhHDActionPerformed(evt);
            }
        });
        jPopupMenu1.add(popupHoanThanhHD);

        popupHoanThanhHDTreo.setText("Hoàn Thành");
        popupHoanThanhHDTreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupHoanThanhHDTreoActionPerformed(evt);
            }
        });
        jPopupMenu2.add(popupHoanThanhHDTreo);

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Giỏ Hàng"));

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Số Lượng", "Đơn Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHang.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblGioHang.setShowGrid(false);
        tblGioHang.setShowHorizontalLines(true);
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGioHangMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblGioHang);

        btnSua1.setBackground(new java.awt.Color(0, 204, 204));
        btnSua1.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnSua1.setForeground(new java.awt.Color(255, 255, 255));
        btnSua1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Sua123.png"))); // NOI18N
        btnSua1.setText("Sửa SL");
        btnSua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSua1ActionPerformed(evt);
            }
        });

        btnXoa1.setBackground(new java.awt.Color(0, 204, 204));
        btnXoa1.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnXoa1.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cancel-24.png"))); // NOI18N
        btnXoa1.setText("Xóa SP");
        btnXoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa1ActionPerformed(evt);
            }
        });

        btnXoaAllGH.setBackground(new java.awt.Color(0, 204, 204));
        btnXoaAllGH.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnXoaAllGH.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaAllGH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        btnXoaAllGH.setText("Xóa All");
        btnXoaAllGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaAllGHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnXoaAllGH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnXoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXoaAllGH, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnSua1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Sản Phẩm"));

        tblSanPhamBanHang.setModel(new javax.swing.table.DefaultTableModel(
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
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhamBanHang.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblSanPhamBanHang.setShowGrid(false);
        tblSanPhamBanHang.setShowHorizontalLines(true);
        tblSanPhamBanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamBanHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPhamBanHang);

        txtTimSP.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTimSPCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimSP, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(txtTimSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(182, 182, 182))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Hóa Đơn"));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        tbbTao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbbTaoMouseClicked(evt);
            }
        });

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonCho.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonCho.setShowGrid(false);
        tblHoaDonCho.setShowHorizontalLines(true);
        tblHoaDonCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonChoMouseClicked(evt);
            }
        });
        scpTao.setViewportView(tblHoaDonCho);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scpTao, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(scpTao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tbbTao.addTab("HĐ Tạo", jPanel5);

        tblHoaDonGiao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonGiao.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonGiao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonGiaoMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblHoaDonGiaoMouseReleased(evt);
            }
        });
        scpGiao.setViewportView(tblHoaDonGiao);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scpGiao, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(scpGiao, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tbbTao.addTab("HĐ Đang Giao", jPanel6);

        tblHoaDonTreo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng", "Lí do"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonTreo.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonTreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonTreoMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblHoaDonTreoMouseReleased(evt);
            }
        });
        scpTreo.setViewportView(tblHoaDonTreo);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scpTreo, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel35Layout.createSequentialGroup()
                    .addComponent(scpTreo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tbbTao.addTab("HĐ Treo", jPanel35);

        tblHoaDonHoanThanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã HĐ", "Tên Nhân Viên", "Tên Khách Hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonHoanThanh.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonHoanThanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonHoanThanhMouseClicked(evt);
            }
        });
        scpHoanThanh.setViewportView(tblHoaDonHoanThanh);

        tbbTao.addTab("HĐ Đã Hoàn Thành", scpHoanThanh);

        txtNgayTim.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNgayTimCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNgayTim, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbbTao, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNgayTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbbTao, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 204), 2));

        pnlTaiQuay.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Tổng tiền ");

        txtTongTienTQ.setEditable(false);
        txtTongTienTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtTongTienTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTongTienTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTongTienTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTongTienTQ.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("Tiền mặt");

        txtTienMatTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtTienMatTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienMatTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienMatTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienMatTQ.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtTienMatTQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienMatTQActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setText("Tiền CK");

        txtTienCKTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtTienCKTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienCKTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienCKTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienCKTQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienCKTQActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel63.setText("Tiền dư");

        txtTienDuTQ.setEditable(false);
        txtTienDuTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtTienDuTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienDuTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienDuTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienDuTQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienDuTQActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel64.setText("Giảm giá ");

        txtKhuyenMaiThemTQ.setEditable(false);
        txtKhuyenMaiThemTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtKhuyenMaiThemTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtKhuyenMaiThemTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKhuyenMaiThemTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtKhuyenMaiThemTQ.setSelectedTextColor(new java.awt.Color(242, 242, 242));
        txtKhuyenMaiThemTQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKhuyenMaiThemTQActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel65.setText("Khách cần trả");

        txtTienKhachCanTra.setEditable(false);
        txtTienKhachCanTra.setBackground(new java.awt.Color(238, 238, 238));
        txtTienKhachCanTra.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienKhachCanTra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienKhachCanTra.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienKhachCanTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKhachCanTraActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel66.setText("Mã giao dịch");

        txtMaGiaoDichTQ.setBackground(new java.awt.Color(238, 238, 238));
        txtMaGiaoDichTQ.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtMaGiaoDichTQ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaGiaoDichTQ.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jLabel67.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel67.setText("VNĐ");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel3.setText("VNĐ");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel4.setText("VNĐ");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel5.setText("VNĐ");

        jLabel68.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel68.setText("VNĐ");

        jLabel69.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel69.setText("%");

        jLabel93.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel93.setText("VNĐ");

        javax.swing.GroupLayout pnlTaiQuayLayout = new javax.swing.GroupLayout(pnlTaiQuay);
        pnlTaiQuay.setLayout(pnlTaiQuayLayout);
        pnlTaiQuayLayout.setHorizontalGroup(
            pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTaiQuayLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlTaiQuayLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTongTienTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTaiQuayLayout.createSequentialGroup()
                        .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65)
                            .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtMaGiaoDichTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTienCKTQ, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTienKhachCanTra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTaiQuayLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(txtKhuyenMaiThemTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTienMatTQ, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtTienDuTQ, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTaiQuayLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlTaiQuayLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        pnlTaiQuayLayout.setVerticalGroup(
            pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTaiQuayLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTongTienTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67))
                .addGap(18, 18, 18)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(txtKhuyenMaiThemTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69))
                .addGap(27, 27, 27)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(txtTienKhachCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTienMatTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(25, 25, 25)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTienCKTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(27, 27, 27)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaGiaoDichTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66)
                    .addComponent(jLabel68))
                .addGap(32, 32, 32)
                .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel63)
                    .addGroup(pnlTaiQuayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTienDuTQ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel93)))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        tbbTTHD.addTab("Tại quầy", pnlTaiQuay);

        pnlGiaoHang.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setText("Tổng");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setText("Tiền mặt");

        jLabel70.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel70.setText("Tiền dư");

        txtTienDuGH.setEditable(false);
        txtTienDuGH.setBackground(new java.awt.Color(238, 238, 238));
        txtTienDuGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienDuGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienDuGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtTienMatGH.setBackground(new java.awt.Color(238, 238, 238));
        txtTienMatGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienMatGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienMatGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienMatGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienMatGHActionPerformed(evt);
            }
        });

        txtTongTienGH.setEditable(false);
        txtTongTienGH.setBackground(new java.awt.Color(238, 238, 238));
        txtTongTienGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTongTienGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTongTienGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtTienCKGH.setBackground(new java.awt.Color(238, 238, 238));
        txtTienCKGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTienCKGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienCKGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtTienCKGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienCKGHActionPerformed(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel71.setText("Tiền CK");

        jLabel72.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel72.setText("Phí ship");

        txtPhiShip.setBackground(new java.awt.Color(238, 238, 238));
        txtPhiShip.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtPhiShip.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPhiShip.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtPhiShip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhiShipActionPerformed(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel73.setText("Khách cần trả");

        txtCanTraGH.setEditable(false);
        txtCanTraGH.setBackground(new java.awt.Color(238, 238, 238));
        txtCanTraGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtCanTraGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCanTraGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setText("Giảm giá thêm");

        txtGiamGiaGH.setBackground(new java.awt.Color(238, 238, 238));
        txtGiamGiaGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtGiamGiaGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiamGiaGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtGiamGiaGH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiamGiaGHActionPerformed(evt);
            }
        });

        jLabel74.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel74.setText("Mã giao dịch");

        txtMaGiaoDichGH.setBackground(new java.awt.Color(238, 238, 238));
        txtMaGiaoDichGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtMaGiaoDichGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaGiaoDichGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin khách hàng"));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setText("Tên KH");

        txtTenKHGH.setBackground(new java.awt.Color(238, 238, 238));
        txtTenKHGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtTenKHGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTenKHGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jLabel75.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel75.setText("SĐT");

        txtSDTGH.setBackground(new java.awt.Color(238, 238, 238));
        txtSDTGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtSDTGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSDTGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Ngày sinh");

        txtNgaySinhKH.setBackground(new java.awt.Color(238, 238, 238));
        txtNgaySinhKH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtNgaySinhKH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNgaySinhKH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));
        txtNgaySinhKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgaySinhKHActionPerformed(evt);
            }
        });

        jLabel85.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel85.setText("Địa chỉ");

        txtDiaChiGH.setBackground(new java.awt.Color(238, 238, 238));
        txtDiaChiGH.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        txtDiaChiGH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiaChiGH.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        btnLuuKH.setBackground(new java.awt.Color(255, 0, 51));
        btnLuuKH.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnLuuKH.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuKH.setText("Lưu KH");
        btnLuuKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLuuKH, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSDTGH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNgaySinhKH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenKHGH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiaChiGH, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtTenKHGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel75)
                            .addComponent(txtSDTGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtNgaySinhKH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(btnLuuKH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(txtDiaChiGH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel86.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel86.setText("VNĐ");

        jLabel87.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel87.setText("VNĐ");

        jLabel88.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel88.setText("VNĐ");

        jLabel89.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel89.setText("VNĐ");

        jLabel90.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel90.setText("VNĐ");

        jLabel91.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel91.setText("VNĐ");

        jLabel92.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel92.setText("%");

        javax.swing.GroupLayout pnlGiaoHangLayout = new javax.swing.GroupLayout(pnlGiaoHang);
        pnlGiaoHang.setLayout(pnlGiaoHangLayout);
        pnlGiaoHangLayout.setHorizontalGroup(
            pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel73))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGiamGiaGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTongTienGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                                        .addGap(52, 52, 52)
                                        .addComponent(txtTienDuGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtTienMatGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPhiShip, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCanTraGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienCKGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaGiaoDichGH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(35, 35, 35)
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlGiaoHangLayout.setVerticalGroup(
            pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTongTienGH, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86))
                .addGap(18, 18, 18)
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtGiamGiaGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel92)))
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel87)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 16, Short.MAX_VALUE)
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel89)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel90)
                        .addGap(1, 1, 1)
                        .addComponent(txtTienCKGH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(txtPhiShip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel73)
                            .addComponent(txtCanTraGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTienMatGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel71)))
                .addGap(18, 18, 18)
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGiaoHangLayout.createSequentialGroup()
                        .addComponent(jLabel91)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGiaoHangLayout.createSequentialGroup()
                        .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel74)
                            .addComponent(txtMaGiaoDichGH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)))
                .addGroup(pnlGiaoHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(txtTienDuGH, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        tbbTTHD.addTab("Giao hàng", pnlGiaoHang);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel6.setText("Mã HĐ");

        txtMaHD.setEditable(false);
        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });

        txtNgayTao.setEditable(false);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel8.setText("Ngày tạo");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(txtMaHD))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnTaoDon.setBackground(new java.awt.Color(255, 0, 51));
        btnTaoDon.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnTaoDon.setForeground(new java.awt.Color(255, 255, 255));
        btnTaoDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add (1).png"))); // NOI18N
        btnTaoDon.setText("Tạo đơn");
        btnTaoDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoDonActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(0, 204, 0));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-dolar-20.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnHuy.setBackground(new java.awt.Color(255, 0, 51));
        btnHuy.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_kh.png"))); // NOI18N
        btnHuy.setText("Hủy đơn");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 204, 0));
        jButton9.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh (1).png"))); // NOI18N
        jButton9.setText("Xóa Form");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9))
                    .addComponent(tbbTTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTaoDon)))
                .addGap(52, 52, 52))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnTaoDon, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbbTTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 145, Short.MAX_VALUE))
        );

        jPanel23.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlBanHangLayout = new javax.swing.GroupLayout(pnlBanHang);
        pnlBanHang.setLayout(pnlBanHangLayout);
        pnlBanHangLayout.setHorizontalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1156, Short.MAX_VALUE)
            .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBanHangLayout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlBanHangLayout.setVerticalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 714, Short.MAX_VALUE)
            .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBanHangLayout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlTong.add(pnlBanHang, "card2");

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        txtMaGiay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

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
        txtTenGiay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtNamBaoHanh.setBackground(new java.awt.Color(238, 238, 238));
        txtNamBaoHanh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNamBaoHanh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtSoLuong.setBackground(new java.awt.Color(238, 238, 238));
        txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSoLuong.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtGiaNhap.setBackground(new java.awt.Color(238, 238, 238));
        txtGiaNhap.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiaNhap.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        txtGiaBan.setBackground(new java.awt.Color(238, 238, 238));
        txtGiaBan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGiaBan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        jLabel51.setText("Ảnh");

        txtMaBarCode.setBackground(new java.awt.Color(238, 238, 238));
        txtMaBarCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaBarCode.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaBarCode, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(txtTenGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNamBaoHanh, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbHang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                        .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        txtTimKiem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 0)));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1111, Short.MAX_VALUE)
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
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnThem.setBackground(new java.awt.Color(0, 204, 204));
        btnThem.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.setBorder(null);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(0, 204, 204));
        btnSua.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("Sửa");
        btnSua.setBorder(null);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 204, 204));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.setBorder(null);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setBackground(new java.awt.Color(0, 204, 204));
        btnMoi.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnMoi.setText("Mới");
        btnMoi.setBorder(null);
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnHienThi.setBackground(new java.awt.Color(0, 204, 204));
        btnHienThi.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnHienThi.setForeground(new java.awt.Color(255, 255, 255));
        btnHienThi.setText("Hiển thị");
        btnHienThi.setBorder(null);
        btnHienThi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiActionPerformed(evt);
            }
        });

        btnThemExcel.setBackground(new java.awt.Color(0, 204, 204));
        btnThemExcel.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        btnThemExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnThemExcel.setText("Thêm excel");
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
                        .addGap(47, 47, 47)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101)
                        .addComponent(btnThemExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101)
                        .addComponent(btnHienThi, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHienThi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(148, 148, 148))
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
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
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
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã giày", "Tên giày", "Đơn giá", "Số lượng bán", "Tiền mặt", "Tiền chuyển khoản", "Giảm giá thêm(%)", "Doanh thu", "Ngày bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKe.setFocusable(false);
        tblThongKe.setRowHeight(25);
        tblThongKe.setSelectionBackground(new java.awt.Color(255, 204, 204));
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
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
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
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã giày", "Tên giày", "Màu sắc", "Size", "Chất liệu", "Số lượng tồn", "Số lượng bán", "Giá nhập", "Giá bán", "Doanh thu", "Năm bảo hành"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, false, true, true, false
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
                .addComponent(jScrollPane13)
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
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel60)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 353, Short.MAX_VALUE)
                                .addComponent(btnMailSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuatFile1)
                                .addGap(32, 32, 32))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
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
            .addGap(0, 637, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(0, 19, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 19, Short.MAX_VALUE)))
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
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(rdoLam)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNghi)
                                .addGap(19, 19, 19))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel31Layout.createSequentialGroup()
                                        .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtMatKhau))))))
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
                .addContainerGap(49, Short.MAX_VALUE))
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

        lblDongHo.setText("ThoiGianODayNhe");
        getContentPane().add(lblDongHo, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 210, -1));

        jLabel35.setText("Xin Chào:");
        getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 11, 80, -1));

        txtTenNhanVien.setEditable(false);
        getContentPane().add(txtTenNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(377, 8, 178, -1));

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

    private void lblThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThoatMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_Thoat.png/"));
        jLabel1.setIcon(img);
        System.exit(0);
    }//GEN-LAST:event_lblThoatMouseClicked

    private void lblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseClicked
        ImageIcon img = new ImageIcon(getClass().getResource("/img/Hover_ThongKe.png/"));
        jLabel1.setIcon(img);
        setPanelThongKe(thongKe_Serv.getDoanhThuSoLuong().getSoDonHang(), thongKe_Serv.getDoanhThuSoLuong().getDoanhthu());
        setPanelSanPham();
        listTKSanPham = thongKe_Serv.getAll();
        loadTableSanPham(listTKSanPham);

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
        if (ch.getTrangThai() == 1) {
            rdoLam.setSelected(true);
        } else {
            rdoNghi.setSelected(true);
        }
        txtMatKhau.setText(ch.getMatKhau());
    }//GEN-LAST:event_tblNhanvientblNhanvienMouseClicked

    private void btnLoadNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadNVActionPerformed
        fillTableNV();
    }//GEN-LAST:event_btnLoadNVActionPerformed

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
            pnlSoLuong.setVisible(false);
            listTKSanPham = thongKe_Serv.getAll();
            loadTableSanPham(listTKSanPham);
        } else if (cbbThongKeSanPham.getSelectedIndex() == 1) {
            pnlSoLuong.setVisible(false);
            listTKSanPham = thongKe_Serv.getTop5SoLuongBan();
            loadTableSanPham(listTKSanPham);
        } else if (cbbThongKeSanPham.getSelectedIndex() == 2) {
            pnlSoLuong.setVisible(false);
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
    public void getFormHDCT() {
        int check = 0;
        int slsp = Integer.parseInt(JOptionPane.showInputDialog("Số lượng sản phẩm muốn mua"));
        int rowhd = tblHoaDonCho.getSelectedRow();
        int rowsp = tblSanPhamBanHang.getSelectedRow();
        String mahd = tblHoaDonCho.getValueAt(rowhd, 0).toString();
        String masp = tblSanPhamBanHang.getValueAt(rowsp, 0).toString();
        String tensp = tblSanPhamBanHang.getValueAt(rowsp, 1).toString();
        int slcon = SanPhanBanHangRepository.getSLGiay(masp);
        int slthuc = slcon - slsp;
        SanPhanBanHangRepository.updateSLSP(slthuc, masp);
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(mahd);
        String idsp = SanPhanBanHangRepository.getIDSpByMaSP(masp);
        double dongia = Double.parseDouble(tblSanPhamBanHang.getValueAt(rowsp, 5).toString());

        for (int i = 0; i < listgh.size(); i++) {
            if (tensp.equals(listgh.get(i).getTen())) {
                HoaDonCTRepository.updateHDCT(listgh.get(i).getSl() + slsp, idhd, idsp);
                check = 1;
            }
        }
        fillSPBH();
        HoaDonCT hdct = new HoaDonCT(idhd, idsp, slsp, dongia);
        if (check == 0) {
            HoaDonCTRepository.addHDCT(hdct);
            txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
        }
        txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
        txtTongTienGH.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));

    }

    private void jPanel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel12MouseClicked

    }//GEN-LAST:event_jPanel12MouseClicked

    private void tblGioHangMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGioHangMouseReleased
    public void fillHDCT() {
        String mahd = txtMaHD.getText();
        listgh = HoaDonBanHangRepository.getHDCTByMaHD(mahd);
        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listgh.size(); i++) {
            Object[] data = new Object[]{
                listgh.get(i).getMasp(),
                listgh.get(i).getTen(),
                listgh.get(i).getSl(),
                listgh.get(i).getDonGia(),};
            dtm.addRow(data);
        }
    }
    private void btnSua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSua1ActionPerformed
        // TODO add your handling code here:
        int slsp = Integer.parseInt(JOptionPane.showInputDialog("Số lượng sản phẩm sửa"));
        int rowhdct = tblGioHang.getSelectedRow();
        int rowhd = tblHoaDonCho.getSelectedRow();
        String masp = tblGioHang.getValueAt(rowhdct, 0).toString();
        int slht = Integer.parseInt(tblGioHang.getValueAt(rowhdct, 2).toString());
        String idctg = SanPhanBanHangRepository.getIDSpByMaSP(masp);
        String mahd = tblHoaDonCho.getValueAt(rowhd, 0).toString();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(mahd);

        if (slsp < slht) {
            int slt = SanPhanBanHangRepository.getSLGiay(masp);
            int slcn = slt + (slht - slsp);
            SanPhanBanHangRepository.updateSLSP(slcn, masp);
            fillSPBH();
        } else if (slsp > slht) {
            int slt = SanPhanBanHangRepository.getSLGiay(masp);
            int slcn = slt - (slsp - slht);
            SanPhanBanHangRepository.updateSLSP(slcn, masp);
            fillSPBH();
        }
        HoaDonCTRepository.updateSLHDCT(slsp, idctg, idhd);
        txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));

        fillHDCT();
    }//GEN-LAST:event_btnSua1ActionPerformed

    private void btnXoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa1ActionPerformed
        // TODO add your handling code here:
        int rowhdct = tblGioHang.getSelectedRow();
        int rowhd = tblHoaDonCho.getSelectedRow();
        String masp = tblGioHang.getValueAt(rowhdct, 0).toString();
        String idctg = SanPhanBanHangRepository.getIDSpByMaSP(masp);
        String mahd = tblHoaDonCho.getValueAt(rowhd, 0).toString();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(mahd);
        int slxoa = Integer.parseInt(tblGioHang.getValueAt(rowhdct, 2).toString());
        HoaDonCTRepository.deleteDHCT(idctg, idhd);
        int slt = SanPhanBanHangRepository.getSLGiay(masp);
        int slcn = slt + slxoa;
        SanPhanBanHangRepository.updateSLSP(slcn, masp);
        txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
        fillHDCT();
        fillSPBH();
    }//GEN-LAST:event_btnXoa1ActionPerformed

    private void btnXoaAllGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaAllGHActionPerformed
        // TODO add your handling code here:
        int rowhdct = tblGioHang.getRowCount();
        int rowsp = tblSanPhamBanHang.getRowCount();
        int rowhd = tblHoaDonCho.getSelectedRow();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(tblHoaDonCho.getValueAt(rowhd, 0).toString());
        HoaDonCTRepository.deleteAllGH(idhd);
        for (int i = 0; i < rowhdct; i++) {
            for (int j = 0; j < rowsp; j++) {
                String masp = tblGioHang.getValueAt(i, 0).toString();
                int slxoa = Integer.parseInt(tblGioHang.getValueAt(i, 2).toString());
                if (tblGioHang.getValueAt(i, 0).toString().equals(tblSanPhamBanHang.getValueAt(j, 0).toString())) {
                    int slt = SanPhanBanHangRepository.getSLGiay(masp);
                    int slcn = slt + slxoa;
                    SanPhanBanHangRepository.updateSLSP(slcn, masp);
                    fillSPBH();
                }
            }

        }
        String mahd = tblHoaDonCho.getValueAt(rowhd, 0).toString();
        txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
        fillHDCT();
    }//GEN-LAST:event_btnXoaAllGHActionPerformed

    private void tblSanPhamBanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamBanHangMouseClicked
        // TODO add your handling code here:
        getFormHDCT();
        fillHDCT();

        //        txtTongTien.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
    }//GEN-LAST:event_tblSanPhamBanHangMouseClicked

    private void tblHoaDonChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMouseClicked

        listhdbh = HoaDonBanHangRepository.getAllHDCho();
        int row = tblHoaDonCho.getSelectedRow();
        ThongTinHoaDonBanHang hd = listhdbh.get(row);
        String mahd = txtMaHD.getText();
        txtMaHD.setText(hd.getMa());
        txtNgayTao.setText(hd.getNgaytao());
        txtKhuyenMaiThemTQ.setText(String.valueOf(hd.getGiamgiathem()));
        if (hd.getHinhthuc().equals("Tại quầy")) {
            clearHDGH();
            tbbTTHD.setSelectedIndex(0);
            txtTongTienTQ.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
            txtKhuyenMaiThemTQ.setText(String.valueOf(hd.getGiamgiathem()));

        } else if (hd.getHinhthuc().equals("Giao hàng")) {
            clearHDTQ();
            tbbTTHD.setSelectedIndex(1);
            txtTongTienGH.setText(String.valueOf(HoaDonCTRepository.getSumTT(mahd)));
            txtGiamGiaGH.setText(String.valueOf(hd.getGiamgiathem()));

        }
        //        txtKhuyenMaiThemTQ.setText(String.valueOf(0.1));
        //        double canTra = Double.valueOf(txtTongTienTQ.getText()) * (giamGia / 100);
        //        txtTenKHGH.setText(hd.getTenkh());
        //        txtTongTien.setText(String.valueOf(hd.getThanhtien()));
        //        txtTienMatTQ.setText(String.valueOf(hd.getTienmat()));
        //        txtTienCKTQ.setText(String.valueOf(hd.getTienkhac()));
        //        int rowhdct = tblGioHang.getRowCount();
        //        for(int i =0 ; i < rowhdct; i++){
        //
        //        }

        fillHDCT();
    }//GEN-LAST:event_tblHoaDonChoMouseClicked

    private void tblHoaDonGiaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonGiaoMouseClicked
        // TODO add your handling code here:
        clearHDTQ();
        tbbTTHD.setSelectedIndex(1);
        listhdbh = HoaDonBanHangRepository.getAllHDGiao();
        int row = tblHoaDonGiao.getSelectedRow();
        ThongTinHoaDonBanHang hd = listhdbh.get(row);
        txtMaHD.setText(hd.getMa());
        txtDiaChiGH.setText(hd.getDiachi());
        txtTenKHGH.setText(hd.getTenkh());
        txtSDTGH.setText(hd.getSdt());
        txtNgaySinhKH.setText(hd.getNgaysinh());
        txtDiaChiGH.setText(hd.getDiachi());
        txtTongTienGH.setText(String.valueOf(hd.getThanhtien()));
        txtGiamGiaGH.setText(String.valueOf(hd.getGiamgiathem()));
        txtPhiShip.setText(String.valueOf(hd.getPhiship()));
        txtTienMatGH.setText(String.valueOf(hd.getTienmat()));
        txtTienCKGH.setText(String.valueOf(hd.getTienkhac()));
        txtNgayTao.setText(hd.getNgaytao());
        txtMaGiaoDichGH.setText(hd.getMagiaodich());
        String mahd = txtMaHD.getText();
        listgh = HoaDonBanHangRepository.getHDCTByMaHD(mahd);
        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listgh.size(); i++) {
            Object[] data = new Object[]{
                listgh.get(i).getMasp(),
                listgh.get(i).getTen(),
                listgh.get(i).getSl(),
                listgh.get(i).getDonGia(),};
            dtm.addRow(data);
        }
    }//GEN-LAST:event_tblHoaDonGiaoMouseClicked

    private void tblHoaDonHoanThanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonHoanThanhMouseClicked
        // TODO add your handling code here:
        listhdbh = HoaDonBanHangRepository.getAllHDHT();
        int row = tblHoaDonHoanThanh.getSelectedRow();
        ThongTinHoaDonBanHang hd = listhdbh.get(row);
        txtMaHD.setText(hd.getMa());
        txtTenKHGH.setText(hd.getTenkh());
        if (hd.getHinhthuc().equals("Tại quầy")) {
            clearHDGH();
            tbbTTHD.setSelectedIndex(0);
            txtTienMatTQ.setText(String.valueOf(hd.getTienmat()));
            txtTienCKTQ.setText(String.valueOf(hd.getTienkhac()));
            txtTongTienTQ.setText(String.valueOf(hd.getThanhtien()));
            txtKhuyenMaiThemTQ.setText(String.valueOf(hd.getGiamgiathem()));
            txtNgayTao.setText(hd.getNgaytao());
            txtMaGiaoDichTQ.setText(hd.getMagiaodich());
        } else if (hd.getHinhthuc().equals("Giao hàng")) {
            clearHDTQ();
            tbbTTHD.setSelectedIndex(1);
            txtTenKHGH.setText(hd.getTenkh());
            txtSDTGH.setText(hd.getSdt());
            txtNgaySinhKH.setText(hd.getNgaysinh());
            txtDiaChiGH.setText(hd.getDiachi());
            txtTongTienGH.setText(String.valueOf(hd.getThanhtien()));
            txtGiamGiaGH.setText(String.valueOf(hd.getGiamgiathem()));
            txtPhiShip.setText(String.valueOf(hd.getPhiship()));
            txtTienMatGH.setText(String.valueOf(hd.getTienmat()));
            txtTienCKGH.setText(String.valueOf(hd.getTienkhac()));
            txtNgayTao.setText(hd.getNgaytao());
            txtMaGiaoDichGH.setText(hd.getMagiaodich());
        }
        //        if (Double.parseDouble(txtTongTien.getText()) > (Double.parseDouble(txtTienMat.getText()) + Double.parseDouble(txtTienCK.getText()))) {
        //            txtTienDu.setText(String.valueOf(Double.parseDouble(txtTongTien.getText()) - Double.parseDouble(txtTienMat.getText()) - Double.parseDouble(txtTienCK.getText())));
        //        }
        String mahd = txtMaHD.getText();
        listgh = HoaDonBanHangRepository.getHDCTByMaHD(mahd);
        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listgh.size(); i++) {
            Object[] data = new Object[]{
                listgh.get(i).getMasp(),
                listgh.get(i).getTen(),
                listgh.get(i).getSl(),
                listgh.get(i).getDonGia(),};
            dtm.addRow(data);
        }
    }//GEN-LAST:event_tblHoaDonHoanThanhMouseClicked

    private void tbbTaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbbTaoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbbTaoMouseClicked

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:

//        if (scpTao.isVisible() == true) {
//            int row = tblHoaDonCho.getSelectedRow();
//            String mahdt = tblHoaDonCho.getValueAt(row, 0).toString();
//            String idhdt = HoaDonBanHangRepository.getIDHDByMaHD(mahdt);
//
//            System.out.println(mahdt);
//            HoaDonBanHangRepository.deleteHD(idhdt);
// JOptionPane.showMessageDialog(this, "Hủy hóa đơn Giao Hàng");
//        }
        if (scpGiao.isVisible() == true) {
            int rowhd = tblHoaDonGiao.getSelectedRow();
            String idhd = HoaDonBanHangRepository.getIDHDByMaHD(tblHoaDonGiao.getValueAt(rowhd, 0).toString());

            if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Tại quầy")) {
                HoaDonBanHang hdbh = getFormHoaDonBanHang();
                HoaDonBanHangRepository.updateTTHD(hdbh, idhd);
            } else if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Giao hàng")) {
                HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
                HoaDonBanHangRepository.updateTTHD(hdgiao, idhd);
            }

            String text = JOptionPane.showInputDialog("Lí do hủy đơn", "Đơn Hủy");

            HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
            HoaDonBanHangRepository.updateTTHuy(hdgiao, text, idhd);
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được hủy");
            fillHDTReo();
            fillHDGiao();
            fillHDCT();
        }

        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);

        fillHDCho();
        fillHDHT();
        fillHDGiao();
        clearHDGH();
        clearHDTQ();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void txtTienMatTQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienMatTQActionPerformed
        // TODO add your handling code here:
        if (txtTienCKTQ.getText().trim().length() == 0) {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienMatTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));

        } else if (txtTienMatTQ.getText().trim().length() == 0) {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienCKTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));
        } else {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienMatTQ.getText()) + Double.parseDouble(txtTienCKTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));
        }

    }//GEN-LAST:event_txtTienMatTQActionPerformed

    private void txtTienCKTQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienCKTQActionPerformed
        // TODO add your handling code here:
        if (txtTienMatTQ.getText().trim().length() == 0) {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienCKTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));

        } else if (txtTienCKTQ.getText().trim().length() == 0) {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienMatTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));
        } else {
            txtTienDuTQ.setText(String.valueOf(Double.parseDouble(txtTienMatTQ.getText()) + Double.parseDouble(txtTienCKTQ.getText())
                    - Double.parseDouble(txtTienKhachCanTra.getText())));
        }
    }//GEN-LAST:event_txtTienCKTQActionPerformed

    private void txtTienDuTQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienDuTQActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienDuTQActionPerformed

    private void txtKhuyenMaiThemTQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKhuyenMaiThemTQActionPerformed
        // TODO add your handling code here:
        //        double giamGia = Double.valueOf(txtKhuyenMaiThemTQ.getText());
        txtTienKhachCanTra.setText(String.valueOf(Double.valueOf(txtTongTienTQ.getText())
                - (Double.valueOf(txtTongTienTQ.getText()) * (Double.valueOf(txtKhuyenMaiThemTQ.getText()) / 100))));
    }//GEN-LAST:event_txtKhuyenMaiThemTQActionPerformed

    private void txtTienKhachCanTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachCanTraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienKhachCanTraActionPerformed

    private void txtTienMatGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienMatGHActionPerformed
        // TODO add your handling code here:
        if (txtTienCKGH.getText().trim().length() == 0) {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienMatGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));

        } else if (txtTienMatGH.getText().trim().length() == 0) {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienCKGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));
        } else {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienMatGH.getText()) + Double.parseDouble(txtTienCKGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));
        }
    }//GEN-LAST:event_txtTienMatGHActionPerformed

    private void txtTienCKGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienCKGHActionPerformed
        // TODO add your handling code here:
        if (txtTienMatGH.getText().trim().length() == 0) {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienCKGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));

        } else if (txtTienCKGH.getText().trim().length() == 0) {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienMatGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));
        } else {
            txtTienDuGH.setText(String.valueOf(Double.parseDouble(txtTienMatGH.getText()) + Double.parseDouble(txtTienCKGH.getText())
                    - Double.parseDouble(txtCanTraGH.getText())));
        }
    }//GEN-LAST:event_txtTienCKGHActionPerformed

    private void txtPhiShipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhiShipActionPerformed
        // TODO add your handling code here:

        if (txtPhiShip.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    - (Double.parseDouble(txtTongTienGH.getText()) * Double.parseDouble(txtGiamGiaGH.getText()) / 100)));
        } else if (txtGiamGiaGH.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    + Double.parseDouble(txtPhiShip.getText())));
        } else if (txtPhiShip.getText().trim().length() == 0 && txtGiamGiaGH.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())));
        } else {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    - (Double.parseDouble(txtTongTienGH.getText()) * Double.parseDouble(txtGiamGiaGH.getText()) / 100)
                    + Double.parseDouble(txtPhiShip.getText())));
        }
    }//GEN-LAST:event_txtPhiShipActionPerformed

    private void txtGiamGiaGHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiamGiaGHActionPerformed
        // TODO add your handling code here:
        if (txtPhiShip.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    - (Double.parseDouble(txtTongTienGH.getText()) * Double.parseDouble(txtGiamGiaGH.getText()) / 100)));
        } else if (txtGiamGiaGH.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    + Double.parseDouble(txtPhiShip.getText())));
        } else if (txtPhiShip.getText().trim().length() == 0 && txtGiamGiaGH.getText().trim().length() == 0) {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())));
        } else {
            txtCanTraGH.setText(String.valueOf(Double.parseDouble(txtTongTienGH.getText())
                    - (Double.parseDouble(txtTongTienGH.getText()) * Double.parseDouble(txtGiamGiaGH.getText()) / 100)
                    + Double.parseDouble(txtPhiShip.getText())));
        }
    }//GEN-LAST:event_txtGiamGiaGHActionPerformed

    private void txtNgaySinhKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgaySinhKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgaySinhKHActionPerformed
    public KhachHang getFormKhacHang() {
        int slkh = KhachHangRepository.DemKH();
        String ma = "KH" + ++slkh;
        String hoten = txtTenKHGH.getText();
        String ns = txtNgaySinhKH.getText();
        String sdt = txtSDTGH.getText();
        String diachi = txtDiaChiGH.getText();
        return new KhachHang(null, ma, hoten, ns, sdt, diachi);
    }
    private void btnLuuKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuKHActionPerformed
        // TODO add your handling code here:
        KhachHang kh = getFormKhacHang();
        KhachHangRepository.addKh(kh);
        JOptionPane.showMessageDialog(this, "Lưu thông tin khách hàng thành công");
    }//GEN-LAST:event_btnLuuKHActionPerformed
    public HoaDonBanHang getFromHDBH() {
        int sl = HoaDonBanHangRepository.getSoHD();
        String tennv = txtTenNhanVien.getText();
        String ma = "HD" + ++sl;
        String ngaytao = txtNgayTao.getText();
        String idnv = HoaDonBanHangRepository.getIDNVByHoTen(tennv);
        String gioht = String.valueOf(java.time.LocalTime.now());
        String time = gioht.substring(0, 8);

        String tt = "";
        if (pnlTaiQuay.isVisible() == true) {
            tt = "Tại quầy";
        } else {
            tt = "Giao hàng";
        }

        return new HoaDonBanHang(ma, idnv, null, tt, ngaytao, null, null, null, time, null, 2, 0.0, 0.0, 0.0, 0.0);
    }
    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDActionPerformed

    private void btnTaoDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoDonActionPerformed
        // TODO add your handling code here:
        HoaDonBanHang dh = getFromHDBH();
        HoaDonBanHangRepository.add(dh);
        fillHDCho();
        tblHoaDonCho.setRowSelectionInterval(0, 0);
    }//GEN-LAST:event_btnTaoDonActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        clearHDGH();
        clearHDTQ();
    }//GEN-LAST:event_jButton9ActionPerformed

    // Hóa Đơn in 
    void fillHoaDonIn() {
        HoaDonBanHangRepository _BanHangRepository = new HoaDonBanHangRepository();
        DefaultTableModel mol = (DefaultTableModel) HoaDonInView.tblHoaDonIn.getModel();
        mol.setRowCount(0);
        for (var x : _DonInService.getAll(_BanHangRepository.getIDHDByMaHD(txtMaHD.getText()))) {
            mol.addRow(new Object[]{
                x.getTenSanPham(),
                x.getSoLuong(),
                x.getDonGia(),
                x.getDonGia() * x.getSoLuong()
            });
        }
    }

    public void hoaDonInTQ() {
        HoaDonBanHangRepository _BanHangRepository = new HoaDonBanHangRepository();
        HoaDonInView HDV = new HoaDonInView();
        HDV.lblTongCong.setText(txtTongTienTQ.getText());
        System.out.println(txtTienMatTQ.getText());
        System.out.println(txtTienCKTQ.getText());
        HDV.lblTienKhachTRa.setText(
                Integer.parseInt(txtTienMatTQ.getText())
                + Integer.parseInt(txtTienCKTQ.getText()) + "");
        double tienKhuyenMai = 0;
        tienKhuyenMai = Double.parseDouble(txtTongTienTQ.getText()) - Double.parseDouble(txtTienKhachCanTra.getText());
        HDV.lblKhuyenMai.setText("- " + tienKhuyenMai + "");

        Double tienPhaiTra = Double.parseDouble(HDV.lblTongCong.getText()) - tienKhuyenMai;
        HDV.lblTongTienPhaiTra.setText(tienPhaiTra + "");
        Double tienKhachTra = Double.parseDouble(HDV.lblTienKhachTRa.getText()) - Double.parseDouble(HDV.lblTongTienPhaiTra.getText());
        HDV.lblTienThua.setText(tienKhachTra + "");
        HDV.lblMaHD.setText(txtMaHD.getText());
        fillHoaDonIn();
        HDV.setVisible(true);
    }

    public void hoaDonInGH() {
        HoaDonBanHangRepository _BanHangRepository = new HoaDonBanHangRepository();
        HoaDonInView HDV = new HoaDonInView();
        HDV.lblTongCong.setText(txtTongTienGH.getText());
        System.out.println(txtTienMatGH.getText());
        System.out.println(txtTienCKGH.getText());
        HDV.lblTienKhachTRa.setText(
                Integer.parseInt(txtTienMatGH.getText())
                + Integer.parseInt(txtTienCKGH.getText()) + "");
        double tienKhuyenMai = 0;
        tienKhuyenMai = Double.parseDouble(txtTongTienGH.getText()) - Double.parseDouble(txtCanTraGH.getText());
        HDV.lblKhuyenMai.setText("- " + tienKhuyenMai + "");

        Double tienPhaiTra = Double.parseDouble(HDV.lblTongCong.getText()) - tienKhuyenMai;
        HDV.lblTongTienPhaiTra.setText(tienPhaiTra + "");
        Double tienKhachTra = Double.parseDouble(HDV.lblTienKhachTRa.getText()) - Double.parseDouble(HDV.lblTongTienPhaiTra.getText());
        HDV.lblTienThua.setText(tienKhachTra + "");
        HDV.lblMaHD.setText(txtMaHD.getText());
        fillHoaDonIn();
        HDV.setVisible(true);
    }
    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        int rowhd = tblHoaDonCho.getSelectedRow();
        String mahd = tblHoaDonCho.getValueAt(rowhd, 0).toString();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(mahd);

        if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Tại quầy")) {
            HoaDonBanHang hdbh = getFormHoaDonBanHang();
            HoaDonBanHangRepository.updateTTHD(hdbh, idhd);
            hoaDonInTQ();
        }

        if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Giao hàng")) {
            HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
            HoaDonBanHangRepository.updateTTDG(hdgiao, idhd);
            hoaDonInGH();
        }

        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);

        fillHDCho();
        fillHDHT();
        fillHDGiao();
        clearHDGH();
        clearHDTQ();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int n[] = tblThongKe.getSelectedRows();
        for (int i = 0; i < n.length; i++) {
            System.out.println(n[i]);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblHoaDonTreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonTreoMouseClicked
        // TODO add your handling code here:
        clearHDTQ();
        tbbTTHD.setSelectedIndex(1);
        listhdbh = HoaDonBanHangRepository.getAllHDGiao();
        int row = tblHoaDonTreo.getSelectedRow();
        ThongTinHoaDonBanHang hd = listhdbh.get(row);
        txtMaHD.setText(hd.getMa());
        txtDiaChiGH.setText(hd.getDiachi());
        txtTenKHGH.setText(hd.getTenkh());
        txtSDTGH.setText(hd.getSdt());
        txtNgaySinhKH.setText(hd.getNgaysinh());
        txtDiaChiGH.setText(hd.getDiachi());
        txtTongTienGH.setText(String.valueOf(hd.getThanhtien()));
        txtGiamGiaGH.setText(String.valueOf(hd.getGiamgiathem()));
        txtPhiShip.setText(String.valueOf(hd.getPhiship()));
        txtTienMatGH.setText(String.valueOf(hd.getTienmat()));
        txtTienCKGH.setText(String.valueOf(hd.getTienkhac()));
        txtNgayTao.setText(hd.getNgaytao());
        txtMaGiaoDichGH.setText(hd.getMagiaodich());
        String mahd = txtMaHD.getText();
        listgh = HoaDonBanHangRepository.getHDCTByMaHD(mahd);
        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listgh.size(); i++) {
            Object[] data = new Object[]{
                listgh.get(i).getMasp(),
                listgh.get(i).getTen(),
                listgh.get(i).getSl(),
                listgh.get(i).getDonGia(),};
            dtm.addRow(data);
        }
    }//GEN-LAST:event_tblHoaDonTreoMouseClicked

    private void txtNgayTimCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNgayTimCaretUpdate
        // TODO add your handling code here:
        listhdbh = HoaDonBanHangRepository.getHoaDonByNgay(txtNgayTim.getText());
        dtm = (DefaultTableModel) tblHoaDonHoanThanh.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
        dtm = (DefaultTableModel) tblHoaDonCho.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
        dtm = (DefaultTableModel) tblHoaDonGiao.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getThanhtien(),};
            dtm.addRow(data);
        }
    }//GEN-LAST:event_txtNgayTimCaretUpdate

    private void txtTimSPCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTimSPCaretUpdate
        // TODO add your handling code here:
        listspbh = _CuaHangService.getAll(txtTimSP.getText());
        dtm = (DefaultTableModel) tblSanPhamBanHang.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listspbh.size(); i++) {
            Object[] data = new Object[]{
                listspbh.get(i).getMa(),
                listspbh.get(i).getTen(),
                listspbh.get(i).getHang(),
                listspbh.get(i).getSize(),
                listspbh.get(i).getMau(),
                listspbh.get(i).getDongia(),
                listspbh.get(i).getSl(),};
            dtm.addRow(data);
        }
    }//GEN-LAST:event_txtTimSPCaretUpdate

    public void fillHDTReo() {
        listhdbh = HoaDonBanHangRepository.getAllHHTreo();
        dtm = (DefaultTableModel) tblHoaDonTreo.getModel();
        dtm.setRowCount(0);
        for (int i = 0; i < listhdbh.size(); i++) {
            Object[] data = new Object[]{
                listhdbh.get(i).getMa(),
                listhdbh.get(i).getTennv(),
                listhdbh.get(i).getTenkh(),
                listhdbh.get(i).getLiDo(),};
            dtm.addRow(data);
        }
    }
    private void popupTreoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupTreoHDActionPerformed
        // TODO add your handling code here:
        int rowhd = tblHoaDonGiao.getSelectedRow();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(tblHoaDonGiao.getValueAt(rowhd, 0).toString());
        String text = JOptionPane.showInputDialog("Lí do treo đơn", "Đơn Treo");

        HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
        HoaDonBanHangRepository.updateTTTReo(hdgiao, text, idhd);
        JOptionPane.showMessageDialog(this, "Hóa đơn chuyển sang trạng thái treo");
        fillHDTReo();
        fillHDGiao();
        fillHDCT();
    }//GEN-LAST:event_popupTreoHDActionPerformed

    private void tblHoaDonGiaoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonGiaoMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            jPopupMenu1.show(this, 370, 130 + (tblHoaDonTreo.getSelectedRow() * 25));
        }
    }//GEN-LAST:event_tblHoaDonGiaoMouseReleased

    private void popupHoanThanhHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupHoanThanhHDActionPerformed
        // TODO add your handling code here:
        int rowhd = tblHoaDonGiao.getSelectedRow();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(tblHoaDonGiao.getValueAt(rowhd, 0).toString());

        HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
        HoaDonBanHangRepository.updateTTHD(hdgiao, idhd);
        JOptionPane.showMessageDialog(this, "Giao hàng thành công");
        fillHDHT();
        fillHDTReo();
        fillHDGiao();
        fillHDCT();
    }//GEN-LAST:event_popupHoanThanhHDActionPerformed

    private void tblHoaDonTreoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonTreoMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            jPopupMenu2.show(this, 370, 130 + (tblHoaDonTreo.getSelectedRow() * 25));
        }
    }//GEN-LAST:event_tblHoaDonTreoMouseReleased

    private void popupHoanThanhHDTreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupHoanThanhHDTreoActionPerformed
        // TODO add your handling code here:
        int rowhd = tblHoaDonTreo.getSelectedRow();
        String mahd = tblHoaDonTreo.getValueAt(rowhd, 0).toString();
        String idhd = HoaDonBanHangRepository.getIDHDByMaHD(mahd);

        if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Tại quầy")) {
            HoaDonBanHang hdbh = getFormHoaDonBanHang();
            HoaDonBanHangRepository.updateTTHD(hdbh, idhd);
        }

        if (HoaDonBanHangRepository.getHinhThucMuaByIdhd(idhd).equals("Giao hàng")) {
            HoaDonBanHang hdgiao = getFormHoaDonBanHangGiao();
            HoaDonBanHangRepository.updateTTHD(hdgiao, idhd);
        }

        dtm = (DefaultTableModel) tblGioHang.getModel();
        dtm.setRowCount(0);
//        HoaDonBanHangRepository.updateTTDG(idhd);

        fillHDCho();
        fillHDHT();
        fillHDGiao();
        fillHDTReo();
        clearHDGH();
        clearHDTQ();
    }//GEN-LAST:event_popupHoanThanhHDTreoActionPerformed

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
//        String icon = tblGiay.getValueAt(row, 8).toString();
//        lblAnh.setIcon(resizeImage(String.valueOf(icon)));
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

    public NhanVienViewModel getFormNV() {
        String ma = txtMa.getText();
        String ten = txtTen.getText();
        String dc = txtDiaChi.getText();
        String em = txtEmail.getText();
        String ns = txtNgaySinh.getText();
        String sdt = txtSDT.getText();
        int tt;
        if (rdoLam.isSelected()) {
            tt = 1;
        } else {
            tt = 0;
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
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLoadNV;
    private javax.swing.JButton btnLuuKH;
    private javax.swing.JButton btnMailDoanhThu;
    private javax.swing.JButton btnMailSanPham;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnResetNV;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSua1;
    private javax.swing.JButton btnSuaNV;
    private javax.swing.JButton btnSuaThuocTinh;
    private javax.swing.JButton btnTaoDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemExcel;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnThemThuocTinh;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTimKiemTheoNam;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoa1;
    private javax.swing.JButton btnXoaAllGH;
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
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
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
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
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
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
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
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblBanHang;
    private javax.swing.JLabel lblDangXuat;
    private javax.swing.JLabel lblDoanhthu;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblGiaoCa;
    private javax.swing.JLabel lblLoi;
    private javax.swing.JLabel lblNhanvien;
    private javax.swing.JLabel lblSanpham;
    private javax.swing.JLabel lblSoGiayHet;
    private javax.swing.JLabel lblSoGiayKd;
    private javax.swing.JLabel lblSoGiaySapHet;
    private javax.swing.JLabel lblThoat;
    private javax.swing.JLabel lblThongKe;
    private javax.swing.JLabel lblTienLai;
    private javax.swing.JLabel lblTongDonHang;
    private javax.swing.JLabel lblTongTienDautu;
    private javax.swing.JLabel lblTongTienThuVe;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlGiamGia;
    private javax.swing.JPanel pnlGiaoHang;
    private javax.swing.JPanel pnlNhanvien;
    private javax.swing.JPanel pnlSanPham;
    private javax.swing.JPanel pnlSoLuong;
    private javax.swing.JPanel pnlTaiQuay;
    private javax.swing.JPanel pnlTheoNam;
    private javax.swing.JPanel pnlTheoNgay;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JPanel pnlTimkiemTong;
    private javax.swing.JPanel pnlTong;
    private javax.swing.JPanel pnlWebCam;
    private javax.swing.JMenuItem popupHoanThanhHD;
    private javax.swing.JMenuItem popupHoanThanhHDTreo;
    private javax.swing.JMenuItem popupTreoHD;
    private javax.swing.JRadioButton rdoChatLieu;
    private javax.swing.JRadioButton rdoHang;
    private javax.swing.JRadioButton rdoLam;
    private javax.swing.JRadioButton rdoMauSac;
    private javax.swing.JRadioButton rdoNghi;
    private javax.swing.JRadioButton rdoSize;
    private javax.swing.JScrollPane scpGiao;
    private javax.swing.JScrollPane scpHoanThanh;
    private javax.swing.JScrollPane scpTao;
    private javax.swing.JScrollPane scpTreo;
    private javax.swing.JTabbedPane tbbTTHD;
    private javax.swing.JTabbedPane tbbTao;
    private javax.swing.JTable tblGiamgia;
    private javax.swing.JTable tblGiay;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblHoaDonGiao;
    private javax.swing.JTable tblHoaDonHoanThanh;
    private javax.swing.JTable tblHoaDonTreo;
    private javax.swing.JTable tblNhanvien;
    private javax.swing.JTable tblSanPhamBanHang;
    private javax.swing.JTable tblThongKe;
    private javax.swing.JTable tblThongKe1;
    private javax.swing.JTable tblThuocTinh;
    private javax.swing.JTextField txtCanTraGH;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiaChiGH;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtGiamGiaGH;
    private javax.swing.JTextField txtKhuyenMaiThemTQ;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtMaBarCode;
    private javax.swing.JTextField txtMaGiaoDichGH;
    private javax.swing.JTextField txtMaGiaoDichTQ;
    private javax.swing.JTextField txtMaGiay;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaThuocTinh;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtNamBaoHanh;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtNgaySinhKH;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNgayTim;
    private javax.swing.JTextField txtPhiShip;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDTGH;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtSoluong;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTenGiay;
    private javax.swing.JTextField txtTenKHGH;
    public static javax.swing.JTextField txtTenNhanVien;
    private javax.swing.JTextField txtTenThuocTinh;
    private javax.swing.JTextField txtTienCKGH;
    private javax.swing.JTextField txtTienCKTQ;
    private javax.swing.JTextField txtTienDuGH;
    private javax.swing.JTextField txtTienDuTQ;
    private javax.swing.JTextField txtTienKhachCanTra;
    private javax.swing.JTextField txtTienMatGH;
    private javax.swing.JTextField txtTienMatTQ;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimSP;
    private javax.swing.JTextField txtTongTienGH;
    private javax.swing.JTextField txtTongTienTQ;
    // End of variables declaration//GEN-END:variables
}
